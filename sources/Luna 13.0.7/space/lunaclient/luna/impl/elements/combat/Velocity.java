package space.lunaclient.luna.impl.elements.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventPacketReceive;
import space.lunaclient.luna.impl.events.EventUpdate;
import space.lunaclient.luna.util.PlayerUtils;

@ElementInfo(name="Velocity", category=Category.COMBAT, description="No Velocity")
public class Velocity
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Normal", options={"Normal", "Pos-Fucker"}, locked=false)
  public Setting mode;
  
  public Velocity() {}
  
  @EventRegister
  public void onUpdate(EventUpdate event)
  {
    if ((isToggled()) && 
      (this.mode.getValString().equalsIgnoreCase("Pos-Fucker"))) {
      if ((Minecraft.thePlayer.onGround & Minecraft.thePlayer.hurtTime > 3))
      {
        PlayerUtils.tpRel(0.0D, 0.1D, 0.0D);
        Minecraft.thePlayer.setVelocity(0.0D, -1.0E-7D, 0.0D);
      }
    }
  }
  
  @EventRegister
  public void onPacketReceive(EventPacketReceive eventPacketReceive)
  {
    if (!isToggled()) {
      return;
    }
    if (this.mode.getValString().equalsIgnoreCase("Normal"))
    {
      if ((eventPacketReceive.getPacket() instanceof S12PacketEntityVelocity | eventPacketReceive.getPacket() instanceof S27PacketExplosion)) {
        eventPacketReceive.setCancelled(true);
      }
    }
    else if (this.mode.getValString().equalsIgnoreCase("Pos-Fucker")) {
      if ((eventPacketReceive.getPacket() instanceof S08PacketPlayerPosLook & eventPacketReceive.getPacket() instanceof S12PacketEntityVelocity | eventPacketReceive.getPacket() instanceof S08PacketPlayerPosLook & eventPacketReceive.getPacket() instanceof S27PacketExplosion)) {
        eventPacketReceive.setCancelled(true);
      }
    }
  }
}
