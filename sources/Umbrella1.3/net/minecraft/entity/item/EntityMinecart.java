/*
 * Decompiled with CFR 0.150.
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
    private boolean isInReverse;
    private String entityName;
    private static final int[][][] matrix;
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private static final String __OBFID = "CL_00001670";

    static {
        int[][][] arrarrn = new int[10][][];
        int[][] arrarrn2 = new int[2][];
        int[] arrn = new int[3];
        arrn[2] = -1;
        arrarrn2[0] = arrn;
        int[] arrn2 = new int[3];
        arrn2[2] = 1;
        arrarrn2[1] = arrn2;
        arrarrn[0] = arrarrn2;
        int[][] arrarrn3 = new int[2][];
        int[] arrn3 = new int[3];
        arrn3[0] = -1;
        arrarrn3[0] = arrn3;
        int[] arrn4 = new int[3];
        arrn4[0] = 1;
        arrarrn3[1] = arrn4;
        arrarrn[1] = arrarrn3;
        int[][] arrarrn4 = new int[2][];
        int[] arrn5 = new int[3];
        arrn5[0] = -1;
        arrn5[1] = -1;
        arrarrn4[0] = arrn5;
        int[] arrn6 = new int[3];
        arrn6[0] = 1;
        arrarrn4[1] = arrn6;
        arrarrn[2] = arrarrn4;
        int[][] arrarrn5 = new int[2][];
        int[] arrn7 = new int[3];
        arrn7[0] = -1;
        arrarrn5[0] = arrn7;
        int[] arrn8 = new int[3];
        arrn8[0] = 1;
        arrn8[1] = -1;
        arrarrn5[1] = arrn8;
        arrarrn[3] = arrarrn5;
        int[][] arrarrn6 = new int[2][];
        int[] arrn9 = new int[3];
        arrn9[2] = -1;
        arrarrn6[0] = arrn9;
        int[] arrn10 = new int[3];
        arrn10[1] = -1;
        arrn10[2] = 1;
        arrarrn6[1] = arrn10;
        arrarrn[4] = arrarrn6;
        int[][] arrarrn7 = new int[2][];
        int[] arrn11 = new int[3];
        arrn11[1] = -1;
        arrn11[2] = -1;
        arrarrn7[0] = arrn11;
        int[] arrn12 = new int[3];
        arrn12[2] = 1;
        arrarrn7[1] = arrn12;
        arrarrn[5] = arrarrn7;
        int[][] arrarrn8 = new int[2][];
        int[] arrn13 = new int[3];
        arrn13[2] = 1;
        arrarrn8[0] = arrn13;
        int[] arrn14 = new int[3];
        arrn14[0] = 1;
        arrarrn8[1] = arrn14;
        arrarrn[6] = arrarrn8;
        int[][] arrarrn9 = new int[2][];
        int[] arrn15 = new int[3];
        arrn15[2] = 1;
        arrarrn9[0] = arrn15;
        int[] arrn16 = new int[3];
        arrn16[0] = -1;
        arrarrn9[1] = arrn16;
        arrarrn[7] = arrarrn9;
        int[][] arrarrn10 = new int[2][];
        int[] arrn17 = new int[3];
        arrn17[2] = -1;
        arrarrn10[0] = arrn17;
        int[] arrn18 = new int[3];
        arrn18[0] = -1;
        arrarrn10[1] = arrn18;
        arrarrn[8] = arrarrn10;
        int[][] arrarrn11 = new int[2][];
        int[] arrn19 = new int[3];
        arrn19[2] = -1;
        arrarrn11[0] = arrn19;
        int[] arrn20 = new int[3];
        arrn20[0] = 1;
        arrarrn11[1] = arrn20;
        arrarrn[9] = arrarrn11;
        matrix = arrarrn;
    }

    public EntityMinecart(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.7f);
    }

    public static EntityMinecart func_180458_a(World worldIn, double p_180458_1_, double p_180458_3_, double p_180458_5_, EnumMinecartType p_180458_7_) {
        switch (p_180458_7_) {
            case CHEST: {
                return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case FURNACE: {
                return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case TNT: {
                return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case SPAWNER: {
                return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case HOPPER: {
                return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case COMMAND_BLOCK: {
                return new EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
        }
        return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
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
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    public EntityMinecart(World worldIn, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_) {
        this(worldIn);
        this.setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = p_i1713_2_;
        this.prevPosY = p_i1713_4_;
        this.prevPosZ = p_i1713_6_;
    }

    @Override
    public double getMountedYOffset() {
        return (double)this.height * 0.5 - (double)0.2f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.worldObj.isRemote && !this.isDead) {
            boolean var3;
            if (this.func_180431_b(source)) {
                return false;
            }
            this.setRollingDirection(-this.getRollingDirection());
            this.setRollingAmplitude(10);
            this.setBeenAttacked();
            this.setDamage(this.getDamage() + amount * 10.0f);
            boolean bl = var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
            if (var3 || this.getDamage() > 40.0f) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(null);
                }
                if (var3 && !this.hasCustomName()) {
                    this.setDead();
                } else {
                    this.killMinecart(source);
                }
            }
            return true;
        }
        return true;
    }

    public void killMinecart(DamageSource p_94095_1_) {
        this.setDead();
        ItemStack var2 = new ItemStack(Items.minecart, 1);
        if (this.entityName != null) {
            var2.setStackDisplayName(this.entityName);
        }
        this.entityDropItem(var2, 0.0f);
    }

    @Override
    public void performHurtAnimation() {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void setDead() {
        super.setDead();
    }

    @Override
    public void onUpdate() {
        int var2;
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
            MinecraftServer var1 = ((WorldServer)this.worldObj).func_73046_m();
            var2 = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (var1.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= var2) {
                        this.portalCounter = var2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int var3 = this.worldObj.provider.getDimensionId() == -1 ? 0 : -1;
                        this.travelToDimension(var3);
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
                double var15 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double var17 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double var18 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var7 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(var15, var17, var18);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        } else {
            double var10;
            BlockPos var4;
            IBlockState var5;
            int var16;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= (double)0.04f;
            int var14 = MathHelper.floor_double(this.posX);
            if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var14, (var2 = MathHelper.floor_double(this.posY)) - 1, var16 = MathHelper.floor_double(this.posZ)))) {
                --var2;
            }
            if (BlockRailBase.func_176563_d(var5 = this.worldObj.getBlockState(var4 = new BlockPos(var14, var2, var16)))) {
                this.func_180460_a(var4, var5);
                if (var5.getBlock() == Blocks.activator_rail) {
                    this.onActivatorRailPass(var14, var2, var16, (Boolean)var5.getValue(BlockRailPowered.field_176569_M));
                }
            } else {
                this.func_180459_n();
            }
            this.doBlockCollisions();
            this.rotationPitch = 0.0f;
            double var6 = this.prevPosX - this.posX;
            double var8 = this.prevPosZ - this.posZ;
            if (var6 * var6 + var8 * var8 > 0.001) {
                this.rotationYaw = (float)(Math.atan2(var8, var6) * 180.0 / Math.PI);
                if (this.isInReverse) {
                    this.rotationYaw += 180.0f;
                }
            }
            if ((var10 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw)) < -170.0 || var10 >= 170.0) {
                this.rotationYaw += 180.0f;
                this.isInReverse = !this.isInReverse;
            }
            this.setRotation(this.rotationYaw, this.rotationPitch);
            for (Entity var13 : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.2f, 0.0, 0.2f))) {
                if (var13 == this.riddenByEntity || !var13.canBePushed() || !(var13 instanceof EntityMinecart)) continue;
                var13.applyEntityCollision(this);
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

    protected double func_174898_m() {
        return 0.4;
    }

    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {
    }

    protected void func_180459_n() {
        double var1 = this.func_174898_m();
        this.motionX = MathHelper.clamp_double(this.motionX, -var1, var1);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -var1, var1);
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

    protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_) {
        double var33;
        double var31;
        double var27;
        double var25;
        double var23;
        double var21;
        double var19;
        this.fallDistance = 0.0f;
        Vec3 var3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = p_180460_1_.getY();
        boolean var4 = false;
        boolean var5 = false;
        BlockRailBase var6 = (BlockRailBase)p_180460_2_.getBlock();
        if (var6 == Blocks.golden_rail) {
            var4 = (Boolean)p_180460_2_.getValue(BlockRailPowered.field_176569_M);
            var5 = !var4;
        }
        double var7 = 0.0078125;
        BlockRailBase.EnumRailDirection var9 = (BlockRailBase.EnumRailDirection)((Object)p_180460_2_.getValue(var6.func_176560_l()));
        switch (var9) {
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
        int[][] var10 = matrix[var9.func_177015_a()];
        double var11 = var10[1][0] - var10[0][0];
        double var13 = var10[1][2] - var10[0][2];
        double var15 = Math.sqrt(var11 * var11 + var13 * var13);
        double var17 = this.motionX * var11 + this.motionZ * var13;
        if (var17 < 0.0) {
            var11 = -var11;
            var13 = -var13;
        }
        if ((var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 2.0) {
            var19 = 2.0;
        }
        this.motionX = var19 * var11 / var15;
        this.motionZ = var19 * var13 / var15;
        if (this.riddenByEntity instanceof EntityLivingBase && (var21 = (double)((EntityLivingBase)this.riddenByEntity).moveForward) > 0.0) {
            var23 = -Math.sin(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0f);
            var25 = Math.cos(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0f);
            var27 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (var27 < 0.01) {
                this.motionX += var23 * 0.1;
                this.motionZ += var25 * 0.1;
                var5 = false;
            }
        }
        if (var5) {
            var21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var21 < 0.03) {
                this.motionX *= 0.0;
                this.motionY *= 0.0;
                this.motionZ *= 0.0;
            } else {
                this.motionX *= 0.5;
                this.motionY *= 0.0;
                this.motionZ *= 0.5;
            }
        }
        var21 = 0.0;
        var23 = (double)p_180460_1_.getX() + 0.5 + (double)var10[0][0] * 0.5;
        var25 = (double)p_180460_1_.getZ() + 0.5 + (double)var10[0][2] * 0.5;
        var27 = (double)p_180460_1_.getX() + 0.5 + (double)var10[1][0] * 0.5;
        double var29 = (double)p_180460_1_.getZ() + 0.5 + (double)var10[1][2] * 0.5;
        var11 = var27 - var23;
        var13 = var29 - var25;
        if (var11 == 0.0) {
            this.posX = (double)p_180460_1_.getX() + 0.5;
            var21 = this.posZ - (double)p_180460_1_.getZ();
        } else if (var13 == 0.0) {
            this.posZ = (double)p_180460_1_.getZ() + 0.5;
            var21 = this.posX - (double)p_180460_1_.getX();
        } else {
            var31 = this.posX - var23;
            var33 = this.posZ - var25;
            var21 = (var31 * var11 + var33 * var13) * 2.0;
        }
        this.posX = var23 + var11 * var21;
        this.posZ = var25 + var13 * var21;
        this.setPosition(this.posX, this.posY, this.posZ);
        var31 = this.motionX;
        var33 = this.motionZ;
        if (this.riddenByEntity != null) {
            var31 *= 0.75;
            var33 *= 0.75;
        }
        double var35 = this.func_174898_m();
        var31 = MathHelper.clamp_double(var31, -var35, var35);
        var33 = MathHelper.clamp_double(var33, -var35, var35);
        this.moveEntity(var31, 0.0, var33);
        if (var10[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var10[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var10[0][2]) {
            this.setPosition(this.posX, this.posY + (double)var10[0][1], this.posZ);
        } else if (var10[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var10[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var10[1][2]) {
            this.setPosition(this.posX, this.posY + (double)var10[1][1], this.posZ);
        }
        this.applyDrag();
        Vec3 var37 = this.func_70489_a(this.posX, this.posY, this.posZ);
        if (var37 != null && var3 != null) {
            double var38 = (var3.yCoord - var37.yCoord) * 0.05;
            var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var19 > 0.0) {
                this.motionX = this.motionX / var19 * (var19 + var38);
                this.motionZ = this.motionZ / var19 * (var19 + var38);
            }
            this.setPosition(this.posX, var37.yCoord, this.posZ);
        }
        int var44 = MathHelper.floor_double(this.posX);
        int var39 = MathHelper.floor_double(this.posZ);
        if (var44 != p_180460_1_.getX() || var39 != p_180460_1_.getZ()) {
            var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = var19 * (double)(var44 - p_180460_1_.getX());
            this.motionZ = var19 * (double)(var39 - p_180460_1_.getZ());
        }
        if (var4) {
            double var40 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var40 > 0.01) {
                double var42 = 0.06;
                this.motionX += this.motionX / var40 * var42;
                this.motionZ += this.motionZ / var40 * var42;
            } else if (var9 == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.worldObj.getBlockState(p_180460_1_.offsetWest()).getBlock().isNormalCube()) {
                    this.motionX = 0.02;
                } else if (this.worldObj.getBlockState(p_180460_1_.offsetEast()).getBlock().isNormalCube()) {
                    this.motionX = -0.02;
                }
            } else if (var9 == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.worldObj.getBlockState(p_180460_1_.offsetNorth()).getBlock().isNormalCube()) {
                    this.motionZ = 0.02;
                } else if (this.worldObj.getBlockState(p_180460_1_.offsetSouth()).getBlock().isNormalCube()) {
                    this.motionZ = -0.02;
                }
            }
        }
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
    public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float var7 = this.width / 2.0f;
        float var8 = this.height;
        this.func_174826_a(new AxisAlignedBB(x - (double)var7, y, z - (double)var7, x + (double)var7, y + (double)var8, z + (double)var7));
    }

    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_) {
        IBlockState var12;
        int var11;
        int var10;
        int var9 = MathHelper.floor_double(p_70495_1_);
        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var9, (var10 = MathHelper.floor_double(p_70495_3_)) - 1, var11 = MathHelper.floor_double(p_70495_5_)))) {
            --var10;
        }
        if (BlockRailBase.func_176563_d(var12 = this.worldObj.getBlockState(new BlockPos(var9, var10, var11)))) {
            BlockRailBase.EnumRailDirection var13 = (BlockRailBase.EnumRailDirection)((Object)var12.getValue(((BlockRailBase)var12.getBlock()).func_176560_l()));
            p_70495_3_ = var10;
            if (var13.func_177018_c()) {
                p_70495_3_ = var10 + 1;
            }
            int[][] var14 = matrix[var13.func_177015_a()];
            double var15 = var14[1][0] - var14[0][0];
            double var17 = var14[1][2] - var14[0][2];
            double var19 = Math.sqrt(var15 * var15 + var17 * var17);
            if (var14[0][1] != 0 && MathHelper.floor_double(p_70495_1_ += (var15 /= var19) * p_70495_7_) - var9 == var14[0][0] && MathHelper.floor_double(p_70495_5_ += (var17 /= var19) * p_70495_7_) - var11 == var14[0][2]) {
                p_70495_3_ += (double)var14[0][1];
            } else if (var14[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - var9 == var14[1][0] && MathHelper.floor_double(p_70495_5_) - var11 == var14[1][2]) {
                p_70495_3_ += (double)var14[1][1];
            }
            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        return null;
    }

    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
        IBlockState var10;
        int var9;
        int var8;
        int var7 = MathHelper.floor_double(p_70489_1_);
        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var7, (var8 = MathHelper.floor_double(p_70489_3_)) - 1, var9 = MathHelper.floor_double(p_70489_5_)))) {
            --var8;
        }
        if (BlockRailBase.func_176563_d(var10 = this.worldObj.getBlockState(new BlockPos(var7, var8, var9)))) {
            BlockRailBase.EnumRailDirection var11 = (BlockRailBase.EnumRailDirection)((Object)var10.getValue(((BlockRailBase)var10.getBlock()).func_176560_l()));
            int[][] var12 = matrix[var11.func_177015_a()];
            double var13 = 0.0;
            double var15 = (double)var7 + 0.5 + (double)var12[0][0] * 0.5;
            double var17 = (double)var8 + 0.0625 + (double)var12[0][1] * 0.5;
            double var19 = (double)var9 + 0.5 + (double)var12[0][2] * 0.5;
            double var21 = (double)var7 + 0.5 + (double)var12[1][0] * 0.5;
            double var23 = (double)var8 + 0.0625 + (double)var12[1][1] * 0.5;
            double var25 = (double)var9 + 0.5 + (double)var12[1][2] * 0.5;
            double var27 = var21 - var15;
            double var29 = (var23 - var17) * 2.0;
            double var31 = var25 - var19;
            if (var27 == 0.0) {
                p_70489_1_ = (double)var7 + 0.5;
                var13 = p_70489_5_ - (double)var9;
            } else if (var31 == 0.0) {
                p_70489_5_ = (double)var9 + 0.5;
                var13 = p_70489_1_ - (double)var7;
            } else {
                double var33 = p_70489_1_ - var15;
                double var35 = p_70489_5_ - var19;
                var13 = (var33 * var27 + var35 * var31) * 2.0;
            }
            p_70489_1_ = var15 + var27 * var13;
            p_70489_3_ = var17 + var29 * var13;
            p_70489_5_ = var19 + var31 * var13;
            if (var29 < 0.0) {
                p_70489_3_ += 1.0;
            }
            if (var29 > 0.0) {
                p_70489_3_ += 0.5;
            }
            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        return null;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        if (tagCompund.getBoolean("CustomDisplayTile")) {
            int var2 = tagCompund.getInteger("DisplayData");
            if (tagCompund.hasKey("DisplayTile", 8)) {
                Block var3 = Block.getBlockFromName(tagCompund.getString("DisplayTile"));
                if (var3 == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                } else {
                    this.func_174899_a(var3.getStateFromMeta(var2));
                }
            } else {
                Block var3 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));
                if (var3 == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                } else {
                    this.func_174899_a(var3.getStateFromMeta(var2));
                }
            }
            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }
        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0) {
            this.entityName = tagCompund.getString("CustomName");
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        if (this.hasDisplayTile()) {
            tagCompound.setBoolean("CustomDisplayTile", true);
            IBlockState var2 = this.func_174897_t();
            ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2.getBlock());
            tagCompound.setString("DisplayTile", var3 == null ? "" : var3.toString());
            tagCompound.setInteger("DisplayData", var2.getBlock().getMetaFromState(var2));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
        if (this.entityName != null && this.entityName.length() > 0) {
            tagCompound.setString("CustomName", this.entityName);
        }
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!(this.worldObj.isRemote || entityIn.noClip || this.noClip || entityIn == this.riddenByEntity)) {
            double var4;
            double var2;
            double var6;
            if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && this.func_180456_s() == EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01 && this.riddenByEntity == null && entityIn.ridingEntity == null) {
                entityIn.mountEntity(this);
            }
            if ((var6 = (var2 = entityIn.posX - this.posX) * var2 + (var4 = entityIn.posZ - this.posZ) * var4) >= (double)1.0E-4f) {
                var6 = MathHelper.sqrt_double(var6);
                var2 /= var6;
                var4 /= var6;
                double var8 = 1.0 / var6;
                if (var8 > 1.0) {
                    var8 = 1.0;
                }
                var2 *= var8;
                var4 *= var8;
                var2 *= (double)0.1f;
                var4 *= (double)0.1f;
                var2 *= (double)(1.0f - this.entityCollisionReduction);
                var4 *= (double)(1.0f - this.entityCollisionReduction);
                var2 *= 0.5;
                var4 *= 0.5;
                if (entityIn instanceof EntityMinecart) {
                    Vec3 var15;
                    double var10 = entityIn.posX - this.posX;
                    double var12 = entityIn.posZ - this.posZ;
                    Vec3 var14 = new Vec3(var10, 0.0, var12).normalize();
                    double var16 = Math.abs(var14.dotProduct(var15 = new Vec3(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f), 0.0, MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f)).normalize()));
                    if (var16 < (double)0.8f) {
                        return;
                    }
                    double var18 = entityIn.motionX + this.motionX;
                    double var20 = entityIn.motionZ + this.motionZ;
                    if (((EntityMinecart)entityIn).func_180456_s() == EnumMinecartType.FURNACE && this.func_180456_s() != EnumMinecartType.FURNACE) {
                        this.motionX *= (double)0.2f;
                        this.motionZ *= (double)0.2f;
                        this.addVelocity(entityIn.motionX - var2, 0.0, entityIn.motionZ - var4);
                        entityIn.motionX *= (double)0.95f;
                        entityIn.motionZ *= (double)0.95f;
                    } else if (((EntityMinecart)entityIn).func_180456_s() != EnumMinecartType.FURNACE && this.func_180456_s() == EnumMinecartType.FURNACE) {
                        entityIn.motionX *= (double)0.2f;
                        entityIn.motionZ *= (double)0.2f;
                        entityIn.addVelocity(this.motionX + var2, 0.0, this.motionZ + var4);
                        this.motionX *= (double)0.95f;
                        this.motionZ *= (double)0.95f;
                    } else {
                        this.motionX *= (double)0.2f;
                        this.motionZ *= (double)0.2f;
                        this.addVelocity((var18 /= 2.0) - var2, 0.0, (var20 /= 2.0) - var4);
                        entityIn.motionX *= (double)0.2f;
                        entityIn.motionZ *= (double)0.2f;
                        entityIn.addVelocity(var18 + var2, 0.0, var20 + var4);
                    }
                } else {
                    this.addVelocity(-var2, 0.0, -var4);
                    entityIn.addVelocity(var2 / 4.0, 0.0, var4 / 4.0);
                }
            }
        }
    }

    @Override
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.minecartX = p_180426_1_;
        this.minecartY = p_180426_3_;
        this.minecartZ = p_180426_5_;
        this.minecartYaw = p_180426_7_;
        this.minecartPitch = p_180426_8_;
        this.turnProgress = p_180426_9_ + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    public void setDamage(float p_70492_1_) {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    public float getDamage() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setRollingAmplitude(int p_70497_1_) {
        this.dataWatcher.updateObject(17, p_70497_1_);
    }

    public int getRollingAmplitude() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setRollingDirection(int p_70494_1_) {
        this.dataWatcher.updateObject(18, p_70494_1_);
    }

    public int getRollingDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public abstract EnumMinecartType func_180456_s();

    public IBlockState func_174897_t() {
        return !this.hasDisplayTile() ? this.func_180457_u() : Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20));
    }

    public IBlockState func_180457_u() {
        return Blocks.air.getDefaultState();
    }

    public int getDisplayTileOffset() {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    public int getDefaultDisplayTileOffset() {
        return 6;
    }

    public void func_174899_a(IBlockState p_174899_1_) {
        this.getDataWatcher().updateObject(20, Block.getStateId(p_174899_1_));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int p_94086_1_) {
        this.getDataWatcher().updateObject(21, p_94086_1_);
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile() {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean p_94096_1_) {
        this.getDataWatcher().updateObject(22, (byte)(p_94096_1_ ? 1 : 0));
    }

    @Override
    public void setCustomNameTag(String p_96094_1_) {
        this.entityName = p_96094_1_;
    }

    @Override
    public String getName() {
        return this.entityName != null ? this.entityName : super.getName();
    }

    @Override
    public boolean hasCustomName() {
        return this.entityName != null;
    }

    @Override
    public String getCustomNameTag() {
        return this.entityName;
    }

    @Override
    public IChatComponent getDisplayName() {
        if (this.hasCustomName()) {
            ChatComponentText var2 = new ChatComponentText(this.entityName);
            var2.getChatStyle().setChatHoverEvent(this.func_174823_aP());
            var2.getChatStyle().setInsertion(this.getUniqueID().toString());
            return var2;
        }
        ChatComponentTranslation var1 = new ChatComponentTranslation(this.getName(), new Object[0]);
        var1.getChatStyle().setChatHoverEvent(this.func_174823_aP());
        var1.getChatStyle().setInsertion(this.getUniqueID().toString());
        return var1;
    }

    public static enum EnumMinecartType {
        RIDEABLE("RIDEABLE", 0, 0, "MinecartRideable"),
        CHEST("CHEST", 1, 1, "MinecartChest"),
        FURNACE("FURNACE", 2, 2, "MinecartFurnace"),
        TNT("TNT", 3, 3, "MinecartTNT"),
        SPAWNER("SPAWNER", 4, 4, "MinecartSpawner"),
        HOPPER("HOPPER", 5, 5, "MinecartHopper"),
        COMMAND_BLOCK("COMMAND_BLOCK", 6, 6, "MinecartCommandBlock");

        private static final Map field_180051_h;
        private final int field_180052_i;
        private final String field_180049_j;
        private static final EnumMinecartType[] $VALUES;
        private static final String __OBFID = "CL_00002226";

        static {
            field_180051_h = Maps.newHashMap();
            $VALUES = new EnumMinecartType[]{RIDEABLE, CHEST, FURNACE, TNT, SPAWNER, HOPPER, COMMAND_BLOCK};
            for (EnumMinecartType var3 : EnumMinecartType.values()) {
                field_180051_h.put(var3.func_180039_a(), var3);
            }
        }

        private EnumMinecartType(String p_i45847_1_, int p_i45847_2_, int p_i45847_3_, String p_i45847_4_) {
            this.field_180052_i = p_i45847_3_;
            this.field_180049_j = p_i45847_4_;
        }

        public int func_180039_a() {
            return this.field_180052_i;
        }

        public String func_180040_b() {
            return this.field_180049_j;
        }

        public static EnumMinecartType func_180038_a(int p_180038_0_) {
            EnumMinecartType var1 = (EnumMinecartType)((Object)field_180051_h.get(p_180038_0_));
            return var1 == null ? RIDEABLE : var1;
        }
    }
}

