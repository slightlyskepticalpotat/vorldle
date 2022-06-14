import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuButton extends JFrame {

	public MenuButton() {
		JFrame f = new JFrame("Menu Buttons");
		f.setSize(512, 512);
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);

		Icon Questionmark = new ImageIcon("Questionmark.png");
		Icon Upwardtrend = new ImageIcon("Upwardtrend.png");
		Icon Gear = new ImageIcon("Gear.png");

		JButton help = new JButton(Questionmark);
		JButton stats = new JButton(Upwardtrend);
		JButton settings = new JButton(Gear);

		help.setBounds(0, 0, 30, 30);
		stats.setBounds(0, 400, 30, 30);
		settings.setBounds(0, 450, 30, 30);

		f.add(help);

		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Actions
			}
		});

		stats.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Actions
			}
		});

		settings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Actions
			}
		});

	}
}
