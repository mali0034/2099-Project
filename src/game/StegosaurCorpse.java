package game;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

public class StegosaurCorpse extends Item {
    /**
     * Keep track of how old the Corpse is
     */
    int age;
    /***
     * Constructor.
     */
    public StegosaurCorpse() {
        super("StegosaurCorpse",'c',false);
        age=0;
    }

    /**
     * Allows Passage of time for corpse
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    /**
     * returns how old the corpse is
     * @return int value for the age of corpse
     */
    public int getAge() {
        return age;
    }
}
