import java.awt.Color;
import java.awt.Graphics;

/**
 * The Class Block.
 * 
 * The class is for use with GenerationMain. It is intended to be used within a
 * 2d array, where each (x,y) index represents a 10px X 10px block with 4 walls.
 */
public class Block {

	/**
	 * The type determines the color of the block. 0 == black, 1 == white, 2 ==
	 * yellow, 3 == green
	 */
	private int type;
	
	/** Multiplier that determines size of blocks */
	private int mult;

	/** The x coordinate. */
	private int x;

	/** The y coordinate */
	private int y;

	/** True implied this block has been visited, false implies it hasn't. */
	private boolean visited;

	/**
	 * To represent the walls, I am using a string of length 4. The string will
	 * be populated with only binary numbers, where 1 means there is a wall and
	 * 0 means the wall has been broken. The first character represents North,
	 * and then runs clockwise from there.
	 * 
	 * For example: 1111 represents a block with all 4 walls. 0111 represents a
	 * block where the top wall has been broken. 0000 represents a block with no
	 * walls.
	 */
	private StringBuilder walls;

	/**
	 * Instantiates a new block.
	 * 
	 * Type is automatically set to 0, representing an untouched block that will
	 * be painted black. x and y coordinates are multiplied by some number passed in and increased
	 * by 50 for the purposes of painting the block. Visited is set to false and
	 * all walls are up.
	 * 
	 * @param xCord
	 *            the x cord
	 * @param yCord
	 *            the y cord
	 */
	public Block(int xCord, int yCord, int mult) {
		this.mult = mult;
		type = 0;
		x = mult * xCord + 50;
		y = mult * yCord + 50;
		visited = false;
		walls = new StringBuilder("1111");
	}

	/**
	 * Paints the block in based on type, location, and walls.
	 * 
	 * @param g
	 *            the g
	 */
	public void paintBlock(Graphics g) {
		switch (type) {
		case 0:
			g.setColor(Color.black);
			break;
		case 1:
			g.setColor(Color.WHITE);
			break;
		case 2:
			g.setColor(Color.YELLOW);
			break;
		case 3:
			g.setColor(Color.GREEN);
			break;
		}

		g.fillRect(x, y, mult, mult);

		for (int i = 0; i < 4; i++) {
			char c = walls.charAt(i);
			int ax, ay, bx, by;
			switch (i) {
			case 0:
				ax = x;
				ay = y;
				bx = x + mult;
				by = y;
				break;
			case 1:
				ax = x + mult;
				ay = y;
				bx = x + mult;
				by = y + mult;
				break;
			case 2:
				ax = x + mult;
				ay = y + mult;
				bx = x;
				by = y + mult;
				break;
			case 3:
				ax = x;
				ay = y;
				bx = x;
				by = y + mult;
				break;
			default:
				ax = 0;
				ay = 0;
				bx = 0;
				by = 0;
			}

			g.setColor(Color.BLACK);
			if (c == '1')
				g.drawLine(ax, ay, bx, by);
		}
	}

	/**
	 * Break north wall.
	 */
	public void breakNorthWall() {
		walls.setCharAt(0, '0');
	}

	/**
	 * Break east wall.
	 */
	public void breakEastWall() {
		walls.setCharAt(1, '0');
	}

	/**
	 * Break south wall.
	 */
	public void breakSouthWall() {
		walls.setCharAt(2, '0');
	}

	/**
	 * Break west wall.
	 */
	public void breakWestWall() {
		walls.setCharAt(3, '0');
	}

	/**
	 * Gets the north wall.
	 * 
	 * @return the north wall
	 */
	public char getNorthWall() {
		return walls.charAt(0);
	}

	/**
	 * Gets the east wall.
	 * 
	 * @return the east wall
	 */
	public char getEastWall() {
		return walls.charAt(1);
	}

	/**
	 * Gets the south wall.
	 * 
	 * @return the south wall
	 */
	public char getSouthWall() {
		return walls.charAt(2);
	}

	/**
	 * Gets the west wall.
	 * 
	 * @return the west wall
	 */
	public char getWestWall() {
		return walls.charAt(3);
	}

	/**
	 * Sets the type.
	 * 
	 * @param t
	 *            the new type
	 */
	public void setType(int t) {
		type = t;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the x.
	 * 
	 * @return the x
	 */
	public int getX() {
		return (x - 50) / mult;
	}

	/**
	 * Gets the y.
	 * 
	 * @return the y
	 */
	public int getY() {
		return (y - 50) / mult;
	}

	/**
	 * Gets the visited.
	 * 
	 * @return the visited
	 */
	public boolean getVisited() {
		return visited;
	}

	/**
	 * Sets the visited.
	 * 
	 * @param b
	 *            the new visited
	 */
	public void setVisited(boolean b) {
		visited = b;
	}
}
