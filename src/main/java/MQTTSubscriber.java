import org.eclipse.paho.client.mqttv3.*;
import java.awt.Color;

/**
 * This class is a simple MQTT subscriber that listens to a TOPIC.
 * The BROKER is test.mosquitto.org and the TOPIC is cal-poly/csc/309.
 * (run this and the publisher at the same time)
 *
 * @author javiergs
 * @version 1.0
 */

public class MQTTSubscriber implements Runnable, MqttCallback {

	private final static String BROKER = "tcp://test.mosquitto.org:1883";
	private final static String TOPIC = "csc509/multiverse/";
	private final static String CLIENT_ID = "jgs-subscriber";

	private volatile boolean running = true;
	private MqttClient client;

	public void stopSubscriber() {
        running = false;
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
                System.out.println("Disconnected from MQTT broker.");
            } catch (MqttException e) {
				System.out.println("Error while disconnecting MQTTSubscriber");
				e.printStackTrace();
            }
        }
    }

	@Override
    public void run() {
		MqttClient client = null;
		try {
			client = new MqttClient(BROKER, CLIENT_ID);
			client.setCallback(new MQTTSubscriber());
			client.connect();
			System.out.println("Connected to BROKER: " + BROKER);
			client.subscribe(TOPIC);
			System.out.println("Subscribed to TOPIC: " + TOPIC);

            while (running) {
                Thread.sleep(1000);
            }
		} catch (MqttException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload());
		System.out.println("Positions of Square arrived. Topic: " + s +
			" Message: " + payload);
        
		// payload ex: id,100,200,RED

		String[] squareDetails = payload.split(",");

		// check if payload is correct length
		if (squareDetails.length != 4) {
			System.err.println("Invalid payload format: " + payload);
			return;
		}

		// try adding new payload to Blackboard
		try {
			String id = squareDetails[0];
			int x = Integer.parseInt(squareDetails[1]);
			int y = Integer.parseInt(squareDetails[2]);
			String colorName = squareDetails[3].trim();

			// try converting String colocName to Color object
			Color color;
			try {
				color = (Color) Color.class.getField(colorName.toUpperCase()).get(null);
			} catch (Exception e) {
				System.err.println("Unknown color: " + colorName);
				return;
			}

			Square square = new Square(x, y, id, color);
			Blackboard.getInstance().addSquare(square);

		}
		catch (Exception e) {
			System.err.println("Error parsing coordinates from payload: " + payload);
		}
	}


	    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}