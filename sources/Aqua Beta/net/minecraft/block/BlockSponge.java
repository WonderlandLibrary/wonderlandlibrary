package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Tuple;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSponge
extends Block {
    public static final PropertyBool WET = PropertyBool.create((String)"wet");

    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)WET, (Comparable)Boolean.valueOf((boolean)false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)(this.getUnlocalizedName() + ".dry.name"));
    }

    public int damageDropped(IBlockState state) {
        return (Boolean)state.getValue((IProperty)WET) != false ? 1 : 0;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.tryAbsorb(worldIn, pos, state);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.tryAbsorb(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
        if (!((Boolean)state.getValue((IProperty)WET)).booleanValue() && this.absorb(worldIn, pos)) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)WET, (Comparable)Boolean.valueOf((boolean)true)), 2);
            worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock((Block)Blocks.water));
        }
    }

    private boolean absorb(World worldIn, BlockPos pos) {
        LinkedList queue = Lists.newLinkedList();
        ArrayList arraylist = Lists.newArrayList();
        queue.add((Object)new Tuple((Object)pos, (Object)0));
        int i = 0;
        while (!queue.isEmpty()) {
            Tuple tuple = (Tuple)queue.poll();
            BlockPos blockpos = (BlockPos)tuple.getFirst();
            int j = (Integer)tuple.getSecond();
            for (EnumFacing enumfacing : EnumFacing.values()) {
                BlockPos blockpos1 = blockpos.offset(enumfacing);
                if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() != Material.water) continue;
                worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
                arraylist.add((Object)blockpos1);
                ++i;
                if (j >= 6) continue;
                queue.add((Object)new Tuple((Object)blockpos1, (Object)(j + 1)));
            }
            if (i <= 64) continue;
            break;
        }
        for (BlockPos blockpos2 : arraylist) {
            worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.air);
        }
        return i > 0;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add((Object)new ItemStack(itemIn, 1, 0));
        list.add((Object)new ItemStack(itemIn, 1, 1));
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)WET, (Comparable)Boolean.valueOf(((meta & 1) == 1 ? 1 : 0) != 0));
    }

    public int getMetaFromState(IBlockState state) {
        return (Boolean)state.getValue((IProperty)WET) != false ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{WET});
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        EnumFacing enumfacing;
        if (((Boolean)state.getValue((IProperty)WET)).booleanValue() && (enumfacing = EnumFacing.random((Random)rand)) != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.offset(enumfacing))) {
            double d0 = pos.getX();
            double d1 = pos.getY();
            double d2 = pos.getZ();
            if (enumfacing == EnumFacing.DOWN) {
                d1 -= 0.05;
                d0 += rand.nextDouble();
                d2 += rand.nextDouble();
            } else {
                d1 += rand.nextDouble() * 0.8;
                if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                    d2 += rand.nextDouble();
                    d0 = enumfacing == EnumFacing.EAST ? (d0 += 1.0) : (d0 += 0.05);
                } else {
                    d0 += rand.nextDouble();
                    d2 = enumfacing == EnumFacing.SOUTH ? (d2 += 1.0) : (d2 += 0.05);
                }
            }
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
        }
    }
}
