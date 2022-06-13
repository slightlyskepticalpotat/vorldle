import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class GamePanel extends JPanel implements Runnable {
	public Thread gameThread;
	public Image image;
	public Graphics graphics;
	public Countries countries;
	public int needsReset = 1;
	public String currentCountry;
	public Menu menu;
	public Proximity squares;

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
		if (needsReset == 1) {
			currentCountry = countries.getRandomCountry();
			needsReset = 0;
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
		for (int i = 0; i < 512; i += 32) { // draw central line of squares
			if (green != 0) {
				green -= 1;
				g.setColor(Color.green);
			} else if (yellow != 0) {
				yellow -= 1;
				g.setColor(Color.yellow);
			} else if (white != 0) {
				white -= 1;
				g.setColor(Color.white);
			} else {
				return;
			}
			g.fillRect(256, i, 8, 8);
		}
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
}
