package space.lunaclient.luna.impl.elements.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.BooleanSetting;
import space.lunaclient.luna.api.setting.ModeSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventSlowDown;

@ElementInfo(name="NoSlowdown", category=Category.PLAYER)
public class NoSlow
  extends Element
{
  @ModeSetting(name="Normal", currentOption="Normal", options={"Normal", "AAC"}, locked=false)
  public static Setting mode;
  @BooleanSetting(name="Release", booleanValue=false)
  public static Setting release;
  
  public NoSlow() {}
  
  public void onDisable()
  {
    if (mode.getValString().equalsIgnoreCase("Effect")) {
      Minecraft.thePlayer.removePotionEffect(3);
    }
  }
  
  @EventRegister
  public void onUpdate(EventSlowDown eventSlowDown)
  {
    if (Minecraft.thePlayer.isUsingItem())
    {
      if ((mode.getValString().equalsIgnoreCase("AAC") & Minecraft.thePlayer.isMoving())) {
        Minecraft.thePlayer.setSpeed(0.05D);
      }
      eventSlowDown.setCancelled(false);
      eventSlowDown.setCancelled(true);
      if (release.getValBoolean()) {
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, Minecraft.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
      }
    }
  }
}
