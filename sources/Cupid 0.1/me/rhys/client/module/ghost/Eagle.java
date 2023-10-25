package me.rhys.client.module.ghost;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class Eagle extends Module {
  public Eagle(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public Block getBlock(BlockPos pos) {
    return this.mc.theWorld.getBlockState(pos).getBlock();
  }
  
  public Block getBlockUnderPlayer(EntityPlayer player) {
    return getBlock(new BlockPos(player.posX, player.posY - 1.0D, player.posZ));
  }
  
  @EventTarget
  public void onUpdate(PlayerUpdateEvent event) {
    if (getBlockUnderPlayer((EntityPlayer)this.mc.thePlayer) instanceof net.minecraft.block.BlockAir) {
      if (this.mc.thePlayer.onGround)
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true); 
    } else if (this.mc.thePlayer.onGround) {
      KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
    } 
  }
  
  public void onEnable() {
    this.mc.thePlayer.setSneaking(false);
    super.onEnable();
  }
  
  public void onDisable() {
    KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
    super.onDisable();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\ghost\Eagle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */