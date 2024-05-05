package slavikcodd3r.rainbow.event.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import slavikcodd3r.rainbow.event.Event;

public class AttackEvent extends Event
{
    private EntityPlayer playerIn;
    private Entity target;
    
    public AttackEvent(final EntityPlayer playerIn, final Entity target) {
        this.playerIn = playerIn;
        this.target = target;
    }
    
    public EntityPlayer getPlayerIn() {
        return this.playerIn;
    }
    
    public Entity getTarget() {
        return this.target;
    }
}
