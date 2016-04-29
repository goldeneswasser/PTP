package extendedMyDraw;
// This example is from _Java Examples in a Nutshell_. (http://www.oreilly.com)

// Copyright (c) 1997 by David Flanagan
// This example is provided WITHOUT ANY WARRANTY either expressed or implied.
// You may study, use, modify, and distribute it for non-commercial purposes.
// For any commercial use, see http://www.davidflanagan.com/javaexamples

import java.applet.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;

/** The application class. Processes high-level commands sent by GUI */
public class extendedMyDraw {
	/** main entry point. Just create an instance of this application class */
	public static void main(String[] args) {
		new extendedMyDraw();
	}

	/** Application constructor: create an instance of our GUI class */
	public extendedMyDraw() {
		window = new DrawGUI(this);
	}

	public JFrame window;

	/**
	 * This is the application method that processes commands sent by the GUI
	 */
	public void doCommand(String command) {
		if (command.equals("clear")) { // clear the GUI window
			// It would be more modular to include this functionality in the GUI
			// class itself. But for demonstration purposes, we do it here.
			window.getContentPane().repaint();
		} else if (command.equals("quit")) { // quit the application
			window.dispose(); // close the GUI
			System.exit(0); // and exit.
		}
	}
}

/** This class implements the GUI for our application */
class DrawGUI extends JFrame {
	extendedMyDraw app; // A reference to the application, to send commands to.
	Color color;
	JFrame ourJFrame;
	JTextField window_width;
	JTextField window_height;
	JTextField color_changer;
	JTextField upper_left_x;
	JTextField upper_left_y;
	JTextField lower_right_x;
	JTextField lower_right_y;
	HashMap<String, Color> _colorList;
	HashMap<Color, String> _colorListr;
	BufferedImage image;
	Graphics BufferedImageGraphics;
	

