package game;

import edu.monash.fit2099.engine.Item;

public class VegetarianMealKit extends Item {

    /***
     * Constructor.
     *  @param name the name of this Item
     * @param displayChar the character to use to represent this item if it is on the ground
     * @param portable true if and only if the Item can be picked up
     */
    public VegetarianMealKit(String name, char displayChar, boolean portable) {
        super(name, displayChar, portable);
    }
}
