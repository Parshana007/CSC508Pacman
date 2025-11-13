import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * MQTT publisher that sends messages to a TOPIC.
 * The broker is test.mosquitto.org and the TOPIC is csc509/multiverse/
 *
 * @version 1.0
 */



public class MQTTPublisher implements PropertyChangeListener {
    private final String BROKER;
    private final String TOPIC;

    private MqttClient client;

    public MQTTPublisher(String broker, String topic) {
        String CLIENT_ID = "jgs-publisher-" + System.currentTimeMillis();

        this.BROKER = broker;
        this.TOPIC = topic.endsWith("/") ? topic : topic + "/";
        try {
            client = new MqttClient(this.BROKER, CLIENT_ID, new MemoryPersistence());
            client.connect();
            System.out.println("Publisher connected to " + broker + " on topic " + this.TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

//public class MQTTPublisher implements PropertyChangeListener{
////    private final static String BROKER = "tcp://test.mosquitto.org:1883";
//    private final static String BROKER = "tcp://broker.hivemq.com:1883";
//    private final static String TOPIC = "csc509/multiverse/";
//    private final static String CLIENT_ID = "jgs-subscriber-" + System.currentTimeMillis();;
//
//
//    private MqttClient client;
//
//    public MQTTPublisher() {
//        try {
//            client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
//            client.connect();
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            Square mySquare = Blackboard.getInstance().getMySquare();

            String content = mySquare.getId() + "," + mySquare.getX() + "," + mySquare.getY() + "," + mySquare.getColor().getRed() + "," + mySquare.getColor().getGreen() + "," + mySquare.getColor().getBlue();

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(2);
            if (client.isConnected()) {
                client.publish(TOPIC + Blackboard.getInstance().getMySquare().getId(), message);
                System.out.println("Publisher sending message");
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}