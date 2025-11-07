import java.awt.*;

public class Square {
    private int x;
    private int y;
    private Color color;
    private String id;

    public Square(int x, int y, String id, Color color) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(x,y,1,1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
