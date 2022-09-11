package game;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.monash.fit2099.engine.*;

/**
 * The main class for the Jurassic World game.
 *
 */
public class Application {

	public static void main(String[] args) {
		int selection;
		do{
			selection=selectMenu();
			switch(selection) {
				case 1:
					sandBoxMode();
					break;
				case 2:
					challengeMode();
					break;
			}
		}while (selection!=3);
		System.out.println("Thanks For Playing");
	}


	public static int selectMenu(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("------------");
		System.out.println("1) SandBox");
		System.out.println("2) Challenge");
		System.out.println("3) Exit");
		System.out.println("------------");
		System.out.print("Select option: ");
		int res= scanner.nextInt();
		return res;
	}

	public static void sandBoxMode(){
		World world = new World(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(),new Bush(),new Lake());

		List<String> mapNorth = Arrays.asList(
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"..................................................................~.............",
				"......................................+++.......................................",
				"............~..........................++++.....................................",
				"...................................+++++........................................",
				".....................................++++++.....................................",
				"......................................+++.......................................",
				".....................................+++........................................",
				"................................................................................",
				"............+++.................................................................",
				".............+++++..............................................................",
				"...............++...................~....................+++++..................",
				".............+++....................................++++++++....................",
				"............+++.......................................+++.......................",
				"................................................................................",
				".........................................................................++.....",
				"........................................................................++.++...",
				".................~..............................................~..........++++...",
				"..........................................................................++....",
				"................................................................................");

		List<String> map = Arrays.asList(
				"................................................................................",
				"................................................................................",
				".....#######....................................................................",
				".....#_____#....................................................................",
				".....#_____#....................................................................",
				".....###.###....................................................................",
				"................................................................................",
				"......................................+++.......................................",
				".......................................++++.....................................",
				"...................................+++++........................................",
				".....................................++++++.....................................",
				"......................................+++.......................................",
				".....................................+++........................................",
				"................................................................................",
				"............+++.................................................................",
				".............+++++..............................................................",
				"...............++........................................+++++..................",
				".............+++....................................++++++++....................",
				"............+++.......................................+++.......................",
				"................................................................................",
				".........................................................................++.....",
				"........................................................................++.++...",
				".........................................................................++++...",
				"..........................................................................++....",
				"................................................................................");
		GameMap gameMap = new GameMap(groundFactory, map );
		GameMap gameMapNorth = new GameMap(groundFactory,mapNorth);

		for (int x : gameMap.getXRange()) {
			gameMap.at(x,0).addExit(new Exit("North",gameMapNorth.at(x,24),"8"));
		}
		for (int x:gameMapNorth.getXRange()){
			gameMapNorth.at(x,24).addExit((new Exit("South",gameMap.at(x,0),"2")));
		}
		world.addGameMap(gameMap);
		world.addGameMap(gameMapNorth);

		Actor player = new Player("Player", '@', 100);
		world.addPlayer(player, gameMap.at(9, 4));
		VendingMachine vendingMachine = new VendingMachine("VendingMachine",'[',false);
		vendingMachine.addAction(new BuyFruitAction());
		vendingMachine.addAction(new BuyVegeterianKitAction());
		vendingMachine.addAction(new BuyCarnivoreKitAction());
		vendingMachine.addAction(new BuyStegosaurEggAction());
		vendingMachine.addAction(new BuyBrachiosaurEggAction());
		vendingMachine.addAction(new BuyPterodactylEggAction());
		vendingMachine.addAction(new BuyAllosaurEggAction());
		vendingMachine.addAction(new BuyLaserGunAction());

		gameMap.at(30,20).setGround(new Lake());
		gameMap.at(40,15).setGround(new Lake());
		gameMap.at(50,20).setGround(new Lake());
		gameMap.at(30,10).setGround(new Lake());
		gameMap.at(50,10).setGround(new Lake());
		gameMap.at(10,15).setGround(new Lake());
		gameMap.at(30,20).setGround(new Lake());
		gameMap.at(10,8).setGround(new Lake());

		gameMap.at(10,7).setGround(new Tree());
		gameMap.at(10,7).setGround(new Tree());

		gameMap.at(10,3).addItem(vendingMachine);

		gameMap.at(30,9).addActor(new Stegosaur("Stegosaur", 'm', 0));
		gameMap.at(28, 9).addActor(new Stegosaur("Stegosaur", 'f', 0));

		gameMap.at(10, 12).addActor(new Brachiosaur("Brachiosaur", 'm', 0));
		gameMap.at(8, 12).addActor(new Brachiosaur("Brachiosaur", 'm', 0));
		gameMap.at(12, 12).addActor(new Brachiosaur("Brachiosaur", 'f', 0));
		gameMap.at(13, 12).addActor(new Brachiosaur("Brachiosaur", 'f', 0));

		// gameMap.at(26, 9).addActor(new Brachiosaur("Brachoisaur", 'f', 0));
		//gameMap.at(10, 7).addActor(new Allosaur("Allosaur", 'f', 0));
		//gameMap.at(10, 6).addActor(new Pterodactyl("Pterodactyl", 'm', 0));
		// gameMap.at(10, 7).addActor(new Pterodactyl("Pterodactyl", 'f', 0));
		// gameMap.at(10, 7).addItem(new StegosaurCorpse());
		//gameMap.at(16,8).addItem(new AllosaurEgg());

        
		world.run();
	}

