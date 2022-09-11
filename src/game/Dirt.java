package game;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;
import java.util.Random;

/**
 * A class that represents bare dirt.
 */
public class Dirt extends Ground {
	/**
	 * Constructor
	 */
	public Dirt() {
		super('.');
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
	 * Allows passage of time for Dirt
	 * @param location The location of the Ground
	 */
	@Override
	public void tick(Location location) {
		String[] directions= {"North","NorthEast","East","SouthEast","South","SouthWest","West","NorthWest"};
		int minY = location.map().getYRange().min();
		int maxY = location.map().getYRange().max();
		int minX = location.map().getXRange().min();
		int maxX = location.map().getXRange().max();
		int nearbyBushes = 0;
		boolean treeCheck = false;

		super.tick(location);
		int randomNumber = nextID();
		for (String elem : directions){
			if (elem.equals("North")){
				int newY = location.y()+1;
				if (newY>=minY && newY<maxY){
					if (location.map().at(location.x(),newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(location.x(),newY).getGround() instanceof Bush && ((Bush) location.map().at(location.x(),newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("South")){
				int newY = location.y()-1;
				if (newY>=minY && newY<maxY){
					if (location.map().at(location.x(),newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(location.x(),newY).getGround() instanceof Bush && ((Bush) location.map().at(location.x(),newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("West")){
				int newX = location.x()-1;
				if (newX>=minX && newX<maxX){
					if (location.map().at(newX,location.y()).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,location.y()).getGround() instanceof Bush && ((Bush) location.map().at(newX,location.y()).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("East")){
				int newX = location.x()+1;
				if (newX>=minX && newX<maxX){
					if (location.map().at(newX,location.y()).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,location.y()).getGround() instanceof Bush && ((Bush) location.map().at(newX,location.y()).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("NorthEast")){
				int newX = location.x()+1;
				int newY = location.y()+1;
				if ((newX>=minX && newX<maxX) && (newY>=minY && newY<maxY) ){
					if (location.map().at(newX,newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,newY).getGround() instanceof Bush && ((Bush) location.map().at(newX,newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("NorthWest")){
				int newX = location.x()-1;
				int newY = location.y()+1;
				if ((newX>=minX && newX<maxX) && (newY>=minY && newY<maxY) ){
					if (location.map().at(newX,newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,newY).getGround() instanceof Bush && ((Bush) location.map().at(newX,newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("SouthEast")){
				int newX = location.x()+1;
				int newY = location.y()-1;
				if ((newX>=minX && newX<maxX) && (newY>=minY && newY<maxY) ){
					if (location.map().at(newX,newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,newY).getGround() instanceof Bush && ((Bush) location.map().at(newX,newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}
			else if (elem.equals("SouthWest")){
				int newX = location.x()-1;
				int newY = location.y()-1;
				if ((newX>=minX && newX<maxX) && (newY>=minY && newY<maxY) ){
					if (location.map().at(newX,newY).getGround() instanceof Tree){
						treeCheck=true;
					}
					if (location.map().at(newX,newY).getGround() instanceof Bush && ((Bush) location.map().at(newX,newY).getGround()).getAge()>1){
						nearbyBushes++;
					}
				}
			}

		}
		if (!treeCheck){
			if (nearbyBushes>=2){
				if (randomNumber<=10){
					location.setGround(new Bush());
				}
			}
			else{
				if (randomNumber<=1){
					location.setGround(new Bush());
				}
			}
		}

	}

}
