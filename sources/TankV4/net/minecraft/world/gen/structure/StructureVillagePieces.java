package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;

public class StructureVillagePieces {
   private static StructureComponent func_176069_e(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      if (var7 > 3 + var0.terrainType) {
         return null;
      } else if (Math.abs(var3 - var0.getBoundingBox().minX) <= 112 && Math.abs(var5 - var0.getBoundingBox().minZ) <= 112) {
         StructureBoundingBox var8 = StructureVillagePieces.Path.func_175848_a(var0, var1, var2, var3, var4, var5, var6);
         if (var8 != null && var8.minY > 10) {
            StructureVillagePieces.Path var9 = new StructureVillagePieces.Path(var0, var7, var2, var8, var6);
            int var10 = (var9.boundingBox.minX + var9.boundingBox.maxX) / 2;
            int var11 = (var9.boundingBox.minZ + var9.boundingBox.maxZ) / 2;
            int var12 = var9.boundingBox.maxX - var9.boundingBox.minX;
            int var13 = var9.boundingBox.maxZ - var9.boundingBox.minZ;
            int var14 = var12 > var13 ? var12 : var13;
            if (var0.getWorldChunkManager().areBiomesViable(var10, var11, var14 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
               var1.add(var9);
               var0.field_74930_j.add(var9);
               return var9;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static StructureVillagePieces.Village func_176065_a(StructureVillagePieces.Start var0, StructureVillagePieces.PieceWeight var1, List var2, Random var3, int var4, int var5, int var6, EnumFacing var7, int var8) {
      Class var9 = var1.villagePieceClass;
      Object var10 = null;
      if (var9 == StructureVillagePieces.House4Garden.class) {
         var10 = StructureVillagePieces.House4Garden.func_175858_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.Church.class) {
         var10 = StructureVillagePieces.Church.func_175854_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.House1.class) {
         var10 = StructureVillagePieces.House1.func_175850_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.WoodHut.class) {
         var10 = StructureVillagePieces.WoodHut.func_175853_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.Hall.class) {
         var10 = StructureVillagePieces.Hall.func_175857_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.Field1.class) {
         var10 = StructureVillagePieces.Field1.func_175851_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.Field2.class) {
         var10 = StructureVillagePieces.Field2.func_175852_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.House2.class) {
         var10 = StructureVillagePieces.House2.func_175855_a(var0, var2, var3, var4, var5, var6, var7, var8);
      } else if (var9 == StructureVillagePieces.House3.class) {
         var10 = StructureVillagePieces.House3.func_175849_a(var0, var2, var3, var4, var5, var6, var7, var8);
      }

      return (StructureVillagePieces.Village)var10;
   }

   public static List getStructureVillageWeightedPieceList(Random var0, int var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.getRandomIntegerInRange(var0, 2 + var1, 4 + var1 * 2)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getRandomIntegerInRange(var0, 0 + var1, 1 + var1)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.getRandomIntegerInRange(var0, 0 + var1, 2 + var1)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.getRandomIntegerInRange(var0, 2 + var1, 5 + var1 * 3)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.getRandomIntegerInRange(var0, 0 + var1, 2 + var1)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.getRandomIntegerInRange(var0, 1 + var1, 4 + var1)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.getRandomIntegerInRange(var0, 2 + var1, 4 + var1 * 2)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.getRandomIntegerInRange(var0, 0, 1 + var1)));
      var2.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.getRandomIntegerInRange(var0, 0 + var1, 3 + var1 * 2)));
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         if (((StructureVillagePieces.PieceWeight)var3.next()).villagePiecesLimit == 0) {
            var3.remove();
         }
      }

      return var2;
   }

