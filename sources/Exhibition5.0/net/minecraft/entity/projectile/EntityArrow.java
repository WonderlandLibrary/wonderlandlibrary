// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public class EntityArrow extends Entity implements IProjectile
{
    private int field_145791_d;
    private int field_145792_e;
    private int field_145789_f;
    private Block field_145790_g;
    private int inData;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;
    private static final String __OBFID = "CL_00001715";
    
    public EntityArrow(final World worldIn) {
        super(worldIn);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityArrow(final World worldIn, final double p_i1754_2_, final double p_i1754_4_, final double p_i1754_6_) {
        super(worldIn);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
        this.setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
    }
    
    public EntityArrow(final World worldIn, final EntityLivingBase p_i1755_2_, final EntityLivingBase p_i1755_3_, final float p_i1755_4_, final float p_i1755_5_) {
        super(worldIn);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = p_i1755_2_;
        if (p_i1755_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.posY = p_i1755_2_.posY + p_i1755_2_.getEyeHeight() - 0.10000000149011612;
        final double var6 = p_i1755_3_.posX - p_i1755_2_.posX;
        final double var7 = p_i1755_3_.getEntityBoundingBox().minY + p_i1755_3_.height / 3.0f - this.posY;
        final double var8 = p_i1755_3_.posZ - p_i1755_2_.posZ;
        final double var9 = MathHelper.sqrt_double(var6 * var6 + var8 * var8);
        if (var9 >= 1.0E-7) {
            final float var10 = (float)(Math.atan2(var8, var6) * 180.0 / 3.141592653589793) - 90.0f;
            final float var11 = (float)(-(Math.atan2(var7, var9) * 180.0 / 3.141592653589793));
            final double var12 = var6 / var9;
            final double var13 = var8 / var9;
            this.setLocationAndAngles(p_i1755_2_.posX + var12, this.posY, p_i1755_2_.posZ + var13, var10, var11);
            final float var14 = (float)(var9 * 0.20000000298023224);
            this.setThrowableHeading(var6, var7 + var14, var8, p_i1755_4_, p_i1755_5_);
        }
    }
    
    public EntityArrow(final World worldIn, final EntityLivingBase p_i1756_2_, final float p_i1756_3_) {
        super(worldIn);
        this.field_145791_d = -1;
        this.field_145792_e = -1;
        this.field_145789_f = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = p_i1756_2_;
        if (p_i1756_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.setSize(0.5f, 0.5f);
        this.setLocationAndAngles(p_i1756_2_.posX, p_i1756_2_.posY + p_i1756_2_.getEyeHeight(), p_i1756_2_.posZ, p_i1756_2_.rotationYaw, p_i1756_2_.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5f, 1.0f);
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, (byte)0);
    }
    
    @Override
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, final float p_70186_7_, final float p_70186_8_) {
        final float var9 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= var9;
        p_70186_3_ /= var9;
        p_70186_5_ /= var9;
        p_70186_1_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * p_70186_8_;
        p_70186_1_ *= p_70186_7_;
        p_70186_3_ *= p_70186_7_;
        p_70186_5_ *= p_70186_7_;
        this.motionX = p_70186_1_;
        this.motionY = p_70186_3_;
        this.motionZ = p_70186_5_;
        final float var10 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        final float n = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(Math.atan2(p_70186_3_, var10) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRotation(p_180426_7_, p_180426_8_);
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float var7 = MathHelper.sqrt_double(x * x + z * z);
            final float n = (float)(Math.atan2(x, z) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(Math.atan2(y, var7) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final float n = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(Math.atan2(this.motionY, var1) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
        }
        final BlockPos var2 = new BlockPos(this.field_145791_d, this.field_145792_e, this.field_145789_f);
        IBlockState var3 = this.worldObj.getBlockState(var2);
        final Block var4 = var3.getBlock();
        if (var4.getMaterial() != Material.air) {
            var4.setBlockBoundsBasedOnState(this.worldObj, var2);
            final AxisAlignedBB var5 = var4.getCollisionBoundingBox(this.worldObj, var2, var3);
            if (var5 != null && var5.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            final int var6 = var4.getMetaFromState(var3);
            if (var4 == this.field_145790_g && var6 == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            }
            else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else {
            ++this.ticksInAir;
            Vec3 var7 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 var8 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var9 = this.worldObj.rayTraceBlocks(var7, var8, false, true, false);
            var7 = new Vec3(this.posX, this.posY, this.posZ);
            var8 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (var9 != null) {
                var8 = new Vec3(var9.hitVec.xCoord, var9.hitVec.yCoord, var9.hitVec.zCoord);
            }
            Entity var10 = null;
            final List var11 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var12 = 0.0;
            for (int var13 = 0; var13 < var11.size(); ++var13) {
                final Entity var14 = var11.get(var13);
                if (var14.canBeCollidedWith() && (var14 != this.shootingEntity || this.ticksInAir >= 5)) {
                    final float var15 = 0.3f;
                    final AxisAlignedBB var16 = var14.getEntityBoundingBox().expand(var15, var15, var15);
                    final MovingObjectPosition var17 = var16.calculateIntercept(var7, var8);
                    if (var17 != null) {
                        final double var18 = var7.distanceTo(var17.hitVec);
                        if (var18 < var12 || var12 == 0.0) {
                            var10 = var14;
                            var12 = var18;
                        }
                    }
                }
            }
            if (var10 != null) {
                var9 = new MovingObjectPosition(var10);
            }
            if (var9 != null && var9.entityHit != null && var9.entityHit instanceof EntityPlayer) {
                final EntityPlayer var19 = (EntityPlayer)var9.entityHit;
                if (var19.capabilities.disableDamage || (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(var19))) {
                    var9 = null;
                }
            }
            if (var9 != null) {
                if (var9.entityHit != null) {
                    final float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int var21 = MathHelper.ceiling_double_int(var20 * this.damage);
                    if (this.getIsCritical()) {
                        var21 += this.rand.nextInt(var21 / 2 + 2);
                    }
                    DamageSource var22;
                    if (this.shootingEntity == null) {
                        var22 = DamageSource.causeArrowDamage(this, this);
                    }
                    else {
                        var22 = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }
                    if (this.isBurning() && !(var9.entityHit instanceof EntityEnderman)) {
                        var9.entityHit.setFire(5);
                    }
                    if (var9.entityHit.attackEntityFrom(var22, var21)) {
                        if (var9.entityHit instanceof EntityLivingBase) {
                            final EntityLivingBase var23 = (EntityLivingBase)var9.entityHit;
                            if (!this.worldObj.isRemote) {
                                var23.setArrowCountInEntity(var23.getArrowCountInEntity() + 1);
                            }
                            if (this.knockbackStrength > 0) {
                                final float var24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                                if (var24 > 0.0f) {
                                    var9.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579 / var24, 0.1, this.motionZ * this.knockbackStrength * 0.6000000238418579 / var24);
                                }
                            }
                            if (this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(var23, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase)this.shootingEntity, var23);
                            }
                            if (this.shootingEntity != null && var9.entityHit != this.shootingEntity && var9.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0f));
                            }
                        }
                        this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                        if (!(var9.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    }
                    else {
                        this.motionX *= -0.10000000149011612;
                        this.motionY *= -0.10000000149011612;
                        this.motionZ *= -0.10000000149011612;
                        this.rotationYaw += 180.0f;
                        this.prevRotationYaw += 180.0f;
                        this.ticksInAir = 0;
                    }
                }
                else {
                    final BlockPos var25 = var9.getBlockPos();
                    this.field_145791_d = var25.getX();
                    this.field_145792_e = var25.getY();
                    this.field_145789_f = var25.getZ();
                    var3 = this.worldObj.getBlockState(var25);
                    this.field_145790_g = var3.getBlock();
                    this.inData = this.field_145790_g.getMetaFromState(var3);
                    this.motionX = (float)(var9.hitVec.xCoord - this.posX);
                    this.motionY = (float)(var9.hitVec.yCoord - this.posY);
                    this.motionZ = (float)(var9.hitVec.zCoord - this.posZ);
                    final float var26 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / var26 * 0.05000000074505806;
                    this.posY -= this.motionY / var26 * 0.05000000074505806;
                    this.posZ -= this.motionZ / var26 * 0.05000000074505806;
                    this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                    if (this.field_145790_g.getMaterial() != Material.air) {
                        this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, var25, var3, this);
                    }
                }
            }
            if (this.getIsCritical()) {
                for (int var13 = 0; var13 < 4; ++var13) {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * var13 / 4.0, this.posY + this.motionY * var13 / 4.0, this.posZ + this.motionZ * var13 / 4.0, -this.motionX, -this.motionY + 0.2, -this.motionZ, new int[0]);
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationPitch = (float)(Math.atan2(this.motionY, var20) * 180.0 / 3.141592653589793);
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
            float var26 = 0.99f;
            final float var15 = 0.05f;
            if (this.isInWater()) {
                for (int var27 = 0; var27 < 4; ++var27) {
                    final float var24 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * var24, this.posY - this.motionY * var24, this.posZ - this.motionZ * var24, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                var26 = 0.6f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= var26;
            this.motionY *= var26;
            this.motionZ *= var26;
            this.motionY -= var15;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.field_145791_d);
        tagCompound.setShort("yTile", (short)this.field_145792_e);
        tagCompound.setShort("zTile", (short)this.field_145789_f);
        tagCompound.setShort("life", (short)this.ticksInGround);
        final ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.field_145790_g);
        tagCompound.setString("inTile", (var2 == null) ? "" : var2.toString());
        tagCompound.setByte("inData", (byte)this.inData);
        tagCompound.setByte("shake", (byte)this.arrowShake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte)this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.field_145791_d = tagCompund.getShort("xTile");
        this.field_145792_e = tagCompund.getShort("yTile");
        this.field_145789_f = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");
        if (tagCompund.hasKey("inTile", 8)) {
            this.field_145790_g = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.field_145790_g = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.inData = (tagCompund.getByte("inData") & 0xFF);
        this.arrowShake = (tagCompund.getByte("shake") & 0xFF);
        this.inGround = (tagCompund.getByte("inGround") == 1);
        if (tagCompund.hasKey("damage", 99)) {
            this.damage = tagCompund.getDouble("damage");
        }
        if (tagCompund.hasKey("pickup", 99)) {
            this.canBePickedUp = tagCompund.getByte("pickup");
        }
        else if (tagCompund.hasKey("player", 99)) {
            this.canBePickedUp = (tagCompund.getBoolean("player") ? 1 : 0);
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean var2 = this.canBePickedUp == 1 || (this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode);
            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
                var2 = false;
            }
            if (var2) {
                this.playSound("random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public void setDamage(final double p_70239_1_) {
        this.damage = p_70239_1_;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public void setKnockbackStrength(final int p_70240_1_) {
        this.knockbackStrength = p_70240_1_;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    public void setIsCritical(final boolean p_70243_1_) {
        final byte var2 = this.dataWatcher.getWatchableObjectByte(16);
        if (p_70243_1_) {
            this.dataWatcher.updateObject(16, (byte)(var2 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(var2 & 0xFFFFFFFE));
        }
    }
    
    public boolean getIsCritical() {
        final byte var1 = this.dataWatcher.getWatchableObjectByte(16);
        return (var1 & 0x1) != 0x0;
    }
}
