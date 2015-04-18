/**
 * 
 */
package jumpingalien.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of worlds involving several properties. 
 * This class has an association with the class of game objects.
 * 
 * @invar 	| for each object in this.getGameObjects()
 * 			| object.canHaveAsPosition(object.getPosition()[0],object.getPosition()[1])
 * 
 * @author 	Andreas Schryvers & Jonathan Oostvogels
 * 			2e Bachelor ingenieurswetenschappen
 * 			Subversion repository: https://code.google.com/p/ogp-jumping-alien/
 */
public class World {
	
	/**
	 * Initialize this new world with the given parameters.
	 * 
	 * @param xLimit
	 * 		  The maximum pixel position in the x direction.
	 * @param yLimit
	 * 		  The maximum pixel position in the y direction.
	 * @param tileLength
	 * 		  The length of a square tile.
	 * @param windowSize
	 * 		  The size of the window the player can see.
	 * @param targetTileX
	 * 		  The x position of the target tile.
	 * @param targetTileY
	 * 		  The y position of the target tile.
	 * 
	 * @pre		| xLimit > 0;
	 * @pre		| yLimit > 0;
	 * 
	 * @post	| new.getXLimit() == xLimit
	 * @post	| new.getYLimit() == yLimit
	 * @post	| new.getTileLength() == tileLength
	 * @post	| new.getGameObjects() == HashSet<GameObject>
	 * @effect  | for all i, j pixels in world
	 * 			| 	setTerrainAt(i,j,TerrainType.AIR)
	 * @effect  | setWindowSize(windowSize[0],windowSize[1]);
	 * @effect 	| setWindowPosition(0,0);
	 * @effect 	| adjustWindow();
	 * @effect 	| setTargetTileX(targetTileX);
	 * @effect 	| setTargetTileY(targetTileY);
	 * @effect 	| setGameOver(false);
	 * @effect 	| setDidPlayerWin(false);
	 * 
	 */
	//is het nodig om ook ook te zeggen dat gameover op false gezet wordt?!
	
	public World(int xLimit, int yLimit, int tileLength,
						int[] windowSize, int targetTileX, int targetTileY) {
		assert xLimit > 0;
		assert yLimit > 0;
		
		X_LIMIT = xLimit;
		Y_LIMIT = yLimit;
		TILE_LENGTH = tileLength;
		gameObjects = new HashSet<GameObject>();
		tiles = new TerrainType[(xLimit+1)/tileLength][(yLimit+1)/tileLength];
		
		for(int i = 0; i < (xLimit+1)/tileLength; i++)
			for(int j = 0; j < (yLimit+1)/tileLength; j++)
				setTerrainAt(i,j,TerrainType.AIR);
		
		setWindowSize(windowSize[0],windowSize[1]);
		setWindowPosition(0,0);
		adjustWindow();
		setTargetTileX(targetTileX);
		setTargetTileY(targetTileY);
		setGameOver(false);
		setDidPlayerWin(false);
	}
	
	/**
	 * Associate the given object with this world.
	 * 
	 * @param obj
	 * 		  The object that should be associated with this world.
	 * @effect  | obj.setMyWorld(this)
	 * @effect  | getGameObjects().add(obj)
	 * @effect	| if (obj instanceof Mazub)
	 * @effect	|	setMyMazub((Mazub) obj)
	 */
	public void addObject(GameObject obj) {
		obj.setMyWorld(this);
		getGameObjects().add(obj);
		if(obj instanceof Mazub)
			setMyMazub((Mazub) obj);
	}
	
	/**
	 * Return a set of all the game objects of a certain class.
	 * 
	 * @param c
	 * 		  The class from which to get the objects from.
	 * 
	 * @return | for each obj in getGameObjects()
	 * 		   |    if(c.isInstance(obj))
	 * 		   | 	   result.add(obj)
	 */
	public Set getAllInstancesOf(Class c) { //Set<GameObject> mag niet van de facade
		Set<GameObject> result = new HashSet<>();
		for(GameObject obj : getGameObjects()) {
			if(c.isInstance(obj))
				result.add(obj);
		}
		return result;
	}

	
	/**
	 * Return the x limit of this world.
	 */
	@Basic @Immutable
	public int getXLimit() {
		return X_LIMIT;
	}
	
	/**
	 * Variable registering the x limit of this world.
	 */
	private final int X_LIMIT;
	
	
	/**
	 * Return the y limit of this world.
	 */
	@Basic @Immutable
	public int getYLimit() {
		return Y_LIMIT;
	}
	
