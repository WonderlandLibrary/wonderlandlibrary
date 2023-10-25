/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityBoat
extends Entity {
    private static final DataParameter<Integer> TIME_SINCE_HIT = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FORWARD_DIRECTION = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE_TAKEN = EntityDataManager.createKey(EntityBoat.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> BOAT_TYPE = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean>[] DATA_ID_PADDLE = new DataParameter[]{EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN), EntityDataManager.createKey(EntityBoat.class, DataSerializers.BOOLEAN)};
    private final float[] paddlePositions = new float[2];
    private float momentum;
    private float outOfControlTicks;
    private float deltaRotation;
    private int lerpSteps;
    private double boatPitch;
    private double lerpY;
    private double lerpZ;
    private double boatYaw;
    private double lerpXRot;
    private boolean leftInputDown;
    private boolean rightInputDown;
    private boolean forwardInputDown;
    private boolean backInputDown;
    private double waterLevel;
    private float boatGlide;
    private Status status;
    private Status previousStatus;
    private double lastYd;

    public EntityBoat(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(1.375f, 0.5625f);
    }

    public EntityBoat(World worldIn, double x, double y, double z) {
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
        this.dataManager.register(TIME_SINCE_HIT, 0);
        this.dataManager.register(FORWARD_DIRECTION, 1);
        this.dataManager.register(DAMAGE_TAKEN, Float.valueOf(0.0f));
        this.dataManager.register(BOAT_TYPE, Type.OAK.ordinal());
        for (DataParameter<Boolean> dataparameter : DATA_ID_PADDLE) {
            this.dataManager.register(dataparameter, false);
        }
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.canBePushed() ? entityIn.getEntityBoundingBox() : null;
    }

    @Override
    @Nullable
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (!this.world.isRemote && !this.isDead) {
            boolean flag;
            if (source instanceof EntityDamageSourceIndirect && source.getEntity() != null && this.isPassenger(source.getEntity())) {
                return false;
            }
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0f);
            this.setBeenAttacked();
            boolean bl = flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
            if (flag || this.getDamageTaken() > 40.0f) {
                if (!flag && this.world.getGameRules().getBoolean("doEntityDrops")) {
                    this.dropItemWithOffset(this.getItemBoat(), 1, 0.0f);
                }
                this.setDead();
            }
            return true;
        }
        return true;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (entityIn instanceof EntityBoat) {
            if (entityIn.getEntityBoundingBox().minY < this.getEntityBoundingBox().maxY) {
                super.applyEntityCollision(entityIn);
            }
        } else if (entityIn.getEntityBoundingBox().minY <= this.getEntityBoundingBox().minY) {
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
            case DARK_OAK: 
        }
        return Items.DARK_OAK_BOAT;
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
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.boatPitch = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.boatYaw = yaw;
        this.lerpXRot = pitch;
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
        this.outOfControlTicks = this.status != Status.UNDER_WATER && this.status != Status.UNDER_FLOWING_WATER ? 0.0f : (this.outOfControlTicks += 1.0f);
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
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        } else {
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
        }
        for (int i = 0; i <= 1; ++i) {
            if (this.getPaddleState(i)) {
                SoundEvent soundevent;
                if (!this.isSilent() && (double)(this.paddlePositions[i] % ((float)Math.PI * 2)) <= 0.7853981633974483 && ((double)this.paddlePositions[i] + (double)0.3926991f) % (Math.PI * 2) >= 0.7853981633974483 && (soundevent = this.func_193047_k()) != null) {
                    Vec3d vec3d = this.getLook(1.0f);
                    double d0 = i == 1 ? -vec3d.z : vec3d.z;
                    double d1 = i == 1 ? vec3d.x : -vec3d.x;
                    this.world.playSound(null, this.posX + d0, this.posY, this.posZ + d1, soundevent, this.getSoundCategory(), 1.0f, 0.8f + 0.4f * this.rand.nextFloat());
                }
                this.paddlePositions[i] = (float)((double)this.paddlePositions[i] + (double)0.3926991f);
                continue;
            }
            this.paddlePositions[i] = 0.0f;
        }
        this.doBlockCollisions();
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(0.2f, -0.01f, 0.2f), EntitySelectors.getTeamCollisionPredicate(this));
        if (!list.isEmpty()) {
            boolean flag = !this.world.isRemote && !(this.getControllingPassenger() instanceof EntityPlayer);
            for (int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);
                if (entity.isPassenger(this)) continue;
                if (flag && this.getPassengers().size() < 2 && !entity.isRiding() && entity.width < this.width && entity instanceof EntityLivingBase && !(entity instanceof EntityWaterMob) && !(entity instanceof EntityPlayer)) {
                    entity.startRiding(this);
                    continue;
                }
                this.applyEntityCollision(entity);
            }
        }
    }

    @Nullable
    protected SoundEvent func_193047_k() {
        switch (this.getBoatStatus()) {
            case IN_WATER: 
            case UNDER_WATER: 
            case UNDER_FLOWING_WATER: {
                return SoundEvents.field_193779_I;
            }
            case ON_LAND: {
                return SoundEvents.field_193778_H;
            }
        }
        return null;
    }

    private void tickLerp() {
        if (this.lerpSteps > 0 && !this.canPassengerSteer()) {
            double d0 = this.posX + (this.boatPitch - this.posX) / (double)this.lerpSteps;
            double d1 = this.posY + (this.lerpY - this.posY) / (double)this.lerpSteps;
            double d2 = this.posZ + (this.lerpZ - this.posZ) / (double)this.lerpSteps;
            double d3 = MathHelper.wrapDegrees(this.boatYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.lerpSteps);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.lerpXRot - (double)this.rotationPitch) / (double)this.lerpSteps);
            --this.lerpSteps;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
    }

    public void setPaddleState(boolean p_184445_1_, boolean p_184445_2_) {
        this.dataManager.set(DATA_ID_PADDLE[0], p_184445_1_);
        this.dataManager.set(DATA_ID_PADDLE[1], p_184445_2_);
    }

    public float getRowingTime(int p_184448_1_, float limbSwing) {
        return this.getPaddleState(p_184448_1_) ? (float)MathHelper.clampedLerp((double)this.paddlePositions[p_184448_1_] - (double)0.3926991f, this.paddlePositions[p_184448_1_], limbSwing) : 0.0f;
    }

    private Status getBoatStatus() {
        Status entityboat$status = this.getUnderwaterStatus();
        if (entityboat$status != null) {
            this.waterLevel = this.getEntityBoundingBox().maxY;
            return entityboat$status;
        }
        if (this.checkInWater()) {
            return Status.IN_WATER;
        }
        float f = this.getBoatGlide();
        if (f > 0.0f) {
            this.boatGlide = f;
            return Status.ON_LAND;
        }
        return Status.IN_AIR;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public float getWaterLevelAbove() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(axisalignedbb.maxY - this.lastYd);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            float f1;
            block4: for (int k1 = k; k1 < l; ++k1) {
                float f = 0.0f;
                int l1 = i;
                while (true) {
                    if (l1 >= j) {
                        float f2;
                        if (!(f < 1.0f)) continue block4;
                        float f3 = f2 = (float)blockpos$pooledmutableblockpos.getY() + f;
                        return f3;
                    }
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(l1, k1, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        if (iblockstate.getMaterial() == Material.WATER) {
                            f = Math.max(f, BlockLiquid.func_190973_f(iblockstate, this.world, blockpos$pooledmutableblockpos));
                        }
                        if (f >= 1.0f) continue block4;
                    }
                    ++l1;
                }
            }
            float f = f1 = (float)(l + 1);
            return f;
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public float getBoatGlide() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY - 0.001, axisalignedbb.minZ, axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
        int i = MathHelper.floor(axisalignedbb1.minX) - 1;
        int j = MathHelper.ceil(axisalignedbb1.maxX) + 1;
        int k = MathHelper.floor(axisalignedbb1.minY) - 1;
        int l = MathHelper.ceil(axisalignedbb1.maxY) + 1;
        int i1 = MathHelper.floor(axisalignedbb1.minZ) - 1;
        int j1 = MathHelper.ceil(axisalignedbb1.maxZ) + 1;
        ArrayList<AxisAlignedBB> list = Lists.newArrayList();
        float f = 0.0f;
        int k1 = 0;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int l1 = i; l1 < j; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                    if (j2 == 2) continue;
                    for (int k2 = k; k2 < l; ++k2) {
                        if (j2 > 0 && (k2 == k || k2 == l - 1)) continue;
                        blockpos$pooledmutableblockpos.setPos(l1, k2, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        iblockstate.addCollisionBoxToList(this.world, blockpos$pooledmutableblockpos, axisalignedbb1, list, this, false);
                        if (!list.isEmpty()) {
                            f += iblockstate.getBlock().slipperiness;
                            ++k1;
                        }
                        list.clear();
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return f / (float)k1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean checkInWater() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.minY);
        int l = MathHelper.ceil(axisalignedbb.minY + 0.001);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        this.waterLevel = Double.MIN_VALUE;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        if (iblockstate.getMaterial() != Material.WATER) continue;
                        float f = BlockLiquid.func_190972_g(iblockstate, this.world, blockpos$pooledmutableblockpos);
                        this.waterLevel = Math.max((double)f, this.waterLevel);
                        flag |= axisalignedbb.minY < (double)f;
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return flag;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Nullable
    private Status getUnderwaterStatus() {
        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        double d0 = axisalignedbb.maxY + 0.001;
        int i = MathHelper.floor(axisalignedbb.minX);
        int j = MathHelper.ceil(axisalignedbb.maxX);
        int k = MathHelper.floor(axisalignedbb.maxY);
        int l = MathHelper.ceil(d0);
        int i1 = MathHelper.floor(axisalignedbb.minZ);
        int j1 = MathHelper.ceil(axisalignedbb.maxZ);
        boolean flag = false;
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
                        IBlockState iblockstate = this.world.getBlockState(blockpos$pooledmutableblockpos);
                        if (iblockstate.getMaterial() != Material.WATER || !(d0 < (double)BlockLiquid.func_190972_g(iblockstate, this.world, blockpos$pooledmutableblockpos))) continue;
                        if (iblockstate.getValue(BlockLiquid.LEVEL) != 0) {
                            Status entityboat$status;
                            Status status = entityboat$status = Status.UNDER_FLOWING_WATER;
                            return status;
                        }
                        flag = true;
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
        double d0 = -0.04f;
        double d1 = this.hasNoGravity() ? 0.0 : (double)-0.04f;
        double d2 = 0.0;
        this.momentum = 0.05f;
        if (this.previousStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
            this.waterLevel = this.getEntityBoundingBox().minY + (double)this.height;
            this.setPosition(this.posX, (double)(this.getWaterLevelAbove() - this.height) + 0.101, this.posZ);
            this.motionY = 0.0;
            this.lastYd = 0.0;
            this.status = Status.IN_WATER;
        } else {
            if (this.status == Status.IN_WATER) {
                d2 = (this.waterLevel - this.getEntityBoundingBox().minY) / (double)this.height;
                this.momentum = 0.9f;
            } else if (this.status == Status.UNDER_FLOWING_WATER) {
                d1 = -7.0E-4;
                this.momentum = 0.9f;
            } else if (this.status == Status.UNDER_WATER) {
                d2 = 0.01f;
                this.momentum = 0.45f;
            } else if (this.status == Status.IN_AIR) {
                this.momentum = 0.9f;
            } else if (this.status == Status.ON_LAND) {
                this.momentum = this.boatGlide;
                if (this.getControllingPassenger() instanceof EntityPlayer) {
                    this.boatGlide /= 2.0f;
                }
            }
            this.motionX *= (double)this.momentum;
            this.motionZ *= (double)this.momentum;
            this.deltaRotation *= this.momentum;
            this.motionY += d1;
            if (d2 > 0.0) {
                double d3 = 0.65;
                this.motionY += d2 * 0.06153846016296973;
                double d4 = 0.75;
                this.motionY *= 0.75;
            }
        }
    }

    private void controlBoat() {
        if (this.isBeingRidden()) {
            float f = 0.0f;
            if (this.leftInputDown) {
                this.deltaRotation += -1.0f;
            }
            if (this.rightInputDown) {
                this.deltaRotation += 1.0f;
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
            this.motionX += (double)(MathHelper.sin(-this.rotationYaw * ((float)Math.PI / 180)) * f);
            this.motionZ += (double)(MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180)) * f);
            this.setPaddleState(this.rightInputDown && !this.leftInputDown || this.forwardInputDown, this.leftInputDown && !this.rightInputDown || this.forwardInputDown);
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            float f = 0.0f;
            float f1 = (float)((this.isDead ? (double)0.01f : this.getMountedYOffset()) + passenger.getYOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                f = i == 0 ? 0.2f : -0.6f;
                if (passenger instanceof EntityAnimal) {
                    f = (float)((double)f + 0.2);
                }
            }
            Vec3d vec3d = new Vec3d(f, 0.0, 0.0).rotateYaw(-this.rotationYaw * ((float)Math.PI / 180) - 1.5707964f);
            passenger.setPosition(this.posX + vec3d.x, this.posY + (double)f1, this.posZ + vec3d.z);
            passenger.rotationYaw += this.deltaRotation;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.deltaRotation);
            this.applyYawToEntity(passenger);
            if (passenger instanceof EntityAnimal && this.getPassengers().size() > 1) {
                int j = passenger.getEntityId() % 2 == 0 ? 90 : 270;
                passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + (float)j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + (float)j);
            }
        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0f, 105.0f);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    @Override
    public void applyOrientationToEntity(Entity entityToUpdate) {
        this.applyYawToEntity(entityToUpdate);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setString("Type", this.getBoatType().getName());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("Type", 8)) {
            this.setBoatType(Type.getTypeFromString(compound.getString("Type")));
        }
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand stack) {
        if (player.isSneaking()) {
            return false;
        }
        if (!this.world.isRemote && this.outOfControlTicks < 60.0f) {
            player.startRiding(this);
        }
        return true;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
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
            } else if (this.world.getBlockState(new BlockPos(this).down()).getMaterial() != Material.WATER && y < 0.0) {
                this.fallDistance = (float)((double)this.fallDistance - y);
            }
        }
    }

    public boolean getPaddleState(int p_184457_1_) {
        return this.dataManager.get(DATA_ID_PADDLE[p_184457_1_]) != false && this.getControllingPassenger() != null;
    }

    public void setDamageTaken(float damageTaken) {
        this.dataManager.set(DAMAGE_TAKEN, Float.valueOf(damageTaken));
    }

    public float getDamageTaken() {
        return this.dataManager.get(DAMAGE_TAKEN).floatValue();
    }

    public void setTimeSinceHit(int timeSinceHit) {
        this.dataManager.set(TIME_SINCE_HIT, timeSinceHit);
    }

    public int getTimeSinceHit() {
        return this.dataManager.get(TIME_SINCE_HIT);
    }

    public void setForwardDirection(int forwardDirection) {
        this.dataManager.set(FORWARD_DIRECTION, forwardDirection);
    }

    public int getForwardDirection() {
        return this.dataManager.get(FORWARD_DIRECTION);
    }

    public void setBoatType(Type boatType) {
        this.dataManager.set(BOAT_TYPE, boatType.ordinal());
    }

    public Type getBoatType() {
        return Type.byId(this.dataManager.get(BOAT_TYPE));
    }

    @Override
    protected boolean canFitPassenger(Entity passenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    public void updateInputs(boolean p_184442_1_, boolean p_184442_2_, boolean p_184442_3_, boolean p_184442_4_) {
        this.leftInputDown = p_184442_1_;
        this.rightInputDown = p_184442_2_;
        this.forwardInputDown = p_184442_3_;
        this.backInputDown = p_184442_4_;
    }

    public static enum Type {
        OAK(BlockPlanks.EnumType.OAK.getMetadata(), "oak"),
        SPRUCE(BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce"),
        BIRCH(BlockPlanks.EnumType.BIRCH.getMetadata(), "birch"),
        JUNGLE(BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle"),
        ACACIA(BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia"),
        DARK_OAK(BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak");

        private final String name;
        private final int metadata;

        private Type(int metadataIn, String nameIn) {
            this.name = nameIn;
            this.metadata = metadataIn;
        }

        public String getName() {
            return this.name;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String toString() {
            return this.name;
        }

        public static Type byId(int id) {
            if (id < 0 || id >= Type.values().length) {
                id = 0;
            }
            return Type.values()[id];
        }

        public static Type getTypeFromString(String nameIn) {
            for (int i = 0; i < Type.values().length; ++i) {
                if (!Type.values()[i].getName().equals(nameIn)) continue;
                return Type.values()[i];
            }
            return Type.values()[0];
        }
    }

    public static enum Status {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;

    }
}

