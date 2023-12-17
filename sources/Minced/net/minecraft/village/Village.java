// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.nbt.NBTBase;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.nbt.NBTTagList;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import java.util.Iterator;
import net.minecraft.util.math.Vec3i;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.world.World;

public class Village
{
    private World world;
    private final List<VillageDoorInfo> villageDoorInfoList;
    private BlockPos centerHelper;
    private BlockPos center;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private final Map<String, Integer> playerReputation;
    private final List<VillageAggressor> villageAgressors;
    private int numIronGolems;
    
    public Village() {
        this.villageDoorInfoList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = (Map<String, Integer>)Maps.newHashMap();
        this.villageAgressors = (List<VillageAggressor>)Lists.newArrayList();
    }
    
    public Village(final World worldIn) {
        this.villageDoorInfoList = (List<VillageDoorInfo>)Lists.newArrayList();
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = (Map<String, Integer>)Maps.newHashMap();
        this.villageAgressors = (List<VillageAggressor>)Lists.newArrayList();
        this.world = worldIn;
    }
    
    public void setWorld(final World worldIn) {
        this.world = worldIn;
    }
    
    public void tick(final int tickCounterIn) {
        this.tickCounter = tickCounterIn;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (tickCounterIn % 20 == 0) {
            this.updateNumVillagers();
        }
        if (tickCounterIn % 30 == 0) {
            this.updateNumIronGolems();
        }
        final int i = this.numVillagers / 10;
        if (this.numIronGolems < i && this.villageDoorInfoList.size() > 20 && this.world.rand.nextInt(7000) == 0) {
            final Vec3d vec3d = this.findRandomSpawnPos(this.center, 2, 4, 2);
            if (vec3d != null) {
                final EntityIronGolem entityirongolem = new EntityIronGolem(this.world);
                entityirongolem.setPosition(vec3d.x, vec3d.y, vec3d.z);
                this.world.spawnEntity(entityirongolem);
                ++this.numIronGolems;
            }
        }
    }
    
    private Vec3d findRandomSpawnPos(final BlockPos pos, final int x, final int y, final int z) {
        for (int i = 0; i < 10; ++i) {
            final BlockPos blockpos = pos.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);
            if (this.isBlockPosWithinSqVillageRadius(blockpos) && this.isAreaClearAround(new BlockPos(x, y, z), blockpos)) {
                return new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }
        return null;
    }
    
