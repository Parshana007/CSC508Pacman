import javax.swing.*;
import java.awt.*;
import java.util.Objects;
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
    }

    public Main() {
        setLayout(new BorderLayout());
        WorldPanel wp = new WorldPanel();
        add(wp, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel brokerDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] brokerOptions = {"tcp://test.mosquitto.org:1883", "tcp://broker.hivemq.com:1883"};
        JComboBox<String> brokerDropdownMenu = new JComboBox<>(brokerOptions);
        brokerDropdownPanel.add(new JLabel("Broker:"));
        brokerDropdownPanel.add(brokerDropdownMenu);

        JPanel topicDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] topicOptions = {"calpoly/csc509/personal/", "calpoly/csc509/multiverse/"};
        JComboBox<String> topicDropdownMenu = new JComboBox<>(topicOptions);
        topicDropdownPanel.add(new JLabel("Topic:"));
        topicDropdownPanel.add(topicDropdownMenu);

        topPanel.add(brokerDropdownPanel);
        topPanel.add(topicDropdownPanel);

        add(topPanel, BorderLayout.NORTH);

        MQTTPublisher mp = new MQTTPublisher(
                Objects.requireNonNull(brokerDropdownMenu.getSelectedItem()).toString(),
                Objects.requireNonNull(topicDropdownMenu.getSelectedItem()).toString());

        Blackboard.getInstance().addPropertyChangeListener(mp);

        MQTTSubscriber sub = new MQTTSubscriber(
                Objects.requireNonNull(brokerDropdownMenu.getSelectedItem()).toString(),
                Objects.requireNonNull(topicDropdownMenu.getSelectedItem()).toString());

        brokerDropdownMenu.addActionListener(e -> {
                    String newBroker = (String) brokerDropdownMenu.getSelectedItem();
                    mp.setBroker(newBroker);
                    sub.setBroker(newBroker);
                }
        );

        topicDropdownMenu.addActionListener(e -> {
                    String newBroker = (String) topicDropdownMenu.getSelectedItem();
                    mp.setTopic(newBroker);
                    sub.setTopic(newBroker);
                }
        );

        new Thread(sub::start).start();
        Blackboard.getInstance().addPropertyChangeListener(wp);
        SwingUtilities.invokeLater(wp::requestFocusInWindow);
    }
}
