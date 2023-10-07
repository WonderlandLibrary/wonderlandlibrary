package cc.novoline.events.events;

import net.minecraft.entity.Entity;

public class RenderEntityEvent implements Event{

    private Entity entity;
    private State state;


    public RenderEntityEvent(Entity entity, State state) {
        this.entity = entity;
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public Entity getEntity() {
        return entity;
    }

    public enum State{
        PRE,POST;
    }




}
