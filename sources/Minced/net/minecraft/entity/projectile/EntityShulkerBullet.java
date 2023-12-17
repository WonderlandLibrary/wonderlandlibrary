// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.WorldServer;
import net.minecraft.util.math.RayTraceResult;
import java.util.Iterator;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityShulkerBullet extends Entity
{
    private EntityLivingBase owner;
    private Entity target;
    @Nullable
    private EnumFacing direction;
    private int steps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID ownerUniqueId;
    private BlockPos ownerBlockPos;
    @Nullable
    private UUID targetUniqueId;
    private BlockPos targetBlockPos;
    
    public EntityShulkerBullet(final World worldIn) {
        super(worldIn);
        this.setSize(0.3125f, 0.3125f);
        this.noClip = true;
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
    
    public EntityShulkerBullet(final World worldIn, final double x, final double y, final double z, final double motionXIn, final double motionYIn, final double motionZIn) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.motionX = motionXIn;
        this.motionY = motionYIn;
        this.motionZ = motionZIn;
    }
    
    public EntityShulkerBullet(final World worldIn, final EntityLivingBase ownerIn, final Entity targetIn, final EnumFacing.Axis p_i46772_4_) {
        this(worldIn);
        this.owner = ownerIn;
        final BlockPos blockpos = new BlockPos(ownerIn);
        final double d0 = blockpos.getX() + 0.5;
        final double d2 = blockpos.getY() + 0.5;
        final double d3 = blockpos.getZ() + 0.5;
        this.setLocationAndAngles(d0, d2, d3, this.rotationYaw, this.rotationPitch);
        this.target = targetIn;
        this.direction = EnumFacing.UP;
        this.selectNextMoveDirection(p_i46772_4_);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        if (this.owner != null) {
            final BlockPos blockpos = new BlockPos(this.owner);
            final NBTTagCompound nbttagcompound = NBTUtil.createUUIDTag(this.owner.getUniqueID());
            nbttagcompound.setInteger("X", blockpos.getX());
            nbttagcompound.setInteger("Y", blockpos.getY());
            nbttagcompound.setInteger("Z", blockpos.getZ());
            compound.setTag("Owner", nbttagcompound);
        }
        if (this.target != null) {
            final BlockPos blockpos2 = new BlockPos(this.target);
            final NBTTagCompound nbttagcompound2 = NBTUtil.createUUIDTag(this.target.getUniqueID());
            nbttagcompound2.setInteger("X", blockpos2.getX());
            nbttagcompound2.setInteger("Y", blockpos2.getY());
            nbttagcompound2.setInteger("Z", blockpos2.getZ());
            compound.setTag("Target", nbttagcompound2);
        }
        if (this.direction != null) {
            compound.setInteger("Dir", this.direction.getIndex());
        }
        compound.setInteger("Steps", this.steps);
        compound.setDouble("TXD", this.targetDeltaX);
        compound.setDouble("TYD", this.targetDeltaY);
        compound.setDouble("TZD", this.targetDeltaZ);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
        this.steps = compound.getInteger("Steps");
        this.targetDeltaX = compound.getDouble("TXD");
        this.targetDeltaY = compound.getDouble("TYD");
        this.targetDeltaZ = compound.getDouble("TZD");
        if (compound.hasKey("Dir", 99)) {
            this.direction = EnumFacing.byIndex(compound.getInteger("Dir"));
        }
        if (compound.hasKey("Owner", 10)) {
            final NBTTagCompound nbttagcompound = compound.getCompoundTag("Owner");
            this.ownerUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound);
            this.ownerBlockPos = new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z"));
        }
        if (compound.hasKey("Target", 10)) {
            final NBTTagCompound nbttagcompound2 = compound.getCompoundTag("Target");
            this.targetUniqueId = NBTUtil.getUUIDFromTag(nbttagcompound2);
            this.targetBlockPos = new BlockPos(nbttagcompound2.getInteger("X"), nbttagcompound2.getInteger("Y"), nbttagcompound2.getInteger("Z"));
        }
    }
    
    @Override
    protected void entityInit() {
    }
    
    private void setDirection(@Nullable final EnumFacing directionIn) {
        this.direction = directionIn;
    }
    
    private void selectNextMoveDirection(@Nullable final EnumFacing.Axis p_184569_1_) {
        double d0 = 0.5;
        BlockPos blockpos;
        if (this.target == null) {
            blockpos = new BlockPos(this).down();
        }
        else {
            d0 = this.target.height * 0.5;
            blockpos = new BlockPos(this.target.posX, this.target.posY + d0, this.target.posZ);
        }
        double d2 = blockpos.getX() + 0.5;
        double d3 = blockpos.getY() + d0;
        double d4 = blockpos.getZ() + 0.5;
        EnumFacing enumfacing = null;
        if (blockpos.distanceSqToCenter(this.posX, this.posY, this.posZ) >= 4.0) {
            final BlockPos blockpos2 = new BlockPos(this);
            final List<EnumFacing> list = (List<EnumFacing>)Lists.newArrayList();
            if (p_184569_1_ != EnumFacing.Axis.X) {
                if (blockpos2.getX() < blockpos.getX() && this.world.isAirBlock(blockpos2.east())) {
                    list.add(EnumFacing.EAST);
                }
                else if (blockpos2.getX() > blockpos.getX() && this.world.isAirBlock(blockpos2.west())) {
                    list.add(EnumFacing.WEST);
                }
            }
            if (p_184569_1_ != EnumFacing.Axis.Y) {
                if (blockpos2.getY() < blockpos.getY() && this.world.isAirBlock(blockpos2.up())) {
                    list.add(EnumFacing.UP);
                }
                else if (blockpos2.getY() > blockpos.getY() && this.world.isAirBlock(blockpos2.down())) {
                    list.add(EnumFacing.DOWN);
                }
            }
            if (p_184569_1_ != EnumFacing.Axis.Z) {
                if (blockpos2.getZ() < blockpos.getZ() && this.world.isAirBlock(blockpos2.south())) {
                    list.add(EnumFacing.SOUTH);
                }
                else if (blockpos2.getZ() > blockpos.getZ() && this.world.isAirBlock(blockpos2.north())) {
                    list.add(EnumFacing.NORTH);
                }
            }
            enumfacing = EnumFacing.random(this.rand);
            if (list.isEmpty()) {
                for (int i = 5; !this.world.isAirBlock(blockpos2.offset(enumfacing)) && i > 0; enumfacing = EnumFacing.random(this.rand), --i) {}
            }
            else {
                enumfacing = list.get(this.rand.nextInt(list.size()));
            }
            d2 = this.posX + enumfacing.getXOffset();
            d3 = this.posY + enumfacing.getYOffset();
            d4 = this.posZ + enumfacing.getZOffset();
        }
        this.setDirection(enumfacing);
        final double d5 = d2 - this.posX;
        final double d6 = d3 - this.posY;
        final double d7 = d4 - this.posZ;
        final double d8 = MathHelper.sqrt(d5 * d5 + d6 * d6 + d7 * d7);
        if (d8 == 0.0) {
            this.targetDeltaX = 0.0;
            this.targetDeltaY = 0.0;
            this.targetDeltaZ = 0.0;
        }
        else {
            this.targetDeltaX = d5 / d8 * 0.15;
            this.targetDeltaY = d6 / d8 * 0.15;
            this.targetDeltaZ = d7 / d8 * 0.15;
        }
        this.isAirBorne = true;
        this.steps = 10 + this.rand.nextInt(5) * 10;
    }
    
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
        else {
            super.onUpdate();
            if (!this.world.isRemote) {
                if (this.target == null && this.targetUniqueId != null) {
                    for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, new AxisAlignedBB(this.targetBlockPos.add(-2, -2, -2), this.targetBlockPos.add(2, 2, 2)))) {
                        if (entitylivingbase.getUniqueID().equals(this.targetUniqueId)) {
                            this.target = entitylivingbase;
                            break;
                        }
                    }
                    this.targetUniqueId = null;
                }
                if (this.owner == null && this.ownerUniqueId != null) {
                    for (final EntityLivingBase entitylivingbase2 : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, new AxisAlignedBB(this.ownerBlockPos.add(-2, -2, -2), this.ownerBlockPos.add(2, 2, 2)))) {
                        if (entitylivingbase2.getUniqueID().equals(this.ownerUniqueId)) {
                            this.owner = entitylivingbase2;
                            break;
                        }
                    }
                    this.ownerUniqueId = null;
                }
                if (this.target == null || !this.target.isEntityAlive() || (this.target instanceof EntityPlayer && ((EntityPlayer)this.target).isSpectator())) {
                    if (!this.hasNoGravity()) {
                        this.motionY -= 0.04;
                    }
                }
                else {
                    this.targetDeltaX = MathHelper.clamp(this.targetDeltaX * 1.025, -1.0, 1.0);
                    this.targetDeltaY = MathHelper.clamp(this.targetDeltaY * 1.025, -1.0, 1.0);
                    this.targetDeltaZ = MathHelper.clamp(this.targetDeltaZ * 1.025, -1.0, 1.0);
                    this.motionX += (this.targetDeltaX - this.motionX) * 0.2;
                    this.motionY += (this.targetDeltaY - this.motionY) * 0.2;
                    this.motionZ += (this.targetDeltaZ - this.motionZ) * 0.2;
                }
                final RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, false, this.owner);
                if (raytraceresult != null) {
                    this.bulletHit(raytraceresult);
                }
            }
            this.setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            ProjectileHelper.rotateTowardsMovement(this, 0.5f);
            if (this.world.isRemote) {
                this.world.spawnParticle(EnumParticleTypes.END_ROD, this.posX - this.motionX, this.posY - this.motionY + 0.15, this.posZ - this.motionZ, 0.0, 0.0, 0.0, new int[0]);
            }
            else if (this.target != null && !this.target.isDead) {
                if (this.steps > 0) {
                    --this.steps;
                    if (this.steps == 0) {
                        this.selectNextMoveDirection((this.direction == null) ? null : this.direction.getAxis());
                    }
                }
                if (this.direction != null) {
                    final BlockPos blockpos = new BlockPos(this);
                    final EnumFacing.Axis enumfacing$axis = this.direction.getAxis();
                    if (this.world.isBlockNormalCube(blockpos.offset(this.direction), false)) {
                        this.selectNextMoveDirection(enumfacing$axis);
                    }
                    else {
                        final BlockPos blockpos2 = new BlockPos(this.target);
                        if ((enumfacing$axis == EnumFacing.Axis.X && blockpos.getX() == blockpos2.getX()) || (enumfacing$axis == EnumFacing.Axis.Z && blockpos.getZ() == blockpos2.getZ()) || (enumfacing$axis == EnumFacing.Axis.Y && blockpos.getY() == blockpos2.getY())) {
                            this.selectNextMoveDirection(enumfacing$axis);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isBurning() {
        return false;
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 16384.0;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
    
    protected void bulletHit(final RayTraceResult result) {
        if (result.entityHit == null) {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2, 0.2, 0.2, 0.0, new int[0]);
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0f, 1.0f);
        }
        else {
            final boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), 4.0f);
            if (flag) {
                this.applyEnchantments(this.owner, result.entityHit);
                if (result.entityHit instanceof EntityLivingBase) {
                    ((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 200));
                }
            }
        }
        this.setDead();
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (!this.world.isRemote) {
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0f, 1.0f);
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 15, 0.2, 0.2, 0.2, 0.0, new int[0]);
            this.setDead();
        }
        return true;
    }
}
