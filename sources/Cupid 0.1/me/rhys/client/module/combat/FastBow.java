package me.rhys.client.module.combat;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow extends Module {
  public FastBow(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  @EventTarget
  private void onPostUpdate(PlayerUpdateEvent event) {
    if (this.mc.thePlayer.getItemInUse().getItem() instanceof net.minecraft.item.ItemBow && this.mc.thePlayer.getItemInUseDuration() == 16) {
      int i = 0;
      while (i < 5) {
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer(true));
        i++;
      } 
      this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
      this.mc.thePlayer.stopUsingItem();
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\combat\FastBow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */