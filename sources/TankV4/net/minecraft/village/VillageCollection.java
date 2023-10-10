package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.BlockDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;

public class VillageCollection extends WorldSavedData {
   private final List newDoors = Lists.newArrayList();
   private int tickCounter;
   private final List villagerPositionsList = Lists.newArrayList();
   private World worldObj;
   private final List villageList = Lists.newArrayList();

   public void readFromNBT(NBTTagCompound var1) {
      this.tickCounter = var1.getInteger("Tick");
      NBTTagList var2 = var1.getTagList("Villages", 10);

      for(int var3 = 0; var3 < var2.tagCount(); ++var3) {
         NBTTagCompound var4 = var2.getCompoundTagAt(var3);
         Village var5 = new Village();
         var5.readVillageDataFromNBT(var4);
         this.villageList.add(var5);
      }

   }

   private void addDoorsAround(BlockPos var1) {
      byte var2 = 16;
      byte var3 = 4;
      byte var4 = 16;

      for(int var5 = -var2; var5 < var2; ++var5) {
         for(int var6 = -var3; var6 < var3; ++var6) {
            for(int var7 = -var4; var7 < var4; ++var7) {
               BlockPos var8 = var1.add(var5, var6, var7);
               if (var8 != false) {
                  VillageDoorInfo var9 = this.checkDoorExistence(var8);
                  if (var9 == null) {
                     this.addToNewDoorsList(var8);
                  } else {
                     var9.func_179849_a(this.tickCounter);
                  }
               }
            }
         }
      }

   }

   public void addToVillagerPositionList(BlockPos var1) {
      if (this.villagerPositionsList.size() <= 64 && var1 != false) {
         this.villagerPositionsList.add(var1);
      }

   }

   public void writeToNBT(NBTTagCompound var1) {
      var1.setInteger("Tick", this.tickCounter);
      NBTTagList var2 = new NBTTagList();
      Iterator var4 = this.villageList.iterator();

      while(var4.hasNext()) {
         Village var3 = (Village)var4.next();
         NBTTagCompound var5 = new NBTTagCompound();
         var3.writeVillageDataToNBT(var5);
         var2.appendTag(var5);
      }

      var1.setTag("Villages", var2);
   }

   public Village getNearestVillage(BlockPos var1, int var2) {
      Village var3 = null;
      double var4 = 3.4028234663852886E38D;
      Iterator var7 = this.villageList.iterator();

      while(var7.hasNext()) {
         Village var6 = (Village)var7.next();
         double var8 = var6.getCenter().distanceSq(var1);
         if (var8 < var4) {
            float var10 = (float)(var2 + var6.getVillageRadius());
            if (var8 <= (double)(var10 * var10)) {
               var3 = var6;
               var4 = var8;
            }
         }
      }

      return var3;
   }

   public List getVillageList() {
      return this.villageList;
   }

   private int countBlocksCanSeeSky(BlockPos var1, EnumFacing var2, int var3) {
      int var4 = 0;

      for(int var5 = 1; var5 <= 5; ++var5) {
         if (this.worldObj.canSeeSky(var1.offset(var2, var5))) {
            ++var4;
            if (var4 >= var3) {
               return var4;
            }
         }
      }

      return var4;
   }

   private void addNewDoorsToVillageOrCreateVillage() {
      for(int var1 = 0; var1 < this.newDoors.size(); ++var1) {
         VillageDoorInfo var2 = (VillageDoorInfo)this.newDoors.get(var1);
         Village var3 = this.getNearestVillage(var2.getDoorBlockPos(), 32);
         if (var3 == null) {
            var3 = new Village(this.worldObj);
            this.villageList.add(var3);
            this.markDirty();
         }

         var3.addVillageDoorInfo(var2);
      }

      this.newDoors.clear();
   }

   public VillageCollection(String var1) {
      super(var1);
   }

   public void tick() {
      ++this.tickCounter;
      Iterator var2 = this.villageList.iterator();

      while(var2.hasNext()) {
         Village var1 = (Village)var2.next();
         var1.tick(this.tickCounter);
      }

      this.removeAnnihilatedVillages();
      this.dropOldestVillagerPosition();
      this.addNewDoorsToVillageOrCreateVillage();
      if (this.tickCounter % 400 == 0) {
         this.markDirty();
      }

   }

   private void removeAnnihilatedVillages() {
      Iterator var1 = this.villageList.iterator();

      while(var1.hasNext()) {
         Village var2 = (Village)var1.next();
         if (var2.isAnnihilated()) {
            var1.remove();
            this.markDirty();
         }
      }

   }

   public void setWorldsForAll(World var1) {
      this.worldObj = var1;
      Iterator var3 = this.villageList.iterator();

      while(var3.hasNext()) {
         Village var2 = (Village)var3.next();
         var2.setWorld(var1);
      }

   }

   private VillageDoorInfo checkDoorExistence(BlockPos var1) {
      Iterator var3 = this.newDoors.iterator();

      while(var3.hasNext()) {
         VillageDoorInfo var2 = (VillageDoorInfo)var3.next();
         if (var2.getDoorBlockPos().getX() == var1.getX() && var2.getDoorBlockPos().getZ() == var1.getZ() && Math.abs(var2.getDoorBlockPos().getY() - var1.getY()) <= 1) {
            return var2;
         }
      }

      var3 = this.villageList.iterator();

      while(var3.hasNext()) {
         Village var5 = (Village)var3.next();
         VillageDoorInfo var4 = var5.getExistedDoor(var1);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   private void addToNewDoorsList(BlockPos var1) {
      EnumFacing var2 = BlockDoor.getFacing(this.worldObj, var1);
      EnumFacing var3 = var2.getOpposite();
      int var4 = this.countBlocksCanSeeSky(var1, var2, 5);
      int var5 = this.countBlocksCanSeeSky(var1, var3, var4 + 1);
      if (var4 != var5) {
         this.newDoors.add(new VillageDoorInfo(var1, var4 < var5 ? var2 : var3, this.tickCounter));
      }

   }

   private void dropOldestVillagerPosition() {
      if (!this.villagerPositionsList.isEmpty()) {
         this.addDoorsAround((BlockPos)this.villagerPositionsList.remove(0));
      }

   }

   public static String fileNameForProvider(WorldProvider var0) {
      return "villages" + var0.getInternalNameSuffix();
   }

   public VillageCollection(World var1) {
      super(fileNameForProvider(var1.provider));
      this.worldObj = var1;
      this.markDirty();
   }
}
