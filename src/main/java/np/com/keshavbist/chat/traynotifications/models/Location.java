package np.com.keshavbist.chat.traynotifications.models;

public class Location {

    private double x, y;

    public Location(double xLoc, double yLoc) {
        this.x = xLoc;
        this.y = yLoc;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
