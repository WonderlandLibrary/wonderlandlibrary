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
        for (Village village : this.villageList) {
            village.setWorld(worldIn);
        }
    }

    public void addToVillagerPositionList(BlockPos pos) {
        if (this.villagerPositionsList.size() <= 64 && !this.positionInList(pos)) {
            this.villagerPositionsList.add(pos);
        }
    }

    public void tick() {
        ++this.tickCounter;
        for (Village village : this.villageList) {
            village.tick(this.tickCounter);
        }
        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();
        if (this.tickCounter % 400 == 0) {
            this.markDirty();
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
        for (Village village1 : this.villageList) {
            float f2;
            double d1 = village1.getCenter().distanceSq(doorBlock);
            if (!(d1 < d0) || !(d1 <= (double)((f2 = (float)(radius + village1.getVillageRadius())) * f2))) continue;
            village = village1;
            d0 = d1;
        }
        return village;
    }

    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.addDoorsAround(this.villagerPositionsList.remove(0));
        }
    }

    private void addNewDoorsToVillageOrCreateVillage() {
        for (int i2 = 0; i2 < this.newDoors.size(); ++i2) {
            VillageDoorInfo villagedoorinfo = this.newDoors.get(i2);
            Village village = this.getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
            if (village == null) {
                village = new Village(this.worldObj);
                this.villageList.add(village);
                this.markDirty();
            }
            village.addVillageDoorInfo(villagedoorinfo);
        }
        this.newDoors.clear();
    }

    private void addDoorsAround(BlockPos central) {
        int i2 = 16;
        int j2 = 4;
        int k2 = 16;
        for (int l2 = -i2; l2 < i2; ++l2) {
            for (int i1 = -j2; i1 < j2; ++i1) {
                for (int j1 = -k2; j1 < k2; ++j1) {
                    BlockPos blockpos = central.add(l2, i1, j1);
                    if (!this.isWoodDoor(blockpos)) continue;
                    VillageDoorInfo villagedoorinfo = this.checkDoorExistence(blockpos);
                    if (villagedoorinfo == null) {
                        this.addToNewDoorsList(blockpos);
                        continue;
                    }
                    villagedoorinfo.func_179849_a(this.tickCounter);
                }
            }
        }
    }

    private VillageDoorInfo checkDoorExistence(BlockPos doorBlock) {
        for (VillageDoorInfo villagedoorinfo : this.newDoors) {
            if (villagedoorinfo.getDoorBlockPos().getX() != doorBlock.getX() || villagedoorinfo.getDoorBlockPos().getZ() != doorBlock.getZ() || Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) > 1) continue;
            return villagedoorinfo;
        }
        for (Village village : this.villageList) {
            VillageDoorInfo villagedoorinfo1 = village.getExistedDoor(doorBlock);
            if (villagedoorinfo1 == null) continue;
            return villagedoorinfo1;
        }
        return null;
    }

    private void addToNewDoorsList(BlockPos doorBlock) {
        int j2;
        EnumFacing enumfacing = BlockDoor.getFacing(this.worldObj, doorBlock);
        EnumFacing enumfacing1 = enumfacing.getOpposite();
        int i2 = this.countBlocksCanSeeSky(doorBlock, enumfacing, 5);
        if (i2 != (j2 = this.countBlocksCanSeeSky(doorBlock, enumfacing1, i2 + 1))) {
            this.newDoors.add(new VillageDoorInfo(doorBlock, i2 < j2 ? enumfacing : enumfacing1, this.tickCounter));
        }
    }

    private int countBlocksCanSeeSky(BlockPos centerPos, EnumFacing direction, int limitation) {
        int i2 = 0;
        for (int j2 = 1; j2 <= 5; ++j2) {
            if (!this.worldObj.canSeeSky(centerPos.offset(direction, j2)) || ++i2 < limitation) continue;
            return i2;
        }
        return i2;
    }

    private boolean positionInList(BlockPos pos) {
        for (BlockPos blockpos : this.villagerPositionsList) {
            if (!blockpos.equals(pos)) continue;
            return true;
        }
        return false;
    }

    private boolean isWoodDoor(BlockPos doorPos) {
        Block block = this.worldObj.getBlockState(doorPos).getBlock();
        return block instanceof BlockDoor ? block.getMaterial() == Material.wood : false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglist = nbt.getTagList("Villages", 10);
        for (int i2 = 0; i2 < nbttaglist.tagCount(); ++i2) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i2);
            Village village = new Village();
            village.readVillageDataFromNBT(nbttagcompound);
            this.villageList.add(village);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();
        for (Village village : this.villageList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            village.writeVillageDataToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }
        nbt.setTag("Villages", nbttaglist);
    }

    public static String fileNameForProvider(WorldProvider provider) {
        return "villages" + provider.getInternalNameSuffix();
    }
}

