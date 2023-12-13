package slavikcodd3r.rainbow.event.events;

import net.minecraft.util.AxisAlignedBB;
import slavikcodd3r.rainbow.event.Event;
import net.minecraft.entity.Entity;

public class BoundingBoxEntityEvent extends Event
{
    Entity entity;
    AxisAlignedBB bb;
    
    public BoundingBoxEntityEvent(final Entity entity, final AxisAlignedBB bb) {
        this.entity = entity;
        this.bb = bb;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
    
    public AxisAlignedBB getBb() {
        return this.bb;
    }
    
    public void setBb(final AxisAlignedBB bb) {
        this.bb = bb;
    }
}
