package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

/**
 * A herbivorous dinosaur.
 */
public class Stegosaur extends Dinosaur {

    /**
     * Static variables
     */
    public static int HUNGRY_HIT_POINTS = 90;
    public static int THIRSTY_WATER_LEVEL = 40;
    public static int HEAL_AMOUNT_FROM_EATING = 10;
    public static int HEAL_AMOUNT_FROM_GETTING_FED = 20;
    public static int WELL_FED_HIT_POINTS = 50;
    public static int HATCH_DURATION = 5;
    public static int GROW_TO_ADULT_DURATION = 5;
    public static int MATE_TO_HATCH_DURATION = 20;
    public static int HURT_AMOUNT_FROM_BEING_ATTACKED = 20;
    public static int TURNS_UNTIL_VULNERABLE_TO_ATTACK = 20;
    
    private int turnsUntilVulnerableToAttack = 0;

    public ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();

	/** 
	 * Constructor.
	 * All Stegosaurs are represented by a 'd' and have 100 hit points.
	 * 
	 * @param name the name of this Stegosaur
	 */
	public Stegosaur(String name, char gender, int turnsUntilAdult) {
		super(name, 'S', 50, 100, gender, turnsUntilAdult, 60, 100);

        if(turnsUntilAdult != 0) {
            this.hitPoints = 10;
        }

		this.behaviours.add(new WanderBehaviour());
        this.behaviours.add(null);
	}

	@Override
	public Actions getAllowableActions(Actor otherActor, String direction, GameMap map) {
	    Actions list= new Actions();
	    list.add(new AttackAction(this));
	    list.add(new FeedDinosaurAction(this));
	    list.add(new FeedVegKitAction(this));
		return list;
	}

    /**
     * Returns true if is vulnerable to attacks
     */
    public boolean vulnerableToAttacks() {
        return this.turnsUntilVulnerableToAttack == 0;
    }

    /**
     * Returns heal amount from eating
     */
    public int getHealAmountFromEating() {
        return Stegosaur.HEAL_AMOUNT_FROM_EATING;
    }

    /**
     * Returns true if is well fed, else false
     */
    public boolean isWellFed() {
        return this.hitPoints > Stegosaur.WELL_FED_HIT_POINTS;
    }

    /**
     * Returns true if is hungry, else false
     */
    public boolean isHungry() {
        return this.hitPoints < Stegosaur.HUNGRY_HIT_POINTS;
    }

    /**
     * Returns true if is thirsty, else false
     */
    public boolean isThirsty() {
        return this.waterLevel < Stegosaur.THIRSTY_WATER_LEVEL;
    }

    /**
     * Returns hatch duration
     */
    public int getHatchDuration() {
        return Stegosaur.HATCH_DURATION;
    }

    /**
     * Returns mate to hatch duration
     */
    public int getMateToHatchDuration() {
        return Stegosaur.MATE_TO_HATCH_DURATION;
    }

    /**
     * Returns grow to adult duration
     */
    public int getGrowToAdultDuration() {
        return Stegosaur.GROW_TO_ADULT_DURATION;
    }

    /**
     * Returns turns until vulnerable attack
     */
    public int getTurnsUntilVulnerableToAttack() {
        return Stegosaur.TURNS_UNTIL_VULNERABLE_TO_ATTACK;
    }

    /**
     * Returns hurt amount from being attacked
     */
    public int getHurtAmountFromBeingAttacked() {
        return Stegosaur.HURT_AMOUNT_FROM_BEING_ATTACKED;
    }

    /**
     * Set turns until vulnerable to attack
     */
    public void setTurnsUntilVulnerableToAttack(int turnsUntilVulnerableToAttack) {
        this.turnsUntilVulnerableToAttack = turnsUntilVulnerableToAttack;
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

        // Display hungry Stegosaur message
        if(this.isHungry()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting hungry!");
        }

        // Display thirsty Stegosaur message
        if(this.isThirsty()) {
            display.println(this + " at (" + here.x() + "," + here.y() + ") is getting thisty!");
        }

        // Dies after 20 turns of unconsiousness
        if(this.turnsAfterUnconscious >= 20) {
            this.die(map);
        }
    }

	/**
	 * Figure out what to do next.
	 * 
	 * 
	 * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {

        if(this.isUnconscious()) {
            display.println(this + " is unconscious.");
            return new DoNothingAction();
        }

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
                // Food is at adjacent square 
                if(here.distanceTo(nearestFoodLocation) <= 1) {
                    Action eat = new EatAction(nearestFoodLocation);
                    if (eat != null)
                        return eat;
                    
                // Food not at adjacent square
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
        location.addItem(new StegosaurCorpse());
    };

    /**
     * Returns nearest food source
     * Stegosaur can eat from bush or ground
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
                for(Item item : destination.getItems()) {
                    if(item instanceof Fruit) {
                        if(here.distanceTo(destination) < minDistance) {
                            minDistance = here.distanceTo(destination);
                            there = destination;
                        } 
                    }
                }
                // Check for nearest bush with fruits
                if(destination.getGround() instanceof Bush) {
                    Bush bush = (Bush) destination.getGround();
                    if(bush.hasFruit()) {
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
}
