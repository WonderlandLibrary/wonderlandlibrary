package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import xyz.northclient.features.Event;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class StrafeEvent extends Event {
    private float forward, strafe;
    private float friction, attributeSpeed;
    private float yaw;

    public void setSpeed(final double speed) {
        setFriction((float) (getForward() != 0 && getStrafe() != 0 ? speed * 0.98F : speed));
        //MoveUtil.stop();
    }
}
