package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

import java.util.Random;

public class FeedDinosaurAction extends Action {
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
    public FeedDinosaurAction(Actor target) {
        this.target = target;
    }


    /**
     * Feeds a Dinosaur a Fruit
     *
     * @see Action#execute(Actor, GameMap)
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        boolean fruitPresence=false;
        for (Item item : actor.getInventory()){
            if (item instanceof Fruit) {
                fruitPresence = true;
                break;
            }
        }
        if (fruitPresence){
            if (target instanceof Stegosaur){
                ((Stegosaur) target).heal(((Stegosaur) target).HEAL_AMOUNT_FROM_GETTING_FED);
                for (Item item : actor.getInventory()) {
                    if (item instanceof Fruit){
                        actor.removeItemFromInventory(item);
                        break;
                    }
                }
                actor.addEcoPoints(10);
                fed="fed";
            }
            else if (target instanceof Brachiosaur){
                ((Brachiosaur) target).heal(((Brachiosaur) target).HEAL_AMOUNT_FROM_GETTING_FED);
                for (Item item : actor.getInventory()) {
                    if (item instanceof Fruit){
                        actor.removeItemFromInventory(item);
                        break;
                    }
                }
                fed="fed";
                actor.addEcoPoints(10);
            }

        }
        else{
            fed="noFruit";
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
        else if(fed.equals("noFruit")){
            fed="menu";
            return actor+" does not have sufficient fruit to feed "+target;
        }
        else{
            return actor+" can feed "+target;
        }
    }
}
