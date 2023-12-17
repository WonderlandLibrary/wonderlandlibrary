// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.MoverType;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntitySquid extends EntityWaterMob
{
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    public float squidRotation;
    public float prevSquidRotation;
    public float tentacleAngle;
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    private float rotationVelocity;
    private float rotateSpeed;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    
    public EntitySquid(final World worldIn) {
        super(worldIn);
        this.setSize(0.8f, 0.8f);
        this.rand.setSeed(1 + this.getEntityId());
        this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
    }
    
    public static void registerFixesSquid(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySquid.class);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new AIMoveRandom(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SQUID_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SQUID_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SQUID_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SQUID;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if (this.squidRotation > 6.283185307179586) {
            if (this.world.isRemote) {
                this.squidRotation = 6.2831855f;
            }
            else {
                this.squidRotation -= (float)6.283185307179586;
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
                }
                this.world.setEntityState(this, (byte)19);
            }
        }
        if (this.inWater) {
            if (this.squidRotation < 3.1415927f) {
                final float f = this.squidRotation / 3.1415927f;
                this.tentacleAngle = MathHelper.sin(f * f * 3.1415927f) * 3.1415927f * 0.25f;
                if (f > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                    this.rotateSpeed = 1.0f;
                }
                else {
                    this.rotateSpeed *= 0.8f;
                }
            }
            else {
                this.tentacleAngle = 0.0f;
                this.randomMotionSpeed *= 0.9f;
                this.rotateSpeed *= 0.99f;
            }
            if (!this.world.isRemote) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            final float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-(float)MathHelper.atan2(this.motionX, this.motionZ) * 57.295776f - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw += (float)(3.141592653589793 * this.rotateSpeed * 1.5);
            this.squidPitch += (-(float)MathHelper.atan2(f2, this.motionY) * 57.295776f - this.squidPitch) * 0.1f;
        }
        else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * 3.1415927f * 0.25f;
            if (!this.world.isRemote) {
                this.motionX = 0.0;
                this.motionZ = 0.0;
                if (this.isPotionActive(MobEffects.LEVITATION)) {
                    this.motionY += 0.05 * (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY;
                }
                else if (!this.hasNoGravity()) {
                    this.motionY -= 0.08;
                }
                this.motionY *= 0.9800000190734863;
            }
            this.squidPitch += (float)((-90.0f - this.squidPitch) * 0.02);
        }
    }
    
    @Override
    public void travel(final float strafe, final float vertical, final float forward) {
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 45.0 && this.posY < this.world.getSeaLevel() && super.getCanSpawnHere();
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 19) {
            this.squidRotation = 0.0f;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public void setMovementVector(final float randomMotionVecXIn, final float randomMotionVecYIn, final float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }
    
    public boolean hasMovementVector() {
        return this.randomMotionVecX != 0.0f || this.randomMotionVecY != 0.0f || this.randomMotionVecZ != 0.0f;
    }
    
    static class AIMoveRandom extends EntityAIBase
    {
        private final EntitySquid squid;
        
        public AIMoveRandom(final EntitySquid p_i45859_1_) {
            this.squid = p_i45859_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            final int i = this.squid.getIdleTime();
            if (i > 100) {
                this.squid.setMovementVector(0.0f, 0.0f, 0.0f);
            }
            else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
                final float f = this.squid.getRNG().nextFloat() * 6.2831855f;
                final float f2 = MathHelper.cos(f) * 0.2f;
                final float f3 = -0.1f + this.squid.getRNG().nextFloat() * 0.2f;
                final float f4 = MathHelper.sin(f) * 0.2f;
                this.squid.setMovementVector(f2, f3, f4);
            }
        }
    }
}
