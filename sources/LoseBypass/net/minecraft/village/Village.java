/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.village;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

public class Village {
    private World worldObj;
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private TreeMap<String, Integer> playerReputation = new TreeMap();
    private List<VillageAggressor> villageAgressors = Lists.newArrayList();
    private int numIronGolems;

    public Village() {
    }

    public Village(World worldIn) {
        this.worldObj = worldIn;
    }

    public void setWorld(World worldIn) {
        this.worldObj = worldIn;
    }

    public void tick(int p_75560_1_) {
        int i;
        this.tickCounter = p_75560_1_;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (p_75560_1_ % 20 == 0) {
            this.updateNumVillagers();
        }
        if (p_75560_1_ % 30 == 0) {
            this.updateNumIronGolems();
        }
        if (this.numIronGolems >= (i = this.numVillagers / 10)) return;
        if (this.villageDoorInfoList.size() <= 20) return;
        if (this.worldObj.rand.nextInt(7000) != 0) return;
        Vec3 vec3 = this.func_179862_a(this.center, 2, 4, 2);
        if (vec3 == null) return;
        EntityIronGolem entityirongolem = new EntityIronGolem(this.worldObj);
        entityirongolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        this.worldObj.spawnEntityInWorld(entityirongolem);
        ++this.numIronGolems;
    }

    private Vec3 func_179862_a(BlockPos p_179862_1_, int p_179862_2_, int p_179862_3_, int p_179862_4_) {
        int i = 0;
        while (i < 10) {
            BlockPos blockpos = p_179862_1_.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
            if (this.func_179866_a(blockpos) && this.func_179861_a(new BlockPos(p_179862_2_, p_179862_3_, p_179862_4_), blockpos)) {
                return new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
            ++i;
        }
        return null;
    }

    private boolean func_179861_a(BlockPos p_179861_1_, BlockPos p_179861_2_) {
        if (!World.doesBlockHaveSolidTopSurface(this.worldObj, p_179861_2_.down())) {
            return false;
        }
        int i = p_179861_2_.getX() - p_179861_1_.getX() / 2;
        int j = p_179861_2_.getZ() - p_179861_1_.getZ() / 2;
        int k = i;
        block0: while (k < i + p_179861_1_.getX()) {
            int l = p_179861_2_.getY();
            while (true) {
                if (l < p_179861_2_.getY() + p_179861_1_.getY()) {
                } else {
                    ++k;
                    continue block0;
                }
                for (int i1 = j; i1 < j + p_179861_1_.getZ(); ++i1) {
                    if (!this.worldObj.getBlockState(new BlockPos(k, l, i1)).getBlock().isNormalCube()) continue;
                    return false;
                }
                ++l;
            }
            break;
        }
        return true;
    }

    private void updateNumIronGolems() {
        List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numIronGolems = list.size();
    }

    private void updateNumVillagers() {
        List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numVillagers = list.size();
        if (this.numVillagers != 0) return;
        this.playerReputation.clear();
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers() {
        return this.numVillagers;
    }

    public boolean func_179866_a(BlockPos pos) {
        if (!(this.center.distanceSq(pos) < (double)(this.villageRadius * this.villageRadius))) return false;
        return true;
    }

    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo getNearestDoor(BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (iterator.hasNext()) {
            VillageDoorInfo villagedoorinfo1 = iterator.next();
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
            if (j >= i) continue;
            villagedoorinfo = villagedoorinfo1;
            i = j;
        }
        return villagedoorinfo;
    }

    public VillageDoorInfo getDoorInfo(BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (iterator.hasNext()) {
            VillageDoorInfo villagedoorinfo1 = iterator.next();
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);
            j = j > 256 ? (j *= 1000) : villagedoorinfo1.getDoorOpeningRestrictionCounter();
            if (j >= i) continue;
            villagedoorinfo = villagedoorinfo1;
            i = j;
        }
        return villagedoorinfo;
    }

    public VillageDoorInfo getExistedDoor(BlockPos doorBlock) {
        VillageDoorInfo villagedoorinfo;
        if (this.center.distanceSq(doorBlock) > (double)(this.villageRadius * this.villageRadius)) {
            return null;
        }
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while ((villagedoorinfo = iterator.next()).getDoorBlockPos().getX() != doorBlock.getX() || villagedoorinfo.getDoorBlockPos().getZ() != doorBlock.getZ() || Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) > 1);
        return villagedoorinfo;
    }

