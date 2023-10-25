/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
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
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
    private TreeMap<String, Integer> playerReputation;
    private BlockPos center;
    private List<VillageAggressor> villageAgressors;
    private int noBreedTicks;
    private int lastAddDoorTimestamp;
    private int numIronGolems;
    private int tickCounter;
    private int numVillagers;
    private int villageRadius;
    private BlockPos centerHelper = BlockPos.ORIGIN;
    private World worldObj;

    private void removeDeadAndOutOfRangeDoors() {
        boolean bl = false;
        boolean bl2 = this.worldObj.rand.nextInt(50) == 0;
        Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (iterator.hasNext()) {
            VillageDoorInfo villageDoorInfo = iterator.next();
            if (bl2) {
                villageDoorInfo.resetDoorOpeningRestrictionCounter();
            }
            if (this.isWoodDoor(villageDoorInfo.getDoorBlockPos()) && Math.abs(this.tickCounter - villageDoorInfo.getInsidePosY()) <= 1200) continue;
            this.centerHelper = this.centerHelper.subtract(villageDoorInfo.getDoorBlockPos());
            bl = true;
            villageDoorInfo.setIsDetachedFromVillageFlag(true);
            iterator.remove();
        }
        if (bl) {
            this.updateVillageRadiusAndCenter();
        }
    }

    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }

    public int getNumVillagers() {
        return this.numVillagers;
    }

    public void tick(int n) {
        Vec3 vec3;
        int n2;
        this.tickCounter = n;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (n % 20 == 0) {
            this.updateNumVillagers();
        }
        if (n % 30 == 0) {
            this.updateNumIronGolems();
        }
        if (this.numIronGolems < (n2 = this.numVillagers / 10) && this.villageDoorInfoList.size() > 20 && this.worldObj.rand.nextInt(7000) == 0 && (vec3 = this.func_179862_a(this.center, 2, 4, 2)) != null) {
            EntityIronGolem entityIronGolem = new EntityIronGolem(this.worldObj);
            entityIronGolem.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
            this.worldObj.spawnEntityInWorld(entityIronGolem);
            ++this.numIronGolems;
        }
    }

    public void writeVillageDataToNBT(NBTTagCompound nBTTagCompound) {
        nBTTagCompound.setInteger("PopSize", this.numVillagers);
        nBTTagCompound.setInteger("Radius", this.villageRadius);
        nBTTagCompound.setInteger("Golems", this.numIronGolems);
        nBTTagCompound.setInteger("Stable", this.lastAddDoorTimestamp);
        nBTTagCompound.setInteger("Tick", this.tickCounter);
        nBTTagCompound.setInteger("MTick", this.noBreedTicks);
        nBTTagCompound.setInteger("CX", this.center.getX());
        nBTTagCompound.setInteger("CY", this.center.getY());
        nBTTagCompound.setInteger("CZ", this.center.getZ());
        nBTTagCompound.setInteger("ACX", this.centerHelper.getX());
        nBTTagCompound.setInteger("ACY", this.centerHelper.getY());
        nBTTagCompound.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList nBTTagList = new NBTTagList();
        for (VillageDoorInfo villageDoorInfo : this.villageDoorInfoList) {
            NBTTagCompound nBTTagCompound2 = new NBTTagCompound();
            nBTTagCompound2.setInteger("X", villageDoorInfo.getDoorBlockPos().getX());
            nBTTagCompound2.setInteger("Y", villageDoorInfo.getDoorBlockPos().getY());
            nBTTagCompound2.setInteger("Z", villageDoorInfo.getDoorBlockPos().getZ());
            nBTTagCompound2.setInteger("IDX", villageDoorInfo.getInsideOffsetX());
            nBTTagCompound2.setInteger("IDZ", villageDoorInfo.getInsideOffsetZ());
            nBTTagCompound2.setInteger("TS", villageDoorInfo.getInsidePosY());
            nBTTagList.appendTag(nBTTagCompound2);
        }
        nBTTagCompound.setTag("Doors", nBTTagList);
        NBTTagList nBTTagList2 = new NBTTagList();
        for (String string : this.playerReputation.keySet()) {
            NBTTagCompound nBTTagCompound3 = new NBTTagCompound();
            PlayerProfileCache playerProfileCache = MinecraftServer.getServer().getPlayerProfileCache();
            GameProfile gameProfile = playerProfileCache.getGameProfileForUsername(string);
            if (gameProfile == null) continue;
            nBTTagCompound3.setString("UUID", gameProfile.getId().toString());
            nBTTagCompound3.setInteger("S", this.playerReputation.get(string));
            nBTTagList2.appendTag(nBTTagCompound3);
        }
        nBTTagCompound.setTag("Players", nBTTagList2);
    }

    public int setReputationForPlayer(String string, int n) {
        int n2 = this.getReputationForPlayer(string);
        int n3 = MathHelper.clamp_int(n2 + n, -30, 10);
        this.playerReputation.put(string, n3);
        return n3;
    }

    private void updateNumIronGolems() {
        List<EntityIronGolem> list = this.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numIronGolems = list.size();
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }

    public void addVillageDoorInfo(VillageDoorInfo villageDoorInfo) {
        this.villageDoorInfoList.add(villageDoorInfo);
        this.centerHelper = this.centerHelper.add(villageDoorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = villageDoorInfo.getInsidePosY();
    }

    public Village() {
        this.center = BlockPos.ORIGIN;
        this.playerReputation = new TreeMap();
        this.villageAgressors = Lists.newArrayList();
    }

    public void addOrRenewAgressor(EntityLivingBase entityLivingBase) {
        for (VillageAggressor villageAggressor : this.villageAgressors) {
            if (villageAggressor.agressor != entityLivingBase) continue;
            villageAggressor.agressionTime = this.tickCounter;
            return;
        }
        this.villageAgressors.add(new VillageAggressor(entityLivingBase, this.tickCounter));
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase entityLivingBase) {
        double d = Double.MAX_VALUE;
        EntityPlayer entityPlayer = null;
        for (String string : this.playerReputation.keySet()) {
            double d2;
            EntityPlayer entityPlayer2;
            if (!this.isPlayerReputationTooLow(string) || (entityPlayer2 = this.worldObj.getPlayerEntityByName(string)) == null || !((d2 = entityPlayer2.getDistanceSqToEntity(entityLivingBase)) <= d)) continue;
            entityPlayer = entityPlayer2;
            d = d2;
        }
        return entityPlayer;
    }

    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }

    public boolean isPlayerReputationTooLow(String string) {
        return this.getReputationForPlayer(string) <= -15;
    }

    public Village(World world) {
        this.center = BlockPos.ORIGIN;
        this.playerReputation = new TreeMap();
        this.villageAgressors = Lists.newArrayList();
        this.worldObj = world;
    }

    private void removeDeadAndOldAgressors() {
        Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
        while (iterator.hasNext()) {
            VillageAggressor villageAggressor = iterator.next();
            if (villageAggressor.agressor.isEntityAlive() && Math.abs(this.tickCounter - villageAggressor.agressionTime) <= 300) continue;
            iterator.remove();
        }
    }

    public void setWorld(World world) {
        this.worldObj = world;
    }

    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }

    public boolean func_179866_a(BlockPos blockPos) {
        return this.center.distanceSq(blockPos) < (double)(this.villageRadius * this.villageRadius);
    }

    public int getReputationForPlayer(String string) {
        Integer n = this.playerReputation.get(string);
        return n != null ? n : 0;
    }

    public void setDefaultPlayerReputation(int n) {
        for (String string : this.playerReputation.keySet()) {
            this.setReputationForPlayer(string, n);
        }
    }

    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    private Vec3 func_179862_a(BlockPos blockPos, int n, int n2, int n3) {
        int n4 = 0;
        while (n4 < 10) {
            BlockPos blockPos2 = blockPos.add(this.worldObj.rand.nextInt(16) - 8, this.worldObj.rand.nextInt(6) - 3, this.worldObj.rand.nextInt(16) - 8);
            if (this.func_179866_a(blockPos2) && this.func_179861_a(new BlockPos(n, n2, n3), blockPos2)) {
                return new Vec3(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ());
            }
            ++n4;
        }
        return null;
    }

    private boolean func_179861_a(BlockPos blockPos, BlockPos blockPos2) {
        if (!World.doesBlockHaveSolidTopSurface(this.worldObj, blockPos2.down())) {
            return false;
        }
        int n = blockPos2.getX() - blockPos.getX() / 2;
        int n2 = blockPos2.getZ() - blockPos.getZ() / 2;
        int n3 = n;
        while (n3 < n + blockPos.getX()) {
            int n4 = blockPos2.getY();
            while (n4 < blockPos2.getY() + blockPos.getY()) {
                int n5 = n2;
                while (n5 < n2 + blockPos.getZ()) {
                    if (this.worldObj.getBlockState(new BlockPos(n3, n4, n5)).getBlock().isNormalCube()) {
                        return false;
                    }
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
        return true;
    }

    private void updateVillageRadiusAndCenter() {
        int n = this.villageDoorInfoList.size();
        if (n == 0) {
            this.center = new BlockPos(0, 0, 0);
            this.villageRadius = 0;
        } else {
            this.center = new BlockPos(this.centerHelper.getX() / n, this.centerHelper.getY() / n, this.centerHelper.getZ() / n);
            int n2 = 0;
            for (VillageDoorInfo villageDoorInfo : this.villageDoorInfoList) {
                n2 = Math.max(villageDoorInfo.getDistanceToDoorBlockSq(this.center), n2);
            }
            this.villageRadius = Math.max(32, (int)Math.sqrt(n2) + 1);
        }
    }

    private void updateNumVillagers() {
        List<EntityVillager> list = this.worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numVillagers = list.size();
        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }
    }

    private boolean isWoodDoor(BlockPos blockPos) {
        Block block = this.worldObj.getBlockState(blockPos).getBlock();
        return block instanceof BlockDoor ? block.getMaterial() == Material.wood : false;
    }

    public VillageDoorInfo getDoorInfo(BlockPos blockPos) {
        VillageDoorInfo villageDoorInfo = null;
        int n = Integer.MAX_VALUE;
        for (VillageDoorInfo villageDoorInfo2 : this.villageDoorInfoList) {
            int n2 = villageDoorInfo2.getDistanceToDoorBlockSq(blockPos);
            n2 = n2 > 256 ? (n2 *= 1000) : villageDoorInfo2.getDoorOpeningRestrictionCounter();
            if (n2 >= n) continue;
            villageDoorInfo = villageDoorInfo2;
            n = n2;
        }
        return villageDoorInfo;
    }

    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entityLivingBase) {
        double d = Double.MAX_VALUE;
        VillageAggressor villageAggressor = null;
        int n = 0;
        while (n < this.villageAgressors.size()) {
            VillageAggressor villageAggressor2 = this.villageAgressors.get(n);
            double d2 = villageAggressor2.agressor.getDistanceSqToEntity(entityLivingBase);
            if (d2 <= d) {
                villageAggressor = villageAggressor2;
                d = d2;
            }
            ++n;
        }
        return villageAggressor != null ? villageAggressor.agressor : null;
    }

    public VillageDoorInfo getNearestDoor(BlockPos blockPos) {
        VillageDoorInfo villageDoorInfo = null;
        int n = Integer.MAX_VALUE;
        for (VillageDoorInfo villageDoorInfo2 : this.villageDoorInfoList) {
            int n2 = villageDoorInfo2.getDistanceToDoorBlockSq(blockPos);
            if (n2 >= n) continue;
            villageDoorInfo = villageDoorInfo2;
            n = n2;
        }
        return villageDoorInfo;
    }

    public BlockPos getCenter() {
        return this.center;
    }

    public void readVillageDataFromNBT(NBTTagCompound nBTTagCompound) {
        Object object;
        this.numVillagers = nBTTagCompound.getInteger("PopSize");
        this.villageRadius = nBTTagCompound.getInteger("Radius");
        this.numIronGolems = nBTTagCompound.getInteger("Golems");
        this.lastAddDoorTimestamp = nBTTagCompound.getInteger("Stable");
        this.tickCounter = nBTTagCompound.getInteger("Tick");
        this.noBreedTicks = nBTTagCompound.getInteger("MTick");
        this.center = new BlockPos(nBTTagCompound.getInteger("CX"), nBTTagCompound.getInteger("CY"), nBTTagCompound.getInteger("CZ"));
        this.centerHelper = new BlockPos(nBTTagCompound.getInteger("ACX"), nBTTagCompound.getInteger("ACY"), nBTTagCompound.getInteger("ACZ"));
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Doors", 10);
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            NBTTagCompound nBTTagCompound2 = nBTTagList.getCompoundTagAt(n);
            object = new VillageDoorInfo(new BlockPos(nBTTagCompound2.getInteger("X"), nBTTagCompound2.getInteger("Y"), nBTTagCompound2.getInteger("Z")), nBTTagCompound2.getInteger("IDX"), nBTTagCompound2.getInteger("IDZ"), nBTTagCompound2.getInteger("TS"));
            this.villageDoorInfoList.add((VillageDoorInfo)object);
            ++n;
        }
        NBTTagList nBTTagList2 = nBTTagCompound.getTagList("Players", 10);
        int n2 = 0;
        while (n2 < nBTTagList2.tagCount()) {
            object = nBTTagList2.getCompoundTagAt(n2);
            if (((NBTTagCompound)object).hasKey("UUID")) {
                PlayerProfileCache playerProfileCache = MinecraftServer.getServer().getPlayerProfileCache();
                GameProfile gameProfile = playerProfileCache.getProfileByUUID(UUID.fromString(((NBTTagCompound)object).getString("UUID")));
                if (gameProfile != null) {
                    this.playerReputation.put(gameProfile.getName(), ((NBTTagCompound)object).getInteger("S"));
                }
            } else {
                this.playerReputation.put(((NBTTagCompound)object).getString("Name"), ((NBTTagCompound)object).getInteger("S"));
            }
            ++n2;
        }
    }

    public VillageDoorInfo getExistedDoor(BlockPos blockPos) {
        if (this.center.distanceSq(blockPos) > (double)(this.villageRadius * this.villageRadius)) {
            return null;
        }
        for (VillageDoorInfo villageDoorInfo : this.villageDoorInfoList) {
            if (villageDoorInfo.getDoorBlockPos().getX() != blockPos.getX() || villageDoorInfo.getDoorBlockPos().getZ() != blockPos.getZ() || Math.abs(villageDoorInfo.getDoorBlockPos().getY() - blockPos.getY()) > 1) continue;
            return villageDoorInfo;
        }
        return null;
    }

    class VillageAggressor {
        public int agressionTime;
        public EntityLivingBase agressor;

        VillageAggressor(EntityLivingBase entityLivingBase, int n) {
            this.agressor = entityLivingBase;
            this.agressionTime = n;
        }
    }
}

