package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder.1;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.alphacentauri.AC;
import org.alphacentauri.management.bypass.AntiCheat;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.modules.ModuleFastLadder;

public class BlockLadder extends Block {
   public static final PropertyDirection FACING = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);

   protected BlockLadder() {
      super(Material.circuits);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
      this.setCreativeTab(CreativeTabs.tabDecorations);
   }

   public boolean isFullCube() {
      return false;
   }

   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
      return worldIn.getBlockState(pos.west()).getBlock().isNormalCube()?true:(worldIn.getBlockState(pos.east()).getBlock().isNormalCube()?true:(worldIn.getBlockState(pos.north()).getBlock().isNormalCube()?true:worldIn.getBlockState(pos.south()).getBlock().isNormalCube()));
   }

   public int getMetaFromState(IBlockState state) {
      return ((EnumFacing)state.getValue(FACING)).getIndex();
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      return super.getCollisionBoundingBox(worldIn, pos, state);
   }

   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
      EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
      if(!this.canBlockStay(worldIn, pos, enumfacing)) {
         this.dropBlockAsItem(worldIn, pos, state, 0);
         worldIn.setBlockToAir(pos);
      }

      super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
   }

   private boolean isGaped(BlockPos pos) {
      return !(Minecraft.getMinecraft().theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockLadder);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public IBlockState getStateFromMeta(int meta) {
      EnumFacing enumfacing = EnumFacing.getFront(meta);
      if(enumfacing.getAxis() == EnumFacing.Axis.Y) {
         enumfacing = EnumFacing.NORTH;
      }

      return this.getDefaultState().withProperty(FACING, enumfacing);
   }

   public EnumWorldBlockLayer getBlockLayer() {
      return EnumWorldBlockLayer.CUTOUT;
   }

   protected BlockState createBlockState() {
      return new BlockState(this, new IProperty[]{FACING});
   }

   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      if(facing.getAxis().isHorizontal() && this.canBlockStay(worldIn, pos, facing)) {
         return this.getDefaultState().withProperty(FACING, facing);
      } else {
         for(EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if(this.canBlockStay(worldIn, pos, enumfacing)) {
               return this.getDefaultState().withProperty(FACING, enumfacing);
            }
         }

         return this.getDefaultState();
      }
   }

   protected boolean canBlockStay(World worldIn, BlockPos pos, EnumFacing facing) {
      return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube();
   }

   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
      this.setBlockBoundsBasedOnState(worldIn, pos);
      return super.getSelectedBoundingBox(worldIn, pos);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
      IBlockState iblockstate = worldIn.getBlockState(pos);
      if(iblockstate.getBlock() == this) {
         Module fastLadder = AC.getModuleManager().get(ModuleFastLadder.class);
         float f = fastLadder.getBypass() == AntiCheat.AAC && fastLadder.isEnabled() && !this.isGaped(pos) && !AC.getMC().getPlayer().isSneaking() && AC.getMC().getPlayer().fallDistance < 1.0F && !AC.getMC().getPlayer().isOnLadder()?0.9F:0.125F;
         switch(1.$SwitchMap$net$minecraft$util$EnumFacing[((EnumFacing)iblockstate.getValue(FACING)).ordinal()]) {
         case 1:
            this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
            break;
         case 2:
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
            break;
         case 3:
            this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            break;
         case 4:
         default:
            this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
         }
      }

   }
}