	/**
	 * Variable registering the y limit of this world.
	 */
	private final int Y_LIMIT; 
	
	/**
	 * Return the tile length of this world.
	 */
	@Basic @Immutable
	public int getTileLength() {
		return TILE_LENGTH;
	}
	
	/**
	 * Variable registering the tile length of this world.
	 */
	private final int TILE_LENGTH;
	
	/**
	 * Remove the given object from this world.
	 * 
	 * @param obj
	 * 		  The object to remove
	 * @effect | getGameObjects().remove(obj)
	 */
	public void removeObject(GameObject obj) {

		getGameObjects().remove(obj);
	}
	
	/**
	 * Return the set of game objects that is associated with this world.
	 */
	@Basic
	public Set<GameObject> getGameObjects() {
		return gameObjects;
	}
	
	/**
	 * Variable registering the game objects that are associated with this world.
	 */
	private Set<GameObject> gameObjects;
	
	//no documentation needed
	public void advanceTime(double duration) throws IllegalArgumentException {
		//if ((duration < 0) || (duration >= 0.2))
			//throw new IllegalArgumentException("Illegal time duration!");
		getMyMazub().advanceTime(duration);
		int[] new_pos1 = myMazub.getPosition();
		if ( !((new_pos1[0] <= getXLimit()) && (new_pos1[0] >= 0) ) ||
				(! ((new_pos1[1] <= getYLimit()) && (new_pos1[1] >= 0))))
			myMazub.terminate(true);


		if(GameObject.rectanglesCollide(new_pos1[0],new_pos1[1],getMyMazub().getWidth(),getMyMazub().getHeight(),
				getTargetTileX()*getTileLength(),getTargetTileY()*getTileLength(),(getTargetTileX() + 1)*getTileLength(),(getTargetTileY() + 1)*getTileLength())) {
			myMazub.terminate(true);
			setDidPlayerWin(true);
		}
			
//		
//		int[] tile = getMatchingTile(new_pos1[0],new_pos1[1]);
//		if (tile[0] == getTargetTileX() && tile[1] == getTargetTileY()) {
//			myMazub.terminate(true);
//			setDidPlayerWin(true);
//		}
		
		adjustWindow();

		Set<GameObject> copySet = new HashSet<>();
		copySet.addAll(getGameObjects());
		for(GameObject obj: copySet) {
						if(! (obj instanceof Mazub))  {
							obj.advanceTime(duration);
							int[] new_pos2 = obj.getPosition();
							if ( new_pos2[0] < 0 || new_pos2[0] > getXLimit() ||
									new_pos2[1] < 0 || new_pos2[1] > getYLimit() )
								obj.terminate(true);
						}
		}
	}
	
	/**
	 * Return the Mazub that is associated with this world.
	 */
	@Basic
	public Mazub getMyMazub() {
		return myMazub;
	}

	/**
	 * Set the Mazub that is associated with this world to the given Mazub.
	 * @param myMazub
	 * 		  The new Mazub for this world.
	 * @post  new.getMyMazub() == myMazub
	 */
	private void setMyMazub(Mazub myMazub) {
		this.myMazub = myMazub;
	}

	/**
	 * Variable registering the Mazub that is associated with this world.
	 */
	private Mazub myMazub;
	
	/**
	 * Return the window size of this world as an array of width and height.
	 */
	@Basic
	public int[] getWindowSize() {
		return this.windowSize;
	}
	
	/**
	 * Set the window size of this world to the given width and height.
	 * @param width
	 * 		  The new width for this world.
	 * @param height
	 * 		  The new height for this world.
	 * @post  new.getWindowSize() == {width,height}
	 */
	public void setWindowSize(int width, int height) {
		this.windowSize[0] = width;
		this.windowSize[1] = height;
	}
	
	/**
	 * Variable registering the window size of this world.
	 */
	private int[] windowSize = new int[2];
	
	/**
	 * Return the window position of this world.
	 */
	public int[] getWindowPosition() {
		return this.windowPosition;
	}
	
