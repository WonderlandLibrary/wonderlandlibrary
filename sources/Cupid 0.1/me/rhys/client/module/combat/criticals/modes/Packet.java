package me.rhys.client.module.combat.criticals.modes;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.util.Timer;
import me.rhys.client.module.combat.criticals.Criticals;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Packet extends ModuleMode<Criticals> {
  private final double[] offsets;
  
  private final Timer timer;
  
  public Packet(String name, Criticals parent) {
    super(name, (Module)parent);
    this.offsets = new double[] { 0.05D, 0.0D, 0.05D, 0.0D };
    this.timer = new Timer();
  }
  
  public void doCriticalHit() {
    if (this.timer.hasReached(60.0D) && this.mc.thePlayer.onGround) {
      this.timer.reset();
      for (double offset : this.offsets)
        this.mc.thePlayer.sendQueue.addToSendQueue((net.minecraft.network.Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + offset, this.mc.thePlayer.posZ, false)); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\criticals\modes\Packet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */