import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Main application to run the square game.
 *
 * @version 1.0
 */
public class Main extends JFrame {
    public static void main(String[] args) {
        String id = args.length > 0 ? args[0] : "default" + System.currentTimeMillis();
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        Square mySquare = new Square(400, 300, id, new Color(red, green, blue));
        Blackboard.getInstance().addSquare(mySquare);
        Main m = new Main();
        m.setTitle("");
        m.setSize(800, 600);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.setVisible(true);
        MQTTPublisher mp = new MQTTPublisher();
        Blackboard.getInstance().addPropertyChangeListener(mp);
        new Thread(() -> {
            MQTTSubscriber sub = new MQTTSubscriber();
            sub.start();
        }).start();
    }

    public Main() {
        setLayout(new GridLayout(1, 1));
        WorldPanel wp = new WorldPanel();
        add(wp);
        Blackboard.getInstance().addPropertyChangeListener(wp);
        SwingUtilities.invokeLater(wp::requestFocusInWindow);
    }
}