/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;

public class VillageCollection
extends WorldSavedData {
    private World worldObj;
    private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
    private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
    private final List<Village> villageList = Lists.newArrayList();
    private int tickCounter;

    public VillageCollection(String name) {
        super(name);
    }

    public VillageCollection(World worldIn) {
        super(VillageCollection.fileNameForProvider(worldIn.provider));
        this.worldObj = worldIn;
        this.markDirty();
    }

    public void setWorldsForAll(World worldIn) {
        this.worldObj = worldIn;
        Iterator<Village> iterator = this.villageList.iterator();
        while (iterator.hasNext()) {
            Village village = iterator.next();
            village.setWorld(worldIn);
        }
    }

    public void addToVillagerPositionList(BlockPos pos) {
        if (this.villagerPositionsList.size() > 64) return;
        if (this.positionInList(pos)) return;
        this.villagerPositionsList.add(pos);
    }

    public void tick() {
        ++this.tickCounter;
        Iterator<Village> iterator = this.villageList.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.removeAnnihilatedVillages();
                this.dropOldestVillagerPosition();
                this.addNewDoorsToVillageOrCreateVillage();
                if (this.tickCounter % 400 != 0) return;
                this.markDirty();
                return;
            }
            Village village = iterator.next();
            village.tick(this.tickCounter);
        }
    }

    private void removeAnnihilatedVillages() {
        Iterator<Village> iterator = this.villageList.iterator();
        while (iterator.hasNext()) {
            Village village = iterator.next();
            if (!village.isAnnihilated()) continue;
            iterator.remove();
            this.markDirty();
        }
    }

    public List<Village> getVillageList() {
        return this.villageList;
    }

    public Village getNearestVillage(BlockPos doorBlock, int radius) {
        Village village = null;
        double d0 = 3.4028234663852886E38;
        Iterator<Village> iterator = this.villageList.iterator();
        while (iterator.hasNext()) {
            float f;
            Village village1 = iterator.next();
            double d1 = village1.getCenter().distanceSq(doorBlock);
            if (!(d1 < d0) || !(d1 <= (double)((f = (float)(radius + village1.getVillageRadius())) * f))) continue;
            village = village1;
            d0 = d1;
        }
        return village;
    }

    private void dropOldestVillagerPosition() {
        if (this.villagerPositionsList.isEmpty()) return;
        this.addDoorsAround(this.villagerPositionsList.remove(0));
    }

    private void addNewDoorsToVillageOrCreateVillage() {
        int i = 0;
        while (true) {
            if (i >= this.newDoors.size()) {
                this.newDoors.clear();
                return;
            }
            VillageDoorInfo villagedoorinfo = this.newDoors.get(i);
            Village village = this.getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
            if (village == null) {
                village = new Village(this.worldObj);
                this.villageList.add(village);
                this.markDirty();
            }
            village.addVillageDoorInfo(villagedoorinfo);
            ++i;
        }
    }

    private void addDoorsAround(BlockPos central) {
        int i = 16;
        int j = 4;
        int k = 16;
        int l = -i;
        block0: while (l < i) {
            int i1 = -j;
            while (true) {
                if (i1 < j) {
                } else {
                    ++l;
                    continue block0;
                }
                for (int j1 = -k; j1 < k; ++j1) {
                    BlockPos blockpos = central.add(l, i1, j1);
                    if (!this.isWoodDoor(blockpos)) continue;
                    VillageDoorInfo villagedoorinfo = this.checkDoorExistence(blockpos);
                    if (villagedoorinfo == null) {
                        this.addToNewDoorsList(blockpos);
                        continue;
                    }
                    villagedoorinfo.func_179849_a(this.tickCounter);
                }
                ++i1;
            }
            break;
        }
        return;
    }

    private VillageDoorInfo checkDoorExistence(BlockPos doorBlock) {
        Village village;
        VillageDoorInfo villagedoorinfo1;
        for (VillageDoorInfo villagedoorinfo : this.newDoors) {
            if (villagedoorinfo.getDoorBlockPos().getX() != doorBlock.getX() || villagedoorinfo.getDoorBlockPos().getZ() != doorBlock.getZ() || Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) > 1) continue;
            return villagedoorinfo;
        }
        Iterator<Object> iterator = this.villageList.iterator();
        do {
            if (!iterator.hasNext()) return null;
        } while ((villagedoorinfo1 = (village = (Village)iterator.next()).getExistedDoor(doorBlock)) == null);
        return villagedoorinfo1;
    }

    private void addToNewDoorsList(BlockPos doorBlock) {
        int j;
        EnumFacing enumfacing = BlockDoor.getFacing(this.worldObj, doorBlock);
        EnumFacing enumfacing1 = enumfacing.getOpposite();
        int i = this.countBlocksCanSeeSky(doorBlock, enumfacing, 5);
        if (i == (j = this.countBlocksCanSeeSky(doorBlock, enumfacing1, i + 1))) return;
        this.newDoors.add(new VillageDoorInfo(doorBlock, i < j ? enumfacing : enumfacing1, this.tickCounter));
    }

    private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation) {
        int i = 0;
        int j = 1;
        while (j <= 5) {
            if (this.worldObj.canSeeSky(centerPos.offset(direction, j)) && ++i >= limitation) {
                return i;
            }
            ++j;
        }
        return i;
    }

    private boolean positionInList(BlockPos pos) {
        BlockPos blockpos;
        Iterator<BlockPos> iterator = this.villagerPositionsList.iterator();
        do {
            if (!iterator.hasNext()) return false;
        } while (!(blockpos = iterator.next()).equals(pos));
        return true;
    }

    private boolean isWoodDoor(BlockPos doorPos) {
        Block block = this.worldObj.getBlockState(doorPos).getBlock();
        if (!(block instanceof BlockDoor)) {
            return false;
        }
        if (block.getMaterial() != Material.wood) return false;
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglist = nbt.getTagList("Villages", 10);
        int i = 0;
        while (i < nbttaglist.tagCount()) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            Village village = new Village();
            village.readVillageDataFromNBT(nbttagcompound);
            this.villageList.add(village);
            ++i;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();
        Iterator<Village> iterator = this.villageList.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                nbt.setTag("Villages", nbttaglist);
                return;
            }
            Village village = iterator.next();
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            village.writeVillageDataToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }
    }

    public static String fileNameForProvider(WorldProvider provider) {
        return "villages" + provider.getInternalNameSuffix();
    }
}

