package me.rhys.client.module.player.crasher.impl;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.TickEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.crasher.Crasher;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public class Interact extends ModuleMode<Crasher> {
  public Interact(String name, Crasher parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  public void onTick(TickEvent event) {
    if (this.mc.thePlayer != null) {
      int spawnX = (int)(this.mc.theWorld.getWorldTime() % 2000L);
      int spawnZ = (int)(this.mc.theWorld.getWorldTime() / 2000L);
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(spawnX * 16, 70, spawnZ * -16), 0, this.mc.thePlayer.getCurrentEquippedItem(), 0.0F, 0.0F, 0.0F));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\crasher\impl\Interact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */