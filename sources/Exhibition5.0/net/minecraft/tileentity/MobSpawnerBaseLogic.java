// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.tileentity;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagList;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.nbt.NBTBase;
import java.util.Iterator;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import java.util.List;

public abstract class MobSpawnerBaseLogic
{
    private int spawnDelay;
    private String mobID;
    private final List minecartToSpawn;
    private WeightedRandomMinecart randomEntity;
    private double field_98287_c;
    private double field_98284_d;
    private int minSpawnDelay;
    private int maxSpawnDelay;
    private int spawnCount;
    private Entity cachedEntity;
    private int maxNearbyEntities;
    private int activatingRangeFromPlayer;
    private int spawnRange;
    private static final String __OBFID = "CL_00000129";
    
    public MobSpawnerBaseLogic() {
        this.spawnDelay = 20;
        this.mobID = "Pig";
        this.minecartToSpawn = Lists.newArrayList();
        this.minSpawnDelay = 200;
        this.maxSpawnDelay = 800;
        this.spawnCount = 4;
        this.maxNearbyEntities = 6;
        this.activatingRangeFromPlayer = 16;
        this.spawnRange = 4;
    }
    
    private String getEntityNameToSpawn() {
        if (this.getRandomEntity() == null) {
            if (this.mobID.equals("Minecart")) {
                this.mobID = "MinecartRideable";
            }
            return this.mobID;
        }
        return this.getRandomEntity().entityType;
    }
    
    public void setEntityName(final String p_98272_1_) {
        this.mobID = p_98272_1_;
    }
    
    private boolean isActivated() {
        final BlockPos var1 = this.func_177221_b();
        return this.getSpawnerWorld().func_175636_b(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5, this.activatingRangeFromPlayer);
    }
    
