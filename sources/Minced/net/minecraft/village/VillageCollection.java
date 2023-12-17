// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.village;

import net.minecraft.world.WorldProvider;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockDoor;
import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3i;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class VillageCollection extends WorldSavedData
{
    private World world;
    private final List<BlockPos> villagerPositionsList;
    private final List<VillageDoorInfo> newDoors;
    private final List<Village> villageList;
    private int tickCounter;
    
    public VillageCollection(final String name) {
        super(name);
        this.villagerPositionsList = (List<BlockPos>)Lists.newArrayList();
        this.newDoors = (List<VillageDoorInfo>)Lists.newArrayList();
        this.villageList = (List<Village>)Lists.newArrayList();
    }
    
    public VillageCollection(final World worldIn) {
        super(fileNameForProvider(worldIn.provider));
        this.villagerPositionsList = (List<BlockPos>)Lists.newArrayList();
        this.newDoors = (List<VillageDoorInfo>)Lists.newArrayList();
        this.villageList = (List<Village>)Lists.newArrayList();
        this.world = worldIn;
        this.markDirty();
    }
    
    public void setWorldsForAll(final World worldIn) {
        this.world = worldIn;
        for (final Village village : this.villageList) {
            village.setWorld(worldIn);
        }
    }
    
    public void addToVillagerPositionList(final BlockPos pos) {
        if (this.villagerPositionsList.size() <= 64 && !this.positionInList(pos)) {
            this.villagerPositionsList.add(pos);
        }
    }
    
    public void tick() {
        ++this.tickCounter;
        for (final Village village : this.villageList) {
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
        final Iterator<Village> iterator = this.villageList.iterator();
        while (iterator.hasNext()) {
            final Village village = iterator.next();
            if (village.isAnnihilated()) {
                iterator.remove();
                this.markDirty();
            }
        }
    }
    
    public List<Village> getVillageList() {
        return this.villageList;
    }
    
    public Village getNearestVillage(final BlockPos doorBlock, final int radius) {
        Village village = null;
        double d0 = 3.4028234663852886E38;
        for (final Village village2 : this.villageList) {
            final double d2 = village2.getCenter().distanceSq(doorBlock);
            if (d2 < d0) {
                final float f = (float)(radius + village2.getVillageRadius());
                if (d2 > f * f) {
                    continue;
                }
                village = village2;
                d0 = d2;
            }
        }
        return village;
    }
    
    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.addDoorsAround(this.villagerPositionsList.remove(0));
        }
    }
    
    private void addNewDoorsToVillageOrCreateVillage() {
        for (int i = 0; i < this.newDoors.size(); ++i) {
            final VillageDoorInfo villagedoorinfo = this.newDoors.get(i);
            Village village = this.getNearestVillage(villagedoorinfo.getDoorBlockPos(), 32);
            if (village == null) {
                village = new Village(this.world);
                this.villageList.add(village);
                this.markDirty();
            }
            village.addVillageDoorInfo(villagedoorinfo);
        }
        this.newDoors.clear();
    }
    
    private void addDoorsAround(final BlockPos central) {
        final int i = 16;
        final int j = 4;
        final int k = 16;
        for (int l = -16; l < 16; ++l) {
            for (int i2 = -4; i2 < 4; ++i2) {
                for (int j2 = -16; j2 < 16; ++j2) {
                    final BlockPos blockpos = central.add(l, i2, j2);
                    if (this.isWoodDoor(blockpos)) {
                        final VillageDoorInfo villagedoorinfo = this.checkDoorExistence(blockpos);
                        if (villagedoorinfo == null) {
                            this.addToNewDoorsList(blockpos);
                        }
                        else {
                            villagedoorinfo.setLastActivityTimestamp(this.tickCounter);
                        }
                    }
                }
            }
        }
    }
    
    @Nullable
    private VillageDoorInfo checkDoorExistence(final BlockPos doorBlock) {
        for (final VillageDoorInfo villagedoorinfo : this.newDoors) {
            if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1) {
                return villagedoorinfo;
            }
        }
        for (final Village village : this.villageList) {
            final VillageDoorInfo villagedoorinfo2 = village.getExistedDoor(doorBlock);
            if (villagedoorinfo2 != null) {
                return villagedoorinfo2;
            }
        }
        return null;
    }
    
    private void addToNewDoorsList(final BlockPos doorBlock) {
        final EnumFacing enumfacing = BlockDoor.getFacing(this.world, doorBlock);
        final EnumFacing enumfacing2 = enumfacing.getOpposite();
        final int i = this.countBlocksCanSeeSky(doorBlock, enumfacing, 5);
        final int j = this.countBlocksCanSeeSky(doorBlock, enumfacing2, i + 1);
        if (i != j) {
            this.newDoors.add(new VillageDoorInfo(doorBlock, (i < j) ? enumfacing : enumfacing2, this.tickCounter));
        }
    }
    
    private int countBlocksCanSeeSky(final BlockPos centerPos, final EnumFacing direction, final int limitation) {
        int i = 0;
        for (int j = 1; j <= 5; ++j) {
            if (this.world.canSeeSky(centerPos.offset(direction, j)) && ++i >= limitation) {
                return i;
            }
        }
        return i;
    }
    
    private boolean positionInList(final BlockPos pos) {
        for (final BlockPos blockpos : this.villagerPositionsList) {
            if (blockpos.equals(pos)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isWoodDoor(final BlockPos doorPos) {
        final IBlockState iblockstate = this.world.getBlockState(doorPos);
        final Block block = iblockstate.getBlock();
        return block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        final NBTTagList nbttaglist = nbt.getTagList("Villages", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            final Village village = new Village();
            village.readVillageDataFromNBT(nbttagcompound);
            this.villageList.add(village);
        }
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        compound.setInteger("Tick", this.tickCounter);
        final NBTTagList nbttaglist = new NBTTagList();
        for (final Village village : this.villageList) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            village.writeVillageDataToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }
        compound.setTag("Villages", nbttaglist);
        return compound;
    }
    
    public static String fileNameForProvider(final WorldProvider provider) {
        return "villages" + provider.getDimensionType().getSuffix();
    }
}
