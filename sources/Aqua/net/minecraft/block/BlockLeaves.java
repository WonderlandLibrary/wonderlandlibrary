package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public abstract class BlockLeaves
extends BlockLeavesBase {
    public static final PropertyBool DECAYABLE = PropertyBool.create((String)"decayable");
    public static final PropertyBool CHECK_DECAY = PropertyBool.create((String)"check_decay");
    int[] surroundings;
    protected int iconIndex;
    protected boolean isTransparent;

    public BlockLeaves() {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2f);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColor((double)0.5, (double)1.0);
    }

    public int getRenderColor(IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        return BiomeColorHelper.getFoliageColorAtPos((IBlockAccess)worldIn, (BlockPos)pos);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int i1;
        int l;
        int i = 1;
        int j = i + 1;
        int k = pos.getX();
        if (worldIn.isAreaLoaded(new BlockPos(k - j, (l = pos.getY()) - j, (i1 = pos.getZ()) - j), new BlockPos(k + j, l + j, i1 + j))) {
            for (int j1 = -i; j1 <= i; ++j1) {
                for (int k1 = -i; k1 <= i; ++k1) {
                    for (int l1 = -i; l1 <= i; ++l1) {
                        BlockPos blockpos = pos.add(j1, k1, l1);
                        IBlockState iblockstate = worldIn.getBlockState(blockpos);
                        if (iblockstate.getBlock().getMaterial() != Material.leaves || ((Boolean)iblockstate.getValue((IProperty)CHECK_DECAY)).booleanValue()) continue;
                        worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)true)), 4);
                    }
                }
            }
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)state.getValue((IProperty)CHECK_DECAY)).booleanValue() && ((Boolean)state.getValue((IProperty)DECAYABLE)).booleanValue()) {
            int l2;
            int i = 4;
            int j = i + 1;
            int k = pos.getX();
            int l = pos.getY();
            int i1 = pos.getZ();
            int j1 = 32;
            int k1 = j1 * j1;
            int l1 = j1 / 2;
            if (this.surroundings == null) {
                this.surroundings = new int[j1 * j1 * j1];
            }
            if (worldIn.isAreaLoaded(new BlockPos(k - j, l - j, i1 - j), new BlockPos(k + j, l + j, i1 + j))) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int i2 = -i; i2 <= i; ++i2) {
                    for (int j2 = -i; j2 <= i; ++j2) {
                        for (int k2 = -i; k2 <= i; ++k2) {
                            Block block = worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(k + i2, l + j2, i1 + k2)).getBlock();
                            if (block != Blocks.log && block != Blocks.log2) {
                                if (block.getMaterial() == Material.leaves) {
                                    this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = -2;
                                    continue;
                                }
                                this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = -1;
                                continue;
                            }
                            this.surroundings[(i2 + l1) * k1 + (j2 + l1) * j1 + k2 + l1] = 0;
                        }
                    }
                }
                for (int i3 = 1; i3 <= 4; ++i3) {
                    for (int j3 = -i; j3 <= i; ++j3) {
                        for (int k3 = -i; k3 <= i; ++k3) {
                            for (int l3 = -i; l3 <= i; ++l3) {
                                if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1] != i3 - 1) continue;
                                if (this.surroundings[(j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1] == -2) {
                                    this.surroundings[(j3 + l1 - 1) * k1 + (k3 + l1) * j1 + l3 + l1] = i3;
                                }
                                if (this.surroundings[(j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1] == -2) {
                                    this.surroundings[(j3 + l1 + 1) * k1 + (k3 + l1) * j1 + l3 + l1] = i3;
                                }
                                if (this.surroundings[(j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1] == -2) {
                                    this.surroundings[(j3 + l1) * k1 + (k3 + l1 - 1) * j1 + l3 + l1] = i3;
                                }
                                if (this.surroundings[(j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1] == -2) {
                                    this.surroundings[(j3 + l1) * k1 + (k3 + l1 + 1) * j1 + l3 + l1] = i3;
                                }
                                if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1)] == -2) {
                                    this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + (l3 + l1 - 1)] = i3;
                                }
                                if (this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1] != -2) continue;
                                this.surroundings[(j3 + l1) * k1 + (k3 + l1) * j1 + l3 + l1 + 1] = i3;
                            }
                        }
                    }
                }
            }
            if ((l2 = this.surroundings[l1 * k1 + l1 * j1 + l1]) >= 0) {
                worldIn.setBlockState(pos, state.withProperty((IProperty)CHECK_DECAY, (Comparable)Boolean.valueOf((boolean)false)), 4);
            } else {
                this.destroy(worldIn, pos);
            }
        }
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (worldIn.isRainingAt(pos.up()) && !World.doesBlockHaveSolidTopSurface((IBlockAccess)worldIn, (BlockPos)pos.down()) && rand.nextInt(15) == 1) {
            double d0 = (float)pos.getX() + rand.nextFloat();
            double d1 = (double)pos.getY() - 0.05;
            double d2 = (float)pos.getZ() + rand.nextFloat();
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
        }
    }

    private void destroy(World worldIn, BlockPos pos) {
        this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
        worldIn.setBlockToAir(pos);
    }

    public int quantityDropped(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock((Block)Blocks.sapling);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote) {
            int i = this.getSaplingDropChance(state);
            if (fortune > 0 && (i -= 2 << fortune) < 10) {
                i = 10;
            }
            if (worldIn.rand.nextInt(i) == 0) {
                Item item = this.getItemDropped(state, worldIn.rand, fortune);
                BlockLeaves.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack(item, 1, this.damageDropped(state)));
            }
            i = 200;
            if (fortune > 0 && (i -= 10 << fortune) < 40) {
                i = 40;
            }
            this.dropApple(worldIn, pos, state, i);
        }
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
    }

    protected int getSaplingDropChance(IBlockState state) {
        return 20;
    }

    public boolean isOpaqueCube() {
        return !this.fancyGraphics;
    }

    public void setGraphicsLevel(boolean fancy) {
        this.isTransparent = fancy;
        this.fancyGraphics = fancy;
        this.iconIndex = fancy ? 0 : 1;
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return this.isTransparent ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }

    public boolean isVisuallyOpaque() {
        return false;
    }

    public abstract BlockPlanks.EnumType getWoodType(int var1);
}
