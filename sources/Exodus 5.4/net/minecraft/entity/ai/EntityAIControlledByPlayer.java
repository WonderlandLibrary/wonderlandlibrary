/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class EntityAIControlledByPlayer
extends EntityAIBase {
    private boolean speedBoosted;
    private int maxSpeedBoostTime;
    private int speedBoostTime;
    private float currentSpeed;
    private final EntityLiving thisEntity;
    private final float maxSpeed;

    public boolean isSpeedBoosted() {
        return this.speedBoosted;
    }

    public EntityAIControlledByPlayer(EntityLiving entityLiving, float f) {
        this.thisEntity = entityLiving;
        this.maxSpeed = f;
        this.setMutexBits(7);
    }

    @Override
    public void resetTask() {
        this.speedBoosted = false;
        this.currentSpeed = 0.0f;
    }

    public boolean isControlledByPlayer() {
        return !this.isSpeedBoosted() && this.currentSpeed > this.maxSpeed * 0.3f;
    }

    public void boostSpeed() {
        this.speedBoosted = true;
        this.speedBoostTime = 0;
        this.maxSpeedBoostTime = this.thisEntity.getRNG().nextInt(841) + 140;
    }

    @Override
    public void updateTask() {
        Object object;
        EntityPlayer entityPlayer = (EntityPlayer)this.thisEntity.riddenByEntity;
        EntityCreature entityCreature = (EntityCreature)this.thisEntity;
        float f = MathHelper.wrapAngleTo180_float(entityPlayer.rotationYaw - this.thisEntity.rotationYaw) * 0.5f;
        if (f > 5.0f) {
            f = 5.0f;
        }
        if (f < -5.0f) {
            f = -5.0f;
        }
        this.thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.thisEntity.rotationYaw + f);
        if (this.currentSpeed < this.maxSpeed) {
            this.currentSpeed += (this.maxSpeed - this.currentSpeed) * 0.01f;
        }
        if (this.currentSpeed > this.maxSpeed) {
            this.currentSpeed = this.maxSpeed;
        }
        int n = MathHelper.floor_double(this.thisEntity.posX);
        int n2 = MathHelper.floor_double(this.thisEntity.posY);
        int n3 = MathHelper.floor_double(this.thisEntity.posZ);
        float f2 = this.currentSpeed;
        if (this.speedBoosted) {
            if (this.speedBoostTime++ > this.maxSpeedBoostTime) {
                this.speedBoosted = false;
            }
            f2 += f2 * 1.15f * MathHelper.sin((float)this.speedBoostTime / (float)this.maxSpeedBoostTime * (float)Math.PI);
        }
        float f3 = 0.91f;
        if (this.thisEntity.onGround) {
            f3 = this.thisEntity.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_float((float)((float)n)), (int)(MathHelper.floor_float((float)((float)n2)) - 1), (int)MathHelper.floor_float((float)((float)n3)))).getBlock().slipperiness * 0.91f;
        }
        float f4 = 0.16277136f / (f3 * f3 * f3);
        float f5 = MathHelper.sin(entityCreature.rotationYaw * (float)Math.PI / 180.0f);
        float f6 = MathHelper.cos(entityCreature.rotationYaw * (float)Math.PI / 180.0f);
        float f7 = entityCreature.getAIMoveSpeed() * f4;
        float f8 = Math.max(f2, 1.0f);
        f8 = f7 / f8;
        float f9 = f2 * f8;
        float f10 = -(f9 * f5);
        float f11 = f9 * f6;
        if (MathHelper.abs(f10) > MathHelper.abs(f11)) {
            if (f10 < 0.0f) {
                f10 -= this.thisEntity.width / 2.0f;
            }
            if (f10 > 0.0f) {
                f10 += this.thisEntity.width / 2.0f;
            }
            f11 = 0.0f;
        } else {
            f10 = 0.0f;
            if (f11 < 0.0f) {
                f11 -= this.thisEntity.width / 2.0f;
            }
            if (f11 > 0.0f) {
                f11 += this.thisEntity.width / 2.0f;
            }
        }
        int n4 = MathHelper.floor_double(this.thisEntity.posX + (double)f10);
        int n5 = MathHelper.floor_double(this.thisEntity.posZ + (double)f11);
        int n6 = MathHelper.floor_float(this.thisEntity.width + 1.0f);
        int n7 = MathHelper.floor_float(this.thisEntity.height + entityPlayer.height + 1.0f);
        int n8 = MathHelper.floor_float(this.thisEntity.width + 1.0f);
        if (n != n4 || n3 != n5) {
            boolean bl;
            object = this.thisEntity.worldObj.getBlockState(new BlockPos(n, n2, n3)).getBlock();
            boolean bl2 = bl = !this.isStairOrSlab((Block)object) && (((Block)object).getMaterial() != Material.air || !this.isStairOrSlab(this.thisEntity.worldObj.getBlockState(new BlockPos(n, n2 - 1, n3)).getBlock()));
            if (bl && WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, n4, n2, n5, n6, n7, n8, false, false, true) == 0 && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, n, n2 + 1, n3, n6, n7, n8, false, false, true) && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, n4, n2 + 1, n5, n6, n7, n8, false, false, true)) {
                entityCreature.getJumpHelper().setJumping();
            }
        }
        if (!entityPlayer.capabilities.isCreativeMode && this.currentSpeed >= this.maxSpeed * 0.5f && this.thisEntity.getRNG().nextFloat() < 0.006f && !this.speedBoosted && (object = entityPlayer.getHeldItem()) != null && ((ItemStack)object).getItem() == Items.carrot_on_a_stick) {
            ((ItemStack)object).damageItem(1, entityPlayer);
            if (((ItemStack)object).stackSize == 0) {
                ItemStack itemStack = new ItemStack(Items.fishing_rod);
                itemStack.setTagCompound(((ItemStack)object).getTagCompound());
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack;
            }
        }
        this.thisEntity.moveEntityWithHeading(0.0f, f2);
    }

    @Override
    public boolean shouldExecute() {
        return this.thisEntity.isEntityAlive() && this.thisEntity.riddenByEntity != null && this.thisEntity.riddenByEntity instanceof EntityPlayer && (this.speedBoosted || this.thisEntity.canBeSteered());
    }

    @Override
    public void startExecuting() {
        this.currentSpeed = 0.0f;
    }

    private boolean isStairOrSlab(Block block) {
        return block instanceof BlockStairs || block instanceof BlockSlab;
    }
}