    public void addVillageDoorInfo(VillageDoorInfo doorInfo) {
        this.villageDoorInfoList.add(doorInfo);
        this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = doorInfo.getInsidePosY();
    }

    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn) {
        VillageAggressor village$villageaggressor;
        Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
        do {
            if (!iterator.hasNext()) {
                this.villageAgressors.add(new VillageAggressor(entitylivingbaseIn, this.tickCounter));
                return;
            }
            village$villageaggressor = iterator.next();
        } while (village$villageaggressor.agressor != entitylivingbaseIn);
        village$villageaggressor.agressionTime = this.tickCounter;
    }

    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn) {
        double d0 = Double.MAX_VALUE;
        VillageAggressor village$villageaggressor = null;
        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            VillageAggressor village$villageaggressor1 = this.villageAgressors.get(i);
            double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);
            if (!(d1 <= d0)) continue;
            village$villageaggressor = village$villageaggressor1;
            d0 = d1;
        }
        if (village$villageaggressor == null) return null;
        EntityLivingBase entityLivingBase = village$villageaggressor.agressor;
        return entityLivingBase;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;
        Iterator<String> iterator = this.playerReputation.keySet().iterator();
        while (iterator.hasNext()) {
            double d1;
            EntityPlayer entityplayer1;
            String s = iterator.next();
            if (!this.isPlayerReputationTooLow(s) || (entityplayer1 = this.worldObj.getPlayerEntityByName(s)) == null || !((d1 = entityplayer1.getDistanceSqToEntity(villageDefender)) <= d0)) continue;
            entityplayer = entityplayer1;
            d0 = d1;
        }
        return entityplayer;
    }

    private void removeDeadAndOldAgressors() {
        Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
        while (iterator.hasNext()) {
            VillageAggressor village$villageaggressor = iterator.next();
            if (village$villageaggressor.agressor.isEntityAlive() && Math.abs(this.tickCounter - village$villageaggressor.agressionTime) <= 300) continue;
            iterator.remove();
        }
    }

    private void removeDeadAndOutOfRangeDoors() {
        boolean flag = false;
        boolean flag1 = this.worldObj.rand.nextInt(50) == 0;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                if (!flag) return;
                this.updateVillageRadiusAndCenter();
                return;
            }
            VillageDoorInfo villagedoorinfo = iterator.next();
            if (flag1) {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }
            if (this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) && Math.abs(this.tickCounter - villagedoorinfo.getInsidePosY()) <= 1200) continue;
            this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
            flag = true;
            villagedoorinfo.setIsDetachedFromVillageFlag(true);
            iterator.remove();
        }
    }

    private boolean isWoodDoor(BlockPos pos) {
        Block block = this.worldObj.getBlockState(pos).getBlock();
        if (!(block instanceof BlockDoor)) {
            return false;
        }
        if (block.getMaterial() != Material.wood) return false;
        return true;
    }

    private void updateVillageRadiusAndCenter() {
        int i = this.villageDoorInfoList.size();
        if (i == 0) {
            this.center = new BlockPos(0, 0, 0);
            this.villageRadius = 0;
            return;
        }
        this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
        int j = 0;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
                return;
            }
            VillageDoorInfo villagedoorinfo = iterator.next();
            j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
        }
    }

    public int getReputationForPlayer(String p_82684_1_) {
        Integer integer = this.playerReputation.get(p_82684_1_);
        if (integer == null) return 0;
        int n = integer;
        return n;
    }

    public int setReputationForPlayer(String p_82688_1_, int p_82688_2_) {
        int i = this.getReputationForPlayer(p_82688_1_);
        int j = MathHelper.clamp_int(i + p_82688_2_, -30, 10);
        this.playerReputation.put(p_82688_1_, j);
        return j;
    }

    public boolean isPlayerReputationTooLow(String p_82687_1_) {
        if (this.getReputationForPlayer(p_82687_1_) > -15) return false;
        return true;
    }

    public void readVillageDataFromNBT(NBTTagCompound p_82690_1_) {
        this.numVillagers = p_82690_1_.getInteger("PopSize");
        this.villageRadius = p_82690_1_.getInteger("Radius");
        this.numIronGolems = p_82690_1_.getInteger("Golems");
        this.lastAddDoorTimestamp = p_82690_1_.getInteger("Stable");
        this.tickCounter = p_82690_1_.getInteger("Tick");
        this.noBreedTicks = p_82690_1_.getInteger("MTick");
        this.center = new BlockPos(p_82690_1_.getInteger("CX"), p_82690_1_.getInteger("CY"), p_82690_1_.getInteger("CZ"));
        this.centerHelper = new BlockPos(p_82690_1_.getInteger("ACX"), p_82690_1_.getInteger("ACY"), p_82690_1_.getInteger("ACZ"));
        NBTTagList nbttaglist = p_82690_1_.getTagList("Doors", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }
        NBTTagList nbttaglist1 = p_82690_1_.getTagList("Players", 10);
        int j = 0;
        while (j < nbttaglist1.tagCount()) {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);
            if (nbttagcompound1.hasKey("UUID")) {
                PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
                GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));
                if (gameprofile != null) {
                    this.playerReputation.put(gameprofile.getName(), nbttagcompound1.getInteger("S"));
                }
            } else {
                this.playerReputation.put(nbttagcompound1.getString("Name"), nbttagcompound1.getInteger("S"));
            }
            ++j;
        }
    }

    public void writeVillageDataToNBT(NBTTagCompound p_82689_1_) {
        p_82689_1_.setInteger("PopSize", this.numVillagers);
        p_82689_1_.setInteger("Radius", this.villageRadius);
        p_82689_1_.setInteger("Golems", this.numIronGolems);
        p_82689_1_.setInteger("Stable", this.lastAddDoorTimestamp);
        p_82689_1_.setInteger("Tick", this.tickCounter);
        p_82689_1_.setInteger("MTick", this.noBreedTicks);
        p_82689_1_.setInteger("CX", this.center.getX());
        p_82689_1_.setInteger("CY", this.center.getY());
        p_82689_1_.setInteger("CZ", this.center.getZ());
        p_82689_1_.setInteger("ACX", this.centerHelper.getX());
        p_82689_1_.setInteger("ACY", this.centerHelper.getY());
        p_82689_1_.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();
        for (VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
            nbttaglist.appendTag(nbttagcompound);
        }
        p_82689_1_.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator<String> iterator = this.playerReputation.keySet().iterator();
        while (true) {
            if (!iterator.hasNext()) {
                p_82689_1_.setTag("Players", nbttaglist1);
                return;
            }
            String s = iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            PlayerProfileCache playerprofilecache = MinecraftServer.getServer().getPlayerProfileCache();
            GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
            if (gameprofile == null) continue;
            nbttagcompound1.setString("UUID", gameprofile.getId().toString());
            nbttagcompound1.setInteger("S", this.playerReputation.get(s));
            nbttaglist1.appendTag(nbttagcompound1);
        }
    }

    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }

    public boolean isMatingSeason() {
        if (this.noBreedTicks == 0) return true;
        if (this.tickCounter - this.noBreedTicks >= 3600) return true;
        return false;
    }

    public void setDefaultPlayerReputation(int p_82683_1_) {
        Iterator<String> iterator = this.playerReputation.keySet().iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            this.setReputationForPlayer(s, p_82683_1_);
        }
    }

    class VillageAggressor {
        public EntityLivingBase agressor;
        public int agressionTime;

        VillageAggressor(EntityLivingBase p_i1674_2_, int p_i1674_3_) {
            this.agressor = p_i1674_2_;
            this.agressionTime = p_i1674_3_;
        }
    }
}

