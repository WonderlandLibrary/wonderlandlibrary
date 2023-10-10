package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block {
   protected final boolean isPowered;

   public void onNeighborBlockChange(World var1, BlockPos var2, IBlockState var3, Block var4) {
      if (!var1.isRemote) {
         BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)var3.getValue(this.getShapeProperty());
         boolean var6 = false;
         if (!World.doesBlockHaveSolidTopSurface(var1, var2.down())) {
            var6 = true;
         }

         if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(var1, var2.east())) {
            var6 = true;
         } else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(var1, var2.west())) {
            var6 = true;
         } else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(var1, var2.north())) {
            var6 = true;
         } else if (var5 == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(var1, var2.south())) {
            var6 = true;
         }

         if (var6) {
            this.dropBlockAsItem(var1, var2, var3, 0);
            var1.setBlockToAir(var2);
         } else {
            this.onNeighborChangedInternal(var1, var2, var3, var4);
         }
      }

   }

   public AxisAlignedBB getCollisionBoundingBox(World var1, BlockPos var2, IBlockState var3) {
      return null;
   }

   public abstract IProperty getShapeProperty();

   public int getMobilityFlag() {
      return 0;
   }

   public void breakBlock(World var1, BlockPos var2, IBlockState var3) {
      super.breakBlock(var1, var2, var3);
      if (((BlockRailBase.EnumRailDirection)var3.getValue(this.getShapeProperty())).isAscending()) {
         var1.notifyNeighborsOfStateChange(var2.up(), this);
      }

      if (this.isPowered) {
         var1.notifyNeighborsOfStateChange(var2, this);
         var1.notifyNeighborsOfStateChange(var2.down(), this);
      }

   }

   public void onBlockAdded(World var1, BlockPos var2, IBlockState var3) {
      if (!var1.isRemote) {
         var3 = this.func_176564_a(var1, var2, var3, true);
         if (this.isPowered) {
            this.onNeighborBlockChange(var1, var2, var3, this);
         }
      }

   }

   public boolean canPlaceBlockAt(World var1, BlockPos var2) {
      return World.doesBlockHaveSolidTopSurface(var1, var2.down());
   }

   public void setBlockBoundsBasedOnState(IBlockAccess var1, BlockPos var2) {
      IBlockState var3 = var1.getBlockState(var2);
      BlockRailBase.EnumRailDirection var4 = var3.getBlock() == this ? (BlockRailBase.EnumRailDirection)var3.getValue(this.getShapeProperty()) : null;
      if (var4 != null && var4.isAscending()) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      }

   }

   protected IBlockState func_176564_a(World var1, BlockPos var2, IBlockState var3, boolean var4) {
      return var1.isRemote ? var3 : (new BlockRailBase.Rail(this, var1, var2, var3)).func_180364_a(var1.isBlockPowered(var2), var4).getBlockState();
   }

   protected void onNeighborChangedInternal(World var1, BlockPos var2, IBlockState var3, Block var4) {
   }

   public MovingObjectPosition collisionRayTrace(World var1, BlockPos var2, Vec3 var3, Vec3 var4) {
      this.setBlockBoundsBasedOnState(var1, var2);
      return super.collisionRayTrace(var1, var2, var3, var4);
   }

   public static boolean isRailBlock(IBlockState var0) {
      Block var1 = var0.getBlock();
      return var1 == Blocks.rail || var1 == Blocks.golden_rail || var1 == Blocks.detector_rail || var1 == Blocks.activator_rail;
   }

   public static boolean isRailBlock(World var0, BlockPos var1) {
      return isRailBlock(var0.getBlockState(var1));
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   protected BlockRailBase(boolean var1) {
      super(Material.circuits);
      this.isPowered = var1;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      this.setCreativeTab(CreativeTabs.tabTransport);
   }

   public static enum EnumRailDirection implements IStringSerializable {
      SOUTH_EAST(6, "south_east"),
      ASCENDING_NORTH(4, "ascending_north"),
      NORTH_EAST(9, "north_east"),
      ASCENDING_SOUTH(5, "ascending_south");

      private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
      private final String name;
      SOUTH_WEST(7, "south_west");

      private final int meta;
      NORTH_SOUTH(0, "north_south"),
      EAST_WEST(1, "east_west"),
      ASCENDING_EAST(2, "ascending_east");

      private static final BlockRailBase.EnumRailDirection[] ENUM$VALUES = new BlockRailBase.EnumRailDirection[]{NORTH_SOUTH, EAST_WEST, ASCENDING_EAST, ASCENDING_WEST, ASCENDING_NORTH, ASCENDING_SOUTH, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST};
      NORTH_WEST(8, "north_west"),
      ASCENDING_WEST(3, "ascending_west");

      public String toString() {
         return this.name;
      }

      public static BlockRailBase.EnumRailDirection byMetadata(int var0) {
         if (var0 < 0 || var0 >= META_LOOKUP.length) {
            var0 = 0;
         }

         return META_LOOKUP[var0];
      }

      public boolean isAscending() {
         return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
      }

      public int getMetadata() {
         return this.meta;
      }

      public String getName() {
         return this.name;
      }

      private EnumRailDirection(int var3, String var4) {
         this.meta = var3;
         this.name = var4;
      }

      static {
         BlockRailBase.EnumRailDirection[] var3;
         int var2 = (var3 = values()).length;

         for(int var1 = 0; var1 < var2; ++var1) {
            BlockRailBase.EnumRailDirection var0 = var3[var1];
            META_LOOKUP[var0.getMetadata()] = var0;
         }

      }
   }

   public class Rail {
      final BlockRailBase this$0;
      private final World world;
      private final List field_150657_g;
      private final boolean isPowered;
      private IBlockState state;
      private final BlockPos pos;
      private static volatile int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
      private final BlockRailBase block;

      private void func_150645_c(BlockRailBase.Rail var1) {
         this.field_150657_g.add(var1.pos);
         BlockPos var2 = this.pos.north();
         BlockPos var3 = this.pos.south();
         BlockPos var4 = this.pos.west();
         BlockPos var5 = this.pos.east();
         boolean var6 = this.func_180363_c(var2);
         boolean var7 = this.func_180363_c(var3);
         boolean var8 = this.func_180363_c(var4);
         boolean var9 = this.func_180363_c(var5);
         BlockRailBase.EnumRailDirection var10 = null;
         if (var6 || var7) {
            var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         if (var8 || var9) {
            var10 = BlockRailBase.EnumRailDirection.EAST_WEST;
         }

         if (!this.isPowered) {
            if (var7 && var9 && !var6 && !var8) {
               var10 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }

            if (var7 && var8 && !var6 && !var9) {
               var10 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }

            if (var6 && var8 && !var7 && !var9) {
               var10 = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }

            if (var6 && var9 && !var7 && !var8) {
               var10 = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
         }

         if (var10 == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock(this.world, var2.up())) {
               var10 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }

            if (BlockRailBase.isRailBlock(this.world, var3.up())) {
               var10 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
         }

         if (var10 == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock(this.world, var5.up())) {
               var10 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }

            if (BlockRailBase.isRailBlock(this.world, var4.up())) {
               var10 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
         }

         if (var10 == null) {
            var10 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         this.state = this.state.withProperty(this.block.getShapeProperty(), var10);
         this.world.setBlockState(this.pos, this.state, 3);
      }

      public Rail(BlockRailBase var1, World var2, BlockPos var3, IBlockState var4) {
         this.this$0 = var1;
         this.field_150657_g = Lists.newArrayList();
         this.world = var2;
         this.pos = var3;
         this.state = var4;
         this.block = (BlockRailBase)var4.getBlock();
         BlockRailBase.EnumRailDirection var5 = (BlockRailBase.EnumRailDirection)var4.getValue(var1.getShapeProperty());
         this.isPowered = this.block.isPowered;
         this.func_180360_a(var5);
      }

      private BlockRailBase.Rail findRailAt(BlockPos var1) {
         IBlockState var2 = this.world.getBlockState(var1);
         if (BlockRailBase.isRailBlock(var2)) {
            return this.this$0.new Rail(this.this$0, this.world, var1, var2);
         } else {
            BlockPos var3 = var1.up();
            var2 = this.world.getBlockState(var3);
            if (BlockRailBase.isRailBlock(var2)) {
               return this.this$0.new Rail(this.this$0, this.world, var3, var2);
            } else {
               var3 = var1.down();
               var2 = this.world.getBlockState(var3);
               return BlockRailBase.isRailBlock(var2) ? this.this$0.new Rail(this.this$0, this.world, var3, var2) : null;
            }
         }
      }

      public IBlockState getBlockState() {
         return this.state;
      }

      public BlockRailBase.Rail func_180364_a(boolean var1, boolean var2) {
         BlockPos var3 = this.pos.north();
         BlockPos var4 = this.pos.south();
         BlockPos var5 = this.pos.west();
         BlockPos var6 = this.pos.east();
         boolean var7 = this.func_180361_d(var3);
         boolean var8 = this.func_180361_d(var4);
         boolean var9 = this.func_180361_d(var5);
         boolean var10 = this.func_180361_d(var6);
         BlockRailBase.EnumRailDirection var11 = null;
         if ((var7 || var8) && !var9 && !var10) {
            var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         if ((var9 || var10) && !var7 && !var8) {
            var11 = BlockRailBase.EnumRailDirection.EAST_WEST;
         }

         if (!this.isPowered) {
            if (var8 && var10 && !var7 && !var9) {
               var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
            }

            if (var8 && var9 && !var7 && !var10) {
               var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
            }

            if (var7 && var9 && !var8 && !var10) {
               var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
            }

            if (var7 && var10 && !var8 && !var9) {
               var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
            }
         }

         if (var11 == null) {
            if (var7 || var8) {
               var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (var9 || var10) {
               var11 = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered) {
               if (var1) {
                  if (var8 && var10) {
                     var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                  }

                  if (var9 && var8) {
                     var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                  }

                  if (var10 && var7) {
                     var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                  }

                  if (var7 && var9) {
                     var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                  }
               } else {
                  if (var7 && var9) {
                     var11 = BlockRailBase.EnumRailDirection.NORTH_WEST;
                  }

                  if (var10 && var7) {
                     var11 = BlockRailBase.EnumRailDirection.NORTH_EAST;
                  }

                  if (var9 && var8) {
                     var11 = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                  }

                  if (var8 && var10) {
                     var11 = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                  }
               }
            }
         }

         if (var11 == BlockRailBase.EnumRailDirection.NORTH_SOUTH) {
            if (BlockRailBase.isRailBlock(this.world, var3.up())) {
               var11 = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
            }

            if (BlockRailBase.isRailBlock(this.world, var4.up())) {
               var11 = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
            }
         }

         if (var11 == BlockRailBase.EnumRailDirection.EAST_WEST) {
            if (BlockRailBase.isRailBlock(this.world, var6.up())) {
               var11 = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
            }

            if (BlockRailBase.isRailBlock(this.world, var5.up())) {
               var11 = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
            }
         }

         if (var11 == null) {
            var11 = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
         }

         this.func_180360_a(var11);
         this.state = this.state.withProperty(this.block.getShapeProperty(), var11);
         if (var2 || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, 3);

            for(int var12 = 0; var12 < this.field_150657_g.size(); ++var12) {
               BlockRailBase.Rail var13 = this.findRailAt((BlockPos)this.field_150657_g.get(var12));
               if (var13 != null) {
                  var13.func_150651_b();
                  if (this != false) {
                     var13.func_150645_c(this);
                  }
               }
            }
         }

         return this;
      }

      protected int countAdjacentRails() {
         int var1 = 0;
         Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();

         while(var3.hasNext()) {
            Object var2 = var3.next();
            if (this.pos.offset((EnumFacing)var2) == false) {
               ++var1;
            }
         }

         return var1;
      }

      static int[] $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection() {
         int[] var10000 = $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection;
         if (var10000 != null) {
            return var10000;
         } else {
            int[] var0 = new int[BlockRailBase.EnumRailDirection.values().length];

            try {
               var0[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
            } catch (NoSuchFieldError var11) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
            } catch (NoSuchFieldError var10) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
            } catch (NoSuchFieldError var9) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
            } catch (NoSuchFieldError var8) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
            } catch (NoSuchFieldError var7) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
            } catch (NoSuchFieldError var6) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
            } catch (NoSuchFieldError var5) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
            } catch (NoSuchFieldError var4) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
            } catch (NoSuchFieldError var3) {
            }

            try {
               var0[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
            } catch (NoSuchFieldError var2) {
            }

            $SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection = var0;
            return var0;
         }
      }

      private boolean func_150653_a(BlockRailBase.Rail var1) {
         return this.func_180363_c(var1.pos);
      }

      private void func_180360_a(BlockRailBase.EnumRailDirection var1) {
         this.field_150657_g.clear();
         switch($SWITCH_TABLE$net$minecraft$block$BlockRailBase$EnumRailDirection()[var1.ordinal()]) {
         case 1:
            this.field_150657_g.add(this.pos.north());
            this.field_150657_g.add(this.pos.south());
            break;
         case 2:
            this.field_150657_g.add(this.pos.west());
            this.field_150657_g.add(this.pos.east());
            break;
         case 3:
            this.field_150657_g.add(this.pos.west());
            this.field_150657_g.add(this.pos.east().up());
            break;
         case 4:
            this.field_150657_g.add(this.pos.west().up());
            this.field_150657_g.add(this.pos.east());
            break;
         case 5:
            this.field_150657_g.add(this.pos.north().up());
            this.field_150657_g.add(this.pos.south());
            break;
         case 6:
            this.field_150657_g.add(this.pos.north());
            this.field_150657_g.add(this.pos.south().up());
            break;
         case 7:
            this.field_150657_g.add(this.pos.east());
            this.field_150657_g.add(this.pos.south());
            break;
         case 8:
            this.field_150657_g.add(this.pos.west());
            this.field_150657_g.add(this.pos.south());
            break;
         case 9:
            this.field_150657_g.add(this.pos.west());
            this.field_150657_g.add(this.pos.north());
            break;
         case 10:
            this.field_150657_g.add(this.pos.east());
            this.field_150657_g.add(this.pos.north());
         }

      }

      private boolean func_180361_d(BlockPos var1) {
         BlockRailBase.Rail var2 = this.findRailAt(var1);
         if (var2 == null) {
            return false;
         } else {
            var2.func_150651_b();
            return var2.func_150649_b(this);
         }
      }

      private boolean func_180363_c(BlockPos var1) {
         for(int var2 = 0; var2 < this.field_150657_g.size(); ++var2) {
            BlockPos var3 = (BlockPos)this.field_150657_g.get(var2);
            if (var3.getX() == var1.getX() && var3.getZ() == var1.getZ()) {
               return true;
            }
         }

         return false;
      }

      private void func_150651_b() {
         for(int var1 = 0; var1 < this.field_150657_g.size(); ++var1) {
            BlockRailBase.Rail var2 = this.findRailAt((BlockPos)this.field_150657_g.get(var1));
            if (var2 != null && var2.func_150653_a(this)) {
               this.field_150657_g.set(var1, var2.pos);
            } else {
               this.field_150657_g.remove(var1--);
            }
         }

      }
   }
}
