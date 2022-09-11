package game;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

public class PterodactylCorpse extends Item {
    /**
     * Keep track of how old the Corpse is
     */
    int age;
    /***
     * Constructor.
     */
    public PterodactylCorpse() {
        super("PterodactylCorpse",'p',false);
        age=0;
    }

    /**
     * Allows Passage of time for Corpse
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }


    /**
     * Returns how old the corpse is
     * @return
     */
    public int getAge() {
        return age;
    }
}

