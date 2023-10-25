/*
 * Decompiled with CFR 0.152.
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
    private int yTile = -1;
    public Entity caughtEntity;
    private Block inTile;
    private double clientMotionZ;
    private double fishX;
    private static final List<WeightedRandomFishable> JUNK = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9f), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10));
    private double fishYaw;
    private static final List<WeightedRandomFishable> TREASURE = Arrays.asList(new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25f).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable());
    private double fishPitch;
    public int shake;
    private float fishApproachAngle;
    private int ticksCatchable;
    private double fishZ;
    private double fishY;
    private double clientMotionX;
    private int ticksInGround;
    public EntityPlayer angler;
    private boolean inGround;
    private int fishPosRotationIncrements;
    private int zTile = -1;
    private int ticksCatchableDelay;
    private int xTile = -1;
    private int ticksCaughtDelay;
    private int ticksInAir;
    private double clientMotionY;
    private static final List<WeightedRandomFishable> FISH = Arrays.asList(new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13));

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setShort("xTile", (short)this.xTile);
        nBTTagCompound.setShort("yTile", (short)this.yTile);
        nBTTagCompound.setShort("zTile", (short)this.zTile);
        ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
        nBTTagCompound.setString("inTile", resourceLocation == null ? "" : resourceLocation.toString());
        nBTTagCompound.setByte("shake", (byte)this.shake);
        nBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
    }

    @Override
    protected void entityInit() {
    }

    public void handleHookCasting(double d, double d2, double d3, float f, float f2) {
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3);
        d /= (double)f3;
        d2 /= (double)f3;
        d3 /= (double)f3;
        d += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        d2 += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        d3 += this.rand.nextGaussian() * (double)0.0075f * (double)f2;
        this.motionX = d *= (double)f;
        this.motionY = d2 *= (double)f;
        this.motionZ = d3 *= (double)f;
        float f4 = MathHelper.sqrt_double(d * d + d3 * d3);
        this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(d, d3) * 180.0 / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(d2, f4) * 180.0 / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    public void setVelocity(double d, double d2, double d3) {
        this.clientMotionX = this.motionX = d;
        this.clientMotionY = this.motionY = d2;
        this.clientMotionZ = this.motionZ = d3;
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.fishX = d;
        this.fishY = d2;
        this.fishZ = d3;
        this.fishYaw = f;
        this.fishPitch = f2;
        this.fishPosRotationIncrements = n;
        this.motionX = this.clientMotionX;
        this.motionY = this.clientMotionY;
        this.motionZ = this.clientMotionZ;
    }

    @Override
    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        this.xTile = nBTTagCompound.getShort("xTile");
        this.yTile = nBTTagCompound.getShort("yTile");
        this.zTile = nBTTagCompound.getShort("zTile");
        this.inTile = nBTTagCompound.hasKey("inTile", 8) ? Block.getBlockFromName(nBTTagCompound.getString("inTile")) : Block.getBlockById(nBTTagCompound.getByte("inTile") & 0xFF);
        this.shake = nBTTagCompound.getByte("shake") & 0xFF;
        this.inGround = nBTTagCompound.getByte("inGround") == 1;
    }

    public EntityFishHook(World world) {
        super(world);
        this.setSize(0.25f, 0.25f);
        this.ignoreFrustumCheck = true;
    }

    public EntityFishHook(World world, double d, double d2, double d3, EntityPlayer entityPlayer) {
        this(world);
        this.setPosition(d, d2, d3);
        this.ignoreFrustumCheck = true;
        this.angler = entityPlayer;
        entityPlayer.fishEntity = this;
    }

    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d2 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d2)) {
            d2 = 4.0;
        }
        return d < (d2 *= 64.0) * d2;
    }

    public EntityFishHook(World world, EntityPlayer entityPlayer) {
        super(world);
        this.ignoreFrustumCheck = true;
        this.angler = entityPlayer;
        this.angler.fishEntity = this;
        this.setSize(0.25f, 0.25f);
        this.setLocationAndAngles(entityPlayer.posX, entityPlayer.posY + (double)entityPlayer.getEyeHeight(), entityPlayer.posZ, entityPlayer.rotationYaw, entityPlayer.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.posY -= (double)0.1f;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * 0.16f);
        this.setPosition(this.posX, this.posY, this.posZ);
        float f = 0.4f;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * (float)Math.PI) * f;
        this.handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5f, 1.0f);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.fishPosRotationIncrements > 0) {
            double d = this.posX + (this.fishX - this.posX) / (double)this.fishPosRotationIncrements;
            double d2 = this.posY + (this.fishY - this.posY) / (double)this.fishPosRotationIncrements;
            double d3 = this.posZ + (this.fishZ - this.posZ) / (double)this.fishPosRotationIncrements;
            double d4 = MathHelper.wrapAngleTo180_double(this.fishYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d4 / (double)this.fishPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.fishPitch - (double)this.rotationPitch) / (double)this.fishPosRotationIncrements);
            --this.fishPosRotationIncrements;
            this.setPosition(d, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        } else {
            Object object;
            if (!this.worldObj.isRemote) {
                object = this.angler.getCurrentEquippedItem();
                if (this.angler.isDead || !this.angler.isEntityAlive() || object == null || ((ItemStack)object).getItem() != Items.fishing_rod || this.getDistanceSqToEntity(this.angler) > 1024.0) {
                    this.setDead();
                    this.angler.fishEntity = null;
                    return;
                }
                if (this.caughtEntity != null) {
                    if (!this.caughtEntity.isDead) {
                        this.posX = this.caughtEntity.posX;
                        double d = this.caughtEntity.height;
                        this.posY = this.caughtEntity.getEntityBoundingBox().minY + d * 0.8;
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
            object = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingObjectPosition = this.worldObj.rayTraceBlocks((Vec3)object, vec3);
            object = new Vec3(this.posX, this.posY, this.posZ);
            vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingObjectPosition != null) {
                vec3 = new Vec3(movingObjectPosition.hitVec.xCoord, movingObjectPosition.hitVec.yCoord, movingObjectPosition.hitVec.zCoord);
            }
            Entity entity = null;
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d = 0.0;
            int n = 0;
            while (n < list.size()) {
                Entity entity2 = list.get(n);
                if (entity2.canBeCollidedWith() && (entity2 != this.angler || this.ticksInAir >= 5)) {
                    double d5;
                    float f = 0.3f;
                    AxisAlignedBB axisAlignedBB = entity2.getEntityBoundingBox().expand(f, f, f);
                    MovingObjectPosition movingObjectPosition2 = axisAlignedBB.calculateIntercept((Vec3)object, vec3);
                    if (movingObjectPosition2 != null && ((d5 = ((Vec3)object).squareDistanceTo(movingObjectPosition2.hitVec)) < d || d == 0.0)) {
                        entity = entity2;
                        d = d5;
                    }
                }
                ++n;
            }
            if (entity != null) {
                movingObjectPosition = new MovingObjectPosition(entity);
            }
            if (movingObjectPosition != null) {
                if (movingObjectPosition.entityHit != null) {
                    if (movingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0f)) {
                        this.caughtEntity = movingObjectPosition.entityHit;
                    }
                } else {
                    this.inGround = true;
                }
            }
            if (!this.inGround) {
                double d6;
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / Math.PI);
                this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0 / Math.PI);
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
                float f2 = 0.92f;
                if (this.onGround || this.isCollidedHorizontally) {
                    f2 = 0.5f;
                }
                int n2 = 5;
                double d7 = 0.0;
                int n3 = 0;
                while (n3 < n2) {
                    AxisAlignedBB axisAlignedBB = this.getEntityBoundingBox();
                    double d8 = axisAlignedBB.maxY - axisAlignedBB.minY;
                    double d9 = axisAlignedBB.minY + d8 * (double)n3 / (double)n2;
                    d6 = axisAlignedBB.minY + d8 * (double)(n3 + 1) / (double)n2;
                    AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX, d9, axisAlignedBB.minZ, axisAlignedBB.maxX, d6, axisAlignedBB.maxZ);
                    if (this.worldObj.isAABBInMaterial(axisAlignedBB2, Material.water)) {
                        d7 += 1.0 / (double)n2;
                    }
                    ++n3;
                }
                if (!this.worldObj.isRemote && d7 > 0.0) {
                    WorldServer worldServer = (WorldServer)this.worldObj;
                    int n4 = 1;
                    BlockPos blockPos = new BlockPos(this).up();
                    if (this.rand.nextFloat() < 0.25f && this.worldObj.canLightningStrike(blockPos)) {
                        n4 = 2;
                    }
                    if (this.rand.nextFloat() < 0.5f && !this.worldObj.canSeeSky(blockPos)) {
                        --n4;
                    }
                    if (this.ticksCatchable > 0) {
                        --this.ticksCatchable;
                        if (this.ticksCatchable <= 0) {
                            this.ticksCaughtDelay = 0;
                            this.ticksCatchableDelay = 0;
                        }
                    } else if (this.ticksCatchableDelay > 0) {
                        this.ticksCatchableDelay -= n4;
                        if (this.ticksCatchableDelay <= 0) {
                            this.motionY -= (double)0.2f;
                            this.playSound("random.splash", 0.25f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4f);
                            float f3 = MathHelper.floor_double(this.getEntityBoundingBox().minY);
                            worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, (double)(f3 + 1.0f), this.posZ, (int)(1.0f + this.width * 20.0f), (double)this.width, 0.0, (double)this.width, (double)0.2f, new int[0]);
                            worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, (double)(f3 + 1.0f), this.posZ, (int)(1.0f + this.width * 20.0f), (double)this.width, 0.0, (double)this.width, (double)0.2f, new int[0]);
                            this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
                        } else {
                            double d10;
                            this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0);
                            float f4 = this.fishApproachAngle * ((float)Math.PI / 180);
                            float f5 = MathHelper.sin(f4);
                            float f6 = MathHelper.cos(f4);
                            d6 = this.posX + (double)(f5 * (float)this.ticksCatchableDelay * 0.1f);
                            double d11 = (float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f;
                            Block block = worldServer.getBlockState(new BlockPos((int)d6, (int)d11 - 1, (int)(d10 = this.posZ + (double)(f6 * (float)this.ticksCatchableDelay * 0.1f)))).getBlock();
                            if (block == Blocks.water || block == Blocks.flowing_water) {
                                if (this.rand.nextFloat() < 0.15f) {
                                    worldServer.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d6, d11 - (double)0.1f, d10, 1, (double)f5, 0.1, (double)f6, 0.0, new int[0]);
                                }
                                float f7 = f5 * 0.04f;
                                float f8 = f6 * 0.04f;
                                worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, d6, d11, d10, 0, (double)f8, 0.01, (double)(-f7), 1.0, new int[0]);
                                worldServer.spawnParticle(EnumParticleTypes.WATER_WAKE, d6, d11, d10, 0, (double)(-f8), 0.01, (double)f7, 1.0, new int[0]);
                            }
                        }
                    } else if (this.ticksCaughtDelay > 0) {
                        this.ticksCaughtDelay -= n4;
                        float f9 = 0.15f;
                        if (this.ticksCaughtDelay < 20) {
                            f9 = (float)((double)f9 + (double)(20 - this.ticksCaughtDelay) * 0.05);
                        } else if (this.ticksCaughtDelay < 40) {
                            f9 = (float)((double)f9 + (double)(40 - this.ticksCaughtDelay) * 0.02);
                        } else if (this.ticksCaughtDelay < 60) {
                            f9 = (float)((double)f9 + (double)(60 - this.ticksCaughtDelay) * 0.01);
                        }
                        if (this.rand.nextFloat() < f9) {
                            double d12;
                            double d13;
                            float f10 = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f) * ((float)Math.PI / 180);
                            float f11 = MathHelper.randomFloatClamp(this.rand, 25.0f, 60.0f);
                            d6 = this.posX + (double)(MathHelper.sin(f10) * f11 * 0.1f);
                            Block block = worldServer.getBlockState(new BlockPos((int)d6, (int)(d13 = (double)((float)MathHelper.floor_double(this.getEntityBoundingBox().minY) + 1.0f)) - 1, (int)(d12 = this.posZ + (double)(MathHelper.cos(f10) * f11 * 0.1f)))).getBlock();
                            if (block == Blocks.water || block == Blocks.flowing_water) {
                                worldServer.spawnParticle(EnumParticleTypes.WATER_SPLASH, d6, d13, d12, 2 + this.rand.nextInt(2), (double)0.1f, 0.0, (double)0.1f, 0.0, new int[0]);
                            }
                        }
                        if (this.ticksCaughtDelay <= 0) {
                            this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0f, 360.0f);
                            this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
                        }
                    } else {
                        this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
                        this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier(this.angler) * 20 * 5;
                    }
                    if (this.ticksCatchable > 0) {
                        this.motionY -= (double)(this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2;
                    }
                }
                double d14 = d7 * 2.0 - 1.0;
                this.motionY += (double)0.04f * d14;
                if (d7 > 0.0) {
                    f2 = (float)((double)f2 * 0.9);
                    this.motionY *= 0.8;
                }
                this.motionX *= (double)f2;
                this.motionY *= (double)f2;
                this.motionZ *= (double)f2;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
    }

    private ItemStack getFishingResult() {
        float f = this.worldObj.rand.nextFloat();
        int n = EnchantmentHelper.getLuckOfSeaModifier(this.angler);
        int n2 = EnchantmentHelper.getLureModifier(this.angler);
        float f2 = 0.1f - (float)n * 0.025f - (float)n2 * 0.01f;
        float f3 = 0.05f + (float)n * 0.01f - (float)n2 * 0.01f;
        f2 = MathHelper.clamp_float(f2, 0.0f, 1.0f);
        f3 = MathHelper.clamp_float(f3, 0.0f, 1.0f);
        if (f < f2) {
            this.angler.triggerAchievement(StatList.junkFishedStat);
            return WeightedRandom.getRandomItem(this.rand, JUNK).getItemStack(this.rand);
        }
        if ((f -= f2) < f3) {
            this.angler.triggerAchievement(StatList.treasureFishedStat);
            return WeightedRandom.getRandomItem(this.rand, TREASURE).getItemStack(this.rand);
        }
        float f4 = f - f3;
        this.angler.triggerAchievement(StatList.fishCaughtStat);
        return WeightedRandom.getRandomItem(this.rand, FISH).getItemStack(this.rand);
    }

    public static List<WeightedRandomFishable> func_174855_j() {
        return FISH;
    }

    public int handleHookRetraction() {
        if (this.worldObj.isRemote) {
            return 0;
        }
        int n = 0;
        if (this.caughtEntity != null) {
            double d = this.angler.posX - this.posX;
            double d2 = this.angler.posY - this.posY;
            double d3 = this.angler.posZ - this.posZ;
            double d4 = MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3);
            double d5 = 0.1;
            this.caughtEntity.motionX += d * d5;
            this.caughtEntity.motionY += d2 * d5 + (double)MathHelper.sqrt_double(d4) * 0.08;
            this.caughtEntity.motionZ += d3 * d5;
            n = 3;
        } else if (this.ticksCatchable > 0) {
            EntityItem entityItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getFishingResult());
            double d = this.angler.posX - this.posX;
            double d6 = this.angler.posY - this.posY;
            double d7 = this.angler.posZ - this.posZ;
            double d8 = MathHelper.sqrt_double(d * d + d6 * d6 + d7 * d7);
            double d9 = 0.1;
            entityItem.motionX = d * d9;
            entityItem.motionY = d6 * d9 + (double)MathHelper.sqrt_double(d8) * 0.08;
            entityItem.motionZ = d7 * d9;
            this.worldObj.spawnEntityInWorld(entityItem);
            this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5, this.angler.posZ + 0.5, this.rand.nextInt(6) + 1));
            n = 1;
        }
        if (this.inGround) {
            n = 2;
        }
        this.setDead();
        this.angler.fishEntity = null;
        return n;
    }
}

