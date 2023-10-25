package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class BlockBrewingStand
extends BlockContainer {
    public static final PropertyBool[] HAS_BOTTLE = new PropertyBool[]{PropertyBool.create((String)"has_bottle_0"), PropertyBool.create((String)"has_bottle_1"), PropertyBool.create((String)"has_bottle_2")};

    public BlockBrewingStand() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)HAS_BOTTLE[0], (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)HAS_BOTTLE[1], (Comparable)Boolean.valueOf((boolean)false)).withProperty((IProperty)HAS_BOTTLE[2], (Comparable)Boolean.valueOf((boolean)false)));
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal((String)"item.brewingStand.name");
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public int getRenderType() {
        return 3;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBrewingStand();
    }

    public boolean isFullCube() {
        return false;
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.setBlockBounds(0.4375f, 0.0f, 0.4375f, 0.5625f, 0.875f, 0.5625f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            playerIn.displayGUIChest((IInventory)((TileEntityBrewingStand)tileentity));
            playerIn.triggerAchievement(StatList.field_181729_M);
        }
        return true;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tileentity;
        if (stack.hasDisplayName() && (tileentity = worldIn.getTileEntity(pos)) instanceof TileEntityBrewingStand) {
            ((TileEntityBrewingStand)tileentity).setName(stack.getDisplayName());
        }
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        double d0 = (float)pos.getX() + 0.4f + rand.nextFloat() * 0.2f;
        double d1 = (float)pos.getY() + 0.7f + rand.nextFloat() * 0.3f;
        double d2 = (float)pos.getZ() + 0.4f + rand.nextFloat() * 0.2f;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBrewingStand) {
            InventoryHelper.dropInventoryItems((World)worldIn, (BlockPos)pos, (IInventory)((TileEntityBrewingStand)tileentity));
        }
        super.breakBlock(worldIn, pos, state);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.brewing_stand;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.brewing_stand;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return Container.calcRedstone((TileEntity)worldIn.getTileEntity(pos));
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();
        for (int i = 0; i < 3; ++i) {
            iblockstate = iblockstate.withProperty((IProperty)HAS_BOTTLE[i], (Comparable)Boolean.valueOf(((meta & 1 << i) > 0 ? 1 : 0) != 0));
        }
        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        int i = 0;
        for (int j = 0; j < 3; ++j) {
            if (!((Boolean)state.getValue((IProperty)HAS_BOTTLE[j])).booleanValue()) continue;
            i |= 1 << j;
        }
        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{HAS_BOTTLE[0], HAS_BOTTLE[1], HAS_BOTTLE[2]});
    }
}
