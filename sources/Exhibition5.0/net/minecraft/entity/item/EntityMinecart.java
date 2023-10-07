// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.IWorldNameable;
import net.minecraft.entity.Entity;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
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
    
    public EntityMinecart(final World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.7f);
    }
    
    public static EntityMinecart func_180458_a(final World worldIn, final double p_180458_1_, final double p_180458_3_, final double p_180458_5_, final EnumMinecartType p_180458_7_) {
        switch (SwitchEnumMinecartType.field_180037_a[p_180458_7_.ordinal()]) {
            case 1: {
                return new EntityMinecartChest(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case 2: {
                return new EntityMinecartFurnace(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case 3: {
                return new EntityMinecartTNT(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case 4: {
                return new EntityMinecartMobSpawner(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case 5: {
                return new EntityMinecartHopper(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            case 6: {
                return new EntityMinecartCommandBlock(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
            default: {
                return new EntityMinecartEmpty(worldIn, p_180458_1_, p_180458_3_, p_180458_5_);
            }
        }
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
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
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
    
    public EntityMinecart(final World worldIn, final double p_i1713_2_, final double p_i1713_4_, final double p_i1713_6_) {
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
        return this.height * 0.5 - 0.20000000298023224;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.worldObj.isRemote || this.isDead) {
            return true;
        }
        if (this.func_180431_b(source)) {
            return false;
        }
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setBeenAttacked();
        this.setDamage(this.getDamage() + amount * 10.0f);
        final boolean var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
        if (var3 || this.getDamage() > 40.0f) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(null);
            }
            if (var3 && !this.hasCustomName()) {
                this.setDead();
            }
            else {
                this.killMinecart(source);
            }
        }
        return true;
    }
    
    public void killMinecart(final DamageSource p_94095_1_) {
        this.setDead();
        final ItemStack var2 = new ItemStack(Items.minecart, 1);
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
            final MinecraftServer var1 = ((WorldServer)this.worldObj).func_73046_m();
            final int var2 = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (var1.getAllowNether()) {
                    if (this.ridingEntity == null && this.portalCounter++ >= var2) {
                        this.portalCounter = var2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte var3;
                        if (this.worldObj.provider.getDimensionId() == -1) {
                            var3 = 0;
                        }
                        else {
                            var3 = -1;
                        }
                        this.travelToDimension(var3);
                    }
                    this.inPortal = false;
                }
            }
            else {
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
                final double var4 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
                final double var5 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
                final double var6 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
                final double var7 = MathHelper.wrapAngleTo180_double(this.minecartYaw - this.rotationYaw);
                this.rotationYaw += (float)(var7 / this.turnProgress);
                this.rotationPitch += (float)((this.minecartPitch - this.rotationPitch) / this.turnProgress);
                --this.turnProgress;
                this.setPosition(var4, var5, var6);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033;
            final int var8 = MathHelper.floor_double(this.posX);
            int var2 = MathHelper.floor_double(this.posY);
            final int var9 = MathHelper.floor_double(this.posZ);
            if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var8, var2 - 1, var9))) {
                --var2;
            }
            final BlockPos var10 = new BlockPos(var8, var2, var9);
            final IBlockState var11 = this.worldObj.getBlockState(var10);
            if (BlockRailBase.func_176563_d(var11)) {
                this.func_180460_a(var10, var11);
                if (var11.getBlock() == Blocks.activator_rail) {
                    this.onActivatorRailPass(var8, var2, var9, (boolean)var11.getValue(BlockRailPowered.field_176569_M));
                }
            }
            else {
                this.func_180459_n();
            }
            this.doBlockCollisions();
            this.rotationPitch = 0.0f;
            final double var12 = this.prevPosX - this.posX;
            final double var13 = this.prevPosZ - this.posZ;
            if (var12 * var12 + var13 * var13 > 0.001) {
                this.rotationYaw = (float)(Math.atan2(var13, var12) * 180.0 / 3.141592653589793);
                if (this.isInReverse) {
                    this.rotationYaw += 180.0f;
                }
            }
            final double var14 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);
            if (var14 < -170.0 || var14 >= 170.0) {
                this.rotationYaw += 180.0f;
                this.isInReverse = !this.isInReverse;
            }
            this.setRotation(this.rotationYaw, this.rotationPitch);
            for (final Entity var16 : this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224))) {
                if (var16 != this.riddenByEntity && var16.canBePushed() && var16 instanceof EntityMinecart) {
                    var16.applyEntityCollision(this);
                }
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
    
    public void onActivatorRailPass(final int p_96095_1_, final int p_96095_2_, final int p_96095_3_, final boolean p_96095_4_) {
    }
    
    protected void func_180459_n() {
        final double var1 = this.func_174898_m();
        this.motionX = MathHelper.clamp_double(this.motionX, -var1, var1);
        this.motionZ = MathHelper.clamp_double(this.motionZ, -var1, var1);
        if (this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= 0.949999988079071;
            this.motionY *= 0.949999988079071;
            this.motionZ *= 0.949999988079071;
        }
    }
    
    protected void func_180460_a(final BlockPos p_180460_1_, final IBlockState p_180460_2_) {
        this.fallDistance = 0.0f;
        final Vec3 var3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = p_180460_1_.getY();
        boolean var4 = false;
        boolean var5 = false;
        final BlockRailBase var6 = (BlockRailBase)p_180460_2_.getBlock();
        if (var6 == Blocks.golden_rail) {
            var4 = (boolean)p_180460_2_.getValue(BlockRailPowered.field_176569_M);
            var5 = !var4;
        }
        final double var7 = 0.0078125;
        final BlockRailBase.EnumRailDirection var8 = (BlockRailBase.EnumRailDirection)p_180460_2_.getValue(var6.func_176560_l());
        switch (SwitchEnumMinecartType.field_180036_b[var8.ordinal()]) {
            case 1: {
                this.motionX -= 0.0078125;
                ++this.posY;
                break;
            }
            case 2: {
                this.motionX += 0.0078125;
                ++this.posY;
                break;
            }
            case 3: {
                this.motionZ += 0.0078125;
                ++this.posY;
                break;
            }
            case 4: {
                this.motionZ -= 0.0078125;
                ++this.posY;
                break;
            }
        }
        final int[][] var9 = EntityMinecart.matrix[var8.func_177015_a()];
        double var10 = var9[1][0] - var9[0][0];
        double var11 = var9[1][2] - var9[0][2];
        final double var12 = Math.sqrt(var10 * var10 + var11 * var11);
        final double var13 = this.motionX * var10 + this.motionZ * var11;
        if (var13 < 0.0) {
            var10 = -var10;
            var11 = -var11;
        }
        double var14 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (var14 > 2.0) {
            var14 = 2.0;
        }
        this.motionX = var14 * var10 / var12;
        this.motionZ = var14 * var11 / var12;
        if (this.riddenByEntity instanceof EntityLivingBase) {
            final double var15 = ((EntityLivingBase)this.riddenByEntity).moveForward;
            if (var15 > 0.0) {
                final double var16 = -Math.sin(this.riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
                final double var17 = Math.cos(this.riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
                final double var18 = this.motionX * this.motionX + this.motionZ * this.motionZ;
                if (var18 < 0.01) {
                    this.motionX += var16 * 0.1;
                    this.motionZ += var17 * 0.1;
                    var5 = false;
                }
            }
        }
        if (var5) {
            final double var15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var15 < 0.03) {
                this.motionX *= 0.0;
                this.motionY *= 0.0;
                this.motionZ *= 0.0;
            }
            else {
                this.motionX *= 0.5;
                this.motionY *= 0.0;
                this.motionZ *= 0.5;
            }
        }
        double var15 = 0.0;
        final double var16 = p_180460_1_.getX() + 0.5 + var9[0][0] * 0.5;
        final double var17 = p_180460_1_.getZ() + 0.5 + var9[0][2] * 0.5;
        final double var18 = p_180460_1_.getX() + 0.5 + var9[1][0] * 0.5;
        final double var19 = p_180460_1_.getZ() + 0.5 + var9[1][2] * 0.5;
        var10 = var18 - var16;
        var11 = var19 - var17;
        if (var10 == 0.0) {
            this.posX = p_180460_1_.getX() + 0.5;
            var15 = this.posZ - p_180460_1_.getZ();
        }
        else if (var11 == 0.0) {
            this.posZ = p_180460_1_.getZ() + 0.5;
            var15 = this.posX - p_180460_1_.getX();
        }
        else {
            final double var20 = this.posX - var16;
            final double var21 = this.posZ - var17;
            var15 = (var20 * var10 + var21 * var11) * 2.0;
        }
        this.posX = var16 + var10 * var15;
        this.posZ = var17 + var11 * var15;
        this.setPosition(this.posX, this.posY, this.posZ);
        double var20 = this.motionX;
        double var21 = this.motionZ;
        if (this.riddenByEntity != null) {
            var20 *= 0.75;
            var21 *= 0.75;
        }
        final double var22 = this.func_174898_m();
        var20 = MathHelper.clamp_double(var20, -var22, var22);
        var21 = MathHelper.clamp_double(var21, -var22, var22);
        this.moveEntity(var20, 0.0, var21);
        if (var9[0][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var9[0][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var9[0][2]) {
            this.setPosition(this.posX, this.posY + var9[0][1], this.posZ);
        }
        else if (var9[1][1] != 0 && MathHelper.floor_double(this.posX) - p_180460_1_.getX() == var9[1][0] && MathHelper.floor_double(this.posZ) - p_180460_1_.getZ() == var9[1][2]) {
            this.setPosition(this.posX, this.posY + var9[1][1], this.posZ);
        }
        this.applyDrag();
        final Vec3 var23 = this.func_70489_a(this.posX, this.posY, this.posZ);
        if (var23 != null && var3 != null) {
            final double var24 = (var3.yCoord - var23.yCoord) * 0.05;
            var14 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var14 > 0.0) {
                this.motionX = this.motionX / var14 * (var14 + var24);
                this.motionZ = this.motionZ / var14 * (var14 + var24);
            }
            this.setPosition(this.posX, var23.yCoord, this.posZ);
        }
        final int var25 = MathHelper.floor_double(this.posX);
        final int var26 = MathHelper.floor_double(this.posZ);
        if (var25 != p_180460_1_.getX() || var26 != p_180460_1_.getZ()) {
            var14 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = var14 * (var25 - p_180460_1_.getX());
            this.motionZ = var14 * (var26 - p_180460_1_.getZ());
        }
        if (var4) {
            final double var27 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var27 > 0.01) {
                final double var28 = 0.06;
                this.motionX += this.motionX / var27 * var28;
                this.motionZ += this.motionZ / var27 * var28;
            }
            else if (var8 == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.worldObj.getBlockState(p_180460_1_.offsetWest()).getBlock().isNormalCube()) {
                    this.motionX = 0.02;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.offsetEast()).getBlock().isNormalCube()) {
                    this.motionX = -0.02;
                }
            }
            else if (var8 == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.worldObj.getBlockState(p_180460_1_.offsetNorth()).getBlock().isNormalCube()) {
                    this.motionZ = 0.02;
                }
                else if (this.worldObj.getBlockState(p_180460_1_.offsetSouth()).getBlock().isNormalCube()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }
    
    protected void applyDrag() {
        if (this.riddenByEntity != null) {
            this.motionX *= 0.996999979019165;
            this.motionY *= 0.0;
            this.motionZ *= 0.996999979019165;
        }
        else {
            this.motionX *= 0.9599999785423279;
            this.motionY *= 0.0;
            this.motionZ *= 0.9599999785423279;
        }
    }
    
    @Override
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float var7 = this.width / 2.0f;
        final float var8 = this.height;
        this.func_174826_a(new AxisAlignedBB(x - var7, y, z - var7, x + var7, y + var8, z + var7));
    }
    
    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, final double p_70495_7_) {
        final int var9 = MathHelper.floor_double(p_70495_1_);
        int var10 = MathHelper.floor_double(p_70495_3_);
        final int var11 = MathHelper.floor_double(p_70495_5_);
        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var9, var10 - 1, var11))) {
            --var10;
        }
        final IBlockState var12 = this.worldObj.getBlockState(new BlockPos(var9, var10, var11));
        if (BlockRailBase.func_176563_d(var12)) {
            final BlockRailBase.EnumRailDirection var13 = (BlockRailBase.EnumRailDirection)var12.getValue(((BlockRailBase)var12.getBlock()).func_176560_l());
            p_70495_3_ = var10;
            if (var13.func_177018_c()) {
                p_70495_3_ = var10 + 1;
            }
            final int[][] var14 = EntityMinecart.matrix[var13.func_177015_a()];
            double var15 = var14[1][0] - var14[0][0];
            double var16 = var14[1][2] - var14[0][2];
            final double var17 = Math.sqrt(var15 * var15 + var16 * var16);
            var15 /= var17;
            var16 /= var17;
            p_70495_1_ += var15 * p_70495_7_;
            p_70495_5_ += var16 * p_70495_7_;
            if (var14[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - var9 == var14[0][0] && MathHelper.floor_double(p_70495_5_) - var11 == var14[0][2]) {
                p_70495_3_ += var14[0][1];
            }
            else if (var14[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - var9 == var14[1][0] && MathHelper.floor_double(p_70495_5_) - var11 == var14[1][2]) {
                p_70495_3_ += var14[1][1];
            }
            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
        return null;
    }
    
    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
        final int var7 = MathHelper.floor_double(p_70489_1_);
        int var8 = MathHelper.floor_double(p_70489_3_);
        final int var9 = MathHelper.floor_double(p_70489_5_);
        if (BlockRailBase.func_176562_d(this.worldObj, new BlockPos(var7, var8 - 1, var9))) {
            --var8;
        }
        final IBlockState var10 = this.worldObj.getBlockState(new BlockPos(var7, var8, var9));
        if (BlockRailBase.func_176563_d(var10)) {
            final BlockRailBase.EnumRailDirection var11 = (BlockRailBase.EnumRailDirection)var10.getValue(((BlockRailBase)var10.getBlock()).func_176560_l());
            final int[][] var12 = EntityMinecart.matrix[var11.func_177015_a()];
            double var13 = 0.0;
            final double var14 = var7 + 0.5 + var12[0][0] * 0.5;
            final double var15 = var8 + 0.0625 + var12[0][1] * 0.5;
            final double var16 = var9 + 0.5 + var12[0][2] * 0.5;
            final double var17 = var7 + 0.5 + var12[1][0] * 0.5;
            final double var18 = var8 + 0.0625 + var12[1][1] * 0.5;
            final double var19 = var9 + 0.5 + var12[1][2] * 0.5;
            final double var20 = var17 - var14;
            final double var21 = (var18 - var15) * 2.0;
            final double var22 = var19 - var16;
            if (var20 == 0.0) {
                p_70489_1_ = var7 + 0.5;
                var13 = p_70489_5_ - var9;
            }
            else if (var22 == 0.0) {
                p_70489_5_ = var9 + 0.5;
                var13 = p_70489_1_ - var7;
            }
            else {
                final double var23 = p_70489_1_ - var14;
                final double var24 = p_70489_5_ - var16;
                var13 = (var23 * var20 + var24 * var22) * 2.0;
            }
            p_70489_1_ = var14 + var20 * var13;
            p_70489_3_ = var15 + var21 * var13;
            p_70489_5_ = var16 + var22 * var13;
            if (var21 < 0.0) {
                ++p_70489_3_;
            }
            if (var21 > 0.0) {
                p_70489_3_ += 0.5;
            }
            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        return null;
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        if (tagCompund.getBoolean("CustomDisplayTile")) {
            final int var2 = tagCompund.getInteger("DisplayData");
            if (tagCompund.hasKey("DisplayTile", 8)) {
                final Block var3 = Block.getBlockFromName(tagCompund.getString("DisplayTile"));
                if (var3 == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else {
                    this.func_174899_a(var3.getStateFromMeta(var2));
                }
            }
            else {
                final Block var3 = Block.getBlockById(tagCompund.getInteger("DisplayTile"));
                if (var3 == null) {
                    this.func_174899_a(Blocks.air.getDefaultState());
                }
                else {
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
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        if (this.hasDisplayTile()) {
            tagCompound.setBoolean("CustomDisplayTile", true);
            final IBlockState var2 = this.func_174897_t();
            final ResourceLocation var3 = (ResourceLocation)Block.blockRegistry.getNameForObject(var2.getBlock());
            tagCompound.setString("DisplayTile", (var3 == null) ? "" : var3.toString());
            tagCompound.setInteger("DisplayData", var2.getBlock().getMetaFromState(var2));
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
        if (this.entityName != null && this.entityName.length() > 0) {
            tagCompound.setString("CustomName", this.entityName);
        }
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        if (!this.worldObj.isRemote && !entityIn.noClip && !this.noClip && entityIn != this.riddenByEntity) {
            if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && this.func_180456_s() == EnumMinecartType.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01 && this.riddenByEntity == null && entityIn.ridingEntity == null) {
                entityIn.mountEntity(this);
            }
            double var2 = entityIn.posX - this.posX;
            double var3 = entityIn.posZ - this.posZ;
            double var4 = var2 * var2 + var3 * var3;
            if (var4 >= 9.999999747378752E-5) {
                var4 = MathHelper.sqrt_double(var4);
                var2 /= var4;
                var3 /= var4;
                double var5 = 1.0 / var4;
                if (var5 > 1.0) {
                    var5 = 1.0;
                }
                var2 *= var5;
                var3 *= var5;
                var2 *= 0.10000000149011612;
                var3 *= 0.10000000149011612;
                var2 *= 1.0f - this.entityCollisionReduction;
                var3 *= 1.0f - this.entityCollisionReduction;
                var2 *= 0.5;
                var3 *= 0.5;
                if (entityIn instanceof EntityMinecart) {
                    final double var6 = entityIn.posX - this.posX;
                    final double var7 = entityIn.posZ - this.posZ;
                    final Vec3 var8 = new Vec3(var6, 0.0, var7).normalize();
                    final Vec3 var9 = new Vec3(MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f), 0.0, MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f)).normalize();
                    final double var10 = Math.abs(var8.dotProduct(var9));
                    if (var10 < 0.800000011920929) {
                        return;
                    }
                    double var11 = entityIn.motionX + this.motionX;
                    double var12 = entityIn.motionZ + this.motionZ;
                    if (((EntityMinecart)entityIn).func_180456_s() == EnumMinecartType.FURNACE && this.func_180456_s() != EnumMinecartType.FURNACE) {
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(entityIn.motionX - var2, 0.0, entityIn.motionZ - var3);
                        entityIn.motionX *= 0.949999988079071;
                        entityIn.motionZ *= 0.949999988079071;
                    }
                    else if (((EntityMinecart)entityIn).func_180456_s() != EnumMinecartType.FURNACE && this.func_180456_s() == EnumMinecartType.FURNACE) {
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(this.motionX + var2, 0.0, this.motionZ + var3);
                        this.motionX *= 0.949999988079071;
                        this.motionZ *= 0.949999988079071;
                    }
                    else {
                        var11 /= 2.0;
                        var12 /= 2.0;
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(var11 - var2, 0.0, var12 - var3);
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(var11 + var2, 0.0, var12 + var3);
                    }
                }
                else {
                    this.addVelocity(-var2, 0.0, -var3);
                    entityIn.addVelocity(var2 / 4.0, 0.0, var3 / 4.0);
                }
            }
        }
    }
    
    @Override
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
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
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.velocityX = x;
        this.motionY = y;
        this.velocityY = y;
        this.motionZ = z;
        this.velocityZ = z;
    }
    
    public void setDamage(final float p_70492_1_) {
        this.dataWatcher.updateObject(19, p_70492_1_);
    }
    
    public float getDamage() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }
    
    public void setRollingAmplitude(final int p_70497_1_) {
        this.dataWatcher.updateObject(17, p_70497_1_);
    }
    
    public int getRollingAmplitude() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }
    
    public void setRollingDirection(final int p_70494_1_) {
        this.dataWatcher.updateObject(18, p_70494_1_);
    }
    
    public int getRollingDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public abstract EnumMinecartType func_180456_s();
    
    public IBlockState func_174897_t() {
        return this.hasDisplayTile() ? Block.getStateById(this.getDataWatcher().getWatchableObjectInt(20)) : this.func_180457_u();
    }
    
    public IBlockState func_180457_u() {
        return Blocks.air.getDefaultState();
    }
    
    public int getDisplayTileOffset() {
        return this.hasDisplayTile() ? this.getDataWatcher().getWatchableObjectInt(21) : this.getDefaultDisplayTileOffset();
    }
    
    public int getDefaultDisplayTileOffset() {
        return 6;
    }
    
    public void func_174899_a(final IBlockState p_174899_1_) {
        this.getDataWatcher().updateObject(20, Block.getStateId(p_174899_1_));
        this.setHasDisplayTile(true);
    }
    
    public void setDisplayTileOffset(final int p_94086_1_) {
        this.getDataWatcher().updateObject(21, p_94086_1_);
        this.setHasDisplayTile(true);
    }
    
    public boolean hasDisplayTile() {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }
    
    public void setHasDisplayTile(final boolean p_94096_1_) {
        this.getDataWatcher().updateObject(22, (byte)(byte)(p_94096_1_ ? 1 : 0));
    }
    
    @Override
    public void setCustomNameTag(final String p_96094_1_) {
        this.entityName = p_96094_1_;
    }
    
    @Override
    public String getName() {
        return (this.entityName != null) ? this.entityName : super.getName();
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
            final ChatComponentText var2 = new ChatComponentText(this.entityName);
            var2.getChatStyle().setChatHoverEvent(this.func_174823_aP());
            var2.getChatStyle().setInsertion(this.getUniqueID().toString());
            return var2;
        }
        final ChatComponentTranslation var3 = new ChatComponentTranslation(this.getName(), new Object[0]);
        var3.getChatStyle().setChatHoverEvent(this.func_174823_aP());
        var3.getChatStyle().setInsertion(this.getUniqueID().toString());
        return var3;
    }
    
    static {
        matrix = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
    }
    
    public enum EnumMinecartType
    {
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
        
        private EnumMinecartType(final String p_i45847_1_, final int p_i45847_2_, final int p_i45847_3_, final String p_i45847_4_) {
            this.field_180052_i = p_i45847_3_;
            this.field_180049_j = p_i45847_4_;
        }
        
        public int func_180039_a() {
            return this.field_180052_i;
        }
        
        public String func_180040_b() {
            return this.field_180049_j;
        }
        
        public static EnumMinecartType func_180038_a(final int p_180038_0_) {
            final EnumMinecartType var1 = EnumMinecartType.field_180051_h.get(p_180038_0_);
            return (var1 == null) ? EnumMinecartType.RIDEABLE : var1;
        }
        
        static {
            field_180051_h = Maps.newHashMap();
            $VALUES = new EnumMinecartType[] { EnumMinecartType.RIDEABLE, EnumMinecartType.CHEST, EnumMinecartType.FURNACE, EnumMinecartType.TNT, EnumMinecartType.SPAWNER, EnumMinecartType.HOPPER, EnumMinecartType.COMMAND_BLOCK };
            for (final EnumMinecartType var4 : values()) {
                EnumMinecartType.field_180051_h.put(var4.func_180039_a(), var4);
            }
        }
    }
    
    static final class SwitchEnumMinecartType
    {
        static final int[] field_180037_a;
        static final int[] field_180036_b;
        private static final String __OBFID = "CL_00002227";
        
        static {
            field_180036_b = new int[BlockRailBase.EnumRailDirection.values().length];
            try {
                SwitchEnumMinecartType.field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumMinecartType.field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumMinecartType.field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumMinecartType.field_180036_b[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            field_180037_a = new int[EnumMinecartType.values().length];
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.CHEST.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.FURNACE.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.TNT.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError7) {}
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.SPAWNER.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError8) {}
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.HOPPER.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError9) {}
            try {
                SwitchEnumMinecartType.field_180037_a[EnumMinecartType.COMMAND_BLOCK.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError10) {}
        }
    }
}
