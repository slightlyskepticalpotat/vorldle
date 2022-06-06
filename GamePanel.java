import java.awt.*;
import java.awt.event.*;
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

    public GamePanel() {
        this.setFocusable(true);
        gameThread = new Thread(this);
        gameThread.start();
        countries = new Countries("database.txt");
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
            BufferedImage image = ImageIO.read(new File("img/" + currentCountry + ".png"));
            g.drawImage(image, 0, 0, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run(){
        // the CPU runs our game code too quickly - we need to slow it down! The following lines of code "force" the computer to get stuck in a loop for short intervals between calling other methods to update the screen.
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000/amountOfTicks;
        double delta = 0;
        long now;

        while(true){ // this is the infinite game loop
            now = System.nanoTime();
            delta = delta + (now-lastTime)/ns;
            lastTime = now;

            // only move objects around and update screen if enough time has passed
            if(delta >= 1){
                repaint();
                delta--;
            }
        }
    }
}
