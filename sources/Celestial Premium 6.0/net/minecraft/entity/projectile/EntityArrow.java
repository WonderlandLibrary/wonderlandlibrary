/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.movement.Speed;
import org.celestial.client.feature.impl.visuals.PearlESP;

public abstract class EntityArrow
extends Entity
implements IProjectile {
    private static final Predicate<Entity> ARROW_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>(){

        @Override
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    private static final DataParameter<Byte> CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    private Block inTile;
    private int inData;
    protected boolean inGround;
    protected int timeInGround;
    public PickupStatus pickupStatus = PickupStatus.DISALLOWED;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0;
    private int knockbackStrength;

    public EntityArrow(World worldIn) {
        super(worldIn);
        this.setSize(0.5f, 0.5f);
    }

    public EntityArrow(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public EntityArrow(World worldIn, EntityLivingBase shooter) {
        this(worldIn, shooter.posX, shooter.posY + (double)shooter.getEyeHeight() - (double)0.1f, shooter.posZ);
        this.shootingEntity = shooter;
        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = PickupStatus.ALLOWED;
        }
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        return distance < (d0 = d0 * 64.0 * EntityArrow.getRenderDistanceWeight()) * d0;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(CRITICAL, (byte)0);
    }

    public void setAim(Entity shooter, float pitch, float yaw, float p_184547_4_, float velocity, float inaccuracy) {
        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        float f1 = -MathHelper.sin(pitch * ((float)Math.PI / 180));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180)) * MathHelper.cos(pitch * ((float)Math.PI / 180));
        this.setThrowableHeading(f, f1, f2, velocity, inaccuracy);
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;
        if (!shooter.onGround) {
            this.motionY += shooter.motionY;
        }
    }

    @Override
    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
        float f = MathHelper.sqrt(x * x + y * y + z * z);
        x /= (double)f;
        y /= (double)f;
        z /= (double)f;
        x += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        y += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        z += this.rand.nextGaussian() * (double)0.0075f * (double)inaccuracy;
        this.motionX = x *= (double)velocity;
        this.motionY = y *= (double)velocity;
        this.motionZ = z *= (double)velocity;
        float f1 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(y, f1) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }

    @Override
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
        if (Celestial.instance.featureManager.getFeatureByClass(PearlESP.class).getState()) {
            PearlESP.handleEntityPrediction(this);
        }
    }

    @Override
    public void onUpdate() {
        AxisAlignedBB axisalignedbb;
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
        BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() != Material.AIR && (axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos)) != Block.NULL_AABB && axisalignedbb.offset(blockpos).isVecInside(new Vec3d(this.posX, this.posY, this.posZ))) {
            this.inGround = true;
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            int j = block.getMetaFromState(iblockstate);
            if (!(block == this.inTile && j == this.inData || this.world.collidesWithAnyBlock(this.getEntityBoundingBox().expandXyz(0.05)))) {
                this.inGround = false;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2f);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2f);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            } else {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            }
            ++this.timeInGround;
        } else {
            Entity entity;
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (raytraceresult != null) {
                vec3d = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }
            if ((entity = this.findEntityOnPath(vec3d1, vec3d)) != null) {
                raytraceresult = new RayTraceResult(entity);
            }
            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;
                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }
            if (raytraceresult != null) {
                this.onHit(raytraceresult);
            }
            if (this.getIsCritical()) {
                for (int k = 0; k < 4; ++k) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)k / 4.0, this.posY + this.motionY * (double)k / 4.0, this.posZ + this.motionZ * (double)k / 4.0, -this.motionX, -this.motionY + 0.2, -this.motionZ, new int[0]);
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f4) * 57.29577951308232);
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
            float f1 = 0.99f;
            float f2 = 0.05f;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    float f3 = 0.25f;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25, this.posY - this.motionY * 0.25, this.posZ - this.motionZ * 0.25, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                f1 = 0.6f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= (double)f1;
            this.motionY *= (double)f1;
            this.motionZ *= (double)f1;
            if (!this.hasNoGravity()) {
                this.motionY -= (double)0.05f;
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;
        if (entity != null) {
            DamageSource damagesource;
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double)f * this.damage);
            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }
            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            } else {
                Speed.isDamagedByBow = true;
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }
            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }
            if (entity.attackEntityFrom(damagesource, i)) {
                if (entity instanceof EntityLivingBase) {
                    float f1;
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                    if (!this.world.isRemote) {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }
                    if (this.knockbackStrength > 0 && (f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 0.0f) {
                        entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * (double)0.6f / (double)f1, 0.1, this.motionZ * (double)this.knockbackStrength * (double)0.6f / (double)f1);
                    }
                    if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                    }
                    this.arrowHit(entitylivingbase);
                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0f));
                    }
                }
                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                if (!(entity instanceof EntityEnderman)) {
                    this.setDead();
                }
            } else {
                this.motionX *= (double)-0.1f;
                this.motionY *= (double)-0.1f;
                this.motionZ *= (double)-0.1f;
                this.rotationYaw += 180.0f;
                this.prevRotationYaw += 180.0f;
                this.ticksInAir = 0;
                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < (double)0.001f) {
                    if (this.pickupStatus == PickupStatus.ALLOWED) {
                        this.entityDropItem(this.getArrowStack(), 0.1f);
                    }
                    this.setDead();
                }
            }
        } else {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            this.xTile = blockpos.getX();
            this.yTile = blockpos.getY();
            this.zTile = blockpos.getZ();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.inTile = iblockstate.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockstate);
            this.motionX = (float)(raytraceResultIn.hitVec.x - this.posX);
            this.motionY = (float)(raytraceResultIn.hitVec.y - this.posY);
            this.motionZ = (float)(raytraceResultIn.hitVec.z - this.posZ);
            float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / (double)f2 * (double)0.05f;
            this.posY -= this.motionY / (double)f2 * (double)0.05f;
            this.posZ -= this.motionZ / (double)f2 * (double)0.05f;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);
            if (iblockstate.getMaterial() != Material.AIR) {
                this.inTile.onEntityCollidedWithBlock(this.world, blockpos, iblockstate, this);
            }
        }
    }

    @Override
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
        super.moveEntity(x, p_70091_2_, p_70091_4_, p_70091_6_);
        if (this.inGround) {
            this.xTile = MathHelper.floor(this.posX);
            this.yTile = MathHelper.floor(this.posY);
            this.zTile = MathHelper.floor(this.posZ);
        }
    }

    protected void arrowHit(EntityLivingBase living) {
    }

    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end) {
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expandXyz(1.0), ARROW_TARGETS);
        double d0 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            double d1;
            AxisAlignedBB axisalignedbb;
            RayTraceResult raytraceresult;
            Entity entity1 = list.get(i);
            if (entity1 == this.shootingEntity && this.ticksInAir < 5 || (raytraceresult = (axisalignedbb = entity1.getEntityBoundingBox().expandXyz(0.3f)).calculateIntercept(start, end)) == null || !((d1 = start.squareDistanceTo(raytraceresult.hitVec)) < d0) && d0 != 0.0) continue;
            entity = entity1;
            d0 = d1;
        }
        return entity;
    }

    public static void registerFixesArrow(DataFixer fixer, String name) {
    }

    public static void registerFixesArrow(DataFixer fixer) {
        EntityArrow.registerFixesArrow(fixer, "Arrow");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        compound.setShort("life", (short)this.ticksInGround);
        ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
        compound.setByte("inData", (byte)this.inData);
        compound.setByte("shake", (byte)this.arrowShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
        compound.setDouble("damage", this.damage);
        compound.setBoolean("crit", this.getIsCritical());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        this.ticksInGround = compound.getShort("life");
        this.inTile = compound.hasKey("inTile", 8) ? Block.getBlockFromName(compound.getString("inTile")) : Block.getBlockById(compound.getByte("inTile") & 0xFF);
        this.inData = compound.getByte("inData") & 0xFF;
        this.arrowShake = compound.getByte("shake") & 0xFF;
        boolean bl = this.inGround = compound.getByte("inGround") == 1;
        if (compound.hasKey("damage", 99)) {
            this.damage = compound.getDouble("damage");
        }
        if (compound.hasKey("pickup", 99)) {
            this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
        } else if (compound.hasKey("player", 99)) {
            this.pickupStatus = compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED;
        }
        this.setIsCritical(compound.getBoolean("crit"));
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag;
            boolean bl = flag = this.pickupStatus == PickupStatus.ALLOWED || this.pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode;
            if (this.pickupStatus == PickupStatus.ALLOWED && !entityIn.inventory.addItemStackToInventory(this.getArrowStack())) {
                flag = false;
            }
            if (flag) {
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    protected abstract ItemStack getArrowStack();

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDamage(double damageIn) {
        this.damage = damageIn;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return 0.0f;
    }

    public void setIsCritical(boolean critical) {
        byte b0 = this.dataManager.get(CRITICAL);
        if (critical) {
            this.dataManager.set(CRITICAL, (byte)(b0 | 1));
        } else {
            this.dataManager.set(CRITICAL, (byte)(b0 & 0xFFFFFFFE));
        }
    }

    public boolean getIsCritical() {
        byte b0 = this.dataManager.get(CRITICAL);
        return (b0 & 1) != 0;
    }

    public void func_190547_a(EntityLivingBase p_190547_1_, float p_190547_2_) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        this.setDamage((double)(p_190547_2_ * 2.0f) + this.rand.nextGaussian() * 0.25 + (double)((float)this.world.getDifficulty().getDifficultyId() * 0.11f));
        if (i > 0) {
            this.setDamage(this.getDamage() + (double)i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockbackStrength(j);
        }
        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0) {
            this.setFire(100);
        }
    }

    public static enum PickupStatus {
        DISALLOWED,
        ALLOWED,
        CREATIVE_ONLY;


        public static PickupStatus getByOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > PickupStatus.values().length) {
                ordinal = 0;
            }
            return PickupStatus.values()[ordinal];
        }
    }
}

