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

	public Menu(int x, int y) {
		Menu.X_POS = x;
		Menu.Y_POS = y;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(48, 163, 84));
		g.setFont(new Font("Segoe UI", Font.BOLD, 30));
		g.drawString(String.valueOf("VORLDLE"), Menu.X_POS, Menu.Y_POS);
	}
}
