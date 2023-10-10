package today.flux.event;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.entity.Entity;

public class NameTagRenderEvent extends EventCancellable {
    private Entity entity;
    private double x, y, z;

    public NameTagRenderEvent(Entity entity, double x, double y, double z){
        this.entity = entity;

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getX() {
    	
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
