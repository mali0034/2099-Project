package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

public class BuyBrachiosaurEggAction extends Action {
    /**
     * Determines what should be displayed on menu
     */
    private String itemStatus="menu";

    /**
     *Constructor
     */
    public BuyBrachiosaurEggAction() {
    }
    /**
     * Sell Brachiosaur Egg to Actor.
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
            actor.addItemToInventory(new BrachiosaurEgg());
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
            return "You have purchased 1 Brachiosaur Egg";
        }
        else if (itemStatus.equals("failed")){
            itemStatus="menu";
            return "Insufficient Funds";
        }
        else{
            return actor + " can buy Brachiosaur Egg from VendingMachine";
        }
    }
}
