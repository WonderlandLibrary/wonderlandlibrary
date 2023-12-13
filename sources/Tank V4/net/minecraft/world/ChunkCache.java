package net.minecraft.world;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class ChunkCache implements IBlockAccess {
   protected boolean hasExtendedLevels;
   protected Chunk[][] chunkArray;
   protected int chunkX;
   protected int chunkZ;
   protected World worldObj;

   public int getLightFor(EnumSkyBlock var1, BlockPos var2) {
      if (var2.getY() >= 0 && var2.getY() < 256) {
         int var3 = (var2.getX() >> 4) - this.chunkX;
         int var4 = (var2.getZ() >> 4) - this.chunkZ;
         return this.chunkArray[var3][var4].getLightFor(var1, var2);
      } else {
         return var1.defaultLightValue;
      }
   }

   public WorldType getWorldType() {
      return this.worldObj.getWorldType();
   }

   public ChunkCache(World var1, BlockPos var2, BlockPos var3, int var4) {
      this.worldObj = var1;
      this.chunkX = var2.getX() - var4 >> 4;
      this.chunkZ = var2.getZ() - var4 >> 4;
      int var5 = var3.getX() + var4 >> 4;
      int var6 = var3.getZ() + var4 >> 4;
      this.chunkArray = new Chunk[var5 - this.chunkX + 1][var6 - this.chunkZ + 1];
      this.hasExtendedLevels = true;

      int var7;
      int var8;
      for(var7 = this.chunkX; var7 <= var5; ++var7) {
         for(var8 = this.chunkZ; var8 <= var6; ++var8) {
            this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ] = var1.getChunkFromChunkCoords(var7, var8);
         }
      }

      for(var7 = var2.getX() >> 4; var7 <= var3.getX() >> 4; ++var7) {
         for(var8 = var2.getZ() >> 4; var8 <= var3.getZ() >> 4; ++var8) {
            Chunk var9 = this.chunkArray[var7 - this.chunkX][var8 - this.chunkZ];
            if (var9 != null && !var9.getAreLevelsEmpty(var2.getY(), var3.getY())) {
               this.hasExtendedLevels = false;
            }
         }
      }

   }

   public int getCombinedLight(BlockPos var1, int var2) {
      int var3 = this.getLightForExt(EnumSkyBlock.SKY, var1);
      int var4 = this.getLightForExt(EnumSkyBlock.BLOCK, var1);
      if (var4 < var2) {
         var4 = var2;
      }

      return var3 << 20 | var4 << 4;
   }

   private int getLightForExt(EnumSkyBlock var1, BlockPos var2) {
      if (var1 == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
         return 0;
      } else if (var2.getY() >= 0 && var2.getY() < 256) {
         int var3;
         if (this.getBlockState(var2).getBlock().getUseNeighborBrightness()) {
            var3 = 0;
            EnumFacing[] var7;
            int var6 = (var7 = EnumFacing.values()).length;

            for(int var5 = 0; var5 < var6; ++var5) {
               EnumFacing var9 = var7[var5];
               int var8 = this.getLightFor(var1, var2.offset(var9));
               if (var8 > var3) {
                  var3 = var8;
               }

               if (var3 >= 15) {
                  return var3;
               }
            }

            return var3;
         } else {
            var3 = (var2.getX() >> 4) - this.chunkX;
            int var4 = (var2.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[var3][var4].getLightFor(var1, var2);
         }
      } else {
         return var1.defaultLightValue;
      }
   }

   public int getStrongPower(BlockPos var1, EnumFacing var2) {
      IBlockState var3 = this.getBlockState(var1);
      return var3.getBlock().getStrongPower(this, var1, var3, var2);
   }

   public TileEntity getTileEntity(BlockPos var1) {
      int var2 = (var1.getX() >> 4) - this.chunkX;
      int var3 = (var1.getZ() >> 4) - this.chunkZ;
      return this.chunkArray[var2][var3].getTileEntity(var1, Chunk.EnumCreateEntityType.IMMEDIATE);
   }

   public boolean extendedLevelsInChunkCache() {
      return this.hasExtendedLevels;
   }

   public IBlockState getBlockState(BlockPos var1) {
      if (var1.getY() >= 0 && var1.getY() < 256) {
         int var2 = (var1.getX() >> 4) - this.chunkX;
         int var3 = (var1.getZ() >> 4) - this.chunkZ;
         if (var2 >= 0 && var2 < this.chunkArray.length && var3 >= 0 && var3 < this.chunkArray[var2].length) {
            Chunk var4 = this.chunkArray[var2][var3];
            if (var4 != null) {
               return var4.getBlockState(var1);
            }
         }
      }

      return Blocks.air.getDefaultState();
   }

   public BiomeGenBase getBiomeGenForCoords(BlockPos var1) {
      return this.worldObj.getBiomeGenForCoords(var1);
   }

   public boolean isAirBlock(BlockPos var1) {
      return this.getBlockState(var1).getBlock().getMaterial() == Material.air;
   }
}
