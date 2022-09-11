package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Special Action for attacking other Actors.
 */
public class MateAction extends Action {

	/**
	 * The Actor to mate with
	 */
	protected Actor target;

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public MateAction(Actor target) {
		this.target = target;
	}

	@Override
	public String execute(Actor actor, GameMap map) {        
        // Update turns until hatch
        if(actor instanceof Stegosaur) {
            Stegosaur parent = (Stegosaur) actor;
            if(parent.isFemale()) {
                if(!parent.isPregnant()) {
                    parent.setTurnsUntilLayEgg(parent.getHatchDuration());
                    return "Stegosaur is pregnant.";
                } else {
                    return "Stegosaur is already pregnant.";
                }
            } else {
                return "Stegosaur mated.";
            }
        // Brachiosaur
        } else if(actor instanceof Brachiosaur) {
            Brachiosaur parent = (Brachiosaur) actor;
            if(parent.isFemale()) {
                if(!parent.isPregnant()) {
                    parent.setTurnsUntilLayEgg(parent.getHatchDuration());
                    return "Brachiosaur is pregnant.";
                } else {
                    return "Brachiosaur is already pregnant.";
                }
            } else {
                return "Brachiosaur mated.";
            }
        // Allosaur
        } else if(actor instanceof Allosaur) {
            Allosaur parent = (Allosaur) actor;
            if(parent.isFemale()) {
                if(!parent.isPregnant()) {
                    parent.setTurnsUntilLayEgg(parent.getHatchDuration());
                    return "Allosaur is pregnant.";
                } else {
                    return "Allosaur is already pregnant.";
                }
            } else {
                return "Allosaur mated.";
            }
        // Pterodactyl
        } else if(actor instanceof Pterodactyl) {
            Pterodactyl parent = (Pterodactyl) actor;
            if(parent.isFemale()) {
                if(!parent.isPregnant()) {
                    parent.setTurnsUntilLayEgg(parent.getHatchDuration());
                    return "Pterodactyl is pregnant.";
                } else {
                    return "Pterodactyl is already pregnant.";
                }
            } else {
                return "Pterodactyl mated.";
            }
        }
        return null;
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " mates with " + target;
	}
}
