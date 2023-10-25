package me.rhys.client.module.movement.highjump.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.highjump.HighJump;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class Verus extends ModuleMode<HighJump> {
  @Name("Speed")
  @Clamp(min = 1.0D, max = 10.0D)
  public double movementSpeed;
  
  public Verus(String name, HighJump parent) {
    super(name, (Module)parent);
    this.movementSpeed = 3.0D;
  }
  
  public void onEnable() {
    verusDamage();
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    if (this.mc.thePlayer.onGround)
      event.motionY = this.mc.thePlayer.motionY = this.movementSpeed; 
  }
  
  public void verusDamage() {
    this.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.5D, this.mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem((World)this.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0F, 0.94F, 0.0F));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.05D, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688697815D, this.mc.thePlayer.posZ, true));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\highjump\modes\Verus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */