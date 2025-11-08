import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * WorldPanel handles the drawing of squares, updating the square states,
 * and manages key events (up, down, left, right) of a given square.
 *
 * @version 1.0
 */
public class WorldPanel extends JPanel implements KeyListener, PropertyChangeListener {
    public WorldPanel() {
        setBackground(Color.WHITE);
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawSquares(g);
    }

    private void drawGrid(Graphics g) {
        int cellSize = 20;
        for (int y = 0; y < getHeight(); y += cellSize) {
            g.drawLine(0, y, getWidth(), y);
        }
        for (int x = 0; x < getWidth(); x += cellSize) {
            g.drawLine(x, 0, x, getHeight());
        }
    }

    private void drawSquares(Graphics g) {
        for (Square square : Blackboard.getInstance().getSquarePositions().values()) {
            square.draw(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (SwingUtilities.isEventDispatchThread()) {
            repaint();
        } else {
            SwingUtilities.invokeLater(this::repaint);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
    public void keyTyped(KeyEvent e) {}
}
