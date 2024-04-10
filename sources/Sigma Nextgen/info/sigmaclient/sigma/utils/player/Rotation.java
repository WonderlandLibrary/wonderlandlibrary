package info.sigmaclient.sigma.utils.player;

import lombok.Getter;
import lombok.Setter;

public class Rotation {
    @Setter
    @Getter
    private float yaw, pitch;
    public Rotation(float yaw, float pitch){
        this.yaw = yaw;
        this.pitch = pitch;
    }
}
