package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

public class FeedCarnKitAction extends Action {
    /**
     * The dinosaur that is getting fed
     */
    private Actor target;
    /**
     * Determines what is displayed on menu
     */
    private String fed="menu";

    /**
     * Constructor
     * @param target
     */
    public FeedCarnKitAction(Actor target) {
        this.target = target;
    }

    /**
     * Feeds a Dinosaur a Carnivore Meal Kit
     *
     * @see Action#execute(Actor, GameMap)
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        boolean carnivoreKitPresence=false;
        for (Item item : actor.getInventory()){
            if (item instanceof CarnivoreMealKit) {
                carnivoreKitPresence = true;
                break;
            }
        }


        if (carnivoreKitPresence){
            if (target instanceof Allosaur){
                int healAmount = target.getMaxHitPoints()-target.getHitPoints();
                ((Allosaur) target).heal(healAmount);
                for (Item item : actor.getInventory()) {
                    if (item instanceof CarnivoreMealKit){
                        actor.removeItemFromInventory(item);
                        break;
                    }
                }
                fed="fed";
            }
        }
        else{
            fed="noKit";
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
        if (fed.equals("fed")){
            fed="menu";
            return actor + " feeds " + target;
        }
        else if(fed.equals("noKit")){
            fed="menu";
            return actor+" does not have Carnivore Meal Kit to feed "+target;
        }
        else{
            return actor+" can feed "+target + " Carnivore Meal Kit";
        }
    }
}
