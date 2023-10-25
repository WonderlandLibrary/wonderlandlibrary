/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;

public class EntityEnderCrystal
extends Entity {
    private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);
    public int innerRotation;

    public EntityEnderCrystal(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0f, 2.0f);
        this.innerRotation = this.rand.nextInt(100000);
    }

    public EntityEnderCrystal(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.getDataManager().register(BEAM_TARGET, Optional.absent());
        this.getDataManager().register(SHOW_BOTTOM, true);
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
        if (!this.world.isRemote) {
            BlockPos blockpos = new BlockPos(this);
            if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(blockpos).getBlock() != Blocks.FIRE) {
                this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (this.getBeamTarget() != null) {
            compound.setTag("BeamTarget", NBTUtil.createPosTag(this.getBeamTarget()));
        }
        compound.setBoolean("ShowBottom", this.shouldShowBottom());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey("BeamTarget", 10)) {
            this.setBeamTarget(NBTUtil.getPosFromTag(compound.getCompoundTag("BeamTarget")));
        }
        if (compound.hasKey("ShowBottom", 1)) {
            this.setShowBottom(compound.getBoolean("ShowBottom"));
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getEntity() instanceof EntityDragon) {
            return false;
        }
        if (!this.isDead && !this.world.isRemote) {
            this.setDead();
            if (!this.world.isRemote) {
                if (!source.isExplosion()) {
                    this.world.createExplosion(null, this.posX, this.posY, this.posZ, 6.0f, true);
                }
                this.onCrystalDestroyed(source);
            }
        }
        return true;
    }

    @Override
    public void onKillCommand() {
        this.onCrystalDestroyed(DamageSource.generic);
        super.onKillCommand();
    }

    private void onCrystalDestroyed(DamageSource source) {
        WorldProviderEnd worldproviderend;
        DragonFightManager dragonfightmanager;
        if (this.world.provider instanceof WorldProviderEnd && (dragonfightmanager = (worldproviderend = (WorldProviderEnd)this.world.provider).getDragonFightManager()) != null) {
            dragonfightmanager.onCrystalDestroyed(this, source);
        }
    }

    public void setBeamTarget(@Nullable BlockPos beamTarget) {
        this.getDataManager().set(BEAM_TARGET, Optional.fromNullable(beamTarget));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return this.getDataManager().get(BEAM_TARGET).orNull();
    }

    public void setShowBottom(boolean showBottom) {
        this.getDataManager().set(SHOW_BOTTOM, showBottom);
    }

    public boolean shouldShowBottom() {
        return this.getDataManager().get(SHOW_BOTTOM);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return super.isInRangeToRenderDist(distance) || this.getBeamTarget() != null;
    }
}

