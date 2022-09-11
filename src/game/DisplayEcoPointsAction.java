package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class DisplayEcoPointsAction extends Action {
    /**
     * Amount of eco points player has
     */
    private int ecoPoints;
    /**
     * Determines what is displayed on menu
     */
    private String actionSelected="menu";

    /**
     * Constructor
     * @param inputEcoPoints Amount of Eco points Player has
     */
    public DisplayEcoPointsAction(int inputEcoPoints) {
        ecoPoints=inputEcoPoints;
    }

    /**
     * Displays the Eco Points Player Has
     *
     * @see Action#execute(Actor, GameMap)
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a suitable description to display in the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        actionSelected="selected";
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
        if (actionSelected.equals("selected")){
            actionSelected="menu";
            return actor + " has "+ ecoPoints+" EcoPoints";
        }
        else{
            return "Display EcoPoints of "+actor;
        }
    }

    /**
     * The display Key in the menu
     * @return String containing display key
     */
    @Override
    public String hotkey() {
        return "y";
    }

}
