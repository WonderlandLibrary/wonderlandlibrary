package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public abstract class StructureStart {
   private int chunkPosX;
   private int chunkPosZ;
   protected StructureBoundingBox boundingBox;
   protected LinkedList components = new LinkedList();

   public StructureStart() {
   }

   public void generateStructure(World var1, Random var2, StructureBoundingBox var3) {
      Iterator var4 = this.components.iterator();

      while(var4.hasNext()) {
         StructureComponent var5 = (StructureComponent)var4.next();
         if (var5.getBoundingBox().intersectsWith(var3) && !var5.addComponentParts(var1, var2, var3)) {
            var4.remove();
         }
      }

   }

   public StructureBoundingBox getBoundingBox() {
      return this.boundingBox;
   }

   public void readStructureComponentsFromNBT(World var1, NBTTagCompound var2) {
      this.chunkPosX = var2.getInteger("ChunkX");
      this.chunkPosZ = var2.getInteger("ChunkZ");
      if (var2.hasKey("BB")) {
         this.boundingBox = new StructureBoundingBox(var2.getIntArray("BB"));
      }

      NBTTagList var3 = var2.getTagList("Children", 10);

      for(int var4 = 0; var4 < var3.tagCount(); ++var4) {
         this.components.add(MapGenStructureIO.getStructureComponent(var3.getCompoundTagAt(var4), var1));
      }

      this.readFromNBT(var2);
   }

   protected void setRandomHeight(World var1, Random var2, int var3, int var4) {
      int var5 = var4 - var3 + 1 - this.boundingBox.getYSize();
      boolean var6 = true;
      int var10;
      if (var5 > 1) {
         var10 = var3 + var2.nextInt(var5);
      } else {
         var10 = var3;
      }

      int var7 = var10 - this.boundingBox.minY;
      this.boundingBox.offset(0, var7, 0);
      Iterator var9 = this.components.iterator();

      while(var9.hasNext()) {
         StructureComponent var8 = (StructureComponent)var9.next();
         var8.func_181138_a(0, var7, 0);
      }

   }

   public void func_175787_b(ChunkCoordIntPair var1) {
   }

   public LinkedList getComponents() {
      return this.components;
   }

   protected void updateBoundingBox() {
      this.boundingBox = StructureBoundingBox.getNewBoundingBox();
      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         StructureComponent var1 = (StructureComponent)var2.next();
         this.boundingBox.expandTo(var1.getBoundingBox());
      }

   }

   public int getChunkPosX() {
      return this.chunkPosX;
   }

   protected void markAvailableHeight(World var1, Random var2, int var3) {
      int var4 = var1.func_181545_F() - var3;
      int var5 = this.boundingBox.getYSize() + 1;
      if (var5 < var4) {
         var5 += var2.nextInt(var4 - var5);
      }

      int var6 = var5 - this.boundingBox.maxY;
      this.boundingBox.offset(0, var6, 0);
      Iterator var8 = this.components.iterator();

      while(var8.hasNext()) {
         StructureComponent var7 = (StructureComponent)var8.next();
         var7.func_181138_a(0, var6, 0);
      }

   }

   public void readFromNBT(NBTTagCompound var1) {
   }

   public StructureStart(int var1, int var2) {
      this.chunkPosX = var1;
      this.chunkPosZ = var2;
   }

   public NBTTagCompound writeStructureComponentsToNBT(int var1, int var2) {
      NBTTagCompound var3 = new NBTTagCompound();
      var3.setString("id", MapGenStructureIO.getStructureStartName(this));
      var3.setInteger("ChunkX", var1);
      var3.setInteger("ChunkZ", var2);
      var3.setTag("BB", this.boundingBox.toNBTTagIntArray());
      NBTTagList var4 = new NBTTagList();
      Iterator var6 = this.components.iterator();

      while(var6.hasNext()) {
         StructureComponent var5 = (StructureComponent)var6.next();
         var4.appendTag(var5.createStructureBaseNBT());
      }

      var3.setTag("Children", var4);
      this.writeToNBT(var3);
      return var3;
   }

   public int getChunkPosZ() {
      return this.chunkPosZ;
   }

   public boolean isSizeableStructure() {
      return true;
   }

   public void writeToNBT(NBTTagCompound var1) {
   }

   public boolean func_175788_a(ChunkCoordIntPair var1) {
      return true;
   }
}
