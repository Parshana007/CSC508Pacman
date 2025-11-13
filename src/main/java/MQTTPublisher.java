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
public class MQTTPublisher implements PropertyChangeListener{
    private String broker = "tcp://test.mosquitto.org:1883";
	private String topic = "csc509/multiverse/";
    private final static String CLIENT_ID = "jgs-subscriber-" + System.currentTimeMillis();;


    private MqttClient client;

	public MQTTPublisher(String broker, String topic) {
		this.broker = broker;
        this.topic = topic;
        new Thread(this::connectClient).start();
	}

    public void setBroker(String broker) {
        if (!this.broker.equals(broker)) {
            this.broker = broker;
            connectClient();
            System.out.println("Broker set to " + broker);
        }
    }

    public void setTopic(String topic) {
        this.topic = topic;
        System.out.println("Topic set to " + topic);
    }

    public void connectClient() {
        try {
            client = new MqttClient(broker, CLIENT_ID, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		try {
			Square mySquare = Blackboard.getInstance().getMySquare();

			String content = mySquare.getId() + "," + mySquare.getX() + "," + mySquare.getY() + "," + mySquare.getColor().getRed() + "," + mySquare.getColor().getGreen() + "," + mySquare.getColor().getBlue();

			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(2);
			if (client.isConnected()) {
				client.publish(topic + Blackboard.getInstance().getMySquare().getId(), message);
				System.out.println("Publisher sending message");
			}

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
