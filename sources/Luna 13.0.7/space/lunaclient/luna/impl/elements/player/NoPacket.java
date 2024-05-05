package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventPacketReceive;

@ElementInfo(name="NoPacket", category=Category.PLAYER, description="Allows you to stop incoming packets.")
public class NoPacket
  extends Element
{
  @ModeSetting(name="Mode", currentOption="Rotate", options={"Rotate", "CloseINV", "Both"}, locked=false)
  public Setting mode;
  
  public NoPacket() {}
  
  @EventRegister
  public void onPacketReceive(EventPacketReceive event)
  {
    if ((isToggled() & !(mc.currentScreen instanceof GuiDownloadTerrain))) {
      if ((this.mode.getValString().equalsIgnoreCase("Rotate") | this.mode.getValString().equalsIgnoreCase("All")))
      {
        if ((event.getPacket() instanceof S08PacketPlayerPosLook))
        {
          S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
          packet.field_148936_d = Minecraft.thePlayer.rotationYaw;
          packet.field_148937_e = Minecraft.thePlayer.rotationPitch;
        }
      }
      else if (((this.mode.getValString().equalsIgnoreCase("CloseINV") | this.mode.getValString().equalsIgnoreCase("Both"))) && 
        ((event.getPacket() instanceof S2EPacketCloseWindow | event.getPacket() instanceof S30PacketWindowItems))) {
        event.setCancelled(true);
      }
    }
  }
}
