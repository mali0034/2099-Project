package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;

import java.util.ArrayList;
import java.util.Random;

public class Pterodactyl extends Dinosaur {
    public static int HUNGRY_HIT_POINTS = 20;
    public static int WELL_FED_HIT_POINTS = 50;
    public static int THIRSTY_WATER_LEVEL = 40;
    public static int MAX_FLYING_STAMINA = 30;
    public static int HATCH_DURATION = 5;

    public int flyingStaminaLeftInTurns;
    
    public ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();

    /**
     * Constructor
     * @param name
     * @param gender
     * @param turnsUntilAdult
     */
    public Pterodactyl(String name, char gender, int turnsUntilAdult) {
        super(name, 'P', 100, 160, gender, turnsUntilAdult, 60, 200);

        if(turnsUntilAdult != 0) {
            this.hitPoints = 10;
        }

        this.flyingStaminaLeftInTurns = 2;

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

    @Override
    /**
     * Update state every turn
     */
    public void tick(GameMap map, Display display) {
        this.hurt(1);
        this.getThirsty(1);
        this.flyingStaminaLeftInTurns -= 1;

        Location here = map.locationOf(this);
        
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

        // Remove fishes from lake
        if(map.locationOf(this).getGround() instanceof Lake) {
            Random rand = new Random();
            Lake lake = (Lake) map.locationOf(this).getGround();
            
            int maxFish = Math.min(lake.getFish(), 3);
            int noOfFish = rand.nextInt(maxFish);
            
            lake.removeFishes(noOfFish);

            display.println(this + " caught " + noOfFish + " fishes from lake at (" + here.x() + "," + here.y() + ")");
            
            // Heal by 30 water points
            this.getMoist(30);
            display.println(this + ": water points +30");
        }

        if(here.getGround() instanceof Tree) {
            display.println(this + " flying stamina is reset.");
            this.resetFlyingStamina();
        }
    }

	/**
	 * Figure out what to do next.
	 * 
	 * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
        Location here = map.locationOf(this);

        // Follow tree
        if(this.flyingStaminaLeftInTurns == 0) {
            Location nearestTreeLocation = this.getNearestTreeLocation(map);
            // Update follow behaviour to follow tree
            this.behaviours.set(1, new FollowBehaviour( nearestTreeLocation ));
            Action follow = behaviours.get(1).getAction(this, map);
            if(follow != null)
                return follow;
        }

        // Lay egg if ready to lay (must be on a tree to lay egg)
        if(this.canLayEgg() && map.actorLocations.locationOf(this).getGround() instanceof Tree) {
            Action layEgg = new LayEggAction();
            if (layEgg != null)
                return layEgg;
        }
        
        // Drink water/follow water source
        if(this.isThirsty()) {
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
        
        // Mate if well fed
        if(this.isWellFed()) {
            Location nearestMateLocation = this.getNearestMateLocation(map);
            if(nearestMateLocation != null) {
                Actor targetMate = map.actorLocations.getActorAt(nearestMateLocation);

                // Mate is at adjacent square
                // Must be on tree to mate
                if(here.distanceTo(nearestMateLocation) <= 1 && here.getGround() instanceof Tree) {
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
     * Returns true if is well fed, else false
     */
    public boolean isWellFed() {
        return this.hitPoints > Pterodactyl.WELL_FED_HIT_POINTS;
    }
    
    /**
     * Returns true if is hungry, else false
     */
    public boolean isHungry() {
        return this.hitPoints < Pterodactyl.HUNGRY_HIT_POINTS;
    }

    /**
     * Returns true if is thirsty, else false
     */
    public boolean isThirsty() {
        return this.waterLevel < Pterodactyl.THIRSTY_WATER_LEVEL;
    }

    /**
     * Returns true if is thirsty, else false
     */
    public int getHatchDuration() {
        return Pterodactyl.HATCH_DURATION;
    }

    /**
     * Reset flying stamina
     */
    public void resetFlyingStamina() {
        this.flyingStaminaLeftInTurns = Pterodactyl.MAX_FLYING_STAMINA;
    }

    /**
     * Returns true if can be attacked, else false
     */
    public boolean isVulnerable(GameMap map) {
        if(map.locationOf(this).getGround() instanceof Tree) {
            return false;
        }
        return !this.isFlying();
    }

    /**
     * Returns true if is flying, else false
     */
    public boolean isFlying() {
        return this.flyingStaminaLeftInTurns > 0;
    }

    /**
     * Dies
     */
    public void die(GameMap map) {
        Location location = map.actorLocations.locationOf(this);
        map.removeActor(this);
        location.addItem(new PterodactylCorpse());
    };
    
    public int nextID() {
        Random r = new Random();
        int low = 0;//using literal values is not a good idea, replace them with input parameters
        int high = 100;
        return (r.nextInt(high - low) + low);
    }

    
    /**
     * Returns nearest food source
     * Pterodactyl can eat from lake
     * @return
     */
    public Location getNearestTreeLocation(GameMap map) {
        float minDistance = Float.MAX_VALUE;
        Location here = map.locationOf(this);
        Location there = null;

        // Loop through all adjacent squares
        for(int y : map.getYRange()) {
            for(int x : map.getXRange()) {
                Location destination = map.at(x, y);
                if(destination.getGround() instanceof Tree) {
                    if(here.distanceTo(destination) < minDistance) {
                        minDistance = here.distanceTo(destination);
                        there = destination;
                    }
                }
            }
        }
        return there;
    }
    
    /**
     * Returns nearest food source
     * Pterodactyl can eat from lake
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
                // Check for nearest corpse
                for(Item item : destination.getItems()) {
                    if(item instanceof AllosaurCorpse || item instanceof BrachiosaurCorpse || item instanceof StegosaurCorpse || item instanceof PterodactylCorpse) {
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

    @Override
    /**
     * Returns nearest mate
     * @return
     */
    public Location getNearestMateLocation(GameMap map) {
        // Get mate with nearest distance
        Location here = map.locationOf(this);
        float minDistance = Float.MAX_VALUE;
        Location there = null;

        // Male
        if(!this.isFemale()) {
            // Iterate through all actors of the map
            for (Actor actor : map.actorLocations) {
                // Dinosaur and is of same breed
                if(actor instanceof Dinosaur && this.getClass().equals(actor.getClass())) {
                    Dinosaur targetDinosaur = (Dinosaur) actor;
                    // Must be on tree
                    if(map.locationOf(actor).getGround() instanceof Tree) {
                        // Is female and not pregnant
                        if(targetDinosaur.isFemale() && !targetDinosaur.isPregnant()) {
                            Location destination = map.actorLocations.locationOf(targetDinosaur);
                            float distance = here.distanceTo(destination);
                            
                            if(distance < minDistance) {
                                minDistance = distance;
                                there = destination;
                            }
                        }
                    }
                }
            }
        // Female
        } else {
            if(!this.isPregnant()) {
                // Iterate through all actors of the map
                for (Actor actor : map.actorLocations) {
                    if(actor instanceof Dinosaur && this.getClass().equals(actor.getClass())) {
                        Dinosaur targetDinosaur = (Dinosaur) actor;
                        // Must be on tree
                        if(map.locationOf(actor).getGround() instanceof Tree) {
                            if(!targetDinosaur.isFemale()) {
                                Location destination = map.actorLocations.locationOf(targetDinosaur);
                                float distance = here.distanceTo(destination);
                
                                if(distance < minDistance) {
                                    minDistance = distance;
                                    there = destination;
                                }
                            }
                        }
                    }
                }
            }
        }
        return there;
    }
}
