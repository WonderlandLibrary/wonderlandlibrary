package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
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
  private int spawnDelay = 20;
  
  private String mobID = "Pig";
  
  private final List<WeightedRandomMinecart> minecartToSpawn = Lists.newArrayList();
  
  private WeightedRandomMinecart randomEntity;
  
  private double mobRotation;
  
  private double prevMobRotation;
  
  private int minSpawnDelay = 200;
  
  private int maxSpawnDelay = 800;
  
  private int spawnCount = 4;
  
  private Entity cachedEntity;
  
  private int maxNearbyEntities = 6;
  
  private int activatingRangeFromPlayer = 16;
  
  private int spawnRange = 4;
  
  private String getEntityNameToSpawn() {
    if (getRandomEntity() == null) {
      if (this.mobID != null && this.mobID.equals("Minecart"))
        this.mobID = "MinecartRideable"; 
      return this.mobID;
    } 
    return (getRandomEntity()).entityType;
  }
  
  public void setEntityName(String name) {
    this.mobID = name;
  }
  
  private boolean isActivated() {
    BlockPos blockpos = getSpawnerPosition();
    return getSpawnerWorld().isAnyPlayerWithinRangeAt(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, this.activatingRangeFromPlayer);
  }
  
  public void updateSpawner() {
    if (isActivated()) {
      BlockPos blockpos = getSpawnerPosition();
      if ((getSpawnerWorld()).isRemote) {
        double d3 = (blockpos.getX() + (getSpawnerWorld()).rand.nextFloat());
        double d4 = (blockpos.getY() + (getSpawnerWorld()).rand.nextFloat());
        double d5 = (blockpos.getZ() + (getSpawnerWorld()).rand.nextFloat());
        getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
        getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D, new int[0]);
        if (this.spawnDelay > 0)
          this.spawnDelay--; 
        this.prevMobRotation = this.mobRotation;
        this.mobRotation = (this.mobRotation + (1000.0F / (this.spawnDelay + 200.0F))) % 360.0D;
      } else {
        if (this.spawnDelay == -1)
          resetTimer(); 
        if (this.spawnDelay > 0) {
          this.spawnDelay--;
          return;
        } 
        boolean flag = false;
        for (int i = 0; i < this.spawnCount; i++) {
          Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), getSpawnerWorld());
          if (entity == null)
            return; 
          int j = getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), (blockpos.getX() + 1), (blockpos.getY() + 1), (blockpos.getZ() + 1))).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
          if (j >= this.maxNearbyEntities) {
            resetTimer();
            return;
          } 
          double d0 = blockpos.getX() + ((getSpawnerWorld()).rand.nextDouble() - (getSpawnerWorld()).rand.nextDouble()) * this.spawnRange + 0.5D;
          double d1 = (blockpos.getY() + (getSpawnerWorld()).rand.nextInt(3) - 1);
          double d2 = blockpos.getZ() + ((getSpawnerWorld()).rand.nextDouble() - (getSpawnerWorld()).rand.nextDouble()) * this.spawnRange + 0.5D;
          EntityLiving entityliving = (entity instanceof EntityLiving) ? (EntityLiving)entity : null;
          entity.setLocationAndAngles(d0, d1, d2, (getSpawnerWorld()).rand.nextFloat() * 360.0F, 0.0F);
          if (entityliving == null || (entityliving.getCanSpawnHere() && entityliving.isNotColliding())) {
            spawnNewEntity(entity, true);
            getSpawnerWorld().playAuxSFX(2004, blockpos, 0);
            if (entityliving != null)
              entityliving.spawnExplosionParticle(); 
            flag = true;
          } 
        } 
        if (flag)
          resetTimer(); 
      } 
    } 
  }
  
  private Entity spawnNewEntity(Entity entityIn, boolean spawn) {
    if (getRandomEntity() != null) {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      entityIn.writeToNBTOptional(nbttagcompound);
      for (String s : (getRandomEntity()).nbtData.getKeySet()) {
        NBTBase nbtbase = (getRandomEntity()).nbtData.getTag(s);
        nbttagcompound.setTag(s, nbtbase.copy());
      } 
      entityIn.readFromNBT(nbttagcompound);
      if (entityIn.worldObj != null && spawn)
        entityIn.worldObj.spawnEntityInWorld(entityIn); 
      for (Entity entity = entityIn; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2) {
        NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
        Entity entity1 = EntityList.createEntityByName(nbttagcompound2.getString("id"), entityIn.worldObj);
        if (entity1 != null) {
          NBTTagCompound nbttagcompound1 = new NBTTagCompound();
          entity1.writeToNBTOptional(nbttagcompound1);
          for (String s1 : nbttagcompound2.getKeySet()) {
            NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
            nbttagcompound1.setTag(s1, nbtbase1.copy());
          } 
          entity1.readFromNBT(nbttagcompound1);
          entity1.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
          if (entityIn.worldObj != null && spawn)
            entityIn.worldObj.spawnEntityInWorld(entity1); 
          entity.mountEntity(entity1);
        } 
        entity = entity1;
      } 
    } else if (entityIn instanceof net.minecraft.entity.EntityLivingBase && entityIn.worldObj != null && spawn) {
      if (entityIn instanceof EntityLiving)
        ((EntityLiving)entityIn).onInitialSpawn(entityIn.worldObj.getDifficultyForLocation(new BlockPos(entityIn)), (IEntityLivingData)null); 
      entityIn.worldObj.spawnEntityInWorld(entityIn);
    } 
    return entityIn;
  }
  
  private void resetTimer() {
    if (this.maxSpawnDelay <= this.minSpawnDelay) {
      this.spawnDelay = this.minSpawnDelay;
    } else {
      int i = this.maxSpawnDelay - this.minSpawnDelay;
      this.spawnDelay = this.minSpawnDelay + (getSpawnerWorld()).rand.nextInt(i);
    } 
    if (this.minecartToSpawn.size() > 0)
      setRandomEntity((WeightedRandomMinecart)WeightedRandom.getRandomItem((getSpawnerWorld()).rand, this.minecartToSpawn)); 
    func_98267_a(1);
  }
  
  public void readFromNBT(NBTTagCompound nbt) {
    this.mobID = nbt.getString("EntityId");
    this.spawnDelay = nbt.getShort("Delay");
    this.minecartToSpawn.clear();
    if (nbt.hasKey("SpawnPotentials", 9)) {
      NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++)
        this.minecartToSpawn.add(new WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i))); 
    } 
    if (nbt.hasKey("SpawnData", 10)) {
      setRandomEntity(new WeightedRandomMinecart(nbt.getCompoundTag("SpawnData"), this.mobID));
    } else {
      setRandomEntity((WeightedRandomMinecart)null);
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
    if (nbt.hasKey("SpawnRange", 99))
      this.spawnRange = nbt.getShort("SpawnRange"); 
    if (getSpawnerWorld() != null)
      this.cachedEntity = null; 
  }
  
  public void writeToNBT(NBTTagCompound nbt) {
    String s = getEntityNameToSpawn();
    if (!StringUtils.isNullOrEmpty(s)) {
      nbt.setString("EntityId", s);
      nbt.setShort("Delay", (short)this.spawnDelay);
      nbt.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
      nbt.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
      nbt.setShort("SpawnCount", (short)this.spawnCount);
      nbt.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
      nbt.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
      nbt.setShort("SpawnRange", (short)this.spawnRange);
      if (getRandomEntity() != null)
        nbt.setTag("SpawnData", (getRandomEntity()).nbtData.copy()); 
      if (getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
        NBTTagList nbttaglist = new NBTTagList();
        if (this.minecartToSpawn.size() > 0) {
          for (WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn)
            nbttaglist.appendTag((NBTBase)mobspawnerbaselogic$weightedrandomminecart.toNBT()); 
        } else {
          nbttaglist.appendTag((NBTBase)getRandomEntity().toNBT());
        } 
        nbt.setTag("SpawnPotentials", (NBTBase)nbttaglist);
      } 
    } 
  }
  
  public Entity func_180612_a(World worldIn) {
    if (this.cachedEntity == null) {
      Entity entity = EntityList.createEntityByName(getEntityNameToSpawn(), worldIn);
      if (entity != null) {
        entity = spawnNewEntity(entity, false);
        this.cachedEntity = entity;
      } 
    } 
    return this.cachedEntity;
  }
  
  public boolean setDelayToMin(int delay) {
    if (delay == 1 && (getSpawnerWorld()).isRemote) {
      this.spawnDelay = this.minSpawnDelay;
      return true;
    } 
    return false;
  }
  
  private WeightedRandomMinecart getRandomEntity() {
    return this.randomEntity;
  }
  
  public void setRandomEntity(WeightedRandomMinecart p_98277_1_) {
    this.randomEntity = p_98277_1_;
  }
  
  public abstract void func_98267_a(int paramInt);
  
  public abstract World getSpawnerWorld();
  
  public abstract BlockPos getSpawnerPosition();
  
  public double getMobRotation() {
    return this.mobRotation;
  }
  
  public double getPrevMobRotation() {
    return this.prevMobRotation;
  }
  
  public class WeightedRandomMinecart extends WeightedRandom.Item {
    private final NBTTagCompound nbtData;
    
    private final String entityType;
    
    public WeightedRandomMinecart(NBTTagCompound tagCompound) {
      this(tagCompound.getCompoundTag("Properties"), tagCompound.getString("Type"), tagCompound.getInteger("Weight"));
    }
    
    public WeightedRandomMinecart(NBTTagCompound tagCompound, String type) {
      this(tagCompound, type, 1);
    }
    
    private WeightedRandomMinecart(NBTTagCompound tagCompound, String type, int weight) {
      super(weight);
      if (type.equals("Minecart"))
        if (tagCompound != null) {
          type = EntityMinecart.EnumMinecartType.byNetworkID(tagCompound.getInteger("Type")).getName();
        } else {
          type = "MinecartRideable";
        }  
      this.nbtData = tagCompound;
      this.entityType = type;
    }
    
    public NBTTagCompound toNBT() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setTag("Properties", (NBTBase)this.nbtData);
      nbttagcompound.setString("Type", this.entityType);
      nbttagcompound.setInteger("Weight", this.itemWeight);
      return nbttagcompound;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\tileentity\MobSpawnerBaseLogic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */