package club.cloverhook.event.minecraft;

import club.cloverhook.event.CancellableEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

/**
 * @author antja03
 */
public class RenderNametagEvent extends CancellableEvent {

    private EntityLivingBase entity;

    public RenderNametagEvent(EntityLivingBase entity) {
        this.entity = entity;
    }

    public EntityLivingBase getEntity() {
        return entity;
    }
}
