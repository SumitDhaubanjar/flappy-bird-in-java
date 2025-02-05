import javax.swing.*;




public class App {
    public static void main(String args[]) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
        JFrame frame = new JFrame("flappy bird");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setSize(boardWidth,boardHeight);

        FlappyBird flappybird = new FlappyBird();
        frame.add(flappybird);
        frame.pack();
        flappybird.requestFocus();
        frame.setVisible(true);

    }
}
