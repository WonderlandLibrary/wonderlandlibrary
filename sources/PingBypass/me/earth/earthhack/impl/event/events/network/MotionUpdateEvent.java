package me.earth.earthhack.impl.event.events.network;

import me.earth.earthhack.api.event.events.Stage;
import me.earth.earthhack.api.event.events.StageEvent;

public class MotionUpdateEvent extends StageEvent
{
    private final float previousYaw;
    private final float previousPitch;

    private float rotationYaw;
    private float rotationPitch;

    public MotionUpdateEvent(Stage stage, MotionUpdateEvent event)
    {
        this(stage, event.rotationYaw, event.rotationPitch);
    }

    public MotionUpdateEvent(Stage stage, float rotationYaw, float rotationPitch)
    {
        super(stage);
        this.previousYaw = rotationYaw;
        this.previousPitch = rotationPitch;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    public float getYaw()
    {
        return rotationYaw;
    }

    public void setYaw(float rotationYaw)
    {
        this.rotationYaw = rotationYaw;
    }

    public float getPitch()
    {
        return rotationPitch;
    }

    public void setPitch(float rotationPitch)
    {
        this.rotationPitch = rotationPitch;
    }

    public float getPreviousYaw()
    {
        return previousYaw;
    }

    public float getPreviousPitch()
    {
        return previousPitch;
    }

}
