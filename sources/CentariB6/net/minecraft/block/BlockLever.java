package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockButton;
import net.minecraft.block.BlockLever.1;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block {
   public static final PropertyEnum FACING = PropertyEnum.create("facing", BlockLever.EnumOrientation.class);
   public static final PropertyBool POWERED = PropertyBool.create("powered");

   protected BlockLever() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, BlockLever.EnumOrientation.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
      this.setCreativeTab(CreativeTabs.tabRedstone);
   }

   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return !((Boolean)state.getValue(POWERED)).booleanValue()?0:(((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing() == side?15:0);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      for(EnumFacing enumfacing : EnumFacing.values()) {
         if(func_181090_a(worldIn, pos, enumfacing)) {
            return true;
         }
      }

      return false;
   }

   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
      return ((Boolean)state.getValue(POWERED)).booleanValue()?15:0;
   }

   public int getMetaFromState(IBlockState state) {
      int i = 0;
      i = i | ((BlockLever.EnumOrientation)state.getValue(FACING)).getMetadata();
      if(((Boolean)state.getValue(POWERED)).booleanValue()) {
         i |= 8;
      }

      return i;
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      if(this.func_181091_e(worldIn, pos, state) && !func_181090_a(worldIn, pos, ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing().getOpposite())) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

   }

   private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_) {
      if(this.canPlaceBlockAt(p_181091_1_, p_181091_2_)) {
         return true;
      } else {
         this.dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
         p_181091_1_.setBlockToAir(p_181091_2_);
         return false;
      }
   }

   protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_) {
      return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
   }

   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
      if(worldIn.isRemote) {
         return true;
      } else {
         state = state.cycleProperty(POWERED);
         worldIn.setBlockState(pos, state, 3);
         worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue(POWERED)).booleanValue()?0.6F:0.5F);
         worldIn.notifyNeighborsOfStateChange(pos, this);
         EnumFacing enumfacing = ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing();
         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
         return true;
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
      if(((Boolean)state.getValue(POWERED)).booleanValue()) {
         worldIn.notifyNeighborsOfStateChange(pos, this);
         EnumFacing enumfacing = ((BlockLever.EnumOrientation)state.getValue(FACING)).getFacing();
         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
      }

      super.breakBlock(worldIn, pos, state);
   }

   public IBlockState getStateFromMeta(int meta) {
      return this.getDefaultState().withProperty(FACING, BlockLever.EnumOrientation.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING, POWERED});
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
      if(func_181090_a(worldIn, pos, facing.getOpposite())) {
         return iblockstate.withProperty(FACING, BlockLever.EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
      } else {
         for(EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if(enumfacing != facing && func_181090_a(worldIn, pos, enumfacing.getOpposite())) {
               return iblockstate.withProperty(FACING, BlockLever.EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
            }
         }

         if(World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) {
            return iblockstate.withProperty(FACING, BlockLever.EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
         } else {
            return iblockstate;
         }
      }
   }

   public boolean canProvidePower() {
      return true;
   }

   public static int getMetadataForFacing(EnumFacing facing) {
      switch(1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
      case 1:
         return 0;
      case 2:
         return 5;
      case 3:
         return 4;
      case 4:
         return 3;
      case 5:
         return 2;
      case 6:
         return 1;
      default:
         return -1;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      float f = 0.1875F;
      switch(1.$SwitchMap$net$minecraft$block$BlockLever$EnumOrientation[((BlockLever.EnumOrientation)worldIn.getBlockState(pos).getValue(FACING)).ordinal()]) {
      case 1:
         this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
         break;
      case 2:
         this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
         break;
      case 3:
         this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
         break;
      case 4:
         this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
         break;
      case 5:
      case 6:
         f = 0.25F;
         this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
         break;
      case 7:
      case 8:
         f = 0.25F;
         this.setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
      }

   }

   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
      return func_181090_a(worldIn, pos, side.getOpposite());
   }

   public static enum EnumOrientation implements IStringSerializable {
      DOWN_X(0, "down_x", EnumFacing.DOWN),
      EAST(1, "east", EnumFacing.EAST),
      WEST(2, "west", EnumFacing.WEST),
      SOUTH(3, "south", EnumFacing.SOUTH),
      NORTH(4, "north", EnumFacing.NORTH),
      UP_Z(5, "up_z", EnumFacing.UP),
      UP_X(6, "up_x", EnumFacing.UP),
      DOWN_Z(7, "down_z", EnumFacing.DOWN);

      private static final BlockLever.EnumOrientation[] META_LOOKUP = new BlockLever.EnumOrientation[values().length];
      private final int meta;
      private final String name;
      private final EnumFacing facing;

      private EnumOrientation(int meta, String name, EnumFacing facing) {
         this.meta = meta;
         this.name = name;
         this.facing = facing;
      }

      static {
         for(BlockLever.EnumOrientation blocklever$enumorientation : values()) {
            META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
         }

      }

      public String toString() {
         return this.name;
      }

      public String getName() {
         return this.name;
      }

      public int getMetadata() {
         return this.meta;
      }

      public static BlockLever.EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing) {
         switch(1.$SwitchMap$net$minecraft$util$EnumFacing[clickedSide.ordinal()]) {
         case 1:
            switch(1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[entityFacing.getAxis().ordinal()]) {
            case 1:
               return DOWN_X;
            case 2:
               return DOWN_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
            }
         case 2:
            switch(1.$SwitchMap$net$minecraft$util$EnumFacing$Axis[entityFacing.getAxis().ordinal()]) {
            case 1:
               return UP_X;
            case 2:
               return UP_Z;
            default:
               throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
            }
         case 3:
            return NORTH;
         case 4:
            return SOUTH;
         case 5:
            return WEST;
         case 6:
            return EAST;
         default:
            throw new IllegalArgumentException("Invalid facing: " + clickedSide);
         }
      }

      public EnumFacing getFacing() {
         return this.facing;
      }

      public static BlockLever.EnumOrientation byMetadata(int meta) {
         if(meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
         }

         return META_LOOKUP[meta];
      }
   }
}
