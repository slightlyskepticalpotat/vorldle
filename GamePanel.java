import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Countries countries;
	public int needsReset = 1;
	public String currentCountry;
	public Menu menu;
	public Proximity squares;
	public String guess = "";
	public Scanner scan = new Scanner(System.in);
	public int score = 0;
	public int guessesLeft = 6;
	public Proximity squareCalculator;
	public double percent;
	public int squareLocation = 0;
	public String textBox = "";
	public int readyCheck = 0;
	public Bearing direction;

	public GamePanel() {
		this.setFocusable(true);
		this.addKeyListener(this);
		gameThread = new Thread(this);
		gameThread.start();
		countries = new Countries("src/database.txt");
		menu = new Menu(180, 50);
		// MenuButton();
		this.setPreferredSize(new Dimension(512, 900));
	}

	public void paint(Graphics g) {

		image = createImage(512, 900);
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}

	public void draw(Graphics g) {
		// DrawArrow.draw();
		// Countries.draw();
		if (needsReset == 0 && guessesLeft > 0) {
			if (readyCheck == 1) {
				guess = textBox;
				textBox = "";
				guessesLeft -= 1;
				readyCheck = 0;
				guess = countries.getCode(guess);
			}

			g.setColor(Color.black);	
			g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
			FontMetrics metrics = g.getFontMetrics();
			if ((System.currentTimeMillis() / 1000) % 2 == 1) {
				g.drawString("  " + textBox, (512 - metrics.stringWidth("  " + textBox)) / 2, 700);
			} else {
				g.drawString("> " + textBox, (512 - metrics.stringWidth("> " + textBox)) / 2, 700);
			}

			// we check win conditions here
			if (countries.getName(guess).equals(countries.getName(currentCountry))) {
				score += 1;
				needsReset = 1;
			} else if (guessesLeft != 6) {
				if (guess.equals("No Country Found")) {
					g.setColor(Color.gray);	
					g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					metrics = g.getFontMetrics();
					g.drawString("Invalid Country", (512 - metrics.stringWidth("Invalid Country")) / 2, 550);
					g.drawString(guessesLeft + " Guesses Left", (512 - metrics.stringWidth(guessesLeft + " Guesses Left")) / 2, 600);
				} else {
					int distance = findDistance(countries.getLat(guess), countries.getLon(guess), countries.getLat(currentCountry), countries.getLon(currentCountry), 0, 0);
					// System.out.println("The country is in the "+ getBearing(countries.getLat(guess), countries.getLon(guess),countries.getLat(currentCountry), countries.getLon(currentCountry))+ " direction.");
					percent = squareCalculator.PercentProximity(findDistance(countries.getLat(guess), countries.getLon(guess), countries.getLat(currentCountry), countries.getLon(currentCountry), 0, 0));
					squareCalculator.SquareProgress(percent);
					
					squareLocation = 31;
					for (int i = 0; i < squareCalculator.greenSquares; i+= 1) {
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

					g.setColor(Color.gray);	
					g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
					metrics = g.getFontMetrics();
					g.drawString("Guess: " + countries.getName(guess), (512 - metrics.stringWidth("Guess: " + countries.getName(guess))) / 2, 550);	
					String message = distance + " km (" + (int) percent + "%)";
					g.drawString(message, (512 - metrics.stringWidth(message)) / 2, 600);
					g.drawString(guessesLeft + " Guesses Left", (512 - metrics.stringWidth(guessesLeft + " Guesses Left")) / 2, 650);
				}
			}
			g.drawString("Score: " + score, (512 - metrics.stringWidth("Score: " + score)) / 2, 850);
		}
		if (needsReset == 1 || guessesLeft == 0) {
			currentCountry = countries.getRandomCountry();
			System.out.println("DEBUG ANSWER " + countries.getName(currentCountry));
			needsReset = 0;
			guessesLeft = 6;
		}
		g.setColor(Color.black);
		g.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		FontMetrics metrics = g.getFontMetrics();
		g.drawString(countries.getName(currentCountry).replaceAll("[a-zA-Z]", "_ ").trim(), (512 - metrics.stringWidth(countries.getName(currentCountry).replaceAll("[a-zA-Z]", "_ ").trim())) / 2, 128);

		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/img/" + currentCountry + ".png"));
			g.drawImage(image, 128, 180, null);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		paintComponent(g, getBearing(countries.getLat(guess),countries.getLon(guess),countries.getLat(currentCountry), countries.getLon(currentCountry)), 1);
		
		menu.draw(g);
	}

	public void run() {
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

	public static int findDistance(double lat1, double lon1, double lat2, double lon2, double el1, double el2) {
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return (int) Math.round(Math.sqrt(distance));
	}

	public static double getBearing(double lat1, double lon1, double lat2, double lon2) {

		double x, y, bearingrad, bearingdeg;

		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		x = Math.cos(lat2) * Math.sin(lon2 - lon1);

		y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);

		bearingrad = Math.atan2(x, y);
		bearingdeg = Math.toDegrees(bearingrad);

		if (bearingdeg < 0) {
			bearingdeg += 360;
		}
		return bearingdeg;
	}
	
	public void paintComponent(Graphics g, double degree, double scaling) {
		BufferedImage Arrow = LoadImage("src/Arrow.png"); // Idk import an image lol

		AffineTransform at = AffineTransform.getTranslateInstance(200, 600); // change the position if needed
		at.rotate(Math.toRadians(degree), Arrow.getWidth() / 2, Arrow.getHeight() / 2);
		at.scale(scaling, scaling);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(Arrow, at, null);
		repaint();
	}

	BufferedImage LoadImage(String FileName) {
		BufferedImage img = null;

		try {
			img = ImageIO.read(new File(FileName));
		} catch (IOException e) {

		}

		return img;
	}
	
	//Draws the arrow for which the country is to the direction of

	public void keyPressed(KeyEvent e){
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			readyCheck = 1;
			return;
		} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && textBox.length() > 0) {
			textBox = textBox.substring(0, textBox.length() - 1); 
		} else if (e.getKeyChar() == KeyEvent.VK_DELETE) {
			System.exit(0);
		}
		if (isPrintableChar(e.getKeyChar())) {
			textBox = textBox + e.getKeyChar();
		}
  	}

	public void keyTyped(KeyEvent e){}
	public void keyReleased(KeyEvent e){}

	public boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }
}
