package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xyz.northclient.features.Event;

@Getter
@Setter
@AllArgsConstructor
public class JumpEvent extends Event {
    private double motionY;
    private float yaw;
    private boolean boosting;
}