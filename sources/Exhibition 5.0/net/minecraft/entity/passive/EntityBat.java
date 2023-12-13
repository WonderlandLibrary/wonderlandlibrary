// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.passive;

import java.util.Calendar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;

public class EntityBat extends EntityAmbientCreature
{
    private BlockPos spawnPosition;
    private static final String __OBFID = "CL_00001637";
    
    public EntityBat(final World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.9f);
        this.setIsBatHanging(true);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.1f;
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95f;
    }
    
    @Override
    protected String getLivingSound() {
        return (this.getIsBatHanging() && this.rand.nextInt(4) != 0) ? null : "mob.bat.idle";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.bat.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.bat.death";
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    protected void collideWithEntity(final Entity p_82167_1_) {
    }
    
    @Override
    protected void collideWithNearbyEntities() {
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0);
    }
    
    public boolean getIsBatHanging() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setIsBatHanging(final boolean p_82236_1_) {
        final byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_82236_1_) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(var2 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getIsBatHanging()) {
            final double motionX = 0.0;
            this.motionZ = motionX;
            this.motionY = motionX;
            this.motionX = motionX;
            this.posY = MathHelper.floor_double(this.posY) + 1.0 - this.height;
        }
        else {
            this.motionY *= 0.6000000238418579;
        }
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        final BlockPos var1 = new BlockPos(this);
        final BlockPos var2 = var1.offsetUp();
        if (this.getIsBatHanging()) {
            if (!this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
                this.setIsBatHanging(false);
                this.worldObj.playAuxSFXAtEntity(null, 1015, var1, 0);
            }
            else {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = this.rand.nextInt(360);
                }
                if (this.worldObj.getClosestPlayerToEntity(this, 4.0) != null) {
                    this.setIsBatHanging(false);
                    this.worldObj.playAuxSFXAtEntity(null, 1015, var1, 0);
                }
            }
        }
        else {
            if (this.spawnPosition != null && (!this.worldObj.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
                this.spawnPosition = null;
            }
            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }
            final double var3 = this.spawnPosition.getX() + 0.5 - this.posX;
            final double var4 = this.spawnPosition.getY() + 0.1 - this.posY;
            final double var5 = this.spawnPosition.getZ() + 0.5 - this.posZ;
            this.motionX += (Math.signum(var3) * 0.5 - this.motionX) * 0.10000000149011612;
            this.motionY += (Math.signum(var4) * 0.699999988079071 - this.motionY) * 0.10000000149011612;
            this.motionZ += (Math.signum(var5) * 0.5 - this.motionZ) * 0.10000000149011612;
            final float var6 = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0 / 3.141592653589793) - 90.0f;
            final float var7 = MathHelper.wrapAngleTo180_float(var6 - this.rotationYaw);
            this.moveForward = 0.5f;
            this.rotationYaw += var7;
            if (this.rand.nextInt(100) == 0 && this.worldObj.getBlockState(var2).getBlock().isNormalCube()) {
                this.setIsBatHanging(true);
            }
        }
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected void func_180433_a(final double p_180433_1_, final boolean p_180433_3_, final Block p_180433_4_, final BlockPos p_180433_5_) {
    }
    
    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (!this.worldObj.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.dataWatcher.updateObject(16, tagCompund.getByte("BatFlags"));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final BlockPos var1 = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (var1.getY() >= 63) {
            return false;
        }
        final int var2 = this.worldObj.getLightFromNeighbors(var1);
        byte var3 = 4;
        if (this.func_175569_a(this.worldObj.getCurrentDate())) {
            var3 = 7;
        }
        else if (this.rand.nextBoolean()) {
            return false;
        }
        return var2 <= this.rand.nextInt(var3) && super.getCanSpawnHere();
    }
    
    private boolean func_175569_a(final Calendar p_175569_1_) {
        return (p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20) || (p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3);
    }
    
    @Override
    public float getEyeHeight() {
        return this.height / 2.0f;
    }
}
