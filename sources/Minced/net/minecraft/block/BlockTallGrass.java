// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;

public class BlockTallGrass extends BlockBush implements IGrowable
{
    public static final PropertyEnum<EnumType> TYPE;
    protected static final AxisAlignedBB TALL_GRASS_AABB;
    
    protected BlockTallGrass() {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTallGrass.TYPE, EnumType.DEAD_BUSH));
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockTallGrass.TALL_GRASS_AABB;
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos pos, final IBlockState state) {
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    @Override
    public boolean isReplaceable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return (rand.nextInt(8) == 0) ? Items.WHEAT_SEEDS : Items.AIR;
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return 1 + random.nextInt(fortune * 2 + 1);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(StatList.getBlockStats(this));
            Block.spawnAsEntity(worldIn, pos, new ItemStack(Blocks.TALLGRASS, 1, state.getValue(BlockTallGrass.TYPE).getMeta()));
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
    }
    
    @Override
    public void getSubBlocks(final CreativeTabs itemIn, final NonNullList<ItemStack> items) {
        for (int i = 1; i < 3; ++i) {
            items.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    public boolean canGrow(final World worldIn, final BlockPos pos, final IBlockState state, final boolean isClient) {
        return state.getValue(BlockTallGrass.TYPE) != EnumType.DEAD_BUSH;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random rand, final BlockPos pos, final IBlockState state) {
        BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.GRASS;
        if (state.getValue(BlockTallGrass.TYPE) == EnumType.FERN) {
            blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.FERN;
        }
        if (Blocks.DOUBLE_PLANT.canPlaceBlockAt(worldIn, pos)) {
            Blocks.DOUBLE_PLANT.placeAt(worldIn, pos, blockdoubleplant$enumplanttype, 2);
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockTallGrass.TYPE, EnumType.byMetadata(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue(BlockTallGrass.TYPE).getMeta();
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockTallGrass.TYPE });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XYZ;
    }
    
    static {
        TYPE = PropertyEnum.create("type", EnumType.class);
        TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552, 0.0, 0.09999999403953552, 0.8999999761581421, 0.800000011920929, 0.8999999761581421);
    }
    
    public enum EnumType implements IStringSerializable
    {
        DEAD_BUSH(0, "dead_bush"), 
        GRASS(1, "tall_grass"), 
        FERN(2, "fern");
        
        private static final EnumType[] META_LOOKUP;
        private final int meta;
        private final String name;
        
        private EnumType(final int meta, final String name) {
            this.meta = meta;
            this.name = name;
        }
        
        public int getMeta() {
            return this.meta;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= EnumType.META_LOOKUP.length) {
                meta = 0;
            }
            return EnumType.META_LOOKUP[meta];
        }
        
        @Override
        public String getName() {
            return this.name;
        }
        
        static {
            META_LOOKUP = new EnumType[values().length];
            for (final EnumType blocktallgrass$enumtype : values()) {
                EnumType.META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype;
            }
        }
    }
}
