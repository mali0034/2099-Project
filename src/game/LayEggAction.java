package game;

import java.util.Random;
import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;

/**
 * Special Action for attacking other Actors.
 */
public class LayEggAction extends Action {

    /**
	 * Random number generator
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 *
	 */
	public LayEggAction() {
	}


	@Override
    /**
     * Create child of random gender
     * @param actor
     * @param map
     * @return
     */
	public String execute(Actor actor, GameMap map) {

        Egg egg = null;
        // Get parent current location
        Location current = map.locationOf(actor);
        if(actor instanceof Stegosaur) {
            egg = new StegosaurEgg();
        } else if(actor instanceof Brachiosaur) {
            egg = new BrachiosaurEgg();
        } else if(actor instanceof Allosaur) {
            egg = new AllosaurEgg();
        } else if(actor instanceof Pterodactyl) {
            egg = new PterodactylEgg();
        }

        if(egg == null) {
            return null;
        }
        
        // Loop through all adjacent squares to hatch egg
        int y = -1;
        while(y<=1) {
            int x = -1;
            while(x<=1) {
                try {
                    Location targetLocation = map.at(current.x() + x, current.y() + y);
                    targetLocation.addItem((Item) egg);
                    // Reset actor turnsUntilHatch
                    Dinosaur dinosaur = (Dinosaur) actor;
                    dinosaur.setTurnsUntilLayEgg(-1);
                    return actor + " laid egg.";
                } catch (Exception e) {
                    x++;
                }
            }                        
            y++;
        }
        return actor + " cannot find a place to lay egg.";
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " laid egg.";
	}
}