	public static void challengeMode(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("Enter Number of Moves needed : ");
		int inputMovesNum=scanner.nextInt();
		System.out.println("Enter Target Eco Points");
		int inputTargetEcoPoints=scanner.nextInt();

		ChallengeWorld world = new ChallengeWorld(new Display());

		FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(), new Wall(), new Floor(), new Tree(),new Bush(),new Lake());

		List<String> mapNorth = Arrays.asList(
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"................................................................................",
				"..................................................................~.............",
				"......................................+++.......................................",
				"............~..........................++++.....................................",
				"...................................+++++........................................",
				".....................................++++++.....................................",
				"......................................+++.......................................",
				".....................................+++........................................",
				"................................................................................",
				"............+++.................................................................",
				".............+++++..............................................................",
				"...............++...................~....................+++++..................",
				".............+++....................................++++++++....................",
				"............+++.......................................+++.......................",
				"................................................................................",
				".........................................................................++.....",
				"........................................................................++.++...",
				".................~..............................................~..........++++...",
				"..........................................................................++....",
				"................................................................................");

		List<String> map = Arrays.asList(
				"................................................................................",
				"................................................................................",
				".....#######....................................................................",
				".....#_____#....................................................................",
				".....#_____#....................................................................",
				".....###.###....................................................................",
				"................................................................................",
				"......................................+++.......................................",
				".......................................++++.....................................",
				"...................................+++++........................................",
				".....................................++++++.....................................",
				"......................................+++.......................................",
				".....................................+++........................................",
				"................................................................................",
				"............+++.................................................................",
				".............+++++..............................................................",
				"...............++........................................+++++..................",
				".............+++....................................++++++++....................",
				"............+++.......................................+++.......................",
				"................................................................................",
				".........................................................................++.....",
				"........................................................................++.++...",
				".........................................................................++++...",
				"..........................................................................++....",
				"................................................................................");
		GameMap gameMap = new GameMap(groundFactory, map );
		GameMap gameMapNorth = new GameMap(groundFactory,mapNorth);

		for (int x : gameMap.getXRange()) {
			gameMap.at(x,0).addExit(new Exit("North",gameMapNorth.at(x,24),"8"));
		}
		for (int x:gameMapNorth.getXRange()){
			gameMapNorth.at(x,24).addExit((new Exit("South",gameMap.at(x,0),"2")));
		}
		world.addGameMap(gameMap);
		world.addGameMap(gameMapNorth);

		Actor player = new Player("Player", '@', 100);
		world.addPlayer(player, gameMap.at(9, 4));
		VendingMachine vendingMachine = new VendingMachine("VendingMachine",'[',false);
		vendingMachine.addAction(new BuyFruitAction());
		vendingMachine.addAction(new BuyVegeterianKitAction());
		vendingMachine.addAction(new BuyCarnivoreKitAction());
		vendingMachine.addAction(new BuyStegosaurEggAction());
		vendingMachine.addAction(new BuyBrachiosaurEggAction());
		vendingMachine.addAction(new BuyAllosaurEggAction());
		vendingMachine.addAction(new BuyLaserGunAction());

		gameMap.at(30,20).setGround(new Lake());
		gameMap.at(40,15).setGround(new Lake());
		gameMap.at(50,20).setGround(new Lake());
		gameMap.at(30,10).setGround(new Lake());
		gameMap.at(50,10).setGround(new Lake());
		gameMap.at(10,15).setGround(new Lake());
		gameMap.at(30,20).setGround(new Lake());
		gameMap.at(10,8).setGround(new Lake());


		gameMap.at(10,3).addItem(vendingMachine);


		// Place a pair of stegosaurs in the middle of the map
		 gameMap.at(30,9).addActor(new Stegosaur("Stegosaur", 'm', 0));
		 gameMap.at(28, 9).addActor(new Stegosaur("Stegosaur", 'f', 0));

		 gameMap.at(10, 12).addActor(new Brachiosaur("Brachiosaur", 'm', 0));
		 gameMap.at(8, 12).addActor(new Brachiosaur("Brachiosaur", 'm', 0));
		 gameMap.at(12, 12).addActor(new Brachiosaur("Brachiosaur", 'f', 0));
		 gameMap.at(13, 12).addActor(new Brachiosaur("Brachiosaur", 'f', 0));

		// gameMap.at(26, 9).addActor(new Brachiosaur("Brachoisaur", 'f', 0));
		//gameMap.at(10, 7).addActor(new Allosaur("Allosaur", 'f', 0));
		//gameMap.at(10, 6).addActor(new Pterodactyl("Pterodactyl", 'm', 0));
		// gameMap.at(10, 7).addActor(new Pterodactyl("Pterodactyl", 'f', 0));
		// gameMap.at(10, 7).addItem(new StegosaurCorpse());
		//gameMap.at(16,8).addItem(new AllosaurEgg());
		world.run(inputMovesNum,inputTargetEcoPoints);
	}
}
