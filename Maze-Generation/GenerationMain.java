import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;

/**
 * The Class GenerationMain.
 * 
 * Created by: Kevin Smith
 * 
 * Class: Georgia Institute of Technology CS 1332, Data Structures and
 * Algorithms Fall Semester, 2013
 * 
 * This application gives the user several different algorithms to create mazes,
 * with each algorithm offering a different style. It also includes a solve
 * method, using a depth first search approach. Do not try to run a generation
 * algorithm after another one without resetting first.
 * 
 * Note: I provide a link with a more thorough explanation as well as another
 * example for several of the algorithms. Rather than studying his code in
 * depth, I only read the general ideas and then implemented my own version. In
 * other words, his examples may or may not be coded the same way as mine, BUT
 * they should all follow the same general ideas. This should be taken into
 * account when reading through his explanations/code.
 * 
 */
@SuppressWarnings("serial")
public class GenerationMain extends JFrame implements Runnable {
	/** The image. Used for double buffering. */
	private Image image;

	/** The second. Used for double buffering. */
	private Graphics second;

	/** The maze. */
	private Block[][] maze;

	/** Determines the size of the blocks */
	private int multiplier;

	/** The number of rows within the maze. */
	private int rows;

	/** The number of columns within the maze. */
	private int cols;

	/** The height of the JFrame. */
	private int height;

	/** The width of the JFrame. */
	private int width;

	/** The random start. */
	private Block randomStart;

	/** The start. */
	private Block start;

	/** The end. */
	private Block end;

	/** The speed. The lower the integer, the higher the speed. */
	private int speed;

	/** Boolean used to start the solve algorithm within the run method. */
	private boolean solve;

	/** Boolean used to start the depth first algorithm within the run method. */
	private boolean depthFirst;

	/**
	 * Boolean used to start the randomized prim's algorithm within the run
	 * method.
	 */
	private boolean prims;

	/**
	 * Boolean used to start the hunt and search algorithm within the run
	 * method.
	 */
	private boolean hunt;

	/** Boolean used to start the binary tree algorithm within the run method. */
	private boolean binary;

	/** Boolean used to start the eller's algorithm within the run method. */
	private boolean ellers;

	/**
	 * Instantiates a new generation main.
	 */
	public GenerationMain() {
		// jframe setups and keylistener
		width = 700;
		height = 810;
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new MyKeyListener());

		// maze setups
		rows = 30;
		cols = 30;
		maze = new Block[rows][cols];
		speed = 50;
		reset();

		setVisible(true);

