package dev.monsoon.util.entity;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class RotationUtils2 {
  public static Minecraft mc;
  
  public float blockDamage;
  
  public float hitDelay;
  
  public boolean shouldHitBlock(int x, int y, int z, double dist) {
    Block block = getBlock(x, y, z);
    boolean isNotLiquid = !(block instanceof net.minecraft.block.BlockLiquid);
    boolean canSeeBlock = canBlockBeSeen(x, y, z);
    return (isNotLiquid && canSeeBlock);
  }
  
  public void movePlayerToBlock(double posX, double posY, double posZ, float dist) {
    faceBlock(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
    moveForward();
    if (mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && !Keyboard.isKeyDown(this.mc.gameSettings.keyBindJump.getKeyCode()) && !mc.thePlayer.isInWater())
    	mc.thePlayer.jump(); 
    if (canReach(posX, posY, posZ, dist))
      stopMoving(); 
  }
  
  public void placeBlock(int posX, int posY, int posZ) {
	  mc.thePlayer.sendQueue.addToSendQueue((Packet)new C08PacketPlayerBlockPlacement(getBlockPos(posX, posY, posZ), getEnumFacing(posX, posY, posZ).getIndex(), mc.thePlayer.getHeldItem(), 0.0F, 0.0F, 0.0F));
  }
  
  public int getNextSlotInContainer(Container container) {
    for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; i++) {
      if (container.getInventory().get(i) != null)
        return i; 
    } 
    return -1;
  }
  
  public boolean isContainerEmpty(Container container) {
    for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; i++) {
      if (container.getSlot(i).getHasStack())
        return false; 
    } 
    return true;
  }
  
  public boolean canReach(double posX, double posY, double posZ, float distance) {
    double blockDistance = getDistance(posX, posY, posZ);
    return (blockDistance < distance && blockDistance > -distance);
  }
  
  public TileEntityChest getBestEntity(double range) {
    TileEntityChest tempEntity = null;
    double dist = range;
    for (Object i : mc.theWorld.loadedEntityList) {
      TileEntityChest entity = (TileEntityChest)i;
      if (getDistanceToEntity((TileEntity)entity) <= 6.0F) {
        double curDist = getDistanceToEntity((TileEntity)entity);
        if (curDist > dist)
          continue; 
        dist = curDist;
        tempEntity = entity;
      } 
    } 
    return tempEntity;
  }
  
  public float getDistanceToEntity(TileEntity tileEntity) {
    float var2 = (float)(mc.thePlayer.posX - tileEntity.getPos().getX());
    float var3 = (float)(mc.thePlayer.posY - tileEntity.getPos().getY());
    float var4 = (float)(mc.thePlayer.posZ - tileEntity.getPos().getZ());
    return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
  }
  
  private Float[] getRotationsTileEntity(TileEntity entity) {
    double posX = entity.getPos().getX() - mc.thePlayer.posX;
    double posZ = entity.getPos().getY() - mc.thePlayer.posZ;
    double posY = (entity.getPos().getZ() + 1) - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
    double helper = MathHelper.sqrt_double(posX * posX + posZ * posZ);
    float newYaw = (float)Math.toDegrees(-Math.atan(posX / posZ));
    float newPitch = (float)-Math.toDegrees(Math.atan(posY / helper));
    if (posZ < 0.0D && posX < 0.0D) {
      newYaw = (float)(90.0D + Math.toDegrees(Math.atan(posZ / posX)));
    } else if (posZ < 0.0D && posX > 0.0D) {
      newYaw = (float)(-90.0D + Math.toDegrees(Math.atan(posZ / posX)));
    } 
    return new Float[] { Float.valueOf(newYaw), Float.valueOf(newPitch) };
  }
  
  public void breakBlockLegit(int posX, int posY, int posZ, int delay) {
    this.hitDelay++;
    mc.thePlayer.swingItem();
    if (this.blockDamage == 0.0F)
    	mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, getBlockPos(posX, posY, posZ), getEnumFacing(posX, posY, posZ))); 
    if (this.hitDelay >= delay) {
      this.blockDamage += getBlock(posX, posY, posZ).getPlayerRelativeBlockHardness((EntityPlayer)mc.thePlayer, (World)mc.theWorld, new BlockPos(posX, posY, posZ));
      mc.theWorld.sendBlockBreakProgress(mc.thePlayer.getEntityId(), new BlockPos(posX, posY, posZ), (int)(this.blockDamage * 10.0F) - 1);
      if (this.blockDamage >= (this.mc.playerController.isInCreativeMode() ? 0.0F : 1.0F)) {
    	  mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, getBlockPos(posX, posY, posZ), getEnumFacing(posX, posY, posZ)));
        this.mc.playerController.func_178888_a(getBlockPos(posX, posY, posZ), getEnumFacing(posX, posY, posZ));
        this.blockDamage = 0.0F;
        this.hitDelay = 0.0F;
      } 
    } 
  }
  
  public boolean canBlockBeSeen(int posX, int posY, int posZ) {
    Vec3 player = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
    Vec3 block = new Vec3((posX + 0.5F), (posY + 0.5F), (posZ + 0.5F));
    return ((mc.theWorld.rayTraceBlocks(player, block) != null) ? Boolean.valueOf(((mc.theWorld.rayTraceBlocks(player, block)).field_178784_b != null)) : null).booleanValue();
  }
  
  public void breakBlock(double posX, double posY, double posZ) {
	  mc.thePlayer.swingItem();
	  mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, getBlockPos(posX, posY, posZ), getEnumFacing((int)posX, (int)posY, (int)posZ)));
	  mc.thePlayer.sendQueue.addToSendQueue((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, getBlockPos(posX, posY, posZ), getEnumFacing((int)posX, (int)posY, (int)posZ)));
  }
  
  public EnumFacing getEnumFacing(float posX, float posY, float posZ) {
    return EnumFacing.func_176737_a(posX, posY, posZ);
  }
  
  public BlockPos getBlockPos(double x, double y, double z) {
    BlockPos pos = new BlockPos(x, y, z);
    return pos;
  }
  
  public void moveForward() {
    this.mc.gameSettings.keyBindForward.pressed = true;
  }
  
  public void moveLeft() {
    this.mc.gameSettings.keyBindLeft.pressed = true;
  }
  
  public void moveRight() {
    this.mc.gameSettings.keyBindRight.pressed = true;
  }
  
  public void moveBack() {
    this.mc.gameSettings.keyBindBack.pressed = true;
  }
  
  public void stopMoving() {
    this.mc.gameSettings.keyBindForward.pressed = false;
    this.mc.gameSettings.keyBindLeft.pressed = false;
    this.mc.gameSettings.keyBindRight.pressed = false;
    this.mc.gameSettings.keyBindBack.pressed = false;
  }
  
  public Block getBlock(double posX, double posY, double posZ) {
    posX = MathHelper.floor_double(posX);
    posY = MathHelper.floor_double(posY);
    posZ = MathHelper.floor_double(posZ);
    return mc.theWorld.getChunkFromBlockCoords(new BlockPos(posX, posY, posZ)).getBlock(new BlockPos(posX, posY, posZ));
  }
  
  public float getDistance(double x, double y, double z) {
    float xDiff = (float)(mc.thePlayer.posX - x);
    float yDiff = (float)(mc.thePlayer.posY - y);
    float zDiff = (float)(mc.thePlayer.posZ - z);
    return MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
  }
  
  public float getDistanceToVec(double x, double y, double z, double x2, double y2, double z2) {
    float xDiff = (float)(x - x2);
    float yDiff = (float)(y - y2);
    float zDiff = (float)(z - z2);
    return MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
  }
  
  public static float[] faceBlock(double posX, double posY, double posZ) {
    //Helper.mc();
    double diffX = posX - mc.thePlayer.posX;
    //Helper.mc();
    double diffZ = posZ - mc.thePlayer.posZ;
    //Helper.mc();
    //Helper.mc();
    double diffY = posY - mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
    double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
    float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
    float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
   // Helper.mc();
    float lyaw = mc.thePlayer.rotationYaw;
    //Helper.mc();
    float lpitch = mc.thePlayer.rotationPitch;
    return new float[] { lyaw += MathHelper.wrapAngleTo180_float(yaw - lyaw), lpitch += MathHelper.wrapAngleTo180_float(pitch - lpitch) };
  }
  
  public boolean isVisibleFOV(TileEntity tileEntity, EntityPlayerSP thePlayer, int fov) {
    return (((Math.abs(getRotationsTileEntity(tileEntity)[0].floatValue() - thePlayer.rotationYaw) % 360.0F > 180.0F) ? (360.0F - Math.abs(getRotationsTileEntity(tileEntity)[0].floatValue() - thePlayer.rotationYaw) % 360.0F) : (Math.abs(getRotationsTileEntity(tileEntity)[0].floatValue() - thePlayer.rotationYaw) % 360.0F)) <= fov);
  }
  
  public static float[] getRotations(EntityLivingBase ent) {
    double x = ent.posX;
    double z = ent.posZ;
    double y = ent.posY + (ent.getEyeHeight() / 2.0F) - 0.5D;
    return getRotationFromPosition(x, z, y);
  }
  
  public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
    double posX = 0.0D;
    double posY = 0.0D;
    double posZ = 0.0D;
    for (Entity ent : targetList) {
      posX += ent.posX;
      posY += ent.boundingBox.maxY - 2.0D;
      posZ += ent.posZ;
    } 
    posX /= targetList.size();
    posY /= targetList.size();
    posZ /= targetList.size();
    return new float[] { getRotationFromPosition(posX, posZ, posY)[0], getRotationFromPosition(posX, posZ, posY)[1] };
  }
  
  public static float[] getRotationFromPosition(double x, double z, double y) {
    Minecraft.getMinecraft();
    double xDiff = x - mc.thePlayer.posX;
    Minecraft.getMinecraft();
    double zDiff = z - mc.thePlayer.posZ;
    Minecraft.getMinecraft();
    double yDiff = y - mc.thePlayer.posY - 0.6D;
    double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
    float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
    float pitch = (float)-(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
    return new float[] { yaw, pitch };
  }
  
  public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
    float g = 0.006F;
    float sqrt = velocity * velocity * velocity * velocity - 0.006F * (0.006F * d3 * d3 + 2.0F * d1 * velocity * velocity);
    return (float)Math.toDegrees(Math.atan(((velocity * velocity) - Math.sqrt(sqrt)) / (0.006F * d3)));
  }
  
  public static float getNewAngle(float angle) {
    angle %= 360.0F;
    if (angle >= 180.0F)
      angle -= 360.0F; 
    if (angle < -180.0F)
      angle += 360.0F; 
    return angle;
  }
  
  public static float getDistanceBetweenAngles(float angle1, float angle2) {
    float angle3 = Math.abs(angle1 - angle2) % 360.0F;
    if (angle3 > 180.0F)
      angle3 = 360.0F - angle3; 
    return angle3;
  }
}
