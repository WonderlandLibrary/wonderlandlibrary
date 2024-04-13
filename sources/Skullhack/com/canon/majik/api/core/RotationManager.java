package com.canon.majik.api.core;

import com.canon.majik.api.event.eventBus.EventListener;
import com.canon.majik.api.event.eventBus.Priority;
import com.canon.majik.api.event.events.PlayerUpdateEvent;
import com.canon.majik.api.utils.Globals;

public class RotationManager implements Globals {
    private float spoofedYaw, spoofedPitch;

    public RotationManager(){
        Initializer.eventBus.registerListener(this);
    }

    @EventListener(getPriority = Priority.LOWEST)
    public void onMotionUpdate(PlayerUpdateEvent event){
        setSpoofedYaw(event.getYaw());
        setSpoofedPitch(event.getPitch());
    }


    public float getSpoofedYaw() {
        return spoofedYaw;
    }

    public float getSpoofedPitch() {
        return spoofedPitch;
    }

    public void setSpoofedYaw(float spoofedYaw) {
        this.spoofedYaw = spoofedYaw;
    }

    public void setSpoofedPitch(float spoofedPitch) {
        this.spoofedPitch = spoofedPitch;
    }
}
