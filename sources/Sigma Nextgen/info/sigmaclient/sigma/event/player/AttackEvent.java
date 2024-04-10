package info.sigmaclient.sigma.event.player;

import info.sigmaclient.sigma.event.Event;
import net.minecraft.entity.Entity;

public class AttackEvent extends Event {
    public Entity LivingEntity;
    public boolean post = false;
    public AttackEvent POST(){
        post = true;
        return this;
    }
    public AttackEvent PRE(){
        post = false;
        return this;
    }
    public AttackEvent(Entity LivingEntity){
        this.eventID = 11;
        this.LivingEntity = LivingEntity;
    }
}
