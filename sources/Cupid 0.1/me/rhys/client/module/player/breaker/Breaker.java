package me.rhys.client.module.player.breaker;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.player.PlayerUpdateEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.base.util.entity.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Breaker extends Module {
  private BlockPos globalPos;
  
  public Breaker(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.range = 4.0F;
    this.EnderStone = false;
    this.Bed = true;
    this.Cake = false;
    this.Egg = false;
    this.Chest = false;
    this.Ore = false;
  }
  
  public static TimeHelper timer = new TimeHelper();
  
  @Name("Range")
  @Clamp(min = 3.0D, max = 6.0D)
  public float range;
  
  @Name("EnderStone")
  public boolean EnderStone;
  
  @Name("Bed")
  public boolean Bed;
  
  @Name("Cake")
  public boolean Cake;
  
  @Name("Egg")
  public boolean Egg;
  
  @Name("Chest")
  public boolean Chest;
  
  @Name("Ore")
  public boolean Ore;
  
  @EventTarget
  private void onUpdatePre(PlayerUpdateEvent event) {
    if (event.isCancelled()) {
      this.globalPos = null;
      return;
    } 
    this.globalPos = null;
    if (event.isCancelled() || this.mc.thePlayer.ticksExisted % 20 == 0 || this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer)
      return; 
    float radius = this.range + 2.0F, y = radius;
    while (y >= -radius) {
      float x = -radius;
      while (x <= radius) {
        float z = -radius;
        while (z <= radius) {
          BlockPos pos = new BlockPos(this.mc.thePlayer.posX - 0.5D + x, this.mc.thePlayer.posY - 0.5D + y, this.mc.thePlayer.posZ - 0.5D + z);
          Block block = this.mc.theWorld.getBlockState(pos).getBlock();
          if (getFacingDirection(pos) != null && this.mc.thePlayer.getDistance(this.mc.thePlayer.posX + x, this.mc.thePlayer.posY + y, this.mc.thePlayer.posZ + z) < this.mc.playerController.getBlockReachDistance() && isValidBlock(block)) {
            float[] rotations = BlockUtil.getBlockRotations(pos.getX(), pos.getY(), pos.getZ());
            this.globalPos = pos;
            return;
          } 
          z++;
        } 
        x++;
      } 
      y--;
    } 
  }
  
  @EventTarget
  public void onUpdatePost(PlayerUpdateEvent event) {
    if (this.globalPos != null && !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer)) {
      EnumFacing direction;
      if ((direction = getFacingDirection(this.globalPos)) != null);
      if (timer.isDelayComplete(500.0D)) {
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.globalPos, EnumFacing.DOWN));
        this.mc.thePlayer.sendQueue
          .addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.globalPos, EnumFacing.DOWN));
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.globalPos, EnumFacing.DOWN));
        this.mc.thePlayer.swingItem();
        timer.reset();
      } 
    } 
  }
  
  private boolean isValidBlock(Block block) {
    if (Block.getIdFromBlock(block) == 121 && this.EnderStone)
      return true; 
    if (Block.getIdFromBlock(block) == 26 && this.Bed)
      return true; 
    if (block instanceof net.minecraft.block.BlockDragonEgg && this.Egg)
      return true; 
    if (block instanceof net.minecraft.block.BlockCake && this.Cake)
      return true; 
    if (block instanceof net.minecraft.block.BlockOre && this.Ore)
      return true; 
    if (block instanceof net.minecraft.block.BlockChest && this.Chest)
      return true; 
    return false;
  }
  
  private EnumFacing getFacingDirection(BlockPos pos) {
    EnumFacing direction = null;
    if (!this.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.UP;
    } else if (!this.mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.DOWN;
    } else if (!this.mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.EAST;
    } else if (!this.mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.WEST;
    } else if (!this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.SOUTH;
    } else if (!this.mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
      direction = EnumFacing.NORTH;
    } 
    return direction;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\breaker\Breaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */