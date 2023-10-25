package fun.rich.client.event.events.impl.render;

import fun.rich.client.event.events.callables.EventCancellable;
import net.minecraft.entity.EntityLivingBase;

public class EventRenderPlayerName extends EventCancellable {
    private final EntityLivingBase entity;
    private String renderedName;

    public EventRenderPlayerName(EntityLivingBase entity, String renderedName) {
        this.entity = entity;
        this.renderedName = renderedName;
    }

    public EntityLivingBase getEntity() {
        return this.entity;
    }

    public String getRenderedName() {
        return this.renderedName;
    }

    public void setRenderedName(String renderedName) {
        this.renderedName = renderedName;
    }

}
