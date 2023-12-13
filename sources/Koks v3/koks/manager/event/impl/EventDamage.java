package koks.manager.event.impl;

import koks.manager.event.Event;

/**
 * @author kroko
 * @created on 15.11.2020 : 19:01
 */
public class EventDamage extends Event {
    float before, now, difference;
    boolean invulnerable;

    public EventDamage(float before, float now, float difference, boolean invulnerable) {
        this.before = before;
        this.now = now;
        this.difference = difference;
        this.invulnerable = invulnerable;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    public float getBefore() {
        return before;
    }

    public void setBefore(float before) {
        this.before = before;
    }

    public float getNow() {
        return now;
    }

    public void setNow(float now) {
        this.now = now;
    }

    public float getDifference() {
        return difference;
    }

    public void setDifference(float difference) {
        this.difference = difference;
    }
}
