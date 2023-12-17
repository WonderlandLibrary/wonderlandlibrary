// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.block.BlockPlanks;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.block.properties.IProperty;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.SoundEvents;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.Entity;

public class EntityBoat extends Entity
{
    private static final DataParameter<Integer> TIME_SINCE_HIT;
    private static final DataParameter<Integer> FORWARD_DIRECTION;
    private static final DataParameter<Float> DAMAGE_TAKEN;
    private static final DataParameter<Integer> BOAT_TYPE;
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE;
    private final float[] paddlePositions;
    private float momentum;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYaw;
    private double lerpPitch;
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private double waterLevel;
    private float boatGlide;
    private Status status;
    private Status previousStatus;
    private double lastYd;
    
    public EntityBoat(final World worldIn) {
        super(worldIn);
        this.paddlePositions = new float[2];
        this.preventEntitySpawning = true;
        this.setSize(1.375f, 0.5625f);
    }
    
    public EntityBoat(final World worldIn, final double x, final double y, final double z) {
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
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityBoat.TIME_SINCE_HIT, 0);
        this.dataManager.register(EntityBoat.FORWARD_DIRECTION, 1);
        this.dataManager.register(EntityBoat.DAMAGE_TAKEN, 0.0f);
        this.dataManager.register(EntityBoat.BOAT_TYPE, Type.OAK.ordinal());
        for (final DataParameter<Boolean> dataparameter : EntityBoat.DATA_ID_PADDLE) {
            this.dataManager.register(dataparameter, false);
        }
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }
    
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }
    
    @Override
    public boolean canBePushed() {
        return true;
    }
    
    @Override
    public double getMountedYOffset() {
        return -0.1;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.world.isRemote || this.isDead) {
            return true;
        }
        if (source instanceof EntityDamageSourceIndirect && source.getTrueSource() != null && this.isPassenger(source.getTrueSource())) {
            return false;
        }
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() + amount * 10.0f);
        this.markVelocityChanged();
        final boolean flag = source.getTrueSource() instanceof EntityPlayer && ((EntityPlayer)source.getTrueSource()).capabilities.isCreativeMode;
        if (flag || this.getDamageTaken() > 40.0f) {
            if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                this.dropItemWithOffset(this.getItemBoat(), 1, 0.0f);
            }
            this.setDead();
        }
        return true;
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        if (entityIn instanceof EntityBoat) {
            if (entityIn.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        }
        else if (entityIn.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
            super.applyEntityCollision(entityIn);
        }
    }
    
    public Item getItemBoat() {
        switch (this.getBoatType()) {
            default: {
                return Items.BOAT;
            }
            case SPRUCE: {
                return Items.SPRUCE_BOAT;
            }
            case BIRCH: {
                return Items.BIRCH_BOAT;
            }
            case JUNGLE: {
                return Items.JUNGLE_BOAT;
            }
            case ACACIA: {
                return Items.ACACIA_BOAT;
            }
            case DARK_OAK: {
                return Items.DARK_OAK_BOAT;
            }
        }
    }
    
    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0f);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYaw = yaw;
        this.lerpPitch = pitch;
        this.lerpSteps = 10;
    }
    
    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return this.getHorizontalFacing().rotateY();
    }
    
    @Override
    public void onUpdate() {
        this.previousStatus = this.status;
        this.status = this.getBoatStatus();
        if (this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0f;
        }
        else {
            ++this.outOfControlTicks;
        }
        if (!this.world.isRemote && this.outOfControlTicks >= 60.0f) {
            this.removePassengers();
        }
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamageTaken() > 0.0f) {
            this.setDamageTaken(this.getDamageTaken() - 1.0f);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        super.onUpdate();
        this.tickLerp();
        if (this.canPassengerSteer()) {
            if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                this.setPaddleState(false, false);
            }
            this.updateMotion();
            if (this.world.isRemote) {
                this.controlBoat();
                this.world.sendPacketToServer(new CPacketSteerBoat(this.getPaddleState(0), this.getPaddleState(1)));
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        }
        else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
        }
        for (int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                if (!this.isSilent() && this.paddlePositions[i] % 6.2831855f <= 0.7853981633974483 && (this.paddlePositions[i] + 0.39269909262657166) % 6.283185307179586 >= 0.7853981633974483) {
                    final SoundEvent soundevent = this.getPaddleSound();
                    if (soundevent != null) {
                        final Vec3d vec3d = this.getLook(1.0f);
                        final double d0 = (i == 1) ? (-vec3d.z) : vec3d.z;
                        final double d2 = (i == 1) ? vec3d.x : (-vec3d.x);
                        this.world.playSound(null, this.posX + d0, this.posY, this.posZ + d2, soundevent, this.getSoundCategory(), 1.0f, 0.8f + 0.4f * this.rand.nextFloat());
                    }
                }
                this.paddlePositions[i] += 0.39269909262657166;
            }
            else {
                this.paddlePositions[i] = 0.0f;
            }
        }
        this.doBlockCollisions();
        final List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().grow(0.20000000298023224, -0.009999999776482582, 0.20000000298023224), EntitySelectors.getTeamCollisionPredicate(this));
        if (!list.isEmpty()) {
            final boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);
            for (int j = 0; j < list.size(); ++j) {
                final Entity entity = list.get(j);
                if (!entity.isPassenger(this)) {
                    if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer)) {
                        entity.startRiding(this);
                    }
                    else {
                        this.applyEntityCollision(entity);
                    }
                }
            }
        }
    }
    
    @Nullable
    protected SoundEvent getPaddleSound() {
        switch (this.getBoatStatus()) {
            case IN_WATER:
            case UNDER_WATER:
            case UNDER_FLOWING_WATER: {
                return SoundEvents.ENTITY_BOAT_PADDLE_WATER;
            }
            case ON_LAND: {
                return SoundEvents.ENTITY_BOAT_PADDLE_LAND;
            }
            default: {
                return null;
            }
        }
    }
    
    private void tickLerp() {
        if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
            final double d0 = this.posX + (this.lerpX - this.posX) / this.lerpSteps;
            final double d2 = this.posY + (this.lerpY - this.posY) / this.lerpSteps;
            final double d3 = this.posZ + (this.lerpZ - this.posZ) / this.lerpSteps;
            final double d4 = MathHelper.wrapDegrees(this.lerpYaw - this.rotationYaw);
            this.rotationYaw += (float)(d4 / this.lerpSteps);
            this.rotationPitch += (float)((this.lerpPitch - this.rotationPitch) / this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d2, d3);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }
    
    public void setPaddleState(final boolean left, final boolean right) {
        this.dataManager.set(EntityBoat.DATA_ID_PADDLE[0], left);
        this.dataManager.set(EntityBoat.DATA_ID_PADDLE[1], right);
    }
    
    public float getRowingTime(final int side, final float limbSwing) {
        return this.getPaddleState(side) ? ((float)MathHelper.clampedLerp(this.paddlePositions[side] - 0.39269909262657166, this.paddlePositions[side], limbSwing)) : 0.0f;
    }
    
    private Status getBoatStatus() {
        final Status entityboat$status = this.getUnderwaterStatus();
        if (entityboat$status != null) {
            this.waterLevel = this.getEntityBoundingBox().maxY;
            return entityboat$status;
        }
        if (this.checkInWater()) {
            return Status.IN_WATER;
        }
        final float f = this.getBoatGlide();
        if (f > 0.0f) {
            this.boatGlide = f;
            return Status.ON_LAND;
        }
        return Status.IN_AIR;
    }
    
    public float getWaterLevelAbove() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        final int i = MathHelper.floor(axisalignedbb.minX);
        final int j = MathHelper.ceil(axisalignedbb.maxX);
        final int k = MathHelper.floor(axisalignedbb.maxY);
        final int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        final int i2 = MathHelper.floor(axisalignedbb.minZ);
        final int j2 = MathHelper.ceil(axisalignedbb.maxZ);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            int k2 = k;
        Label_0207_Outer:
            while (k2 < l) {
                float f = 0.0f;
                int l2 = i;
            Label_0207:
                while (true) {
                    while (l2 < j) {
                        for (int i3 = i2; i3 < j2; ++i3) {
                            blockpos$pooledmutableblockpos.setPos(l2, k2, i3);
                            final IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                            if (iblockstate.getMaterial() == Material.WATER) {
                                f = Math.max(f, BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos));
                            }
                            if (f >= 1.0f) {
                                break Label_0207;
                            }
                        }
                        ++l2;
                        continue Label_0207_Outer;
                        ++k2;
                        continue Label_0207_Outer;
                    }
                    if (f < 1.0f) {
                        final float f2 = blockpos$pooledmutableblockpos.getY() + f;
                        return f2;
                    }
                    continue Label_0207;
                }
            }
            final float f3 = (float)(l + 1);
            return f3;
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
    }
    
    public float getBoatGlide() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        final AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        final int i = MathHelper.floor(axisalignedbb2.minX) - 1;
        final int j = MathHelper.ceil(axisalignedbb2.maxX) + 1;
        final int k = MathHelper.floor(axisalignedbb2.minY) - 1;
        final int l = MathHelper.ceil(axisalignedbb2.maxY) + 1;
        final int i2 = MathHelper.floor(axisalignedbb2.minZ) - 1;
        final int j2 = MathHelper.ceil(axisalignedbb2.maxZ) + 1;
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        float f = 0.0f;
        int k2 = 0;
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int l2 = i; l2 < j; ++l2) {
                for (int i3 = i2; i3 < j2; ++i3) {
                    final int j3 = ((l2 == i || l2 == j - 1) + (i3 == i2 || i3 == j2 - 1)) ? 1 : 0;
                    if (j3 != 2) {
                        for (int k3 = k; k3 < l; ++k3) {
                            if (j3 <= 0 || (k3 != k && k3 != l - 1)) {
                                blockpos$pooledmutableblockpos.setPos(l2, k3, i3);
                                final IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                                iblockstate.addCollisionBoxToList(this.world, blockpos$pooledmutableblockpos, axisalignedbb2, list, this, false);
                                if (!list.isEmpty()) {
                                    f += iblockstate.getBlock().slipperiness;
                                    ++k2;
                                }
                                list.clear();
                            }
                        }
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return f / k2;
    }
    
    private boolean checkInWater() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        final int i = MathHelper.floor(axisalignedbb.minX);
        final int j = MathHelper.ceil(axisalignedbb.maxX);
        final int k = MathHelper.floor(axisalignedbb.minY);
        final int l = MathHelper.ceil(axisalignedbb.minY + 0.001);
        final int i2 = MathHelper.floor(axisalignedbb.minZ);
        final int j2 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = k; l2 < l; ++l2) {
                    for (int i3 = i2; i3 < j2; ++i3) {
                        blockpos$pooledmutableblockpos.setPos(k2, l2, i3);
                        final IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        if (iblockstate.getMaterial() == Material.WATER) {
                            final float f = BlockLiquid.getLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos);
                            this.waterLevel = Math.max(f, this.waterLevel);
                            flag |= (axisalignedbb.minY < f);
                        }
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return flag;
    }
    
    @Nullable
    private Status getUnderwaterStatus() {
        final AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        final double d0 = axisalignedbb.maxY + 0.001;
        final int i = MathHelper.floor(axisalignedbb.minX);
        final int j = MathHelper.ceil(axisalignedbb.maxX);
        final int k = MathHelper.floor(axisalignedbb.maxY);
        final int l = MathHelper.ceil(d0);
        final int i2 = MathHelper.floor(axisalignedbb.minZ);
        final int j2 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = k; l2 < l; ++l2) {
                    for (int i3 = i2; i3 < j2; ++i3) {
                        blockpos$pooledmutableblockpos.setPos(k2, l2, i3);
                        final IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        if (iblockstate.getMaterial() == Material.WATER && d0 < BlockLiquid.getLiquidHeight(iblockstate, this.world, blockpos$pooledmutableblockpos)) {
                            if (iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL) != 0) {
                                final Status entityboat$status = Status.UNDER_FLOWING_WATER;
                                return entityboat$status;
                            }
                            flag = true;
                        }
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return flag ? Status.UNDER_WATER : null;
    }
    
    private void updateMotion() {
        final double d0 = -0.03999999910593033;
        double d2 = this.hasNoGravity() ? 0.0 : -0.03999999910593033;
        double d3 = 0.0;
        this.momentum = 0.05f;
        if (this.previousStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
            this.waterLevel = this.getEntityBoundingBox().minY + this.height;
            this.setPosition(this.posX, this.getWaterLevelAbove() - this.height + 0.101, this.posZ);
            this.motionY = 0.0;
            this.lastYd = 0.0;
            this.status = Status.IN_WATER;
        }
        else {
            if (this.status == Status.IN_WATER) {
                d3 = (this.waterLevel - this.getEntityBoundingBox().minY) / this.height;
                this.momentum = 0.9f;
            }
            else if (this.status == Status.UNDER_FLOWING_WATER) {
                d2 = -7.0E-4;
                this.momentum = 0.9f;
            }
            else if (this.status == Status.UNDER_WATER) {
                d3 = 0.009999999776482582;
                this.momentum = 0.45f;
            }
            else if (this.status == Status.IN_AIR) {
                this.momentum = 0.9f;
            }
            else if (this.status == Status.ON_LAND) {
                this.momentum = this.boatGlide;
                if (this.getControllingPassenger() instanceof EntityPlayer) {
                    this.boatGlide /= 2.0f;
                }
            }
            this.motionX *= this.momentum;
            this.motionZ *= this.momentum;
            this.deltaRotation *= this.momentum;
            this.motionY += d2;
            if (d3 > 0.0) {
                final double d4 = 0.65;
                this.motionY += d3 * 0.06153846016296973;
                final double d5 = 0.75;
                this.motionY *= 0.75;
            }
        }
    }
    
    private void controlBoat() {
        if (this.isBeingRidden()) {
            float f = 0.0f;
            if (this.leftInputDown) {
                --this.deltaRotation;
            }
            if (this.rightInputDown) {
                ++this.deltaRotation;
            }
            if (this.rightInputDown != this.leftInputDown && !this.forwardInputDown && !this.backInputDown) {
                f += 0.005f;
            }
            this.rotationYaw += this.deltaRotation;
            if (this.forwardInputDown) {
                f += 0.04f;
            }
            if (this.backInputDown) {
                f -= 0.005f;
            }
            this.motionX += MathHelper.sin(-this.rotationYaw * 0.017453292f) * f;
            this.motionZ += MathHelper.cos(this.rotationYaw * 0.017453292f) * f;
            this.setPaddleState((this.rightInputDown && !this.leftInputDown) || this.forwardInputDown, (this.leftInputDown && !this.rightInputDown) || this.forwardInputDown);
        }
    }
    
    @Override
    public void updatePassenger(final Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = 0.0f;
            final float f2 = (float)((this.isDead ? 0.009999999776482582 : this.getMountedYOffset()) + passenger.getYOffset());
            if (this.getPassengers().size() > 1) {
                final int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    f = 0.2f;
                }
                else {
                    f = -0.6f;
                }
                if (passenger instanceof EntityAnimal) {
                    f += (float)0.2;
                }
            }
            final Vec3d vec3d = new Vec3d(f, 0.0, 0.0).rotateYaw(-this.rotationYaw * 0.017453292f - 1.5707964f);
            passenger.setPosition(this.posX + vec3d.x, this.posY + f2, this.posZ + vec3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);
            if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
                final int j = (passenger.getEntityId() % 2 == 0) ? 90 : 270;
                passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
            }
        }
    }
    
    protected void applyYawToEntity(final Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        final float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        final float f2 = MathHelper.clamp(f, -105.0f, 105.0f);
        entityToUpdate.prevRotationYaw += f2 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw += f2 - f);
    }
    
    @Override
    public void applyOrientationToEntity(final Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setString("Type", this.getBoatType().getName());
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        if (compound.hasKey("Type", 8)) {
            this.setBoatType(Type.getTypeFromString(compound.getString("Type")));
        }
    }
    
    @Override
    public boolean processInitialInteract(final EntityPlayer player, final EnumHand hand) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.world.isRemote && this.outOfControlTicks < 60.0f) {
            player.startRiding(this);
        }
        return true;
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
        this.lastYd = this.motionY;
        if (!this.isRiding()) {
            if (onGroundIn) {
                if (this.fallDistance > 3.0f) {
                    if (this.status != Status.ON_LAND) {
                        this.fallDistance = 0.0f;
                        return;
                    }
                    this.fall(this.fallDistance, 1.0f);
                    if (!this.world.isRemote && !this.isDead) {
                        this.setDead();
                        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                            for (int i = 0; i < 3; ++i) {
                                this.entityDropItem(new ItemStack(Item.getItemFromBlock(Blocks.PLANKS), 1, this.getBoatType().getMetadata()), 0.0f);
                            }
                            for (int j = 0; j < 2; ++j) {
                                this.dropItemWithOffset(Items.STICK, 1, 0.0f);
                            }
                        }
                    }
                }
                this.fallDistance = 0.0f;
            }
            else if (this.world.getBlockState(new BlockPos(this).down()).getMaterial() != Material.WATER && y < 0.0) {
                this.fallDistance -= (float)y;
            }
        }
    }
    
    public boolean getPaddleState(final int side) {
        return this.dataManager.get(EntityBoat.DATA_ID_PADDLE[side]) && this.getControllingPassenger() != null;
    }
    
    public void setDamageTaken(final float damageTaken) {
        this.dataManager.set(EntityBoat.DAMAGE_TAKEN, damageTaken);
    }
    
    public float getDamageTaken() {
        return this.dataManager.get(EntityBoat.DAMAGE_TAKEN);
    }
    
    public void setTimeSinceHit(final int timeSinceHit) {
        this.dataManager.set(EntityBoat.TIME_SINCE_HIT, timeSinceHit);
    }
    
    public int getTimeSinceHit() {
        return this.dataManager.get(EntityBoat.TIME_SINCE_HIT);
    }
    
    public void setForwardDirection(final int forwardDirection) {
        this.dataManager.set(EntityBoat.FORWARD_DIRECTION, forwardDirection);
    }
    
    public int getForwardDirection() {
        return this.dataManager.get(EntityBoat.FORWARD_DIRECTION);
    }
    
    public void setBoatType(final Type boatType) {
        this.dataManager.set(EntityBoat.BOAT_TYPE, boatType.ordinal());
    }
    
    public Type getBoatType() {
        return Type.byId(this.dataManager.get(EntityBoat.BOAT_TYPE));
    }
    
    @Override
    protected boolean canFitPassenger(final Entity passenger) {
        return this.getPassengers().size() < 2;
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        final List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }
    
    public void updateInputs(final boolean p_184442_1_, final boolean p_184442_2_, final boolean p_184442_3_, final boolean p_184442_4_) {
        this.leftInputDown = p_184442_1_;
        this.rightInputDown = p_184442_2_;
        this.forwardInputDown = p_184442_3_;
        this.backInputDown = p_184442_4_;
    }
    
    static {
        TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
        FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
        DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
        BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
        DATA_ID_PADDLE = new DataParameter[] { EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN) };
    }
    
    public enum Status
    {
        IN_WATER, 
        UNDER_WATER, 
        UNDER_FLOWING_WATER, 
        ON_LAND, 
        IN_AIR;
    }
    
    public enum Type
    {
        OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"), 
        SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"), 
        BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"), 
        JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"), 
        ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"), 
        DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");
        
        private final String name;
        private final int metadata;
        
        private Type(final int metadataIn, final String nameIn) {
            this.name = nameIn;
            this.metadata = metadataIn;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getMetadata() {
            return this.metadata;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static Type byId(int id) {
            if (id < 0 || id >= values().length) {
                id = 0;
            }
            return values()[id];
        }
        
        public static Type getTypeFromString(final String nameIn) {
            for (int i = 0; i < values().length; ++i) {
                if (values()[i].getName().equals(nameIn)) {
                    return values()[i];
                }
            }
            return values()[0];
        }
    }
}
