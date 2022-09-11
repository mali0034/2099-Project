package game;

import edu.monash.fit2099.engine.Location;

public class PterodactylEgg extends Egg {
    private int age;
    /***
     * Constructor.
     */
    public PterodactylEgg() {
        super("PterodactylEgg",'p',true);
        age=0;
    }

    /**
     * Passage of time for Egg
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    /**
     * Age of egg
     * @return int containing age of egg
     */

    public int getAge() {
        return age;
    }
}
