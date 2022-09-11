package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

import java.util.Random;

public class Lake extends Ground {

    /**
     * Amount of Sips a dinosaur can take from the Lake
     */
    private int sips;
    /**
     * Amount of Fish in the Lake
     */
    private int fish;

    /**
     * Constructor.
     *
     */
    public Lake() {
        super('~');
        sips = 25;
        fish = 5;
    }

    @Override
    /**
     * Land-based creatures cannot enter water
     */
    public boolean canActorEnter(Actor actor) {
        return actor instanceof Pterodactyl;
    }

    /**
     * Returns the number of fish in lake
     * @return int value of number of fish
     */
    public int getFish() {
        return fish;
    }


    /**
     * removes input number of Fish from Lake
     * @param noOfFish Number of fish to be removed
     */
    public void removeFishes(int noOfFish) {
        fish -= noOfFish;
    }

    /**
     * Allows Passage of time for Lake
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        int randomNumber2 = nextID();
        super.tick(location);
        if (location.map().isRaining()){
            double rainFall = rainMultiplier();
            sips+= Math.round(rainFall*20);
        }

        if (randomNumber2<60 && fish<=25){
            fish+=1;
        }
    }

    /**
     * Randomly generates a number
     * @return int value that is randomly generated
     */
    public int nextID() {
        Random r = new Random();
        int low = 0;//using literal values is not a good idea, replace them with input parameters
        int high = 100;
        return (r.nextInt(high - low) + low);
    }

    /**
     *Generates random number between 0.1 and 0.6
     * @return int value for rainMultiplier
     */
    public double rainMultiplier() {
        Random r = new Random();
        double low = 0.1;
        double high = 0.6;
        return r.nextDouble()*(high-low)+low;
    }

    /**
     * Removes sips from Lake
     */
    public void waterDrank(){
        sips--;
    }

    /**
     * Returns the number of sips that can be taken
     * @return int value for sips available
     */
    public int getSips() {
        return sips;
    }
}
