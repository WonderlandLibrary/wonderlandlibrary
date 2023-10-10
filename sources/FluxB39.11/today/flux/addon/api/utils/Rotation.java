package today.flux.addon.api.utils;

import lombok.Getter;
import lombok.Setter;

public class Rotation {
    @Setter @Getter
    public float yaw, pitch = 0;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
