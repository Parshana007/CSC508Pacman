import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ControlPanel extends JPanel {
    public ControlPanel(WorldPanel worldPanel) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel brokerDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] brokerOptions = {"tcp://broker.hivemq.com:1883", "tcp://test.mosquitto.org:1883"};
        JComboBox<String> brokerDropdownMenu = new JComboBox<>(brokerOptions);
        brokerDropdownPanel.add(new JLabel("Broker:"));
        brokerDropdownPanel.add(brokerDropdownMenu);

        JPanel topicDropdownPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] topicOptions = {"calpoly/csc509/multiverse/", "calpoly/csc509/personal/"};
        JComboBox<String> topicDropdownMenu = new JComboBox<>(topicOptions);
        topicDropdownPanel.add(new JLabel("Topic:"));
        topicDropdownPanel.add(topicDropdownMenu);


        JPanel colorPanel =  new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton chooseButton = new JButton("Change Square Color");
        colorPanel.add(new JLabel("Color:"));
        colorPanel.add(chooseButton);

        topPanel.add(brokerDropdownPanel);
        topPanel.add(topicDropdownPanel);
        topPanel.add(colorPanel);

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

        chooseButton.addActionListener(e -> {
            Color chosenColor = JColorChooser.showDialog(new JPanel(), "Choose Square Color", Color.BLACK);
            if(chosenColor != null){
                Blackboard.getInstance().getMySquare().setColor(chosenColor);
            }
            worldPanel.requestFocusInWindow();
        });

        new Thread(sub::start).start();
    }
}
