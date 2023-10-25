/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart
extends Entity
implements IWorldNameable {
    private double velocityX;
    private static final int[][][] matrix;
    private double minecartYaw;
    private double minecartX;
    private int turnProgress;
    private double minecartY;
    private double minecartZ;
    private double velocityZ;
    private double velocityY;
    private boolean isInReverse;
    private double minecartPitch;
    private String entityName;

    static {
        int[][][] nArrayArray = new int[10][][];
        int[][] nArrayArray2 = new int[2][];
        int[] nArray = new int[3];
        nArray[2] = -1;
        nArrayArray2[0] = nArray;
        int[] nArray2 = new int[3];
        nArray2[2] = 1;
        nArrayArray2[1] = nArray2;
        nArrayArray[0] = nArrayArray2;
        int[][] nArrayArray3 = new int[2][];
        int[] nArray3 = new int[3];
        nArray3[0] = -1;
        nArrayArray3[0] = nArray3;
        int[] nArray4 = new int[3];
        nArray4[0] = 1;
        nArrayArray3[1] = nArray4;
        nArrayArray[1] = nArrayArray3;
        int[][] nArrayArray4 = new int[2][];
        int[] nArray5 = new int[3];
        nArray5[0] = -1;
        nArray5[1] = -1;
        nArrayArray4[0] = nArray5;
        int[] nArray6 = new int[3];
        nArray6[0] = 1;
        nArrayArray4[1] = nArray6;
        nArrayArray[2] = nArrayArray4;
        int[][] nArrayArray5 = new int[2][];
        int[] nArray7 = new int[3];
        nArray7[0] = -1;
        nArrayArray5[0] = nArray7;
        int[] nArray8 = new int[3];
        nArray8[0] = 1;
        nArray8[1] = -1;
        nArrayArray5[1] = nArray8;
        nArrayArray[3] = nArrayArray5;
        int[][] nArrayArray6 = new int[2][];
        int[] nArray9 = new int[3];
        nArray9[2] = -1;
        nArrayArray6[0] = nArray9;
        int[] nArray10 = new int[3];
        nArray10[1] = -1;
        nArray10[2] = 1;
        nArrayArray6[1] = nArray10;
        nArrayArray[4] = nArrayArray6;
        int[][] nArrayArray7 = new int[2][];
        int[] nArray11 = new int[3];
        nArray11[1] = -1;
        nArray11[2] = -1;
        nArrayArray7[0] = nArray11;
        int[] nArray12 = new int[3];
        nArray12[2] = 1;
        nArrayArray7[1] = nArray12;
        nArrayArray[5] = nArrayArray7;
        int[][] nArrayArray8 = new int[2][];
        int[] nArray13 = new int[3];
        nArray13[2] = 1;
        nArrayArray8[0] = nArray13;
        int[] nArray14 = new int[3];
        nArray14[0] = 1;
        nArrayArray8[1] = nArray14;
        nArrayArray[6] = nArrayArray8;
        int[][] nArrayArray9 = new int[2][];
        int[] nArray15 = new int[3];
        nArray15[2] = 1;
        nArrayArray9[0] = nArray15;
        int[] nArray16 = new int[3];
        nArray16[0] = -1;
        nArrayArray9[1] = nArray16;
        nArrayArray[7] = nArrayArray9;
        int[][] nArrayArray10 = new int[2][];
        int[] nArray17 = new int[3];
        nArray17[2] = -1;
        nArrayArray10[0] = nArray17;
        int[] nArray18 = new int[3];
        nArray18[0] = -1;
        nArrayArray10[1] = nArray18;
        nArrayArray[8] = nArrayArray10;
        int[][] nArrayArray11 = new int[2][];
        int[] nArray19 = new int[3];
        nArray19[2] = -1;
        nArrayArray11[0] = nArray19;
        int[] nArray20 = new int[3];
        nArray20[0] = 1;
        nArrayArray11[1] = nArray20;
        nArrayArray[9] = nArrayArray11;
        matrix = nArrayArray;
    }

    protected void func_180460_a(BlockPos blockPos, IBlockState iBlockState) {
        double d;
        double d2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        this.fallDistance = 0.0f;
        Vec3 vec3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = blockPos.getY();
        boolean bl = false;
        boolean bl2 = false;
        BlockRailBase blockRailBase = (BlockRailBase)iBlockState.getBlock();
        if (blockRailBase == Blocks.golden_rail) {
            bl = iBlockState.getValue(BlockRailPowered.POWERED);
            bl2 = !bl;
        }
        double d8 = 0.0078125;
        BlockRailBase.EnumRailDirection enumRailDirection = iBlockState.getValue(blockRailBase.getShapeProperty());
        switch (enumRailDirection) {
            case ASCENDING_EAST: {
                this.motionX -= 0.0078125;
                this.posY += 1.0;
                break;
            }
            case ASCENDING_WEST: {
                this.motionX += 0.0078125;
                this.posY += 1.0;
                break;
            }
            case ASCENDING_NORTH: {
                this.motionZ += 0.0078125;
                this.posY += 1.0;
                break;
            }
            case ASCENDING_SOUTH: {
                this.motionZ -= 0.0078125;
                this.posY += 1.0;
            }
        }
        int[][] nArray = matrix[enumRailDirection.getMetadata()];
        double d9 = nArray[1][0] - nArray[0][0];
        double d10 = nArray[1][2] - nArray[0][2];
        double d11 = Math.sqrt(d9 * d9 + d10 * d10);
        double d12 = this.motionX * d9 + this.motionZ * d10;
        if (d12 < 0.0) {
            d9 = -d9;
            d10 = -d10;
        }
        if ((d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 2.0) {
            d7 = 2.0;
        }
        this.motionX = d7 * d9 / d11;
        this.motionZ = d7 * d10 / d11;
        if (this.riddenByEntity instanceof EntityLivingBase && (d6 = (double)((EntityLivingBase)this.riddenByEntity).moveForward) > 0.0) {
            d5 = -Math.sin(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0f);
            d4 = Math.cos(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0f);
            d3 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (d3 < 0.01) {
                this.motionX += d5 * 0.1;
                this.motionZ += d4 * 0.1;
                bl2 = false;
            }
        }
        if (bl2) {
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d6 < 0.03) {
                this.motionX *= 0.0;
                this.motionY *= 0.0;
                this.motionZ *= 0.0;
            } else {
                this.motionX *= 0.5;
                this.motionY *= 0.0;
                this.motionZ *= 0.5;
            }
        }
        d6 = 0.0;
        d5 = (double)blockPos.getX() + 0.5 + (double)nArray[0][0] * 0.5;
        d4 = (double)blockPos.getZ() + 0.5 + (double)nArray[0][2] * 0.5;
        d3 = (double)blockPos.getX() + 0.5 + (double)nArray[1][0] * 0.5;
        double d13 = (double)blockPos.getZ() + 0.5 + (double)nArray[1][2] * 0.5;
        d9 = d3 - d5;
        d10 = d13 - d4;
        if (d9 == 0.0) {
            this.posX = (double)blockPos.getX() + 0.5;
            d6 = this.posZ - (double)blockPos.getZ();
        } else if (d10 == 0.0) {
            this.posZ = (double)blockPos.getZ() + 0.5;
            d6 = this.posX - (double)blockPos.getX();
        } else {
            d2 = this.posX - d5;
            d = this.posZ - d4;
            d6 = (d2 * d9 + d * d10) * 2.0;
        }
        this.posX = d5 + d9 * d6;
        this.posZ = d4 + d10 * d6;
        this.setPosition(this.posX, this.posY, this.posZ);
        d2 = this.motionX;
        d = this.motionZ;
        if (this.riddenByEntity != null) {
            d2 *= 0.75;
            d *= 0.75;
        }
        double d14 = this.getMaximumSpeed();
        d2 = MathHelper.clamp_double(d2, -d14, d14);
        d = MathHelper.clamp_double(d, -d14, d14);
        this.moveEntity(d2, 0.0, d);
        if (nArray[0][1] != 0 && MathHelper.floor_double(this.posX) - blockPos.getX() == nArray[0][0] && MathHelper.floor_double(this.posZ) - blockPos.getZ() == nArray[0][2]) {
            this.setPosition(this.posX, this.posY + (double)nArray[0][1], this.posZ);
        } else if (nArray[1][1] != 0 && MathHelper.floor_double(this.posX) - blockPos.getX() == nArray[1][0] && MathHelper.floor_double(this.posZ) - blockPos.getZ() == nArray[1][2]) {
            this.setPosition(this.posX, this.posY + (double)nArray[1][1], this.posZ);
        }
        this.applyDrag();
        Vec3 vec32 = this.func_70489_a(this.posX, this.posY, this.posZ);
        if (vec32 != null && vec3 != null) {
            double d15 = (vec3.yCoord - vec32.yCoord) * 0.05;
            d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d7 > 0.0) {
                this.motionX = this.motionX / d7 * (d7 + d15);
                this.motionZ = this.motionZ / d7 * (d7 + d15);
            }
            this.setPosition(this.posX, vec32.yCoord, this.posZ);
        }
        int n = MathHelper.floor_double(this.posX);
        int n2 = MathHelper.floor_double(this.posZ);
        if (n != blockPos.getX() || n2 != blockPos.getZ()) {
            d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d7 * (double)(n - blockPos.getX());
            this.motionZ = d7 * (double)(n2 - blockPos.getZ());
        }
        if (bl) {
            double d16 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d16 > 0.01) {
                double d17 = 0.06;
                this.motionX += this.motionX / d16 * d17;
                this.motionZ += this.motionZ / d16 * d17;
            } else if (enumRailDirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.worldObj.getBlockState(blockPos.west()).getBlock().isNormalCube()) {
                    this.motionX = 0.02;
                } else if (this.worldObj.getBlockState(blockPos.east()).getBlock().isNormalCube()) {
                    this.motionX = -0.02;
                }
            } else if (enumRailDirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.worldObj.getBlockState(blockPos.north()).getBlock().isNormalCube()) {
                    this.motionZ = 0.02;
                } else if (this.worldObj.getBlockState(blockPos.south()).getBlock().isNormalCube()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }

    public boolean hasDisplayTile() {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public int getRollingDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    @Override
    public void onUpdate() {
        int n;
        int n2;
        if (this.getRollingAmplitude() > 0) {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }
        if (this.getDamage() > 0.0f) {
            this.setDamage(this.getDamage() - 1.0f);
        }
        if (this.posY < -64.0) {
            this.kill();
        }
        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftServer = ((WorldServer)this.worldObj).getMinecraftServer();
            n2 = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftServer.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= n2) {
                        this.portalCounter = n2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        n = this.worldObj.provider.getDimensionId() == -1 ? 0 : -1;
                        this.travelToDimension(n);
                    }
                    this.inPortal = false;
                }
            } else {
                if (this.portalCounter > 0) {
                    this.portalCounter -= 4;
                }
                if (this.portalCounter < 0) {
                    this.portalCounter = 0;
                }
            }
            if (this.timeUntilPortal > 0) {
                --this.timeUntilPortal;
            }
            this.worldObj.theProfiler.endSection();
        }
        if (this.worldObj.isRemote) {
            if (this.turnProgress > 0) {
                double d = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double d2 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double d3 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double d4 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d4 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(d, d2, d3);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            double d;
            BlockPos blockPos;
            IBlockState iBlockState;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= (double)0.04f;
            int n3 = MathHelper.floor_double(this.posX);
            if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(n3, (n2 = MathHelper.floor_double(this.posY)) - 1, n = MathHelper.floor_double(this.posZ)))) {
                --n2;
            }
            if (BlockRailBase.isRailBlock(iBlockState = this.worldObj.getBlockState(blockPos = new BlockPos(n3, n2, n)))) {
                this.func_180460_a(blockPos, iBlockState);
                if (iBlockState.getBlock() == Blocks.activator_rail) {
                    this.onActivatorRailPass(n3, n2, n, iBlockState.getValue(BlockRailPowered.POWERED));
                }
            } else {
                this.moveDerailedMinecart();
            }
            this.doBlockCollisions();
            this.rotationPitch = 0.0f;
            double d5 = this.prevPosX - this.posX;
            double d6 = this.prevPosZ - this.posZ;
            if (d5 * d5 + d6 * d6 > 0.001) {
                this.rotationYaw = (float)(MathHelper.func_181159_b(d6, d5) * 180.0 / Math.PI);
                if (this.isInReverse) {
                    this.rotationYaw += 180.0f;
                }
            }
            if ((d = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw)) < -170.0 || d >= 170.0) {
                this.rotationYaw += 180.0f;
                this.isInReverse = !this.isInReverse;
            }
            this.setRotation(this.rotationYaw, this.rotationPitch);
            for (Entity entity : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.2f, 0.0, 0.2f))) {
                if (entity == this.riddenByEntity || !entity.canBePushed() || !(entity instanceof EntityMinecart)) continue;
                entity.applyEntityCollision(this);
            }
            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                if (this.riddenByEntity.ridingEntity == this) {
                    this.riddenByEntity.ridingEntity = null;
                }
                this.riddenByEntity = null;
            }
            this.handleWaterMovement();
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public void setHasDisplayTile(boolean bl) {
        this.getDataWatcher().updateObject(22, (byte)(bl ? 1 : 0));
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.air.getDefaultState();
    }

    public void setRollingDirection(int n) {
        this.dataWatcher.updateObject(18, n);
    }

    @Override
    public void setCustomNameTag(String string) {
        this.entityName = string;
    }

    @Override
    public void setPositionAndRotation2(double d, double d2, double d3, float f, float f2, int n, boolean bl) {
        this.minecartX = d;
        this.minecartY = d2;
        this.minecartZ = d3;
        this.minecartYaw = f;
        this.minecartPitch = f2;
        this.turnProgress = n + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0f));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, (byte)0);
    }

    @Override
    public double getMountedYOffset() {
        return 0.0;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    public int getDisplayTileOffset() {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        if (!(this.worldObj.isRemote || entity.noClip || this.noClip || entity == this.riddenByEntity)) {
            double d;
            double d2;
            double d3;
            if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer) && !(entity instanceof EntityIronGolem) && this.getMinecartType() == EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01 && this.riddenByEntity == null && entity.ridingEntity == null) {
                entity.mountEntity(this);
            }
            if ((d3 = (d2 = entity.posX - this.posX) * d2 + (d = entity.posZ - this.posZ) * d) >= (double)1.0E-4f) {
                d3 = MathHelper.sqrt_double(d3);
                d2 /= d3;
                d /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d2 *= d4;
                d *= d4;
                d2 *= (double)0.1f;
                d *= (double)0.1f;
                d2 *= (double)(1.0f - this.entityCollisionReduction);
                d *= (double)(1.0f - this.entityCollisionReduction);
                d2 *= 0.5;
                d *= 0.5;
                if (entity instanceof EntityMinecart) {
                    Vec3 vec3;
                    double d5 = entity.posX - this.posX;
                    double d6 = entity.posZ - this.posZ;
                    Vec3 vec32 = new Vec3(d5, 0.0, d6).normalize();
                    double d7 = Math.abs(vec32.dotProduct(vec3 = new Vec3(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f), 0.0, MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f)).normalize()));
                    if (d7 < (double)0.8f) {
                        return;
                    }
                    double d8 = entity.motionX + this.motionX;
                    double d9 = entity.motionZ + this.motionZ;
                    if (((EntityMinecart)entity).getMinecartType() == EnumMinecartType.FURNACE && this.getMinecartType() != EnumMinecartType.FURNACE) {
                        this.motionX *= (double)0.2f;
                        this.motionZ *= (double)0.2f;
                        this.addVelocity(entity.motionX - d2, 0.0, entity.motionZ - d);
                        entity.motionX *= (double)0.95f;
                        entity.motionZ *= (double)0.95f;
                    } else if (((EntityMinecart)entity).getMinecartType() != EnumMinecartType.FURNACE && this.getMinecartType() == EnumMinecartType.FURNACE) {
                        entity.motionX *= (double)0.2f;
                        entity.motionZ *= (double)0.2f;
                        entity.addVelocity(this.motionX + d2, 0.0, this.motionZ + d);
                        this.motionX *= (double)0.95f;
                        this.motionZ *= (double)0.95f;
                    } else {
                        this.motionX *= (double)0.2f;
                        this.motionZ *= (double)0.2f;
                        this.addVelocity((d8 /= 2.0) - d2, 0.0, (d9 /= 2.0) - d);
                        entity.motionX *= (double)0.2f;
                        entity.motionZ *= (double)0.2f;
                        entity.addVelocity(d8 + d2, 0.0, d9 + d);
                    }
                } else {
                    this.addVelocity(-d2, 0.0, -d);
                    entity.addVelocity(d2 / 4.0, 0.0, d / 4.0);
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entity) {
        return entity.canBePushed() ? entity.getEntityBoundingBox() : null;
    }

    public void setDisplayTileOffset(int n) {
        this.getDataWatcher().updateObject(21, n);
        this.setHasDisplayTile(true);
    }

    @Override
    public void performHurtAnimation() {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0f);
    }

    public void killMinecart(DamageSource damageSource) {
        this.setDead();
        if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemStack = new ItemStack(Items.minecart, 1);
            if (this.entityName != null) {
                itemStack.setStackDisplayName(this.entityName);
            }
            this.entityDropItem(itemStack, 0.0f);
        }
    }

    public static EntityMinecart func_180458_a(World world, double d, double d2, double d3, EnumMinecartType enumMinecartType) {
        switch (enumMinecartType) {
            case CHEST: {
                return new EntityMinecartChest(world, d, d2, d3);
            }
            case FURNACE: {
                return new EntityMinecartFurnace(world, d, d2, d3);
            }
            case TNT: {
                return new EntityMinecartTNT(world, d, d2, d3);
            }
            case SPAWNER: {
                return new EntityMinecartMobSpawner(world, d, d2, d3);
            }
            case HOPPER: {
                return new EntityMinecartHopper(world, d, d2, d3);
            }
            case COMMAND_BLOCK: {
                return new EntityMinecartCommandBlock(world, d, d2, d3);
            }
        }
        return new EntityMinecartEmpty(world, d, d2, d3);
    }

    public int getRollingAmplitude() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public EntityMinecart(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.7f);
    }

    @Override
    public void setPosition(double d, double d2, double d3) {
        this.posX = d;
        this.posY = d2;
        this.posZ = d3;
        float f = this.width / 2.0f;
        float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(d - (double)f, d2, d3 - (double)f, d + (double)f, d2 + (double)f2, d3 + (double)f));
    }

    public void func_174899_a(IBlockState iBlockState) {
        this.getDataWatcher().updateObject(20, Block.getStateId(iBlockState));
        this.setHasDisplayTile(true);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        if (this.hasDisplayTile()) {
            nBTTagCompound.setBoolean("CustomDisplayTile", true);
            IBlockState iBlockState = this.getDisplayTile();
            ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(iBlockState.getBlock());
            nBTTagCompound.setString("DisplayTile", resourceLocation == null ? "" : resourceLocation.toString());
            nBTTagCompound.setInteger("DisplayData", iBlockState.getBlock().getMetaFromState(iBlockState));
            nBTTagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
        if (this.entityName != null && this.entityName.length() > 0) {
            nBTTagCompound.setString("CustomName", this.entityName);
        }
    }

    public Vec3 func_70495_a(double d, double d2, double d3, double d4) {
        IBlockState iBlockState;
        int n;
        int n2;
        int n3 = MathHelper.floor_double(d);
        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(n3, (n2 = MathHelper.floor_double(d2)) - 1, n = MathHelper.floor_double(d3)))) {
            --n2;
        }
        if (BlockRailBase.isRailBlock(iBlockState = this.worldObj.getBlockState(new BlockPos(n3, n2, n)))) {
            BlockRailBase.EnumRailDirection enumRailDirection = iBlockState.getValue(((BlockRailBase)iBlockState.getBlock()).getShapeProperty());
            d2 = n2;
            if (enumRailDirection.isAscending()) {
                d2 = n2 + 1;
            }
            int[][] nArray = matrix[enumRailDirection.getMetadata()];
            double d5 = nArray[1][0] - nArray[0][0];
            double d6 = nArray[1][2] - nArray[0][2];
            double d7 = Math.sqrt(d5 * d5 + d6 * d6);
            if (nArray[0][1] != 0 && MathHelper.floor_double(d += (d5 /= d7) * d4) - n3 == nArray[0][0] && MathHelper.floor_double(d3 += (d6 /= d7) * d4) - n == nArray[0][2]) {
                d2 += (double)nArray[0][1];
            } else if (nArray[1][1] != 0 && MathHelper.floor_double(d) - n3 == nArray[1][0] && MathHelper.floor_double(d3) - n == nArray[1][2]) {
                d2 += (double)nArray[1][1];
            }
            return this.func_70489_a(d, d2, d3);
        }
        return null;
    }

    protected void applyDrag() {
        if (this.riddenByEntity != null) {
            this.motionX *= (double)0.997f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.997f;
        } else {
            this.motionX *= (double)0.96f;
            this.motionY *= 0.0;
            this.motionZ *= (double)0.96f;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        if (nBTTagCompound.getBoolean("CustomDisplayTile")) {
            int n = nBTTagCompound.getInteger("DisplayData");
            if (nBTTagCompound.hasKey("DisplayTile", 8)) {
                Block block = Block.getBlockFromName(nBTTagCompound.getString("DisplayTile"));
                if (block == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                } else {
                    this.func_174899_a(block.getStateFromMeta(n));
                }
            } else {
                Block block = Block.getBlockById(nBTTagCompound.getInteger("DisplayTile"));
                if (block == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                } else {
                    this.func_174899_a(block.getStateFromMeta(n));
                }
            }
            this.setDisplayTileOffset(nBTTagCompound.getInteger("DisplayOffset"));
        }
        if (nBTTagCompound.hasKey("CustomName", 8) && nBTTagCompound.getString("CustomName").length() > 0) {
            this.entityName = nBTTagCompound.getString("CustomName");
        }
    }

    @Override
    public void setVelocity(double d, double d2, double d3) {
        this.velocityX = this.motionX = d;
        this.velocityY = this.motionY = d2;
        this.velocityZ = this.motionZ = d3;
    }

    protected double getMaximumSpeed() {
        return 0.4;
    }

    @Override
    public IChatComponent getDisplayName() {
        if (this.hasCustomName()) {
            ChatComponentText chatComponentText = new ChatComponentText(this.entityName);
            chatComponentText.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatComponentText.getChatStyle().setInsertion(this.getUniqueID().toString());
            return chatComponentText;
        }
        ChatComponentTranslation chatComponentTranslation = new ChatComponentTranslation(this.getName(), new Object[0]);
        chatComponentTranslation.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatComponentTranslation.getChatStyle().setInsertion(this.getUniqueID().toString());
        return chatComponentTranslation;
    }

    public EntityMinecart(World world, double d, double d2, double d3) {
        this(world);
        this.setPosition(d, d2, d3);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = d;
        this.prevPosY = d2;
        this.prevPosZ = d3;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }

    public void onActivatorRailPass(int n, int n2, int n3, boolean bl) {
    }

    public abstract EnumMinecartType getMinecartType();

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void setDamage(float f) {
        this.dataWatcher.updateObject(19, Float.valueOf(f));
    }

    protected void moveDerailedMinecart() {
        double d = this.getMaximumSpeed();
        this.motionX = MathHelper.clamp_double(this.motionX, -d, d);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -d, d);
        if (this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= (double)0.95f;
            this.motionY *= (double)0.95f;
            this.motionZ *= (double)0.95f;
        }
    }

    public void setRollingAmplitude(int n) {
        this.dataWatcher.updateObject(17, n);
    }

    public IBlockState getDisplayTile() {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTile() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
    }

    public float getDamage() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public Vec3 func_70489_a(double d, double d2, double d3) {
        IBlockState iBlockState;
        int n;
        int n2;
        int n3 = MathHelper.floor_double(d);
        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(n3, (n2 = MathHelper.floor_double(d2)) - 1, n = MathHelper.floor_double(d3)))) {
            --n2;
        }
        if (BlockRailBase.isRailBlock(iBlockState = this.worldObj.getBlockState(new BlockPos(n3, n2, n)))) {
            BlockRailBase.EnumRailDirection enumRailDirection = iBlockState.getValue(((BlockRailBase)iBlockState.getBlock()).getShapeProperty());
            int[][] nArray = matrix[enumRailDirection.getMetadata()];
            double d4 = 0.0;
            double d5 = (double)n3 + 0.5 + (double)nArray[0][0] * 0.5;
            double d6 = (double)n2 + 0.0625 + (double)nArray[0][1] * 0.5;
            double d7 = (double)n + 0.5 + (double)nArray[0][2] * 0.5;
            double d8 = (double)n3 + 0.5 + (double)nArray[1][0] * 0.5;
            double d9 = (double)n2 + 0.0625 + (double)nArray[1][1] * 0.5;
            double d10 = (double)n + 0.5 + (double)nArray[1][2] * 0.5;
            double d11 = d8 - d5;
            double d12 = (d9 - d6) * 2.0;
            double d13 = d10 - d7;
            if (d11 == 0.0) {
                d = (double)n3 + 0.5;
                d4 = d3 - (double)n;
            } else if (d13 == 0.0) {
                d3 = (double)n + 0.5;
                d4 = d - (double)n3;
            } else {
                double d14 = d - d5;
                double d15 = d3 - d7;
                d4 = (d14 * d11 + d15 * d13) * 2.0;
            }
            d = d5 + d11 * d4;
            d2 = d6 + d12 * d4;
            d3 = d7 + d13 * d4;
            if (d12 < 0.0) {
                d2 += 1.0;
            }
            if (d12 > 0.0) {
                d2 += 0.5;
            }
            return new Vec3(d, d2, d3);
        }
        return null;
    }

    @Override
    public boolean hasCustomName() {
        return this.entityName != null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (!this.worldObj.isRemote && !this.isDead) {
            boolean bl;
            if (this.isEntityInvulnerable(damageSource)) {
                return false;
            }
            this.setRollingDirection(-this.getRollingDirection());
            this.setRollingAmplitude(10);
            this.setBeenAttacked();
            this.setDamage(this.getDamage() + f * 10.0f);
            boolean bl2 = bl = damageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)damageSource.getEntity()).capabilities.isCreativeMode;
            if (bl || this.getDamage() > 40.0f) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(null);
                }
                if (bl && !this.hasCustomName()) {
                    this.setDead();
                } else {
                    this.killMinecart(damageSource);
                }
            }
            return true;
        }
        return true;
    }

    @Override
    public String getName() {
        return this.entityName != null ? this.entityName : super.getName();
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    public int getDefaultDisplayTileOffset() {
        return 6;
    }

    @Override
    public String getCustomNameTag() {
        return this.entityName;
    }

    public static enum EnumMinecartType {
        RIDEABLE(0, "MinecartRideable"),
        CHEST(1, "MinecartChest"),
        FURNACE(2, "MinecartFurnace"),
        TNT(3, "MinecartTNT"),
        SPAWNER(4, "MinecartSpawner"),
        HOPPER(5, "MinecartHopper"),
        COMMAND_BLOCK(6, "MinecartCommandBlock");

        private static final Map<Integer, EnumMinecartType> ID_LOOKUP;
        private final int networkID;
        private final String name;

        public String getName() {
            return this.name;
        }

        public int getNetworkID() {
            return this.networkID;
        }

        static {
            ID_LOOKUP = Maps.newHashMap();
            EnumMinecartType[] enumMinecartTypeArray = EnumMinecartType.values();
            int n = enumMinecartTypeArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumMinecartType enumMinecartType = enumMinecartTypeArray[n2];
                ID_LOOKUP.put(enumMinecartType.getNetworkID(), enumMinecartType);
                ++n2;
            }
        }

        public static EnumMinecartType byNetworkID(int n) {
            EnumMinecartType enumMinecartType = ID_LOOKUP.get(n);
            return enumMinecartType == null ? RIDEABLE : enumMinecartType;
        }

        private EnumMinecartType(int n2, String string2) {
            this.networkID = n2;
            this.name = string2;
        }
    }
}

