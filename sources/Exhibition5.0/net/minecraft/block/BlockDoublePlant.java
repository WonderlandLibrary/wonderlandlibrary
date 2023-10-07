// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.IStringSerializable;
import net.minecraft.block.state.BlockState;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;

public class BlockDoublePlant extends BlockBush implements IGrowable
{
    public static final PropertyEnum VARIANT_PROP;
    public static final PropertyEnum HALF_PROP;
    private static final String __OBFID = "CL_00000231";
    
    public BlockDoublePlant() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.VARIANT_PROP, EnumPlantType.SUNFLOWER).withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.LOWER));
        this.setHardness(0.0f);
        this.setStepSound(Block.soundTypeGrass);
        this.setUnlocalizedName("doublePlant");
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public EnumPlantType func_176490_e(final IBlockAccess p_176490_1_, final BlockPos p_176490_2_) {
        IBlockState var3 = p_176490_1_.getBlockState(p_176490_2_);
        if (var3.getBlock() == this) {
            var3 = this.getActualState(var3, p_176490_1_, p_176490_2_);
            return (EnumPlantType)var3.getValue(BlockDoublePlant.VARIANT_PROP);
        }
        return EnumPlantType.FERN;
    }
    
    @Override
    public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.offsetUp());
    }
    
    @Override
    public boolean isReplaceable(final World worldIn, final BlockPos pos) {
        final IBlockState var3 = worldIn.getBlockState(pos);
        if (var3.getBlock() != this) {
            return true;
        }
        final EnumPlantType var4 = (EnumPlantType)this.getActualState(var3, worldIn, pos).getValue(BlockDoublePlant.VARIANT_PROP);
        return var4 == EnumPlantType.FERN || var4 == EnumPlantType.GRASS;
    }
    
    @Override
    protected void func_176475_e(final World worldIn, final BlockPos p_176475_2_, final IBlockState p_176475_3_) {
        if (!this.canBlockStay(worldIn, p_176475_2_, p_176475_3_)) {
            final boolean var4 = p_176475_3_.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER;
            final BlockPos var5 = var4 ? p_176475_2_ : p_176475_2_.offsetUp();
            final BlockPos var6 = var4 ? p_176475_2_.offsetDown() : p_176475_2_;
            final Object var7 = var4 ? this : worldIn.getBlockState(var5).getBlock();
            final Object var8 = var4 ? worldIn.getBlockState(var6).getBlock() : this;
            if (var7 == this) {
                worldIn.setBlockState(var5, Blocks.air.getDefaultState(), 3);
            }
            if (var8 == this) {
                worldIn.setBlockState(var6, Blocks.air.getDefaultState(), 3);
                if (!var4) {
                    this.dropBlockAsItem(worldIn, var6, p_176475_3_, 0);
                }
            }
        }
    }
    
    @Override
    public boolean canBlockStay(final World worldIn, final BlockPos p_180671_2_, final IBlockState p_180671_3_) {
        if (p_180671_3_.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock() == this;
        }
        final IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetUp());
        return var4.getBlock() == this && super.canBlockStay(worldIn, p_180671_2_, var4);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER) {
            return null;
        }
        final EnumPlantType var4 = (EnumPlantType)state.getValue(BlockDoublePlant.VARIANT_PROP);
        return (var4 == EnumPlantType.FERN) ? null : ((var4 == EnumPlantType.GRASS) ? ((rand.nextInt(8) == 0) ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
    }
    
    @Override
    public int damageDropped(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF_PROP) != EnumBlockHalf.UPPER && state.getValue(BlockDoublePlant.VARIANT_PROP) != EnumPlantType.GRASS) ? ((EnumPlantType)state.getValue(BlockDoublePlant.VARIANT_PROP)).func_176936_a() : 0;
    }
    
    @Override
    public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass) {
        final EnumPlantType var4 = this.func_176490_e(worldIn, pos);
        return (var4 != EnumPlantType.GRASS && var4 != EnumPlantType.FERN) ? 16777215 : BiomeColorHelper.func_180286_a(worldIn, pos);
    }
    
    public void func_176491_a(final World worldIn, final BlockPos p_176491_2_, final EnumPlantType p_176491_3_, final int p_176491_4_) {
        worldIn.setBlockState(p_176491_2_, this.getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT_PROP, p_176491_3_), p_176491_4_);
        worldIn.setBlockState(p_176491_2_.offsetUp(), this.getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.UPPER), p_176491_4_);
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        worldIn.setBlockState(pos.offsetUp(), this.getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.UPPER), 2);
    }
    
    @Override
    public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te) {
        if (worldIn.isRemote || playerIn.getCurrentEquippedItem() == null || playerIn.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(BlockDoublePlant.HALF_PROP) != EnumBlockHalf.LOWER || !this.func_176489_b(worldIn, pos, state, playerIn)) {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }
    
    @Override
    public void onBlockHarvested(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.offsetDown()).getBlock() == this) {
                if (!playerIn.capabilities.isCreativeMode) {
                    final IBlockState var5 = worldIn.getBlockState(pos.offsetDown());
                    final EnumPlantType var6 = (EnumPlantType)var5.getValue(BlockDoublePlant.VARIANT_PROP);
                    if (var6 != EnumPlantType.FERN && var6 != EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.offsetDown(), true);
                    }
                    else if (!worldIn.isRemote) {
                        if (playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
                            this.func_176489_b(worldIn, pos, var5, playerIn);
                            worldIn.setBlockToAir(pos.offsetDown());
                        }
                        else {
                            worldIn.destroyBlock(pos.offsetDown(), true);
                        }
                    }
                    else {
                        worldIn.setBlockToAir(pos.offsetDown());
                    }
                }
                else {
                    worldIn.setBlockToAir(pos.offsetDown());
                }
            }
        }
        else if (playerIn.capabilities.isCreativeMode && worldIn.getBlockState(pos.offsetUp()).getBlock() == this) {
            worldIn.setBlockState(pos.offsetUp(), Blocks.air.getDefaultState(), 2);
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }
    
    private boolean func_176489_b(final World worldIn, final BlockPos p_176489_2_, final IBlockState p_176489_3_, final EntityPlayer p_176489_4_) {
        final EnumPlantType var5 = (EnumPlantType)p_176489_3_.getValue(BlockDoublePlant.VARIANT_PROP);
        if (var5 != EnumPlantType.FERN && var5 != EnumPlantType.GRASS) {
            return false;
        }
        p_176489_4_.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        final int var6 = ((var5 == EnumPlantType.GRASS) ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();
        Block.spawnAsEntity(worldIn, p_176489_2_, new ItemStack(Blocks.tallgrass, 2, var6));
        return true;
    }
    
    @Override
    public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list) {
        for (final EnumPlantType var7 : EnumPlantType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176936_a()));
        }
    }
    
    @Override
    public int getDamageValue(final World worldIn, final BlockPos pos) {
        return this.func_176490_e(worldIn, pos).func_176936_a();
    }
    
    @Override
    public boolean isStillGrowing(final World worldIn, final BlockPos p_176473_2_, final IBlockState p_176473_3_, final boolean p_176473_4_) {
        final EnumPlantType var5 = this.func_176490_e(worldIn, p_176473_2_);
        return var5 != EnumPlantType.GRASS && var5 != EnumPlantType.FERN;
    }
    
    @Override
    public boolean canUseBonemeal(final World worldIn, final Random p_180670_2_, final BlockPos p_180670_3_, final IBlockState p_180670_4_) {
        return true;
    }
    
    @Override
    public void grow(final World worldIn, final Random p_176474_2_, final BlockPos p_176474_3_, final IBlockState p_176474_4_) {
        Block.spawnAsEntity(worldIn, p_176474_3_, new ItemStack(this, 1, this.func_176490_e(worldIn, p_176474_3_).func_176936_a()));
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return ((meta & 0x8) > 0) ? this.getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(BlockDoublePlant.HALF_PROP, EnumBlockHalf.LOWER).withProperty(BlockDoublePlant.VARIANT_PROP, EnumPlantType.func_176938_a(meta & 0x7));
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
        if (state.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER) {
            final IBlockState var4 = worldIn.getBlockState(pos.offsetDown());
            if (var4.getBlock() == this) {
                state = state.withProperty(BlockDoublePlant.VARIANT_PROP, var4.getValue(BlockDoublePlant.VARIANT_PROP));
            }
        }
        return state;
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (state.getValue(BlockDoublePlant.HALF_PROP) == EnumBlockHalf.UPPER) ? 8 : ((EnumPlantType)state.getValue(BlockDoublePlant.VARIANT_PROP)).func_176936_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockDoublePlant.HALF_PROP, BlockDoublePlant.VARIANT_PROP });
    }
    
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
    
    static {
        VARIANT_PROP = PropertyEnum.create("variant", EnumPlantType.class);
        HALF_PROP = PropertyEnum.create("half", EnumBlockHalf.class);
    }
    
    enum EnumBlockHalf implements IStringSerializable
    {
        UPPER("UPPER", 0), 
        LOWER("LOWER", 1);
        
        private static final EnumBlockHalf[] $VALUES;
        private static final String __OBFID = "CL_00002122";
        
        private EnumBlockHalf(final String p_i45724_1_, final int p_i45724_2_) {
        }
        
        @Override
        public String toString() {
            return this.getName();
        }
        
        @Override
        public String getName() {
            return (this == EnumBlockHalf.UPPER) ? "upper" : "lower";
        }
        
        static {
            $VALUES = new EnumBlockHalf[] { EnumBlockHalf.UPPER, EnumBlockHalf.LOWER };
        }
    }
    
    public enum EnumPlantType implements IStringSerializable
    {
        SUNFLOWER("SUNFLOWER", 0, 0, "sunflower"), 
        SYRINGA("SYRINGA", 1, 1, "syringa"), 
        GRASS("GRASS", 2, 2, "double_grass", "grass"), 
        FERN("FERN", 3, 3, "double_fern", "fern"), 
        ROSE("ROSE", 4, 4, "double_rose", "rose"), 
        PAEONIA("PAEONIA", 5, 5, "paeonia");
        
        private static final EnumPlantType[] field_176941_g;
        private final int field_176949_h;
        private final String field_176950_i;
        private final String field_176947_j;
        private static final EnumPlantType[] $VALUES;
        private static final String __OBFID = "CL_00002121";
        
        private EnumPlantType(final String p_i45722_1_, final int p_i45722_2_, final int p_i45722_3_, final String p_i45722_4_) {
            this(p_i45722_1_, p_i45722_2_, p_i45722_3_, p_i45722_4_, p_i45722_4_);
        }
        
        private EnumPlantType(final String p_i45723_1_, final int p_i45723_2_, final int p_i45723_3_, final String p_i45723_4_, final String p_i45723_5_) {
            this.field_176949_h = p_i45723_3_;
            this.field_176950_i = p_i45723_4_;
            this.field_176947_j = p_i45723_5_;
        }
        
        public int func_176936_a() {
            return this.field_176949_h;
        }
        
        @Override
        public String toString() {
            return this.field_176950_i;
        }
        
        public static EnumPlantType func_176938_a(int p_176938_0_) {
            if (p_176938_0_ < 0 || p_176938_0_ >= EnumPlantType.field_176941_g.length) {
                p_176938_0_ = 0;
            }
            return EnumPlantType.field_176941_g[p_176938_0_];
        }
        
        @Override
        public String getName() {
            return this.field_176950_i;
        }
        
        public String func_176939_c() {
            return this.field_176947_j;
        }
        
        static {
            field_176941_g = new EnumPlantType[values().length];
            $VALUES = new EnumPlantType[] { EnumPlantType.SUNFLOWER, EnumPlantType.SYRINGA, EnumPlantType.GRASS, EnumPlantType.FERN, EnumPlantType.ROSE, EnumPlantType.PAEONIA };
            for (final EnumPlantType var4 : values()) {
                EnumPlantType.field_176941_g[var4.func_176936_a()] = var4;
            }
        }
    }
}
