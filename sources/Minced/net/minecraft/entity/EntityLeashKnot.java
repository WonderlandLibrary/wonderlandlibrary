// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.block.BlockFence;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging
{
    public EntityLeashKnot(final World worldIn) {
        super(worldIn);
    }
    
    public EntityLeashKnot(final World worldIn, final BlockPos hangingPositionIn) {
        super(worldIn, hangingPositionIn);
        this.setPosition(hangingPositionIn.getX() + 0.5, hangingPositionIn.getY() + 0.5, hangingPositionIn.getZ() + 0.5);
        final float f = 0.125f;
        final float f2 = 0.1875f;
        final float f3 = 0.25f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875, this.posY - 0.25 + 0.125, this.posZ - 0.1875, this.posX + 0.1875, this.posY + 0.25 + 0.125, this.posZ + 0.1875));
        this.forceSpawn = true;
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        super.setPosition(MathHelper.floor(x) + 0.5, MathHelper.floor(y) + 0.5, MathHelper.floor(z) + 0.5);
    }
    
    @Override
    protected void updateBoundingBox() {
        this.posX = this.hangingPosition.getX() + 0.5;
        this.posY = this.hangingPosition.getY() + 0.5;
        this.posZ = this.hangingPosition.getZ() + 0.5;
    }
    
    public void updateFacingWithBoundingBox(final EnumFacing facingDirectionIn) {
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
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 1024.0;
    }
    
    @Override
    public void onBroken(@Nullable final Entity brokenEntity) {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_BREAK, 1.0f, 1.0f);
    }
    
    @Override
    public boolean writeToNBTOptional(final NBTTagCompound compound) {
        return false;
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (this.world.isRemote) {
            return true;
        }
        boolean flag = false;
        final double d0 = 7.0;
        final List<EntityLiving> list = this.world.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, new AxisAlignedBB(this.posX - 7.0, this.posY - 7.0, this.posZ - 7.0, this.posX + 7.0, this.posY + 7.0, this.posZ + 7.0));
        for (final EntityLiving entityliving : list) {
            if (entityliving.getLeashed() && entityliving.getLeashHolder() == player) {
                entityliving.setLeashHolder(this, true);
                flag = true;
            }
        }
        if (!flag) {
            this.setDead();
            if (player.capabilities.isCreativeMode) {
                for (final EntityLiving entityliving2 : list) {
                    if (entityliving2.getLeashed() && entityliving2.getLeashHolder() == this) {
                        entityliving2.clearLeashed(true, false);
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }
    
    public static EntityLeashKnot createKnot(final World worldIn, final BlockPos fence) {
        final EntityLeashKnot entityleashknot = new EntityLeashKnot(worldIn, fence);
        worldIn.spawnEntity(entityleashknot);
        entityleashknot.playPlaceSound();
        return entityleashknot;
    }
    
    @Nullable
    public static EntityLeashKnot getKnotForPosition(final World worldIn, final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getY();
        final int k = pos.getZ();
        for (final EntityLeashKnot entityleashknot : worldIn.getEntitiesWithinAABB((Class<? extends EntityLeashKnot>)EntityLeashKnot.class, new AxisAlignedBB(i - 1.0, j - 1.0, k - 1.0, i + 1.0, j + 1.0, k + 1.0))) {
            if (entityleashknot.getHangingPosition().equals(pos)) {
                return entityleashknot;
            }
        }
        return null;
    }
    
    @Override
    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_PLACE, 1.0f, 1.0f);
    }
}
