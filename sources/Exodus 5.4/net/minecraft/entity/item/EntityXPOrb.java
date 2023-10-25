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
    private EntityPlayer closestPlayer;
    public int delayBeforeCanPickup;
    private int xpValue;
    private int xpTargetColor;
    private int xpOrbHealth = 5;
    public int xpColor;
    public int xpOrbAge;

    public int getXpValue() {
        return this.xpValue;
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    public EntityXPOrb(World world) {
        super(world);
        this.setSize(0.25f, 0.25f);
    }

    public int getTextureByXP() {
        return this.xpValue >= 2477 ? 10 : (this.xpValue >= 1237 ? 9 : (this.xpValue >= 617 ? 8 : (this.xpValue >= 307 ? 7 : (this.xpValue >= 149 ? 6 : (this.xpValue >= 73 ? 5 : (this.xpValue >= 37 ? 4 : (this.xpValue >= 17 ? 3 : (this.xpValue >= 7 ? 2 : (this.xpValue >= 3 ? 1 : 0)))))))));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.xpOrbHealth = nBTTagCompound.getShort("Health") & 0xFF;
        this.xpOrbAge = nBTTagCompound.getShort("Age");
        this.xpValue = nBTTagCompound.getShort("Value");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("Health", (byte)this.xpOrbHealth);
        nBTTagCompound.setShort("Age", (short)this.xpOrbAge);
        nBTTagCompound.setShort("Value", (short)this.xpValue);
    }

    @Override
    public void onUpdate() {
        double d;
        double d2;
        double d3;
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
        double d6 = 8.0;
        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d6 * d6) {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d6);
            }
            this.xpTargetColor = this.xpColor;
        }
        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
        if (this.closestPlayer != null && (d5 = 1.0 - (d4 = Math.sqrt((d3 = (this.closestPlayer.posX - this.posX) / d6) * d3 + (d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() - this.posY) / d6) * d2 + (d = (this.closestPlayer.posZ - this.posZ) / d6) * d))) > 0.0) {
            d5 *= d5;
            this.motionX += d3 / d4 * d5 * 0.1;
            this.motionY += d2 / d4 * d5 * 0.1;
            this.motionZ += d / d4 * d5 * 0.1;
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
        if (this.xpOrbAge >= 6000) {
            this.setDead();
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        this.setBeenAttacked();
        this.xpOrbHealth = (int)((float)this.xpOrbHealth - f);
        if (this.xpOrbHealth <= 0) {
            this.setDead();
        }
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
    }

    public EntityXPOrb(World world, double d, double d2, double d3, int n) {
        super(world);
        this.setSize(0.5f, 0.5f);
        this.setPosition(d, d2, d3);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * (double)0.2f - (double)0.1f) * 2.0f;
        this.motionY = (float)(Math.random() * 0.2) * 2.0f;
        this.motionZ = (float)(Math.random() * (double)0.2f - (double)0.1f) * 2.0f;
        this.xpValue = n;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public int getBrightnessForRender(float f) {
        float f2 = 0.5f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        int n = super.getBrightnessForRender(f);
        int n2 = n & 0xFF;
        int n3 = n >> 16 & 0xFF;
        if ((n2 += (int)(f2 * 15.0f * 16.0f)) > 240) {
            n2 = 240;
        }
        return n2 | n3 << 16;
    }

    public static int getXPSplit(int n) {
        return n >= 2477 ? 2477 : (n >= 1237 ? 1237 : (n >= 617 ? 617 : (n >= 307 ? 307 : (n >= 149 ? 149 : (n >= 73 ? 73 : (n >= 37 ? 37 : (n >= 17 ? 17 : (n >= 7 ? 7 : (n >= 3 ? 3 : 1)))))))));
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        if (!this.worldObj.isRemote && this.delayBeforeCanPickup == 0 && entityPlayer.xpCooldown == 0) {
            entityPlayer.xpCooldown = 2;
            this.worldObj.playSoundAtEntity(entityPlayer, "random.orb", 0.1f, 0.5f * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.8f));
            entityPlayer.onItemPickup(this, 1);
            entityPlayer.addExperience(this.xpValue);
            this.setDead();
        }
    }

    @Override
    protected void dealFireDamage(int n) {
        this.attackEntityFrom(DamageSource.inFire, n);
    }
}

