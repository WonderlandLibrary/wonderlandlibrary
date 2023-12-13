package today.flux.event;

import com.darkmagician6.eventapi.events.Event;
import today.flux.utility.Location;

public class MoveEvent implements Event {

    public double x;
    public double y;
    public double z;
    private Location location;
    private boolean safeWalk;
    private boolean onGround;

    public MoveEvent(final Location location, final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.location = location;
    }

    public double getX() {
        return this.x;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isSafeWalk() {
        return this.safeWalk;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setSafeWalk(final boolean value) {
        this.safeWalk = value;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
