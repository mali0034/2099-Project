package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;

import java.util.Random;

public class PickUpFruitFromBushAction extends Action {
    /**
     * item that can be picked up
     */
    protected Item item;
    /**
     * Determines what is displayed on menu
     */
    private String pickedUp="menu";

    /**
     *Constructor
     * @param item  Fruit to be added to invetory
     */

    public PickUpFruitFromBushAction(Item item) {
        this.item = item;
    }
    /**
     * Add the Fruit to the actor's inventory if successful
     *
     * @see Action#execute(Actor, GameMap)
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        int randomNumber = nextID();
        if (randomNumber<60){
            pickedUp="failed";
        }
        else{
            if (map.locationOf(actor).getGround() instanceof Bush){
                if (((Bush) map.locationOf(actor).getGround()).getFruitsInBush().size()>0){
                    actor.addItemToInventory(item);
                    ((Bush) map.locationOf(actor).getGround()).getFruitsInBush().remove(((Bush) map.locationOf(actor).getGround()).getFruitsInBush().size()-1);
                    actor.addEcoPoints(10);
                }
            }
            pickedUp="pickedUp";
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
        if (pickedUp.equals("failed")){
            pickedUp="menu";
            return "You search the bush for fruit, but you can’t find any ripe ones.";
        }
        else if (pickedUp.equals("pickedUp")){
            pickedUp="menu";
            return "You Found Fruit";
        }
        else{
            return actor + " can search through Bush for " + item;
        }
    }

    /**
     * Generates a Random Number
     * @return int value that is randomly generated
     */
    public int nextID() {
        Random r = new Random();
        int low = 0;//using literal values is not a good idea, replace them with input parameters
        int high = 100;
        return (r.nextInt(high - low) + low);
    }
}
