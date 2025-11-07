import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorldPanel extends JPanel implements KeyListener, PropertyChangeListener {
    public WorldPanel() {
        setBackground(Color.darkGray);
    }

    @Override
    public void paintComponent(Graphics g) {
        drawGrid(g);
    }

    private void drawGrid(Graphics g) {
        for (int y = 0; y < getHeight(); y++) {
            g.drawLine(0, y, getWidth(), y);
        }
        for (int x = 0; x < getWidth(); x++) {
            g.drawLine(x, 0, x, getHeight());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            Blackboard.getInstance().up();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            Blackboard.getInstance().down();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            Blackboard.getInstance().left();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            Blackboard.getInstance().right();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
