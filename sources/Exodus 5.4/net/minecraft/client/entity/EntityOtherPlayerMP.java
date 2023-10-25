/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityOtherPlayerMP
extends AbstractClientPlayer {
    private double otherPlayerMPYaw;
    private double otherPlayerMPZ;
    private double otherPlayerMPY;
    private boolean isItemInUse;
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPPitch;

    @Override
    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
        if (n == 0) {
            this.inventory.mainInventory[this.inventory.currentItem] = itemStack;
        } else {
            this.inventory.armorInventory[n - 1] = itemStack;
        }
    }

    public EntityOtherPlayerMP(World world, GameProfile gameProfile) {
        super(world, gameProfile);
        this.stepHeight = 0.0f;
        this.noClip = true;
        this.renderOffsetY = 0.25f;
        this.renderDistanceWeight = 10.0;
    }

    @Override
    public void onLivingUpdate() {
        if (this.otherPlayerMPPosRotationIncrements > 0) {
            double d = this.posX + (this.otherPlayerMPX - this.posX) / (double)this.otherPlayerMPPosRotationIncrements;
            double d2 = this.posY + (this.otherPlayerMPY - this.posY) / (double)this.otherPlayerMPPosRotationIncrements;
            double d3 = this.posZ + (this.otherPlayerMPZ - this.posZ) / (double)this.otherPlayerMPPosRotationIncrements;
            double d4 = this.otherPlayerMPYaw - (double)this.rotationYaw;
            while (d4 < -180.0) {
                d4 += 360.0;
            }
            while (d4 >= 180.0) {
                d4 -= 360.0;
            }
            this.rotationYaw = (float)((double)this.rotationYaw + d4 / (double)this.otherPlayerMPPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.otherPlayerMPPitch - (double)this.rotationPitch) / (double)this.otherPlayerMPPosRotationIncrements);
            --this.otherPlayerMPPosRotationIncrements;
            this.setPosition(d, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        this.prevCameraYaw = this.cameraYaw;
        this.updateArmSwingProgress();
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)Math.atan(-this.motionY * (double)0.2f) * 15.0f;
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            f2 = 0.0f;
        }
        this.cameraYaw += (f - this.cameraYaw) * 0.4f;
        this.cameraPitch += (f2 - this.cameraPitch) * 0.8f;
    }

    @Override
    public boolean canCommandSenderUseCommand(int n, String string) {
        return false;
    }

    @Override
    public void addChatMessage(IChatComponent iChatComponent) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(iChatComponent);
    }

    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.otherPlayerMPX = d;
        this.otherPlayerMPY = d2;
        this.otherPlayerMPZ = d3;
        this.otherPlayerMPYaw = f;
        this.otherPlayerMPPitch = f2;
        this.otherPlayerMPPosRotationIncrements = n;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return true;
    }

    @Override
    public void onUpdate() {
        this.renderOffsetY = 0.0f;
        super.onUpdate();
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d = this.posX - this.prevPosX;
        double d2 = this.posZ - this.prevPosZ;
        float f = MathHelper.sqrt_double(d * d + d2 * d2) * 4.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        this.limbSwingAmount += (f - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
        if (!this.isItemInUse && this.isEating() && this.inventory.mainInventory[this.inventory.currentItem] != null) {
            ItemStack itemStack = this.inventory.mainInventory[this.inventory.currentItem];
            this.setItemInUse(this.inventory.mainInventory[this.inventory.currentItem], itemStack.getItem().getMaxItemUseDuration(itemStack));
            this.isItemInUse = true;
        } else if (this.isItemInUse && !this.isEating()) {
            this.clearItemInUse();
            this.isItemInUse = false;
        }
    }
}

