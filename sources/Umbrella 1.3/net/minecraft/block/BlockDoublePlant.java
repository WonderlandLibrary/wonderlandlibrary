/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

public class BlockDoublePlant
extends BlockBush
implements IGrowable {
    public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", EnumPlantType.class);
    public static final PropertyEnum HALF_PROP = PropertyEnum.create("half", EnumBlockHalf.class);
    private static final String __OBFID = "CL_00000231";

    public BlockDoublePlant() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT_PROP, (Comparable)((Object)EnumPlantType.SUNFLOWER)).withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.LOWER)));
        this.setHardness(0.0f);
        this.setStepSound(soundTypeGrass);
        this.setUnlocalizedName("doublePlant");
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public EnumPlantType func_176490_e(IBlockAccess p_176490_1_, BlockPos p_176490_2_) {
        IBlockState var3 = p_176490_1_.getBlockState(p_176490_2_);
        if (var3.getBlock() == this) {
            var3 = this.getActualState(var3, p_176490_1_, p_176490_2_);
            return (EnumPlantType)((Object)var3.getValue(VARIANT_PROP));
        }
        return EnumPlantType.FERN;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.offsetUp());
    }

    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
        IBlockState var3 = worldIn.getBlockState(pos);
        if (var3.getBlock() != this) {
            return true;
        }
        EnumPlantType var4 = (EnumPlantType)((Object)this.getActualState(var3, worldIn, pos).getValue(VARIANT_PROP));
        return var4 == EnumPlantType.FERN || var4 == EnumPlantType.GRASS;
    }

    @Override
    protected void func_176475_e(World worldIn, BlockPos p_176475_2_, IBlockState p_176475_3_) {
        if (!this.canBlockStay(worldIn, p_176475_2_, p_176475_3_)) {
            BlockDoublePlant var8;
            boolean var4 = p_176475_3_.getValue(HALF_PROP) == EnumBlockHalf.UPPER;
            BlockPos var5 = var4 ? p_176475_2_ : p_176475_2_.offsetUp();
            BlockPos var6 = var4 ? p_176475_2_.offsetDown() : p_176475_2_;
            BlockDoublePlant var7 = var4 ? this : worldIn.getBlockState(var5).getBlock();
            Block block = var8 = var4 ? worldIn.getBlockState(var6).getBlock() : this;
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
    public boolean canBlockStay(World worldIn, BlockPos p_180671_2_, IBlockState p_180671_3_) {
        if (p_180671_3_.getValue(HALF_PROP) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(p_180671_2_.offsetDown()).getBlock() == this;
        }
        IBlockState var4 = worldIn.getBlockState(p_180671_2_.offsetUp());
        return var4.getBlock() == this && super.canBlockStay(worldIn, p_180671_2_, var4);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER) {
            return null;
        }
        EnumPlantType var4 = (EnumPlantType)((Object)state.getValue(VARIANT_PROP));
        return var4 == EnumPlantType.FERN ? null : (var4 == EnumPlantType.GRASS ? (rand.nextInt(8) == 0 ? Items.wheat_seeds : null) : Item.getItemFromBlock(this));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(HALF_PROP) != EnumBlockHalf.UPPER && state.getValue(VARIANT_PROP) != EnumPlantType.GRASS ? ((EnumPlantType)((Object)state.getValue(VARIANT_PROP))).func_176936_a() : 0;
    }

    @Override
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        EnumPlantType var4 = this.func_176490_e(worldIn, pos);
        return var4 != EnumPlantType.GRASS && var4 != EnumPlantType.FERN ? 0xFFFFFF : BiomeColorHelper.func_180286_a(worldIn, pos);
    }

    public void func_176491_a(World worldIn, BlockPos p_176491_2_, EnumPlantType p_176491_3_, int p_176491_4_) {
        worldIn.setBlockState(p_176491_2_, this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.LOWER)).withProperty(VARIANT_PROP, (Comparable)((Object)p_176491_3_)), p_176491_4_);
        worldIn.setBlockState(p_176491_2_.offsetUp(), this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.UPPER)), p_176491_4_);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.offsetUp(), this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.UPPER)), 2);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te) {
        if (worldIn.isRemote || playerIn.getCurrentEquippedItem() == null || playerIn.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(HALF_PROP) != EnumBlockHalf.LOWER || !this.func_176489_b(worldIn, pos, state, playerIn)) {
            super.harvestBlock(worldIn, playerIn, pos, state, te);
        }
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn) {
        if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.offsetDown()).getBlock() == this) {
                if (!playerIn.capabilities.isCreativeMode) {
                    IBlockState var5 = worldIn.getBlockState(pos.offsetDown());
                    EnumPlantType var6 = (EnumPlantType)((Object)var5.getValue(VARIANT_PROP));
                    if (var6 != EnumPlantType.FERN && var6 != EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.offsetDown(), true);
                    } else if (!worldIn.isRemote) {
                        if (playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears) {
                            this.func_176489_b(worldIn, pos, var5, playerIn);
                            worldIn.setBlockToAir(pos.offsetDown());
                        } else {
                            worldIn.destroyBlock(pos.offsetDown(), true);
                        }
                    } else {
                        worldIn.setBlockToAir(pos.offsetDown());
                    }
                } else {
                    worldIn.setBlockToAir(pos.offsetDown());
                }
            }
        } else if (playerIn.capabilities.isCreativeMode && worldIn.getBlockState(pos.offsetUp()).getBlock() == this) {
            worldIn.setBlockState(pos.offsetUp(), Blocks.air.getDefaultState(), 2);
        }
        super.onBlockHarvested(worldIn, pos, state, playerIn);
    }

    private boolean func_176489_b(World worldIn, BlockPos p_176489_2_, IBlockState p_176489_3_, EntityPlayer p_176489_4_) {
        EnumPlantType var5 = (EnumPlantType)((Object)p_176489_3_.getValue(VARIANT_PROP));
        if (var5 != EnumPlantType.FERN && var5 != EnumPlantType.GRASS) {
            return false;
        }
        p_176489_4_.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
        int var6 = (var5 == EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).func_177044_a();
        BlockDoublePlant.spawnAsEntity(worldIn, p_176489_2_, new ItemStack(Blocks.tallgrass, 2, var6));
        return true;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumPlantType var7 : EnumPlantType.values()) {
            list.add(new ItemStack(itemIn, 1, var7.func_176936_a()));
        }
    }

    @Override
    public int getDamageValue(World worldIn, BlockPos pos) {
        return this.func_176490_e(worldIn, pos).func_176936_a();
    }

    @Override
    public boolean isStillGrowing(World worldIn, BlockPos p_176473_2_, IBlockState p_176473_3_, boolean p_176473_4_) {
        EnumPlantType var5 = this.func_176490_e(worldIn, p_176473_2_);
        return var5 != EnumPlantType.GRASS && var5 != EnumPlantType.FERN;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random p_180670_2_, BlockPos p_180670_3_, IBlockState p_180670_4_) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random p_176474_2_, BlockPos p_176474_3_, IBlockState p_176474_4_) {
        BlockDoublePlant.spawnAsEntity(worldIn, p_176474_3_, new ItemStack(this, 1, this.func_176490_e(worldIn, p_176474_3_).func_176936_a()));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.UPPER)) : this.getDefaultState().withProperty(HALF_PROP, (Comparable)((Object)EnumBlockHalf.LOWER)).withProperty(VARIANT_PROP, (Comparable)((Object)EnumPlantType.func_176938_a(meta & 7)));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState var4;
        if (state.getValue(HALF_PROP) == EnumBlockHalf.UPPER && (var4 = worldIn.getBlockState(pos.offsetDown())).getBlock() == this) {
            state = state.withProperty(VARIANT_PROP, var4.getValue(VARIANT_PROP));
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF_PROP) == EnumBlockHalf.UPPER ? 8 : ((EnumPlantType)((Object)state.getValue(VARIANT_PROP))).func_176936_a();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HALF_PROP, VARIANT_PROP);
    }

    @Override
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    static enum EnumBlockHalf implements IStringSerializable
    {
        UPPER("UPPER", 0),
        LOWER("LOWER", 1);

        private static final EnumBlockHalf[] $VALUES;
        private static final String __OBFID = "CL_00002122";

        static {
            $VALUES = new EnumBlockHalf[]{UPPER, LOWER};
        }

        private EnumBlockHalf(String p_i45724_1_, int p_i45724_2_) {
        }

        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }

    public static enum EnumPlantType implements IStringSerializable
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

        static {
            field_176941_g = new EnumPlantType[EnumPlantType.values().length];
            $VALUES = new EnumPlantType[]{SUNFLOWER, SYRINGA, GRASS, FERN, ROSE, PAEONIA};
            EnumPlantType[] var0 = EnumPlantType.values();
            int var1 = var0.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                EnumPlantType var3;
                EnumPlantType.field_176941_g[var3.func_176936_a()] = var3 = var0[var2];
            }
        }

        private EnumPlantType(String p_i45722_1_, int p_i45722_2_, int p_i45722_3_, String p_i45722_4_) {
            this(p_i45722_1_, p_i45722_2_, p_i45722_3_, p_i45722_4_, p_i45722_4_);
        }

        private EnumPlantType(String p_i45723_1_, int p_i45723_2_, int p_i45723_3_, String p_i45723_4_, String p_i45723_5_) {
            this.field_176949_h = p_i45723_3_;
            this.field_176950_i = p_i45723_4_;
            this.field_176947_j = p_i45723_5_;
        }

        public int func_176936_a() {
            return this.field_176949_h;
        }

        public String toString() {
            return this.field_176950_i;
        }

        public static EnumPlantType func_176938_a(int p_176938_0_) {
            if (p_176938_0_ < 0 || p_176938_0_ >= field_176941_g.length) {
                p_176938_0_ = 0;
            }
            return field_176941_g[p_176938_0_];
        }

        @Override
        public String getName() {
            return this.field_176950_i;
        }

        public String func_176939_c() {
            return this.field_176947_j;
        }
    }
}

