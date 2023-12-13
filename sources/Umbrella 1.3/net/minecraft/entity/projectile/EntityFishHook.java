/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook
extends Entity {
    private static final List JUNK = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeColorDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
    private static final List VALUABLES = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable());
    private static final List FISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getItemDamage()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getItemDamage()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getItemDamage()), 13));
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
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
        return FISH;
    }

    public EntityFishHook(World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
    }

    public EntityFishHook(World worldIn, double p_i1765_2_, double p_i1765_4_, double p_i1765_6_, EntityPlayer p_i1765_8_) {
        this(worldIn);
        this.setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
        this.ignoreFrustumCheck = true;
        this.angler = p_i1765_8_;
        p_i1765_8_.fishEntity = this;
    }

    public EntityFishHook(World worldIn, EntityPlayer p_i1766_2_) {
        super(worldIn);
        this.ignoreFrustumCheck = true;
        this.angler = p_i1766_2_;
        this.angler.fishEntity = this;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + (double)p_i1766_2_.getEyeHeight(), p_i1766_2_.posZ, p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.posY -= (double)0.1f;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        float var3 = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * var3;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * var3;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * (float)Math.PI) * var3;
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        return distance < (var3 *= 64.0) * var3;
    }

    public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_) {
        float var9 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
        p_146035_1_ /= (double)var9;
        p_146035_3_ /= (double)var9;
        p_146035_5_ /= (double)var9;
        p_146035_1_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_146035_8_;
        p_146035_3_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_146035_8_;
        p_146035_5_ += this.rand.nextGaussian() * (double)0.0075f * (double)p_146035_8_;
        this.motionX = p_146035_1_ *= (double)p_146035_7_;
        this.motionY = p_146035_3_ *= (double)p_146035_7_;
        this.motionZ = p_146035_5_ *= (double)p_146035_7_;
        float var10 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(p_146035_3_, var10) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
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
    public void setVelocity(double x, double y, double z) {
        this.clientMotionX = this.motionX = x;
        this.clientMotionY = this.motionY = y;
        this.clientMotionZ = this.motionZ = z;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fishPosRotationIncrements > 0) {
            double var28 = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
            double var29 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
            double var30 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
            double var7 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.fishPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(var28, var29, var30);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else {
            double var13;
            if (!this.worldObj.isRemote) {
                ItemStack var1 = this.angler.getCurrentEquippedItem();
                if (this.angler.isDead || !this.angler.isEntityAlive() || var1 == null || var1.getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0) {
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }
                if (this.caughtEntity != null) {
                    if (!this.caughtEntity.isDead) {
                        this.posX = this.caughtEntity.posX;
                        double var10002 = this.caughtEntity.height;
                        this.posY = this.caughtEntity.getEntityBoundingBox().minY + var10002 * 0.8;
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
                this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInAir;
            }
            Vec3 var27 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var27, var2);
            var27 = new Vec3(this.posX, this.posY, this.posZ);
            var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (var3 != null) {
                var2 = new Vec3(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
            }
            Entity var4 = null;
            List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double var6 = 0.0;
            for (int var8 = 0; var8 < var5.size(); ++var8) {
                Entity var9 = (Entity)var5.get(var8);
                if (!var9.canBeCollidedWith() || var9 == this.angler && this.ticksInAir < 5) continue;
                float var10 = 0.3f;
                AxisAlignedBB var11 = var9.getEntityBoundingBox().expand(var10, var10, var10);
                MovingObjectPosition var12 = var11.calculateIntercept(var27, var2);
                if (var12 == null || !((var13 = var27.distanceTo(var12.hitVec)) < var6) && var6 != 0.0) continue;
                var4 = var9;
                var6 = var13;
            }
            if (var4 != null) {
                var3 = new MovingObjectPosition(var4);
            }
            if (var3 != null) {
                if (var3.entityHit != null) {
                    if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0f)) {
                        this.caughtEntity = var3.entityHit;
                    }
                } else {
                    this.inGround = true;
                }
            }
            if (!this.inGround) {
                double var19;
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float var31 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
                this.rotationPitch = (float)(Math.atan2(this.motionY, var31) * 180.0 / Math.PI);
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
                float var32 = 0.92f;
                if (this.onGround || this.isCollidedHorizontally) {
                    var32 = 0.5f;
                }
                int var33 = 5;
                double var34 = 0.0;
                for (int var35 = 0; var35 < var33; ++var35) {
                    AxisAlignedBB var14 = this.getEntityBoundingBox();
                    double var15 = var14.maxY - var14.minY;
                    double var17 = var14.minY + var15 * (double)var35 / (double)var33;
                    var19 = var14.minY + var15 * (double)(var35 + 1) / (double)var33;
                    AxisAlignedBB var21 = new AxisAlignedBB(var14.minX, var17, var14.minZ, var14.maxX, var19, var14.maxZ);
                    if (!this.worldObj.isAABBInMaterial(var21, Material.water)) continue;
                    var34 += 1.0 / (double)var33;
                }
                if (!this.worldObj.isRemote && var34 > 0.0) {
                    WorldServer var36 = (WorldServer)this.worldObj;
                    int var37 = 1;
                    BlockPos var38 = new BlockPos(this).offsetUp();
                    if (this.rand.nextFloat() < 0.25f && this.worldObj.func_175727_C(var38)) {
                        var37 = 2;
                    }
                    if (this.rand.nextFloat() < 0.5f && !this.worldObj.isAgainstSky(var38)) {
                        --var37;
                    }
                    if (this.ticksCatchable > 0) {
                        --this.ticksCatchable;
                        if (this.ticksCatchable <= 0) {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    } else if (this.ticksCatchableDelay > 0) {
                        this.ticksCatchableDelay -= var37;
                        if (this.ticksCatchableDelay <= 0) {
                            this.motionY -= (double)0.2f;
                            this.playSound("random.splash", 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                            float var16 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
                            var36.func_175739_a(EnumParticleTypes.WATER_BUBBLE, this.posX, var16 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                            var36.func_175739_a(EnumParticleTypes.WATER_WAKE, this.posX, var16 + 1.0f, this.posZ, (int)(1.0f + this.width * 20.0f), this.width, 0.0, this.width, 0.2f, new int[0]);
                            this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                        } else {
                            this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0);
                            float var16 = this.fishApproachAngle * ((float)Math.PI / 180);
                            float var39 = MathHelper.sin(var16);
                            float var18 = MathHelper.cos(var16);
                            var19 = this.posX + (double)(var39 * (float)this.ticksCatchableDelay * 0.1f);
                            double var40 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            double var23 = this.posZ + (double)(var18 * (float)this.ticksCatchableDelay * 0.1f);
                            if (this.rand.nextFloat() < 0.15f) {
                                var36.func_175739_a(EnumParticleTypes.WATER_BUBBLE, var19, var40 - (double)0.1f, var23, 1, var39, 0.1, var18, 0.0, new int[0]);
                            }
                            float var25 = var39 * 0.04f;
                            float var26 = var18 * 0.04f;
                            var36.func_175739_a(EnumParticleTypes.WATER_WAKE, var19, var40, var23, 0, var26, 0.01, -var25, 1.0, new int[0]);
                            var36.func_175739_a(EnumParticleTypes.WATER_WAKE, var19, var40, var23, 0, -var26, 0.01, var25, 1.0, new int[0]);
                        }
                    } else if (this.ticksCaughtDelay > 0) {
                        this.ticksCaughtDelay -= var37;
                        float var16 = 0.15f;
                        if (this.ticksCaughtDelay < 20) {
                            var16 = (float)((double)var16 + (double)(20 - this.ticksCaughtDelay) * 0.05);
                        } else if (this.ticksCaughtDelay < 40) {
                            var16 = (float)((double)var16 + (double)(40 - this.ticksCaughtDelay) * 0.02);
                        } else if (this.ticksCaughtDelay < 60) {
                            var16 = (float)((double)var16 + (double)(60 - this.ticksCaughtDelay) * 0.01);
                        }
                        if (this.rand.nextFloat() < var16) {
                            float var39 = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f) * ((float)Math.PI / 180);
                            float var18 = MathHelper.randomFloatClamp(this.rand, 25.0f, 60.0f);
                            var19 = this.posX + (double)(MathHelper.sin(var39) * var18 * 0.1f);
                            double var40 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            double var23 = this.posZ + (double)(MathHelper.cos(var39) * var18 * 0.1f);
                            var36.func_175739_a(EnumParticleTypes.WATER_SPLASH, var19, var40, var23, 2 + this.rand.nextInt(2), 0.1f, 0.0, 0.1f, 0.0, new int[0]);
                        }
                        if (this.ticksCaughtDelay <= 0) {
                            this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f);
                            this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                        }
                    } else {
                        this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                        this.ticksCaughtDelay -= EnchantmentHelper.func_151387_h(this.angler) * 20 * 5;
                    }
                    if (this.ticksCatchable > 0) {
                        this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2;
                    }
                }
                var13 = var34 * 2.0 - 1.0;
                this.motionY += (double)0.04f * var13;
                if (var34 > 0.0) {
                    var32 = (float)((double)var32 * 0.9);
                    this.motionY *= 0.8;
                }
                this.motionX *= (double)var32;
                this.motionY *= (double)var32;
                this.motionZ *= (double)var32;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", var2 == null ? "" : var2.toString());
        tagCompound.setByte("shake", (byte)this.shake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.inTile = tagCompund.hasKey("inTile", 8) ? Block.getBlockFromName(tagCompund.getString("inTile")) : Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        this.shake = tagCompund.getByte("shake") & 0xFF;
        this.inGround = tagCompund.getByte("inGround") == 1;
    }

    public int handleHookRetraction() {
        if (this.worldObj.isRemote) {
            return 0;
        }
        int var1 = 0;
        if (this.caughtEntity != null) {
            double var2 = this.angler.posX - this.posX;
            double var4 = this.angler.posY - this.posY;
            double var6 = this.angler.posZ - this.posZ;
            double var8 = MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1;
            this.caughtEntity.motionX += var2 * var10;
            this.caughtEntity.motionY += var4 * var10 + (double)MathHelper.sqrt_double(var8) * 0.08;
            this.caughtEntity.motionZ += var6 * var10;
            var1 = 3;
        } else if (this.ticksCatchable > 0) {
            EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.func_146033_f());
            double var3 = this.angler.posX - this.posX;
            double var5 = this.angler.posY - this.posY;
            double var7 = this.angler.posZ - this.posZ;
            double var9 = MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1;
            var13.motionX = var3 * var11;
            var13.motionY = var5 * var11 + (double)MathHelper.sqrt_double(var9) * 0.08;
            var13.motionZ = var7 * var11;
            this.worldObj.spawnEntityInWorld(var13);
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
        int var2 = EnchantmentHelper.func_151386_g(this.angler);
        int var3 = EnchantmentHelper.func_151387_h(this.angler);
        float var4 = 0.1f - (float)var2 * 0.025f - (float)var3 * 0.01f;
        float var5 = 0.05f + (float)var2 * 0.01f - (float)var3 * 0.01f;
        var4 = MathHelper.clamp_float(var4, 0.0f, 1.0f);
        var5 = MathHelper.clamp_float(var5, 0.0f, 1.0f);
        if (var1 < var4) {
            this.angler.triggerAchievement(StatList.junkFishedStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
        }
        if ((var1 -= var4) < var5) {
            this.angler.triggerAchievement(StatList.treasureFishedStat);
            return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, VALUABLES)).getItemStack(this.rand);
        }
        float var10000 = var1 - var5;
        this.angler.triggerAchievement(StatList.fishCaughtStat);
        return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
    }

    @Override
    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }
    }
}

