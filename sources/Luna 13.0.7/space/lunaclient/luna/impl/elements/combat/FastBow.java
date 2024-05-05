package space.lunaclient.luna.impl.elements.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import space.lunaclient.luna.api.element.Category;
import space.lunaclient.luna.api.element.Element;
import space.lunaclient.luna.api.element.ElementInfo;
import space.lunaclient.luna.api.event.EventRegister;
import space.lunaclient.luna.api.setting.DoubleSetting;
import space.lunaclient.luna.api.setting.Setting;
import space.lunaclient.luna.impl.events.EventUpdate;

@ElementInfo(name="FastBow", category=Category.COMBAT, description="Fire multiple arrows at once")
public class FastBow
  extends Element
{
  int counter;
  @DoubleSetting(name="Packets", currentValue=18.0D, minValue=1.0D, maxValue=30.0D, onlyInt=false, locked=false)
  private Setting packets;
  
  public FastBow() {}
  
  @EventRegister
  public void onUpdate(EventUpdate e)
  {
    if (!isToggled()) {
      return;
    }
    if ((Minecraft.thePlayer.onGround & Minecraft.thePlayer.getCurrentEquippedItem() != null & Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow & mc.gameSettings.keyBindUseItem.pressed))
    {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory
        .getCurrentItem()));
      
      int index = 0;
      while (index < 20)
      {
        if (!Minecraft.thePlayer.isDead) {
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9D, Minecraft.thePlayer.posZ, Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch, true));
        }
        index++;
      }
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    }
    if ((Minecraft.thePlayer.onGround & Minecraft.thePlayer.getCurrentEquippedItem() != null & Minecraft.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow & mc.gameSettings.keyBindUseItem.pressed))
    {
      Minecraft.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
      if (Minecraft.thePlayer.ticksExisted % 2 == 0)
      {
        Minecraft.thePlayer.setItemInUseCount(12);
        this.counter += 1;
        if (this.counter > 1)
        {
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.06D, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
          
          Minecraft.thePlayer.swingItem();
        }
        if (this.counter > 0)
        {
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 20.0D, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
          
          this.counter = 0;
        }
        for (int index = 0; index < this.packets.getValDouble(); index++)
        {
          Minecraft.getNetHandler().netManager.flushOutboundQueue();
          Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-9D, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
        }
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      }
    }
  }
}
