package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class representing Brachiosaur (a type of Dinosaur)
 */
public class Brachiosaur extends Dinosaur {

    public static int HUNGRY_HIT_POINTS = 50;
    public static int THIRSTY_WATER_LEVEL = 40;
    public static int HEAL_AMOUNT_FROM_EATING = 5;
    public static int HEAL_AMOUNT_FROM_GETTING_FED = 20;
    public static int WELL_FED_HIT_POINTS = 70;
    public static int MATE_TO_HATCH_DURATION = 20;
    public static int HATCH_DURATION = 30;
    public static int GROW_TO_ADULT_DURATION = 50;

    public ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();

    public Brachiosaur(String name, char gender, int turnsUntilAdult) {
        super(name, 'B', 100, 160, gender, turnsUntilAdult, 60, 200);

        if(turnsUntilAdult != 0) {
            this.hitPoints = 10;
        }

		this.behaviours.add(new WanderBehaviour());
		this.behaviours.add(null);
    }

    @Override
    /**
     * Returns a list of allowable actions
     */
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
        Actions list= new Actions();
        list.add(new AttackAction(this));
        list.add(new FeedDinosaurAction(this));
        list.add(new FeedVegKitAction(this));
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
        return this.hitPoints > Brachiosaur.WELL_FED_HIT_POINTS;
    }

    /**
     * Returns true if is hungry, else false
     */
    public boolean isHungry() {
        return this.hitPoints < Brachiosaur.HUNGRY_HIT_POINTS;
    }

    /**
     * Returns true if is thirsty, else false
     */
    public boolean isThirsty() {
        return this.waterLevel < Brachiosaur.THIRSTY_WATER_LEVEL;
    }

    /**
     * Returns hatch duration
     */
    public int getHatchDuration() {
        return Brachiosaur.HATCH_DURATION;
    }

    /**
     * Returns mate to hatch duration
     */
    public int getMateToHatchDuration() {
        return Brachiosaur.MATE_TO_HATCH_DURATION;
    }

    /**
     * Returns grow to adult duration
     */
    public int getGrowToAdultDuration() {
        return Brachiosaur.GROW_TO_ADULT_DURATION;
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
        }

        if(this.isHungry()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting hungry!");
        }

        if(this.isThirsty()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting thisty!");
        }

        int randomNumber = nextID();
        if (map.locationOf(this).getGround() instanceof Bush && randomNumber<50){
            map.locationOf(this).setGround(new Dirt());
        }
    }

    /**
     * Dies
     */
    public void die(GameMap map) {
        Location location = map.actorLocations.locationOf(this);
        map.removeActor(this);
        location.addItem(new BrachiosaurCorpse());
    };

	/**
	 * Figure out what to do next.
	 * 
	 * 
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

            Location nearestFoodSourceLocation = this.getNearestFoodSourceLocation(map);
            Location here = map.actorLocations.locationOf(this);

            if(nearestFoodSourceLocation != null) {
                // Food is at adjacent square 
                if(here.distanceTo(nearestFoodSourceLocation) <= 1) {
                    Action eat = new EatAction(nearestFoodSourceLocation);
                    if (eat != null)
                        return eat;
                // Food is not at adjacent square 
                } else {
                    // Update follow behaviour to follow food
                    this.behaviours.set(1, new FollowBehaviour( nearestFoodSourceLocation ));
                    Action follow = behaviours.get(1).getAction(this, map);
                    if (follow != null)
                        return follow;
                }
            }
        }
        
        // Breed if well fed
        if(this.isWellFed()) {
            Location nearestMateLocation = this.getNearestMateLocation(map);
            if(nearestMateLocation != null) {
                Actor targetMate = map.actorLocations.getActorAt(nearestMateLocation);
                Location here = map.actorLocations.locationOf(this);

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
     * Returns nearest food source
     * Brachiosaur finds nearest tree with fruits
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
                // Finds tree with fruits
                if(destination.getGround() instanceof Tree) {
                    Tree tree = (Tree) destination.getGround();
                    if(tree.hasFruitsInTree()) {
                        if(here.distanceTo(destination) < minDistance) {
                            minDistance = here.distanceTo(destination);
                            there = destination; 
                        }
                    }
                }
            }
        }
        return there;
    }

    public int nextID() {
        Random r = new Random();
        int low = 0;//using literal values is not a good idea, replace them with input parameters
        int high = 100;
        return (r.nextInt(high - low) + low);
    }
}
