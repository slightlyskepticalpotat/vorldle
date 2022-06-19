/*
* This Menu class draws the green title of the game. Having it as a
* separate class helped in testing because we could pass the constructor
* different arguments to have it draw the menu at a different location.
*
* @author  Anthony Chen, Tony Zhang
* @version 1.0
* @since   2022-06-19
*/

import java.awt.*;

public class Menu extends Rectangle {
	
	public static int X_POS;
	public static int Y_POS;

	public Menu(int x, int y) { // creates menu at given x and y positions
		Menu.X_POS = x;
		Menu.Y_POS = y;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(48, 163, 84)); // nice shade of green
		g.setFont(new Font("Segoe UI", Font.BOLD, 30)); // pick a font and bold it
		g.drawString(String.valueOf("VORLDLE"), Menu.X_POS, Menu.Y_POS); // actually display the title on the screen
	}
}
