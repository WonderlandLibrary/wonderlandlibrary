// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.projectile;

import net.minecraft.item.ItemFishFood;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.entity.Entity;

public class EntityFishHook extends Entity
{
    private static final List JUNK;
    private static final List VALUABLES;
    private static final List FISH;
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int shake;
    public EntityPlayer angler;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private int fishPosRotationIncrements;
    private double fishX;
    private double fishY;
    private double fishZ;
    private double fishYaw;
    private double fishPitch;
    private double clientMotionX;
    private double clientMotionY;
    private double clientMotionZ;
    private static final String __OBFID = "CL_00001663";
    
    public static List func_174855_j() {
        return EntityFishHook.FISH;
    }
    
    public EntityFishHook(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
    }
    
    public EntityFishHook(final World worldIn, final double p_i1765_2_, final double p_i1765_4_, final double p_i1765_6_, final EntityPlayer p_i1765_8_) {
        this(worldIn);
        this.setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
        this.ignoreFrustumCheck = true;
        this.angler = p_i1765_8_;
        p_i1765_8_.fishEntity = this;
    }
    
    public EntityFishHook(final World worldIn, final EntityPlayer p_i1766_2_) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.ignoreFrustumCheck = true;
        this.angler = p_i1766_2_;
        (this.angler.fishEntity = this).setSize(0.25f, 0.25f);
        this.setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + p_i1766_2_.getEyeHeight(), p_i1766_2_.posZ, p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        final float var3 = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var3;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f) * var3;
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        var3 *= 64.0;
        return distance < var3 * var3;
    }
    
    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, final float p_146035_7_, final float p_146035_8_) {
        final float var9 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= var9;
        p_146035_3_ /= var9;
        p_146035_5_ /= var9;
        p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937 * p_146035_8_;
        p_146035_1_ *= p_146035_7_;
        p_146035_3_ *= p_146035_7_;
        p_146035_5_ *= p_146035_7_;
        this.motionX = p_146035_1_;
        this.motionY = p_146035_3_;
        this.motionZ = p_146035_5_;
        final float var10 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        final float n = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(Math.atan2(p_146035_3_, var10) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        this.fishX = p_180426_1_;
        this.fishY = p_180426_3_;
        this.fishZ = p_180426_5_;
        this.fishYaw = p_180426_7_;
        this.fishPitch = p_180426_8_;
        this.fishPosRotationIncrements = p_180426_9_;
        this.motionX = this.clientMotionX;
        this.motionY = this.clientMotionY;
        this.motionZ = this.clientMotionZ;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.clientMotionX = x;
        this.motionY = y;
        this.clientMotionY = y;
        this.motionZ = z;
        this.clientMotionZ = z;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fishPosRotationIncrements > 0) {
            final double var28 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
            final double var29 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
            final double var30 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
            final double var31 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
            this.rotationYaw += (float)(var31 / this.fishPosRotationIncrements);
            this.rotationPitch += (float)((this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(var28, var29, var30);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else {
            if (!this.worldObj.isRemote) {
                final ItemStack var32 = this.angler.getCurrentEquippedItem();
                if (this.angler.isDead || !this.angler.isEntityAlive() || var32 == null || var32.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0) {
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }
                if (this.caughtEntity != null) {
                    if (!this.caughtEntity.isDead) {
                        this.posX = this.caughtEntity.posX;
                        final double var33 = this.caughtEntity.height;
                        this.posY = this.caughtEntity.getEntityBoundingBox().minY + var33 * 0.8;
                        this.posZ = this.caughtEntity.posZ;
                        return;
                    }
                    this.caughtEntity = null;
                }
            }
            if (this.shake > 0) {
                --this.shake;
            }
            if (this.inGround) {
                if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
                    ++this.ticksInGround;
                    if (this.ticksInGround == 1200) {
                        this.setDead();
                    }
                    return;
                }
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else {
                ++this.ticksInAir;
            }
            Vec3 var34 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 var35 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var36 = this.worldObj.rayTraceBlocks(var34, var35);
            var34 = new Vec3(this.posX, this.posY, this.posZ);
            var35 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (var36 != null) {
                var35 = new Vec3(var36.hitVec.xCoord, var36.hitVec.yCoord, var36.hitVec.zCoord);
            }
            Entity var37 = null;
            final List var38 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var39 = 0.0;
            for (int var40 = 0; var40 < var38.size(); ++var40) {
                final Entity var41 = var38.get(var40);
                if (var41.canBeCollidedWith() && (var41 != this.angler || this.ticksInAir >= 5)) {
                    final float var42 = 0.3f;
                    final AxisAlignedBB var43 = var41.getEntityBoundingBox().expand(var42, var42, var42);
                    final MovingObjectPosition var44 = var43.calculateIntercept(var34, var35);
                    if (var44 != null) {
                        final double var45 = var34.distanceTo(var44.hitVec);
                        if (var45 < var39 || var39 == 0.0) {
                            var37 = var41;
                            var39 = var45;
                        }
                    }
                }
            }
            if (var37 != null) {
                var36 = new MovingObjectPosition(var37);
            }
            if (var36 != null) {
                if (var36.entityHit != null) {
                    if (var36.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0f)) {
                        this.caughtEntity = var36.entityHit;
                    }
                }
                else {
                    this.inGround = true;
                }
            }
            if (!this.inGround) {
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                final float var46 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
                this.rotationPitch = (float)(Math.atan2(this.motionY, var46) * 180.0 / 3.141592653589793);
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
                float var47 = 0.92f;
                if (this.onGround || this.isCollidedHorizontally) {
                    var47 = 0.5f;
                }
                final byte var48 = 5;
                double var49 = 0.0;
                for (int var50 = 0; var50 < var48; ++var50) {
                    final AxisAlignedBB var51 = this.getEntityBoundingBox();
                    final double var52 = var51.maxY - var51.minY;
                    final double var53 = var51.minY + var52 * var50 / var48;
                    final double var54 = var51.minY + var52 * (var50 + 1) / var48;
                    final AxisAlignedBB var55 = new AxisAlignedBB(var51.minX, var53, var51.minZ, var51.maxX, var54, var51.maxZ);
                    if (this.worldObj.isAABBInMaterial(var55, Material.water)) {
                        var49 += 1.0 / var48;
                    }
                }
                if (!this.worldObj.isRemote && var49 > 0.0) {
                    final WorldServer var56 = (WorldServer)this.worldObj;
                    int var57 = 1;
                    final BlockPos var58 = new BlockPos(this).offsetUp();
                    if (this.rand.nextFloat() < 0.25f && this.worldObj.func_175727_C(var58)) {
                        var57 = 2;
                    }
                    if (this.rand.nextFloat() < 0.5f && !this.worldObj.isAgainstSky(var58)) {
                        --var57;
                    }
                    if (this.ticksCatchable > 0) {
                        --this.ticksCatchable;
                        if (this.ticksCatchable <= 0) {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    }
                    else if (this.ticksCatchableDelay > 0) {
                        this.ticksCatchableDelay -= var57;
                        if (this.ticksCatchableDelay <= 0) {
                            this.motionY -= 0.20000000298023224;
                            this.playSound("random.splash", 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                            final float var59 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
                            var56.func_175739_a(EnumParticleTypes.WATER_BUBBLE, this.posX, var59 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.20000000298023224, new int[0]);
                            var56.func_175739_a(EnumParticleTypes.WATER_WAKE, this.posX, var59 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.20000000298023224, new int[0]);
                            this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                        }
                        else {
                            this.fishApproachAngle += (float)(this.rand.nextGaussian() * 4.0);
                            final float var59 = this.fishApproachAngle * 0.017453292f;
                            final float var60 = MathHelper.sin(var59);
                            final float var61 = MathHelper.cos(var59);
                            final double var54 = this.posX + var60 * this.ticksCatchableDelay * 0.1f;
                            final double var62 = MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            final double var63 = this.posZ + var61 * this.ticksCatchableDelay * 0.1f;
                            if (this.rand.nextFloat() < 0.15f) {
                                var56.func_175739_a(EnumParticleTypes.WATER_BUBBLE, var54, var62 - 0.10000000149011612, var63, 1, var60, 0.1, var61, 0.0, new int[0]);
                            }
                            final float var64 = var60 * 0.04f;
                            final float var65 = var61 * 0.04f;
                            var56.func_175739_a(EnumParticleTypes.WATER_WAKE, var54, var62, var63, 0, var65, 0.01, -var64, 1.0, new int[0]);
                            var56.func_175739_a(EnumParticleTypes.WATER_WAKE, var54, var62, var63, 0, -var65, 0.01, var64, 1.0, new int[0]);
                        }
                    }
                    else if (this.ticksCaughtDelay > 0) {
                        this.ticksCaughtDelay -= var57;
                        float var59 = 0.15f;
                        if (this.ticksCaughtDelay < 20) {
                            var59 += (float)((20 - this.ticksCaughtDelay) * 0.05);
                        }
                        else if (this.ticksCaughtDelay < 40) {
                            var59 += (float)((40 - this.ticksCaughtDelay) * 0.02);
                        }
                        else if (this.ticksCaughtDelay < 60) {
                            var59 += (float)((60 - this.ticksCaughtDelay) * 0.01);
                        }
                        if (this.rand.nextFloat() < var59) {
                            final float var60 = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f) * 0.017453292f;
                            final float var61 = MathHelper.randomFloatClamp(this.rand, 25.0f, 60.0f);
                            final double var54 = this.posX + MathHelper.sin(var60) * var61 * 0.1f;
                            final double var62 = MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            final double var63 = this.posZ + MathHelper.cos(var60) * var61 * 0.1f;
                            var56.func_175739_a(EnumParticleTypes.WATER_SPLASH, var54, var62, var63, 2 + this.rand.nextInt(2), 0.10000000149011612, 0.0, 0.10000000149011612, 0.0, new int[0]);
                        }
                        if (this.ticksCaughtDelay <= 0) {
                            this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f);
                            this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                        }
                    }
                    else {
                        this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                        this.ticksCaughtDelay -= EnchantmentHelper.func_151387_h(this.angler) * 20 * 5;
                    }
                    if (this.ticksCatchable > 0) {
                        this.motionY -= this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat() * 0.2;
                    }
                }
                final double var45 = var49 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * var45;
                if (var49 > 0.0) {
                    var47 *= 0.9;
                    this.motionY *= 0.8;
                }
                this.motionX *= var47;
                this.motionY *= var47;
                this.motionZ *= var47;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        final ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", (var2 == null) ? "" : var2.toString());
        tagCompound.setByte("shake", (byte)this.shake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.shake = (tagCompund.getByte("shake") & 0xFF);
        this.inGround = (tagCompund.getByte("inGround") == 1);
    }
    
    public int handleHookRetraction() {
        if (this.worldObj.isRemote) {
            return 0;
        }
        byte var1 = 0;
        if (this.caughtEntity != null) {
            final double var2 = this.angler.posX - this.posX;
            final double var3 = this.angler.posY - this.posY;
            final double var4 = this.angler.posZ - this.posZ;
            final double var5 = MathHelper.sqrt_double(var2 * var2 + var3 * var3 + var4 * var4);
            final double var6 = 0.1;
            final Entity caughtEntity = this.caughtEntity;
            caughtEntity.motionX += var2 * var6;
            final Entity caughtEntity2 = this.caughtEntity;
            caughtEntity2.motionY += var3 * var6 + MathHelper.sqrt_double(var5) * 0.08;
            final Entity caughtEntity3 = this.caughtEntity;
            caughtEntity3.motionZ += var4 * var6;
            var1 = 3;
        }
        else if (this.ticksCatchable > 0) {
            final EntityItem var7 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.func_146033_f());
            final double var8 = this.angler.posX - this.posX;
            final double var9 = this.angler.posY - this.posY;
            final double var10 = this.angler.posZ - this.posZ;
            final double var11 = MathHelper.sqrt_double(var8 * var8 + var9 * var9 + var10 * var10);
            final double var12 = 0.1;
            var7.motionX = var8 * var12;
            var7.motionY = var9 * var12 + MathHelper.sqrt_double(var11) * 0.08;
            var7.motionZ = var10 * var12;
            this.worldObj.spawnEntityInWorld(var7);
            this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5, this.angler.posZ + 0.5, this.rand.nextInt(6) + 1));
            var1 = 1;
        }
        if (this.inGround) {
            var1 = 2;
        }
        this.setDead();
        this.angler.fishEntity = null;
        return var1;
    }
    
    private ItemStack func_146033_f() {
        float var1 = this.worldObj.rand.nextFloat();
        final int var2 = EnchantmentHelper.func_151386_g(this.angler);
        final int var3 = EnchantmentHelper.func_151387_h(this.angler);
        float var4 = 0.1f - var2 * 0.025f - var3 * 0.01f;
        float var5 = 0.05f + var2 * 0.01f - var3 * 0.01f;
        var4 = MathHelper.clamp_float(var4, 0.0f, 1.0f);
        var5 = MathHelper.clamp_float(var5, 0.0f, 1.0f);
        if (var1 < var4) {
            this.angler.triggerAchievement(StatList.junkFishedStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.JUNK)).getItemStack(this.rand);
        }
        var1 -= var4;
        if (var1 < var5) {
            this.angler.triggerAchievement(StatList.treasureFishedStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.VALUABLES)).getItemStack(this.rand);
        }
        final float var6 = var1 - var5;
        this.angler.triggerAchievement(StatList.fishCaughtStat);
        return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.FISH)).getItemStack(this.rand);
    }
    
    @Override
    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }
    }
    
    static {
        JUNK = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeColorDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
        VALUABLES = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable());
        FISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getItemDamage()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getItemDamage()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getItemDamage()), 13));
    }
}
