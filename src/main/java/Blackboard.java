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
    private final PropertyChangeSupport propertyChangeSupport;
    private final Map<String, Square> squarePositions;
    private String mySquareId;
    private Square mySquare;
    
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    private Blackboard() {
        super(new Object());  
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.squarePositions = new HashMap<String, Square>();
    }

    public static synchronized Blackboard getInstance() {
        if (instance == null) {
            instance = new Blackboard();
        }
        return instance;
    }

    public void addObserver(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removeObserver(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public void setMySquareId(String squareId) {
        this.mySquareId = squareId;
    }

    public String getMySquareId() {
        return this.mySquareId;
    }

    public void setMySquare(Square square) {
        this.mySquare = square;
    }

    public Square getMySquare() {
        return this.mySquare;
    }

    public Square getMyIDSquare() {
        Square currentSquare = null;

        for (Map.Entry<String,Square> entry : squarePositions.entrySet()) {
            String key = entry.getKey();
            if (key.equals(mySquareId)) {
                currentSquare = (Square) entry.getValue();
            }
        }
        return currentSquare;
    }

    public Square findSquare(Square square) {
        Square existingSquare = getMyIDSquare();
        if (existingSquare != null) {
            return existingSquare;
        }
        return null;
    }


    public Square addSquare(Square square) {
        squarePositions.put(square.getId(), square);
        this.mySquare = square;
        logger.info("square added");
        propertyChangeSupport.firePropertyChange("addedSquare", "", squarePositions);
        return square;
    }

    public synchronized Map<String, Square> getSquarePositions() {
        return squarePositions;
    }

    public void up() {
        // Find the square --> subtract one to y
        Square mySquareUpdate = new Square(mySquare.getX(), mySquare.getY() - 1, mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquareId, mySquareUpdate);
        this.mySquare = mySquareUpdate;
    }

    public void down() {
        // Find the square --> add one to y
        Square mySquareUpdate = new Square(mySquare.getX(), mySquare.getY() + 1, mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquareId, mySquareUpdate);
        this.mySquare = mySquareUpdate;    
    }

    public void left() {
        // Find the square --> subtract one to x
        Square mySquareUpdate = new Square(mySquare.getX() - 1, mySquare.getY(), mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquareId, mySquareUpdate);
        this.mySquare = mySquareUpdate;    
    }

    public void right() {
        // Find the square --> add one to x
        Square mySquareUpdate = new Square(mySquare.getX() + 1, mySquare.getY(), mySquare.getId(), mySquare.getColor());

        squarePositions.put(mySquareId, mySquareUpdate);
        this.mySquare = mySquareUpdate;
    }
}