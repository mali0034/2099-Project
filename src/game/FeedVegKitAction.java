package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

public class FeedVegKitAction extends Action {
    /**
     * The dinosaur that will be fed
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
    public FeedVegKitAction(Actor target) {
        this.target = target;
    }

    /**
     * Feeds Dinosaur Vegetarian Meal Kit
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        boolean vegetarianKitPresence=false;
        for (Item item : actor.getInventory()){
            if (item instanceof VegetarianMealKit) {
                vegetarianKitPresence = true;
                break;
            }
        }

        if (vegetarianKitPresence){
            if (target instanceof Stegosaur){
                int healAmount = target.getMaxHitPoints()-target.getHitPoints();
                ((Stegosaur) target).heal(healAmount);
                for (Item item : actor.getInventory()) {
                    if (item instanceof VegetarianMealKit){
                        actor.removeItemFromInventory(item);
                        break;
                    }
                }
                fed="fed";
            }
            else if (target instanceof Brachiosaur){
                int healAmount = target.getMaxHitPoints()-target.getHitPoints();
                ((Brachiosaur) target).heal(healAmount);
                for (Item item : actor.getInventory()) {
                    if (item instanceof VegetarianMealKit){
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
            return actor+" does not have Vegetarian Meal Kit to feed "+target;
        }
        else{
            return actor+" can feed "+target + " Vegetarian Meal Kit";
        }
    }
}
