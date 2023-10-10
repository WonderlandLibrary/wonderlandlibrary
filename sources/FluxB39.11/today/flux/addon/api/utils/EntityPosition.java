package today.flux.addon.api.utils;

import lombok.Getter;
import lombok.Setter;

public class EntityPosition {
    @Getter @Setter
    public double x, y, z = 0;

    public EntityPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
