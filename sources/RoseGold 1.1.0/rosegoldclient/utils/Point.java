package rosegoldclient.utils;

import net.minecraft.util.math.Vec3d;

public class Point {
    private Vec3d location;
    private long delay;
    private boolean pathfind;

    public Point(Vec3d location, long delay, boolean pathfind) {
        this.location = location;
        this.delay = delay;
        this.pathfind = pathfind;
    }

    public Point(Vec3d location, long delay) {
        this.location = location;
        this.delay = delay;
        this.pathfind = false;
    }

    public Point(Vec3d location, boolean pathfind) {
        this.location = location;
        this.delay = 0;
        this.pathfind = pathfind;
    }

    public Point(Vec3d location) {
        this.location = location;
        this.delay = 0;
        this.pathfind = false;
    }

    public Vec3d getLocation() {
        return location;
    }

    public void setLocation(Vec3d location) {
        this.location = location;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isPathfind() {
        return pathfind;
    }

    public void setPathfind(boolean pathfind) {
        this.pathfind = pathfind;
    }

    @Override
    public String toString() {
        return this.location + " with" + this.delay + " ms delay with" + (this.pathfind ? " " : " no ") + "pathfinding";
    }
}
