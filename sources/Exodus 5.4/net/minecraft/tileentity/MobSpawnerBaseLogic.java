/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public abstract class MobSpawnerBaseLogic {
    private final List<WeightedRandomMinecart> minecartToSpawn = Lists.newArrayList();
    private int spawnRange = 4;
    private int spawnDelay = 20;
    private WeightedRandomMinecart randomEntity;
    private int maxSpawnDelay = 800;
    private String mobID = "Pig";
    private Entity cachedEntity;
    private int minSpawnDelay = 200;
    private int activatingRangeFromPlayer = 16;
    private int maxNearbyEntities = 6;
    private double mobRotation;
    private double prevMobRotation;
    private int spawnCount = 4;

    public void setEntityName(String string) {
        this.mobID = string;
    }

    private WeightedRandomMinecart getRandomEntity() {
        return this.randomEntity;
    }

    private boolean isActivated() {
        BlockPos blockPos = this.getSpawnerPosition();
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, this.activatingRangeFromPlayer);
    }

    public void setRandomEntity(WeightedRandomMinecart weightedRandomMinecart) {
        this.randomEntity = weightedRandomMinecart;
    }

    public abstract void func_98267_a(int var1);

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
        this.mobID = nBTTagCompound.getString("EntityId");
        this.spawnDelay = nBTTagCompound.getShort("Delay");
        this.minecartToSpawn.clear();
        if (nBTTagCompound.hasKey("SpawnPotentials", 9)) {
            NBTTagList nBTTagList = nBTTagCompound.getTagList("SpawnPotentials", 10);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                this.minecartToSpawn.add(new WeightedRandomMinecart(nBTTagList.getCompoundTagAt(n)));
                ++n;
            }
        }
        if (nBTTagCompound.hasKey("SpawnData", 10)) {
            this.setRandomEntity(new WeightedRandomMinecart(nBTTagCompound.getCompoundTag("SpawnData"), this.mobID));
        } else {
            this.setRandomEntity(null);
        }
        if (nBTTagCompound.hasKey("MinSpawnDelay", 99)) {
            this.minSpawnDelay = nBTTagCompound.getShort("MinSpawnDelay");
            this.maxSpawnDelay = nBTTagCompound.getShort("MaxSpawnDelay");
            this.spawnCount = nBTTagCompound.getShort("SpawnCount");
        }
        if (nBTTagCompound.hasKey("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = nBTTagCompound.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = nBTTagCompound.getShort("RequiredPlayerRange");
        }
        if (nBTTagCompound.hasKey("SpawnRange", 99)) {
            this.spawnRange = nBTTagCompound.getShort("SpawnRange");
        }
        if (this.getSpawnerWorld() != null) {
            this.cachedEntity = null;
        }
    }

    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        } else {
            int n = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(n);
        }
        if (this.minecartToSpawn.size() > 0) {
            this.setRandomEntity(WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }
        this.func_98267_a(1);
    }

    public abstract BlockPos getSpawnerPosition();

    public double getMobRotation() {
        return this.mobRotation;
    }

    public Entity func_180612_a(World world) {
        Entity entity;
        if (this.cachedEntity == null && (entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), world)) != null) {
            this.cachedEntity = entity = this.spawnNewEntity(entity, false);
        }
        return this.cachedEntity;
    }

    private Entity spawnNewEntity(Entity entity, boolean bl) {
        if (this.getRandomEntity() != null) {
            Object object;
            NBTTagCompound object2 = new NBTTagCompound();
            entity.writeToNBTOptional(object2);
            for (String object3 : this.getRandomEntity().nbtData.getKeySet()) {
                object = this.getRandomEntity().nbtData.getTag(object3);
                object2.setTag(object3, ((NBTBase)object).copy());
            }
            entity.readFromNBT(object2);
            if (entity.worldObj != null && bl) {
                entity.worldObj.spawnEntityInWorld(entity);
            }
            Object object4 = entity;
            while (object2.hasKey("Riding", 10)) {
                NBTTagCompound nBTTagCompound = object2.getCompoundTag("Riding");
                object = EntityList.createEntityByName(nBTTagCompound.getString("id"), entity.worldObj);
                if (object != null) {
                    NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
                    ((Entity)object).writeToNBTOptional(nBTTagCompound2);
                    for (String string : nBTTagCompound.getKeySet()) {
                        NBTBase nBTBase = nBTTagCompound.getTag(string);
                        nBTTagCompound2.setTag(string, nBTBase.copy());
                    }
                    ((Entity)object).readFromNBT(nBTTagCompound2);
                    ((Entity)object).setLocationAndAngles(((Entity)object4).posX, ((Entity)object4).posY, ((Entity)object4).posZ, ((Entity)object4).rotationYaw, ((Entity)object4).rotationPitch);
                    if (entity.worldObj != null && bl) {
                        entity.worldObj.spawnEntityInWorld((Entity)object);
                    }
                    ((Entity)object4).mountEntity((Entity)object);
                }
                object4 = object;
                object2 = nBTTagCompound;
            }
        } else if (entity instanceof EntityLivingBase && entity.worldObj != null && bl) {
            if (entity instanceof EntityLiving) {
                ((EntityLiving)entity).onInitialSpawn(entity.worldObj.getDifficultyForLocation(new BlockPos(entity)), null);
            }
            entity.worldObj.spawnEntityInWorld(entity);
        }
        return entity;
    }

    public void updateSpawner() {
        if (this.isActivated()) {
            BlockPos blockPos = this.getSpawnerPosition();
            if (this.getSpawnerWorld().isRemote) {
                double d = (float)blockPos.getX() + this.getSpawnerWorld().rand.nextFloat();
                double d2 = (float)blockPos.getY() + this.getSpawnerWorld().rand.nextFloat();
                double d3 = (float)blockPos.getZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d, d2, d3, 0.0, 0.0, 0.0, new int[0]);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0f / ((float)this.spawnDelay + 200.0f))) % 360.0;
            } else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean bl = false;
                int n = 0;
                while (n < this.spawnCount) {
                    Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                    if (entity == null) {
                        return;
                    }
                    int n2 = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
                    if (n2 >= this.maxNearbyEntities) {
                        this.resetTimer();
                        return;
                    }
                    double d = (double)blockPos.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5;
                    double d4 = blockPos.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1;
                    double d5 = (double)blockPos.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5;
                    EntityLiving entityLiving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(d, d4, d5, this.getSpawnerWorld().rand.nextFloat() * 360.0f, 0.0f);
                    if (entityLiving == null || entityLiving.getCanSpawnHere() && entityLiving.isNotColliding()) {
                        this.spawnNewEntity(entity, true);
                        this.getSpawnerWorld().playAuxSFX(2004, blockPos, 0);
                        if (entityLiving != null) {
                            entityLiving.spawnExplosionParticle();
                        }
                        bl = true;
                    }
                    ++n;
                }
                if (bl) {
                    this.resetTimer();
                }
            }
        }
    }

    public double getPrevMobRotation() {
        return this.prevMobRotation;
    }

    public boolean setDelayToMin(int n) {
        if (n == 1 && this.getSpawnerWorld().isRemote) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }

    public void writeToNBT(NBTTagCompound nBTTagCompound) {
        String string = this.getEntityNameToSpawn();
        if (!StringUtils.isNullOrEmpty(string)) {
            nBTTagCompound.setString("EntityId", string);
            nBTTagCompound.setShort("Delay", (short)this.spawnDelay);
            nBTTagCompound.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
            nBTTagCompound.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
            nBTTagCompound.setShort("SpawnCount", (short)this.spawnCount);
            nBTTagCompound.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
            nBTTagCompound.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
            nBTTagCompound.setShort("SpawnRange", (short)this.spawnRange);
            if (this.getRandomEntity() != null) {
                nBTTagCompound.setTag("SpawnData", this.getRandomEntity().nbtData.copy());
            }
            if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
                NBTTagList nBTTagList = new NBTTagList();
                if (this.minecartToSpawn.size() > 0) {
                    for (WeightedRandomMinecart weightedRandomMinecart : this.minecartToSpawn) {
                        nBTTagList.appendTag(weightedRandomMinecart.toNBT());
                    }
                } else {
                    nBTTagList.appendTag(this.getRandomEntity().toNBT());
                }
                nBTTagCompound.setTag("SpawnPotentials", nBTTagList);
            }
        }
    }

    public abstract World getSpawnerWorld();

    private String getEntityNameToSpawn() {
        if (this.getRandomEntity() == null) {
            if (this.mobID != null && this.mobID.equals("Minecart")) {
                this.mobID = "MinecartRideable";
            }
            return this.mobID;
        }
        return this.getRandomEntity().entityType;
    }

    public class WeightedRandomMinecart
    extends WeightedRandom.Item {
        private final NBTTagCompound nbtData;
        private final String entityType;

        public WeightedRandomMinecart(NBTTagCompound nBTTagCompound, String string) {
            this(nBTTagCompound, string, 1);
        }

        public WeightedRandomMinecart(NBTTagCompound nBTTagCompound) {
            this(nBTTagCompound.getCompoundTag("Properties"), nBTTagCompound.getString("Type"), nBTTagCompound.getInteger("Weight"));
        }

        public NBTTagCompound toNBT() {
            NBTTagCompound nBTTagCompound = new NBTTagCompound();
            nBTTagCompound.setTag("Properties", this.nbtData);
            nBTTagCompound.setString("Type", this.entityType);
            nBTTagCompound.setInteger("Weight", this.itemWeight);
            return nBTTagCompound;
        }

        private WeightedRandomMinecart(NBTTagCompound nBTTagCompound, String string, int n) {
            super(n);
            if (string.equals("Minecart")) {
                string = nBTTagCompound != null ? EntityMinecart.EnumMinecartType.byNetworkID(nBTTagCompound.getInteger("Type")).getName() : "MinecartRideable";
            }
            this.nbtData = nBTTagCompound;
            this.entityType = string;
        }
    }
}

