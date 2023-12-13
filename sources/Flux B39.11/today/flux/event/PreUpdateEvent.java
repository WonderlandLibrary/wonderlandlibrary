package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import lombok.Getter;
import lombok.Setter;
import today.flux.Flux;
import today.flux.utility.ChatUtils;


public class PreUpdateEvent extends EventCancellable {
    @Setter @Getter
    public double x, y, z;
    public float yaw;
    public float pitch;
    @Setter @Getter
    public boolean onGround;
    private boolean modified;

    public PreUpdateEvent(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.y = y;
        this.z = z;
        this.x = x;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        this.modified = true;
    }

    public void setPitch(float pitch) {
        if (Math.abs(pitch) > 90) {
            ChatUtils.debug("WARNING: PITCH IS " + pitch);
            if (Flux.DEBUG_MODE) {
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        this.pitch = pitch;
        this.modified = true;
    }

    public void setRotation(float[] rotation) {
        setYaw(rotation[0]);
        setPitch(rotation[1]);
        this.modified = true;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public boolean isModified() {
        return modified;
    }
}
