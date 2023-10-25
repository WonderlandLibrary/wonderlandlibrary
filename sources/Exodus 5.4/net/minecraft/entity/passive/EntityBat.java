/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import java.util.Calendar;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBat
extends EntityAmbientCreature {
    private BlockPos spawnPosition;

    public EntityBat(World world) {
        super(world);
        this.setSize(0.5f, 0.9f);
        this.setIsBatHanging(true);
    }

    @Override
    protected float getSoundVolume() {
        return 0.1f;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getIsBatHanging()) {
            this.motionZ = 0.0;
            this.motionY = 0.0;
            this.motionX = 0.0;
            this.posY = (double)MathHelper.floor_double(this.posY) + 1.0 - (double)this.height;
        } else {
            this.motionY *= (double)0.6f;
        }
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public float getEyeHeight() {
        return this.height / 2.0f;
    }

    @Override
    protected void updateFallState(double d, boolean bl, Block block, BlockPos blockPos) {
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95f;
    }

    public boolean getIsBatHanging() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }

    private boolean isDateAroundHalloween(Calendar calendar) {
        return calendar.get(2) + 1 == 10 && calendar.get(5) >= 20 || calendar.get(2) + 1 == 11 && calendar.get(5) <= 3;
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.dataWatcher.updateObject(16, nBTTagCompound.getByte("BatFlags"));
    }

    @Override
    public void fall(float f, float f2) {
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected String getLivingSound() {
        return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
    }

    @Override
    public boolean getCanSpawnHere() {
        BlockPos blockPos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (blockPos.getY() >= this.worldObj.func_181545_F()) {
            return false;
        }
        int n = this.worldObj.getLightFromNeighbors(blockPos);
        int n2 = 4;
        if (this.isDateAroundHalloween(this.worldObj.getCurrentDate())) {
            n2 = 7;
        } else if (this.rand.nextBoolean()) {
            return false;
        }
        return n > this.rand.nextInt(n2) ? false : super.getCanSpawnHere();
    }

    public void setIsBatHanging(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFE));
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte(0));
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        BlockPos blockPos = new BlockPos(this);
        BlockPos blockPos2 = blockPos.up();
        if (this.getIsBatHanging()) {
            if (!this.worldObj.getBlockState(blockPos2).getBlock().isNormalCube()) {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity(null, 1015, blockPos, 0);
            } else {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = this.rand.nextInt(360);
                }
                if (this.worldObj.getClosestPlayerToEntity(this, 4.0) != null) {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity(null, 1015, blockPos, 0);
                }
            }
        } else {
            if (!(this.spawnPosition == null || this.worldObj.isAirBlock(this.spawnPosition) && this.spawnPosition.getY() >= 1)) {
                this.spawnPosition = null;
            }
            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }
            double d = (double)this.spawnPosition.getX() + 0.5 - this.posX;
            double d2 = (double)this.spawnPosition.getY() + 0.1 - this.posY;
            double d3 = (double)this.spawnPosition.getZ() + 0.5 - this.posZ;
            this.motionX += (Math.signum(d) * 0.5 - this.motionX) * (double)0.1f;
            this.motionY += (Math.signum(d2) * (double)0.7f - this.motionY) * (double)0.1f;
            this.motionZ += (Math.signum(d3) * 0.5 - this.motionZ) * (double)0.1f;
            float f = (float)(MathHelper.func_181159_b(this.motionZ, this.motionX) * 180.0 / Math.PI) - 90.0f;
            float f2 = MathHelper.wrapAngleTo180_float(f - this.rotationYaw);
            this.moveForward = 0.5f;
            this.rotationYaw += f2;
            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(blockPos2).getBlock().isNormalCube()) {
                this.setIsBatHanging(true);
            }
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0);
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (!this.worldObj.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
        }
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }

    @Override
    protected String getHurtSound() {
        return "mob.bat.hurt";
    }
}

