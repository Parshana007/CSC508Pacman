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

        Color squareColor = JColorChooser.showDialog(null, "Choose your square color", Color.BLUE);
        if (squareColor == null) {
            Random random = new Random();
            squareColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        }

        String broker = JOptionPane.showInputDialog("Enter broker URL:", "tcp://broker.hivemq.com:1883");
        if (broker == null || broker.isEmpty()) {
            broker = "tcp://broker.hivemq.com:1883";
        }

        String topic = JOptionPane.showInputDialog("Enter topic:", "calpoly/csc508/brokerverse");
        if (topic == null || topic.isEmpty()) {
            topic = "calpoly/csc508/brokerverse";
        }

        Square mySquare = new Square(400, 300, id, new Color(squareColor.getRed(), squareColor.getGreen(), squareColor.getBlue()));
        Blackboard.getInstance().addSquare(mySquare);
        Main m = new Main();
        m.setTitle("");
        m.setSize(800, 600);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.setVisible(true);
        MQTTPublisher mp = new MQTTPublisher(broker, topic);
        Blackboard.getInstance().addPropertyChangeListener(mp);
        final String fBroker = broker;
        final String fTopic = topic;
        new Thread(() -> {
            MQTTSubscriber sub = new MQTTSubscriber(fBroker, fTopic);
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