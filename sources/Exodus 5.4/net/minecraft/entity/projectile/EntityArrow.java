/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrow
extends Entity
implements IProjectile {
    private int knockbackStrength;
    private double damage = 2.0;
    private int zTile = -1;
    private boolean inGround;
    public int arrowShake;
    private int yTile = -1;
    public int canBePickedUp;
    private int xTile = -1;
    private int ticksInAir;
    public Entity shootingEntity;
    private int ticksInGround;
    private Block inTile;
    private int inData;

    public EntityArrow(World world, EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2, float f, float f2) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = entityLivingBase;
        if (entityLivingBase instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.posY = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - (double)0.1f;
        double d = entityLivingBase2.posX - entityLivingBase.posX;
        double d2 = entityLivingBase2.getEntityBoundingBox().minY + (double)(entityLivingBase2.height / 3.0f) - this.posY;
        double d3 = entityLivingBase2.posZ - entityLivingBase.posZ;
        double d4 = MathHelper.sqrt_double(d * d + d3 * d3);
        if (d4 >= 1.0E-7) {
            float f3 = (float)(MathHelper.func_181159_b(d3, d) * 180.0 / Math.PI) - 90.0f;
            float f4 = (float)(-(MathHelper.func_181159_b(d2, d4) * 180.0 / Math.PI));
            double d5 = d / d4;
            double d6 = d3 / d4;
            this.setLocationAndAngles(entityLivingBase.posX + d5, this.posY, entityLivingBase.posZ + d6, f3, f4);
            float f5 = (float)(d4 * (double)0.2f);
            this.setThrowableHeading(d, d2 + (double)f5, d3, f, f2);
        }
    }

    public EntityArrow(World world, double d, double d2, double d3) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
        this.setPosition(d, d2, d3);
    }

    public void setKnockbackStrength(int n) {
        this.knockbackStrength = n;
    }

    public void setIsCritical(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 1));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFE));
        }
    }

    public EntityArrow(World world, EntityLivingBase entityLivingBase, float f) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = entityLivingBase;
        if (entityLivingBase instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.setSize(0.5f, 0.5f);
        this.setLocationAndAngles(entityLivingBase.posX, entityLivingBase.posY + (double)entityLivingBase.getEyeHeight(), entityLivingBase.posZ, entityLivingBase.rotationYaw, entityLivingBase.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.posY -= (double)0.1f;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * (float)Math.PI);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, f * 1.5f, 1.0f);
    }

    @Override
    public boolean canAttackWithItem() {
        return false;
    }

    public double getDamage() {
        return this.damage;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("xTile", (short)this.xTile);
        nBTTagCompound.setShort("yTile", (short)this.yTile);
        nBTTagCompound.setShort("zTile", (short)this.zTile);
        nBTTagCompound.setShort("life", (short)this.ticksInGround);
        ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        nBTTagCompound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setByte("inData", (byte)this.inData);
        nBTTagCompound.setByte("shake", (byte)this.arrowShake);
        nBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        nBTTagCompound.setByte("pickup", (byte)this.canBePickedUp);
        nBTTagCompound.setDouble("damage", this.damage);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, (byte)0);
    }

    @Override
    public void onUpdate() {
        Object object;
        BlockPos blockPos;
        IBlockState iBlockState;
        Block block;
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0 / Math.PI);
        }
        if ((block = (iBlockState = this.worldObj.getBlockState(blockPos = new BlockPos(this.xTile, this.yTile, this.zTile))).getBlock()).getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, blockPos);
            object = block.getCollisionBoundingBox(this.worldObj, blockPos, iBlockState);
            if (object != null && ((AxisAlignedBB)object).isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            int n = block.getMetaFromState(iBlockState);
            if (block == this.inTile && n == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            Object object2;
            float f;
            Object object3;
            ++this.ticksInAir;
            object = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingObjectPosition = this.worldObj.rayTraceBlocks((Vec3)object, vec3, false, true, false);
            object = new Vec3(this.posX, this.posY, this.posZ);
            vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingObjectPosition != null) {
                vec3 = new Vec3(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
            }
            Object object4 = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d = 0.0;
            int n = 0;
            while (n < list.size()) {
                object3 = list.get(n);
                if (((Entity)object3).canBeCollidedWith() && (object3 != this.shootingEntity || this.ticksInAir >= 5)) {
                    double d2;
                    f = 0.3f;
                    object2 = ((Entity)object3).getEntityBoundingBox().expand(f, f, f);
                    MovingObjectPosition movingObjectPosition2 = ((AxisAlignedBB)object2).calculateIntercept((Vec3)object, vec3);
                    if (movingObjectPosition2 != null && ((d2 = ((Vec3)object).squareDistanceTo(movingObjectPosition2.hitVec)) < d || d == 0.0)) {
                        object4 = object3;
                        d = d2;
                    }
                }
                ++n;
            }
            if (object4 != null) {
                movingObjectPosition = new MovingObjectPosition((Entity)object4);
            }
            if (movingObjectPosition != null && movingObjectPosition.entityHit != null && movingObjectPosition.entityHit instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer)movingObjectPosition.entityHit;
                if (entityPlayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityPlayer)) {
                    movingObjectPosition = null;
                }
            }
            if (movingObjectPosition != null) {
                if (movingObjectPosition.entityHit != null) {
                    float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int n2 = MathHelper.ceiling_double_int((double)f2 * this.damage);
                    if (this.getIsCritical()) {
                        n2 += this.rand.nextInt(n2 / 2 + 2);
                    }
                    DamageSource damageSource = this.shootingEntity == null ? DamageSource.causeArrowDamage(this, this) : DamageSource.causeArrowDamage(this, this.shootingEntity);
                    if (this.isBurning() && !(movingObjectPosition.entityHit instanceof EntityEnderman)) {
                        movingObjectPosition.entityHit.setFire(5);
                    }
                    if (movingObjectPosition.entityHit.attackEntityFrom(damageSource, n2)) {
                        if (movingObjectPosition.entityHit instanceof EntityLivingBase) {
                            float f3;
                            object2 = (EntityLivingBase)movingObjectPosition.entityHit;
                            if (!this.worldObj.isRemote) {
                                ((EntityLivingBase)object2).setArrowCountInEntity(((EntityLivingBase)object2).getArrowCountInEntity() + 1);
                            }
                            if (this.knockbackStrength > 0 && (f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 0.0f) {
                                movingObjectPosition.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * (double)0.6f / (double)f3, 0.1, this.motionZ * (double)this.knockbackStrength * (double)0.6f / (double)f3);
                            }
                            if (this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.applyThornEnchantments((EntityLivingBase)object2, this.shootingEntity);
                                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, (Entity)object2);
                            }
                            if (this.shootingEntity != null && movingObjectPosition.entityHit != this.shootingEntity && movingObjectPosition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0f));
                            }
                        }
                        this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                        if (!(movingObjectPosition.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        this.motionX *= (double)-0.1f;
                        this.motionY *= (double)-0.1f;
                        this.motionZ *= (double)-0.1f;
                        this.rotationYaw += 180.0f;
                        this.prevRotationYaw += 180.0f;
                        this.ticksInAir = 0;
                    }
                } else {
                    BlockPos blockPos2 = movingObjectPosition.getBlockPos();
                    this.xTile = blockPos2.getX();
                    this.yTile = blockPos2.getY();
                    this.zTile = blockPos2.getZ();
                    object3 = this.worldObj.getBlockState(blockPos2);
                    this.inTile = object3.getBlock();
                    this.inData = this.inTile.getMetaFromState((IBlockState)object3);
                    this.motionX = (float)(movingObjectPosition.hitVec.xCoord - this.posX);
                    this.motionY = (float)(movingObjectPosition.hitVec.yCoord - this.posY);
                    this.motionZ = (float)(movingObjectPosition.hitVec.zCoord - this.posZ);
                    f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f * (double)0.05f;
                    this.posY -= this.motionY / (double)f * (double)0.05f;
                    this.posZ -= this.motionZ / (double)f * (double)0.05f;
                    this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                    if (this.inTile.getMaterial() != Material.air) {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, blockPos2, (IBlockState)object3, this);
                    }
                }
            }
            if (this.getIsCritical()) {
                int n3 = 0;
                while (n3 < 4) {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)n3 / 4.0, this.posY + this.motionY * (double)n3 / 4.0, this.posZ + this.motionZ * (double)n3 / 4.0, -this.motionX, -this.motionY + 0.2, -this.motionZ, new int[0]);
                    ++n3;
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / Math.PI);
            this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f4) * 180.0 / Math.PI);
            while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
                this.prevRotationPitch -= 360.0f;
            }
            while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
                this.prevRotationPitch += 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
                this.prevRotationYaw -= 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
                this.prevRotationYaw += 360.0f;
            }
            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
            float f5 = 0.99f;
            f = 0.05f;
            if (this.isInWater()) {
                int n4 = 0;
                while (n4 < 4) {
                    float f6 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)f6, this.posY - this.motionY * (double)f6, this.posZ - this.motionZ * (double)f6, this.motionX, this.motionY, this.motionZ, new int[0]);
                    ++n4;
                }
                f5 = 0.6f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= (double)f5;
            this.motionY *= (double)f5;
            this.motionZ *= (double)f5;
            this.motionY -= (double)f;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean bl;
            boolean bl2 = bl = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityPlayer.capabilities.isCreativeMode;
            if (this.canBePickedUp == 1 && !entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
                bl = false;
            }
            if (bl) {
                this.playSound("random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                entityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    @Override
    public void setThrowableHeading(double d, double d2, double d3, float f, float f2) {
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3);
        d /= (double)f3;
        d2 /= (double)f3;
        d3 /= (double)f3;
        d += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075f * (double)f2;
        d2 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075f * (double)f2;
        d3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * (double)0.0075f * (double)f2;
        this.motionX = d *= (double)f;
        this.motionY = d2 *= (double)f;
        this.motionZ = d3 *= (double)f;
        float f4 = MathHelper.sqrt_double(d * d + d3 * d3);
        this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(d, d3) * 180.0 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(d2, f4) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    public boolean getIsCritical() {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        return (by & 1) != 0;
    }

    @Override
    public void setVelocity(double d, double d2, double d3) {
        this.motionX = d;
        this.motionY = d2;
        this.motionZ = d3;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt_double(d * d + d3 * d3);
            this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(d, d3) * 180.0 / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(d2, f) * 180.0 / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public float getEyeHeight() {
        return 0.0f;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.xTile = nBTTagCompound.getShort("xTile");
        this.yTile = nBTTagCompound.getShort("yTile");
        this.zTile = nBTTagCompound.getShort("zTile");
        this.ticksInGround = nBTTagCompound.getShort("life");
        this.inTile = nBTTagCompound.hasKey("inTile", 8) ? Block.getBlockFromName(nBTTagCompound.getString("inTile")) : Block.getBlockById(nBTTagCompound.getByte("inTile") & 0xFF);
        this.inData = nBTTagCompound.getByte("inData") & 0xFF;
        this.arrowShake = nBTTagCompound.getByte("shake") & 0xFF;
        boolean bl = this.inGround = nBTTagCompound.getByte("inGround") == 1;
        if (nBTTagCompound.hasKey("damage", 99)) {
            this.damage = nBTTagCompound.getDouble("damage");
        }
        if (nBTTagCompound.hasKey("pickup", 99)) {
            this.canBePickedUp = nBTTagCompound.getByte("pickup");
        } else if (nBTTagCompound.hasKey("player", 99)) {
            this.canBePickedUp = nBTTagCompound.getBoolean("player") ? 1 : 0;
        }
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.setPosition(d, d2, d3);
        this.setRotation(f, f2);
    }

    public EntityArrow(World world) {
        super(world);
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
    }

    public void setDamage(double d) {
        this.damage = d;
    }
}

