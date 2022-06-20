/*
* This GamePanel class is the main part of the game. It stores game data,
* displays game graphics, and handles the central logic of the game while 
* also using other classes to read in, process, and display various data.
*
* @author  Anthony Chen, Tony Zhang
* @version 1.0
* @since   2022-06-19
*/

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable, KeyListener { // make game threadable and detect keyboard input

	public Countries countries;
	public Graphics graphics;
	public Image image;
	public Menu menu;
	public Proximity squareCalculator;
	public String currentCountry;
	public String guess = "";
	public String textBox = "";
	public Thread gameThread;
	public double percent;
	public int guessesLeft = 6; // number of country guesses left
	public int needsReset = 1; // if this is 1, pick a different country
	public int readyCheck = 0; // has the player submitted their guess?
	public int score = 0; // how many successful guesses so far
	public int squareLocation = 0;

	public GamePanel() {
		this.setFocusable(true); // let the player focus the window
		this.addKeyListener(this); // add keystroke detection
		gameThread = new Thread(this);
		gameThread.start();
		countries = new Countries("src/database.txt"); // loade the countries
		menu = new Menu(180, 50); // create the menu
		this.setPreferredSize(new Dimension(512, 900)); // create window of specific size
	}

	public void paint(Graphics g) {
		image = createImage(512, 900); // create a blank canvas
		graphics = image.getGraphics();
		draw(graphics); // add game elements to the image
		g.drawImage(image, 0, 0, this);
	}

	public void draw(Graphics g) { // where a sizable portion of the game logic lives
		if (needsReset == 0 && guessesLeft > 0) { // if the guess wasn't correct and there are more guesses left
			if (readyCheck == 1) { // if the player presses enter, we read the guess
				guess = textBox;
				textBox = "";
				guessesLeft -= 1;
				readyCheck = 0;
				guess = countries.getCode(guess);
			}

			g.setColor(Color.black);	
			g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			FontMetrics metrics = g.getFontMetrics();
			if ((System.currentTimeMillis() / 1000) % 2 == 1) { // this shows and hides the >, creating a blink effect
				g.drawString("  " + textBox, (512 - metrics.stringWidth("  " + textBox)) / 2, 700);
			} else {
				g.drawString("> " + textBox, (512 - metrics.stringWidth("> " + textBox)) / 2, 700);
			}

			if (countries.getName(guess).equals(countries.getName(currentCountry))) { // the player won, so we increment the score and mark the game as needing a reset
				score += 1;
				needsReset = 1;
			} else if (guessesLeft != 6) { // if this isn't the first guess
				if (guess.equals("No Country Found")) { // if the guess is invalid we just show that the guess is invalid
					g.setColor(Color.gray);	
					g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					metrics = g.getFontMetrics();
					g.drawString("Invalid Country", (512 - metrics.stringWidth("Invalid Country")) / 2, 550);
					g.drawString(guessesLeft + " Guesses Left", (512 - metrics.stringWidth(guessesLeft + " Guesses Left")) / 2, 600);
				} else { // but if the guess is a real country (but incorrect) we show how far off it was
					int distance = findDistance(countries.getLat(guess), countries.getLon(guess), countries.getLat(currentCountry), countries.getLon(currentCountry), 0, 0); // distance to the country
					percent = squareCalculator.PercentProximity(findDistance(countries.getLat(guess), countries.getLon(guess), countries.getLat(currentCountry), countries.getLon(currentCountry), 0, 0)); // percent of the way there
					squareCalculator.SquareProgress(percent); // calculate number of each colour of square
					
					squareLocation = 31;
					for (int i = 0; i < squareCalculator.greenSquares; i+= 1) { // draw green, yellow, and white (grey) squares in that order
						g.setColor(Color.green);
						g.fillRect(squareLocation, 450, 50, 50);
						squareLocation += 100;
					}
					for (int i = 0; i < squareCalculator.yellowSquares; i+= 1) {
						g.setColor(Color.yellow);
						g.fillRect(squareLocation, 450, 50, 50);
						squareLocation += 100;
					}
					for (int i = 0; i < squareCalculator.whiteSquares; i+= 1) {
						g.setColor(Color.gray);
						g.fillRect(squareLocation, 450, 50, 50);
						squareLocation += 100;
						}

					g.setColor(Color.gray);	 // show the information and the number of guesses left on the screen
					g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					metrics = g.getFontMetrics();
					g.drawString("Guess: " + countries.getName(guess), (512 - metrics.stringWidth("Guess: " + countries.getName(guess))) / 2, 550);	
					String message = distance + " km (" + (int) percent + "%)";
					g.drawString(message, (512 - metrics.stringWidth(message)) / 2, 600);
					g.drawString(guessesLeft + " Guesses Left", (512 - metrics.stringWidth(guessesLeft + " Guesses Left")) / 2, 650);
					paintComponent(g, getBearing(countries.getLat(guess),countries.getLon(guess),countries.getLat(currentCountry), countries.getLon(currentCountry)), 1); // draw the arrow on the screen pointing 
				}
			}
			g.drawString("Score: " + score, (512 - metrics.stringWidth("Score: " + score)) / 2, 850); // show the score at the bottom
		}
		if (needsReset == 1 || guessesLeft == 0) { // if the player wins or we run out of guesses 
			currentCountry = countries.getRandomCountry(); // get a new country 
			System.out.println("DEBUG ANSWER " + countries.getName(currentCountry)); // print the answer
			needsReset = 0;
			guessesLeft = 6;
		}
		g.setColor(Color.black);
		g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(countries.getName(currentCountry).replaceAll("[a-zA-Z]", "_ ").trim(), (512 - metrics.stringWidth(countries.getName(currentCountry).replaceAll("[a-zA-Z]", "_ ").trim())) / 2, 128); // display the country name but with letters replaced with underscores, hyphens and special characters are left in to alert players to the fact that they are there; for example, the official english name of the ivory coast is the côte d'ivoire

		try { // draw the image of the current country
			BufferedImage image = ImageIO.read(getClass().getResource("/img/" + currentCountry + ".png"));
			g.drawImage(image, 128, 180, null);
		} catch (Exception e) { // print out any errors for debugging
			System.out.println(e);
		}
		menu.draw(g); // draw the title "VORLDLE"
	}

	public void run() { // almost untouched from mr anthonys example code
		// the CPU runs our game code too quickly - we need to slow it down! The
		// following lines of code "force" the computer to get stuck in a loop for short
		// intervals between calling other methods to update the screen.
		long lastTime = System.nanoTime();
		double amountOfTicks = 60;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long now;

		while (true) { // this is the infinite game loop
			now = System.nanoTime();
			delta = delta + (now - lastTime) / ns;
			lastTime = now;

			// only move objects around and update screen if enough time has passed
			if (delta >= 1) {
				repaint();
				delta--;
			}
		}
	}

	public static int findDistance(double lat1, double lon1, double lat2, double lon2, double el1, double el2) { // find distance between two points on earth
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;
		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);
		return (int) Math.round(Math.sqrt(distance)); // returns the distance between the points, taking curvature and elevation into account
	}

	public static double getBearing(double lat1, double lon1, double lat2, double lon2) { // get the bearing (direction) from one country to another
		double x, y, bearingrad, bearingdeg;

		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		x = Math.cos(lat2) * Math.sin(lon2 - lon1);
		y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
		bearingrad = Math.atan2(x, y);
		bearingdeg = Math.toDegrees(bearingrad);

		if (bearingdeg < 0) { // make this always return a number from 0 to 360; 0 is north, 90 is east, 180 is south, and 270 is west
			bearingdeg += 360;
		}
		return bearingdeg;
	}
	
	public void paintComponent(Graphics g, double degree, double scaling) { // paints an image and rotates it
		BufferedImage Arrow = LoadImage("src/arrow.png"); // loads the current image (an arrow)
		AffineTransform at = AffineTransform.getTranslateInstance(200, 700); // change the position if needed
		at.rotate(Math.toRadians(degree), Arrow.getWidth() / 2, Arrow.getHeight() / 2); // rotate it for the number of degrees
		at.scale(scaling, scaling); // scale it up or down

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(Arrow, at, null);
		repaint();
	}

	BufferedImage LoadImage(String FileName) { // loads an image from the disk
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(FileName));
		} catch (IOException e) { // prints any errors for easy debugging
			System.out.println(e);
		}
		return img;
	}

	public void keyPressed (KeyEvent e){ // detects a key being pressed
		if (e.getKeyCode() == KeyEvent.VK_ENTER) { // submit a guess
			readyCheck = 1;
			return;
		} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && textBox.length() > 0) { // delete characters on backspace
			textBox = textBox.substring(0, textBox.length() - 1); 
		} else if (e.getKeyChar() == KeyEvent.VK_DELETE) { // exit program on delete key
			System.exit(0);
		}
		if (isPrintableChar(e.getKeyChar())) { // add printable characters to the current invisible text box
			textBox = textBox + e.getKeyChar();
		}
  	}

	public void keyTyped(KeyEvent e) { // method stub because we are required to implement this method
	
	}

	public void keyReleased(KeyEvent e) { // method stub because we are required to implement this method

	}

	public boolean isPrintableChar (char c) { // check if a certain character is printable
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c); // get the block of the character
        return (!Character.isISOControl(c)) && // check if the character is a control character, is undefined, is null, or is a special character
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null && block != Character.UnicodeBlock.SPECIALS;
    }
}
