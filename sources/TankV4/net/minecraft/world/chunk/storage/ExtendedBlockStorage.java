package net.minecraft.world.chunk.storage;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.NibbleArray;
import optifine.Reflector;

public class ExtendedBlockStorage {
   private int blockRefCount;
   private char[] data;
   private NibbleArray blocklightArray;
   private int tickRefCount;
   private NibbleArray skylightArray;
   private int yBase;
   private static final String __OBFID = "CL_00000375";

   public void removeInvalidBlocks() {
      List var1 = Block.BLOCK_STATE_IDS.getObjectList();
      int var2 = var1.size();
      int var3 = 0;
      int var4 = 0;

      for(int var5 = 0; var5 < 16; ++var5) {
         int var6 = var5 << 8;

         for(int var7 = 0; var7 < 16; ++var7) {
            int var8 = var6 | var7 << 4;

            for(int var9 = 0; var9 < 16; ++var9) {
               char var10 = this.data[var8 | var9];
               if (var10 > 0) {
                  ++var3;
                  if (var10 < var2) {
                     IBlockState var11 = (IBlockState)var1.get(var10);
                     if (var11 != null) {
                        Block var12 = var11.getBlock();
                        if (var12.getTickRandomly()) {
                           ++var4;
                        }
                     }
                  }
               }
            }
         }
      }

      this.blockRefCount = var3;
      this.tickRefCount = var4;
   }

   public Block getBlockByExtId(int var1, int var2, int var3) {
      return this.get(var1, var2, var3).getBlock();
   }

   public NibbleArray getBlocklightArray() {
      return this.blocklightArray;
   }

   public ExtendedBlockStorage(int var1, boolean var2) {
      this.yBase = var1;
      this.data = new char[4096];
      this.blocklightArray = new NibbleArray();
      if (var2) {
         this.skylightArray = new NibbleArray();
      }

   }

   public void setSkylightArray(NibbleArray var1) {
      this.skylightArray = var1;
   }

   public void setExtSkylightValue(int var1, int var2, int var3, int var4) {
      this.skylightArray.set(var1, var2, var3, var4);
   }

   public char[] getData() {
      return this.data;
   }

   public void set(int var1, int var2, int var3, IBlockState var4) {
      if (Reflector.IExtendedBlockState.isInstance(var4)) {
         var4 = (IBlockState)Reflector.call(var4, Reflector.IExtendedBlockState_getClean);
      }

      IBlockState var5 = this.get(var1, var2, var3);
      Block var6 = var5.getBlock();
      Block var7 = var4.getBlock();
      if (var6 != Blocks.air) {
         --this.blockRefCount;
         if (var6.getTickRandomly()) {
            --this.tickRefCount;
         }
      }

      if (var7 != Blocks.air) {
         ++this.blockRefCount;
         if (var7.getTickRandomly()) {
            ++this.tickRefCount;
         }
      }

      this.data[var2 << 8 | var3 << 4 | var1] = (char)Block.BLOCK_STATE_IDS.get(var4);
   }

   public void setExtBlocklightValue(int var1, int var2, int var3, int var4) {
      this.blocklightArray.set(var1, var2, var3, var4);
   }

   public boolean isEmpty() {
      return this.blockRefCount == 0;
   }

   public boolean getNeedsRandomTick() {
      return this.tickRefCount > 0;
   }

   public int getExtSkylightValue(int var1, int var2, int var3) {
      return this.skylightArray.get(var1, var2, var3);
   }

   public IBlockState get(int var1, int var2, int var3) {
      IBlockState var4 = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(this.data[var2 << 8 | var3 << 4 | var1]);
      return var4 != null ? var4 : Blocks.air.getDefaultState();
   }

   public void setData(char[] var1) {
      this.data = var1;
   }

   public int getYLocation() {
      return this.yBase;
   }

   public NibbleArray getSkylightArray() {
      return this.skylightArray;
   }

   public void setBlocklightArray(NibbleArray var1) {
      this.blocklightArray = var1;
   }

   public int getExtBlockMetadata(int var1, int var2, int var3) {
      IBlockState var4 = this.get(var1, var2, var3);
      return var4.getBlock().getMetaFromState(var4);
   }

   public int getExtBlocklightValue(int var1, int var2, int var3) {
      return this.blocklightArray.get(var1, var2, var3);
   }
}
