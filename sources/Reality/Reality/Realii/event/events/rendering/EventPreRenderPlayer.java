
package Reality.Realii.event.events.rendering;

import Reality.Realii.event.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
public class EventPreRenderPlayer
extends Event {
	
	private final EntityLivingBase entity;
    private final Runnable modelRenderer;
    private final Runnable layerRenderer;

    public EventPreRenderPlayer(EntityLivingBase entity, Runnable modelRenderer, Runnable layerRenderer) {
        this.entity = entity;
        this.modelRenderer = modelRenderer;
        this.layerRenderer = layerRenderer;
    }

   // @Exclude(Strategy.NAME_REMAPPING)
    public EntityLivingBase getEntity() {
        return entity;
    }

   // @Exclude(Strategy.NAME_REMAPPING)
    public void drawModel() {
        this.modelRenderer.run();
    }

    //@Exclude(Strategy.NAME_REMAPPING) {
    public void drawLayers() {
        this.layerRenderer.run();
    }
}

