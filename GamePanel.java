import java.awt.*;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable {
	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Countries countries;
	public int needsReset = 1;
	public String currentCountry;
	public Menu menu;
	public Proximity squares;
	public String guess;
	public Scanner scan = new Scanner(System.in);
	public int score = 0;
	public int guessesLeft = 6;

	public GamePanel() {
		this.setFocusable(true);
		gameThread = new Thread(this);
		gameThread.start();
		countries = new Countries("src/database.txt");
		menu = new Menu(190, 50);
		// MenuButton();
		this.setPreferredSize(new Dimension(512, 768));

	}

	public void paint(Graphics g) {

		image = createImage(512, 512);
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image, 0, 0, this);
	}

	public void draw(Graphics g) {
		// DrawArrow.draw();
		// Countries.draw();
		if (needsReset == 0 && guessesLeft > 0) {
			System.out.print("Guess: ");
			guess = scan.nextLine();
			guessesLeft -= 1;
			// we check win conditions here
			if (guess.equals(countries.getName(currentCountry))) {
				score += 1;
				System.out.println("You win! You've guessed " + score + " countries so far.");
				needsReset = 1;
			} else {
				System.out.println("Wrong guess!");
				System.out.println("You have " + guessesLeft + " guesses left.");
				guess = countries.getCode(guess);
				if (guess.equals("No Country Found")) {
					System.out.println("Invalid country. Try again!");
				} else {
					System.out.println("Your guess is "
							+ findDistance(countries.getLat(guess), countries.getLon(guess),
									countries.getLat(currentCountry), countries.getLon(currentCountry), 0, 0)
							+ " km off.");
					System.out
							.println(
									"Your guess is on a bearing of "
											+ getBearing(countries.getLat(guess), countries.getLon(guess),
													countries.getLat(currentCountry), countries.getLon(currentCountry))
											+ ".");
				}
				// insert code here to get distance
			}
		}
		if (needsReset == 1 || guessesLeft == 0) {
			currentCountry = countries.getRandomCountry();
			System.out.println("DEBUG " + countries.getName(currentCountry));
			needsReset = 0;
			guessesLeft = 6;
		}
		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/img/" + currentCountry + ".png"));
			g.drawImage(image, 128, 128, null);
		} catch (Exception e) {
			System.out.println(e);
		}
		menu.draw(g);

		// squares.draw(g);
		// need to calculate green, yellow, and white here
		/*
		 * for (int i = 0; i < 512; i += 32) { // draw central line of squares
		 * if (green != 0) {
		 * green -= 1;
		 * g.setColor(Color.green);
		 * } else if (yellow != 0) {
		 * yellow -= 1;
		 * g.setColor(Color.yellow);
		 * } else if (white != 0) {
		 * white -= 1;
		 * g.setColor(Color.white);
		 * } else {
		 * return;
		 * }
		 * g.fillRect(256, i, 8, 8);
		 * }
		 */
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

	public static double findDistance(double lat1, double lon1, double lat2, double lon2, double el1, double el2) {
		final int R = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);
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

		return bearingdeg;
	}
}
