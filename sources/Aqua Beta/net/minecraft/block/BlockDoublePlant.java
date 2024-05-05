package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;

/*
 * Exception performing whole class analysis ignored.
 */
public class BlockDoublePlant
extends BlockBush
implements IGrowable {
    public static final PropertyEnum<EnumPlantType> VARIANT = PropertyEnum.create((String)"variant", EnumPlantType.class);
    public static final PropertyEnum<EnumBlockHalf> HALF = PropertyEnum.create((String)"half", EnumBlockHalf.class);
    public static final PropertyEnum<EnumFacing> FACING = BlockDirectional.FACING;

    public BlockDoublePlant() {
        super(Material.vine);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, (Comparable)EnumPlantType.SUNFLOWER).withProperty(HALF, (Comparable)EnumBlockHalf.LOWER).withProperty(FACING, (Comparable)EnumFacing.NORTH));
        this.setHardness(0.0f);
        this.setStepSound(soundTypeGrass);
        this.setUnlocalizedName("doublePlant");
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public EnumPlantType getVariant(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            iblockstate = this.getActualState(iblockstate, worldIn, pos);
            return (EnumPlantType)iblockstate.getValue(VARIANT);
        }
        return EnumPlantType.FERN;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() != this) {
            return true;
        }
        EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)this.getActualState(iblockstate, (IBlockAccess)worldIn, pos).getValue(VARIANT);
        return blockdoubleplant$enumplanttype == EnumPlantType.FERN || blockdoubleplant$enumplanttype == EnumPlantType.GRASS;
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            BlockDoublePlant block1;
            boolean flag = state.getValue(HALF) == EnumBlockHalf.UPPER;
            BlockPos blockpos = flag ? pos : pos.up();
            BlockPos blockpos1 = flag ? pos.down() : pos;
            BlockDoublePlant block = flag ? this : worldIn.getBlockState(blockpos).getBlock();
            BlockDoublePlant blockDoublePlant = block1 = flag ? worldIn.getBlockState(blockpos1).getBlock() : this;
            if (block == this) {
                worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
            }
            if (block1 == this) {
                worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
                if (!flag) {
                    this.dropBlockAsItem(worldIn, blockpos1, state, 0);
                }
            }
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        IBlockState iblockstate = worldIn.getBlockState(pos.up());
        return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            return null;
        }
        EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue(VARIANT);
        return blockdoubleplant$enumplanttype == EnumPlantType.FERN ? null : (blockdoubleplant$enumplanttype == EnumPlantType.GRASS ? (rand.nextInt(8) == 0 ? Items.wheat_seeds : null) : Item.getItemFromBlock((Block)this));
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(HALF) != EnumBlockHalf.UPPER && state.getValue(VARIANT) != EnumPlantType.GRASS ? ((EnumPlantType)state.getValue(VARIANT)).getMeta() : 0;
    }

    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
        EnumPlantType blockdoubleplant$enumplanttype = this.getVariant(worldIn, pos);
        return blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN ? 0xFFFFFF : BiomeColorHelper.getGrassColorAtPos((IBlockAccess)worldIn, (BlockPos)pos);
    }

    public void placeAt(World worldIn, BlockPos lowerPos, EnumPlantType variant, int flags) {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, (Comparable)EnumBlockHalf.LOWER).withProperty(VARIANT, (Comparable)variant), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(HALF, (Comparable)EnumBlockHalf.UPPER), flags);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, (Comparable)EnumBlockHalf.UPPER), 2);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
        if (worldIn.isRemote || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Items.shears || state.getValue(HALF) != EnumBlockHalf.LOWER || !this.onHarvest(worldIn, pos, state, player)) {
            super.harvestBlock(worldIn, player, pos, state, te);
        }
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.down()).getBlock() == this) {
                if (!player.capabilities.isCreativeMode) {
                    IBlockState iblockstate = worldIn.getBlockState(pos.down());
                    EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)iblockstate.getValue(VARIANT);
                    if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
                        worldIn.destroyBlock(pos.down(), true);
                    } else if (!worldIn.isRemote) {
                        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.shears) {
                            this.onHarvest(worldIn, pos, iblockstate, player);
                            worldIn.setBlockToAir(pos.down());
                        } else {
                            worldIn.destroyBlock(pos.down(), true);
                        }
                    } else {
                        worldIn.setBlockToAir(pos.down());
                    }
                } else {
                    worldIn.setBlockToAir(pos.down());
                }
            }
        } else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this) {
            worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        EnumPlantType blockdoubleplant$enumplanttype = (EnumPlantType)state.getValue(VARIANT);
        if (blockdoubleplant$enumplanttype != EnumPlantType.FERN && blockdoubleplant$enumplanttype != EnumPlantType.GRASS) {
            return false;
        }
        player.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock((Block)this)]);
        int i = (blockdoubleplant$enumplanttype == EnumPlantType.GRASS ? BlockTallGrass.EnumType.GRASS : BlockTallGrass.EnumType.FERN).getMeta();
        BlockDoublePlant.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack((Block)Blocks.tallgrass, 2, i));
        return true;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumPlantType blockdoubleplant$enumplanttype : EnumPlantType.values()) {
            list.add((Object)new ItemStack(itemIn, 1, blockdoubleplant$enumplanttype.getMeta()));
        }
    }

    public int getDamageValue(World worldIn, BlockPos pos) {
        return this.getVariant((IBlockAccess)worldIn, pos).getMeta();
    }

    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        EnumPlantType blockdoubleplant$enumplanttype = this.getVariant((IBlockAccess)worldIn, pos);
        return blockdoubleplant$enumplanttype != EnumPlantType.GRASS && blockdoubleplant$enumplanttype != EnumPlantType.FERN;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        BlockDoublePlant.spawnAsEntity((World)worldIn, (BlockPos)pos, (ItemStack)new ItemStack((Block)this, 1, this.getVariant((IBlockAccess)worldIn, pos).getMeta()));
    }

    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, (Comparable)EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(HALF, (Comparable)EnumBlockHalf.LOWER).withProperty(VARIANT, (Comparable)EnumPlantType.byMetadata((int)(meta & 7)));
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate;
        if (state.getValue(HALF) == EnumBlockHalf.UPPER && (iblockstate = worldIn.getBlockState(pos.down())).getBlock() == this) {
            state = state.withProperty(VARIANT, iblockstate.getValue(VARIANT));
        }
        return state;
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF) == EnumBlockHalf.UPPER ? 8 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex() : ((EnumPlantType)state.getValue(VARIANT)).getMeta();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{HALF, VARIANT, FACING});
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }
}
