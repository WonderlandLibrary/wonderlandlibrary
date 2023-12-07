package Velo.impl.Event;


import Velo.api.Event.Event;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class EventUpdateModel extends Event<EventUpdateModel> {


    public Entity entity;
    public ModelPlayer modelPlayer;

    public EventUpdateModel(Entity entity, ModelPlayer modelPlayer) {
        this.entity = entity;
        this.modelPlayer = modelPlayer;
    }

}
