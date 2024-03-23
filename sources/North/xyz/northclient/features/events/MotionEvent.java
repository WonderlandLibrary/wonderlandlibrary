package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import lombok.Getter;
import lombok.Setter;

public class MotionEvent extends Event {
    @Setter
    @Getter
    public double x;
    @Setter
    @Getter
    public double y;
    @Setter
    @Getter
    public double z;
    @Setter
    @Getter
    public float yaw,pitch;
    @Setter
    @Getter
    public float previousYaw,previousPitch;

    public MotionEvent(double x, double y, double z, float yaw, float pitch, float previousYaw, float previousPitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
