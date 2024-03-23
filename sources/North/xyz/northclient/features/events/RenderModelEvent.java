package xyz.northclient.features.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityLivingBase;
import xyz.northclient.features.Event;

@AllArgsConstructor
@Getter @Setter
public class RenderModelEvent extends Event {
    private final EntityLivingBase entity;
    private final Runnable modelRenderer;
    private final Runnable layerRenderer;

    public void drawModel() {
        this.modelRenderer.run();
    }
    public void drawLayers() {
        this.layerRenderer.run();
    }
}
