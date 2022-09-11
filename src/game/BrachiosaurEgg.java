package game;

import edu.monash.fit2099.engine.Location;

/**
 * Class that represents egg of Brachiosaur
 */
public class BrachiosaurEgg extends Egg {
    /**
     * The age of Egg
     */
    int age;
    /***
     * Constructor.
     */
    public BrachiosaurEgg() {
        super("BrachiosaurEgg",'b', true);
        age=0;
    }

    /**
     * Allows passage of time for Egg
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    public int getAge() {
        return age;
    }
}
