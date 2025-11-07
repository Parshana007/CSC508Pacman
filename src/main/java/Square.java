public class Square {
    // Should have an x, y, color??
    private int x;
    private int y;
    private Color color;

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

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
