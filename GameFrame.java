static GraphicsDevice device = GraphicsEnvironment
        .getLocalGraphicsEnvironment().getScreenDevices()[0];

public static void main(String[] args) {

    final JFrame frame = new JFrame("Worldle");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setUndecorated(true);

    device.setFullScreenWindow(frame);

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    frame.add(panel);

    frame.pack();
    frame.setVisible(true);

}
