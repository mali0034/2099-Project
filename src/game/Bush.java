package game;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

import java.util.ArrayList;
import java.util.Random;

public class Bush extends Ground {
    /**
     * Age of the Bush
     */
    int age=0;
    private ArrayList<Fruit> fruitsInBush;
    /**
     * Constructor.
     *
     */
    public Bush() {
        super('o');
        fruitsInBush = new ArrayList<Fruit>();
    }

    /**
     * Allows passage of time for bush
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        age++;

        if (age>=8) {
            int randomNumber = nextID();
            if (randomNumber < 10) {
                fruitsInBush.add(new Fruit("RipeFruit", 'F', true));
            }
        }
    }

    /**
     * Produces random number
     * @return int number that is randomly generated
     */
    public int nextID() {
        Random r = new Random();
        int low = 0;//using literal values is not a good idea, replace them with input parameters
        int high = 100;
        return (r.nextInt(high - low) + low);
    }

    /**
     * return Age of Bush
     * @return int value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * returns the reference to the fruitsInBush Array
     * @return reference for fruitsInBush Array
     */
    public ArrayList<Fruit> getFruitsInBush() {
        return fruitsInBush;
    }

    /**
     * Remove fruit from bush
     */
    public void removeFruit() {
        this.fruitsInBush.remove( this.fruitsInBush.size() - 1 );
    }


    /**
     * Returns true if bush has fruits
     */
    public boolean hasFruit() {
        return this.fruitsInBush.size() > 0;
    }
}
