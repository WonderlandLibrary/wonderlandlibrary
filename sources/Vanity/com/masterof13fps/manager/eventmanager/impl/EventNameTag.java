package com.masterof13fps.manager.eventmanager.impl;

import com.masterof13fps.manager.eventmanager.Event;

public class EventNameTag extends Event {

    String name;
    double x, y, z, maxDistance;

    public EventNameTag(String name, double x, double y, double z, double maxDistance) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.maxDistance = maxDistance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }
}
