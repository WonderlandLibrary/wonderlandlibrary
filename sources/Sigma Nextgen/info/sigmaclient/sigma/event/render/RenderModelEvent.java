package info.sigmaclient.sigma.event.render;

import info.sigmaclient.sigma.event.Event;
import net.minecraft.entity.LivingEntity;

public class RenderModelEvent extends Event {
    private final LivingEntity entity;
    private final Runnable modelRenderer;
    private final Runnable layerRenderer;

    public RenderModelEvent(LivingEntity entity, Runnable modelRenderer, Runnable layerRenderer) {
        this.eventID = 10;
        this.entity = entity;
        this.modelRenderer = modelRenderer;
        this.layerRenderer = layerRenderer;
    }
    public LivingEntity getEntity() {
        return entity;
    }

    public void drawModel() {
        this.modelRenderer.run();
//        this.layerRenderer.run();
    }
    public void drawLayers() {
        this.layerRenderer.run();
    }
}
