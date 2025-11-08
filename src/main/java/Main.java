import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        String id = args.length > 0 ? args[0] : "default" + System.currentTimeMillis();
        Square mySquare = new Square(400, 300, id, Color.GREEN);
        Blackboard.getInstance().addSquare(mySquare);
        Main m = new Main();
        m.setTitle("");
        m.setSize(800, 600);
        m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        m.setVisible(true);
        MQTTPublisher mp = new MQTTPublisher();
        Blackboard.getInstance().addPropertyChangeListener(mp);
        new Thread(() -> {
            MQTTSubscriber sub = new MQTTSubscriber();
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
