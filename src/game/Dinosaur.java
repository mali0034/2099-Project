package game;

import java.util.ArrayList; 
import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;

public abstract class Dinosaur extends Actor {
    public ArrayList<Behaviour> behaviours; // 0 refers to wander behaviour, 1 refers to follow behaviour

    public int turnsAfterUnconscious;
    public int waterLevel;
    public int maxWaterLevel;
    public char gender;
    public int turnsUntilLayEgg = -1;
    public int turnsUntilAdult;
    public int turnsUntilDecompose = -1;

    /**
     * Constructor
     */
    public Dinosaur(String name, char displayChar, int initHitPoints, int maxHitPoints, char gender, int turnsUntilAdult, int waterLevel, int maxWaterLevel) {
        super(name, displayChar, initHitPoints);

        this.maxHitPoints = maxHitPoints;
        this.gender = gender;
        this.turnsUntilAdult = turnsUntilAdult;
        this.waterLevel = waterLevel;
        this.maxWaterLevel = maxWaterLevel;
    }

    public boolean isUnconscious() {
        return this.hitPoints <= 0 || this.waterLevel <= 0;
    }

	@Override
	public abstract Actions getAllowableActions(Actor otherActor, String direction, GameMap map);
    
    
    @Override
    /**
     * Update state of Dinosaur after every turn
     * EXAMPLE: turnsAfterUnconscious, turnsUntilHatch
     * Display status of Dinosaur too (if any)
     */
    public abstract void tick(GameMap map, Display display);

    @Override
    /**
     * Figure out what to do next
     * @see edu.monash.fit2099.engine.Actor#playTurn(Actions, Action, GameMap, Display)
     */
    public abstract Action playTurn(Actions actions, Action lastAction, GameMap map, Display display);


    /**
     * Returns true if pregnant, else false
     */
    public boolean isPregnant() {
        return this.turnsUntilLayEgg >= 0; 
    }

    /**
     * Set turns until hatch
     * @param turnsUntilHatch
     */
    public void setTurnsUntilLayEgg(int turnsUntilLayEgg) {
        this.turnsUntilLayEgg = turnsUntilLayEgg;
    }

    /**
     * Returns nearest food source
     */
    public abstract Location getNearestFoodSourceLocation(GameMap map);
   
    /**
     * Returns true if can hatch egg
     */
    public boolean isFemale() {
        return this.gender == 'f';
    }

    /**
     * Returns true if opposite sex
     * @return
     */
    public boolean isOppositeSex(Dinosaur otherDinosaur) {
        return this.gender != otherDinosaur.gender;
    }

    /**
     * Returns true if can hatch egg
     */
    public boolean canLayEgg() {
        return this.isAdult() && this.isFemale() && this.turnsUntilLayEgg == 0;
    }

    /**
     * Returns true if is adult
     * @return
     */
    public boolean isAdult() {
        return this.turnsUntilAdult == 0;
    }

    /**
     * Returns true if dinosaur is dead, else false
     */
    public boolean isDead() {
        return this.turnsUntilDecompose >= 0;
    }
    
    /**
     * Remove dinosaur from system 
     */
    public void decompose(GameMap map) {
        map.removeActor(this);
    }

    /**
     * Increase water level
     * @param waterLevel water level to get moist by
     */
    public void getMoist(int waterLevel) {
        this.waterLevel += waterLevel;
		this.waterLevel = Math.min(this.waterLevel, this.maxWaterLevel);
    }

    /**
     * Decrease water level
     * @param waterLevel Water level to get thirsty by
     */
    public void getThirsty(int waterLevel) {
        this.waterLevel -= waterLevel;
    }

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
        // Female
        } else {
            if(!this.isPregnant()) {
                // Iterate through all actors of the map
                for (Actor actor : map.actorLocations) {
                    if(actor instanceof Dinosaur && this.getClass().equals(actor.getClass())) {
                        Dinosaur targetDinosaur = (Dinosaur) actor;
                        // Male
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
        return there;
    }

    /**
     * Returns nearest water source location (lake)
     */
    public Location getNearestWaterSource(GameMap map) {
        float minDistance = Float.MAX_VALUE;
        Location here = map.locationOf(this);
        Location there = null;

        // Loop through all adjacent squares
        for(int y : map.getYRange()) {
            for(int x : map.getXRange()) {
                Location destination = map.at(x, y);
                // Check for nearest lake
                if(destination.getGround() instanceof Lake) {
                    Lake lake = (Lake) destination.getGround();
                    if(here.distanceTo(destination) < minDistance) {
                            minDistance = here.distanceTo(destination);
                            there = destination;
                    }
                }
            }
        }
        return there;
    }
}
