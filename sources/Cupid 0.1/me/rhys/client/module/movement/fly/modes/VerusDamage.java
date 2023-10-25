package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.movement.fly.Fly;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class VerusDamage extends ModuleMode<Fly> {
  @Name("Speed")
  @Clamp(min = 1.0D, max = 5.0D)
  public double speed = 3.0D;
  
  @Name("Ground Spoof")
  public boolean groundSpoof = false;
  
  public VerusDamage(String name, Fly parent) {
    super(name, (Module)parent);
  }
  
  public void onEnable() {
    verusDamage();
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    event.setOnGround(this.groundSpoof);
  }
  
  @EventTarget
  void playerMove(PlayerMoveEvent event) {
    EntityPlayerSP player = event.getPlayer();
    if (player == null)
      return; 
    event.motionY = player.motionY = this.mc.gameSettings.keyBindJump.pressed ? 0.5D : (this.mc.gameSettings.keyBindSneak.pressed ? -0.5D : 0.0D);
    if (this.mc.thePlayer.isPlayerMoving()) {
      event.setMovementSpeed(this.speed);
    } else {
      event.motionZ = event.motionX = 0.0D;
    } 
  }
  
  public void verusDamage() {
    this.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.5D, this.mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem((World)this.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0F, 0.94F, 0.0F));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.05D, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688697815D, this.mc.thePlayer.posZ, true));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\VerusDamage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */