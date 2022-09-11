package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

public class Fruit extends Item {
    /**
     * Keep track of how old the fruit is
     */
    int age=0;
    /***
     * Constructor.
     *  @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public Fruit(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }

    /**
     *Allows passage of time for the fruit on the ground
     * @param currentLocation The location of the ground on which we lie.
     */
    @Override
    public void tick(Location currentLocation) {
        super.tick(currentLocation);
        age++;
    }

    /**
     * Retrieves the age of the fruit
     * @return int value for the age of the Fruit
     */
    public int getAge() {
        return age;
    }
}
