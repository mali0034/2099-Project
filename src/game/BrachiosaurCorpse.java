package game;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

public class BrachiosaurCorpse extends Item {
    /**
     * Keep track of how old the Corpse is
     */
    int age;
    /***
     * Constructor.
     */
    public BrachiosaurCorpse() {
        super("BrachiosaurCorpse",'c',false);
        age=0;
    }

    /**
     * Allows passage of time for Corpse
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    /**
     * Gives age of BrachiosaurEgg
     * @return
     */
    public int getAge() {
        return age;
    }
}
