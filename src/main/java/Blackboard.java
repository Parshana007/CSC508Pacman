import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class Blackboard extends PropertyChangeSupport{
    private static Blackboard instance;
    private final PropertyChangeSupport propertyChangeSupport;
    private final List<Square> squarePositions;
    
    private static final Logger logger = LoggerFactory.getLogger(Blackboard.class);

    private Blackboard() {
        super(new Object());  
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.squarePositions = new ArrayList<>();
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

    // Something that adds a position to blackboard: location i.e. x, y, color 
    public synchronized void addSquare(Square square) {
        List<Square> oldValue = new ArrayList<>(squarePositions);
        squarePositions.add(square);
        logger.info("square added");
        propertyChangeSupport.firePropertyChange("clickPositions", oldValue, squarePositions);
    }

    // Something to update the location of the square??

    public synchronized ArrayList<Square> getSquarePositions() {
        return new ArrayList<>(squarePositions);
    }

}
