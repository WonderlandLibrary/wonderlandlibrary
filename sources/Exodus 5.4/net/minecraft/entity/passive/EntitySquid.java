/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySquid
extends EntityWaterMob {
    public float lastTentacleAngle;
    private float randomMotionVecX;
    private float field_70871_bB;
    public float squidYaw;
    private float randomMotionVecZ;
    public float tentacleAngle;
    public float prevSquidRotation;
    private float rotationVelocity;
    public float squidRotation;
    private float randomMotionVecY;
    public float prevSquidPitch;
    private float randomMotionSpeed;
    public float prevSquidYaw;
    public float squidPitch;

    @Override
    protected String getDeathSound() {
        return null;
    }

    @Override
    protected String getHurtSound() {
        return null;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
    }

    public boolean func_175567_n() {
        return this.randomMotionVecX != 0.0f || this.randomMotionVecY != 0.0f || this.randomMotionVecZ != 0.0f;
    }

    @Override
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
                this.worldObj.setEntityState(this, (byte)19);
            }
        }
        if (this.inWater) {
            float f;
            if (this.squidRotation < (float)Math.PI) {
                f = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25f;
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
            f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float)MathHelper.func_181159_b(this.motionX, this.motionZ)) * 180.0f / (float)Math.PI - this.renderYawOffset) * 0.1f;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.field_70871_bB * 1.5);
            this.squidPitch += (-((float)MathHelper.func_181159_b(f, this.motionY)) * 180.0f / (float)Math.PI - this.squidPitch) * 0.1f;
        } else {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25f;
            if (!this.worldObj.isRemote) {
                this.motionX = 0.0;
                this.motionY -= 0.08;
                this.motionY *= (double)0.98f;
                this.motionZ = 0.0;
            }
            this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0f - this.squidPitch) * 0.02);
        }
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 19) {
            this.squidRotation = 0.0f;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(3 + n) + 1;
        int n3 = 0;
        while (n3 < n2) {
            this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0f);
            ++n3;
        }
    }

    @Override
    protected Item getDropItem() {
        return null;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    public void func_175568_b(float f, float f2, float f3) {
        this.randomMotionVecX = f;
        this.randomMotionVecY = f2;
        this.randomMotionVecZ = f3;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    public EntitySquid(World world) {
        super(world);
        this.setSize(0.95f, 0.95f);
        this.rand.setSeed(1 + this.getEntityId());
        this.rotationVelocity = 1.0f / (this.rand.nextFloat() + 1.0f) * 0.2f;
        this.tasks.addTask(0, new AIMoveRandom(this));
    }

    @Override
    protected String getLivingSound() {
        return null;
    }

    @Override
    public void moveEntityWithHeading(float f, float f2) {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
    public boolean getCanSpawnHere() {
        return this.posY > 45.0 && this.posY < (double)this.worldObj.func_181545_F() && super.getCanSpawnHere();
    }

    @Override
    public boolean isInWater() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0, -0.6f, 0.0), Material.water, this);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    static class AIMoveRandom
    extends EntityAIBase {
        private EntitySquid squid;

        public AIMoveRandom(EntitySquid entitySquid) {
            this.squid = entitySquid;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            int n = this.squid.getAge();
            if (n > 100) {
                this.squid.func_175568_b(0.0f, 0.0f, 0.0f);
            } else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.func_175567_n()) {
                float f = this.squid.getRNG().nextFloat() * (float)Math.PI * 2.0f;
                float f2 = MathHelper.cos(f) * 0.2f;
                float f3 = -0.1f + this.squid.getRNG().nextFloat() * 0.2f;
                float f4 = MathHelper.sin(f) * 0.2f;
                this.squid.func_175568_b(f2, f3, f4);
            }
        }
    }
}

