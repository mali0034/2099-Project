package game;

import edu.monash.fit2099.engine.*;

/**
 * A class that figures out a MoveAction that will move the actor one step 
 * closer to a target Actor.
 */
public class FollowBehaviour implements Behaviour {

	private Actor target;
    private Location destination = null;

	/**
	 * Constructor.
	 * 
	 * @param subject the Actor to follow
	 */
	public FollowBehaviour(Actor subject) {
		this.target = subject;
	}

	/**
	 * Constructor.
	 * 
	 * @param subject the Actor to follow
	 */
	public FollowBehaviour(Location location) {
		this.destination = location;
	}

	@Override
	public Action getAction(Actor actor, GameMap map) {
        // Returns null if destination is not in map, or target/actor is not in the same map
        if(this.destination == null) {
            if(!map.contains(target) || !map.contains(actor)) {
                return null;
            }
        } else {
            if(this.destination.map() != map) {
                return null;
            }
        }

        Location there;
    
        if(this.destination != null ) {
            there = this.destination;
        } else {
            there = map.locationOf(target);
        }
        
        Location here = map.locationOf(actor);

        int currentDistance = distance(here, there);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.canActorEnter(actor)) {
                int newDistance = distance(destination, there);
                if (newDistance < currentDistance) {
                    return new MoveActorAction(destination, exit.getName());
                }
            }
        }
        
		return null;
	}

	/**
	 * Compute the Manhattan distance between two locations.
	 * 
	 * @param a the first location
	 * @param b the first location
	 * @return the number of steps between a and b if you only move in the four cardinal directions.
	 */
	private int distance(Location a, Location b) {
		return Math.abs(a.x() - b.x()) + Math.abs(a.y() - b.y());
	}
}