package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityOtherPlayerMP
  extends AbstractClientPlayer
{
  private boolean isItemInUse;
  private int otherPlayerMPPosRotationIncrements;
  private double otherPlayerMPX;
  private double otherPlayerMPY;
  private double otherPlayerMPZ;
  private double otherPlayerMPYaw;
  private double otherPlayerMPPitch;
  private static final String __OBFID = "CL_00000939";
  
  public EntityOtherPlayerMP(World worldIn, GameProfile p_i45075_2_)
  {
    super(worldIn, p_i45075_2_);
    this.stepHeight = 0.0F;
    this.noClip = true;
    this.field_71082_cx = 0.25F;
    this.renderDistanceWeight = 10.0D;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    return true;
  }
  
  public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
  {
    this.otherPlayerMPX = p_180426_1_;
    this.otherPlayerMPY = p_180426_3_;
    this.otherPlayerMPZ = p_180426_5_;
    this.otherPlayerMPYaw = p_180426_7_;
    this.otherPlayerMPPitch = p_180426_8_;
    this.otherPlayerMPPosRotationIncrements = p_180426_9_;
  }
  
  public void onUpdate()
  {
    this.field_71082_cx = 0.0F;
    super.onUpdate();
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double var1 = this.posX - this.prevPosX;
    double var3 = this.posZ - this.prevPosZ;
    float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3) * 4.0F;
    if (var5 > 1.0F) {
      var5 = 1.0F;
    }
    this.limbSwingAmount += (var5 - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
    if ((!this.isItemInUse) && (isEating()) && (this.inventory.mainInventory[this.inventory.currentItem] != null))
    {
      ItemStack var6 = this.inventory.mainInventory[this.inventory.currentItem];
      setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], var6.getItem().getMaxItemUseDuration(var6));
      this.isItemInUse = true;
    }
    else if ((this.isItemInUse) && (!isEating()))
    {
      clearItemInUse();
      this.isItemInUse = false;
    }
  }
  
  public void onLivingUpdate()
  {
    if (this.otherPlayerMPPosRotationIncrements > 0)
    {
      double var1 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
      double var3 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
      double var5 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
      for (double var7 = this.otherPlayerMPYaw - this.rotationYaw; var7 < -180.0D; var7 += 360.0D) {}
      while (var7 >= 180.0D) {
        var7 -= 360.0D;
      }
      this.rotationYaw = ((float)(this.rotationYaw + var7 / this.otherPlayerMPPosRotationIncrements));
      this.rotationPitch = ((float)(this.rotationPitch + (this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements));
      this.otherPlayerMPPosRotationIncrements -= 1;
      setPosition(var1, var3, var5);
      setRotation(this.rotationYaw, this.rotationPitch);
    }
    this.prevCameraYaw = this.cameraYaw;
    updateArmSwingProgress();
    float var9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
    float var2 = (float)Math.atan(-this.motionY * 0.20000000298023224D) * 15.0F;
    if (var9 > 0.1F) {
      var9 = 0.1F;
    }
    if ((!this.onGround) || (getHealth() <= 0.0F)) {
      var9 = 0.0F;
    }
    if ((this.onGround) || (getHealth() <= 0.0F)) {
      var2 = 0.0F;
    }
    this.cameraYaw += (var9 - this.cameraYaw) * 0.4F;
    this.cameraPitch += (var2 - this.cameraPitch) * 0.8F;
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
  {
    if (slotIn == 0) {
      this.inventory.mainInventory[this.inventory.currentItem] = itemStackIn;
    } else {
      this.inventory.armorInventory[(slotIn - 1)] = itemStackIn;
    }
  }
  
  public void addChatMessage(IChatComponent message)
  {
    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
  }
  
  public boolean canCommandSenderUseCommand(int permissionLevel, String command)
  {
    return false;
  }
  
  public BlockPos getPosition()
  {
    return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
  }
}
