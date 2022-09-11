package edu.monash.fit2099.engine;

import game.Bush;

/**
 * Action to allow items to be picked up.
 */
public class PickUpItemAction extends Action {

	protected Item item;
	private String pickUp="menu";

	/**
	 * Constructor.
	 *
	 * @param item the item to pick up
	 */
	public PickUpItemAction(Item item) {
		this.item = item;
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
			map.locationOf(actor).removeItem(item);
			actor.addItemToInventory(item);
			pickUp="pickedUp";
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
		if (pickUp.equals("pickedUp")){
			pickUp="menu";
			return actor + " picks up the " + item+" from the ground";
		}
		else{
			return actor + " can pick up the " + item+ " from the ground";
		}
	}
}
