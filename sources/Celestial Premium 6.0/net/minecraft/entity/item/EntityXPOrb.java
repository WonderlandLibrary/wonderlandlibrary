/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
    public int getBrightnessForRender() {
        float f = 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        int i = super.getBrightnessForRender();
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        if ((j += (int)(f * 15.0f * 16.0f)) > 240) {
            j = 240;
        }
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
        if (!this.hasNoGravity()) {
            this.motionY -= (double)0.03f;
        }
        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
            this.motionY = 0.2f;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
        }
        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        double d0 = 8.0;
        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > 64.0) {
                this.closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0);
            }
            this.xpTargetColor = this.xpColor;
        }
        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
        if (this.closestPlayer != null && (d5 = 1.0 - (d4 = Math.sqrt((d1 = (this.closestPlayer.posX - this.posX) / 8.0) * d1 + (d2 = (this.closestPlayer.posY + (double)this.closestPlayer.getEyeHeight() / 2.0 - this.posY) / 8.0) * d2 + (d3 = (this.closestPlayer.posZ - this.posZ) / 8.0) * d3))) > 0.0) {
            d5 *= d5;
            this.motionX += d1 / d4 * d5 * 0.1;
            this.motionY += d2 / d4 * d5 * 0.1;
            this.motionZ += d3 / d4 * d5 * 0.1;
        }
        this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = 0.98f;
        if (this.onGround) {
            f = this.world.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor((double)this.posX), (int)(MathHelper.floor((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor((double)this.posZ))).getBlock().slipperiness * 0.98f;
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
    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
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
        if (this.xpOrbHealth <= 0) {
            this.setDead();
        }
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setShort("Health", (short)this.xpOrbHealth);
        compound.setShort("Age", (short)this.xpOrbAge);
        compound.setShort("Value", (short)this.xpValue);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.xpOrbHealth = compound.getShort("Health");
        this.xpOrbAge = compound.getShort("Age");
        this.xpValue = compound.getShort("Value");
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote && this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
            entityIn.xpCooldown = 2;
            entityIn.onItemPickup(this, 1);
            ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, entityIn);
            if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getItemDamage());
                this.xpValue -= this.durabilityToXp(i);
                itemstack.setItemDamage(itemstack.getItemDamage() - i);
            }
            if (this.xpValue > 0) {
                entityIn.addExperience(this.xpValue);
            }
            this.setDead();
        }
    }

    private int durabilityToXp(int durability) {
        return durability / 2;
    }

    private int xpToDurability(int xp) {
        return xp * 2;
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
        return this.xpValue >= 3 ? 1 : 0;
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
        return expValue >= 3 ? 3 : 1;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }
}

