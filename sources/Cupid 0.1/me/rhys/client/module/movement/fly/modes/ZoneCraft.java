package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class ZoneCraft extends ModuleMode<Fly> {
  public ZoneCraft(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory
          
          .getCurrentItem(), 0.0F, 0.0F, 0.0F));
    if (this.mc.thePlayer.posY >= 80.0D && this.mc.thePlayer.posY <= 110.0D)
      this.mc.thePlayer.motionY = 0.41999998688697815D; 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\ZoneCraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */