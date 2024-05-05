package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class EventRenderLayer implements NamedEvent {

    private String mode;
    private final String type;
    private EntityPlayer entityPlayer;
    public LayerRenderer<?> layerRenderer;

    public EventRenderLayer(String mode, String typ, LayerRenderer<?> layerRenderer) {
        this.mode = mode;
        this.layerRenderer = layerRenderer;
        this.type = typ;
    }

    public EventRenderLayer(String mode, String typ, EntityPlayer entityPlayer, LayerRenderer<?> layerRenderer) {
        this.mode = mode;
        this.layerRenderer = layerRenderer;
        this.type = typ;
        this.entityPlayer = entityPlayer;
    }

    public String getType() {
        return type;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String name() {
        return "renderLayer";
    }
}