package xyz.northclient.features.events;

import xyz.northclient.features.Event;
import lombok.Getter;
import lombok.Setter;

public class MoveEvent extends Event {
    @Setter
    @Getter
    public double x;
    @Setter
    @Getter
    public double y;
    @Setter
    @Getter
    public double z;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
