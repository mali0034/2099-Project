package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;

import java.util.ArrayList;


/**
 * Special Action for attacking other Actors.
 */
public class DrinkAction extends Action {
	protected Location location;

    /**
     *  Multiple constructors for different types of Dinosaurs with different food types
     * /

     /**
     *
     * @param location
     */
    public DrinkAction(Location location) {
		this.location = location;
	}

	@Override
    /**
     * Dinosaur drinks from lake
     */
	public String execute(Actor actor, GameMap map) {
        Dinosaur dinosaur = (Dinosaur) actor;
        Lake lake = (Lake) this.location.getGround();
        if(dinosaur instanceof Brachiosaur) {
            if (lake.getSips()>0){
                dinosaur.getMoist(80);
                lake.waterDrank();
                return dinosaur + " drinks from lake at (" + this.location.x() + "," + this.location.y() + "), water level +80";
            }
        } else {
            if (lake.getSips()>0){
                dinosaur.getMoist(30);
                lake.waterDrank();
                return dinosaur + " drinks from lake at (" + this.location.x() + "," + this.location.y() + "), water level +30";
            }

        }
        return "Lake at ("+ this.location.x() + "," + this.location.y() +") was dry";
    }

	@Override
	public String menuDescription(Actor actor) {
		return actor + " drinks from lake.";
	}
}