    private boolean isAreaClearAround(final BlockPos blockSize, final BlockPos blockLocation) {
        if (!this.world.getBlockState(blockLocation.down()).isTopSolid()) {
            return false;
        }
        final int i = blockLocation.getX() - blockSize.getX() / 2;
        final int j = blockLocation.getZ() - blockSize.getZ() / 2;
        for (int k = i; k < i + blockSize.getX(); ++k) {
            for (int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l) {
                for (int i2 = j; i2 < j + blockSize.getZ(); ++i2) {
                    if (this.world.getBlockState(new BlockPos(k, l, i2)).isNormalCube()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void updateNumIronGolems() {
        final List<EntityIronGolem> list = this.world.getEntitiesWithinAABB((Class<? extends EntityIronGolem>)EntityIronGolem.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numIronGolems = list.size();
    }
    
    private void updateNumVillagers() {
        final List<EntityVillager> list = this.world.getEntitiesWithinAABB((Class<? extends EntityVillager>)EntityVillager.class, new AxisAlignedBB(this.center.getX() - this.villageRadius, this.center.getY() - 4, this.center.getZ() - this.villageRadius, this.center.getX() + this.villageRadius, this.center.getY() + 4, this.center.getZ() + this.villageRadius));
        this.numVillagers = list.size();
        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }
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
    
    public boolean isBlockPosWithinSqVillageRadius(final BlockPos pos) {
        return this.center.distanceSq(pos) < this.villageRadius * this.villageRadius;
    }
    
    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }
    
    public VillageDoorInfo getNearestDoor(final BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : this.villageDoorInfoList) {
            final int j = villagedoorinfo2.getDistanceToDoorBlockSq(pos);
            if (j < i) {
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    public VillageDoorInfo getDoorInfo(final BlockPos pos) {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;
        for (final VillageDoorInfo villagedoorinfo2 : this.villageDoorInfoList) {
            int j = villagedoorinfo2.getDistanceToDoorBlockSq(pos);
            if (j > 256) {
                j *= 1000;
            }
            else {
                j = villagedoorinfo2.getDoorOpeningRestrictionCounter();
            }
            if (j < i) {
                final BlockPos blockpos = villagedoorinfo2.getDoorBlockPos();
                final EnumFacing enumfacing = villagedoorinfo2.getInsideDirection();
                if (!this.world.getBlockState(blockpos.offset(enumfacing, 1)).getBlock().isPassable(this.world, blockpos.offset(enumfacing, 1)) || !this.world.getBlockState(blockpos.offset(enumfacing, -1)).getBlock().isPassable(this.world, blockpos.offset(enumfacing, -1)) || !this.world.getBlockState(blockpos.up().offset(enumfacing, 1)).getBlock().isPassable(this.world, blockpos.up().offset(enumfacing, 1)) || !this.world.getBlockState(blockpos.up().offset(enumfacing, -1)).getBlock().isPassable(this.world, blockpos.up().offset(enumfacing, -1))) {
                    continue;
                }
                villagedoorinfo = villagedoorinfo2;
                i = j;
            }
        }
        return villagedoorinfo;
    }
    
    @Nullable
    public VillageDoorInfo getExistedDoor(final BlockPos doorBlock) {
        if (this.center.distanceSq(doorBlock) > this.villageRadius * this.villageRadius) {
            return null;
        }
        for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
                return villagedoorinfo;
            }
        }
        return null;
    }
    
    public void addVillageDoorInfo(final VillageDoorInfo doorInfo) {
        this.villageDoorInfoList.add(doorInfo);
        this.centerHelper = this.centerHelper.add(doorInfo.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = doorInfo.getLastActivityTimestamp();
    }
    
    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }
    
    public void addOrRenewAgressor(final EntityLivingBase entitylivingbaseIn) {
        for (final VillageAggressor village$villageaggressor : this.villageAgressors) {
            if (village$villageaggressor.agressor == entitylivingbaseIn) {
                village$villageaggressor.agressionTime = this.tickCounter;
                return;
            }
        }
        this.villageAgressors.add(new VillageAggressor(entitylivingbaseIn, this.tickCounter));
    }
    
    @Nullable
    public EntityLivingBase findNearestVillageAggressor(final EntityLivingBase entitylivingbaseIn) {
        double d0 = Double.MAX_VALUE;
        VillageAggressor village$villageaggressor = null;
        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            final VillageAggressor village$villageaggressor2 = this.villageAgressors.get(i);
            final double d2 = village$villageaggressor2.agressor.getDistanceSq(entitylivingbaseIn);
            if (d2 <= d0) {
                village$villageaggressor = village$villageaggressor2;
                d0 = d2;
            }
        }
        return (village$villageaggressor == null) ? null : village$villageaggressor.agressor;
    }
    
    public EntityPlayer getNearestTargetPlayer(final EntityLivingBase villageDefender) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;
        for (final String s : this.playerReputation.keySet()) {
            if (this.isPlayerReputationTooLow(s)) {
                final EntityPlayer entityplayer2 = this.world.getPlayerEntityByName(s);
                if (entityplayer2 == null) {
                    continue;
                }
                final double d2 = entityplayer2.getDistanceSq(villageDefender);
                if (d2 > d0) {
                    continue;
                }
                entityplayer = entityplayer2;
                d0 = d2;
            }
        }
        return entityplayer;
    }
    
    private void removeDeadAndOldAgressors() {
        final Iterator<VillageAggressor> iterator = this.villageAgressors.iterator();
        while (iterator.hasNext()) {
            final VillageAggressor village$villageaggressor = iterator.next();
            if (!village$villageaggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - village$villageaggressor.agressionTime) > 300) {
                iterator.remove();
            }
        }
    }
    
    private void removeDeadAndOutOfRangeDoors() {
        boolean flag = false;
        final boolean flag2 = this.world.rand.nextInt(50) == 0;
        final Iterator<VillageDoorInfo> iterator = this.villageDoorInfoList.iterator();
        while (iterator.hasNext()) {
            final VillageDoorInfo villagedoorinfo = iterator.next();
            if (flag2) {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }
            if (!this.isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoorinfo.getLastActivityTimestamp()) > 1200) {
                this.centerHelper = this.centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
                flag = true;
                villagedoorinfo.setIsDetachedFromVillageFlag(true);
                iterator.remove();
            }
        }
        if (flag) {
            this.updateVillageRadiusAndCenter();
        }
    }
    
    private boolean isWoodDoor(final BlockPos pos) {
        final IBlockState iblockstate = this.world.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD;
    }
    
    private void updateVillageRadiusAndCenter() {
        final int i = this.villageDoorInfoList.size();
        if (i == 0) {
            this.center = BlockPos.ORIGIN;
            this.villageRadius = 0;
        }
        else {
            this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
            int j = 0;
            for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
                j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(this.center), j);
            }
            this.villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
        }
    }
    
    public int getPlayerReputation(final String playerName) {
        final Integer integer = this.playerReputation.get(playerName);
        return (integer == null) ? 0 : integer;
    }
    
    public int modifyPlayerReputation(final String playerName, final int reputation) {
        final int i = this.getPlayerReputation(playerName);
        final int j = MathHelper.clamp(i + reputation, -30, 10);
        this.playerReputation.put(playerName, j);
        return j;
    }
    
    public boolean isPlayerReputationTooLow(final String playerName) {
        return this.getPlayerReputation(playerName) <= -15;
    }
    
    public void readVillageDataFromNBT(final NBTTagCompound compound) {
        this.numVillagers = compound.getInteger("PopSize");
        this.villageRadius = compound.getInteger("Radius");
        this.numIronGolems = compound.getInteger("Golems");
        this.lastAddDoorTimestamp = compound.getInteger("Stable");
        this.tickCounter = compound.getInteger("Tick");
        this.noBreedTicks = compound.getInteger("MTick");
        this.center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
        this.centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
        final NBTTagList nbttaglist = compound.getTagList("Doors", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            this.villageDoorInfoList.add(villagedoorinfo);
        }
        final NBTTagList nbttaglist2 = compound.getTagList("Players", 10);
        for (int j = 0; j < nbttaglist2.tagCount(); ++j) {
            final NBTTagCompound nbttagcompound2 = nbttaglist2.getCompoundTagAt(j);
            if (nbttagcompound2.hasKey("UUID") && this.world != null && this.world.getMinecraftServer() != null) {
                final PlayerProfileCache playerprofilecache = this.world.getMinecraftServer().getPlayerProfileCache();
                final GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound2.getString("UUID")));
                if (gameprofile != null) {
                    this.playerReputation.put(gameprofile.getName(), nbttagcompound2.getInteger("S"));
                }
            }
            else {
                this.playerReputation.put(nbttagcompound2.getString("Name"), nbttagcompound2.getInteger("S"));
            }
        }
    }
    
    public void writeVillageDataToNBT(final NBTTagCompound compound) {
        compound.setInteger("PopSize", this.numVillagers);
        compound.setInteger("Radius", this.villageRadius);
        compound.setInteger("Golems", this.numIronGolems);
        compound.setInteger("Stable", this.lastAddDoorTimestamp);
        compound.setInteger("Tick", this.tickCounter);
        compound.setInteger("MTick", this.noBreedTicks);
        compound.setInteger("CX", this.center.getX());
        compound.setInteger("CY", this.center.getY());
        compound.setInteger("CZ", this.center.getZ());
        compound.setInteger("ACX", this.centerHelper.getX());
        compound.setInteger("ACY", this.centerHelper.getY());
        compound.setInteger("ACZ", this.centerHelper.getZ());
        final NBTTagList nbttaglist = new NBTTagList();
        for (final VillageDoorInfo villagedoorinfo : this.villageDoorInfoList) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getLastActivityTimestamp());
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag("Doors", nbttaglist);
        final NBTTagList nbttaglist2 = new NBTTagList();
        for (final String s : this.playerReputation.keySet()) {
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            final PlayerProfileCache playerprofilecache = this.world.getMinecraftServer().getPlayerProfileCache();
            try {
                final GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);
                if (gameprofile == null) {
                    continue;
                }
                nbttagcompound2.setString("UUID", gameprofile.getId().toString());
                nbttagcompound2.setInteger("S", this.playerReputation.get(s));
                nbttaglist2.appendTag(nbttagcompound2);
            }
            catch (RuntimeException ex) {}
        }
        compound.setTag("Players", nbttaglist2);
    }
    
    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }
    
    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }
    
    public void setDefaultPlayerReputation(final int defaultReputation) {
        for (final String s : this.playerReputation.keySet()) {
            this.modifyPlayerReputation(s, defaultReputation);
        }
    }
    
    class VillageAggressor
    {
        public EntityLivingBase agressor;
        public int agressionTime;
        
        VillageAggressor(final EntityLivingBase agressorIn, final int agressionTimeIn) {
            this.agressor = agressorIn;
            this.agressionTime = agressionTimeIn;
        }
    }
}
