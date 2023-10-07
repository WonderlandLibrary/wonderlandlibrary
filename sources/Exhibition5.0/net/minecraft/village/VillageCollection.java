// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.village;

import net.minecraft.world.WorldProvider;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockDoor;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class VillageCollection extends WorldSavedData
{
    private World worldObj;
    private final List villagerPositionsList;
    private final List newDoors;
    private final List villageList;
    private int tickCounter;
    private static final String __OBFID = "CL_00001635";
    
    public VillageCollection(final String p_i1677_1_) {
        super(p_i1677_1_);
        this.villagerPositionsList = Lists.newArrayList();
        this.newDoors = Lists.newArrayList();
        this.villageList = Lists.newArrayList();
    }
    
    public VillageCollection(final World worldIn) {
        super(func_176062_a(worldIn.provider));
        this.villagerPositionsList = Lists.newArrayList();
        this.newDoors = Lists.newArrayList();
        this.villageList = Lists.newArrayList();
        this.worldObj = worldIn;
        this.markDirty();
    }
    
    public void func_82566_a(final World worldIn) {
        this.worldObj = worldIn;
        for (final Village var3 : this.villageList) {
            var3.func_82691_a(worldIn);
        }
    }
    
    public void func_176060_a(final BlockPos p_176060_1_) {
        if (this.villagerPositionsList.size() <= 64 && !this.func_176057_e(p_176060_1_)) {
            this.villagerPositionsList.add(p_176060_1_);
        }
    }
    
    public void tick() {
        ++this.tickCounter;
        for (final Village var2 : this.villageList) {
            var2.tick(this.tickCounter);
        }
        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();
        if (this.tickCounter % 400 == 0) {
            this.markDirty();
        }
    }
    
    private void removeAnnihilatedVillages() {
        final Iterator var1 = this.villageList.iterator();
        while (var1.hasNext()) {
            final Village var2 = var1.next();
            if (var2.isAnnihilated()) {
                var1.remove();
                this.markDirty();
            }
        }
    }
    
    public List getVillageList() {
        return this.villageList;
    }
    
    public Village func_176056_a(final BlockPos p_176056_1_, final int p_176056_2_) {
        Village var3 = null;
        double var4 = 3.4028234663852886E38;
        for (final Village var6 : this.villageList) {
            final double var7 = var6.func_180608_a().distanceSq(p_176056_1_);
            if (var7 < var4) {
                final float var8 = p_176056_2_ + var6.getVillageRadius();
                if (var7 > var8 * var8) {
                    continue;
                }
                var3 = var6;
                var4 = var7;
            }
        }
        return var3;
    }
    
    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.func_180609_b(this.villagerPositionsList.remove(0));
        }
    }
    
    private void addNewDoorsToVillageOrCreateVillage() {
        for (int var1 = 0; var1 < this.newDoors.size(); ++var1) {
            final VillageDoorInfo var2 = this.newDoors.get(var1);
            Village var3 = this.func_176056_a(var2.func_179852_d(), 32);
            if (var3 == null) {
                var3 = new Village(this.worldObj);
                this.villageList.add(var3);
                this.markDirty();
            }
            var3.addVillageDoorInfo(var2);
        }
        this.newDoors.clear();
    }
    
    private void func_180609_b(final BlockPos p_180609_1_) {
        final byte var2 = 16;
        final byte var3 = 4;
        final byte var4 = 16;
        for (int var5 = -var2; var5 < var2; ++var5) {
            for (int var6 = -var3; var6 < var3; ++var6) {
                for (int var7 = -var4; var7 < var4; ++var7) {
                    final BlockPos var8 = p_180609_1_.add(var5, var6, var7);
                    if (this.func_176058_f(var8)) {
                        final VillageDoorInfo var9 = this.func_176055_c(var8);
                        if (var9 == null) {
                            this.func_176059_d(var8);
                        }
                        else {
                            var9.func_179849_a(this.tickCounter);
                        }
                    }
                }
            }
        }
    }
    
    private VillageDoorInfo func_176055_c(final BlockPos p_176055_1_) {
        for (final VillageDoorInfo var3 : this.newDoors) {
            if (var3.func_179852_d().getX() == p_176055_1_.getX() && var3.func_179852_d().getZ() == p_176055_1_.getZ() && Math.abs(var3.func_179852_d().getY() - p_176055_1_.getY()) <= 1) {
                return var3;
            }
        }
        for (final Village var4 : this.villageList) {
            final VillageDoorInfo var5 = var4.func_179864_e(p_176055_1_);
            if (var5 != null) {
                return var5;
            }
        }
        return null;
    }
    
    private void func_176059_d(final BlockPos p_176059_1_) {
        final EnumFacing var2 = BlockDoor.func_176517_h(this.worldObj, p_176059_1_);
        final EnumFacing var3 = var2.getOpposite();
        final int var4 = this.func_176061_a(p_176059_1_, var2, 5);
        final int var5 = this.func_176061_a(p_176059_1_, var3, var4 + 1);
        if (var4 != var5) {
            this.newDoors.add(new VillageDoorInfo(p_176059_1_, (var4 < var5) ? var2 : var3, this.tickCounter));
        }
    }
    
    private int func_176061_a(final BlockPos p_176061_1_, final EnumFacing p_176061_2_, final int p_176061_3_) {
        int var4 = 0;
        for (int var5 = 1; var5 <= 5; ++var5) {
            if (this.worldObj.isAgainstSky(p_176061_1_.offset(p_176061_2_, var5)) && ++var4 >= p_176061_3_) {
                return var4;
            }
        }
        return var4;
    }
    
    private boolean func_176057_e(final BlockPos p_176057_1_) {
        for (final BlockPos var3 : this.villagerPositionsList) {
            if (var3.equals(p_176057_1_)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean func_176058_f(final BlockPos p_176058_1_) {
        final Block var2 = this.worldObj.getBlockState(p_176058_1_).getBlock();
        return var2 instanceof BlockDoor && var2.getMaterial() == Material.wood;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        final NBTTagList var2 = nbt.getTagList("Villages", 10);
        for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final Village var5 = new Village();
            var5.readVillageDataFromNBT(var4);
            this.villageList.add(var5);
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        nbt.setInteger("Tick", this.tickCounter);
        final NBTTagList var2 = new NBTTagList();
        for (final Village var4 : this.villageList) {
            final NBTTagCompound var5 = new NBTTagCompound();
            var4.writeVillageDataToNBT(var5);
            var2.appendTag(var5);
        }
        nbt.setTag("Villages", var2);
    }
    
    public static String func_176062_a(final WorldProvider p_176062_0_) {
        return "villages" + p_176062_0_.getInternalNameSuffix();
    }
}
