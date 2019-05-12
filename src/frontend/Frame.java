package frontend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.soap.Node;

import backend.Coord;
import backend.Launch;

//test 4

/* The main graphics class for APathfinding. Controls the window,
 * and all path finding node graphics. Need to work on zoom function,
 * currently only zooms to top left corner rather than towards mouse
 * by Devon Crawford
 */
public class Frame extends JPanel
		implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

	ControlHandler ch;
	JFrame window;
	APathfinding pathfinding;
	boolean showSteps, btnHover;
	int size;
	double a1, a2;
	char currentKey = (char) 0;
	Coord start, end;
	String mode;
	
	Timer timer = new Timer(100, this);
	int r = randomWithRange(0, 255);
	int G = randomWithRange(0, 255);
	int b = randomWithRange(0, 255);

	public static void main(String[] args) {
		new Frame();
	}

	public Frame() {
		ch = new ControlHandler(this);
		size = 25;
		mode = "Map Creation";
		showSteps = true;
		btnHover = false;
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		// Set up pathfinding
		pathfinding = new APathfinding(this, size);
		
		// Calculating value of a in speed function 1
		a1 = (5000.0000 / (Math.pow(25.0000/5000, 1/49)));
		a2 = 625.0000;
		
		// Set up window
		window = new JFrame();
		window.setContentPane(this);
		window.setTitle("A* Pathfinding Visualization");
		window.getContentPane().setPreferredSize(new Dimension((Launch.largeur), (Launch.hauteur)));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		// Add all controls
		ch.addAll();
		
		this.revalidate();
		this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Grab dimensions of panel
		int height = getHeight();
		int width = getWidth();
		
		// If no path is found
		if (pathfinding.isNoPath()) {
			// Set timer for animation
			timer.setDelay(50);
			timer.start();

			// Set text of "run" button to "clear"
			ch.getB("run").setText("clear");
			
			// Set mode to "No Path"
			mode = "No Path";
			
			// Set up flicker animation
			Color flicker = new Color(r, G, b);
			g.setColor(flicker);
			g.fillRect(0, 0, getWidth(), getHeight());

			// Place "No Path" text on screen in center
			ch.noPathTBounds();
			ch.getL("noPathT").setVisible(true);
			this.add(ch.getL("noPathT"));
			this.revalidate();
		}

		// If pathfinding is complete (found path)
		if (pathfinding.isComplete()) {
			// Set run button to clear
			ch.getB("run").setText("clear");
			
			// Set timer delay, start for background animation
			timer.setDelay(50);
			timer.start();

			// Make the background flicker
			Color flicker = new Color(r, G, b);
			g.setColor(flicker);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			// Set completed mode
			if(showSteps) {
				mode = "Completed";
			}
			else {
				mode = "Completed in " + pathfinding.getRunTime() + "ms";
			}
		}

		// Draws grid
		g.setColor(Color.lightGray);
		for (int j = 0; j < this.getHeight(); j += size) {
			for (int i = 0; i < this.getWidth(); i += size) {
				g.drawRect(i, j, size, size);
			}
		}

		// Draws all borders
		g.setColor(Color.black);
		for (int i = 0; i < pathfinding.getBorderList().size(); i++) {
			g.fillRect(pathfinding.getBorderList().get(i).getX() + 1, pathfinding.getBorderList().get(i).getY() + 1,
					size - 1, size - 1);
		}

		// Draws start of path
		if (start != null) {
			g.setColor(Color.blue);
			g.fillRect(start.getX() + 1, start.getY() + 1, size - 1, size - 1);
		}
		// Draws end of path
		if (end != null) {
			g.setColor(Color.red);
			g.fillRect(end.getX() + 1, end.getY() + 1, size - 1, size - 1);
		}
		
		// If control panel is being hovered, change colours
		if(btnHover) {
			g.setColor(style.darkText);
			ch.hoverColour();
		}
		else {
			g.setColor(style.btnPanel);
			ch.nonHoverColour();
		}
		// Drawing control panel rectangle
		g.fillRect(10, height-96, 322, 90);

		// Setting mode text
		ch.getL("modeText").setText("Mode: " + mode);
		
		// Position all controls
		ch.position();
		
		// Setting numbers in pathfinding lists
		ch.getL("openC").setText(Integer.toString(pathfinding.getOpenList().size()));
		ch.getL("closedC").setText(Integer.toString(pathfinding.getClosedList().size()));
		ch.getL("pathC").setText(Integer.toString(pathfinding.getPathList().size()));
				
		// Setting speed number text in showSteps or !showSteps mode
		if(showSteps) {
			ch.getL("speedC").setText(Integer.toString(ch.getS("speed").getValue()));
		}
		else {
			ch.getL("speedC").setText("N/A");
		}
					
		// Getting values from checkboxes
		showSteps = ch.getC("showStepsCheck").isSelected();
		
		pathfinding.setTrig(ch.getC("trigCheck").isSelected());
	}

	public void MapCalculations(MouseEvent e) {
		// If left mouse button is clicked
		if (SwingUtilities.isLeftMouseButton(e)) {
			
			// If 's' is pressed create start node
			if (currentKey == 's') {
				int xRollover = e.getX() % size;
				int yRollover = e.getY() % size;

				if (start == null) {
					start = new Coord(e.getX() - xRollover, e.getY() - yRollover);
				} else {
					start.setX(e.getX() - xRollover);
					start.setY(e.getY() - yRollover);
					start.setStart();
				}
				repaint();
			} 
			// If 'e' is pressed create end node
			else if (currentKey == 'e') {
				int xRollover = e.getX() % size;
				int yRollover = e.getY() % size;

				if (end == null) {
					end = new Coord(e.getX() - xRollover, e.getY() - yRollover);
				} else {
					end.setX(e.getX() - xRollover);
					end.setY(e.getY() - yRollover);
					end.setEnd();
				}	
				repaint();
			} 
			// Otherwise, create a wall
			else {
				int xBorder = e.getX() - (e.getX() % size);
				int yBorder = e.getY() - (e.getY() % size);

				Node newBorder = new Node(xBorder, yBorder);
				pathfinding.addBorder(newBorder);

				repaint();
			}
		} 
		// If right mouse button is clicked
		else if (SwingUtilities.isRightMouseButton(e)) {
			int mouseBoxX = e.getX() - (e.getX() % size);
			int mouseBoxY = e.getY() - (e.getY() % size);

			// If 's' is pressed remove start node
			if (currentKey == 's') {
				if (start != null && mouseBoxX == start.getX() && start.getY() == mouseBoxY) {
					start = null;
					repaint();
				}
			} 
			// If 'e' is pressed remove end node
			else if (currentKey == 'e') {
				if (end != null && mouseBoxX == end.getX() && end.getY() == mouseBoxY) {
					end = null;
					repaint();
				}
			} 
			// Otherwise, remove wall
			else {
				int Location = pathfinding.searchBorder(mouseBoxX, mouseBoxY);
				if (Location != -1) {
					pathfinding.removeBorder(Location);
				}
				repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		MapCalculations(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {
		MapCalculations(e);
	}
	
	// Starts path finding
	void start() {
		if(start != null && end != null) {
			if (!showSteps) {
				pathfinding.start(start, end);
			} else {
				pathfinding.setup(start, end);
				setSpeed();
				timer.start();
			}
		}
		else {
			System.out.println("ERROR: Needs start and end points to run.");
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// Moves one step ahead in path finding (called on timer)
		if (pathfinding.isRunning() && showSteps) {
			pathfinding.findPath(pathfinding.getPar());
			mode = "Running";
		}
		// Finish pathfinding background flicker!
		if (pathfinding.isComplete() || pathfinding.isNoPath()) {
			r = (int) (Math.random() * ((r + 15) - (r - 15)) + (r - 15));
			G = (int) (Math.random() * ((G + 15) - (G - 15)) + (G - 15));
			b = (int) (Math.random() * ((b + 15) - (b - 15)) + (b - 15));
			
			if (r >= 240 | r <= 15) {
				r = randomWithRange(0, 255);
			}
			if (G >= 240 | G <= 15) {
				G = randomWithRange(0, 255);
			}
			if (b >= 240 | b <= 15) {
				b = randomWithRange(0, 255);
			}
		}
		
		// Actions of run/stop/clear button
		if(e.getActionCommand() != null) {
			if(e.getActionCommand().equals("run") && !pathfinding.isRunning()) {
				ch.getB("run").setText("stop");
				start();
			}
			else if(e.getActionCommand().equals("clear")) {
				ch.getB("run").setText("run");
				mode = "Map Creation";
				ch.getL("noPathT").setVisible(false);
				pathfinding.reset();
			}
			else if(e.getActionCommand().equals("stop")) {
				ch.getB("run").setText("start");
				timer.stop();
			}
			else if(e.getActionCommand().equals("start")) {
				ch.getB("run").setText("stop");
				timer.start();
			}
		}
		repaint();
	}
	
	// Returns random number between min and max
	int randomWithRange(int min, int max)
	{
	   int range = (max - min) + 1;     
	   return (int)(Math.random() * range) + min;
	}
	
	// Calculates delay with two exponential functions
	void setSpeed() {
		int delay = 0;
		int value = ch.getS("speed").getValue();
		
		if(value == 0) {
			timer.stop();
		}
		else if(value >= 1 && value < 50) {
			if(!timer.isRunning()) {
				timer.start();
			}
			// Exponential function. value(1) == delay(5000). value (50) == delay(25)
			delay = (int)(a1 * (Math.pow(25/5000.0000, value / 49.0000)));
		}
		else if(value >= 50 && value <= 100) {
			if(!timer.isRunning()) {
				timer.start();
			}
			// Exponential function. value (50) == delay(25). value(100) == delay(1).
			delay = (int)(a2 * (Math.pow(1/25.0000, value/50.0000)));
		}
		timer.setDelay(delay);
	}
	
	boolean showSteps() {
		return showSteps;
	}
}


