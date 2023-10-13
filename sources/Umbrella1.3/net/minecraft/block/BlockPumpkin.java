/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class BlockPumpkin
extends BlockDirectional {
    private BlockPattern field_176394_a;
    private BlockPattern field_176393_b;
    private BlockPattern field_176395_M;
    private BlockPattern field_176396_O;
    private static final String __OBFID = "CL_00000291";

    protected BlockPumpkin() {
        super(Material.gourd);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, (Comparable)((Object)EnumFacing.NORTH)));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        this.createGolem(worldIn, pos);
    }

    public boolean func_176390_d(World worldIn, BlockPos p_176390_2_) {
        return this.func_176392_j().func_177681_a(worldIn, p_176390_2_) != null || this.func_176389_S().func_177681_a(worldIn, p_176390_2_) != null;
    }

    private void createGolem(World worldIn, BlockPos p_180673_2_) {
        block9: {
            int var6;
            BlockPattern.PatternHelper var3;
            block8: {
                int var62;
                var3 = this.func_176391_l().func_177681_a(worldIn, p_180673_2_);
                if (var3 == null) break block8;
                for (int var4 = 0; var4 < this.func_176391_l().func_177685_b(); ++var4) {
                    BlockWorldState var5 = var3.func_177670_a(0, var4, 0);
                    worldIn.setBlockState(var5.getPos(), Blocks.air.getDefaultState(), 2);
                }
                EntitySnowman var9 = new EntitySnowman(worldIn);
                BlockPos var11 = var3.func_177670_a(0, 2, 0).getPos();
                var9.setLocationAndAngles((double)var11.getX() + 0.5, (double)var11.getY() + 0.05, (double)var11.getZ() + 0.5, 0.0f, 0.0f);
                worldIn.spawnEntityInWorld(var9);
                for (var62 = 0; var62 < 120; ++var62) {
                    worldIn.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, (double)var11.getX() + worldIn.rand.nextDouble(), (double)var11.getY() + worldIn.rand.nextDouble() * 2.5, (double)var11.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
                }
                for (var62 = 0; var62 < this.func_176391_l().func_177685_b(); ++var62) {
                    BlockWorldState var7 = var3.func_177670_a(0, var62, 0);
                    worldIn.func_175722_b(var7.getPos(), Blocks.air);
                }
                break block9;
            }
            var3 = this.func_176388_T().func_177681_a(worldIn, p_180673_2_);
            if (var3 == null) break block9;
            for (int var4 = 0; var4 < this.func_176388_T().func_177684_c(); ++var4) {
                for (int var12 = 0; var12 < this.func_176388_T().func_177685_b(); ++var12) {
                    worldIn.setBlockState(var3.func_177670_a(var4, var12, 0).getPos(), Blocks.air.getDefaultState(), 2);
                }
            }
            BlockPos var10 = var3.func_177670_a(1, 2, 0).getPos();
            EntityIronGolem var13 = new EntityIronGolem(worldIn);
            var13.setPlayerCreated(true);
            var13.setLocationAndAngles((double)var10.getX() + 0.5, (double)var10.getY() + 0.05, (double)var10.getZ() + 0.5, 0.0f, 0.0f);
            worldIn.spawnEntityInWorld(var13);
            for (var6 = 0; var6 < 120; ++var6) {
                worldIn.spawnParticle(EnumParticleTypes.SNOWBALL, (double)var10.getX() + worldIn.rand.nextDouble(), (double)var10.getY() + worldIn.rand.nextDouble() * 3.9, (double)var10.getZ() + worldIn.rand.nextDouble(), 0.0, 0.0, 0.0, new int[0]);
            }
            for (var6 = 0; var6 < this.func_176388_T().func_177684_c(); ++var6) {
                for (int var14 = 0; var14 < this.func_176388_T().func_177685_b(); ++var14) {
                    BlockWorldState var8 = var3.func_177670_a(var6, var14, 0);
                    worldIn.func_175722_b(var8.getPos(), Blocks.air);
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState((BlockPos)pos).getBlock().blockMaterial.isReplaceable() && World.doesBlockHaveSolidTopSurface(worldIn, pos.offsetDown());
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)placer.func_174811_aO().getOpposite()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, (Comparable)((Object)EnumFacing.getHorizontal(meta)));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)((Object)state.getValue(AGE))).getHorizontalIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, AGE);
    }

    protected BlockPattern func_176392_j() {
        if (this.field_176394_a == null) {
            this.field_176394_a = FactoryBlockPattern.start().aisle(" ", "#", "#").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.field_176394_a;
    }

    protected BlockPattern func_176391_l() {
        if (this.field_176393_b == null) {
            this.field_176393_b = FactoryBlockPattern.start().aisle("^", "#", "#").where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
        }
        return this.field_176393_b;
    }

    protected BlockPattern func_176389_S() {
        if (this.field_176395_M == null) {
            this.field_176395_M = FactoryBlockPattern.start().aisle("~ ~", "###", "~#~").where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176395_M;
    }

    protected BlockPattern func_176388_T() {
        if (this.field_176396_O == null) {
            this.field_176396_O = FactoryBlockPattern.start().aisle("~^~", "###", "~#~").where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).where('~', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.air))).build();
        }
        return this.field_176396_O;
    }
}

