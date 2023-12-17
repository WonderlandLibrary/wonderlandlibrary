// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import java.util.Calendar;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.datasync.DataParameter;

public class EntityBat extends EntityAmbientCreature
{
    private static final DataParameter<Byte> HANGING;
    private BlockPos spawnPosition;
    
    public EntityBat(final World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.9f);
        this.setIsBatHanging(true);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityBat.HANGING, (Byte)0);
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.1f;
    }
    
    @Override
    protected float getSoundPitch() {
        return super.getSoundPitch() * 0.95f;
    }
    
    @Nullable
    public SoundEvent getAmbientSound() {
        return (this.getIsBatHanging() && this.rand.nextInt(4) != 0) ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_BAT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }
    
    @Override
    public boolean canBePushed() {
        return false;
    }
    
    @Override
    protected void collideWithEntity(final Entity entityIn) {
    }
    
    @Override
    protected void collideWithNearbyEntities() {
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
    }
    
    public boolean getIsBatHanging() {
        return (this.dataManager.get(EntityBat.HANGING) & 0x1) != 0x0;
    }
    
    public void setIsBatHanging(final boolean isHanging) {
        final byte b0 = this.dataManager.get(EntityBat.HANGING);
        if (isHanging) {
            this.dataManager.set(EntityBat.HANGING, (byte)(b0 | 0x1));
        }
        else {
            this.dataManager.set(EntityBat.HANGING, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getIsBatHanging()) {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
            this.posY = MathHelper.floor(this.posY) + 1.0 - this.height;
        }
        else {
            this.motionY *= 0.6000000238418579;
        }
    }
    
    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        final BlockPos blockpos = new BlockPos(this);
        final BlockPos blockpos2 = blockpos.up();
        if (this.getIsBatHanging()) {
            if (this.world.getBlockState(blockpos2).isNormalCube()) {
                if (this.rand.nextInt(200) == 0) {
                    this.rotationYawHead = (float)this.rand.nextInt(360);
                }
                if (this.world.getNearestPlayerNotCreative(this, 4.0) != null) {
                    this.setIsBatHanging(false);
                    this.world.playEvent(null, 1025, blockpos, 0);
                }
            }
            else {
                this.setIsBatHanging(false);
                this.world.playEvent(null, 1025, blockpos, 0);
            }
        }
        else {
            if (this.spawnPosition != null && (!this.world.isAirBlock(this.spawnPosition) || this.spawnPosition.getY() < 1)) {
                this.spawnPosition = null;
            }
            if (this.spawnPosition == null || this.rand.nextInt(30) == 0 || this.spawnPosition.distanceSq((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0) {
                this.spawnPosition = new BlockPos((int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7), (int)this.posY + this.rand.nextInt(6) - 2, (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7));
            }
            final double d0 = this.spawnPosition.getX() + 0.5 - this.posX;
            final double d2 = this.spawnPosition.getY() + 0.1 - this.posY;
            final double d3 = this.spawnPosition.getZ() + 0.5 - this.posZ;
            this.motionX += (Math.signum(d0) * 0.5 - this.motionX) * 0.10000000149011612;
            this.motionY += (Math.signum(d2) * 0.699999988079071 - this.motionY) * 0.10000000149011612;
            this.motionZ += (Math.signum(d3) * 0.5 - this.motionZ) * 0.10000000149011612;
            final float f = (float)(MathHelper.atan2(this.motionZ, this.motionX) * 57.29577951308232) - 90.0f;
            final float f2 = MathHelper.wrapDegrees(f - this.rotationYaw);
            this.moveForward = 0.5f;
            this.rotationYaw += f2;
            if (this.rand.nextInt(100) == 0 && this.world.getBlockState(blockpos2).isNormalCube()) {
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
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
    }
    
    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.world.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    public static void registerFixesBat(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityBat.class);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.dataManager.set(EntityBat.HANGING, compound.getByte("BatFlags"));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setByte("BatFlags", this.dataManager.get(EntityBat.HANGING));
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (blockpos.getY() >= this.world.getSeaLevel()) {
            return false;
        }
        final int i = this.world.getLightFromNeighbors(blockpos);
        int j = 4;
        if (this.isDateAroundHalloween(this.world.getCurrentDate())) {
            j = 7;
        }
        else if (this.rand.nextBoolean()) {
            return false;
        }
        return i <= this.rand.nextInt(j) && super.getCanSpawnHere();
    }
    
    private boolean isDateAroundHalloween(final Calendar p_175569_1_) {
        return (p_175569_1_.get(2) + 1 == 10 && p_175569_1_.get(5) >= 20) || (p_175569_1_.get(2) + 1 == 11 && p_175569_1_.get(5) <= 3);
    }
    
    @Override
    public float getEyeHeight() {
        return this.height / 2.0f;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_BAT;
    }
    
    static {
        HANGING = EntityDataManager.createKey(EntityBat.class, DataSerializers.BYTE);
    }
}
