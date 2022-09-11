package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;

import java.util.ArrayList;


/**
 * Special Action for attacking other Actors.
 */
public class EatAction extends Action {

	/**
	 * The Fruit that is to be eaten
	 */
	protected Fruit food;

	protected Location location;
	protected Tree tree;
	protected Actor targetActor;
	protected Egg egg;

    /**
     * Multiple constructors for different types of Dinosaurs with different food types
     * /

    /** 
     * Constructor for Brachiosaur
     * @param Tree tree
     * @param Location treeLocation
     */
    public EatAction(Location location) {
		this.location = location;
	}
    /** 
     * Constructor foor Allosaur
     * @param Actor targetActor
     * @param Location targetActorLocation
     */
	public EatAction(Actor targetActor, Location targetActorLocation) {
		this.targetActor = targetActor;
        this.location = targetActorLocation;
	}


	@Override
	public String execute(Actor actor, GameMap map) {
        // Stegosaur
        if(actor instanceof Stegosaur) {
            Stegosaur stegosaur = (Stegosaur) actor;

            // Eat from bush
            if(this.location.getGround() instanceof Bush) {
                Bush bush = (Bush) this.location.getGround();
                bush.removeFruit();
                stegosaur.heal(stegosaur.getHealAmountFromEating());
                return "Stegosaur has eaten fruit from bush at (" + location.x() + "," + location.y() + ").";
            // Eat from ground
            } else {
                for(Item item : this.location.getItems()) {
                    if(item instanceof Fruit) {
                        // Remove food from location
                        this.location.removeItem(this.food);
                        stegosaur.heal(stegosaur.getHealAmountFromEating());
                        return "Stegosaur has eaten fruit from ground (" + location.x() + "," + location.y() + ").";
                    }      
                }
            }
        // Brachiosaur
        } else if(actor instanceof Brachiosaur) {
            
            Brachiosaur brachiosaur = (Brachiosaur) actor;

            // Eat from trees
            if(this.location.getGround() instanceof Tree) {
                Tree tree = (Tree) this.location.getGround();
                for(Fruit fruit : tree.getFruitsInTree()) {
                    brachiosaur.heal(brachiosaur.getHealAmountFromEating());
                }
                // Empty fruits in tree
                tree.setFruitsInTree(new ArrayList<Fruit>());
            }

            return "Brachiosaur has eaten all fruits in tree at (" + location.x() + "," + location.y() + ").";
        
        // Allosaur
        } else if(actor instanceof Allosaur) {
            
            Allosaur allosaur = (Allosaur) actor;

            if(this.egg != null) {
                actor.heal(10);
                this.location.removeItem(egg);

                return "Allosaur has eaten egg at (" + location.x() + "," + location.y() + ").";
            }

            if(this.targetActor instanceof Stegosaur) {
                Stegosaur targetStegosaur = (Stegosaur) this.targetActor;

                // Attack stegosaur
                if(targetStegosaur.vulnerableToAttacks()) {
                    // Set invulnerability to target for next few turns 
                    targetStegosaur.setTurnsUntilVulnerableToAttack(targetStegosaur.getTurnsUntilVulnerableToAttack());
                    
                    if(allosaur.isAdult()) {
                        targetStegosaur.hurt(targetStegosaur.getHurtAmountFromBeingAttacked());
                        allosaur.heal(allosaur.getHealAmountFromEating());
                        return "Allosaur attacked vulnerable adult Stegosaur at (" + location.x() + "," + location.y() + ").";
                    } else {
                        targetStegosaur.hurt(10);
                        allosaur.heal(10);
                        return "Allosaur attacked vulnerable child Stegosaur at (" + location.x() + "," + location.y() + ").";
                    }
                }
            } else if(this.targetActor instanceof Pterodactyl) {
                Pterodactyl targetPterodactyl = (Pterodactyl) this.targetActor;

                // Eats Pterodactyl
                if(targetPterodactyl.isVulnerable(map)) {
                    allosaur.heal(Integer.MAX_VALUE);
                    map.removeActor(targetPterodactyl);
                    return "Allosaur attacked Pterodactyl at (" + location.x() + "," + location.y() + ").";
                }
            }

            for(Item item : this.location.getItems()) {
                // Eat corpse
                // Stegosaur Corpse
                if(item instanceof StegosaurCorpse) {
                    StegosaurCorpse corpse = (StegosaurCorpse) item;
                    allosaur.heal(50);
                    location.removeItem(corpse);
                    return "Allosaur has eaten Stegosaur corpse at (" + location.x() + "," + location.y() + ").";
                // Brachiosaur Corpse
                } else if (item instanceof BrachiosaurCorpse) {
                    BrachiosaurCorpse corpse = (BrachiosaurCorpse) item;
                    
                    allosaur.heal(Integer.MAX_VALUE);
                    location.removeItem(corpse);
                    return "Allosaur has eaten Brachiosaur corpse at (" + location.x() + "," + location.y() + ").";
                // Allosaur Corpse
                } else if (item instanceof AllosaurCorpse) {
                    AllosaurCorpse corpse = (AllosaurCorpse) item;

                    allosaur.heal(50);
                    location.removeItem(corpse);
                    return "Allosaur has eaten Allosaur corpse at (" + location.x() + "," + location.y() + ").";
                // Pterodactyl Corpse
                } else if (item instanceof PterodactylCorpse) {
                    PterodactylCorpse corpse = (PterodactylCorpse) item;

                    allosaur.heal(30);
                    location.removeItem(corpse);
                    return "Allosaur has eaten Pterodactyl corpse at (" + location.x() + "," + location.y() + ").";
                // Eat egg
                } else if (item instanceof Egg) {
                    allosaur.heal(10);
                    location.removeItem(item); 
                    return "Allosaur has eaten egg at (" + location.x() + "," + location.y() + ").";
                }
            }
        // Pterodactyl
        } else if (actor instanceof Pterodactyl) {
            Pterodactyl pterodactyl = (Pterodactyl) actor;
            pterodactyl.resetFlyingStamina();
            for(Item item : this.location.getItems()) {
                if(item instanceof StegosaurCorpse) {
                    StegosaurCorpse corpse = (StegosaurCorpse) item;
                    pterodactyl.heal(10);
                    return actor + " is eating Stegosaur corpse at (" + location.x() + "," + location.y() + "). HP+10";
                // Brachiosaur Corpse
                } else if (item instanceof BrachiosaurCorpse) {
                    BrachiosaurCorpse corpse = (BrachiosaurCorpse) item;
                    pterodactyl.heal(10);
                    return actor + " is eating Brachiosaur corpse at (" + location.x() + "," + location.y() + "). HP+10";
                // Allosaur Corpse
                } else if (item instanceof AllosaurCorpse) {
                    AllosaurCorpse corpse = (AllosaurCorpse) item;
                    pterodactyl.heal(10);
                    return actor + " is eating Allosaur corpse at (" + location.x() + "," + location.y() + "). HP+10";
                // Pterodactyl Corpse
                } else if (item instanceof PterodactylCorpse) {
                    PterodactylCorpse corpse = (PterodactylCorpse) item;
                    pterodactyl.heal(10);
                    return actor + " is eating Pterodactyl corpse at (" + location.x() + "," + location.y() + "). HP+10";
                }
            }
        }

        return null;
    }

	@Override
    // Food must have a toString() method**
	public String menuDescription(Actor actor) {
		return actor + " eats " + food + ".";
	}
}