	/**
	 * Set the window position of this world to the given x and y.
	 * @param x
	 * 		  The new x coordinate of the windows size for this world.
	 * @param y
	 * 		  The new y coordinate of the windows size for this world
	 * @pre	  | x > 0 && x <= getXLimit() - getWindowSize()[0]
	 * @pre   | y > 0 && y >= getYLimit() - getWindowSize()[1]
	 * 
	 * @post  | new.getWindowPosition() == {x,y}
	 */
	@Raw
	private void setWindowPosition(int x, int y) {
		assert x >= 0 && x <= getXLimit() - getWindowSize()[0];
		assert y >= 0 && y <= getYLimit() - getWindowSize()[1];
		this.windowPosition[0] = x;
		this.windowPosition[1] = y;
	}
	
	private int[] windowPosition = new int[2];
	
	public boolean canHaveAsWindowPosition(int x, int y) {
		return ( x >= 0 && 
				 x <= (getXLimit() - getWindowSize()[0]) &&
				 y >= 0 &&
				 y <= (getYLimit() - getWindowSize()[1]) );
				
	}
	
	public TerrainType getTerrainAt(int x, int y) {
		if( (x > getXLimit()) || (x < 0) || (y > getYLimit()) || (y < 0))
			return TerrainType.AIR;
		int[] location = getMatchingTile(x,y);
		return this.tiles[location[0]][location[1]];
	}
	
	public void setTerrainAt(int x, int y, TerrainType terrain) {
//		int[] location = getMatchingTile(x,y);
		this.tiles[x][y] = terrain;
	}
	
	public int[] getMatchingTile(int x, int y) {
		int[] result = {x/getTileLength(),y/getTileLength()};
		return result;
	}

	private TerrainType[][] tiles;
	
	
	@Value
	public enum TerrainType {
		AIR(true), WATER(true), MAGMA(true), SOLID_GROUND(false);
		
		private TerrainType(boolean isPassable) {
			this.passable = isPassable;
		}
		
		public boolean isPassable() {
			return this.passable;
		}
		
		private final boolean passable;
	}
	
	
	public void endGame() {
		setGameOver(true);
	}

	public void adjustWindow() {
		if(getMyMazub() == null) return;
		int[] pos = getMyMazub().getPosition();
		if ((pos[0] - getWindowPosition()[0]) < 200) {
			if (canHaveAsWindowPosition(pos[0]-200, getWindowPosition()[1]))
				setWindowPosition(pos[0]-200, getWindowPosition()[1]);
			else
				setWindowPosition(0,getWindowPosition()[1]);
			
		}
		
		else if (((getWindowPosition()[0] + getWindowSize()[0]) - pos[0]) < 200+getMyMazub().getWidth()) {
			if (canHaveAsWindowPosition(pos[0]-getWindowSize()[0]+200+getMyMazub().getWidth(), getWindowPosition()[1]))
				setWindowPosition(pos[0]-getWindowSize()[0]+200+getMyMazub().getWidth(), getWindowPosition()[1]);
			else
				setWindowPosition(getXLimit() - getWindowSize()[0], getWindowPosition()[1]);
		}
		if ((pos[1] - getWindowPosition()[1]) < 200) {
			if (canHaveAsWindowPosition(getWindowPosition()[0], pos[1]-200))
				setWindowPosition(getWindowPosition()[0], pos[1]-200);
			else
				setWindowPosition(getWindowPosition()[0], 0);
		}
		
		else if (((getWindowPosition()[1] + getWindowSize()[1]) - pos[1]) < 200+getMyMazub().getHeight()) {
			if (canHaveAsWindowPosition(getWindowPosition()[0], pos[1]-getWindowSize()[1] + 200+getMyMazub().getHeight()))
				setWindowPosition(getWindowPosition()[0],  pos[1]-getWindowSize()[1] + 200+getMyMazub().getHeight());
			else
				setWindowPosition(getWindowPosition()[0], getYLimit() - getWindowSize()[1]);
		}
	}
	
	private int targetTileX;
	public int getTargetTileX() {
		return targetTileX;
	}
	public void setTargetTileX(int targetTileX) {
		this.targetTileX = targetTileX;
	}

	private int targetTileY;
	public int getTargetTileY() {
		return targetTileY;
	}
	public void setTargetTileY(int targetTileY) {
		this.targetTileY = targetTileY;
	}

	private boolean gameOver;

	public boolean isGameOver() {
		return gameOver;
	}

	private void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	private boolean didPlayerWin;

	public boolean getDidPlayerWin() {
		return didPlayerWin;
	}

	private void setDidPlayerWin(boolean didPlayerWin) {
		this.didPlayerWin = didPlayerWin;
	}
	
}
