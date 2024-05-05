package net.minecraft.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySquid
extends EntityWaterMob {
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
    private float field_70871_bB;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;

    public EntitySquid(World worldIn) {
        super(worldIn);
        this.setSize(0.95f, 0.95f);
        this.rand.setSeed((long)(1 + this.getEntityId()));
        this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.tasks.addTask(0, (EntityAIBase)new AIMoveRandom(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
    }

    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    protected String getLivingSound() {
        return null;
    }

    protected String getHurtSound() {
        return null;
    }

    protected String getDeathSound() {
        return null;
    }

    protected float getSoundVolume() {
        return 0.4f;
    }

    protected Item getDropItem() {
        return null;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(3 + lootingModifier) + 1;
        for (int j = 0; j < i; ++j) {
            this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0f);
        }
    }

    public boolean isInWater() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, (double)-0.6f, 0.0), Material.water, (Entity)this);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;
        if ((double)this.squidRotation > Math.PI * 2) {
            if (this.worldObj.isRemote) {
                this.squidRotation = (float)Math.PI * 2;
            } else {
                this.squidRotation = (float)((double)this.squidRotation - Math.PI * 2);
                if (this.rand.nextInt(10) == 0) {
                    this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
                }
                this.worldObj.setEntityState((Entity)this, (byte)19);
            }
        }
        if (this.inWater) {
            if (this.squidRotation < (float)Math.PI) {
                float f = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin((float)(f * f * (float)Math.PI)) * (float)Math.PI * 0.25f;
                if ((double)f > 0.75) {
                    this.randomMotionSpeed = 1.0f;
                    this.field_70871_bB = 1.0f;
                } else {
                    this.field_70871_bB *= 0.8f;
                }
            } else {
                this.tentacleAngle = 0.0f;
                this.randomMotionSpeed *= 0.9f;
                this.field_70871_bB *= 0.99f;
            }
            if (!this.worldObj.isRemote) {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }
            float f1 = MathHelper.sqrt_double((double)(this.motionX * this.motionX + this.motionZ * this.motionZ));
            this.renderYawOffset += (-((float)MathHelper.atan2((double)this.motionX, (double)this.motionZ)) * 180.0f / (float)Math.PI - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.field_70871_bB * 1.5);
            this.squidPitch += (-((float)MathHelper.atan2((double)f1, (double)this.motionY)) * 180.0f / (float)Math.PI - this.squidPitch) * 0.1f;
        } else {
            this.tentacleAngle = MathHelper.abs((float)MathHelper.sin((float)this.squidRotation)) * (float)Math.PI * 0.25f;
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY -= 0.08;
                this.motionY *= (double)0.98f;
                this.motionZ = 0.0;
            }
            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0f - this.squidPitch) * 0.02);
        }
    }

    public void moveEntityWithHeading(float strafe, float forward) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    public boolean getCanSpawnHere() {
        return this.posY > 45.0 && this.posY < (double)this.worldObj.getSeaLevel() && super.getCanSpawnHere();
    }

    public void handleStatusUpdate(byte id) {
        if (id == 19) {
            this.squidRotation = 0.0f;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public void func_175568_b(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
        this.randomMotionVecX = randomMotionVecXIn;
        this.randomMotionVecY = randomMotionVecYIn;
        this.randomMotionVecZ = randomMotionVecZIn;
    }

    public boolean func_175567_n() {
        return this.randomMotionVecX != 0.0f || this.randomMotionVecY != 0.0f || this.randomMotionVecZ != 0.0f;
    }

    static /* synthetic */ boolean access$000(EntitySquid x0) {
        return x0.inWater;
    }
}
