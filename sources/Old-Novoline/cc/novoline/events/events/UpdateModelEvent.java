package cc.novoline.events.events;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class UpdateModelEvent implements Event{

    private ModelPlayer modelPlayer;
    private Entity entity;

    public UpdateModelEvent(Entity entity, ModelPlayer modelPlayer) {
        this.modelPlayer = modelPlayer;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public ModelPlayer getModelPlayer() {
        return modelPlayer;
    }
}
