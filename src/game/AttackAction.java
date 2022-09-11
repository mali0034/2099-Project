package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;

/**
 * Special Action for attacking other Actors.
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	/**
	 * Attack A Dinosaur
	 *
	 * @see Action#execute(Actor, GameMap)
	 * @param actor The actor performing the action.
	 * @param map The map the actor is on.
	 * @return a suitable description to display in the UI
	 */
	@Override
	public String execute(Actor actor, GameMap map) {

		Weapon weapon = actor.getWeapon();

		if (rand.nextBoolean()) {
			return actor + " misses " + target + ".";
		}

		int damage = weapon.damage();
		String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";

		target.hurt(damage);
		if (target instanceof Stegosaur){
			if (!target.isConscious()) {
				StegosaurCorpse corpse = new StegosaurCorpse();
				map.locationOf(target).addItem(corpse);

				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)
					drop.execute(target, map);
				map.removeActor(target);

				result += System.lineSeparator() + target + " is killed.";
			}
		}
		else if (target instanceof Brachiosaur){
			if (!target.isConscious()) {
				BrachiosaurCorpse corpse = new BrachiosaurCorpse();
				map.locationOf(target).addItem(corpse);

				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)
					drop.execute(target, map);
				map.removeActor(target);

				result += System.lineSeparator() + target + " is killed.";
			}
		}
		else if (target instanceof Allosaur) {
			if (!target.isConscious()) {
				AllosaurCorpse corpse = new AllosaurCorpse();
				map.locationOf(target).addItem(corpse);

				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)
					drop.execute(target, map);
				map.removeActor(target);

				result += System.lineSeparator() + target + " is killed.";
			}
		}
        // Pterodactyl
        else {
            if (!target.isConscious()) {
				PterodactylCorpse corpse = new PterodactylCorpse();
				map.locationOf(target).addItem(corpse);

				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)
					drop.execute(target, map);
				map.removeActor(target);

				result += System.lineSeparator() + target + " is killed.";
			}
        }
		return result;
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
		return actor + " attacks " + target;
	}
}
