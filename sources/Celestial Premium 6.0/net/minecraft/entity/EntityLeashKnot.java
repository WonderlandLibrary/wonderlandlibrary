/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLeashKnot
extends EntityHanging {
    public EntityLeashKnot(World worldIn) {
        super(worldIn);
    }

    public EntityLeashKnot(World worldIn, BlockPos hangingPositionIn) {
        super(worldIn, hangingPositionIn);
        this.setPosition((double)hangingPositionIn.getX() + 0.5, (double)hangingPositionIn.getY() + 0.5, (double)hangingPositionIn.getZ() + 0.5);
        float f = 0.125f;
        float f1 = 0.1875f;
        float f2 = 0.25f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875));
        this.forceSpawn = true;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition((double)MathHelper.floor(x) + 0.5, (double)MathHelper.floor(y) + 0.5, (double)MathHelper.floor(z) + 0.5);
    }

    @Override
    protected void updateBoundingBox() {
        this.posX = (double)this.hangingPosition.getX() + 0.5;
        this.posY = (double)this.hangingPosition.getY() + 0.5;
        this.posZ = (double)this.hangingPosition.getZ() + 0.5;
    }

    @Override
    public void updateFacingWithBoundingBox(EnumFacing facingDirectionIn) {
    }

    @Override
    public int getWidthPixels() {
        return 9;
    }

    @Override
    public int getHeightPixels() {
        return 9;
    }

    @Override
    public float getEyeHeight() {
        return -0.0625f;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024.0;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_BREAK, 1.0f, 1.0f);
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        if (this.world.isRemote) {
            return true;
        }
        boolean flag = false;
        double d0 = 7.0;
        List<EntityLiving> list = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - 7.0, this.posY - 7.0, this.posZ - 7.0, this.posX + 7.0, this.posY + 7.0, this.posZ + 7.0));
        for (EntityLiving entityliving : list) {
            if (!entityliving.getLeashed() || entityliving.getLeashedToEntity() != player) continue;
            entityliving.setLeashedToEntity(this, true);
            flag = true;
        }
        if (!flag) {
            this.setDead();
            if (player.capabilities.isCreativeMode) {
                for (EntityLiving entityliving1 : list) {
                    if (!entityliving1.getLeashed() || entityliving1.getLeashedToEntity() != this) continue;
                    entityliving1.clearLeashed(true, false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot createKnot(World worldIn, BlockPos fence) {
        EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
        worldIn.spawnEntityInWorld(entityleashknot);
        entityleashknot.playPlaceSound();
        return entityleashknot;
    }

    @Nullable
    public static EntityLeashKnot getKnotForPosition(World worldIn, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        for (EntityLeashKnot entityleashknot : worldIn.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB((double)i - 1.0, (double)j - 1.0, (double)k - 1.0, (double)i + 1.0, (double)j + 1.0, (double)k + 1.0))) {
            if (!entityleashknot.getHangingPosition().equals(pos)) continue;
            return entityleashknot;
        }
        return null;
    }

    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_PLACE, 1.0f, 1.0f);
    }
}

