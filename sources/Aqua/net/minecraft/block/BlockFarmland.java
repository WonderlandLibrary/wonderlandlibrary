package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFarmland
extends Block {
    public static final PropertyInteger MOISTURE = PropertyInteger.create((String)"moisture", (int)0, (int)7);

    protected BlockFarmland() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)MOISTURE, (Comparable)Integer.valueOf((int)0)));
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.setLightOpacity(255);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int i = (Integer)state.getValue((IProperty)MOISTURE);
        if (!this.hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
            if (i > 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)MOISTURE, (Comparable)Integer.valueOf((int)(i - 1))), 2);
            } else if (!this.hasCrops(worldIn, pos)) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
        } else if (i < 7) {
            worldIn.setBlockState(pos, state.withProperty((IProperty)MOISTURE, (Comparable)Integer.valueOf((int)7)), 2);
        }
    }

    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f) {
                if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getBoolean("mobGriefing")) {
                    return;
                }
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }

    private boolean hasCrops(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof BlockCrops || block instanceof BlockStem;
    }

    private boolean hasWater(World worldIn, BlockPos pos) {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable((BlockPos)pos.add(-4, 0, -4), (BlockPos)pos.add(4, 1, 4))) {
            if (worldIn.getBlockState((BlockPos)blockpos$mutableblockpos).getBlock().getMaterial() != Material.water) continue;
            return true;
        }
        return false;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[side.ordinal()]) {
            case 1: {
                return true;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: {
                Block block = worldIn.getBlockState(pos).getBlock();
                return !block.isOpaqueCube() && block != Blocks.farmland;
            }
        }
        return super.shouldSideBeRendered(worldIn, pos, side);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty((IProperty)BlockDirt.VARIANT, (Comparable)BlockDirt.DirtType.DIRT), rand, fortune);
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock((Block)Blocks.dirt);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)MOISTURE, (Comparable)Integer.valueOf((int)(meta & 7)));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)MOISTURE);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{MOISTURE});
    }
}