    public void updateSpawner() {
        if (this.isActivated()) {
            final BlockPos var1 = this.func_177221_b();
            if (this.getSpawnerWorld().isRemote) {
                final double var2 = var1.getX() + this.getSpawnerWorld().rand.nextFloat();
                final double var3 = var1.getY() + this.getSpawnerWorld().rand.nextFloat();
                final double var4 = var1.getZ() + this.getSpawnerWorld().rand.nextFloat();
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var2, var3, var4, 0.0, 0.0, 0.0, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, var2, var3, var4, 0.0, 0.0, 0.0, new int[0]);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.field_98284_d = this.field_98287_c;
                this.field_98287_c = (this.field_98287_c + 1000.0f / (this.spawnDelay + 200.0f)) % 360.0;
            }
            else {
                if (this.spawnDelay == -1) {
                    this.resetTimer();
                }
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean var5 = false;
                for (int var6 = 0; var6 < this.spawnCount; ++var6) {
                    final Entity var7 = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
                    if (var7 == null) {
                        return;
                    }
                    final int var8 = this.getSpawnerWorld().getEntitiesWithinAABB(var7.getClass(), new AxisAlignedBB(var1.getX(), var1.getY(), var1.getZ(), var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1).expand(this.spawnRange, this.spawnRange, this.spawnRange)).size();
                    if (var8 >= this.maxNearbyEntities) {
                        this.resetTimer();
                        return;
                    }
                    final double var4 = var1.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5;
                    final double var9 = var1.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1;
                    final double var10 = var1.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * this.spawnRange + 0.5;
                    final EntityLiving var11 = (var7 instanceof EntityLiving) ? ((EntityLiving)var7) : null;
                    var7.setLocationAndAngles(var4, var9, var10, this.getSpawnerWorld().rand.nextFloat() * 360.0f, 0.0f);
                    if (var11 == null || (var11.getCanSpawnHere() && var11.handleLavaMovement())) {
                        this.func_180613_a(var7, true);
                        this.getSpawnerWorld().playAuxSFX(2004, var1, 0);
                        if (var11 != null) {
                            var11.spawnExplosionParticle();
                        }
                        var5 = true;
                    }
                }
                if (var5) {
                    this.resetTimer();
                }
            }
        }
    }
    
    private Entity func_180613_a(final Entity p_180613_1_, final boolean p_180613_2_) {
        if (this.getRandomEntity() != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            p_180613_1_.writeToNBTOptional(var3);
            for (final String var5 : this.getRandomEntity().field_98222_b.getKeySet()) {
                final NBTBase var6 = this.getRandomEntity().field_98222_b.getTag(var5);
                var3.setTag(var5, var6.copy());
            }
            p_180613_1_.readFromNBT(var3);
            if (p_180613_1_.worldObj != null && p_180613_2_) {
                p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
            }
            Entity var7 = p_180613_1_;
            while (var3.hasKey("Riding", 10)) {
                final NBTTagCompound var8 = var3.getCompoundTag("Riding");
                final Entity var9 = EntityList.createEntityByName(var8.getString("id"), p_180613_1_.worldObj);
                if (var9 != null) {
                    final NBTTagCompound var10 = new NBTTagCompound();
                    var9.writeToNBTOptional(var10);
                    for (final String var12 : var8.getKeySet()) {
                        final NBTBase var13 = var8.getTag(var12);
                        var10.setTag(var12, var13.copy());
                    }
                    var9.readFromNBT(var10);
                    var9.setLocationAndAngles(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
                    if (p_180613_1_.worldObj != null && p_180613_2_) {
                        p_180613_1_.worldObj.spawnEntityInWorld(var9);
                    }
                    var7.mountEntity(var9);
                }
                var7 = var9;
                var3 = var8;
            }
        }
        else if (p_180613_1_ instanceof EntityLivingBase && p_180613_1_.worldObj != null && p_180613_2_) {
            ((EntityLiving)p_180613_1_).func_180482_a(p_180613_1_.worldObj.getDifficultyForLocation(new BlockPos(p_180613_1_)), null);
            p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
        }
        return p_180613_1_;
    }
    
    private void resetTimer() {
        if (this.maxSpawnDelay <= this.minSpawnDelay) {
            this.spawnDelay = this.minSpawnDelay;
        }
        else {
            final int var10003 = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(var10003);
        }
        if (this.minecartToSpawn.size() > 0) {
            this.setRandomEntity((WeightedRandomMinecart)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }
        this.func_98267_a(1);
    }
    
    public void readFromNBT(final NBTTagCompound p_98270_1_) {
        this.mobID = p_98270_1_.getString("EntityId");
        this.spawnDelay = p_98270_1_.getShort("Delay");
        this.minecartToSpawn.clear();
        if (p_98270_1_.hasKey("SpawnPotentials", 9)) {
            final NBTTagList var2 = p_98270_1_.getTagList("SpawnPotentials", 10);
            for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
                this.minecartToSpawn.add(new WeightedRandomMinecart(var2.getCompoundTagAt(var3)));
            }
        }
        if (p_98270_1_.hasKey("SpawnData", 10)) {
            this.setRandomEntity(new WeightedRandomMinecart(p_98270_1_.getCompoundTag("SpawnData"), this.mobID));
        }
        else {
            this.setRandomEntity(null);
        }
        if (p_98270_1_.hasKey("MinSpawnDelay", 99)) {
            this.minSpawnDelay = p_98270_1_.getShort("MinSpawnDelay");
            this.maxSpawnDelay = p_98270_1_.getShort("MaxSpawnDelay");
            this.spawnCount = p_98270_1_.getShort("SpawnCount");
        }
        if (p_98270_1_.hasKey("MaxNearbyEntities", 99)) {
            this.maxNearbyEntities = p_98270_1_.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = p_98270_1_.getShort("RequiredPlayerRange");
        }
        if (p_98270_1_.hasKey("SpawnRange", 99)) {
            this.spawnRange = p_98270_1_.getShort("SpawnRange");
        }
        if (this.getSpawnerWorld() != null) {
            this.cachedEntity = null;
        }
    }
    
    public void writeToNBT(final NBTTagCompound p_98280_1_) {
        p_98280_1_.setString("EntityId", this.getEntityNameToSpawn());
        p_98280_1_.setShort("Delay", (short)this.spawnDelay);
        p_98280_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
        p_98280_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        p_98280_1_.setShort("SpawnCount", (short)this.spawnCount);
        p_98280_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        p_98280_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
        p_98280_1_.setShort("SpawnRange", (short)this.spawnRange);
        if (this.getRandomEntity() != null) {
            p_98280_1_.setTag("SpawnData", this.getRandomEntity().field_98222_b.copy());
        }
        if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0) {
            final NBTTagList var2 = new NBTTagList();
            if (this.minecartToSpawn.size() > 0) {
                for (final WeightedRandomMinecart var4 : this.minecartToSpawn) {
                    var2.appendTag(var4.func_98220_a());
                }
            }
            else {
                var2.appendTag(this.getRandomEntity().func_98220_a());
            }
            p_98280_1_.setTag("SpawnPotentials", var2);
        }
    }
    
    public Entity func_180612_a(final World worldIn) {
        if (this.cachedEntity == null) {
            Entity var2 = EntityList.createEntityByName(this.getEntityNameToSpawn(), worldIn);
            if (var2 != null) {
                var2 = this.func_180613_a(var2, false);
                this.cachedEntity = var2;
            }
        }
        return this.cachedEntity;
    }
    
    public boolean setDelayToMin(final int p_98268_1_) {
        if (p_98268_1_ == 1 && this.getSpawnerWorld().isRemote) {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        return false;
    }
    
    private WeightedRandomMinecart getRandomEntity() {
        return this.randomEntity;
    }
    
    public void setRandomEntity(final WeightedRandomMinecart p_98277_1_) {
        this.randomEntity = p_98277_1_;
    }
    
    public abstract void func_98267_a(final int p0);
    
    public abstract World getSpawnerWorld();
    
    public abstract BlockPos func_177221_b();
    
    public double func_177222_d() {
        return this.field_98287_c;
    }
    
    public double func_177223_e() {
        return this.field_98284_d;
    }
    
    public class WeightedRandomMinecart extends WeightedRandom.Item
    {
        private final NBTTagCompound field_98222_b;
        private final String entityType;
        private static final String __OBFID = "CL_00000130";
        
        public WeightedRandomMinecart(final MobSpawnerBaseLogic this$0, final NBTTagCompound p_i1945_2_) {
            this(this$0, p_i1945_2_.getCompoundTag("Properties"), p_i1945_2_.getString("Type"), p_i1945_2_.getInteger("Weight"));
        }
        
        public WeightedRandomMinecart(final MobSpawnerBaseLogic this$0, final NBTTagCompound p_i1946_2_, final String p_i1946_3_) {
            this(this$0, p_i1946_2_, p_i1946_3_, 1);
        }
        
        private WeightedRandomMinecart(final NBTTagCompound p_i45757_2_, String p_i45757_3_, final int p_i45757_4_) {
            super(p_i45757_4_);
            if (p_i45757_3_.equals("Minecart")) {
                if (p_i45757_2_ != null) {
                    p_i45757_3_ = EntityMinecart.EnumMinecartType.func_180038_a(p_i45757_2_.getInteger("Type")).func_180040_b();
                }
                else {
                    p_i45757_3_ = "MinecartRideable";
                }
            }
            this.field_98222_b = p_i45757_2_;
            this.entityType = p_i45757_3_;
        }
        
        public NBTTagCompound func_98220_a() {
            final NBTTagCompound var1 = new NBTTagCompound();
            var1.setTag("Properties", this.field_98222_b);
            var1.setString("Type", this.entityType);
            var1.setInteger("Weight", this.itemWeight);
            return var1;
        }
    }
}
