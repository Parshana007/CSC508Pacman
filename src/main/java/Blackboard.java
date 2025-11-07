import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class Blackboard extends PropertyChangeSupport{
    private static Blackboard instance;
    private final Map<String, Square> squarePositions;
    private Square mySquare;
    
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    private Blackboard() {
        super(new Object());
        this.squarePositions = new HashMap<String, Square>();
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void setMySquare(Square square) {
        this.mySquare = square;
    }

    public Square getMySquare() {
        return this.mySquare;
    }


    public Square addSquare(Square square) {
        squarePositions.put(square.getId(), square);
        this.mySquare = square;
        logger.info("square added");
        firePropertyChange("addedSquare", "", squarePositions);
        return square;
    }

    public synchronized Map<String, Square> getSquarePositions() {
        return squarePositions;
    }

    public void up() {
        logger.info(squarePositions.toString());
        // Find the square --> subtract one to y
        Square mySquareUpdate = new Square(mySquare.getX(), mySquare.getY() - 20, mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquare.getId(), mySquareUpdate);
        this.mySquare = mySquareUpdate;
        firePropertyChange("squareMoved", null, mySquareUpdate);
        logger.info(squarePositions.toString());
    }

    public void down() {
        // Find the square --> add one to y
        Square mySquareUpdate = new Square(mySquare.getX(), mySquare.getY() + 20, mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquare.getId(), mySquareUpdate);
        this.mySquare = mySquareUpdate;
        firePropertyChange("squareMoved", null, mySquareUpdate);
    }

    public void left() {
        // Find the square --> subtract one to x
        Square mySquareUpdate = new Square(mySquare.getX() - 20, mySquare.getY(), mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquare.getId(), mySquareUpdate);
        this.mySquare = mySquareUpdate;
        firePropertyChange("squareMoved", null, mySquareUpdate);
    }

    public void right() {
        // Find the square --> add one to x
        Square mySquareUpdate = new Square(mySquare.getX() + 20, mySquare.getY(), mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquare.getId(), mySquareUpdate);
        this.mySquare = mySquareUpdate;
        firePropertyChange("squareMoved", null, mySquareUpdate);
    }
}