package org.luaj.vm2.customs.events;

import org.luaj.vm2.customs.EventHook;
import wtf.expensive.events.Event;
import wtf.expensive.events.impl.player.EventMotion;

public class EventMotionHook extends EventHook {

    private EventMotion motion;

    public EventMotionHook(Event event) {
        super(event);
        this.motion = (EventMotion) event;
    }

    public void setYaw(float yaw) {
        motion.setYaw(yaw);
    }

    public float getYaw() {
        return motion.getYaw();
    }

    public float getPitch() {
        return motion.getPitch();
    }

    public void setPitch(float pitch) {
        motion.setPitch(pitch);
    }

    public void setX(float x) {
        motion.setX(x);
    }

    public void setY(float y) {
        motion.setY(y);
    }
    public void setZ(float z) {
        motion.setZ(z);
    }
    public void setGround(boolean x) {
        motion.setOnGround(x);
    }



    @Override
    public String getName() {
        return "motion_event";
    }
}
