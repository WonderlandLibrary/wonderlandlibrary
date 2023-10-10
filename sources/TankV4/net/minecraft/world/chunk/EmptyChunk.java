package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EmptyChunk extends Chunk {
   public void setLightFor(EnumSkyBlock var1, BlockPos var2, int var3) {
   }

   public void generateHeightMap() {
   }

   public void generateSkylightMap() {
   }

   public void removeEntity(Entity var1) {
   }

   public void addEntity(Entity var1) {
   }

   public Random getRandomWithSeed(long var1) {
      return new Random(this.getWorld().getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ var1);
   }

   public TileEntity getTileEntity(BlockPos var1, Chunk.EnumCreateEntityType var2) {
      return null;
   }

   public int getBlockLightOpacity(BlockPos var1) {
      return 255;
   }

   public boolean getAreLevelsEmpty(int var1, int var2) {
      return true;
   }

   public void removeTileEntity(BlockPos var1) {
   }

   public void getEntitiesOfTypeWithinAAAB(Class var1, AxisAlignedBB var2, List var3, Predicate var4) {
   }

   public int getBlockMetadata(BlockPos var1) {
      return 0;
   }

   public boolean isAtLocation(int var1, int var2) {
      return var1 == this.xPosition && var2 == this.zPosition;
   }

   public int getLightSubtracted(BlockPos var1, int var2) {
      return 0;
   }

   public Block getBlock(BlockPos var1) {
      return Blocks.air;
   }

   public boolean isEmpty() {
      return true;
   }

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      return var1.defaultLightValue;
   }

   public boolean needsSaving(boolean var1) {
      return false;
   }

   public void onChunkLoad() {
   }

   public void addTileEntity(TileEntity var1) {
   }

   public void getEntitiesWithinAABBForEntity(Entity var1, AxisAlignedBB var2, List var3, Predicate var4) {
   }

   public boolean canSeeSky(BlockPos var1) {
      return false;
   }

   public void setChunkModified() {
   }

   public EmptyChunk(World var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public void removeEntityAtIndex(Entity var1, int var2) {
   }

   public int getHeightValue(int var1, int var2) {
      return 0;
   }

   public void onChunkUnload() {
   }

   public void addTileEntity(BlockPos var1, TileEntity var2) {
   }
}
