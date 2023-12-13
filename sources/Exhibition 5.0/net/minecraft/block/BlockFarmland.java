// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockFarmland extends Block
{
    public static final PropertyInteger field_176531_a;
    private static final String __OBFID = "CL_00000241";
    
    protected BlockFarmland() {
        super(Material.ground);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFarmland.field_176531_a, 0));
        this.setTickRandomly(true);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.9375f, 1.0f);
        this.setLightOpacity(255);
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        final int var5 = (int)state.getValue(BlockFarmland.field_176531_a);
        if (!this.func_176530_e(worldIn, pos) && !worldIn.func_175727_C(pos.offsetUp())) {
            if (var5 > 0) {
                worldIn.setBlockState(pos, state.withProperty(BlockFarmland.field_176531_a, var5 - 1), 2);
            }
            else if (!this.func_176529_d(worldIn, pos)) {
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
        }
        else if (var5 < 7) {
            worldIn.setBlockState(pos, state.withProperty(BlockFarmland.field_176531_a, 7), 2);
        }
    }
    
    @Override
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5f) {
                if (!(entityIn instanceof EntityPlayer) && !worldIn.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                    return;
                }
                worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
            }
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
    }
    
    private boolean func_176529_d(final World worldIn, final BlockPos p_176529_2_) {
        final Block var3 = worldIn.getBlockState(p_176529_2_.offsetUp()).getBlock();
        return var3 instanceof BlockCrops || var3 instanceof BlockStem;
    }
    
    private boolean func_176530_e(final World worldIn, final BlockPos p_176530_2_) {
        for (final BlockPos.MutableBlockPos var4 : BlockPos.getAllInBoxMutable(p_176530_2_.add(-4, 0, -4), p_176530_2_.add(4, 1, 4))) {
            if (worldIn.getBlockState(var4).getBlock().getMaterial() == Material.water) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        if (worldIn.getBlockState(pos.offsetUp()).getBlock().getMaterial().isSolid()) {
            worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Item.getItemFromBlock(Blocks.dirt);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockFarmland.field_176531_a, meta & 0x7);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockFarmland.field_176531_a);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockFarmland.field_176531_a });
    }
    
    static {
        field_176531_a = PropertyInteger.create("moisture", 0, 7);
    }
}
