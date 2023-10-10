package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public class StructureStrongholdPieces {
   private static Class strongComponentType;
   private static final StructureStrongholdPieces.Stones strongholdStones = new StructureStrongholdPieces.Stones((StructureStrongholdPieces.Stones)null);
   private static List structurePieceList;
   static int totalWeight;
   private static final StructureStrongholdPieces.PieceWeight[] pieceWeightArray = new StructureStrongholdPieces.PieceWeight[]{new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Straight.class, 40, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Prison.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.LeftTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RightTurn.class, 20, 0), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.RoomCrossing.class, 10, 6), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.StairsStraight.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Stairs.class, 5, 5), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Crossing.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.ChestCorridor.class, 5, 4), new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.Library.class, 10, 2) {
      public boolean canSpawnMoreStructuresOfType(int var1) {
         return super.canSpawnMoreStructuresOfType(var1) && var1 > 4;
      }
   }, new StructureStrongholdPieces.PieceWeight(StructureStrongholdPieces.PortalRoom.class, 20, 1) {
      public boolean canSpawnMoreStructuresOfType(int var1) {
         return super.canSpawnMoreStructuresOfType(var1) && var1 > 5;
      }
   }};

   private static StructureStrongholdPieces.Stronghold func_175954_a(Class var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      Object var8 = null;
      if (var0 == StructureStrongholdPieces.Straight.class) {
         var8 = StructureStrongholdPieces.Straight.func_175862_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.Prison.class) {
         var8 = StructureStrongholdPieces.Prison.func_175860_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.LeftTurn.class) {
         var8 = StructureStrongholdPieces.LeftTurn.func_175867_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.RightTurn.class) {
         var8 = StructureStrongholdPieces.RightTurn.func_175867_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.RoomCrossing.class) {
         var8 = StructureStrongholdPieces.RoomCrossing.func_175859_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.StairsStraight.class) {
         var8 = StructureStrongholdPieces.StairsStraight.func_175861_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.Stairs.class) {
         var8 = StructureStrongholdPieces.Stairs.func_175863_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.Crossing.class) {
         var8 = StructureStrongholdPieces.Crossing.func_175866_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.ChestCorridor.class) {
         var8 = StructureStrongholdPieces.ChestCorridor.func_175868_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.Library.class) {
         var8 = StructureStrongholdPieces.Library.func_175864_a(var1, var2, var3, var4, var5, var6, var7);
      } else if (var0 == StructureStrongholdPieces.PortalRoom.class) {
         var8 = StructureStrongholdPieces.PortalRoom.func_175865_a(var1, var2, var3, var4, var5, var6, var7);
      }

      return (StructureStrongholdPieces.Stronghold)var8;
   }

   private static StructureStrongholdPieces.Stronghold func_175955_b(StructureStrongholdPieces.Stairs2 param0, List param1, Random param2, int param3, int param4, int param5, EnumFacing param6, int param7) {
      // $FF: Couldn't be decompiled
   }

   public static void prepareStructurePieces() {
      structurePieceList = Lists.newArrayList();
      StructureStrongholdPieces.PieceWeight[] var3;
      int var2 = (var3 = pieceWeightArray).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         StructureStrongholdPieces.PieceWeight var0 = var3[var1];
         var0.instancesSpawned = 0;
         structurePieceList.add(var0);
      }

      strongComponentType = null;
   }

   static Class access$1() {
      return strongComponentType;
   }

   static void access$2(Class var0) {
      strongComponentType = var0;
   }

   static StructureStrongholdPieces.Stones access$0() {
      return strongholdStones;
   }

   public static void registerStrongholdPieces() {
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.ChestCorridor.class, "SHCC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Corridor.class, "SHFC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Crossing.class, "SH5C");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.LeftTurn.class, "SHLT");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Library.class, "SHLi");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.PortalRoom.class, "SHPR");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Prison.class, "SHPH");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RightTurn.class, "SHRT");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.RoomCrossing.class, "SHRC");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs.class, "SHSD");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Stairs2.class, "SHStart");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.Straight.class, "SHS");
      MapGenStructureIO.registerStructureComponent(StructureStrongholdPieces.StairsStraight.class, "SHSSD");
   }

   static StructureComponent access$3(StructureStrongholdPieces.Stairs2 var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      return func_175953_c(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static StructureComponent func_175953_c(StructureStrongholdPieces.Stairs2 var0, List var1, Random var2, int var3, int var4, int var5, EnumFacing var6, int var7) {
      if (var7 > 50) {
         return null;
      } else if (Math.abs(var3 - var0.getBoundingBox().minX) <= 112 && Math.abs(var5 - var0.getBoundingBox().minZ) <= 112) {
         StructureStrongholdPieces.Stronghold var8 = func_175955_b(var0, var1, var2, var3, var4, var5, var6, var7 + 1);
         if (var8 != null) {
            var1.add(var8);
            var0.field_75026_c.add(var8);
         }

         return var8;
      } else {
         return null;
      }
   }

   static class Stones extends StructureComponent.BlockSelector {
      Stones(StructureStrongholdPieces.Stones var1) {
         this();
      }

      public void selectBlocks(Random var1, int var2, int var3, int var4, boolean var5) {
         if (var5) {
            float var6 = var1.nextFloat();
            if (var6 < 0.2F) {
               this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META);
            } else if (var6 < 0.5F) {
               this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META);
            } else if (var6 < 0.55F) {
               this.blockstate = Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
            } else {
               this.blockstate = Blocks.stonebrick.getDefaultState();
            }
         } else {
            this.blockstate = Blocks.air.getDefaultState();
         }

      }

      private Stones() {
      }
   }

   public static class RightTurn extends StructureStrongholdPieces.LeftTurn {
      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
         } else {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
         }

      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 4, 4, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 1, 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
               this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
               this.fillWithBlocks(var1, var3, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            return true;
         }
      }
   }

   public static class Stairs2 extends StructureStrongholdPieces.Stairs {
      public StructureStrongholdPieces.PieceWeight strongholdPieceWeight;
      public List field_75026_c = Lists.newArrayList();
      public StructureStrongholdPieces.PortalRoom strongholdPortalRoom;

      public Stairs2(int var1, Random var2, int var3, int var4) {
         super(0, var2, var3, var4);
      }

      public Stairs2() {
      }

      public BlockPos getBoundingBoxCenter() {
         return this.strongholdPortalRoom != null ? this.strongholdPortalRoom.getBoundingBoxCenter() : super.getBoundingBoxCenter();
      }
   }

   public static class StairsStraight extends StructureStrongholdPieces.Stronghold {
      public StairsStraight() {
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 10, 7, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 7, 0);
            this.placeDoor(var1, var2, var3, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 7);
            int var4 = this.getMetadataWithOffset(Blocks.stone_stairs, 2);

            for(int var5 = 0; var5 < 6; ++var5) {
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 1, 6 - var5, 1 + var5, var3);
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 2, 6 - var5, 1 + var5, var3);
               this.setBlockState(var1, Blocks.stone_stairs.getStateFromMeta(var4), 3, 6 - var5, 1 + var5, var3);
               if (var5 < 5) {
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 5 - var5, 1 + var5, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 5 - var5, 1 + var5, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 5 - var5, 1 + var5, var3);
               }
            }

            return true;
         }
      }

      public static StructureStrongholdPieces.StairsStraight func_175861_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -7, 0, 5, 11, 8, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.StairsStraight(var6, var1, var7, var5) : null;
      }

      public StairsStraight(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
      }
   }

   public static class Stairs extends StructureStrongholdPieces.Stronghold {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      private boolean field_75024_a;

      public Stairs(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.field_75024_a = false;
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
      }

      public static StructureStrongholdPieces.Stairs func_175863_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -7, 0, 5, 11, 5, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.Stairs(var6, var1, var7, var5) : null;
      }

      public Stairs(int var1, Random var2, int var3, int var4) {
         super(var1);
         this.field_75024_a = true;
         this.coordBaseMode = EnumFacing.Plane.HORIZONTAL.random(var2);
         this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
         switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
         case 3:
         case 4:
            this.boundingBox = new StructureBoundingBox(var3, 64, var4, var3 + 5 - 1, 74, var4 + 5 - 1);
            break;
         default:
            this.boundingBox = new StructureBoundingBox(var3, 64, var4, var3 + 5 - 1, 74, var4 + 5 - 1);
         }

      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 10, 4, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 7, 0);
            this.placeDoor(var1, var2, var3, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 4);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 6, 1, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 5, 1, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 6, 1, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 5, 2, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 4, 3, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 5, 3, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 4, 3, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 3, 3, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 4, 3, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 3, 2, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 2, 1, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 3, 3, 1, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 2, 1, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 1, 1, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 2, 1, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 1, 2, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.STONE.getMetadata()), 1, 1, 3, var3);
            return true;
         }
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Source", this.field_75024_a);
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.field_75024_a = var1.getBoolean("Source");
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         if (this.field_75024_a) {
            StructureStrongholdPieces.access$2(StructureStrongholdPieces.Crossing.class);
         }

         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
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

      public Stairs() {
      }
   }

   public static class Corridor extends StructureStrongholdPieces.Stronghold {
      private int field_74993_a;

      public Corridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.boundingBox = var3;
         this.field_74993_a = var4 != EnumFacing.NORTH && var4 != EnumFacing.SOUTH ? var3.getXSize() : var3.getZSize();
      }

      public static StructureBoundingBox func_175869_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5) {
         boolean var6 = true;
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 4, var5);
         StructureComponent var8 = StructureComponent.findIntersecting(var0, var7);
         if (var8 == null) {
            return null;
         } else {
            if (var8.getBoundingBox().minY == var7.minY) {
               for(int var9 = 3; var9 >= 1; --var9) {
                  var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, var9 - 1, var5);
                  if (!var8.getBoundingBox().intersectsWith(var7)) {
                     return StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, var9, var5);
                  }
               }
            }

            return null;
         }
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("Steps", this.field_74993_a);
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.field_74993_a = var1.getInteger("Steps");
      }

      public Corridor() {
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            for(int var4 = 0; var4 < this.field_74993_a; ++var4) {
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 0, 0, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 0, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 0, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 0, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 4, 0, var4, var3);

               for(int var5 = 1; var5 <= 3; ++var5) {
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 0, var5, var4, var3);
                  this.setBlockState(var1, Blocks.air.getDefaultState(), 1, var5, var4, var3);
                  this.setBlockState(var1, Blocks.air.getDefaultState(), 2, var5, var4, var3);
                  this.setBlockState(var1, Blocks.air.getDefaultState(), 3, var5, var4, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 4, var5, var4, var3);
               }

               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 0, 4, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 1, 4, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 2, 4, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 4, var4, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 4, 4, var4, var3);
            }

            return true;
         }
      }
   }

   abstract static class Stronghold extends StructureComponent {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door;
      protected StructureStrongholdPieces.Stronghold.Door field_143013_d;
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;

      public Stronghold() {
         this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         var1.setString("EntryDoor", this.field_143013_d.name());
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.valueOf(var1.getString("EntryDoor"));
      }

      protected StructureComponent getNextComponentNormal(StructureStrongholdPieces.Stairs2 var1, List var2, Random var3, int var4, int var5) {
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var4, this.boundingBox.minY + var5, this.boundingBox.minZ - 1, this.coordBaseMode, this.getComponentType());
            case 4:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var4, this.boundingBox.minY + var5, this.boundingBox.maxZ + 1, this.coordBaseMode, this.getComponentType());
            case 5:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var5, this.boundingBox.minZ + var4, this.coordBaseMode, this.getComponentType());
            case 6:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var5, this.boundingBox.minZ + var4, this.coordBaseMode, this.getComponentType());
            }
         }

         return null;
      }

      protected StructureStrongholdPieces.Stronghold.Door getRandomDoor(Random var1) {
         int var2 = var1.nextInt(5);
         switch(var2) {
         case 0:
         case 1:
         default:
            return StructureStrongholdPieces.Stronghold.Door.OPENING;
         case 2:
            return StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR;
         case 3:
            return StructureStrongholdPieces.Stronghold.Door.GRATES;
         case 4:
            return StructureStrongholdPieces.Stronghold.Door.IRON_DOOR;
         }
      }

      protected static boolean canStrongholdGoDeeper(StructureBoundingBox var0) {
         return var0 != null && var0.minY > 10;
      }

      protected StructureComponent getNextComponentZ(StructureStrongholdPieces.Stairs2 var1, List var2, Random var3, int var4, int var5) {
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.EAST, this.getComponentType());
            case 4:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.maxX + 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.EAST, this.getComponentType());
            case 5:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
            case 6:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
            }
         }

         return null;
      }

      protected Stronghold(int var1) {
         super(var1);
         this.field_143013_d = StructureStrongholdPieces.Stronghold.Door.OPENING;
      }

      static int[] $SWITCH_TABLE$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[StructureStrongholdPieces.Stronghold.Door.values().length];

            try {
               var0[StructureStrongholdPieces.Stronghold.Door.GRATES.ordinal()] = 3;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[StructureStrongholdPieces.Stronghold.Door.IRON_DOOR.ordinal()] = 4;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[StructureStrongholdPieces.Stronghold.Door.OPENING.ordinal()] = 1;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[StructureStrongholdPieces.Stronghold.Door.WOOD_DOOR.ordinal()] = 2;
            } catch (NoSuchFieldError var2) {
            }

            $SWITCH_TABLE$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door = var0;
            return var0;
         }
      }

      protected void placeDoor(World var1, Random var2, StructureBoundingBox var3, StructureStrongholdPieces.Stronghold.Door var4, int var5, int var6, int var7) {
         switch($SWITCH_TABLE$net$minecraft$world$gen$structure$StructureStrongholdPieces$Stronghold$Door()[var4.ordinal()]) {
         case 1:
         default:
            this.fillWithBlocks(var1, var3, var5, var6, var7, var5 + 3 - 1, var6 + 3 - 1, var7, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            break;
         case 2:
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 1, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6, var7, var3);
            this.setBlockState(var1, Blocks.oak_door.getDefaultState(), var5 + 1, var6, var7, var3);
            this.setBlockState(var1, Blocks.oak_door.getStateFromMeta(8), var5 + 1, var6 + 1, var7, var3);
            break;
         case 3:
            this.setBlockState(var1, Blocks.air.getDefaultState(), var5 + 1, var6, var7, var3);
            this.setBlockState(var1, Blocks.air.getDefaultState(), var5 + 1, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5, var6, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5 + 1, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5 + 2, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5 + 2, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), var5 + 2, var6, var7, var3);
            break;
         case 4:
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 1, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6 + 2, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), var5 + 2, var6, var7, var3);
            this.setBlockState(var1, Blocks.iron_door.getDefaultState(), var5 + 1, var6, var7, var3);
            this.setBlockState(var1, Blocks.iron_door.getStateFromMeta(8), var5 + 1, var6 + 1, var7, var3);
            this.setBlockState(var1, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 4)), var5 + 2, var6 + 1, var7 + 1, var3);
            this.setBlockState(var1, Blocks.stone_button.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_button, 3)), var5 + 2, var6 + 1, var7 - 1, var3);
         }

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

      protected StructureComponent getNextComponentX(StructureStrongholdPieces.Stairs2 var1, List var2, Random var3, int var4, int var5) {
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 3:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.WEST, this.getComponentType());
            case 4:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX - 1, this.boundingBox.minY + var4, this.boundingBox.minZ + var5, EnumFacing.WEST, this.getComponentType());
            case 5:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            case 6:
               return StructureStrongholdPieces.access$3(var1, var2, var3, this.boundingBox.minX + var5, this.boundingBox.minY + var4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            }
         }

         return null;
      }

      public static enum Door {
         GRATES,
         WOOD_DOOR;

         private static final StructureStrongholdPieces.Stronghold.Door[] ENUM$VALUES = new StructureStrongholdPieces.Stronghold.Door[]{OPENING, WOOD_DOOR, GRATES, IRON_DOOR};
         OPENING,
         IRON_DOOR;
      }
   }

   public static class Library extends StructureStrongholdPieces.Stronghold {
      private boolean isLargeRoom;
      private static final List strongholdLibraryChestContents;

      static {
         strongholdLibraryChestContents = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.book, 0, 1, 3, 20), new WeightedRandomChestContent(Items.paper, 0, 2, 7, 20), new WeightedRandomChestContent(Items.map, 0, 1, 1, 1), new WeightedRandomChestContent(Items.compass, 0, 1, 1, 1)));
      }

      public Library(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
         this.isLargeRoom = var3.getYSize() > 6;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.isLargeRoom = var1.getBoolean("Tall");
      }

      public Library() {
      }

      public static StructureStrongholdPieces.Library func_175864_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -1, 0, 14, 11, 15, var5);
         if (!canStrongholdGoDeeper(var7) || StructureComponent.findIntersecting(var0, var7) != null) {
            var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -1, 0, 14, 6, 15, var5);
            if (!canStrongholdGoDeeper(var7) || StructureComponent.findIntersecting(var0, var7) != null) {
               return null;
            }
         }

         return new StructureStrongholdPieces.Library(var6, var1, var7, var5);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            byte var4 = 11;
            if (!this.isLargeRoom) {
               var4 = 6;
            }

            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 13, var4 - 1, 14, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 4, 1, 0);
            this.func_175805_a(var1, var3, var2, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.web.getDefaultState(), Blocks.web.getDefaultState(), false);
            boolean var5 = true;
            boolean var6 = true;

            int var7;
            for(var7 = 1; var7 <= 13; ++var7) {
               if ((var7 - 1) % 4 == 0) {
                  this.fillWithBlocks(var1, var3, 1, 1, var7, 1, 4, var7, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                  this.fillWithBlocks(var1, var3, 12, 1, var7, 12, 4, var7, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                  this.setBlockState(var1, Blocks.torch.getDefaultState(), 2, 3, var7, var3);
                  this.setBlockState(var1, Blocks.torch.getDefaultState(), 11, 3, var7, var3);
                  if (this.isLargeRoom) {
                     this.fillWithBlocks(var1, var3, 1, 6, var7, 1, 9, var7, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                     this.fillWithBlocks(var1, var3, 12, 6, var7, 12, 9, var7, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
                  }
               } else {
                  this.fillWithBlocks(var1, var3, 1, 1, var7, 1, 4, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                  this.fillWithBlocks(var1, var3, 12, 1, var7, 12, 4, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                  if (this.isLargeRoom) {
                     this.fillWithBlocks(var1, var3, 1, 6, var7, 1, 9, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                     this.fillWithBlocks(var1, var3, 12, 6, var7, 12, 9, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
                  }
               }
            }

            for(var7 = 3; var7 < 12; var7 += 2) {
               this.fillWithBlocks(var1, var3, 3, 1, var7, 4, 3, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 6, 1, var7, 7, 3, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 9, 1, var7, 10, 3, var7, Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);
            }

            if (this.isLargeRoom) {
               this.fillWithBlocks(var1, var3, 1, 5, 1, 3, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 10, 5, 1, 12, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 4, 5, 1, 9, 5, 2, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 4, 5, 12, 9, 5, 13, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
               this.setBlockState(var1, Blocks.planks.getDefaultState(), 9, 5, 11, var3);
               this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 5, 11, var3);
               this.setBlockState(var1, Blocks.planks.getDefaultState(), 9, 5, 10, var3);
               this.fillWithBlocks(var1, var3, 3, 6, 2, 3, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 10, 6, 2, 10, 6, 10, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 4, 6, 2, 9, 6, 2, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
               this.fillWithBlocks(var1, var3, 4, 6, 12, 8, 6, 12, Blocks.oak_fence.getDefaultState(), Blocks.oak_fence.getDefaultState(), false);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 9, 6, 11, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 8, 6, 11, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), 9, 6, 10, var3);
               var7 = this.getMetadataWithOffset(Blocks.ladder, 3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 1, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 2, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 3, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 4, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 5, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 6, 13, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(var7), 10, 7, 13, var3);
               byte var8 = 7;
               byte var9 = 7;
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 1, 9, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8, 9, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 1, 8, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8, 8, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 1, 7, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8, 7, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 2, 7, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 + 1, 7, var9, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 1, 7, var9 - 1, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8 - 1, 7, var9 + 1, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8, 7, var9 - 1, var3);
               this.setBlockState(var1, Blocks.oak_fence.getDefaultState(), var8, 7, var9 + 1, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8 - 2, 8, var9, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8 + 1, 8, var9, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8 - 1, 8, var9 - 1, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8 - 1, 8, var9 + 1, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8, 8, var9 - 1, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), var8, 8, var9 + 1, var3);
            }

            this.generateChestContents(var1, var3, var2, 3, 3, 5, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, Items.enchanted_book.getRandom(var2, 1, 5, 2)), 1 + var2.nextInt(4));
            if (this.isLargeRoom) {
               this.setBlockState(var1, Blocks.air.getDefaultState(), 12, 9, 1, var3);
               this.generateChestContents(var1, var3, var2, 12, 8, 1, WeightedRandomChestContent.func_177629_a(strongholdLibraryChestContents, Items.enchanted_book.getRandom(var2, 1, 5, 2)), 1 + var2.nextInt(4));
            }

            return true;
         }
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Tall", this.isLargeRoom);
      }
   }

   static class PieceWeight {
      public Class pieceClass;
      public final int pieceWeight;
      public int instancesLimit;
      public int instancesSpawned;

      public PieceWeight(Class var1, int var2, int var3) {
         this.pieceClass = var1;
         this.pieceWeight = var2;
         this.instancesLimit = var3;
      }

      public boolean canSpawnMoreStructuresOfType(int var1) {
         return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
      }

      public boolean canSpawnMoreStructures() {
         return this.instancesLimit == 0 || this.instancesSpawned < this.instancesLimit;
      }
   }

   public static class Crossing extends StructureStrongholdPieces.Stronghold {
      private boolean field_74995_d;
      private boolean field_74999_h;
      private boolean field_74997_c;
      private boolean field_74996_b;

      public Crossing() {
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("leftLow", this.field_74996_b);
         var1.setBoolean("leftHigh", this.field_74997_c);
         var1.setBoolean("rightLow", this.field_74995_d);
         var1.setBoolean("rightHigh", this.field_74999_h);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 9, 8, 10, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 4, 3, 0);
            if (this.field_74996_b) {
               this.fillWithBlocks(var1, var3, 0, 3, 1, 0, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            if (this.field_74995_d) {
               this.fillWithBlocks(var1, var3, 9, 3, 1, 9, 5, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            if (this.field_74997_c) {
               this.fillWithBlocks(var1, var3, 0, 5, 7, 0, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            if (this.field_74999_h) {
               this.fillWithBlocks(var1, var3, 9, 5, 7, 9, 7, 9, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            this.fillWithBlocks(var1, var3, 5, 1, 10, 7, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithRandomizedBlocks(var1, var3, 1, 2, 1, 8, 2, 6, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 5, 4, 4, 9, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 8, 1, 5, 8, 4, 9, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 1, 4, 7, 3, 4, 9, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 1, 3, 5, 3, 3, 6, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithBlocks(var1, var3, 1, 3, 4, 3, 3, 4, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 1, 4, 6, 3, 4, 6, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithRandomizedBlocks(var1, var3, 5, 1, 7, 7, 1, 8, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithBlocks(var1, var3, 5, 1, 9, 7, 1, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 5, 2, 7, 7, 2, 7, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 4, 5, 7, 4, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 8, 5, 7, 8, 5, 9, Blocks.stone_slab.getDefaultState(), Blocks.stone_slab.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 5, 5, 7, 7, 5, 9, Blocks.double_stone_slab.getDefaultState(), Blocks.double_stone_slab.getDefaultState(), false);
            this.setBlockState(var1, Blocks.torch.getDefaultState(), 6, 5, 6, var3);
            return true;
         }
      }

      public static StructureStrongholdPieces.Crossing func_175866_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -3, 0, 10, 9, 11, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.Crossing(var6, var1, var7, var5) : null;
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.field_74996_b = var1.getBoolean("leftLow");
         this.field_74997_c = var1.getBoolean("leftHigh");
         this.field_74995_d = var1.getBoolean("rightLow");
         this.field_74999_h = var1.getBoolean("rightHigh");
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         int var4 = 3;
         int var5 = 5;
         if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.NORTH) {
            var4 = 8 - var4;
            var5 = 8 - var5;
         }

         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 5, 1);
         if (this.field_74996_b) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, var4, 1);
         }

         if (this.field_74997_c) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, var5, 7);
         }

         if (this.field_74995_d) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, var4, 1);
         }

         if (this.field_74999_h) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, var5, 7);
         }

      }

      public Crossing(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
         this.field_74996_b = var2.nextBoolean();
         this.field_74997_c = var2.nextBoolean();
         this.field_74995_d = var2.nextBoolean();
         this.field_74999_h = var2.nextInt(3) > 0;
      }
   }

   public static class ChestCorridor extends StructureStrongholdPieces.Stronghold {
      private static final List strongholdChestContents;
      private boolean hasMadeChest;

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
      }

      public ChestCorridor(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
      }

      public static StructureStrongholdPieces.ChestCorridor func_175868_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.ChestCorridor(var6, var1, var7, var5) : null;
      }

      static {
         strongholdChestContents = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.ender_pearl, 0, 1, 1, 10), new WeightedRandomChestContent(Items.diamond, 0, 1, 3, 3), new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_apple, 0, 1, 1, 1), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 1), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1)));
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 1, 0);
            this.placeDoor(var1, var2, var3, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
            this.fillWithBlocks(var1, var3, 3, 1, 2, 3, 1, 4, Blocks.stonebrick.getDefaultState(), Blocks.stonebrick.getDefaultState(), false);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 1, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 1, 5, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 2, var3);
            this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 3, 2, 4, var3);

            for(int var4 = 2; var4 <= 4; ++var4) {
               this.setBlockState(var1, Blocks.stone_slab.getStateFromMeta(BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata()), 2, 1, var4, var3);
            }

            if (!this.hasMadeChest && var3.isVecInside(new BlockPos(this.getXWithOffset(3, 3), this.getYWithOffset(2), this.getZWithOffset(3, 3)))) {
               this.hasMadeChest = true;
               this.generateChestContents(var1, var3, var2, 3, 2, 3, WeightedRandomChestContent.func_177629_a(strongholdChestContents, Items.enchanted_book.getRandom(var2)), 2 + var2.nextInt(2));
            }

            return true;
         }
      }

      public ChestCorridor() {
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.hasMadeChest = var1.getBoolean("Chest");
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Chest", this.hasMadeChest);
      }
   }

   public static class Straight extends StructureStrongholdPieces.Stronghold {
      private boolean expandsX;
      private boolean expandsZ;

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.expandsX = var1.getBoolean("Left");
         this.expandsZ = var1.getBoolean("Right");
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 4, 6, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 1, 0);
            this.placeDoor(var1, var2, var3, StructureStrongholdPieces.Stronghold.Door.OPENING, 1, 1, 6);
            this.randomlyPlaceBlock(var1, var3, var2, 0.1F, 1, 2, 1, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(var1, var3, var2, 0.1F, 3, 2, 1, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(var1, var3, var2, 0.1F, 1, 2, 5, Blocks.torch.getDefaultState());
            this.randomlyPlaceBlock(var1, var3, var2, 0.1F, 3, 2, 5, Blocks.torch.getDefaultState());
            if (this.expandsX) {
               this.fillWithBlocks(var1, var3, 0, 1, 2, 0, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            if (this.expandsZ) {
               this.fillWithBlocks(var1, var3, 4, 1, 2, 4, 3, 4, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            return true;
         }
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
         if (this.expandsX) {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 2);
         }

         if (this.expandsZ) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 2);
         }

      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Left", this.expandsX);
         var1.setBoolean("Right", this.expandsZ);
      }

      public Straight(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
         this.expandsX = var2.nextInt(2) == 0;
         this.expandsZ = var2.nextInt(2) == 0;
      }

      public Straight() {
      }

      public static StructureStrongholdPieces.Straight func_175862_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 7, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.Straight(var6, var1, var7, var5) : null;
      }
   }

   public static class LeftTurn extends StructureStrongholdPieces.Stronghold {
      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
            this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
         } else {
            this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
         }

      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 4, 4, 4, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 1, 0);
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.EAST) {
               this.fillWithBlocks(var1, var3, 4, 1, 1, 4, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
               this.fillWithBlocks(var1, var3, 0, 1, 1, 0, 3, 3, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            }

            return true;
         }
      }

      public static StructureStrongholdPieces.LeftTurn func_175867_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 5, 5, 5, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.LeftTurn(var6, var1, var7, var5) : null;
      }

      public LeftTurn() {
      }

      public LeftTurn(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
      }
   }

   public static class PortalRoom extends StructureStrongholdPieces.Stronghold {
      private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      private boolean hasSpawner;

      public PortalRoom() {
      }

      static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[EnumFacing.values().length];

            try {
               var0[EnumFacing.DOWN.ordinal()] = 1;
            } catch (NoSuchFieldError var7) {
            }

            try {
               var0[EnumFacing.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[EnumFacing.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[EnumFacing.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[EnumFacing.UP.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[EnumFacing.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError var2) {
            }

            $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
            return var0;
         }
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 10, 7, 15, false, var2, StructureStrongholdPieces.access$0());
         this.placeDoor(var1, var2, var3, StructureStrongholdPieces.Stronghold.Door.GRATES, 4, 1, 0);
         byte var4 = 6;
         this.fillWithRandomizedBlocks(var1, var3, 1, var4, 1, 1, var4, 14, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 9, var4, 1, 9, var4, 14, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 2, var4, 1, 8, var4, 2, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 2, var4, 14, 8, var4, 14, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 1, 1, 1, 2, 1, 4, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 8, 1, 1, 9, 1, 4, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithBlocks(var1, var3, 1, 1, 1, 1, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
         this.fillWithBlocks(var1, var3, 9, 1, 1, 9, 1, 3, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);
         this.fillWithRandomizedBlocks(var1, var3, 3, 1, 8, 7, 1, 12, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithBlocks(var1, var3, 4, 1, 9, 6, 1, 11, Blocks.flowing_lava.getDefaultState(), Blocks.flowing_lava.getDefaultState(), false);

         int var5;
         for(var5 = 3; var5 < 14; var5 += 2) {
            this.fillWithBlocks(var1, var3, 0, 3, var5, 0, 4, var5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 10, 3, var5, 10, 4, var5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
         }

         for(var5 = 2; var5 < 9; var5 += 2) {
            this.fillWithBlocks(var1, var3, var5, 3, 15, var5, 4, 15, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
         }

         var5 = this.getMetadataWithOffset(Blocks.stone_brick_stairs, 3);
         this.fillWithRandomizedBlocks(var1, var3, 4, 1, 5, 6, 1, 7, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 4, 2, 6, 6, 2, 7, false, var2, StructureStrongholdPieces.access$0());
         this.fillWithRandomizedBlocks(var1, var3, 4, 3, 7, 6, 3, 7, false, var2, StructureStrongholdPieces.access$0());

         int var6;
         for(var6 = 4; var6 <= 6; ++var6) {
            this.setBlockState(var1, Blocks.stone_brick_stairs.getStateFromMeta(var5), var6, 1, 4, var3);
            this.setBlockState(var1, Blocks.stone_brick_stairs.getStateFromMeta(var5), var6, 2, 5, var3);
            this.setBlockState(var1, Blocks.stone_brick_stairs.getStateFromMeta(var5), var6, 3, 6, var3);
         }

         var6 = EnumFacing.NORTH.getHorizontalIndex();
         int var7 = EnumFacing.SOUTH.getHorizontalIndex();
         int var8 = EnumFacing.EAST.getHorizontalIndex();
         int var9 = EnumFacing.WEST.getHorizontalIndex();
         if (this.coordBaseMode != null) {
            switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[this.coordBaseMode.ordinal()]) {
            case 4:
               var6 = EnumFacing.SOUTH.getHorizontalIndex();
               var7 = EnumFacing.NORTH.getHorizontalIndex();
               break;
            case 5:
               var6 = EnumFacing.WEST.getHorizontalIndex();
               var7 = EnumFacing.EAST.getHorizontalIndex();
               var8 = EnumFacing.SOUTH.getHorizontalIndex();
               var9 = EnumFacing.NORTH.getHorizontalIndex();
               break;
            case 6:
               var6 = EnumFacing.EAST.getHorizontalIndex();
               var7 = EnumFacing.WEST.getHorizontalIndex();
               var8 = EnumFacing.SOUTH.getHorizontalIndex();
               var9 = EnumFacing.NORTH.getHorizontalIndex();
            }
         }

         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var6).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 4, 3, 8, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var6).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 5, 3, 8, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var6).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 6, 3, 8, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var7).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 4, 3, 12, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var7).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 5, 3, 12, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var7).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 6, 3, 12, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var8).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 3, 3, 9, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var8).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 3, 3, 10, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var8).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 3, 3, 11, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var9).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 7, 3, 9, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var9).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 7, 3, 10, var3);
         this.setBlockState(var1, Blocks.end_portal_frame.getStateFromMeta(var9).withProperty(BlockEndPortalFrame.EYE, var2.nextFloat() > 0.9F), 7, 3, 11, var3);
         if (!this.hasSpawner) {
            int var12 = this.getYWithOffset(3);
            BlockPos var10 = new BlockPos(this.getXWithOffset(5, 6), var12, this.getZWithOffset(5, 6));
            if (var3.isVecInside(var10)) {
               this.hasSpawner = true;
               var1.setBlockState(var10, Blocks.mob_spawner.getDefaultState(), 2);
               TileEntity var11 = var1.getTileEntity(var10);
               if (var11 instanceof TileEntityMobSpawner) {
                  ((TileEntityMobSpawner)var11).getSpawnerBaseLogic().setEntityName("Silverfish");
               }
            }
         }

         return true;
      }

      public static StructureStrongholdPieces.PortalRoom func_175865_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -1, 0, 11, 8, 16, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.PortalRoom(var6, var1, var7, var5) : null;
      }

      public PortalRoom(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.boundingBox = var3;
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setBoolean("Mob", this.hasSpawner);
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.hasSpawner = var1.getBoolean("Mob");
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         if (var1 != null) {
            ((StructureStrongholdPieces.Stairs2)var1).strongholdPortalRoom = this;
         }

      }
   }

   public static class RoomCrossing extends StructureStrongholdPieces.Stronghold {
      protected int roomType;
      private static final List strongholdRoomCrossingChestContents;

      static {
         strongholdRoomCrossingChestContents = Lists.newArrayList((Object[])(new WeightedRandomChestContent(Items.iron_ingot, 0, 1, 5, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.redstone, 0, 4, 9, 5), new WeightedRandomChestContent(Items.coal, 0, 3, 8, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 1)));
      }

      public RoomCrossing() {
      }

      protected void readStructureFromNBT(NBTTagCompound var1) {
         super.readStructureFromNBT(var1);
         this.roomType = var1.getInteger("Type");
      }

      public static StructureStrongholdPieces.RoomCrossing func_175859_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -4, -1, 0, 11, 7, 11, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.RoomCrossing(var6, var1, var7, var5) : null;
      }

      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 4, 1);
         this.getNextComponentX((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 4);
         this.getNextComponentZ((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 4);
      }

      public RoomCrossing(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
         this.roomType = var2.nextInt(5);
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 10, 6, 10, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 4, 1, 0);
            this.fillWithBlocks(var1, var3, 4, 1, 10, 6, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 0, 1, 4, 0, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 10, 1, 4, 10, 3, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            int var4;
            switch(this.roomType) {
            case 0:
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 1, 5, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 2, 5, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 3, 5, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), 4, 3, 5, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), 6, 3, 5, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), 5, 3, 4, var3);
               this.setBlockState(var1, Blocks.torch.getDefaultState(), 5, 3, 6, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 4, 1, 4, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 4, 1, 5, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 4, 1, 6, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 6, 1, 4, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 6, 1, 5, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 6, 1, 6, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 5, 1, 4, var3);
               this.setBlockState(var1, Blocks.stone_slab.getDefaultState(), 5, 1, 6, var3);
               break;
            case 1:
               for(var4 = 0; var4 < 5; ++var4) {
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3, 1, 3 + var4, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 7, 1, 3 + var4, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3 + var4, 1, 3, var3);
                  this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 3 + var4, 1, 7, var3);
               }

               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 1, 5, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 2, 5, var3);
               this.setBlockState(var1, Blocks.stonebrick.getDefaultState(), 5, 3, 5, var3);
               this.setBlockState(var1, Blocks.flowing_water.getDefaultState(), 5, 4, 5, var3);
               break;
            case 2:
               for(var4 = 1; var4 <= 9; ++var4) {
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 1, 3, var4, var3);
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 9, 3, var4, var3);
               }

               for(var4 = 1; var4 <= 9; ++var4) {
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), var4, 3, 1, var3);
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), var4, 3, 9, var3);
               }

               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 5, 1, 4, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 5, 1, 6, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 5, 3, 4, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 5, 3, 6, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 1, 5, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, 1, 5, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, 3, 5, var3);
               this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, 3, 5, var3);

               for(var4 = 1; var4 <= 3; ++var4) {
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, var4, 4, var3);
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, var4, 4, var3);
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 4, var4, 6, var3);
                  this.setBlockState(var1, Blocks.cobblestone.getDefaultState(), 6, var4, 6, var3);
               }

               this.setBlockState(var1, Blocks.torch.getDefaultState(), 5, 3, 5, var3);

               for(var4 = 2; var4 <= 8; ++var4) {
                  this.setBlockState(var1, Blocks.planks.getDefaultState(), 2, 3, var4, var3);
                  this.setBlockState(var1, Blocks.planks.getDefaultState(), 3, 3, var4, var3);
                  if (var4 <= 3 || var4 >= 7) {
                     this.setBlockState(var1, Blocks.planks.getDefaultState(), 4, 3, var4, var3);
                     this.setBlockState(var1, Blocks.planks.getDefaultState(), 5, 3, var4, var3);
                     this.setBlockState(var1, Blocks.planks.getDefaultState(), 6, 3, var4, var3);
                  }

                  this.setBlockState(var1, Blocks.planks.getDefaultState(), 7, 3, var4, var3);
                  this.setBlockState(var1, Blocks.planks.getDefaultState(), 8, 3, var4, var3);
               }

               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 1, 3, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 2, 3, var3);
               this.setBlockState(var1, Blocks.ladder.getStateFromMeta(this.getMetadataWithOffset(Blocks.ladder, EnumFacing.WEST.getIndex())), 9, 3, 3, var3);
               this.generateChestContents(var1, var3, var2, 3, 4, 8, WeightedRandomChestContent.func_177629_a(strongholdRoomCrossingChestContents, Items.enchanted_book.getRandom(var2)), 1 + var2.nextInt(4));
            }

            return true;
         }
      }

      protected void writeStructureToNBT(NBTTagCompound var1) {
         super.writeStructureToNBT(var1);
         var1.setInteger("Type", this.roomType);
      }
   }

   public static class Prison extends StructureStrongholdPieces.Stronghold {
      public void buildComponent(StructureComponent var1, List var2, Random var3) {
         this.getNextComponentNormal((StructureStrongholdPieces.Stairs2)var1, var2, var3, 1, 1);
      }

      public Prison() {
      }

      public Prison(int var1, Random var2, StructureBoundingBox var3, EnumFacing var4) {
         super(var1);
         this.coordBaseMode = var4;
         this.field_143013_d = this.getRandomDoor(var2);
         this.boundingBox = var3;
      }

      public static StructureStrongholdPieces.Prison func_175860_a(List var0, Random var1, int var2, int var3, int var4, EnumFacing var5, int var6) {
         StructureBoundingBox var7 = StructureBoundingBox.getComponentToAddBoundingBox(var2, var3, var4, -1, -1, 0, 9, 5, 11, var5);
         return canStrongholdGoDeeper(var7) && StructureComponent.findIntersecting(var0, var7) == null ? new StructureStrongholdPieces.Prison(var6, var1, var7, var5) : null;
      }

      public boolean addComponentParts(World var1, Random var2, StructureBoundingBox var3) {
         if (this.isLiquidInStructureBoundingBox(var1, var3)) {
            return false;
         } else {
            this.fillWithRandomizedBlocks(var1, var3, 0, 0, 0, 8, 4, 10, true, var2, StructureStrongholdPieces.access$0());
            this.placeDoor(var1, var2, var3, this.field_143013_d, 1, 1, 0);
            this.fillWithBlocks(var1, var3, 1, 1, 10, 3, 3, 10, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 1, 4, 3, 1, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 3, 4, 3, 3, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 7, 4, 3, 7, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithRandomizedBlocks(var1, var3, 4, 1, 9, 4, 3, 9, false, var2, StructureStrongholdPieces.access$0());
            this.fillWithBlocks(var1, var3, 4, 1, 4, 4, 3, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.fillWithBlocks(var1, var3, 5, 1, 5, 7, 3, 5, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), 4, 3, 2, var3);
            this.setBlockState(var1, Blocks.iron_bars.getDefaultState(), 4, 3, 8, var3);
            this.setBlockState(var1, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 2, var3);
            this.setBlockState(var1, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 2, var3);
            this.setBlockState(var1, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3)), 4, 1, 8, var3);
            this.setBlockState(var1, Blocks.iron_door.getStateFromMeta(this.getMetadataWithOffset(Blocks.iron_door, 3) + 8), 4, 2, 8, var3);
            return true;
         }
      }
   }
}
