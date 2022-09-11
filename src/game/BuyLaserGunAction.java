package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

public class BuyLaserGunAction extends Action {
    /**
     * Determines what is displayed on menu
     */
    private String itemStatus="menu";

    /**
     *Constructor
     */
    public BuyLaserGunAction() {
    }
    /**
     * Add the item to the actor's inventory.
     *
     * @see Action#execute(Actor, GameMap)
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor.getEcoPoints()>=500){
            itemStatus="bought";
            actor.addItemToInventory(new LaserGun());
            actor.removeEcoPoints(500);
        }
        else{
            itemStatus="failed";
        }
        return menuDescription(actor);
    }

    /**
     * Describe the action in a format suitable for displaying in the menu.
     *
     * @see Action#menuDescription(Actor)
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player picks up the rock"
     */
    @Override
    public String menuDescription(Actor actor) {
        if (itemStatus.equals("bought")){
            itemStatus="menu";
            return "You have purchased 1 LaserGun";
        }
        else if (itemStatus.equals("failed")){
            itemStatus="menu";
            return "Insufficient Funds";
        }
        else{
            return actor + " can buy LaserGun from VendingMachine";
        }
    }
}
