package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar {
  public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);
  
  public BlockLog() {
    super(Material.wood);
    setCreativeTab(CreativeTabs.tabBlock);
    setHardness(2.0F);
    setStepSound(soundTypeWood);
  }
  
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    int i = 4;
    int j = i + 1;
    if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j)))
      for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i))) {
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        if (iblockstate.getBlock().getMaterial() == Material.leaves && !((Boolean)iblockstate.getValue((IProperty)BlockLeaves.CHECK_DECAY)).booleanValue())
          worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4); 
      }  
  }
  
  public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
  }
  
  public enum EnumAxis implements IStringSerializable {
    X("x"),
    Y("y"),
    Z("z"),
    NONE("none");
    
    private final String name;
    
    EnumAxis(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
      switch (axis) {
        case X:
          return X;
        case Y:
          return Y;
        case Z:
          return Z;
      } 
      return NONE;
    }
    
    public String getName() {
      return this.name;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */