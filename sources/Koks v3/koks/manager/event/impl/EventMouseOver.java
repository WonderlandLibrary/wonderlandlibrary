package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 26.09.2020 : 13:36
 */
public class EventMouseOver extends Event {
    public double reach;
    public boolean distanceCheck;

    public EventMouseOver(double reach, boolean distanceCheck) {
        this.reach = reach;
        this.distanceCheck = distanceCheck;
    }

    public double getReach() {
        return reach;
    }

    public void setReach(double reach) {
        this.reach = reach;
    }

    public boolean isDistanceCheck() {
        return distanceCheck;
    }

    public void setDistanceCheck(boolean distanceCheck) {
        this.distanceCheck = distanceCheck;
    }
}
