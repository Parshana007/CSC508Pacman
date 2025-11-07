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

public class MQTTSubscriber implements MqttCallback {

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
	public void messageArrived(String s, MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload());
		System.out.println("Positions of Square arrived. Topic: " + s +
			" Message: " + payload);
        
		//	payload ex: id,x,y,R,G,B
		// payload ex: id,100,200,50,50,50

		String[] squareDetails = payload.split(",");

		// check if payload is correct length
		if (squareDetails.length != 6) {
			System.err.println("Invalid payload format: " + payload);
			return;
		}

		// try adding new payload to Blackboard
		try {
			String id = squareDetails[0];
			int x = Integer.parseInt(squareDetails[1]);
			int y = Integer.parseInt(squareDetails[2]);
			int red = Integer.parseInt(squareDetails[3]);
			int green = Integer.parseInt(squareDetails[4]);
			int blue = Integer.parseInt(squareDetails[5]);

			// using received RGB to create Color object
			Color color = new Color(red, green, blue);

			Square square = new Square(x, y, id, color);
			Blackboard.getInstance().addSquare(square);

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