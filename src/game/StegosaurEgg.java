package game;

import edu.monash.fit2099.engine.Location;

public class StegosaurEgg extends Egg {
    /**
     * How old the egg is
     */
    int age;
    /***
     * Constructor
     */
    public StegosaurEgg() {
        super("StegosaurEgg", 's', true);
        age=0;
    }

    /**
     * Allows Passage of time for egg
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    /**
     * Return the age of the egg
     * @return int value for age of egg
     */
    public int getAge() {
        return age;
    }
}
