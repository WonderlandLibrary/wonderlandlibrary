// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

public class EntityXPOrb extends Entity
{
    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth;
    private int xpValue;
    private EntityPlayer closestPlayer;
    private int xpTargetColor;
    
    public EntityXPOrb(final World worldIn, final double x, final double y, final double z, final int expValue) {
        super(worldIn);
        this.xpOrbHealth = 5;
        this.setSize(0.5f, 0.5f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612) * 2.0f;
        this.motionY = (float)(Math.random() * 0.2) * 2.0f;
        this.motionZ = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612) * 2.0f;
        this.xpValue = expValue;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public EntityXPOrb(final World worldIn) {
        super(worldIn);
        this.xpOrbHealth = 5;
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public int getBrightnessForRender() {
        float f = 0.5f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        final int i = super.getBrightnessForRender();
        int j = i & 0xFF;
        final int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0f * 16.0f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= 0.029999999329447746;
        }
        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
            this.motionY = 0.20000000298023224;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
        }
        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        final double d0 = 8.0;
        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > 64.0) {
                this.closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0);
            }
            this.xpTargetColor = this.xpColor;
        }
        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
        if (this.closestPlayer != null) {
            final double d2 = (this.closestPlayer.posX - this.posX) / 8.0;
            final double d3 = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() / 2.0 - this.posY) / 8.0;
            final double d4 = (this.closestPlayer.posZ - this.posZ) / 8.0;
            final double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            double d6 = 1.0 - d5;
            if (d6 > 0.0) {
                d6 *= d6;
                this.motionX += d2 / d5 * d6 * 0.1;
                this.motionY += d3 / d5 * d6 * 0.1;
                this.motionZ += d4 / d5 * d6 * 0.1;
            }
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = 0.98f;
        if (this.onGround) {
            f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98f;
        }
        this.motionX *= f;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= f;
        if (this.onGround) {
            this.motionY *= -0.8999999761581421;
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
    protected void dealFireDamage(final int amount) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float)amount);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.markVelocityChanged();
        this.xpOrbHealth -= (int)amount;
        if (this.xpOrbHealth <= 0) {
            this.setDead();
        }
        return false;
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setShort("Health", (short)this.xpOrbHealth);
        compound.setShort("Age", (short)this.xpOrbAge);
        compound.setShort("Value", (short)this.xpValue);
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.xpOrbHealth = compound.getShort("Health");
        this.xpOrbAge = compound.getShort("Age");
        this.xpValue = compound.getShort("Value");
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.world.isRemote && this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
            entityIn.xpCooldown = 2;
            entityIn.onItemPickup(this, 1);
            final ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, entityIn);
            if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                final int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getItemDamage());
                this.xpValue -= this.durabilityToXp(i);
                itemstack.setItemDamage(itemstack.getItemDamage() - i);
            }
            if (this.xpValue > 0) {
                entityIn.addExperience(this.xpValue);
            }
            this.setDead();
        }
    }
    
    private int durabilityToXp(final int durability) {
        return durability / 2;
    }
    
    private int xpToDurability(final int xp) {
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
        return (this.xpValue >= 3) ? 1 : 0;
    }
    
    public static int getXPSplit(final int expValue) {
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
        return (expValue >= 3) ? 3 : 1;
    }
    
    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }
}
