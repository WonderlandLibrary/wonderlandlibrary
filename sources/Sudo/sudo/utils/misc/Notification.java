package sudo.utils.misc;

public class Notification {
    private final long timeCreated;
    private final String message;

    private final int r;
    private final int g;
    private final int b;

    public Notification (String message) {
        timeCreated = System.currentTimeMillis();
        this.message = message;
        this.r = 50;
        this.g = 168;
        this.b = 82;
    }

    public Notification(String message, int r, int g, int b) {
        timeCreated = System.currentTimeMillis();
        this.message = message;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public long getTimeCreated () {
        return timeCreated;
    }

    public String getMessage () {
        return message;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}