package game;

import edu.monash.fit2099.engine.*;

import java.util.*;

public class ChallengeWorld {
    protected Display display;
    protected ArrayList<GameMap> gameMaps = new ArrayList<GameMap>();
    protected ActorLocations actorLocations = new ActorLocations();
    protected Actor player; // We only draw the particular map this actor is on.
    protected Map<Actor, Action> lastActionMap = new HashMap<Actor, Action>();

    /**
     * Constructor.
     *
     * @param display the Display that will display this World.
     */
    public ChallengeWorld(Display display) {
        Objects.requireNonNull(display);
        this.display = display;
    }

    /**
     * Add a GameMap to the World.
     * @param gameMap the GameMap to add
     */
    public void addGameMap(GameMap gameMap) {
        Objects.requireNonNull(gameMap);
        gameMaps.add(gameMap);
        gameMap.actorLocations = actorLocations;
    }

    /**
     * Set an actor as the player. The map is drawn just before this Actor's turn
     *
     * @param player   the player to add
     * @param location the Location where the player is to be added
     */
    public void addPlayer(Actor player, Location location) {
        this.player = player;
        actorLocations.add(player, location.map().at(location.x(), location.y()));
        actorLocations.setPlayer(player);
    }

    /**
     * Run the game.
     *
     * On each iteration the gameloop does the following: - displays the player's
     * map - processes the actions of every Actor in the game, regardless of map
     *
     * We could either only process the actors on the current map, which would make
     * time stop on the other maps, or we could process all the actors. We chose to
     * process all the actors.
     *
     * @throws IllegalStateException if the player doesn't exist
     */
    public void run(int moves, int targetEcoPoints) {
        if (player == null)
            throw new IllegalStateException();

        // initialize the last action map to nothing actions;
        for (Actor actor : actorLocations) {
            lastActionMap.put(actor, new DoNothingAction());
        }
        int i = 1;
        // This loop is basically the whole game

        while (i<=moves) {
            if (!stillRunning()){
                break;
            }
            GameMap playersMap = actorLocations.locationOf(player).map();
            playersMap.draw(display);

            // Process all the actors.
            for (Actor actor : actorLocations) {
                if (stillRunning())
                    processActorTurn(actor);
            }

            // Tick over all the maps. For the map stuff.
            for (GameMap gameMap : gameMaps) {
                gameMap.tick();
            }
            if (player.getEcoPoints()>=targetEcoPoints){
                System.out.println("-------------------------------------");
                System.out.println(player+ " has "+player.getEcoPoints()+" EcoPoints");
                System.out.println("Target Eco points achieved");
                break;
            }
            i++;
        }
        if (player.getEcoPoints()<targetEcoPoints){
            System.out.println("Failed to reach target Eco points within set moves");
        }
        display.println(endGameMessage());
    }

    /**
     * Gives an Actor its turn.
     * Update actor's state every turn
     *
     * The Actions an Actor can take include:
     * <ul>
     * <li>those conferred by items it is carrying</li>
     * <li>movement actions for the current location and terrain</li>
     * <li>actions that can be done to Actors in adjacent squares</li>
     * <li>actions that can be done using items in the current location</li>
     * <li>skipping a turn</li>
     * </ul>
     *
     * @param actor the Actor whose turn it is.
     */
    protected void processActorTurn(Actor actor) {
        Location here = actorLocations.locationOf(actor);
        GameMap map = here.map();

        Actions actions = new Actions();
        for (Item item : actor.getInventory()) {
            actions.add(item.getAllowableActions());
            // Game rule. If you're carrying it, you can drop it.
            actions.add(item.getDropAction());
        }

        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();

            // Game rule. You don't get to interact with the ground if someone is standing
            // on it.
            if (actorLocations.isAnActorAt(destination)) {
                actions.add(actorLocations.getActorAt(destination).getAllowableActions(actor, exit.getName(), map));
            } else {
                actions.add(destination.getGround().allowableActions(actor, destination, exit.getName()));
            }
            actions.add(destination.getMoveAction(actor, exit.getName(), exit.getHotKey()));
        }

        for (Item item : here.getItems()) {
            actions.add(item.getAllowableActions());
            // Game rule. If it's on the ground you can pick it up.
            actions.add(item.getPickUpAction());
        }

        if (here.getGround() instanceof Bush){
            if (((Bush) here.getGround()).getFruitsInBush().size()>0){
                actions.add(((Bush) here.getGround()).getFruitsInBush().get(((Bush) here.getGround()).getFruitsInBush().size()-1).getAllowableActions());
                actions.add(((Bush) here.getGround()).getFruitsInBush().get(((Bush) here.getGround()).getFruitsInBush().size()-1).getPickUpFruitFromBushAction());
            }
        }

        if (here.getGround() instanceof Tree){
            if (((Tree) here.getGround()).getFruitsInTree().size()>0){
                actions.add(((Tree) here.getGround()).getFruitsInTree().get(((Tree) here.getGround()).getFruitsInTree().size()-1).getAllowableActions());
                actions.add(((Tree) here.getGround()).getFruitsInTree().get(((Tree) here.getGround()).getFruitsInTree().size()-1).getPickUpFruitFromTreeAction());
            }
        }

        actions.add(new DoNothingAction());
        actions.add(new DisplayEcoPointsAction(actor.getEcoPoints()));
        actions.add(new QuitAction(actorLocations));

        // Update actor's state
        actor.tick(map, display);

        Action action = actor.playTurn(actions, lastActionMap.get(actor), map, display);
        lastActionMap.put(actor, action);

        String result = action.execute(actor, map);
        display.println(result);
    }

    /**
     * Returns true if the game is still running.
     *
     * The game is considered to still be running if the player is still around.
     *
     * @return true if the player is still on the map.
     */
    protected boolean stillRunning() {
        return actorLocations.contains(player);
    }

    /**
     * Return a string that can be displayed when the game ends.
     *
     * @return the string "Game Over"
     */
    protected String endGameMessage() {
        return "Game Over";
    }
}
