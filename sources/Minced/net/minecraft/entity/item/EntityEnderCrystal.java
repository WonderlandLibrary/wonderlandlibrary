// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import javax.annotation.Nullable;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.DamageSource;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Blocks;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import com.google.common.base.Optional;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.Entity;

public class EntityEnderCrystal extends Entity
{
    private static final DataParameter<Optional<BlockPos>> BEAM_TARGET;
    private static final DataParameter<Boolean> SHOW_BOTTOM;
    public int innerRotation;
    
    public EntityEnderCrystal(final World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(2.0f, 2.0f);
        this.innerRotation = this.rand.nextInt(100000);
    }
    
    public EntityEnderCrystal(final World worldIn, final double x, final double y, final double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.getDataManager().register(EntityEnderCrystal.BEAM_TARGET, (Optional<BlockPos>)Optional.absent());
        this.getDataManager().register(EntityEnderCrystal.SHOW_BOTTOM, true);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
        if (!this.world.isRemote) {
            final BlockPos blockpos = new BlockPos(this);
            if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(blockpos).getBlock() != Blocks.FIRE) {
                this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound compound) {
        if (this.getBeamTarget() != null) {
            compound.setTag("BeamTarget", NBTUtil.createPosTag(this.getBeamTarget()));
        }
        compound.setBoolean("ShowBottom", this.shouldShowBottom());
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound compound) {
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
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source.getTrueSource() instanceof EntityDragon) {
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
        this.onCrystalDestroyed(DamageSource.GENERIC);
        super.onKillCommand();
    }
    
    private void onCrystalDestroyed(final DamageSource source) {
        if (this.world.provider instanceof WorldProviderEnd) {
            final WorldProviderEnd worldproviderend = (WorldProviderEnd)this.world.provider;
            final DragonFightManager dragonfightmanager = worldproviderend.getDragonFightManager();
            if (dragonfightmanager != null) {
                dragonfightmanager.onCrystalDestroyed(this, source);
            }
        }
    }
    
    public void setBeamTarget(@Nullable final BlockPos beamTarget) {
        this.getDataManager().set(EntityEnderCrystal.BEAM_TARGET, (Optional<BlockPos>)Optional.fromNullable((Object)beamTarget));
    }
    
    @Nullable
    public BlockPos getBeamTarget() {
        return (BlockPos)this.getDataManager().get(EntityEnderCrystal.BEAM_TARGET).orNull();
    }
    
    public void setShowBottom(final boolean showBottom) {
        this.getDataManager().set(EntityEnderCrystal.SHOW_BOTTOM, showBottom);
    }
    
    public boolean shouldShowBottom() {
        return this.getDataManager().get(EntityEnderCrystal.SHOW_BOTTOM);
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return super.isInRangeToRenderDist(distance) || this.getBeamTarget() != null;
    }
    
    static {
        BEAM_TARGET = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS);
        SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);
    }
}
