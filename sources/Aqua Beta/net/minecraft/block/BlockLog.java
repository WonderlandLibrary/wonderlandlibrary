package net.minecraft.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/*
 * Exception performing whole class analysis ignored.
 */
public abstract class BlockLog
extends BlockRotatedPillar {
    public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create((String)"axis", EnumAxis.class);

    public BlockLog() {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0f);
        this.setStepSound(soundTypeWood);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i = 4;
        int j = i + 1;
        if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j))) {
            for (BlockPos blockpos : BlockPos.getAllInBox((BlockPos)pos.add(-i, -i, -i), (BlockPos)pos.add(i, i, i))) {
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (iblockstate.getBlock().getMaterial() != Material.leaves || ((Boolean)iblockstate.getValue((IProperty)BlockLeaves.CHECK_DECAY)).booleanValue()) continue;
                worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty)BlockLeaves.CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)true)), 4);
            }
        }
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LOG_AXIS, (Comparable)EnumAxis.fromFacingAxis((EnumFacing.Axis)facing.getAxis()));
    }
}
