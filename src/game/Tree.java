package game;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

import java.util.ArrayList;
import java.util.Random;

public class Tree extends Ground {
	/**
	 * Is used to tell if Fruit was produced in a turn
	 */
	private boolean fruitsProducedThisTurn;
	/**
	 * The age of the Tree
	 */
	private int age = 0;
	/**
	 * The fruits in the Tree
	 */
	private ArrayList<Fruit> fruitsInTree;

	/**
	 * Constructor
	 */
	public Tree() {
		super('+');
		fruitsInTree = new ArrayList<Fruit>();
		fruitsProducedThisTurn=false;
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
	 * sets new fruit Array
	 * @param fruits
	 */
    public void setFruitsInTree(ArrayList<Fruit> fruits) {
        this.fruitsInTree = fruits;
    }

	/**
	 * Checks if there are fruits in the Tree
	 * @return true if fruits in tree
	 */
	public boolean hasFruitsInTree() {
        return this.fruitsInTree.size() > 0;
    }

	/**
	 * Allows passage of time for Tree
	 * @param location The location of the Ground
	 */
	@Override
	public void tick(Location location) {
		super.tick(location);
		int randomNumber = nextID();

		age++;
		if (age == 5)
			displayChar = 't';
		if (age == 10)
			displayChar = 'T';

		if (age>=8){
			if (randomNumber<50){
				fruitsInTree.add(new Fruit("RipeFruit",'F',true));
				fruitsProducedThisTurn=true;
			}
		}

		int i=0;
		if (fruitsInTree.size()>0){
			while (i<fruitsInTree.size()){
				int randomNumber2 = nextID();
				if (randomNumber2<5){
					fruitsInTree.remove(i);
					Fruit RipeFruit = new Fruit("RipeFruit",'F',true);
					location.addItem(RipeFruit);
				}
				i++;
			}
		}

	}

	/**
	 * Provides reference for fruitsInTree Array to be accessed
	 * @return ArrayReference for fruitsInTree
	 */
	public ArrayList<Fruit> getFruitsInTree() {
		return fruitsInTree;
	}

	/**
	 * returns the whether fruit produced in this turn
	 * @return boolean true if fruit was produced
	 */
	public boolean isFruitsProducedThisTurn() {
		return fruitsProducedThisTurn;
	}

	/**
	 * resets the value fruitsProducedThisTurn to default false
	 */
	public void resetIsFruitProducedThisTurn(){
		fruitsProducedThisTurn=false;
	}
}