	/**
	 * The GUI constructor does all the work of creating the GUI and setting up
	 * event listeners. Note the use of local and anonymous classes.
	 */
	public DrawGUI(extendedMyDraw application) {
		super("Draw"); // Create the window
		app = application; // Remember the application reference
		color = Color.black; // the current drawing color
		ourJFrame = this;

		_colorList = new HashMap<String, Color>();
		_colorList.put("black", Color.black);
		_colorList.put("blue", Color.blue);
		_colorList.put("red", Color.red);
		_colorList.put("green", Color.green);

		_colorListr = new HashMap<Color, String>();
		_colorListr.put(Color.black, "black");
		_colorListr.put(Color.blue, "blue");
		_colorListr.put(Color.red, "red");
		_colorListr.put(Color.green, "green");

		this.getContentPane().setBackground(Color.white);
		// selector for drawing modes
		JComboBox shape_chooser = new JComboBox();
		shape_chooser.addItem("Scribble");
		shape_chooser.addItem("Rectangle");
		shape_chooser.addItem("Oval");

		// selector for drawing colors
		JComboBox color_chooser = new JComboBox();

		color_chooser.addItem("Black");
		color_chooser.addItem("Green");
		color_chooser.addItem("Red");
		color_chooser.addItem("Blue");

		window_width = new JTextField();
		window_width.setPreferredSize(new Dimension(50, window_width.getPreferredSize().height));
		window_height = new JTextField();
		window_height.setPreferredSize(new Dimension(50, window_height.getPreferredSize().height));

		JButton window_size = new JButton("Größe einstellen");
		JButton get_window_size = new JButton("Größe anzeigen");

		color_changer = new JTextField();
		color_changer.setPreferredSize(new Dimension(50, color_changer.getPreferredSize().height));

		JButton set_draw_color = new JButton("Zeichenfarbe ändern");
		JButton get_draw_color = new JButton("Zeichenfarbe anzeigen");

		JButton set_window_color = new JButton("Hintergrundfarbe ändern");
		JButton get_window_color = new JButton("Hintergrundfarbe anzeigen");

		upper_left_x = new JTextField();
		upper_left_x.setPreferredSize(new Dimension(40, upper_left_x.getPreferredSize().height));
		upper_left_y = new JTextField();
		upper_left_y.setPreferredSize(new Dimension(40, upper_left_y.getPreferredSize().height));
		lower_right_x = new JTextField();
		lower_right_x.setPreferredSize(new Dimension(40, lower_right_x.getPreferredSize().height));
		lower_right_y = new JTextField();
		lower_right_y.setPreferredSize(new Dimension(40, lower_right_y.getPreferredSize().height));

		JButton draw_rect = new JButton("Rechteck zeichnen");
		JButton draw_oval = new JButton("Oval zeichnen");
		JButton draw_poly = new JButton("Polygonzug zeichnen");

		// Create four buttons
		JButton auto = new JButton("Auto");
		JButton save = new JButton("Save");
		JButton clear = new JButton("Clear");
		JButton quit = new JButton("Quit");


		// Set a LayoutManager, and add the choosers and buttons to the window.
		this.getContentPane().setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		this.add(new JLabel("Shape:"));
		this.add(shape_chooser);
		this.add(new JLabel("Color:"));
		this.add(color_chooser);
		this.add(window_width);
		this.add(window_height);
		this.add(window_size);
		this.add(get_window_size);
		this.add(color_changer);
		this.add(set_draw_color);
		this.add(get_draw_color);
		this.add(set_window_color);
		this.add(get_window_color);
		this.add(upper_left_x);
		this.add(upper_left_y);
		this.add(lower_right_x);
		this.add(lower_right_y);
		this.add(draw_rect);
		this.add(draw_oval);
		this.add(draw_poly);
		this.add(auto);
		this.add(save);
		this.add(clear);
		this.add(quit);
		
		upper_left_x.setText("0");
		upper_left_y.setText("0");
		lower_right_x.setText("100");
		lower_right_y.setText("100");

		// Here's a local class used for action listeners for the buttons
		class DrawActionListener implements ActionListener {
			private String command;

			public DrawActionListener(String cmd) {
				command = cmd;
			}

			public void actionPerformed(ActionEvent e) {
				app.doCommand(command);
			}
		}

		// Define action listener adapters that connect the buttons to the app
		clear.addActionListener(new DrawActionListener("clear"));
		quit.addActionListener(new DrawActionListener("quit"));

		// this class determines how mouse events are to be interpreted,
		// depending on the shape mode currently set
		class ShapeManager implements ItemListener {
			DrawGUI gui;
			Graphics contentPaneGraphics;

			abstract class ShapeDrawer extends MouseAdapter implements MouseMotionListener {
				public void mouseMoved(MouseEvent e) {
					/* ignore */ }
			}

			// if this class is active, the mouse is interpreted as a pen
			class ScribbleDrawer extends ShapeDrawer {
				int lastx, lasty;

				public void mousePressed(MouseEvent e) {
					lastx = e.getX();
					lasty = e.getY();
				}

				public void mouseDragged(MouseEvent e) {
					Graphics g = gui.getGraphics();
					int x = e.getX(), y = e.getY();
					g.setColor(gui.color);
					BufferedImageGraphics.setColor(gui.color);
					g.setPaintMode();
					g.drawLine(lastx, lasty, x, y);
					BufferedImageGraphics.drawLine(lastx, lasty, x, y);
					lastx = x;
					lasty = y;
				}
				
				/**
				 * Zeichnet eine Linie entlang der Punkte
				 * @param points Eine Liste mit Koordinatenpunkten
				 */
				public void doDraw (List<Point> points) {
					Graphics g = contentPaneGraphics;
					lastx = points.get(0).x;
					lasty = points.get(0).y;
					g.setColor(gui.color);
					BufferedImageGraphics.setColor(gui.color);
					for(Point aktuellerPunkt : points) {
						BufferedImageGraphics.drawLine(lastx, lasty, aktuellerPunkt.x, aktuellerPunkt.y);
						g.drawLine(lastx, lasty, aktuellerPunkt.x, aktuellerPunkt.y);
						lastx = aktuellerPunkt.x;
						lasty = aktuellerPunkt.y;
					}
				}
			}

			// if this class is active, rectangles are drawn
			class RectangleDrawer extends ShapeDrawer {
				int pressx, pressy;
				int lastx = -1, lasty = -1;

				// mouse pressed => fix first corner of rectangle
				public void mousePressed(MouseEvent e) {
					pressx = e.getX();
					pressy = e.getY();
				}

				// mouse released => fix second corner of rectangle
				// and draw the resulting shape
				public void mouseReleased(MouseEvent e) {
					Graphics g = gui.getGraphics();
					if (lastx != -1) {
						// first undraw a rubber rect
						g.setXORMode(gui.color);
						g.setColor(gui.getBackground());
						BufferedImageGraphics.setColor(gui.getBackground());
						doDraw(pressx, pressy, lastx, lasty, g);
						lastx = -1;
						lasty = -1;
					}
					// these commands finish the rubberband mode
					g.setPaintMode();
					g.setColor(gui.color);
					// draw the finel rectangle
					doDraw(pressx, pressy, e.getX(), e.getY(), g);
				}

				// mouse released => temporarily set second corner of rectangle
				// draw the resulting shape in "rubber-band mode"
				public void mouseDragged(MouseEvent e) {
					Graphics g = gui.getGraphics();
					// these commands set the rubberband mode
					g.setXORMode(gui.color);
					g.setColor(gui.getBackground());
					BufferedImageGraphics.setColor(gui.getBackground());
					if (lastx != -1) {
						// first undraw previous rubber rect
						doDraw(pressx, pressy, lastx, lasty, g);

					}
					lastx = e.getX();
					lasty = e.getY();
					// draw new rubber rect
					doDraw(pressx, pressy, lastx, lasty, g);
				}

				public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
					// calculate upperleft and width/height of rectangle
					int x = Math.min(x0, x1);
					int y = Math.min(y0, y1);
					int w = Math.abs(x1 - x0);
					int h = Math.abs(y1 - y0);
					// draw rectangle
					g.setColor(gui.color);
					BufferedImageGraphics.setColor(gui.color);
					g.drawRect(x, y, w, h);
					BufferedImageGraphics.drawRect(x, y, w, h);
				}
			}

			// if this class is active, ovals are drawn
			class OvalDrawer extends RectangleDrawer {
				public void doDraw(int x0, int y0, int x1, int y1, Graphics g) {
					int x = Math.min(x0, x1);
					int y = Math.min(y0, y1);
					int w = Math.abs(x1 - x0);
					int h = Math.abs(y1 - y0);
					// draw oval instead of rectangle
					g.setColor(gui.color);
					BufferedImageGraphics.setColor(gui.color);
					g.drawOval(x, y, w, h);
					BufferedImageGraphics.drawOval(x, y, w, h);
				}
			}

			ScribbleDrawer scribbleDrawer = new ScribbleDrawer();
			RectangleDrawer rectDrawer = new RectangleDrawer();
			OvalDrawer ovalDrawer = new OvalDrawer();
			ShapeDrawer currentDrawer;

			public ShapeManager(DrawGUI itsGui) {
				gui = itsGui;
				contentPaneGraphics = gui.getContentPane().getGraphics();
				// default: scribble mode
				currentDrawer = scribbleDrawer;
				// activate scribble drawer
				gui.addMouseListener(currentDrawer);
				gui.addMouseMotionListener(currentDrawer);
			}

			// reset the shape drawer
			public void setCurrentDrawer(ShapeDrawer l) {
				if (currentDrawer == l)
					return;

				// deactivate previous drawer
				gui.removeMouseListener(currentDrawer);
				gui.removeMouseMotionListener(currentDrawer);
				// activate new drawer
				currentDrawer = l;
				gui.addMouseListener(currentDrawer);
				gui.addMouseMotionListener(currentDrawer);
			}

			// user selected new shape => reset the shape mode
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem().equals("Scribble")) {
					setCurrentDrawer(scribbleDrawer);
				} else if (e.getItem().equals("Rectangle")) {
					setCurrentDrawer(rectDrawer);
				} else if (e.getItem().equals("Oval")) {
					setCurrentDrawer(ovalDrawer);
				}
			}

			/**
			 * Zeichnet ein Rechteck
			 * @param upper_left Obere linke Koordinate
			 * @param lower_right Untere rechte Koordinate
			 */
			public void drawRectangle(Point upper_left, Point lower_right) {
				rectDrawer.doDraw((int) upper_left.getX(), (int) upper_left.getY(), (int) lower_right.getX(),
						(int) lower_right.getY(), contentPaneGraphics);
			}
			
			/**
			 * Zeichnet ein Oval
			 * @param upper_left Obere linke Koordinate
			 * @param lower_right Untere rechte Koordinate
			 */
			 public void drawOval(Point upper_left, Point lower_right) {
				 ovalDrawer.doDraw((int) upper_left.getX(), (int) upper_left.getY(), (int) lower_right.getX(),
							(int) lower_right.getY(), contentPaneGraphics);
			 }
			 
			 /**
			  * Zeichnet eine Linie anhand der angegebenen Punkte
			  * @param points EIne Liste der Koordinatenpunkte
			  */
			 public void drawPolyLine(List<Point> points) {
				scribbleDrawer.doDraw(points);
			 }
			 
			 /**
			  * Zeichnet eine Figur bestehend aus einer schwarzen Linie, einer grünen Linie, einem roten Oval und einem blauen Rechteck
			  */
			 public void autoDraw() {
					Color oldColor = color;
					
					Point upper_left = new Point();
					upper_left.setLocation(0,0);
					Point lower_right = new Point();
					lower_right.setLocation(111,111);
					List<Point> pointList = new ArrayList<Point>();
					pointList.add(lower_right);
					pointList.add(upper_left);
					
					color = Color.black;
					new ShapeManager((DrawGUI) ourJFrame).drawPolyLine(pointList);
					color = Color.red;
					new ShapeManager((DrawGUI) ourJFrame).drawOval(upper_left, lower_right);
					color = Color.blue;
					new ShapeManager((DrawGUI) ourJFrame).drawRectangle(upper_left, lower_right);
					color = Color.green;
					lower_right.setLocation(111,0);
					upper_left.setLocation(0, 111);
					new ShapeManager((DrawGUI) ourJFrame).drawPolyLine(pointList);
					
					color = oldColor;
			 }
			 
		}

		shape_chooser.addItemListener(new ShapeManager(this));

		class ColorItemListener implements ItemListener {

			// user selected new color => store new color in DrawGUI
			public void itemStateChanged(ItemEvent e) {
				if (e.getItem().equals("Black")) {
					color = Color.black;
				} else if (e.getItem().equals("Green")) {
					color = Color.green;
				} else if (e.getItem().equals("Red")) {
					color = Color.red;
				} else if (e.getItem().equals("Blue")) {
					color = Color.blue;
				}
			}
		}

		color_chooser.addItemListener(new ColorItemListener());

		// Handle the window close request similarly
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				app.doCommand("quit");
			} 

			
		});
		WindowAdapter wa =  new WindowAdapter(){
			public void onWindowResize (WindowEvent e) {
				System.out.println("hallo");
			}
		};
		
		ourJFrame.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				image = new BufferedImage(ourJFrame.getContentPane().getWidth(), ourJFrame.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
				BufferedImageGraphics = image.createGraphics();
				BufferedImageGraphics.fillRect(0, 0, ourJFrame.getContentPane().getWidth(), ourJFrame.getContentPane().getHeight());
				
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// Handle the window resizing
		class ButtonListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				if (e.getSource().equals(window_size)) {
					ourJFrame.getContentPane().setSize(Integer.parseInt(window_width.getText()),
							Integer.parseInt(window_height.getText()));
				}
				if (e.getSource().equals(get_window_size)) {
					window_width.setText("" + ourJFrame.getContentPane().getWidth());
					window_height.setText("" + ourJFrame.getContentPane().getHeight());
				}
				if (e.getSource().equals(set_draw_color)) {
					try {
						setFGColor(color_changer.getText().toLowerCase());
					} catch (ColorException e1) {

					}

				}
				if (e.getSource().equals(get_draw_color)) {
					color_changer.setText(_colorListr.get(color));
				}
				if (e.getSource().equals(set_window_color)) {
					try {
						setBGColor(color_changer.getText().toLowerCase());
					} catch (ColorException e1) {

					}
				}
				if (e.getSource().equals(get_window_color)) {
					if (ourJFrame.getContentPane().getBackground().equals(Color.white)) {
						color_changer.setText("white");
					} else {
						color_changer.setText(_colorListr.get(ourJFrame.getContentPane().getBackground()));
					}
				}
				if (e.getSource().equals(draw_rect)) {
					Point upper_left = new Point();
					upper_left.setLocation(Integer.parseInt(upper_left_x.getText()),Integer.parseInt(upper_left_y.getText()));
					Point lower_right = new Point();
					lower_right.setLocation(Integer.parseInt(lower_right_x.getText()),Integer.parseInt(lower_right_y.getText()));
					new ShapeManager((DrawGUI) ourJFrame).drawRectangle(upper_left, lower_right);
				}
				if (e.getSource().equals(draw_oval)) {
					Point upper_left = new Point();
					upper_left.setLocation(Integer.parseInt(upper_left_x.getText()),Integer.parseInt(upper_left_y.getText()));
					Point lower_right = new Point();
					lower_right.setLocation(Integer.parseInt(lower_right_x.getText()),Integer.parseInt(lower_right_y.getText()));
					new ShapeManager((DrawGUI) ourJFrame).drawOval(upper_left, lower_right);
				}
				if(e.getSource().equals(draw_poly)) {
					Point upper_left = new Point();
					upper_left.setLocation(Integer.parseInt(upper_left_x.getText()),Integer.parseInt(upper_left_y.getText()));
					Point lower_right = new Point();
					lower_right.setLocation(Integer.parseInt(lower_right_x.getText()),Integer.parseInt(lower_right_y.getText()));
					List<Point> pointList = new ArrayList<Point>();
					pointList.add(lower_right);
					pointList.add(upper_left);
					
					new ShapeManager((DrawGUI) ourJFrame).drawPolyLine(pointList);
				}
				if(e.getSource().equals(auto)) {
					new ShapeManager((DrawGUI) ourJFrame).autoDraw();
				}
				if (e.getSource().equals(save)) {
					try {
						writeImage(getDrawing(), "wunderschoen.bmp");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}


		}

		ButtonListener bL = new ButtonListener();
		window_size.addActionListener(bL);
		get_window_size.addActionListener(bL);
		set_draw_color.addActionListener(bL);
		get_draw_color.addActionListener(bL);
		set_window_color.addActionListener(bL);
		get_window_color.addActionListener(bL);
		draw_rect.addActionListener(bL);
		draw_oval.addActionListener(bL);
		draw_poly.addActionListener(bL);
		auto.addActionListener(bL);
		save.addActionListener(bL);

		// Finally, set the size of the window, and pop it up
		this.setSize(500, 400);
		this.setBackground(Color.white);
		this.show();
		
		image = new BufferedImage(ourJFrame.getContentPane().getWidth(), ourJFrame.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImageGraphics = image.createGraphics();
		BufferedImageGraphics.fillRect(0, 0, ourJFrame.getContentPane().getWidth(), ourJFrame.getContentPane().getHeight());
	}

	/**
	 * Setzt die Zeichenfarbe auf new_color
	 * @param new_color Die neue Zeichenfarbe
	 * @throws ColorException
	 */
	private void setFGColor(String new_color) throws ColorException {
		if (_colorList.containsKey(new_color)) {
			color = _colorList.get(new_color);
		} else {
			throw new ColorException();
		}
	}

	
	/**
	 * Setzt die Hintergrundfarbe auf new_color
	 * @param new_color Die neue Hintergrundfarbe
	 * @throws ColorException
	 */
	public void setBGColor(String new_color) throws ColorException {
		HashMap<String, Color> white = new HashMap();
		white.put("white", Color.white);
		if (_colorList.containsKey(new_color)) {
			ourJFrame.getContentPane().setBackground(_colorList.get(new_color));
		} else if (white.containsKey(new_color)) {
			ourJFrame.getContentPane().setBackground(Color.white);
		} else {
			throw new ColorException();
		}
	}

	class ColorException extends Exception {
		public ColorException() {
			System.out.println("Color nicht vorhanden.");
		}
	}
	
	 public Image getDrawing() throws IOException {
		 		 return image;
	 }
	 
	 public void write(Graphics g) {
		 g.drawImage(image, ourJFrame.getContentPane().getWidth(), ourJFrame.getContentPane().getHeight(), null);
	 }
	 
	 public void clear() {
		 app.doCommand("clear");
	 }
	 
	 public void writeImage(Image img, String filename)throws IOException {
		 mybmpfile bmp = new mybmpfile();
		 bmp.write(filename, img);
	 }
	 
	 public Image readImage(String filename) throws IOException {
		return null;
	 }

}