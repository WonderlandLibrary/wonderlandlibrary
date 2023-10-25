package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class OldVerus extends ModuleMode<Disabler> {
  public OldVerus(String name, Disabler parent) {
    super(name, (Module)parent);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof C03PacketPlayer && 
      this.mc.thePlayer.ticksExisted % 45 == 0 && this.mc.thePlayer.isPlayerMoving())
      this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 11.4514D, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, false)); 
    if (event.getPacket() instanceof S08PacketPlayerPosLook) {
      S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)event.getPacket();
      double x = s08PacketPlayerPosLook.getX();
      double y = s08PacketPlayerPosLook.getY();
      double z = s08PacketPlayerPosLook.getZ();
      double distance = Math.sqrt(x * x + y * y + z * z);
      if (distance <= 8.0D) {
        event.setCancelled(true);
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, true));
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\OldVerus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */