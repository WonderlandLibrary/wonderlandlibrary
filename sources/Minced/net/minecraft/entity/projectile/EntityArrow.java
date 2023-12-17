// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.network.datasync.DataParameter;
import com.google.common.base.Predicate;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public abstract class EntityArrow extends Entity implements IProjectile
{
    private static final Predicate<Entity> ARROW_TARGETS;
    private static final DataParameter<Byte> CRITICAL;
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    protected boolean inGround;
    protected int timeInGround;
    public PickupStatus pickupStatus;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;
    
    public EntityArrow(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.damage = 2.0;
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityArrow(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }
    
    public EntityArrow(final World worldIn, final EntityLivingBase shooter) {
        this(worldIn, shooter.posX, shooter.posY + shooter.getEyeHeight() - 0.10000000149011612, shooter.posZ);
        this.shootingEntity = shooter;
        if (shooter instanceof EntityPlayer) {
            this.pickupStatus = PickupStatus.ALLOWED;
        }
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 10.0;
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        d0 = d0 * 64.0 * getRenderDistanceWeight();
        return distance < d0 * d0;
    }
    
    @Override
    protected void entityInit() {
        this.dataManager.register(EntityArrow.CRITICAL, (Byte)0);
    }
    
    public void shoot(final Entity shooter, final float pitch, final float yaw, final float p_184547_4_, final float velocity, final float inaccuracy) {
        final float f = -MathHelper.sin(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f);
        final float f2 = -MathHelper.sin(pitch * 0.017453292f);
        final float f3 = MathHelper.cos(yaw * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f);
        this.shoot(f, f2, f3, velocity, inaccuracy);
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;
        if (!shooter.onGround) {
            this.motionY += shooter.motionY;
        }
    }
    
    @Override
    public void shoot(double x, double y, double z, final float velocity, final float inaccuracy) {
        final float f = MathHelper.sqrt(x * x + y * y + z * z);
        x /= f;
        y /= f;
        z /= f;
        x += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        y += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        z += this.rand.nextGaussian() * 0.007499999832361937 * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        final float f2 = MathHelper.sqrt(x * x + z * z);
        this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
        this.rotationPitch = (float)(MathHelper.atan2(y, f2) * 57.29577951308232);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setPositionAndRotationDirect(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * 57.29577951308232);
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * 57.29577951308232);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 57.29577951308232);
            this.prevRotationYaw = this.rotationYaw;
            this.prevRotationPitch = this.rotationPitch;
        }
        final BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() != Material.AIR) {
            final AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);
            if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            final int j = block.getMetaFromState(iblockstate);
            if ((block != this.inTile || j != this.inData) && !this.world.collidesWithAnyBlock(this.getEntityBoundingBox().grow(0.05))) {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
            else {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            }
            ++this.timeInGround;
        }
        else {
            this.timeInGround = 0;
            ++this.ticksInAir;
            Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);
            vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3d2 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (raytraceresult != null) {
                vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
            }
            final Entity entity = this.findEntityOnPath(vec3d1, vec3d2);
            if (entity != null) {
                raytraceresult = new RayTraceResult(entity);
            }
            if (raytraceresult != null && raytraceresult.entityHit instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)raytraceresult.entityHit;
                if (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)) {
                    raytraceresult = null;
                }
            }
            if (raytraceresult != null) {
                this.onHit(raytraceresult);
            }
            if (this.getIsCritical()) {
                for (int k = 0; k < 4; ++k) {
                    this.world.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0, this.posY + this.motionY * k / 4.0, this.posZ + this.motionZ * k / 4.0, -this.motionX, -this.motionY + 0.2, -this.motionZ, new int[0]);
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 57.29577951308232);
            this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f2) * 57.29577951308232);
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
            float f3 = 0.99f;
            final float f4 = 0.05f;
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    final float f5 = 0.25f;
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25, this.posY - this.motionY * 0.25, this.posZ - this.motionZ * 0.25, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                f3 = 0.6f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= f3;
            this.motionY *= f3;
            this.motionZ *= f3;
            if (!this.hasNoGravity()) {
                this.motionY -= 0.05000000074505806;
            }
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }
    
    protected void onHit(final RayTraceResult raytraceResultIn) {
        final Entity entity = raytraceResultIn.entityHit;
        if (entity != null) {
            final float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil(f * this.damage);
            if (this.getIsCritical()) {
                i += this.rand.nextInt(i / 2 + 2);
            }
            DamageSource damagesource;
            if (this.shootingEntity == null) {
                damagesource = DamageSource.causeArrowDamage(this, this);
            }
            else {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }
            if (this.isBurning() && !(entity instanceof EntityEnderman)) {
                entity.setFire(5);
            }
            if (entity.attackEntityFrom(damagesource, (float)i)) {
                if (entity instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                    if (!this.world.isRemote) {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }
                    if (this.knockbackStrength > 0) {
                        final float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        if (f2 > 0.0f) {
                            entitylivingbase.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579 / f2, 0.1, this.motionZ * this.knockbackStrength * 0.6000000238418579 / f2);
                        }
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
            }
            else {
                this.motionX *= -0.10000000149011612;
                this.motionY *= -0.10000000149011612;
                this.motionZ *= -0.10000000149011612;
                this.rotationYaw += 180.0f;
                this.prevRotationYaw += 180.0f;
                this.ticksInAir = 0;
                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513) {
                    if (this.pickupStatus == PickupStatus.ALLOWED) {
                        this.entityDropItem(this.getArrowStack(), 0.1f);
                    }
                    this.setDead();
                }
            }
        }
        else {
            final BlockPos blockpos = raytraceResultIn.getBlockPos();
            this.xTile = blockpos.getX();
            this.yTile = blockpos.getY();
            this.zTile = blockpos.getZ();
            final IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.inTile = iblockstate.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockstate);
            this.motionX = (float)(raytraceResultIn.hitVec.x - this.posX);
            this.motionY = (float)(raytraceResultIn.hitVec.y - this.posY);
            this.motionZ = (float)(raytraceResultIn.hitVec.z - this.posZ);
            final float f3 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / f3 * 0.05000000074505806;
            this.posY -= this.motionY / f3 * 0.05000000074505806;
            this.posZ -= this.motionZ / f3 * 0.05000000074505806;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);
            if (iblockstate.getMaterial() != Material.AIR) {
                this.inTile.onEntityCollision(this.world, blockpos, iblockstate, this);
            }
        }
    }
    
    @Override
    public void move(final MoverType type, final double x, final double y, final double z) {
        super.move(type, x, y, z);
        if (this.inGround) {
            this.xTile = MathHelper.floor(this.posX);
            this.yTile = MathHelper.floor(this.posY);
            this.zTile = MathHelper.floor(this.posZ);
        }
    }
    
    protected void arrowHit(final EntityLivingBase living) {
    }
    
    @Nullable
    protected Entity findEntityOnPath(final Vec3d start, final Vec3d end) {
        Entity entity = null;
        final List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0), EntityArrow.ARROW_TARGETS);
        double d0 = 0.0;
        for (int i = 0; i < list.size(); ++i) {
            final Entity entity2 = list.get(i);
            if (entity2 != this.shootingEntity || this.ticksInAir >= 5) {
                final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().grow(0.30000001192092896);
                final RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);
                if (raytraceresult != null) {
                    final double d2 = start.squareDistanceTo(raytraceresult.hitVec);
                    if (d2 < d0 || d0 == 0.0) {
                        entity = entity2;
                        d0 = d2;
                    }
                }
            }
        }
        return entity;
    }
    
    public static void registerFixesArrow(final DataFixer fixer, final String name) {
    }
    
    public static void registerFixesArrow(final DataFixer fixer) {
        registerFixesArrow(fixer, "Arrow");
    }
    
    public void writeEntityToNBT(final NBTTagCompound compound) {
        compound.setInteger("xTile", this.xTile);
        compound.setInteger("yTile", this.yTile);
        compound.setInteger("zTile", this.zTile);
        compound.setShort("life", (short)this.ticksInGround);
        final ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(this.inTile);
        compound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        compound.setByte("inData", (byte)this.inData);
        compound.setByte("shake", (byte)this.arrowShake);
        compound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        compound.setByte("pickup", (byte)this.pickupStatus.ordinal());
        compound.setDouble("damage", this.damage);
        compound.setBoolean("crit", this.getIsCritical());
    }
    
    public void readEntityFromNBT(final NBTTagCompound compound) {
        this.xTile = compound.getInteger("xTile");
        this.yTile = compound.getInteger("yTile");
        this.zTile = compound.getInteger("zTile");
        this.ticksInGround = compound.getShort("life");
        if (compound.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(compound.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(compound.getByte("inTile") & 0xFF);
        }
        this.inData = (compound.getByte("inData") & 0xFF);
        this.arrowShake = (compound.getByte("shake") & 0xFF);
        this.inGround = (compound.getByte("inGround") == 1);
        if (compound.hasKey("damage", 99)) {
            this.damage = compound.getDouble("damage");
        }
        if (compound.hasKey("pickup", 99)) {
            this.pickupStatus = PickupStatus.getByOrdinal(compound.getByte("pickup"));
        }
        else if (compound.hasKey("player", 99)) {
            this.pickupStatus = (compound.getBoolean("player") ? PickupStatus.ALLOWED : PickupStatus.DISALLOWED);
        }
        this.setIsCritical(compound.getBoolean("crit"));
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.world.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.pickupStatus == PickupStatus.ALLOWED || (this.pickupStatus == PickupStatus.CREATIVE_ONLY && entityIn.capabilities.isCreativeMode);
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
    
    public void setDamage(final double damageIn) {
        this.damage = damageIn;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public void setKnockbackStrength(final int knockbackStrengthIn) {
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
    
    public void setIsCritical(final boolean critical) {
        final byte b0 = this.dataManager.get(EntityArrow.CRITICAL);
        if (critical) {
            this.dataManager.set(EntityArrow.CRITICAL, (byte)(b0 | 0x1));
        }
        else {
            this.dataManager.set(EntityArrow.CRITICAL, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    public boolean getIsCritical() {
        final byte b0 = this.dataManager.get(EntityArrow.CRITICAL);
        return (b0 & 0x1) != 0x0;
    }
    
    public void setEnchantmentEffectsFromEntity(final EntityLivingBase p_190547_1_, final float p_190547_2_) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, p_190547_1_);
        final int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, p_190547_1_);
        this.setDamage(p_190547_2_ * 2.0f + this.rand.nextGaussian() * 0.25 + this.world.getDifficulty().getId() * 0.11f);
        if (i > 0) {
            this.setDamage(this.getDamage() + i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockbackStrength(j);
        }
        if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, p_190547_1_) > 0) {
            this.setFire(100);
        }
    }
    
    static {
        ARROW_TARGETS = Predicates.and(new Predicate[] { EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, (Predicate)new Predicate<Entity>() {
                public boolean apply(@Nullable final Entity p_apply_1_) {
                    return p_apply_1_.canBeCollidedWith();
                }
            } });
        CRITICAL = EntityDataManager.createKey(EntityArrow.class, DataSerializers.BYTE);
    }
    
    public enum PickupStatus
    {
        DISALLOWED, 
        ALLOWED, 
        CREATIVE_ONLY;
        
        public static PickupStatus getByOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal > values().length) {
                ordinal = 0;
            }
            return values()[ordinal];
        }
    }
}
