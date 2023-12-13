package summer.cheat.eventsystem.events.render;

import net.minecraft.entity.EntityLivingBase;
import summer.cheat.eventsystem.Event;

public class EventRenderEntity extends Event {

    private EntityLivingBase entity;
    private double x, y, z;
    private float partialTicks;

    public EventRenderEntity(State state, EntityLivingBase entity, double x, double y, double z, float partialTicks) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public void setEntity(EntityLivingBase entity) {
        this.entity = entity;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}