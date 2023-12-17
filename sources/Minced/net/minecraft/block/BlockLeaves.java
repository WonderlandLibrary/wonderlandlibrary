// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public abstract class BlockLeaves extends Block
{
    public static final PropertyBool DECAYABLE;
    public static final PropertyBool CHECK_DECAY;
    protected boolean leavesFancy;
    int[] surroundings;
    
    public BlockLeaves() {
        super(Material.LEAVES);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHardness(0.2f);
        this.setLightOpacity(1);
        this.setSoundType(SoundType.PLANT);
    }
    
    @Override
    public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state) {
        final int i = 1;
        final int j = 2;
        final int k = pos.getX();
        final int l = pos.getY();
        final int i2 = pos.getZ();
        if (worldIn.isAreaLoaded(new BlockPos(k - 2, l - 2, i2 - 2), new BlockPos(k + 2, l + 2, i2 + 2))) {
            for (int j2 = -1; j2 <= 1; ++j2) {
                for (int k2 = -1; k2 <= 1; ++k2) {
                    for (int l2 = -1; l2 <= 1; ++l2) {
                        final BlockPos blockpos = pos.add(j2, k2, l2);
                        final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                        if (iblockstate.getMaterial() == Material.LEAVES && !iblockstate.getValue((IProperty<Boolean>)BlockLeaves.CHECK_DECAY)) {
                            worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, true), 4);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        if (!worldIn.isRemote && state.getValue((IProperty<Boolean>)BlockLeaves.CHECK_DECAY) && state.getValue((IProperty<Boolean>)BlockLeaves.DECAYABLE)) {
            final int i = 4;
            final int j = 5;
            final int k = pos.getX();
            final int l = pos.getY();
            final int i2 = pos.getZ();
            final int j2 = 32;
            final int k2 = 1024;
            final int l2 = 16;
            if (this.surroundings == null) {
                this.surroundings = new int[32768];
            }
            if (worldIn.isAreaLoaded(new BlockPos(k - 5, l - 5, i2 - 5), new BlockPos(k + 5, l + 5, i2 + 5))) {
                final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int i3 = -4; i3 <= 4; ++i3) {
                    for (int j3 = -4; j3 <= 4; ++j3) {
                        for (int k3 = -4; k3 <= 4; ++k3) {
                            final IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k + i3, l + j3, i2 + k3));
                            final Block block = iblockstate.getBlock();
                            if (block != Blocks.LOG && block != Blocks.LOG2) {
                                if (iblockstate.getMaterial() == Material.LEAVES) {
                                    this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = -2;
                                }
                                else {
                                    this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = -1;
                                }
                            }
                            else {
                                this.surroundings[(i3 + 16) * 1024 + (j3 + 16) * 32 + k3 + 16] = 0;
                            }
                        }
                    }
                }
                for (int i4 = 1; i4 <= 4; ++i4) {
                    for (int j4 = -4; j4 <= 4; ++j4) {
                        for (int k4 = -4; k4 <= 4; ++k4) {
                            for (int l3 = -4; l3 <= 4; ++l3) {
                                if (this.surroundings[(j4 + 16) * 1024 + (k4 + 16) * 32 + l3 + 16] == i4 - 1) {
                                    if (this.surroundings[(j4 + 16 - 1) * 1024 + (k4 + 16) * 32 + l3 + 16] == -2) {
                                        this.surroundings[(j4 + 16 - 1) * 1024 + (k4 + 16) * 32 + l3 + 16] = i4;
                                    }
                                    if (this.surroundings[(j4 + 16 + 1) * 1024 + (k4 + 16) * 32 + l3 + 16] == -2) {
                                        this.surroundings[(j4 + 16 + 1) * 1024 + (k4 + 16) * 32 + l3 + 16] = i4;
                                    }
                                    if (this.surroundings[(j4 + 16) * 1024 + (k4 + 16 - 1) * 32 + l3 + 16] == -2) {
                                        this.surroundings[(j4 + 16) * 1024 + (k4 + 16 - 1) * 32 + l3 + 16] = i4;
                                    }
                                    if (this.surroundings[(j4 + 16) * 1024 + (k4 + 16 + 1) * 32 + l3 + 16] == -2) {
                                        this.surroundings[(j4 + 16) * 1024 + (k4 + 16 + 1) * 32 + l3 + 16] = i4;
                                    }
                                    if (this.surroundings[(j4 + 16) * 1024 + (k4 + 16) * 32 + (l3 + 16 - 1)] == -2) {
                                        this.surroundings[(j4 + 16) * 1024 + (k4 + 16) * 32 + (l3 + 16 - 1)] = i4;
                                    }
                                    if (this.surroundings[(j4 + 16) * 1024 + (k4 + 16) * 32 + l3 + 16 + 1] == -2) {
                                        this.surroundings[(j4 + 16) * 1024 + (k4 + 16) * 32 + l3 + 16 + 1] = i4;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            final int l4 = this.surroundings[16912];
            if (l4 >= 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false), 4);
            }
            else {
                this.destroy(worldIn, pos);
            }
        }
    }
    
    @Override
    public void randomDisplayTick(final IBlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        if (worldIn.isRainingAt(pos.up()) && !worldIn.getBlockState(pos.down()).isTopSolid() && rand.nextInt(15) == 1) {
            final double d0 = pos.getX() + rand.nextFloat();
            final double d2 = pos.getY() - 0.05;
            final double d3 = pos.getZ() + rand.nextFloat();
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    private void destroy(final World worldIn, final BlockPos pos) {
        this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
        worldIn.setBlockToAir(pos);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return (random.nextInt(20) == 0) ? 1 : 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Item.getItemFromBlock(Blocks.SAPLING);
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
        if (!worldIn.isRemote) {
            int i = this.getSaplingDropChance(state);
            if (fortune > 0) {
                i -= 2 << fortune;
                if (i < 10) {
                    i = 10;
                }
            }
            if (worldIn.rand.nextInt(i) == 0) {
                final Item item = this.getItemDropped(state, worldIn.rand, fortune);
                Block.spawnAsEntity(worldIn, pos, new ItemStack(item, 1, this.damageDropped(state)));
            }
            i = 200;
            if (fortune > 0) {
                i -= 10 << fortune;
                if (i < 40) {
                    i = 40;
                }
            }
            this.dropApple(worldIn, pos, state, i);
        }
    }
    
    protected void dropApple(final World worldIn, final BlockPos pos, final IBlockState state, final int chance) {
    }
    
    protected int getSaplingDropChance(final IBlockState state) {
        return 20;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return !this.leavesFancy;
    }
    
    public void setGraphicsLevel(final boolean fancy) {
        this.leavesFancy = fancy;
    }
    
    @Override
    public BlockRenderLayer getRenderLayer() {
        return this.leavesFancy ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }
    
    @Override
    @Deprecated
    public boolean causesSuffocation(final IBlockState state) {
        return false;
    }
    
    public abstract BlockPlanks.EnumType getWoodType(final int p0);
    
    @Override
    @Deprecated
    public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
        return (this.leavesFancy || blockAccess.getBlockState(pos.offset(side)).getBlock() != this) && super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    static {
        DECAYABLE = PropertyBool.create("decayable");
        CHECK_DECAY = PropertyBool.create("check_decay");
    }
}
