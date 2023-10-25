package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.Event;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0CPacketInput;

public class C0C extends ModuleMode<Disabler> {
  @Name("Custom Values")
  public boolean customValues;
  
  @Name("Strafe Value")
  @Clamp(min = 0.0D, max = 9.0D)
  public double strafeValue;
  
  @Name("Forward Value")
  @Clamp(min = 0.0D, max = 9.0D)
  public double forwardValue;
  
  @Name("Min Value")
  public boolean minValues;
  
  @Name("Jumping")
  public boolean jumping;
  
  @Name("Sneaking")
  public boolean sneaking;
  
  @Name("Custom Tick")
  public boolean customTick;
  
  @Name("Send Tick")
  @Clamp(min = 1.0D, max = 500.0D)
  public int customSendTick;
  
  public C0C(String name, Disabler parent) {
    super(name, (Module)parent);
    this.customValues = false;
    this.strafeValue = 1.2D;
    this.forwardValue = 1.2D;
    this.minValues = false;
    this.jumping = true;
    this.sneaking = true;
    this.customSendTick = 20;
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    if (event.getType() == Event.Type.PRE) {
      if (this.customTick && this.mc.thePlayer.ticksExisted % this.customSendTick != 0)
        return; 
      float x = this.customValues ? (float)this.strafeValue : (this.minValues ? Float.MIN_VALUE : Float.MAX_VALUE);
      float z = this.customValues ? (float)this.forwardValue : (this.minValues ? Float.MIN_VALUE : Float.MAX_VALUE);
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0CPacketInput(x, z, this.jumping, this.sneaking));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\C0C.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */