import org.eclipse.paho.client.mqttv3.*;

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
	private final static String TOPIC = "csc509/multiverse/username/";
	private final static String CLIENT_ID = "jgs-subscriber";
	
	public static void main(String[] args) {
		try {
			MqttClient client = new MqttClient(BROKER, CLIENT_ID);
			Subscriber subscriber = new Subscriber();
			client.setCallback(subscriber);
			client.connect();
			System.out.println("Connected to BROKER: " + BROKER);
			client.subscribe(TOPIC);
			System.out.println("Subscribed to TOPIC: " + TOPIC);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}


	@Override
    public void run() {
		try {
			MqttClient client = new MqttClient(BROKER, CLIENT_ID);
			Subscriber subscriber = new Subscriber();
			client.setCallback(subscriber);
			client.connect();
			System.out.println("Connected to BROKER: " + BROKER);
			client.subscribe(TOPIC);
			System.out.println("Subscribed to TOPIC: " + TOPIC);

            while (true) {
                Thread.sleep(1000); // todo: ??
            }
		} catch (MqttException e) {
			e.printStackTrace();
		}
    }

	  // todo: ??
    public static void main(String[] args) {
        new Thread(new MQTTSubscriber()).start();
    }
	
	
	// @Override
	// public void connectionLost(Throwable throwable) {
	// 	System.out.println("Connection lost: " + throwable.getMessage());
	// }
	

		@Override
	public void messageArrived(String s, MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload());
		System.out.println("Positions of Squares arrived. Topic: " + s +
			" Message: " + payload);

        // Update the shared game state (Blackboard)
        Blackboard.getInstance().updateSquarePositions(payload);
	}
	
	// @Override
	// public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	// 	System.out.println("Delivered complete: " + iMqttDeliveryToken.getMessageId());
	// }

}