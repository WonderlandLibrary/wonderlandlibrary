package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase.Rail;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockRailBase extends Block {
   protected final boolean isPowered;

   protected BlockRailBase(boolean isPowered) {
      super(Material.circuits);
      this.isPowered = isPowered;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      this.setCreativeTab(CreativeTabs.tabTransport);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
   }

   public abstract IProperty getShapeProperty();

   protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_) {
      return worldIn.isRemote?p_176564_3_:(new Rail(this, worldIn, p_176564_2_, p_176564_3_)).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
   }

   public static boolean isRailBlock(IBlockState state) {
      Block block = state.getBlock();
      return block == Blocks.rail || block == Blocks.golden_rail || block == Blocks.detector_rail || block == Blocks.activator_rail;
   }

   public static boolean isRailBlock(World worldIn, BlockPos pos) {
      return isRailBlock(worldIn.getBlockState(pos));
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if(!worldIn.isRemote) {
         BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty());
         boolean flag = false;
         if(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            flag = true;
         }

         if(blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.east())) {
            flag = true;
         } else if(blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.west())) {
            flag = true;
         } else if(blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.north())) {
            flag = true;
         } else if(blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.south())) {
            flag = true;
         }

         if(flag) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
         } else {
            this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
         }
      }

   }

   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      return super.collisionRayTrace(worldIn, pos, start, end);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      super.breakBlock(worldIn, pos, state);
      if(((BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty())).isAscending()) {
         worldIn.notifyNeighborsOfStateChange(pos.up(), this);
      }

      if(this.isPowered) {
         worldIn.notifyNeighborsOfStateChange(pos, this);
         worldIn.notifyNeighborsOfStateChange(pos.down(), this);
      }

   }

   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
      if(!worldIn.isRemote) {
         state = this.func_176564_a(worldIn, pos, state, true);
         if(this.isPowered) {
            this.onNeighborBlockChange(worldIn, pos, state, this);
         }
      }

   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   public int getMobilityFlag() {
      return 0;
   }

   protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() == this?(BlockRailBase.EnumRailDirection)iblockstate.getValue(this.getShapeProperty()):null;
      if(blockrailbase$enumraildirection != null && blockrailbase$enumraildirection.isAscending()) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
      }

   }

   public static enum EnumRailDirection implements IStringSerializable {
      NORTH_SOUTH(0, "north_south"),
      EAST_WEST(1, "east_west"),
      ASCENDING_EAST(2, "ascending_east"),
      ASCENDING_WEST(3, "ascending_west"),
      ASCENDING_NORTH(4, "ascending_north"),
      ASCENDING_SOUTH(5, "ascending_south"),
      SOUTH_EAST(6, "south_east"),
      SOUTH_WEST(7, "south_west"),
      NORTH_WEST(8, "north_west"),
      NORTH_EAST(9, "north_east");

      private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
      private final int meta;
      private final String name;

      private EnumRailDirection(int meta, String name) {
         this.meta = meta;
         this.name = name;
      }

      static {
         for(BlockRailBase.EnumRailDirection blockrailbase$enumraildirection : values()) {
            META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
         }

      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      public boolean isAscending() {
         return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
      }

      public int getMetadata() {
         return this.meta;
      }

      public static BlockRailBase.EnumRailDirection byMetadata(int meta) {
         if(meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }
   }
}
