package club.async.event.impl;

import club.async.event.Event;

public class EventMovePlayer extends Event {

    private double x, y, z;

    public EventMovePlayer(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public void x(double x) {
        this.x = x;
    }

    public void y(double y) {
        this.y = y;
    }

    public void z(double z) {
        this.z = z;
    }

}
