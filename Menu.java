import java.awt.*;

public class Menu extends Rectangle{

	public static int X_POS;
	public static int Y_POS;

	public Menu(int x, int y) {
		Menu.X_POS = x;
		Menu.Y_POS = y;
	}

	public void draw(Graphics g) {
		Color title = new Color(48,163,84);

		g.setColor(title);
		g.setFont(new Font("Segoe UI", Font.BOLD, 30));
		g.drawString(String.valueOf("VORLDLE"), Menu.X_POS,Menu.Y_POS);
	}

}
