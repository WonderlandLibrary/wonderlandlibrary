// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.EntitySelectors;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockRailBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.IWorldNameable;
import net.minecraft.entity.Entity;

public abstract class EntityMinecart extends Entity implements IWorldNameable
{
    private static final DataParameter<Integer> ROLLING_AMPLITUDE;
    private static final DataParameter<Integer> ROLLING_DIRECTION;
    private static final DataParameter<Float> DAMAGE;
    private static final DataParameter<Integer> DISPLAY_TILE;
    private static final DataParameter<Integer> DISPLAY_TILE_OFFSET;
    private static final DataParameter<Boolean> SHOW_BLOCK;
    private boolean isInReverse;
    private static final int[][][] MATRIX;
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    
    public EntityMinecart(final World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.7f);
    }
    
    public static EntityMinecart create(final World worldIn, final double x, final double y, final double z, final Type typeIn) {
        switch (typeIn) {
            case CHEST: {
                return new EntityMinecartChest(worldIn, x, y, z);
            }
            case FURNACE: {
                return new EntityMinecartFurnace(worldIn, x, y, z);
            }
            case TNT: {
                return new EntityMinecartTNT(worldIn, x, y, z);
            }
            case SPAWNER: {
                return new EntityMinecartMobSpawner(worldIn, x, y, z);
            }
            case HOPPER: {
                return new EntityMinecartHopper(worldIn, x, y, z);
            }
            case COMMAND_BLOCK: {
                return new EntityMinecartCommandBlock(worldIn, x, y, z);
            }
            default: {
                return new EntityMinecartEmpty(worldIn, x, y, z);
            }
        }
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityMinecart.ROLLING_AMPLITUDE, 0);
        this.dataManager.register(EntityMinecart.ROLLING_DIRECTION, 1);
        this.dataManager.register(EntityMinecart.DAMAGE, 0.0f);
        this.dataManager.register(EntityMinecart.DISPLAY_TILE, 0);
        this.dataManager.register(EntityMinecart.DISPLAY_TILE_OFFSET, 6);
        this.dataManager.register(EntityMinecart.SHOW_BLOCK, false);
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }
    
    @Override
    public boolean canBePushed() {
        return true;
    }
    
    public EntityMinecart(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }
    
    @Override
    public double getMountedYOffset() {
        return 0.0;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.world.isRemote || this.isDead) {
            return true;
        }
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.markVelocityChanged();
        this.setDamage(this.getDamage() + amount * 10.0f);
        final boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).capabilities.isCreativeMode;
        if (flag || this.getDamage() > 40.0f) {
            this.removePassengers();
            if (flag && !this.hasCustomName()) {
                this.setDead();
            }
            else {
                this.killMinecart(source);
            }
        }
        return true;
    }
    
    public void killMinecart(final DamageSource source) {
        this.setDead();
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            final ItemStack itemstack = new ItemStack(Items.MINECART, 1);
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0f);
        }
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
    public EnumFacing getAdjustedHorizontalFacing() {
        return this.isInReverse ? this.getHorizontalFacing().getOpposite().rotateY() : this.getHorizontalFacing().rotateY();
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
            this.outOfWorld();
        }
        if (!this.world.isRemote && this.world instanceof WorldServer) {
            this.world.profiler.startSection("portal");
            final MinecraftServer minecraftserver = this.world.getMinecraftServer();
            final int i = this.getMaxInPortalTime();
            if (this.inPortal) {
                if (minecraftserver.getAllowNether()) {
                    if (!this.isRiding() && this.portalCounter++ >= i) {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        int j;
                        if (this.world.provider.getDimensionType().getId() == -1) {
                            j = 0;
                        }
                        else {
                            j = -1;
                        }
                        this.changeDimension(j);
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
            this.world.profiler.endSection();
        }
        if (this.world.isRemote) {
            if (this.turnProgress > 0) {
                final double d4 = this.posX + (this.minecartX - this.posX) / this.turnProgress;
                final double d5 = this.posY + (this.minecartY - this.posY) / this.turnProgress;
                final double d6 = this.posZ + (this.minecartZ - this.posZ) / this.turnProgress;
                final double d7 = MathHelper.wrapDegrees(this.minecartYaw - this.rotationYaw);
                this.rotationYaw += (float)(d7 / this.turnProgress);
                this.rotationPitch += (float)((this.minecartPitch - this.rotationPitch) / this.turnProgress);
                --this.turnProgress;
                this.setPosition(d4, d5, d6);
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
            if (!this.hasNoGravity()) {
                this.motionY -= 0.03999999910593033;
            }
            final int k = MathHelper.floor(this.posX);
            int l = MathHelper.floor(this.posY);
            final int i2 = MathHelper.floor(this.posZ);
            if (BlockRailBase.isRailBlock(this.world, new BlockPos(k, l - 1, i2))) {
                --l;
            }
            final BlockPos blockpos = new BlockPos(k, l, i2);
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            if (BlockRailBase.isRailBlock(iblockstate)) {
                this.moveAlongTrack(blockpos, iblockstate);
                if (iblockstate.getBlock() == Blocks.ACTIVATOR_RAIL) {
                    this.onActivatorRailPass(k, l, i2, iblockstate.getValue((IProperty<Boolean>)BlockRailPowered.POWERED));
                }
            }
            else {
                this.moveDerailedMinecart();
            }
            this.doBlockCollisions();
            this.rotationPitch = 0.0f;
            final double d8 = this.prevPosX - this.posX;
            final double d9 = this.prevPosZ - this.posZ;
            if (d8 * d8 + d9 * d9 > 0.001) {
                this.rotationYaw = (float)(MathHelper.atan2(d9, d8) * 180.0 / 3.141592653589793);
                if (this.isInReverse) {
                    this.rotationYaw += 180.0f;
                }
            }
            final double d10 = MathHelper.wrapDegrees(this.rotationYaw - this.prevRotationYaw);
            if (d10 < -170.0 || d10 >= 170.0) {
                this.rotationYaw += 180.0f;
                this.isInReverse = !this.isInReverse;
            }
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (this.getType() == Type.RIDEABLE && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01) {
                final List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224, 0.0, 0.20000000298023224), EntitySelectors.getTeamCollisionPredicate(this));
                if (!list.isEmpty()) {
                    for (int j2 = 0; j2 < list.size(); ++j2) {
                        final Entity entity1 = list.get(j2);
                        if (!(entity1 instanceof EntityPlayer) && !(entity1 instanceof EntityIronGolem) && !(entity1 instanceof EntityMinecart) && !this.isBeingRidden() && !entity1.isRiding()) {
                            entity1.startRiding(this);
                        }
                        else {
                            entity1.applyEntityCollision(this);
                        }
                    }
                }
            }
            else {
                for (final Entity entity2 : this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(0.20000000298023224, 0.0, 0.20000000298023224))) {
                    if (!this.isPassenger(entity2) && entity2.canBePushed() && entity2 instanceof EntityMinecart) {
                        entity2.applyEntityCollision(this);
                    }
                }
            }
            this.handleWaterMovement();
        }
    }
    
    protected double getMaximumSpeed() {
        return 0.4;
    }
    
    public void onActivatorRailPass(final int x, final int y, final int z, final boolean receivingPower) {
    }
    
    protected void moveDerailedMinecart() {
        final double d0 = this.getMaximumSpeed();
        this.motionX = MathHelper.clamp(this.motionX, -d0, d0);
        this.motionZ = MathHelper.clamp(this.motionZ, -d0, d0);
        if (this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (!this.onGround) {
            this.motionX *= 0.949999988079071;
            this.motionY *= 0.949999988079071;
            this.motionZ *= 0.949999988079071;
        }
    }
    
    protected void moveAlongTrack(final BlockPos pos, final IBlockState state) {
        this.fallDistance = 0.0f;
        final Vec3d vec3d = this.getPos(this.posX, this.posY, this.posZ);
        this.posY = pos.getY();
        boolean flag = false;
        boolean flag2 = false;
        final BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();
        if (blockrailbase == Blocks.GOLDEN_RAIL) {
            flag = state.getValue((IProperty<Boolean>)BlockRailPowered.POWERED);
            flag2 = !flag;
        }
        final double d0 = 0.0078125;
        final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = state.getValue(blockrailbase.getShapeProperty());
        switch (blockrailbase$enumraildirection) {
            case ASCENDING_EAST: {
                this.motionX -= 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_WEST: {
                this.motionX += 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_NORTH: {
                this.motionZ += 0.0078125;
                ++this.posY;
                break;
            }
            case ASCENDING_SOUTH: {
                this.motionZ -= 0.0078125;
                ++this.posY;
                break;
            }
        }
        final int[][] aint = EntityMinecart.MATRIX[blockrailbase$enumraildirection.getMetadata()];
        double d2 = aint[1][0] - aint[0][0];
        double d3 = aint[1][2] - aint[0][2];
        final double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        final double d5 = this.motionX * d2 + this.motionZ * d3;
        if (d5 < 0.0) {
            d2 = -d2;
            d3 = -d3;
        }
        double d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (d6 > 2.0) {
            d6 = 2.0;
        }
        this.motionX = d6 * d2 / d4;
        this.motionZ = d6 * d3 / d4;
        final Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (entity instanceof EntityLivingBase) {
            final double d7 = ((EntityLivingBase)entity).moveForward;
            if (d7 > 0.0) {
                final double d8 = -Math.sin(entity.rotationYaw * 0.017453292f);
                final double d9 = Math.cos(entity.rotationYaw * 0.017453292f);
                final double d10 = this.motionX * this.motionX + this.motionZ * this.motionZ;
                if (d10 < 0.01) {
                    this.motionX += d8 * 0.1;
                    this.motionZ += d9 * 0.1;
                    flag2 = false;
                }
            }
        }
        if (flag2) {
            final double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d11 < 0.03) {
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
        final double d12 = pos.getX() + 0.5 + aint[0][0] * 0.5;
        final double d13 = pos.getZ() + 0.5 + aint[0][2] * 0.5;
        final double d14 = pos.getX() + 0.5 + aint[1][0] * 0.5;
        final double d15 = pos.getZ() + 0.5 + aint[1][2] * 0.5;
        d2 = d14 - d12;
        d3 = d15 - d13;
        double d16;
        if (d2 == 0.0) {
            this.posX = pos.getX() + 0.5;
            d16 = this.posZ - pos.getZ();
        }
        else if (d3 == 0.0) {
            this.posZ = pos.getZ() + 0.5;
            d16 = this.posX - pos.getX();
        }
        else {
            final double d17 = this.posX - d12;
            final double d18 = this.posZ - d13;
            d16 = (d17 * d2 + d18 * d3) * 2.0;
        }
        this.posX = d12 + d2 * d16;
        this.posZ = d13 + d3 * d16;
        this.setPosition(this.posX, this.posY, this.posZ);
        double d19 = this.motionX;
        double d20 = this.motionZ;
        if (this.isBeingRidden()) {
            d19 *= 0.75;
            d20 *= 0.75;
        }
        final double d21 = this.getMaximumSpeed();
        d19 = MathHelper.clamp(d19, -d21, d21);
        d20 = MathHelper.clamp(d20, -d21, d21);
        this.move(MoverType.SELF, d19, 0.0, d20);
        if (aint[0][1] != 0 && MathHelper.floor(this.posX) - pos.getX() == aint[0][0] && MathHelper.floor(this.posZ) - pos.getZ() == aint[0][2]) {
            this.setPosition(this.posX, this.posY + aint[0][1], this.posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor(this.posX) - pos.getX() == aint[1][0] && MathHelper.floor(this.posZ) - pos.getZ() == aint[1][2]) {
            this.setPosition(this.posX, this.posY + aint[1][1], this.posZ);
        }
        this.applyDrag();
        final Vec3d vec3d2 = this.getPos(this.posX, this.posY, this.posZ);
        if (vec3d2 != null && vec3d != null) {
            final double d22 = (vec3d.y - vec3d2.y) * 0.05;
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d6 > 0.0) {
                this.motionX = this.motionX / d6 * (d6 + d22);
                this.motionZ = this.motionZ / d6 * (d6 + d22);
            }
            this.setPosition(this.posX, vec3d2.y, this.posZ);
        }
        final int j = MathHelper.floor(this.posX);
        final int i = MathHelper.floor(this.posZ);
        if (j != pos.getX() || i != pos.getZ()) {
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d6 * (j - pos.getX());
            this.motionZ = d6 * (i - pos.getZ());
        }
        if (flag) {
            final double d23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d23 > 0.01) {
                final double d24 = 0.06;
                this.motionX += this.motionX / d23 * 0.06;
                this.motionZ += this.motionZ / d23 * 0.06;
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST) {
                if (this.world.getBlockState(pos.west()).isNormalCube()) {
                    this.motionX = 0.02;
                }
                else if (this.world.getBlockState(pos.east()).isNormalCube()) {
                    this.motionX = -0.02;
                }
            }
            else if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
                if (this.world.getBlockState(pos.north()).isNormalCube()) {
                    this.motionZ = 0.02;
                }
                else if (this.world.getBlockState(pos.south()).isNormalCube()) {
                    this.motionZ = -0.02;
                }
            }
        }
    }
    
    protected void applyDrag() {
        if (this.isBeingRidden()) {
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
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setEntityBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f2, z + f));
    }
    
    @Nullable
    public Vec3d getPosOffset(double x, double y, double z, final double offset) {
        final int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        final int k = MathHelper.floor(z);
        if (BlockRailBase.isRailBlock(this.world, new BlockPos(i, j - 1, k))) {
            --j;
        }
        final IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
        if (BlockRailBase.isRailBlock(iblockstate)) {
            final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            y = j;
            if (blockrailbase$enumraildirection.isAscending()) {
                y = j + 1;
            }
            final int[][] aint = EntityMinecart.MATRIX[blockrailbase$enumraildirection.getMetadata()];
            double d0 = aint[1][0] - aint[0][0];
            double d2 = aint[1][2] - aint[0][2];
            final double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            d0 /= d3;
            d2 /= d3;
            x += d0 * offset;
            z += d2 * offset;
            if (aint[0][1] != 0 && MathHelper.floor(x) - i == aint[0][0] && MathHelper.floor(z) - k == aint[0][2]) {
                y += aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor(x) - i == aint[1][0] && MathHelper.floor(z) - k == aint[1][2]) {
                y += aint[1][1];
            }
            return this.getPos(x, y, z);
        }
        return null;
    }
    
    @Nullable
    public Vec3d getPos(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
        final int i = MathHelper.floor(p_70489_1_);
        int j = MathHelper.floor(p_70489_3_);
        final int k = MathHelper.floor(p_70489_5_);
        if (BlockRailBase.isRailBlock(this.world, new BlockPos(i, j - 1, k))) {
            --j;
        }
        final IBlockState iblockstate = this.world.getBlockState(new BlockPos(i, j, k));
        if (BlockRailBase.isRailBlock(iblockstate)) {
            final BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            final int[][] aint = EntityMinecart.MATRIX[blockrailbase$enumraildirection.getMetadata()];
            final double d0 = i + 0.5 + aint[0][0] * 0.5;
            final double d2 = j + 0.0625 + aint[0][1] * 0.5;
            final double d3 = k + 0.5 + aint[0][2] * 0.5;
            final double d4 = i + 0.5 + aint[1][0] * 0.5;
            final double d5 = j + 0.0625 + aint[1][1] * 0.5;
            final double d6 = k + 0.5 + aint[1][2] * 0.5;
            final double d7 = d4 - d0;
            final double d8 = (d5 - d2) * 2.0;
            final double d9 = d6 - d3;
            double d10;
            if (d7 == 0.0) {
                d10 = p_70489_5_ - k;
            }
            else if (d9 == 0.0) {
                d10 = p_70489_1_ - i;
            }
            else {
                final double d11 = p_70489_1_ - d0;
                final double d12 = p_70489_5_ - d3;
                d10 = (d11 * d7 + d12 * d9) * 2.0;
            }
            p_70489_1_ = d0 + d7 * d10;
            p_70489_3_ = d2 + d8 * d10;
            p_70489_5_ = d3 + d9 * d10;
            if (d8 < 0.0) {
                ++p_70489_3_;
            }
            if (d8 > 0.0) {
                p_70489_3_ += 0.5;
            }
            return new Vec3d(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        return null;
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        return this.hasDisplayTile() ? axisalignedbb.grow(Math.abs(this.getDisplayTileOffset()) / 16.0) : axisalignedbb;
    }
    
    public static void registerFixesMinecart(final DataFixer fixer, final Class<?> name) {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        if (compound.getBoolean("CustomDisplayTile")) {
            Block block;
            if (compound.hasKey("DisplayTile", 8)) {
                block = Block.getBlockFromName(compound.getString("DisplayTile"));
            }
            else {
                block = Block.getBlockById(compound.getInteger("DisplayTile"));
            }
            final int i = compound.getInteger("DisplayData");
            this.setDisplayTile((block == null) ? Blocks.AIR.getDefaultState() : block.getStateFromMeta(i));
            this.setDisplayTileOffset(compound.getInteger("DisplayOffset"));
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        if (this.hasDisplayTile()) {
            compound.setBoolean("CustomDisplayTile", true);
            final IBlockState iblockstate = this.getDisplayTile();
            final ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(iblockstate.getBlock());
            compound.setString("DisplayTile", (resourcelocation == null) ? "" : resourcelocation.toString());
            compound.setInteger("DisplayData", iblockstate.getBlock().getMetaFromState(iblockstate));
            compound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        if (!this.world.isRemote && !entityIn.noClip && !this.noClip && !this.isPassenger(entityIn)) {
            double d0 = entityIn.posX - this.posX;
            double d2 = entityIn.posZ - this.posZ;
            double d3 = d0 * d0 + d2 * d2;
            if (d3 >= 9.999999747378752E-5) {
                d3 = MathHelper.sqrt(d3);
                d0 /= d3;
                d2 /= d3;
                double d4 = 1.0 / d3;
                if (d4 > 1.0) {
                    d4 = 1.0;
                }
                d0 *= d4;
                d2 *= d4;
                d0 *= 0.10000000149011612;
                d2 *= 0.10000000149011612;
                d0 *= 1.0f - this.entityCollisionReduction;
                d2 *= 1.0f - this.entityCollisionReduction;
                d0 *= 0.5;
                d2 *= 0.5;
                if (entityIn instanceof EntityMinecart) {
                    final double d5 = entityIn.posX - this.posX;
                    final double d6 = entityIn.posZ - this.posZ;
                    final Vec3d vec3d = new Vec3d(d5, 0.0, d6).normalize();
                    final Vec3d vec3d2 = new Vec3d(MathHelper.cos(this.rotationYaw * 0.017453292f), 0.0, MathHelper.sin(this.rotationYaw * 0.017453292f)).normalize();
                    final double d7 = Math.abs(vec3d.dotProduct(vec3d2));
                    if (d7 < 0.800000011920929) {
                        return;
                    }
                    double d8 = entityIn.motionX + this.motionX;
                    double d9 = entityIn.motionZ + this.motionZ;
                    if (((EntityMinecart)entityIn).getType() == Type.FURNACE && this.getType() != Type.FURNACE) {
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(entityIn.motionX - d0, 0.0, entityIn.motionZ - d2);
                        entityIn.motionX *= 0.949999988079071;
                        entityIn.motionZ *= 0.949999988079071;
                    }
                    else if (((EntityMinecart)entityIn).getType() != Type.FURNACE && this.getType() == Type.FURNACE) {
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(this.motionX + d0, 0.0, this.motionZ + d2);
                        this.motionX *= 0.949999988079071;
                        this.motionZ *= 0.949999988079071;
                    }
                    else {
                        d8 /= 2.0;
                        d9 /= 2.0;
                        this.motionX *= 0.20000000298023224;
                        this.motionZ *= 0.20000000298023224;
                        this.addVelocity(d8 - d0, 0.0, d9 - d2);
                        entityIn.motionX *= 0.20000000298023224;
                        entityIn.motionZ *= 0.20000000298023224;
                        entityIn.addVelocity(d8 + d0, 0.0, d9 + d2);
                    }
                }
                else {
                    this.addVelocity(-d0, 0.0, -d2);
                    entityIn.addVelocity(d0 / 4.0, 0.0, d2 / 4.0);
                }
            }
        }
    }
    
    @Override
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = yaw;
        this.minecartPitch = pitch;
        this.turnProgress = posRotationIncrements + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        this.velocityX = this.motionX;
        this.velocityY = this.motionY;
        this.velocityZ = this.motionZ;
    }
    
    public void setDamage(final float damage) {
        this.dataManager.set(EntityMinecart.DAMAGE, damage);
    }
    
    public float getDamage() {
        return this.dataManager.get(EntityMinecart.DAMAGE);
    }
    
    public void setRollingAmplitude(final int rollingAmplitude) {
        this.dataManager.set(EntityMinecart.ROLLING_AMPLITUDE, rollingAmplitude);
    }
    
    public int getRollingAmplitude() {
        return this.dataManager.get(EntityMinecart.ROLLING_AMPLITUDE);
    }
    
    public void setRollingDirection(final int rollingDirection) {
        this.dataManager.set(EntityMinecart.ROLLING_DIRECTION, rollingDirection);
    }
    
    public int getRollingDirection() {
        return this.dataManager.get(EntityMinecart.ROLLING_DIRECTION);
    }
    
    public abstract Type getType();
    
    public IBlockState getDisplayTile() {
        return this.hasDisplayTile() ? Block.getStateById(this.getDataManager().get(EntityMinecart.DISPLAY_TILE)) : this.getDefaultDisplayTile();
    }
    
    public IBlockState getDefaultDisplayTile() {
        return Blocks.AIR.getDefaultState();
    }
    
    public int getDisplayTileOffset() {
        return this.hasDisplayTile() ? this.getDataManager().get(EntityMinecart.DISPLAY_TILE_OFFSET) : this.getDefaultDisplayTileOffset();
    }
    
    public int getDefaultDisplayTileOffset() {
        return 6;
    }
    
    public void setDisplayTile(final IBlockState displayTile) {
        this.getDataManager().set(EntityMinecart.DISPLAY_TILE, Block.getStateId(displayTile));
        this.setHasDisplayTile(true);
    }
    
    public void setDisplayTileOffset(final int displayTileOffset) {
        this.getDataManager().set(EntityMinecart.DISPLAY_TILE_OFFSET, displayTileOffset);
        this.setHasDisplayTile(true);
    }
    
    public boolean hasDisplayTile() {
        return this.getDataManager().get(EntityMinecart.SHOW_BLOCK);
    }
    
    public void setHasDisplayTile(final boolean showBlock) {
        this.getDataManager().set(EntityMinecart.SHOW_BLOCK, showBlock);
    }
    
    static {
        ROLLING_AMPLITUDE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
        ROLLING_DIRECTION = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
        DAMAGE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.FLOAT);
        DISPLAY_TILE = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
        DISPLAY_TILE_OFFSET = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.VARINT);
        SHOW_BLOCK = EntityDataManager.createKey(EntityMinecart.class, DataSerializers.BOOLEAN);
        MATRIX = new int[][][] { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };
    }
    
    public enum Type
    {
        RIDEABLE(0, "MinecartRideable"), 
        CHEST(1, "MinecartChest"), 
        FURNACE(2, "MinecartFurnace"), 
        TNT(3, "MinecartTNT"), 
        SPAWNER(4, "MinecartSpawner"), 
        HOPPER(5, "MinecartHopper"), 
        COMMAND_BLOCK(6, "MinecartCommandBlock");
        
        private static final Map<Integer, Type> BY_ID;
        private final int id;
        private final String name;
        
        private Type(final int idIn, final String nameIn) {
            this.id = idIn;
            this.name = nameIn;
        }
        
        public int getId() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Type getById(final int idIn) {
            final Type entityminecart$type = Type.BY_ID.get(idIn);
            return (entityminecart$type == null) ? Type.RIDEABLE : entityminecart$type;
        }
        
        static {
            BY_ID = Maps.newHashMap();
            for (final Type entityminecart$type : values()) {
                Type.BY_ID.put(entityminecart$type.getId(), entityminecart$type);
            }
        }
    }
}
