import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.awt.Color;
import java.util.Map;

/**
 * MQTT subscriber that listens to a TOPIC.
 * The BROKER is test.mosquitto.org and the TOPIC is csc509/multiverse/
 *
 * @version 1.0
 */
public class MQTTSubscriber implements MqttCallback {
    private String broker = "tcp://test.mosquitto.org:1883";
    private String topic = "csc509/multiverse/";
	private final static String CLIENT_ID = "jgs-subscriber-" + System.currentTimeMillis();

	private MqttClient client;

    public MQTTSubscriber(String broker, String topic) {
        this.broker = broker;
        this.topic = topic;
    }

    public void start() {
        try {
            if (client != null && client.isConnected()) {
                client.disconnect();
            }

            client = new MqttClient(broker, CLIENT_ID, new MemoryPersistence());
            client.setCallback(this);
            client.connect();
            client.subscribe(topic + "#");
            System.out.println("Subscriber connected and listening to " + topic + "#");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setBroker(String newBroker) {
        if (!this.broker.equals(newBroker)) {
            this.broker = newBroker;
            System.out.println("Changing subscriber broker to " + newBroker);
            start();
        }
    }

    public void setTopic(String newTopic) {
        if (!this.topic.equals(newTopic)) {
            this.topic = newTopic;
            System.out.println("Changing subscriber topic to " + newTopic);
            try {
                if (client != null && client.isConnected()) {
                    client.unsubscribe("#");
                    client.subscribe(topic + "#");
                } else {
                    start();
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload());
		System.out.println("Positions of Square arrived. Topic: " + s +
			" Message: " + payload);

		String[] squareDetails = payload.split(",");

		// check if payload is correct length
		if (squareDetails.length != 6) {
			System.err.println("Invalid payload format: " + payload);
			return;
		}

		// try adding new payload to Blackboard
		try {
			String id = squareDetails[0];

            // Ignore messages from yourself
            Square mySquare = Blackboard.getInstance().getMySquare();
            if (mySquare != null && id.equals(mySquare.getId())) {
                return;
            }

			int x = Integer.parseInt(squareDetails[1]);
			int y = Integer.parseInt(squareDetails[2]);
			int red = Integer.parseInt(squareDetails[3]);
			int green = Integer.parseInt(squareDetails[4]);
			int blue = Integer.parseInt(squareDetails[5]);

			// using received RGB to create Color object
			Color color = new Color(red, green, blue);

            Map<String, Square> squares = Blackboard.getInstance().getSquarePositions();
            Square existing = squares.get(id);

            if (existing != null) {
                existing.setX(x);
                existing.setY(y);
                existing.setColor(color);
            } else {
                Square newSquare = new Square(x, y, id, color);
                Blackboard.getInstance().addSquare(newSquare);
            }

            Blackboard.getInstance().firePropertyChange("squareMoved", null, squares);
		}
		catch (Exception e) {
			System.err.println("Error parsing payload: " + payload);
		}
	}


	@Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
