package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;


/**
 * Represents an Allosaur in game
 */
public class Allosaur extends Dinosaur {

    /**
     * Static variables for Allosaur
     */
    public static int HUNGRY_HIT_POINTS = 60;
    public static int THIRSTY_WATER_LEVEL = 40;
    public static int WELL_FED_HIT_POINTS = 50;
    public static int MATE_TO_HATCH_DURATION = 20;
    public static int HATCH_DURATION = 50;
    public static int GROW_TO_ADULT_DURATION = 50;

    public ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();

    /**
     * Constructor for Allosaur
     * @param String name of Allosaur
     * @param char gender Gender of Allosaur
     * @param int turnsUntilAdult turns until Allosaur turns into adult
     */
    public Allosaur(String name, char gender, int turnsUntilAdult) {
        super(name, 'A', 100, 100, gender, turnsUntilAdult, 60, 100);

        if(turnsUntilAdult != 0) {
            this.hitPoints = 20;
        }

		this.behaviours.add(new WanderBehaviour());
        this.behaviours.add(null);
    }

    @Override
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions list= new Actions();
        list.add(new AttackAction(this));
        list.add(new FeedCarnKitAction(this));
        return list;
	}
    
    /**
     * Returns heal amount from eating
     */
    public int getHealAmountFromEating() {
        return Brachiosaur.HEAL_AMOUNT_FROM_EATING;
    }

    /**
     * Returns true if is well fed, else false
     */
    public boolean isWellFed() {
        return this.hitPoints > Allosaur.WELL_FED_HIT_POINTS;
    }

    /**
     * Returns true if is hungry, else false
     */
    public boolean isHungry() {
        return this.hitPoints < Allosaur.HUNGRY_HIT_POINTS;
    }

    /**
     * Returns true if is thirsty, else false
     */
    public boolean isThirsty() {
        return this.waterLevel < Allosaur.THIRSTY_WATER_LEVEL;
    }

    /**
     * Returns hatch duration
     */
    public int getHatchDuration() {
        return Allosaur.HATCH_DURATION;
    }

    /**
     * Returns mate to hatch duration
     */
    public int getMateToHatchDuration() {
        return Allosaur.MATE_TO_HATCH_DURATION;
    }

    /**
     * Returns grow to adult duration
     */
    public int getGrowToAdultDuration() {
        return Allosaur.GROW_TO_ADULT_DURATION;
    }

    @Override
    /**
     * Update state every turn
     */
    public void tick(GameMap map, Display display) {
        this.hurt(1);
        this.getThirsty(1);

        Location here = map.actorLocations.locationOf(this);

        
        if(this.isUnconscious()) {
            // Unconscious due to thirst
            if(map.isRaining() && this.waterLevel <= 0) {
                this.getMoist(10);
            }
            this.turnsAfterUnconscious += 1;
        }

        if(this.isPregnant()) {
            this.turnsUntilLayEgg -= 1;
        }
        
        // Dies after 20 turns of unconsiousness
        if(this.turnsAfterUnconscious >= 15) {
            this.die(map);
            return;
        }

        if(this.isHungry()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting hungry!");
        }

        if(this.isThirsty()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting thisty!");
        }
    }

	/**
	 * Figure out what to do next.
	 * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        // Lay egg if ready to lay
        if(this.canLayEgg()) {
            Action layEgg = new LayEggAction();
            if (layEgg != null)
                return layEgg;
        }
        
        // Drink water/follow water source
        if(this.isThirsty()) {
            Location here = map.actorLocations.locationOf(this);
            Location nearestWaterSource = this.getNearestWaterSource(map);

            if(nearestWaterSource != null) {
                // Food is at adjacent square 
                if(here.distanceTo(nearestWaterSource) <= 1) {
                    Action drink = new DrinkAction(nearestWaterSource);
                    if (drink != null)
                        return drink;
                    
                // Food not at adjacent square
                } else {
                    // Update follow behaviour to follow food
                    this.behaviours.set(1, new FollowBehaviour( nearestWaterSource ));
                    Action follow = behaviours.get(1).getAction(this, map);
                    if (follow != null)
                        return follow;
                }
            }
        }

        // Follow nearest food source if hungry
        if(this.isHungry()) {

            Location nearestFoodLocation = this.getNearestFoodSourceLocation(map);
            Location here = map.actorLocations.locationOf(this);

            if(nearestFoodLocation != null) {
                if(here.distanceTo(nearestFoodLocation) <= 1) {
                    EatAction eat = new EatAction( map.actorLocations.getActorAt(nearestFoodLocation), nearestFoodLocation );
                    if(eat != null)
                        return eat;
                } else {
                    // Update follow behaviour to follow food
                    this.behaviours.set(1, new FollowBehaviour( nearestFoodLocation ));
                    Action follow = behaviours.get(1).getAction(this, map);
                    if (follow != null)
                        return follow;
                }
            }
        } 
        
        // Breed if well fed
        if(this.isWellFed()) {    
            Location nearestMateLocation = this.getNearestMateLocation(map);
            Actor targetMate = map.actorLocations.getActorAt(nearestMateLocation);
            Location here = map.actorLocations.locationOf(this);

            if(nearestMateLocation != null) {
                // Mate is at adjacent square
                if(here.distanceTo(nearestMateLocation) <= 1) {
                    Action mate = new MateAction( targetMate );
                    if (mate != null)
                        return mate;
                // Mate not at adjacent square
                } else {
                    this.behaviours.set(1, new FollowBehaviour( targetMate ));
                    Action follow = behaviours.get(1).getAction(this, map);
                    if (follow != null)
                        return follow;
                }
            }
        }
        
        // Wander around
        Action wander = this.behaviours.get(0).getAction(this, map);
        if (wander != null)
            return wander;
        
		return new DoNothingAction();
	}
    
    /**
     * Dies
     */
    public void die(GameMap map) {
        Location location = map.actorLocations.locationOf(this);
        map.removeActor(this);
        location.addItem(new AllosaurCorpse());
    };
    /**
     * Returns nearest food source
     * @return
     */
    public Location getNearestFoodSourceLocation(GameMap map) {
        float minDistance = Float.MAX_VALUE;
        Location here = map.locationOf(this);
        Location there = null;

        // Loop through all adjacent squares
        for(int y : map.getYRange()) {
            for(int x : map.getXRange()) {
                Location destination = map.at(x, y);
                Actor targetActor = map.actorLocations.getActorAt(destination);
                
                if(here.distanceTo(destination) < minDistance) {
                    // Feed on Actors
                    if(targetActor != this) {
                        // Can eat vulnerable/dead stegosaur        
                        if(targetActor instanceof Stegosaur) {
                            Stegosaur stegosaur = (Stegosaur) targetActor;
                            if(stegosaur.vulnerableToAttacks()) {
                                minDistance = here.distanceTo(destination);
                                there = destination;
                            }
                        // Can eat dead brachiosaur    
                        } else if (targetActor instanceof Brachiosaur) {
                            Brachiosaur brachiosaur = (Brachiosaur) targetActor;
                            minDistance = here.distanceTo(destination);
                            there = destination;
                        } else if (targetActor instanceof Allosaur) {
                            Allosaur allosaur = (Allosaur) targetActor;
                            minDistance = here.distanceTo(destination);
                            there = destination; 
                        // Can eat walking Pterodactyl
                        } else if (targetActor instanceof Pterodactyl) {
                            Pterodactyl pterodactyl = (Pterodactyl) targetActor;
                            if(pterodactyl.isVulnerable(map)) {
                                minDistance = here.distanceTo(destination);
                                there = destination; 
                            }
                        }
                    }
                    // Feed on Items (corpse, egg)
                    for(Item item: destination.getItems()) {
                        if(item instanceof StegosaurCorpse || item instanceof BrachiosaurCorpse || item instanceof AllosaurCorpse || item instanceof PterodactylCorpse || item instanceof Egg) {
                            minDistance = here.distanceTo(destination);
                            there = destination;
                        }
                    }
                }
            }
        }
        return there;
    }
}
