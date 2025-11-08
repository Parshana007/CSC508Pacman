import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Blackboard is a singleton that holds the states of all squares in the game.
 *
 * @version 1.0
 */
public class Blackboard extends PropertyChangeSupport{
    private static Blackboard instance;
    private final Map<String, Square> squarePositions;
    private Square mySquare;
    
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    private Blackboard() {
        super(new Object());
        this.squarePositions = new HashMap<>();
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public Square getMySquare() {
        return this.mySquare;
    }


    public void addSquare(Square square) {
        Square existing = squarePositions.get(square.getId());
        squarePositions.put(square.getId(), square);
        if (this.mySquare == null || square.getId().equals(this.mySquare.getId())) {
            this.mySquare = square;
        }
        if (existing == null || !existing.equals(square)) {
            logger.info("square added");
            firePropertyChange("addedSquare", null, squarePositions);
        }
    }

    public synchronized Map<String, Square> getSquarePositions() {
        return squarePositions;
    }

    public void up() {
        // Find the square --> subtract one to y
        mySquare.setY(mySquare.getY() - 20);
        squarePositions.put(mySquare.getId(), mySquare);

        firePropertyChange("squareMoved", null, mySquare);
        logger.info(squarePositions.toString());
    }

    public void down() {
        // Find the square --> add one to y
        mySquare.setY(mySquare.getY() + 20);
        squarePositions.put(mySquare.getId(), mySquare);

        firePropertyChange("squareMoved", null, mySquare);
        logger.info(squarePositions.toString());
    }

    public void left() {
        // Find the square --> subtract one to x
        mySquare.setX(mySquare.getX() - 20);
        squarePositions.put(mySquare.getId(), mySquare);

        firePropertyChange("squareMoved", null, mySquare);
        logger.info(squarePositions.toString());
    }

    public void right() {
        // Find the square --> add one to x
        mySquare.setX(mySquare.getX() + 20);
        squarePositions.put(mySquare.getId(), mySquare);

        firePropertyChange("squareMoved", null, mySquare);
        logger.info(squarePositions.toString());
    }
}