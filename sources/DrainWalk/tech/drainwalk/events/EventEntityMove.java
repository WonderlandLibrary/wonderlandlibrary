package tech.drainwalk.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class EventEntityMove implements Event {
    private Entity ctx;
    private Vec3d from;

    public EventEntityMove(Entity ctx, Vec3d from) {
        this.ctx = ctx;
        this.from = from;
    }

    public Vec3d from() {
        return this.from;
    }

    public Entity ctx() {
        return this.ctx;
    }

}