   static StructureComponent access$1(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      return func_176066_d(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static int func_75079_a(List var0) {
      boolean var1 = false;
      int var2 = 0;

      StructureVillagePieces.PieceWeight var3;
      for(Iterator var4 = var0.iterator(); var4.hasNext(); var2 += var3.villagePieceWeight) {
         var3 = (StructureVillagePieces.PieceWeight)var4.next();
         if (var3.villagePiecesLimit > 0 && var3.villagePiecesSpawned < var3.villagePiecesLimit) {
            var1 = true;
         }
      }

      return var1 ? var2 : -1;
   }

   static StructureComponent access$0(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      return func_176069_e(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static StructureVillagePieces.Village func_176067_c(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      int var8 = func_75079_a(var0.structureVillageWeightedPieceList);
      if (var8 <= 0) {
         return null;
      } else {
         int var9 = 0;

         while(var9 < 5) {
            ++var9;
            int var10 = var2.nextInt(var8);
            Iterator var12 = var0.structureVillageWeightedPieceList.iterator();

            while(var12.hasNext()) {
               StructureVillagePieces.PieceWeight var11 = (StructureVillagePieces.PieceWeight)var12.next();
               var10 -= var11.villagePieceWeight;
               if (var10 < 0) {
                  if (!var11.canSpawnMoreVillagePiecesOfType(var7) || var11 == var0.structVillagePieceWeight && var0.structureVillageWeightedPieceList.size() > 1) {
                     break;
                  }

                  StructureVillagePieces.Village var13 = func_176065_a(var0, var11, var1, var2, var3, var4, var5, var6, var7);
                  if (var13 != null) {
                     ++var11.villagePiecesSpawned;
                     var0.structVillagePieceWeight = var11;
                     if (!var11.canSpawnMoreVillagePieces()) {
                        var0.structureVillageWeightedPieceList.remove(var11);
                     }

                     return var13;
                  }
               }
            }
         }

         StructureBoundingBox var14 = StructureVillagePieces.Torch.func_175856_a(var0, var1, var2, var3, var4, var5, var6);
         if (var14 != null) {
            return new StructureVillagePieces.Torch(var0, var7, var2, var14, var6);
         } else {
            return null;
         }
      }
   }

   public static void registerVillagePieces() {
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House1.class, "ViBH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field1.class, "ViDF");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Field2.class, "ViF");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Torch.class, "ViL");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Hall.class, "ViPH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House4Garden.class, "ViSH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.WoodHut.class, "ViSmH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Church.class, "ViST");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House2.class, "ViS");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Start.class, "ViStart");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class, "ViSR");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.House3.class, "ViTRH");
      MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Well.class, "ViW");
   }

   private static StructureComponent func_176066_d(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      if (var7 > 50) {
         return null;
      } else if (Math.abs(var3 - var0.getBoundingBox().minX) <= 112 && Math.abs(var5 - var0.getBoundingBox().minZ) <= 112) {
         StructureVillagePieces.Village var8 = func_176067_c(var0, var1, var2, var3, var4, var5, var6, var7 + 1);
         if (var8 != null) {
            int var9 = (var8.boundingBox.minX + var8.boundingBox.maxX) / 2;
            int var10 = (var8.boundingBox.minZ + var8.boundingBox.maxZ) / 2;
            int var11 = var8.boundingBox.maxX - var8.boundingBox.minX;
            int var12 = var8.boundingBox.maxZ - var8.boundingBox.minZ;
            int var13 = var11 > var12 ? var11 : var12;
            if (var0.getWorldChunkManager().areBiomesViable(var9, var10, var13 / 2 + 4, MapGenVillage.villageSpawnBiomes)) {
               var1.add(var8);
               var0.field_74932_i.add(var8);
               return var8;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   abstract static class Village extends StructureComponent {
      private int villagersSpawned;
      private boolean isDesertVillage;
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      protected int field_143015_k = -1;

      protected int getAverageGroundLevel(World var1, StructureBoundingBox var2) {
         int var3 = 0;
         int var4 = 0;
         BlockPos.MutableBlockPos var5 = new BlockPos.MutableBlockPos();

         for(int var6 = this.boundingBox.minZ; var6 <= this.boundingBox.maxZ; ++var6) {
            for(int var7 = this.boundingBox.minX; var7 <= this.boundingBox.maxX; ++var7) {
               var5.func_181079_c(var7, 64, var6);
               if (var2.isVecInside(var5)) {
                  var3 += Math.max(var1.getTopSolidOrLiquidBlock(var5).getY(), var1.provider.getAverageGroundLevel());
                  ++var4;
               }
            }
         }

         if (var4 == 0) {
            return -1;
         } else {
            return var3 / var4;
         }
      }

      protected void func_175846_a(boolean var1) {
         this.isDesertVillage = var1;
      }

      protected void spawnVillagers(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6) {
         if (this.villagersSpawned < var6) {
            for(int var7 = this.villagersSpawned; var7 < var6; ++var7) {
               int var8 = this.getXWithOffset(var3 + var7, var5);
               int var9 = this.getYWithOffset(var4);
               int var10 = this.getZWithOffset(var3 + var7, var5);
               if (!var2.isVecInside(new BlockPos(var8, var9, var10))) {
                  break;
               }

               ++this.villagersSpawned;
               EntityVillager var11 = new EntityVillager(var1);
               var11.setLocationAndAngles((double)var8 + 0.5D, (double)var9, (double)var10 + 0.5D, 0.0F, 0.0F);
               var11.onInitialSpawn(var1.getDifficultyForLocation(new BlockPos(var11)), (IEntityLivingData)null);
               var11.setProfession(this.func_180779_c(var7, var11.getProfession()));
               var1.spawnEntityInWorld(var11);
            }
         }

      }

      protected void fillWithBlocks(World var1, StructureBoundingBox var2, int var3, int var4, int var5, int var6, int var7, int var8, IBlockState var9, IBlockState var10, boolean var11) {
         IBlockState var12 = this.func_175847_a(var9);
         IBlockState var13 = this.func_175847_a(var10);
         super.fillWithBlocks(var1, var2, var3, var4, var5, var6, var7, var8, var12, var13, var11);
      }

      protected static boolean canVillageGoDeeper(StructureBoundingBox var0) {
         return var0 != null && var0.minY > 10;
      }

      protected int func_180779_c(int var1, int var2) {
         return var2;
      }

      protected StructureComponent getNextComponentNN(StructureVillagePieces.Start var1, List var2, Random var3, int var4, int var5) {
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.WEST, this.getComponentType());
            case 4:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.WEST, this.getComponentType());
            case 5:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            case 6:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            }
         }

         return null;
      }

      protected Village(StructureVillagePieces.Start var1, int var2) {
         super(var2);
         if (var1 != null) {
            this.isDesertVillage = var1.inDesert;
         }

      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         var1.setInteger("HPos", this.field_143015_k);
         var1.setInteger("VCount", this.villagersSpawned);
         var1.setBoolean("Desert", this.isDesertVillage);
      }

      protected IBlockState func_175847_a(IBlockState var1) {
         if (this.isDesertVillage) {
            if (var1.getBlock() == Blocks.log || var1.getBlock() == Blocks.log2) {
               return Blocks.sandstone.getDefaultState();
            }

            if (var1.getBlock() == Blocks.cobblestone) {
               return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.DEFAULT.getMetadata());
            }

            if (var1.getBlock() == Blocks.planks) {
               return Blocks.sandstone.getStateFromMeta(BlockSandStone.EnumType.SMOOTH.getMetadata());
            }

            if (var1.getBlock() == Blocks.oak_stairs) {
               return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, (EnumFacing)var1.getValue(BlockStairs.FACING));
            }

            if (var1.getBlock() == Blocks.stone_stairs) {
               return Blocks.sandstone_stairs.getDefaultState().withProperty(BlockStairs.FACING, (EnumFacing)var1.getValue(BlockStairs.FACING));
            }

            if (var1.getBlock() == Blocks.gravel) {
               return Blocks.sandstone.getDefaultState();
            }
         }

         return var1;
      }

      protected StructureComponent getNextComponentPP(StructureVillagePieces.Start var1, List var2, Random var3, int var4, int var5) {
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.EAST, this.getComponentType());
            case 4:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.EAST, this.getComponentType());
            case 5:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
            case 6:
               return StructureVillagePieces.access$1(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
            }
         }

         return null;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         this.field_143015_k = var1.getInteger("HPos");
         this.villagersSpawned = var1.getInteger("VCount");
         this.isDesertVillage = var1.getBoolean("Desert");
      }

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.values().length];

            try {
               var0[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            try {
               var0[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
            return var0;
         }
      }

      public Village() {
      }

      protected void replaceAirAndLiquidDownwards(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
         IBlockState var7 = this.func_175847_a(var2);
         super.replaceAirAndLiquidDownwards(var1, var7, var3, var4, var5, var6);
      }

      protected void setBlockState(World var1, IBlockState var2, int var3, int var4, int var5, StructureBoundingBox var6) {
         IBlockState var7 = this.func_175847_a(var2);
         super.setBlockState(var1, var7, var3, var4, var5, var6);
      }
   }

   public static class Hall extends StructureVillagePieces.Village {
      public Hall() {
      }

      public static StructureVillagePieces.Hall func_175857_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 9, 7, 11, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.Hall(var0, var7, var2, var8, var6) : null;
      }

      public Hall(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }

      protected int func_180779_c(int var1, int var2) {
         return var1 == 0 ? 4 : super.func_180779_c(var1, var2);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 0, 6, 8, 0, 10, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, 0, 6, var3);
         this.fillWithBlocks(var1, var3, 2, 1, 6, 2, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 1, 6, 8, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 10, 7, 1, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 0, 0, 8, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 5, 7, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 5, 7, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 4, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 0, 4, 2, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 0, 4, 3, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 4, 2, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 4, 3, var3);
         int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
         int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

         int var6;
         int var7;
         for(var6 = -1; var6 <= 2; ++var6) {
            for(var7 = 0; var7 <= 8; ++var7) {
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var4), var7, 4 + var6, var6, var3);
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var5), var7, 4 + var6, 5 - var6, var3);
            }
         }

         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 2, 1, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 2, 4, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 1, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 5, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 3, 2, 5, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 2, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 6, 2, 5, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 2, 1, 3, var3);
         this.setBlockState(var1, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 3, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 1, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 3, var3);
         this.fillWithBlocks(var1, var3, 5, 0, 1, 7, 0, 3, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
         this.setBlockState(var1, Blocks.double_stone_slab.getDefaultState(), 6, 1, 1, var3);
         this.setBlockState(var1, Blocks.double_stone_slab.getDefaultState(), 6, 1, 2, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 1, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 2, 0, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
         if (this.getBlockStateFromPos(var1, 2, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 2, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, var3);
         }

         this.setBlockState(var1, Blocks.air.getDefaultState(), 6, 1, 5, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 6, 2, 5, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 6, 3, 4, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 6, 1, 5, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));

         for(var6 = 0; var6 < 5; ++var6) {
            for(var7 = 0; var7 < 9; ++var7) {
               this.clearCurrentPositionBlocksUpwards(var1, var7, 7, var6, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var7, -1, var6, var3);
            }
         }

         this.spawnVillagers(var1, var3, 4, 1, 2, 2);
         return true;
      }
   }

   public static class House3 extends StructureVillagePieces.Village {
      public House3(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 7 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 7, 4, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 1, 6, 8, 4, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 0, 5, 8, 0, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 1, 7, 0, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 0, 3, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 0, 0, 8, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 0, 7, 2, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 5, 2, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 0, 6, 2, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 0, 10, 7, 3, 10, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 0, 7, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 5, 2, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 1, 8, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 4, 3, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 5, 2, 8, 5, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 0, 4, 2, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 0, 4, 3, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 4, 2, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 4, 3, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 4, 4, var3);
         int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
         int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

         int var6;
         int var7;
         for(var6 = -1; var6 <= 2; ++var6) {
            for(var7 = 0; var7 <= 8; ++var7) {
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var4), var7, 4 + var6, var6, var3);
               if ((var6 > -1 || var7 <= 1) && (var6 > 0 || var7 <= 3) && (var6 > 1 || var7 <= 4 || var7 >= 6)) {
                  this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var5), var7, 4 + var6, 5 - var6, var3);
               }
            }
         }

         this.fillWithBlocks(var1, var3, 3, 4, 5, 3, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 7, 4, 2, 7, 4, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 5, 4, 4, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 6, 5, 4, 6, 5, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 5, 6, 3, 5, 6, 10, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         var6 = this.getMetadataWithOffset(Blocks.oak_stairs, 0);

         int var8;
         for(var7 = 4; var7 >= 1; --var7) {
            this.setBlockState(var1, Blocks.planks.getDefaultState(), var7, 2 + var7, 7 - var7, var3);

            for(var8 = 8 - var7; var8 <= 10; ++var8) {
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var6), var7, 2 + var7, var8, var3);
            }
         }

         var7 = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 6, 6, 3, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 7, 5, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var7), 6, 6, 4, var3);

         int var9;
         for(var8 = 6; var8 <= 8; ++var8) {
            for(var9 = 5; var9 <= 10; ++var9) {
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var7), var8, 12 - var8, var9, var3);
            }
         }

         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 2, 1, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 2, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 3, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 4, 2, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 2, 0, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 6, 2, 0, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 1, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 3, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 4, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 2, 5, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 6, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 7, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 8, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 8, 2, 9, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 2, 2, 6, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 7, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 8, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 2, 2, 9, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 4, 4, 10, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 4, 10, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 6, 4, 10, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 5, 5, 10, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 1, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 2, 0, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
         this.fillWithBlocks(var1, var3, 1, 0, -1, 3, 2, -1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         if (this.getBlockStateFromPos(var1, 2, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 2, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, var3);
         }

         for(var8 = 0; var8 < 5; ++var8) {
            for(var9 = 0; var9 < 9; ++var9) {
               this.clearCurrentPositionBlocksUpwards(var1, var9, 7, var8, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var9, -1, var8, var3);
            }
         }

         for(var8 = 5; var8 < 11; ++var8) {
            for(var9 = 2; var9 < 9; ++var9) {
               this.clearCurrentPositionBlocksUpwards(var1, var9, 7, var8, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var9, -1, var8, var3);
            }
         }

         this.spawnVillagers(var1, var3, 4, 1, 2, 2);
         return true;
      }

      public House3() {
      }

      public static StructureVillagePieces.House3 func_175849_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 9, 7, 12, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.House3(var0, var7, var2, var8, var6) : null;
      }
   }

   public static class House2 extends StructureVillagePieces.Village {
      private static final List villageBlacksmithChestContents;
      private boolean hasMadeChest;

      public House2() {
      }

      static {
         villageBlacksmithChestContents = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.obsidian), 0, 3, 7, 5), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)));
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Chest", this.hasMadeChest);
      }

      public static StructureVillagePieces.House2 func_175855_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 10, 6, 7, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.House2(var0, var7, var2, var8, var6) : null;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.hasMadeChest = var1.getBoolean("Chest");
      }

      protected int func_180779_c(int var1, int var2) {
         return 3;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 0, 1, 0, 9, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 9, 0, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 0, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 5, 0, 9, 5, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 5, 1, 8, 5, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 0, 0, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 0, 3, 4, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 6, 0, 4, 6, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 3, 3, 1, var3);
         this.fillWithBlocks(var1, var3, 3, 1, 2, 3, 3, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 1, 3, 5, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 6, 5, 3, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 5, 1, 0, 5, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, 1, 0, 9, 3, 0, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 6, 1, 4, 9, 4, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.setBlockState(var1, Blocks.flowing_lava.getDefaultState(), 7, 1, 5, var3);
         this.setBlockState(var1, Blocks.flowing_lava.getDefaultState(), 8, 1, 5, var3);
         this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), 9, 2, 5, var3);
         this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), 9, 2, 4, var3);
         this.fillWithBlocks(var1, var3, 7, 2, 4, 8, 2, 5, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, 1, 3, var3);
         this.setBlockState(var1, Blocks.furnace.getDefaultState(), 6, 2, 3, var3);
         this.setBlockState(var1, Blocks.furnace.getDefaultState(), 6, 3, 3, var3);
         this.setBlockState(var1, Blocks.double_stone_slab.getDefaultState(), 8, 1, 1, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 6, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 2, 6, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 2, 1, 4, var3);
         this.setBlockState(var1, Blocks.wooden_pressure_plate.getDefaultState(), 2, 2, 4, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 1, 1, 5, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1, 5, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), 1, 1, 4, var3);
         if (!this.hasMadeChest && var3.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)))) {
            this.hasMadeChest = true;
            this.generateChestContents(var1, var3, var2, 5, 1, 5, villageBlacksmithChestContents, 3 + var2.nextInt(6));
         }

         int var4;
         for(var4 = 6; var4 <= 8; ++var4) {
            if (this.getBlockStateFromPos(var1, var4, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, var4, -1, -1, var3).getBlock().getMaterial() != Material.air) {
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), var4, 0, -1, var3);
            }
         }

         for(var4 = 0; var4 < 7; ++var4) {
            for(int var5 = 0; var5 < 10; ++var5) {
               this.clearCurrentPositionBlocksUpwards(var1, var5, 6, var4, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var5, -1, var4, var3);
            }
         }

         this.spawnVillagers(var1, var3, 7, 1, 1, 1);
         return true;
      }

      public House2(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }
   }

   public static class Torch extends StructureVillagePieces.Village {
      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 0, 0, 0, 2, 3, 1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 0, 0, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 1, 0, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 2, 0, var3);
         this.setBlockState(var1, Blocks.wool.getStateFromMeta(EnumDyeColor.WHITE.getDyeDamage()), 1, 3, 0, var3);
         boolean var4 = this.coordBaseMode == EnumFacing.EAST || this.coordBaseMode == EnumFacing.NORTH;
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), var4 ? 2 : 0, 3, 0, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 1, 3, 1, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), var4 ? 0 : 2, 3, 0, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 1, 3, -1, var3);
         return true;
      }

      public static StructureBoundingBox func_175856_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 3, 4, 2, var6);
         return StructureComponent.findIntersecting(var1, var7) != null ? null : var7;
      }

      public Torch(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }

      public Torch() {
      }
   }

   public static class Field2 extends StructureVillagePieces.Village {
      private Block cropTypeA;
      private Block cropTypeB;

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
         var1.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.cropTypeA = Block.getBlockById(var1.getInteger("CA"));
         this.cropTypeB = Block.getBlockById(var1.getInteger("CB"));
      }

      public Field2(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
         this.cropTypeA = this.func_151560_a(var3);
         this.cropTypeB = this.func_151560_a(var3);
      }

      public Field2() {
      }

      public static StructureVillagePieces.Field2 func_175852_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 7, 4, 9, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.Field2(var0, var7, var2, var8, var6) : null;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 0, 1, 0, 6, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 0, 5, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 8, 5, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);

         int var4;
         for(var4 = 1; var4 <= 7; ++var4) {
            this.setBlockState(var1, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 1, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 2, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 4, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 5, 1, var4, var3);
         }

         for(var4 = 0; var4 < 9; ++var4) {
            for(int var5 = 0; var5 < 7; ++var5) {
               this.clearCurrentPositionBlocksUpwards(var1, var5, 4, var4, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.dirt.getDefaultState(), var5, -1, var4, var3);
            }
         }

         return true;
      }

      private Block func_151560_a(Random var1) {
         switch(var1.nextInt(5)) {
         case 0:
            return Blocks.carrots;
         case 1:
            return Blocks.potatoes;
         default:
            return Blocks.wheat;
         }
      }
   }

   public static class Field1 extends StructureVillagePieces.Village {
      private Block cropTypeD;
      private Block cropTypeC;
      private Block cropTypeA;
      private Block cropTypeB;

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("CA", Block.blockRegistry.getIDForObject(this.cropTypeA));
         var1.setInteger("CB", Block.blockRegistry.getIDForObject(this.cropTypeB));
         var1.setInteger("CC", Block.blockRegistry.getIDForObject(this.cropTypeC));
         var1.setInteger("CD", Block.blockRegistry.getIDForObject(this.cropTypeD));
      }

      private Block func_151559_a(Random var1) {
         switch(var1.nextInt(5)) {
         case 0:
            return Blocks.carrots;
         case 1:
            return Blocks.potatoes;
         default:
            return Blocks.wheat;
         }
      }

      public Field1() {
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.cropTypeA = Block.getBlockById(var1.getInteger("CA"));
         this.cropTypeB = Block.getBlockById(var1.getInteger("CB"));
         this.cropTypeC = Block.getBlockById(var1.getInteger("CC"));
         this.cropTypeD = Block.getBlockById(var1.getInteger("CD"));
      }

      public static StructureVillagePieces.Field1 func_175851_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 13, 4, 9, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.Field1(var0, var7, var2, var8, var6) : null;
      }

      public Field1(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
         this.cropTypeA = this.func_151559_a(var3);
         this.cropTypeB = this.func_151559_a(var3);
         this.cropTypeC = this.func_151559_a(var3);
         this.cropTypeD = this.func_151559_a(var3);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 0, 1, 0, 12, 4, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 1, 2, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 0, 1, 5, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 7, 0, 1, 8, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 10, 0, 1, 11, 0, 7, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 6, 0, 0, 6, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 0, 1, 3, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, 0, 1, 9, 0, 7, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);

         int var4;
         for(var4 = 1; var4 <= 7; ++var4) {
            this.setBlockState(var1, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 1, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeA.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 2, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 4, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeB.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 5, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 7, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeC.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 8, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 10, 1, var4, var3);
            this.setBlockState(var1, this.cropTypeD.getStateFromMeta(MathHelper.getRandomIntegerInRange(var2, 2, 7)), 11, 1, var4, var3);
         }

         for(var4 = 0; var4 < 9; ++var4) {
            for(int var5 = 0; var5 < 13; ++var5) {
               this.clearCurrentPositionBlocksUpwards(var1, var5, 4, var4, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.dirt.getDefaultState(), var5, -1, var4, var3);
            }
         }

         return true;
      }
   }

   public static class House4Garden extends StructureVillagePieces.Village {
      private boolean isRoofAccessible;

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.isRoofAccessible = var1.getBoolean("Terrace");
      }

      public static StructureVillagePieces.House4Garden func_175858_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 5, 6, 5, var6);
         return StructureComponent.findIntersecting(var1, var8) != null ? null : new StructureVillagePieces.House4Garden(var0, var7, var2, var8, var6);
      }

      public House4Garden(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
         this.isRoofAccessible = var3.nextBoolean();
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 0, 0, 0, 4, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 0, 4, 4, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 4, 1, 3, 4, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 1, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 2, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 3, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 1, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 2, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 3, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 1, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 2, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 3, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 1, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 2, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 3, 4, var3);
         this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 1, 1, 4, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 4, 3, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 2, 2, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 1, 1, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 1, 2, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 1, 3, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 2, 3, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 3, 3, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 3, 2, 0, var3);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 3, 1, 0, var3);
         if (this.getBlockStateFromPos(var1, 2, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 2, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, var3);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 3, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         if (this.isRoofAccessible) {
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 0, 5, 0, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 5, 0, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 2, 5, 0, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 3, 5, 0, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 5, 0, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 0, 5, 4, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 5, 4, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 2, 5, 4, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 3, 5, 4, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 5, 4, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 5, 1, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 5, 2, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 5, 3, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 0, 5, 1, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 0, 5, 2, var3);
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 0, 5, 3, var3);
         }

         int var4;
         if (this.isRoofAccessible) {
            var4 = this.getMetadataWithOffset(Blocks.ladder, 3);
            this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var4), 3, 1, 3, var3);
            this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var4), 3, 2, 3, var3);
            this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var4), 3, 3, 3, var3);
            this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var4), 3, 4, 3, var3);
         }

         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 3, 1, var3);

         for(var4 = 0; var4 < 5; ++var4) {
            for(int var5 = 0; var5 < 5; ++var5) {
               this.clearCurrentPositionBlocksUpwards(var1, var5, 6, var4, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var5, -1, var4, var3);
            }
         }

         this.spawnVillagers(var1, var3, 1, 1, 2, 1);
         return true;
      }

      public House4Garden() {
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Terrace", this.isRoofAccessible);
      }
   }

   public static class Start extends StructureVillagePieces.Well {
      public List structureVillageWeightedPieceList;
      public boolean inDesert;
      public StructureVillagePieces.PieceWeight structVillagePieceWeight;
      public int terrainType;
      public List field_74930_j = Lists.newArrayList();
      public WorldChunkManager worldChunkMngr;
      public List field_74932_i = Lists.newArrayList();

      public Start(WorldChunkManager var1, int var2, Random var3, int var4, int var5, List var6, int var7) {
         super((StructureVillagePieces.Start)null, 0, var3, var4, var5);
         this.worldChunkMngr = var1;
         this.structureVillageWeightedPieceList = var6;
         this.terrainType = var7;
         BiomeGenBase var8 = var1.getBiomeGenerator(new BlockPos(var4, 0, var5), BiomeGenBase.field_180279_ad);
         this.inDesert = var8 == BiomeGenBase.desert || var8 == BiomeGenBase.desertHills;
         this.func_175846_a(this.inDesert);
      }

      public WorldChunkManager getWorldChunkManager() {
         return this.worldChunkMngr;
      }

      public Start() {
      }
   }

   public static class Well extends StructureVillagePieces.Village {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 0, 1, 4, 12, 4, Blocks.cobblestone.getDefaultState(), Blocks.flowing_water.getDefaultState(), false);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 12, 2, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 3, 12, 2, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 12, 3, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 3, 12, 3, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 13, 1, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 14, 1, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 13, 1, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 14, 1, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 13, 4, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 1, 14, 4, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 13, 4, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 14, 4, var3);
         this.fillWithBlocks(var1, var3, 1, 15, 1, 4, 15, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);

         for(int var4 = 0; var4 <= 5; ++var4) {
            for(int var5 = 0; var5 <= 5; ++var5) {
               if (var5 == 0 || var5 == 5 || var4 == 0 || var4 == 5) {
                  this.setBlockState(var1, Blocks.gravel.getDefaultState(), var5, 11, var4, var3);
                  this.clearCurrentPositionBlocksUpwards(var1, var5, 12, var4, var3);
               }
            }
         }

         return true;
      }

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.values().length];

            try {
               var0[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            try {
               var0[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
            return var0;
         }
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
         StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
         StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
         StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
      }

      public Well(StructureVillagePieces.Start var1, int var2, Random var3, int var4, int var5) {
         super(var1, var2);
         this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(var3);
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
         case 3:
         case 4:
            this.boundingBox = new StructureBoundingBox(var4, 64, var5, var4 + 6 - 1, 78, var5 + 6 - 1);
            break;
         default:
            this.boundingBox = new StructureBoundingBox(var4, 64, var5, var4 + 6 - 1, 78, var5 + 6 - 1);
         }

      }

      public Well() {
      }
   }

   public abstract static class Road extends StructureVillagePieces.Village {
      protected Road(StructureVillagePieces.Start var1, int var2) {
         super(var1, var2);
      }

      public Road() {
      }
   }

   public static class PieceWeight {
      public final int villagePieceWeight;
      public Class villagePieceClass;
      public int villagePiecesLimit;
      public int villagePiecesSpawned;

      public PieceWeight(Class var1, int var2, int var3) {
         this.villagePieceClass = var1;
         this.villagePieceWeight = var2;
         this.villagePiecesLimit = var3;
      }

      public boolean canSpawnMoreVillagePiecesOfType(int var1) {
         return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
      }

      public boolean canSpawnMoreVillagePieces() {
         return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
      }
   }

   public static class Church extends StructureVillagePieces.Village {
      public Church(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 12 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 3, 3, 7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 5, 1, 3, 9, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 0, 3, 0, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 0, 3, 10, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 1, 1, 4, 10, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 4, 0, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 4, 0, 4, 4, 4, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 8, 3, 4, 8, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 5, 4, 3, 10, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 5, 5, 3, 5, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 9, 0, 4, 9, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 4, 0, 4, 4, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 0, 11, 2, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 11, 2, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 2, 11, 0, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 2, 11, 4, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 1, 1, 6, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 1, 1, 7, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 2, 1, 7, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 3, 1, 6, var3);
         this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 3, 1, 7, var3);
         this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 1, 5, var3);
         this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 1, 6, var3);
         this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 3, 1, 5, var3);
         this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 1, 2, 7, var3);
         this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 0)), 3, 2, 7, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 3, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 3, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 6, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 7, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 6, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 7, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 6, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 7, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 6, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 7, 4, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 3, 6, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 3, 6, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 3, 8, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.getOpposite()), 2, 4, 7, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateY()), 1, 4, 6, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode.rotateYCCW()), 3, 4, 6, var3);
         this.setBlockState(var1, Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, this.coordBaseMode), 2, 4, 5, var3);
         int var4 = this.getMetadataWithOffset(Blocks.ladder, 4);

         int var5;
         for(var5 = 1; var5 <= 9; ++var5) {
            this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var4), 3, var5, 3, var3);
         }

         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 1, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 2, 2, 0, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 2, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
         if (this.getBlockStateFromPos(var1, 2, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 2, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 2, 0, -1, var3);
         }

         for(var5 = 0; var5 < 9; ++var5) {
            for(int var6 = 0; var6 < 5; ++var6) {
               this.clearCurrentPositionBlocksUpwards(var1, var6, 12, var5, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var6, -1, var5, var3);
            }
         }

         this.spawnVillagers(var1, var3, 2, 1, 2, 1);
         return true;
      }

      protected int func_180779_c(int var1, int var2) {
         return 2;
      }

      public static StructureVillagePieces.Church func_175854_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 5, 12, 9, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.Church(var0, var7, var2, var8, var6) : null;
      }

      public Church() {
      }
   }

   public static class House1 extends StructureVillagePieces.Village {
      public static StructureVillagePieces.House1 func_175850_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 9, 9, 6, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.House1(var0, var7, var2, var8, var6) : null;
      }

      protected int func_180779_c(int var1, int var2) {
         return 1;
      }

      public House1(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 7, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 8, 0, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 5, 0, 8, 5, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 6, 1, 8, 6, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 7, 2, 8, 7, 3, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         int var4 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
         int var5 = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

         int var6;
         int var7;
         for(var6 = -1; var6 <= 2; ++var6) {
            for(var7 = 0; var7 <= 8; ++var7) {
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var4), var7, 6 + var6, var6, var3);
               this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var5), var7, 6 + var6, 5 - var6, var3);
            }
         }

         this.fillWithBlocks(var1, var3, 0, 1, 0, 0, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 5, 8, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 1, 0, 8, 1, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 2, 1, 0, 7, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 2, 0, 0, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 2, 5, 0, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 2, 5, 8, 4, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 2, 0, 8, 4, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 2, 1, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 5, 7, 4, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 8, 2, 1, 8, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 2, 0, 7, 4, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 2, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 2, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 6, 2, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 4, 3, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 3, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 6, 3, 0, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 3, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 3, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 2, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 3, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 8, 3, 3, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 2, 2, 5, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 3, 2, 5, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 5, 2, 5, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 6, 2, 5, var3);
         this.fillWithBlocks(var1, var3, 1, 4, 1, 7, 4, 1, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 4, 4, 7, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 3, 4, 7, 3, 4, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
         this.setBlockState(var1, Blocks.planks.getDefaultState(), 7, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), 7, 1, 3, var3);
         var6 = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var6), 6, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var6), 5, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var6), 4, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_stairs.getStateFromMeta(var6), 3, 1, 4, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 6, 1, 3, var3);
         this.setBlockState(var1, Blocks.wooden_pressure_plate.getDefaultState(), 6, 2, 3, var3);
         this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 4, 1, 3, var3);
         this.setBlockState(var1, Blocks.wooden_pressure_plate.getDefaultState(), 4, 2, 3, var3);
         this.setBlockState(var1, Blocks.crafting_table.getDefaultState(), 7, 1, 1, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 1, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 2, 0, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
         if (this.getBlockStateFromPos(var1, 1, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 1, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, var3);
         }

         for(var7 = 0; var7 < 6; ++var7) {
            for(int var8 = 0; var8 < 9; ++var8) {
               this.clearCurrentPositionBlocksUpwards(var1, var8, 9, var7, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var8, -1, var7, var3);
            }
         }

         this.spawnVillagers(var1, var3, 2, 1, 2, 1);
         return true;
      }

      public House1() {
      }
   }

   public static class WoodHut extends StructureVillagePieces.Village {
      private int tablePosition;
      private boolean isTallHouse;

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("T", this.tablePosition);
         var1.setBoolean("C", this.isTallHouse);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.field_143015_k < 0) {
            this.field_143015_k = this.getAverageGroundLevel(var1, var3);
            if (this.field_143015_k < 0) {
               return true;
            }

            this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 6 - 1, 0);
         }

         this.fillWithBlocks(var1, var3, 1, 1, 1, 3, 5, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 0, 0, 3, 0, 4, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 0, 1, 2, 0, 3, Blocks.dirt.getDefaultState(), Blocks.dirt.getDefaultState(), false);
         if (this.isTallHouse) {
            this.fillWithBlocks(var1, var3, 1, 4, 1, 2, 4, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         } else {
            this.fillWithBlocks(var1, var3, 1, 5, 1, 2, 5, 3, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         }

         this.setBlockState(var1, Blocks.log.getDefaultState(), 1, 4, 0, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 2, 4, 0, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 1, 4, 4, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 2, 4, 4, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 4, 1, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 4, 2, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 0, 4, 3, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 3, 4, 1, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 3, 4, 2, var3);
         this.setBlockState(var1, Blocks.log.getDefaultState(), 3, 4, 3, var3);
         this.fillWithBlocks(var1, var3, 0, 1, 0, 0, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 0, 3, 3, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 4, 0, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 4, 3, 3, 4, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 3, 1, 1, 3, 3, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 0, 2, 3, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 1, 1, 4, 2, 3, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 0, 2, 2, var3);
         this.setBlockState(var1, Blocks.glass_pane.getDefaultState(), 3, 2, 2, var3);
         if (this.tablePosition > 0) {
            this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), this.tablePosition, 1, 3, var3);
            this.setBlockState(var1, Blocks.wooden_pressure_plate.getDefaultState(), this.tablePosition, 2, 3, var3);
         }

         this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 1, 0, var3);
         this.setBlockState(var1, Blocks.air.getDefaultState(), 1, 2, 0, var3);
         this.placeDoorCurrentPosition(var1, var3, var2, 1, 1, 0, EnumFacing.getHorizontal(this.getMetadataWithOffset(Blocks.oak_door, 1)));
         if (this.getBlockStateFromPos(var1, 1, 0, -1, var3).getBlock().getMaterial() == Material.air && this.getBlockStateFromPos(var1, 1, -1, -1, var3).getBlock().getMaterial() != Material.air) {
            this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 3)), 1, 0, -1, var3);
         }

         for(int var4 = 0; var4 < 5; ++var4) {
            for(int var5 = 0; var5 < 4; ++var5) {
               this.clearCurrentPositionBlocksUpwards(var1, var5, 6, var4, var3);
               this.replaceAirAndLiquidDownwards(var1, Blocks.cobblestone.getDefaultState(), var5, -1, var4, var3);
            }
         }

         this.spawnVillagers(var1, var3, 1, 1, 2, 1);
         return true;
      }

      public WoodHut(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
         this.isTallHouse = var3.nextBoolean();
         this.tablePosition = var3.nextInt(3);
      }

      public WoodHut() {
      }

      public static StructureVillagePieces.WoodHut func_175853_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
         StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 4, 6, 5, var6);
         return canVillageGoDeeper(var8) && StructureComponent.findIntersecting(var1, var8) == null ? new StructureVillagePieces.WoodHut(var0, var7, var2, var8, var6) : null;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.tablePosition = var1.getInteger("T");
         this.isTallHouse = var1.getBoolean("C");
      }
   }

   public static class Path extends StructureVillagePieces.Road {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      private int length;

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         boolean var4 = false;

         int var5;
         StructureComponent var6;
         for(var5 = var3.nextInt(5); var5 < this.length - 8; var5 += 2 + var3.nextInt(5)) {
            var6 = this.getNextComponentNN((StructureVillagePieces.Start)var1, var2, var3, 0, var5);
            if (var6 != null) {
               var5 += Math.max(var6.boundingBox.getXSize(), var6.boundingBox.getZSize());
               var4 = true;
            }
         }

         for(var5 = var3.nextInt(5); var5 < this.length - 8; var5 += 2 + var3.nextInt(5)) {
            var6 = this.getNextComponentPP((StructureVillagePieces.Start)var1, var2, var3, 0, var5);
            if (var6 != null) {
               var5 += Math.max(var6.boundingBox.getXSize(), var6.boundingBox.getZSize());
               var4 = true;
            }
         }

         if (var4 && var3.nextInt(3) > 0 && this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
               break;
            case 4:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
               break;
            case 5:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
               break;
            case 6:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            }
         }

         if (var4 && var3.nextInt(3) > 0 && this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
               break;
            case 4:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
               break;
            case 5:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
               break;
            case 6:
               StructureVillagePieces.access$0((StructureVillagePieces.Start)var1, var2, var3, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
            }
         }

      }

      public Path(StructureVillagePieces.Start var1, int var2, Random var3, StructureBoundingBox var4, EnumFacing var5) {
         super(var1, var2);
         this.coordBaseMode = var5;
         this.boundingBox = var4;
         this.length = Math.max(var4.getXSize(), var4.getZSize());
      }

      public static StructureBoundingBox func_175848_a(StructureVillagePieces.Start var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6) {
         for(int var7 = 7 * MathHelper.getRandomIntegerInRange(var2, 3, 5); var7 >= 7; var7 -= 7) {
            StructureBoundingBox var8 = StructureBoundingBox.getComponentToAddBoundingBox(var3, var4, var5, 0, 0, 0, 3, 3, var7, var6);
            if (StructureComponent.findIntersecting(var1, var8) == null) {
               return var8;
            }
         }

         return null;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.length = var1.getInteger("Length");
      }

      public Path() {
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("Length", this.length);
      }

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.values().length];

            try {
               var0[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            try {
               var0[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var1) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
            return var0;
         }
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         IBlockState var4 = this.func_175847_a(Blocks.gravel.getDefaultState());
         IBlockState var5 = this.func_175847_a(Blocks.cobblestone.getDefaultState());

         for(int var6 = this.boundingBox.minX; var6 <= this.boundingBox.maxX; ++var6) {
            for(int var7 = this.boundingBox.minZ; var7 <= this.boundingBox.maxZ; ++var7) {
               BlockPos var8 = new BlockPos(var6, 64, var7);
               if (var3.isVecInside(var8)) {
                  var8 = var1.getTopSolidOrLiquidBlock(var8).down();
                  var1.setBlockState(var8, var4, 2);
                  var1.setBlockState(var8.down(), var5, 2);
               }
            }
         }

         return true;
      }
   }
}
