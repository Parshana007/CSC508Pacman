import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        String id = args.length > 0 ? args[0] : "default";
        Square mySquare = new Square(0, 0, id, Color.GREEN);
        Blackboard.getInstance().addSquare(mySquare);
        Main m = new Main();
        m.setTitle("");
        m.setSize(100, 200);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.setVisible(true);
        MQTTPublisher mp = new MQTTPublisher();
        Thread t1 = new Thread(mp);
        Blackboard.getInstance().addPropertyChangeListener(mp);
        t1.start();
        MQTTSubscriber ms = new MQTTSubscriber();
        Thread t2 = new Thread(ms);
        t2.start();
    }

    public Main() {
        setLayout(new GridLayout(1, 1));
        WorldPanel wp = new WorldPanel();
        add(wp);
    }
}
