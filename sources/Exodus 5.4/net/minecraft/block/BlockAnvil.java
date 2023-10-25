/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block;

import java.util.List;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockAnvil
extends BlockFalling {
    public static final PropertyInteger DAMAGE;
    public static final PropertyDirection FACING;

    @Override
    public int damageDropped(IBlockState iBlockState) {
        return iBlockState.getValue(DAMAGE);
    }

    protected BlockAnvil() {
        super(Material.anvil);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DAMAGE, 0));
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, FACING, DAMAGE);
    }

    @Override
    protected void onStartFalling(EntityFallingBlock entityFallingBlock) {
        entityFallingBlock.setHurtEntities(true);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer entityPlayer, EnumFacing enumFacing, float f, float f2, float f3) {
        if (!world.isRemote) {
            entityPlayer.displayGui(new Anvil(world, blockPos));
        }
        return true;
    }

    @Override
    public IBlockState getStateForEntityRender(IBlockState iBlockState) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
    }

    @Override
    public void onEndFalling(World world, BlockPos blockPos) {
        world.playAuxSFX(1022, blockPos, 0);
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    static {
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        DAMAGE = PropertyInteger.create("damage", 0, 2);
    }

    @Override
    public IBlockState getStateFromMeta(int n) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(n & 3)).withProperty(DAMAGE, (n & 0xF) >> 2);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iBlockAccess, BlockPos blockPos) {
        EnumFacing enumFacing = iBlockAccess.getBlockState(blockPos).getValue(FACING);
        if (enumFacing.getAxis() == EnumFacing.Axis.X) {
            this.setBlockBounds(0.0f, 0.0f, 0.125f, 1.0f, 1.0f, 0.875f);
        } else {
            this.setBlockBounds(0.125f, 0.0f, 0.0f, 0.875f, 1.0f, 1.0f);
        }
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing enumFacing, float f, float f2, float f3, int n, EntityLivingBase entityLivingBase) {
        EnumFacing enumFacing2 = entityLivingBase.getHorizontalFacing().rotateY();
        return super.onBlockPlaced(world, blockPos, enumFacing, f, f2, f3, n, entityLivingBase).withProperty(FACING, enumFacing2).withProperty(DAMAGE, n >> 2);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState iBlockState) {
        int n = 0;
        n |= iBlockState.getValue(FACING).getHorizontalIndex();
        return n |= iBlockState.getValue(DAMAGE) << 2;
    }

    public static class Anvil
    implements IInteractionObject {
        private final BlockPos position;
        private final World world;

        @Override
        public boolean hasCustomName() {
            return false;
        }

        public Anvil(World world, BlockPos blockPos) {
            this.world = world;
            this.position = blockPos;
        }

        @Override
        public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
            return new ContainerRepair(inventoryPlayer, this.world, this.position, entityPlayer);
        }

        @Override
        public String getGuiID() {
            return "minecraft:anvil";
        }

        @Override
        public IChatComponent getDisplayName() {
            return new ChatComponentTranslation(String.valueOf(Blocks.anvil.getUnlocalizedName()) + ".name", new Object[0]);
        }

        @Override
        public String getName() {
            return "anvil";
        }
    }
}

