/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityXPOrb
extends Entity {
    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth = 5;
    private int xpValue;
    private EntityPlayer closestPlayer;
    private int xpTargetColor;

    public EntityXPOrb(World worldIn, double x, double y, double z, int expValue) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * (double)0.2f - (double)0.1f) * 2.0f;
        this.motionY = (float)(Math.random() * 0.2) * 2.0f;
        this.motionZ = (float)(Math.random() * (double)0.2f - (double)0.1f) * 2.0f;
        this.xpValue = expValue;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityXPOrb(World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public int getBrightnessForRender(float partialTicks) {
        float f = 0.5f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        int i = super.getBrightnessForRender(partialTicks);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(f * 15.0f * 16.0f)) <= 240) return j | k << 16;
        j = 240;
        return j | k << 16;
    }

    @Override
    public void onUpdate() {
        double d3;
        double d2;
        double d1;
        double d4;
        double d5;
        super.onUpdate();
        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= (double)0.03f;
        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
            this.motionY = 0.2f;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.playSound("random.fizz", 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
        }
        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        double d0 = 8.0;
        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0) {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
            }
            this.xpTargetColor = this.xpColor;
        }
        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
        if (this.closestPlayer != null && (d5 = 1.0 - (d4 = Math.sqrt((d1 = (this.closestPlayer.posX - this.posX) / d0) * d1 + (d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d0) * d2 + (d3 = (this.closestPlayer.posZ - this.posZ) / d0) * d3))) > 0.0) {
            d5 *= d5;
            this.motionX += d1 / d4 * d5 * 0.1;
            this.motionY += d2 / d4 * d5 * 0.1;
            this.motionZ += d3 / d4 * d5 * 0.1;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float f = 0.98f;
        if (this.onGround) {
            f = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.98f;
        }
        this.motionX *= (double)f;
        this.motionY *= (double)0.98f;
        this.motionZ *= (double)f;
        if (this.onGround) {
            this.motionY *= (double)-0.9f;
        }
        ++this.xpColor;
        ++this.xpOrbAge;
        if (this.xpOrbAge < 6000) return;
        this.setDead();
    }

    @Override
    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
    }

    @Override
    protected void dealFireDamage(int amount) {
        this.attackEntityFrom(DamageSource.inFire, amount);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setBeenAttacked();
        this.xpOrbHealth = (int)((float)this.xpOrbHealth - amount);
        if (this.xpOrbHealth > 0) return false;
        this.setDead();
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("Health", (byte)this.xpOrbHealth);
        tagCompound.setShort("Age", (short)this.xpOrbAge);
        tagCompound.setShort("Value", (short)this.xpValue);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.xpOrbHealth = tagCompund.getShort("Health") & 0xFF;
        this.xpOrbAge = tagCompund.getShort("Age");
        this.xpValue = tagCompund.getShort("Value");
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (this.worldObj.isRemote) return;
        if (this.delayBeforeCanPickup != 0) return;
        if (entityIn.xpCooldown != 0) return;
        entityIn.xpCooldown = 2;
        this.worldObj.playSoundAtEntity(entityIn, "random.orb", 0.1f, 0.5f * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.8f));
        entityIn.onItemPickup(this, 1);
        entityIn.addExperience(this.xpValue);
        this.setDead();
    }

    public int getXpValue() {
        return this.xpValue;
    }

    public int getTextureByXP() {
        if (this.xpValue >= 2477) {
            return 10;
        }
        if (this.xpValue >= 1237) {
            return 9;
        }
        if (this.xpValue >= 617) {
            return 8;
        }
        if (this.xpValue >= 307) {
            return 7;
        }
        if (this.xpValue >= 149) {
            return 6;
        }
        if (this.xpValue >= 73) {
            return 5;
        }
        if (this.xpValue >= 37) {
            return 4;
        }
        if (this.xpValue >= 17) {
            return 3;
        }
        if (this.xpValue >= 7) {
            return 2;
        }
        if (this.xpValue < 3) return 0;
        return 1;
    }

    public static int getXPSplit(int expValue) {
        if (expValue >= 2477) {
            return 2477;
        }
        if (expValue >= 1237) {
            return 1237;
        }
        if (expValue >= 617) {
            return 617;
        }
        if (expValue >= 307) {
            return 307;
        }
        if (expValue >= 149) {
            return 149;
        }
        if (expValue >= 73) {
            return 73;
        }
        if (expValue >= 37) {
            return 37;
        }
        if (expValue >= 17) {
            return 17;
        }
        if (expValue >= 7) {
            return 7;
        }
        if (expValue < 3) return 1;
        return 3;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}

