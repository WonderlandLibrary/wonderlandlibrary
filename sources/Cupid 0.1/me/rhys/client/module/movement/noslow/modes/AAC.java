package me.rhys.client.module.movement.noslow.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerNoSlowEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.client.module.movement.noslow.NoSlow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AAC extends ModuleMode<NoSlow> {
  private final Timer timer = new Timer();
  
  public AAC(String name, NoSlow parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    this.timer.reset();
  }
  
  @EventTarget
  public void onMotion(PlayerMotionEvent event) {
    if (!this.mc.thePlayer.isBlocking())
      return; 
    if (this.timer.hasReached(80.0D)) {
      this.timer.reset();
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      return;
    } 
    if (event.getType() == Event.Type.PRE) {
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    } else {
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
    } 
  }
  
  @EventTarget
  public void onNoSlow(PlayerNoSlowEvent event) {
    event.setCancelled(true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\noslow\modes\AAC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */