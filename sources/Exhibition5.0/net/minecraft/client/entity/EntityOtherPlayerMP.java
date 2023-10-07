// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.entity;

import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.DamageSource;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;

public class EntityOtherPlayerMP extends AbstractClientPlayer
{
    private boolean isItemInUse;
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;
    private static final String __OBFID = "CL_00000939";
    
    public EntityOtherPlayerMP(final World worldIn, final GameProfile p_i45075_2_) {
        super(worldIn, p_i45075_2_);
        this.stepHeight = 0.0f;
        this.noClip = true;
        this.field_71082_cx = 0.25f;
        this.renderDistanceWeight = 10.0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return true;
    }
    
    @Override
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        this.otherPlayerMPX = p_180426_1_;
        this.otherPlayerMPY = p_180426_3_;
        this.otherPlayerMPZ = p_180426_5_;
        this.otherPlayerMPYaw = p_180426_7_;
        this.otherPlayerMPPitch = p_180426_8_;
        this.otherPlayerMPPosRotationIncrements = p_180426_9_;
    }
    
    @Override
    public void onUpdate() {
        this.field_71082_cx = 0.0f;
        super.onUpdate();
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double var1 = this.posX - this.prevPosX;
        final double var2 = this.posZ - this.prevPosZ;
        float var3 = MathHelper.sqrt_double(var1 * var1 + var2 * var2) * 4.0f;
        if (var3 > 1.0f) {
            var3 = 1.0f;
        }
        this.limbSwingAmount += (var3 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
        if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
            final ItemStack var4 = this.inventory.mainInventory[this.inventory.currentItem];
            this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], var4.getItem().getMaxItemUseDuration(var4));
            this.isItemInUse = true;
        }
        else if (this.isItemInUse && !this.isEating()) {
            this.clearItemInUse();
            this.isItemInUse = false;
        }
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.otherPlayerMPPosRotationIncrements > 0) {
            final double var1 = this.posX + (this.otherPlayerMPX - this.posX) / this.otherPlayerMPPosRotationIncrements;
            final double var2 = this.posY + (this.otherPlayerMPY - this.posY) / this.otherPlayerMPPosRotationIncrements;
            final double var3 = this.posZ + (this.otherPlayerMPZ - this.posZ) / this.otherPlayerMPPosRotationIncrements;
            double var4;
            for (var4 = this.otherPlayerMPYaw - this.rotationYaw; var4 < -180.0; var4 += 360.0) {}
            while (var4 >= 180.0) {
                var4 -= 360.0;
            }
            this.rotationYaw += (float)(var4 / this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch += (float)((this.otherPlayerMPPitch - this.rotationPitch) / this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(var1, var2, var3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float var5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var6 = (float)Math.atan(-this.motionY * 0.20000000298023224) * 15.0f;
        if (var5 > 0.1f) {
            var5 = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            var5 = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            var6 = 0.0f;
        }
        this.cameraYaw += (var5 - this.cameraYaw) * 0.4f;
        this.cameraPitch += (var6 - this.cameraPitch) * 0.8f;
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack itemStackIn) {
        if (slotIn == 0) {
            this.inventory.mainInventory[this.inventory.currentItem] = itemStackIn;
        }
        else {
            this.inventory.armorInventory[slotIn - 1] = itemStackIn;
        }
    }
    
    @Override
    public void addChatMessage(final IChatComponent message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(message);
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
        return false;
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }
}
