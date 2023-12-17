// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import java.util.Iterator;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.util.StringUtils;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.WeightedSpawnerEntity;
import java.util.List;

public abstract class MobSpawnerBaseLogic
{
    public int spawnDelay;
    private final List<WeightedSpawnerEntity> potentialSpawns;
    private WeightedSpawnerEntity spawnData;
    private double mobRotation;
    private double prevMobRotation;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    public int spawnCount;
    private Entity cachedEntity;
    private int maxNearbyEntities;
    private int activatingRangeFromPlayer;
    private int spawnRange;
    
    public MobSpawnerBaseLogic() {
        this.spawnDelay = 20;
        this.potentialSpawns = (List<WeightedSpawnerEntity>)Lists.newArrayList();
        this.spawnData = new WeightedSpawnerEntity();
        this.minSpawnDelay = 200;
        this.maxSpawnDelay = 800;
        this.spawnCount = 4;
        this.maxNearbyEntities = 6;
        this.activatingRangeFromPlayer = 16;
        this.spawnRange = 4;
    }
    
    @Nullable
    private ResourceLocation getEntityId() {
        final String s = this.spawnData.getNbt().getString("id");
        return StringUtils.isNullOrEmpty(s) ? null : new ResourceLocation(s);
    }
    
    public void setEntityId(@Nullable final ResourceLocation id) {
        if (id != null) {
            this.spawnData.getNbt().setString("id", id.toString());
        }
    }
    
    private boolean isActivated() {
        final BlockPos blockpos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5, this.activatingRangeFromPlayer);
    }
    
    public void updateSpawner() {
        if (!this.isActivated()) {
            this.prevMobRotation = this.mobRotation;
        }
        else {
            final BlockPos blockpos = this.getSpawnerPosition();
            if (this.getSpawnerWorld().isRemote) {
                final double d3 = blockpos.getX() + this.getSpawnerWorld().rand.nextFloat();
                final double d4 = blockpos.getY() + this.getSpawnerWorld().rand.nextFloat();
                final double d5 = blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0, 0.0, 0.0, new int[0]);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + 1000.0f / (this.spawnDelay + 200.0f)) % 360.0;
            }
            else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean flag = false;
                for (int i = 0; i < this.spawnCount; ++i) {
                    final NBTTagCompound nbttagcompound = this.spawnData.getNbt();
                    final NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                    final World world = this.getSpawnerWorld();
                    final int j = nbttaglist.tagCount();
                    final double d6 = (j >= 1) ? nbttaglist.getDoubleAt(0) : (blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * this.spawnRange + 0.5);
                    final double d7 = (j >= 2) ? nbttaglist.getDoubleAt(1) : (blockpos.getY() + world.rand.nextInt(3) - 1);
                    final double d8 = (j >= 3) ? nbttaglist.getDoubleAt(2) : (blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * this.spawnRange + 0.5);
                    final Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d6, d7, d8, false);
                    if (entity == null) {
                        return;
                    }
                    final int k = world.getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1).grow(this.spawnRange)).size();
                    if (k >= this.maxNearbyEntities) {
                        this.resetTimer();
                        return;
                    }
                    final EntityLiving entityliving = (entity instanceof EntityLiving) ? ((EntityLiving)entity) : null;
                    entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0f, 0.0f);
                    if (entityliving == null || (entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
                        if (this.spawnData.getNbt().getSize() == 1 && this.spawnData.getNbt().hasKey("id", 8) && entity instanceof EntityLiving) {
                            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                        }
                        AnvilChunkLoader.spawnEntity(entity, world);
                        world.playEvent(2004, blockpos, 0);
                        if (entityliving != null) {
                            entityliving.spawnExplosionParticle();
                        }
                        flag = true;
                    }
                }
                if (flag) {
                    this.resetTimer();
                }
            }
        }
    }
    
    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        }
        else {
            final int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }
        if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
        }
        this.broadcastEvent(1);
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        this.spawnDelay = nbt.getShort("Delay");
        this.potentialSpawns.clear();
        if (nbt.hasKey("SpawnPotentials", 9)) {
            final NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i)));
            }
        }
        if (nbt.hasKey("SpawnData", 10)) {
            this.setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompoundTag("SpawnData")));
        }
        else if (!this.potentialSpawns.isEmpty()) {
            this.setNextSpawnData(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
        }
        if (nbt.hasKey("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nbt.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            this.spawnCount = nbt.getShort("SpawnCount");
        }
        if (nbt.hasKey("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }
        if (nbt.hasKey("SpawnRange", 99)) {
            this.spawnRange = nbt.getShort("SpawnRange");
        }
        if (this.getSpawnerWorld() != null) {
            this.cachedEntity = null;
        }
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound p_189530_1_) {
        final ResourceLocation resourcelocation = this.getEntityId();
        if (resourcelocation == null) {
            return p_189530_1_;
        }
        p_189530_1_.setShort("Delay", (short)this.spawnDelay);
        p_189530_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
        p_189530_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        p_189530_1_.setShort("SpawnCount", (short)this.spawnCount);
        p_189530_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        p_189530_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
        p_189530_1_.setShort("SpawnRange", (short)this.spawnRange);
        p_189530_1_.setTag("SpawnData", this.spawnData.getNbt().copy());
        final NBTTagList nbttaglist = new NBTTagList();
        if (this.potentialSpawns.isEmpty()) {
            nbttaglist.appendTag(this.spawnData.toCompoundTag());
        }
        else {
            for (final WeightedSpawnerEntity weightedspawnerentity : this.potentialSpawns) {
                nbttaglist.appendTag(weightedspawnerentity.toCompoundTag());
            }
        }
        p_189530_1_.setTag("SpawnPotentials", nbttaglist);
        return p_189530_1_;
    }
    
    public Entity getCachedEntity() {
        if (this.cachedEntity == null) {
            this.cachedEntity = AnvilChunkLoader.readWorldEntity(this.spawnData.getNbt(), this.getSpawnerWorld(), false);
            if (this.spawnData.getNbt().getSize() == 1 && this.spawnData.getNbt().hasKey("id", 8) && this.cachedEntity instanceof EntityLiving) {
                ((EntityLiving)this.cachedEntity).onInitialSpawn(this.getSpawnerWorld().getDifficultyForLocation(new BlockPos(this.cachedEntity)), null);
            }
        }
        return this.cachedEntity;
    }
    
    public boolean setDelayToMin(final int delay) {
        if (delay == 1 && this.getSpawnerWorld().isRemote) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }
    
    public void setNextSpawnData(final WeightedSpawnerEntity p_184993_1_) {
        this.spawnData = p_184993_1_;
    }
    
    public abstract void broadcastEvent(final int p0);
    
    public abstract World getSpawnerWorld();
    
    public abstract BlockPos getSpawnerPosition();
    
    public double getMobRotation() {
        return this.mobRotation;
    }
    
    public double getPrevMobRotation() {
        return this.prevMobRotation;
    }
}