		// Run the program
		new Thread(this).start();
	}

	/**
	 * Reset.
	 * 
	 * Completely sets the maze back to a blank slate.
	 */
	public void reset() {
		updateSizes();
		maze = new Block[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Block temp = new Block(j, i, multiplier);
				maze[i][j] = temp;
			}
		}
		randomStart = maze[(int) (Math.random() * (rows - 2))][(int) (Math
				.random() * (cols - 2))];
		start = maze[0][0];
		end = maze[rows - 1][cols - 1];
		solve = false;
		depthFirst = false;
		prims = false;
		hunt = false;
		binary = false;
		ellers = false;
	}

	/**
	 * Updates the multiplier whenever called based on how many rows/cols there
	 * are.
	 */
	private void updateSizes() {
		if (rows >= 55 || cols >= 55)
			multiplier = 10;
		else if (rows >= 45 || cols >= 45)
			multiplier = 12;
		else if (rows >= 35 || cols >= 35)
			multiplier = 15;
		else if (rows >= 25 || cols >= 25)
			multiplier = 20;
		else if (rows >= 15 || cols >= 15)
			multiplier = 30;
		else if (rows >= 10 || cols >= 10)
			multiplier = 60;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		// Double buffering method found at:
		// http://www.youtube.com/watch?v=4T3WJEH7zrc
		image = createImage(getWidth(), getHeight());
		second = image.getGraphics();
		paintComponent(second);
		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Paint component.
	 * 
	 * @param g
	 *            the g
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Slower: UP ARROW", 50, 670);
		g.drawString("Slow: LEFT ARROW", 50, 700);
		g.drawString("Medium: DOWN ARROW", 50, 730);
		g.drawString("Fast: RIGHT ARROW", 50, 760);
		g.drawString("Depth First Maze: 1", 250, 670);
		g.drawString("Randomized Prim: 2", 250, 700);
		g.drawString("Hunt & Kill: 3", 250, 730);
		g.drawString("Binary: 4", 250, 760);
		g.drawString("Eller's: 5", 250, 790);
		g.drawString("Reset Maze: SPACE", 450, 670);
		g.drawString("Solve Maze: SHIFT", 450, 700);
		g.drawString("+5 Rows/Cols: D / S", 450, 730);
		g.drawString("-5 Rows/Cols: A / W", 450, 760);

		// Loops through the maze iteratively to paint every block.
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Block temp = maze[i][j];
				temp.paintBlock(g);
			}
		}
	}

	/**
	 * Paint With Delay. Delay is determined by the speed, which can be set
	 * using key events down below.
	 * 
	 * @param g
	 */
	public void paintWithDelay() {
		repaint();
		try {
			Thread.sleep(speed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		/*
		 * Loops until the program is terminated. I'm not sure why I need that
		 * repaint there, but the program won't work without it. It just won't
		 * do anything. Anyways, we have if statements that lead to each of our
		 * algorithms. The booleans are controlled by key events down below, and
		 * when they are done they set their respective values back to false and
		 * I can call reset and another algorithm, or solve the maze if I wish.
		 */
		while (true) {
			repaint();
			if (depthFirst) {
				depthFirstMaze();
			}

			if (prims) {
				randomizedPrim();
			}

			if (hunt) {
				huntAndKill();
			}

			if (binary) {
				binaryTree();
			}

			if (ellers) {
				ellers();
			}

			if (solve) {
				solve();
			}
		}
	}

	/**
	 * Solve.
	 * 
	 * This method uses a depth first approach, using stacks. 1. Choose a random
	 * starting point, and push it onto the stack. 2. Find an unvisited node,
	 * and push that onto the stack. Make this node your current node. 3. Repeat
	 * step 2 until you can find no more unvisited node. Pop from the stack and
	 * update current until you can. 4. Repeat steps 2-3 until you get to the
	 * end of the maze.
	 */
	private void solve() {
		Block current = start;
		Block next;
		Stack<Block> blockStack = new Stack<Block>();

		current.setType(2);
		blockStack.push(current);
		resetVisited();
		while (current != end) {
			next = getNext(current);
			if (next != null) {
				next.setVisited(true);
				blockStack.push(current);
				current = next;
				current.setType(3);
			} else {
				current.setType(1);
				current = blockStack.pop();
			}
			paintWithDelay();
		}

		solve = false;
	}

	/**
	 * Reset visited.
	 * 
	 * Simply iterates through the entire maze setting every node to unvisited.
	 * This is used with the solve method, so that we can go all the way through
	 * a maze without resetting it entirely.
	 */
	private void resetVisited() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Block temp = maze[i][j];
				temp.setVisited(false);
			}
		}
	}

	/**
	 * Gets the next.
	 * 
	 * Used with the solve method. Looks right first, then goes around clockwise
	 * searching for unvisited blocks.
	 * 
	 * @param current
	 *            the current
	 * @return the next
	 */
	private Block getNext(Block current) {
		if (current.getEastWall() == '0' && lookRight(current) != null
				&& !lookRight(current).getVisited())
			return lookRight(current);
		else if (current.getSouthWall() == '0' && lookDown(current) != null
				&& !lookDown(current).getVisited())
			return lookDown(current);
		else if (current.getWestWall() == '0' && lookLeft(current) != null
				&& !lookLeft(current).getVisited())
			return lookLeft(current);
		else if (current.getNorthWall() == '0' && lookUp(current) != null
				&& !lookUp(current).getVisited())
			return lookUp(current);
		else
			return null;
	}

	/**
	 * Depth first maze.
	 * 
	 * Similar to the solve method, this algorithm uses a stack to create a
	 * random maze. 1. Choose a random starting point, and set that to current.
	 * Push current onto the stack. 2. Find a random, unvisited node. Set it to
	 * current, push it onto the stack. Remember to update visited and type. 3.
	 * Repeat step 2 until you can't find an unvisited node, the pop from stack
	 * and update current until you can. 4. Repeat steps 2-3 until the stack is
	 * empty, in which case you've visited every single block.
	 * 
	 * A more thorough explanation can be found here:
	 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
	 */
	private void depthFirstMaze() {
		Block current = randomStart;
		Block unvisited;
		int random;
		Stack<Block> blockStack = new Stack<Block>();

		blockStack.push(current);
		while (!blockStack.isEmpty()) {
			random = (int) (Math.random() * 4);
			unvisited = findUnvisited(current, random);

			if (unvisited != null) {
				unvisited.setVisited(true);
				unvisited.setType(2);
				current.setType(1);
				blockStack.push(current);
				current = unvisited;
			} else {
				current.setType(1);
				current = blockStack.pop();
				current.setType(2);
			}
			paintWithDelay();
		}

		depthFirst = false;
	}

	/**
	 * Randomized Prim's.
	 * 
	 * This algorithm is a variation on prims algorithm. Every neighbor is added
	 * to a set, and then a block is randomly (as opposed to by weight, as it
	 * would be on a graph) chosen from that set and connected to an already
	 * visited cell. 1. Add a random cell to the frontier, in our case a linked
	 * list. 2. Pick a random cell out of the frontier (the first one will be
	 * the cell you chose in step 1, of course.) 3. Find an unvisited cell, add
	 * it to the frontier. 4. Repeat steps 2-3 until the frontier is empty.
	 * 
	 * A more thorough explanation can be found here:
	 * http://weblog.jamisbuck.org/2011/1/10/maze-generation-prim-s-algorithm
	 */
	private void randomizedPrim() {
		Block current = randomStart;
		Block unvisited;
		LinkedList<Block> frontier = new LinkedList<Block>();
		int chooser;
		int random;

		frontier.add(current);
		while (!frontier.isEmpty()) {
			chooser = (int) (Math.random() * (frontier.size() - 1));
			current = frontier.get(chooser);
			random = (int) (Math.random() * 4);
			unvisited = findUnvisited(current, random);

			if (unvisited != null) {
				unvisited.setType(2);
				unvisited.setVisited(true);
				frontier.add(unvisited);
			} else {
				current.setType(1);
				frontier.remove(current);
			}
			paintWithDelay();
		}

		prims = false;
	}

	/**
	 * Hunt and Kill.
	 * 
	 * This one works similar to the depth first variation. You carve a route
	 * randomly through the maze until you find yourself surrounded by visited
	 * nodes. Instead of backtracking, you iterate through the maze from top to
	 * bottom looking for unvisited nodes. 1. Pick a random block for your
	 * starting position. 2. While you have unvisited neighbors, randomly pick
	 * one and keep going. 3. When you find yourself stuck, set current to top
	 * left corner. 4. Iterate through the maze until you find an unvisited
	 * block. 5. Repeat steps 2-4 until every block has been visited.
	 * 
	 * A more thorough explanation can be found here:
	 * http://weblog.jamisbuck.org/2011/1/24/maze-generation-hunt-and-kill-algorithm
	 */
	private void huntAndKill() {
		Block current = randomStart;
		Block unvisited;

		while (current != null) {
			current.setType(1);
			current.setVisited(true);
			unvisited = findUnvisited(current, (int) (Math.random() * 4));
			if (unvisited != null)
				current = unvisited;
			else
				current = huntMaze();
			if (current != null)
				current.setType(2);
			paintWithDelay();
		}

		hunt = false;
	}

	/**
	 * Hunt maze.
	 * 
	 * Helper method for Hunt and Kill. Iterates through looking for unvisited
	 * blocks, then returns that block. If there is no unvisited block, returns
	 * null.
	 * 
	 * @return the block
	 */
	private Block huntMaze() {
		Block hunter = maze[0][0];
		Block unvisited;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				hunter = maze[i][j];
				if (hunter.getVisited()) {
					unvisited = findUnvisited(hunter, (int) (Math.random() * 4));
					if (unvisited != null)
						return unvisited;
				}

			}
		}

		return null;
	}

	/**
	 * Find unvisited.
	 * 
	 * Helper method I originally created for my depth first maze, and ended up
	 * incorporating into some of the others. Uses a random int to search
	 * through its neighbors until it finds an unvisited neighbor block. Returns
	 * null if every block has been visited.
	 * 
	 * @param current
	 *            the current
	 * @param random
	 *            the random
	 * @return the block
	 */
	private Block findUnvisited(Block current, int random) {
		Block wall = new Block(-10, -10, multiplier);
		wall.setVisited(true);

		switch (random) {
		case 0:
			if (lookRight(current) != null)
				wall = lookRight(current);
			if (!wall.getVisited()) {
				current.breakEastWall();
				wall.breakWestWall();
			}
			break;
		case 1:
			if (lookDown(current) != null)
				wall = lookDown(current);
			if (!wall.getVisited()) {
				current.breakSouthWall();
				wall.breakNorthWall();
			}
			break;
		case 2:
			if (lookLeft(current) != null)
				wall = lookLeft(current);
			if (!wall.getVisited()) {
				current.breakWestWall();
				wall.breakEastWall();
			}
			break;
		case 3:
			if (lookUp(current) != null)
				wall = lookUp(current);
			if (!wall.getVisited()) {
				current.breakNorthWall();
				wall.breakSouthWall();
			}
			break;
		}
		if ((lookRight(current) == null || lookRight(current).getVisited())
				&& (lookLeft(current) == null || lookLeft(current).getVisited())
				&& (lookUp(current) == null || lookUp(current).getVisited())
				&& (lookDown(current) == null || lookDown(current).getVisited()))
			return null;
		else if (!wall.getVisited())
			return wall;
		else
			return findUnvisited(current, (int) (Math.random() * 4));
	}

	/**
	 * Binary tree.
	 * 
	 * This one is incredibly simple. Just iterate through the entire maze,
	 * randomly carving passages north or west. 1. Start at the top left. 2.
	 * Carve a passage north or west. 3. Iterate through the rest of the maze,
	 * repeating steps 2. 4. Touch up the top row and left column.
	 * 
	 * Note: Originally I was touching up the top row and left columns during
	 * the main double for loop. This was creating problems with my solve
	 * method, I believe it wasn't breaking all the walls appropriately and
	 * solve couldn't find its way to the end, resulting in a empty stack
	 * exception. I ended up just adding the two extra for loops at the end to
	 * ensure everything was working correctly.
	 * 
	 * More information can be found here:
	 * http://weblog.jamisbuck.org/2011/2/1/maze-generation-binary-tree-algorithm
	 */
	private void binaryTree() {
		Block current;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int random = (int) (Math.random() * 2);
				current = maze[i][j];
				if (random == 0 && lookUp(current) != null) {
					current.breakNorthWall();
					lookUp(current).breakSouthWall();
				} else if (random == 1 && lookLeft(current) != null) {
					current.breakWestWall();
					lookLeft(current).breakEastWall();
				}
				current.setType(2);
				paintWithDelay();
				current.setType(1);
			}
		}
		// Two loops to touch up the top row and left column.
		for (int i = 0; i < cols; i++) {
			current = maze[0][i];
			current.setType(2);
			if (lookRight(current) != null)
				current.breakEastWall();
			if (lookLeft(current) != null)
				current.breakWestWall();
			paintWithDelay();
			current.setType(1);
		}
		for (int i = 0; i < rows; i++) {
			current = maze[i][0];
			current.setType(2);
			if (lookUp(current) != null)
				current.breakNorthWall();
			if (lookDown(current) != null)
				current.breakSouthWall();
			paintWithDelay();
			current.setType(1);
		}

		binary = false;
	}

	/**
	 * For this one, I wrote my own Node & Disjoint Set class. You iterate
	 * through the maze, randomly carving passages East or South or both and
	 * union-ing them into the same set while doing it. The important thing is
	 * to only carve passages east if they are in the same set. When you get to
	 * the bottom, carve a passage to the east for all blocks that aren't in the
	 * same set. 1. Put every block into a list, and then create a disjoint set
	 * passing that list in. 2. Start at the top left. If the next cell is not
	 * in the same set, randomly decide to break right. 3. Randomly decide to
	 * break down. Every set of cells must break down at least once. 4. Repeat
	 * steps 2 - 3 for the entire maze. The last row must break right if two
	 * blocks aren't in the same set.
	 * 
	 * Note: Because of the way I handled breaking south, I believe my
	 * implementation may be slightly off from the one linked to below. For
	 * example, I sometimes force it to break south if the next block is in a
	 * different set even though a block later on could potentially be in the
	 * same set, giving you another chance to break south. In other words, It
	 * may sometimes break south even if it didn't have to.
	 * 
	 * More information can be found here:
	 * http://weblog.jamisbuck.org/2010/12/29/maze-generation-eller-s-algorithm
	 */
	private void ellers() {
		int adjacent;
		int vertical;
		LinkedList<Block> list = new LinkedList<Block>();
		Block curr;
		Block next;
		Block down;
		boolean goneDown = false;

		// Setting up my disjoint set
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				list.add(maze[i][j]);
			}
		}
		DisjointSet<Block> ds = new DisjointSet<Block>(list);

		// Iterating through the maze
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				adjacent = (int) (Math.random() * 2);
				vertical = (int) (Math.random() * 2);
				curr = maze[i][j];

				// handles break right
				curr.setType(2);
				if (j < cols - 1) {
					next = maze[i][j + 1];
					if ((i == rows - 1 || adjacent == 0)
							&& ds.find(curr) != ds.find(next)) {
						ds.union(curr, next);
						curr.breakEastWall();
						next.breakWestWall();
					}
				}
				// handles breaking south
				if (i < rows - 1) {
					down = maze[i + 1][j];
					if (vertical == 0) {
						ds.union(curr, down);
						curr.breakSouthWall();
						down.breakNorthWall();
						goneDown = true;
					}
					if ((j < cols - 1 && ds.find(curr) != ds.find(maze[i][j + 1])) || j == cols - 1) {
						if (!goneDown) {
							ds.union(curr, down);
							curr.breakSouthWall();
							down.breakNorthWall();
						}
						goneDown = false;
					}
				}

				paintWithDelay();
				curr.setType(1);
			}
		}

		ellers = false;
	}

	/**
	 * Look right.
	 * 
	 * @param curr
	 *            the curr
	 * @return the block
	 */
	public Block lookRight(Block curr) {
		if (curr.getX() + 1 > (cols - 1))
			return null;
		else
			return maze[curr.getY()][curr.getX() + 1];
	}

	/**
	 * Look left.
	 * 
	 * @param curr
	 *            the curr
	 * @return the block
	 */
	public Block lookLeft(Block curr) {
		if (curr.getX() - 1 < 0)
			return null;
		else
			return maze[curr.getY()][curr.getX() - 1];
	}

	/**
	 * Look up.
	 * 
	 * @param curr
	 *            the curr
	 * @return the block
	 */
	public Block lookUp(Block curr) {
		if (curr.getY() - 1 < 0)
			return null;
		else
			return maze[curr.getY() - 1][curr.getX()];
	}

	/**
	 * Look down.
	 * 
	 * @param curr
	 *            the curr
	 * @return the block
	 */
	public Block lookDown(Block curr) {
		if (curr.getY() + 1 > (rows - 1))
			return null;
		else
			return maze[curr.getY() + 1][curr.getX()];
	}

	/**
	 * The listener interface for receiving myKey events. The class that is
	 * interested in processing a myKey event implements this interface, and the
	 * object created with that class is registered with a component using the
	 * component's <code>addMyKeyListener<code> method. When
	 * the myKey event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see MyKeyEvent
	 */
	private class MyKeyListener implements KeyListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP)
				speed = 100;
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
				speed = 50;
			if (e.getKeyCode() == KeyEvent.VK_DOWN)
				speed = 25;
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				speed = 1;

			if (e.getKeyCode() == KeyEvent.VK_S && (rows + 5) <= 60
					&& !isRunning()) {
				rows += 5;
				reset();
			}
			if (e.getKeyCode() == KeyEvent.VK_W && (rows - 5) > 5
					&& !isRunning()) {
				rows -= 5;
				reset();
			}
			if (e.getKeyCode() == KeyEvent.VK_D && (cols + 5) <= 60
					&& !isRunning()) {
				cols += 5;
				reset();
			}
			if (e.getKeyCode() == KeyEvent.VK_A && (cols - 5) > 5
					&& !isRunning()) {
				cols -= 5;
				reset();
			}

			if (e.getKeyCode() == KeyEvent.VK_SHIFT && !isRunning())
				solve = true;
			if (e.getKeyCode() == KeyEvent.VK_1 && !isRunning())
				depthFirst = true;
			if (e.getKeyCode() == KeyEvent.VK_2 && !isRunning())
				prims = true;
			if (e.getKeyCode() == KeyEvent.VK_3 && !isRunning())
				hunt = true;
			if (e.getKeyCode() == KeyEvent.VK_4 && !isRunning())
				binary = true;
			if (e.getKeyCode() == KeyEvent.VK_5 && !isRunning())
				ellers = true;

			if (e.getKeyCode() == KeyEvent.VK_SPACE && !isRunning())
				reset();
		}

		/**
		 * Check to maker sure no algorithms are running. Used during keystrokes
		 * to ensure I don't try to run two algorithms at one time.
		 */
		private boolean isRunning() {
			return (solve || depthFirst || prims || hunt || binary || ellers);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
		 */
		public void keyReleased(KeyEvent e) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
		 */
		public void keyTyped(KeyEvent e) {

		}
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		new GenerationMain();
	}
}