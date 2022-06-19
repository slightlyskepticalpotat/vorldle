/*
* This GameFrame class just creates a window for the game, and creates a
* GamePanel object within to actually display the graphics of the game.
*
* @author  Anthony Chen, Tony Zhang
* @version 1.0
* @since   2022-06-19
*/

import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {

    GamePanel panel;

    public GameFrame() {
        panel = new GamePanel(); // run GamePanel constructor
        this.add(panel);
        this.setTitle("Worldle"); // set title for frame
        this.setResizable(false); // frame can't change size
        this.setBackground(Color.white); // set background colour
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X button will stop program execution
        this.pack(); // makes components fit in window - don't need to set JFrame size, as it will adjust accordingly
        this.setVisible(true); // makes window visible to user
        this.setLocationRelativeTo(null); // set window in middle of screen
    }
}