package me.rhys.client.module.movement.fly.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.event.impl.player.PlayerMotionEvent;
import me.rhys.base.event.impl.player.PlayerMoveEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
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

public class Verus extends ModuleMode<Fly> {
  @Name("Abuse Veruses Poor Teleport System")
  public boolean fuckVerus;
  
  public Verus(String name, Fly parent) {
    super(name, (Module)parent);
    this.fuckVerus = false;
  }
  
  public void onDisable() {
    this.mc.timer.timerSpeed = 1.0F;
  }
  
  public void onEnable() {
    verusDamage();
    this.mc.thePlayer.sendMessage("UwU daddy, Enabled Verus Fly.");
  }
  
  @EventTarget
  void onMotion(PlayerMotionEvent event) {
    EntityPlayerSP player = event.getPlayer();
    (player()).motionY = 0.0D;
    event.setOnGround(true);
    this.mc.timer.timerSpeed = 0.8F;
    this.mc.thePlayer.setInWeb();
    this.mc.thePlayer.setSprinting(true);
    if (this.fuckVerus)
      player.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch); 
  }
  
  @EventTarget
  void onMove(PlayerMoveEvent event) {
    event.setMovementSpeed(6.0D);
    this.mc.thePlayer.setSprinting(false);
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof net.minecraft.network.play.client.C00PacketKeepAlive)
      event.setCancelled(true); 
    if (event.getPacket() instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction)
      event.setCancelled(true); 
  }
  
  public void verusDamage() {
    this.mc.getNetHandler().addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.5D, this.mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem((World)this.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0F, 0.94F, 0.0F));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 3.05D, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
    this.mc.getNetHandler().addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688697815D, this.mc.thePlayer.posZ, true));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\movement\fly\modes\Verus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */