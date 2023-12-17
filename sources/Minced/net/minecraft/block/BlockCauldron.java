// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCauldron extends Block
{
    public static final PropertyInteger LEVEL;
    protected static final AxisAlignedBB AABB_LEGS;
    protected static final AxisAlignedBB AABB_WALL_NORTH;
    protected static final AxisAlignedBB AABB_WALL_SOUTH;
    protected static final AxisAlignedBB AABB_WALL_EAST;
    protected static final AxisAlignedBB AABB_WALL_WEST;
    
    public BlockCauldron() {
        super(Material.IRON, MapColor.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, 0));
    }
    
    @Override
    public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockCauldron.AABB_LEGS);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockCauldron.AABB_WALL_WEST);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockCauldron.AABB_WALL_NORTH);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockCauldron.AABB_WALL_EAST);
        Block.addCollisionBoxToList(pos, entityBox, collidingBoxes, BlockCauldron.AABB_WALL_SOUTH);
    }
    
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
        return BlockCauldron.FULL_BLOCK_AABB;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public boolean isFullCube(final IBlockState state) {
        return false;
    }
    
    @Override
    public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        final int i = state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
        final float f = pos.getY() + (6.0f + 3 * i) / 16.0f;
        if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && entityIn.getEntityBoundingBox().minY <= f) {
            entityIn.extinguish();
            this.setWaterLevel(worldIn, pos, state, i - 1);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final ItemStack itemstack = playerIn.getHeldItem(hand);
        if (itemstack.isEmpty()) {
            return true;
        }
        final int i = state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
        final Item item = itemstack.getItem();
        if (item == Items.WATER_BUCKET) {
            if (i < 3 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.setHeldItem(hand, new ItemStack(Items.BUCKET));
                }
                playerIn.addStat(StatList.CAULDRON_FILLED);
                this.setWaterLevel(worldIn, pos, state, 3);
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return true;
        }
        if (item == Items.BUCKET) {
            if (i == 3 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        playerIn.setHeldItem(hand, new ItemStack(Items.WATER_BUCKET));
                    }
                    else if (!playerIn.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                        playerIn.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                    }
                }
                playerIn.addStat(StatList.CAULDRON_USED);
                this.setWaterLevel(worldIn, pos, state, 0);
                worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
            return true;
        }
        if (item == Items.GLASS_BOTTLE) {
            if (i > 0 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack itemstack2 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                    playerIn.addStat(StatList.CAULDRON_USED);
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        playerIn.setHeldItem(hand, itemstack2);
                    }
                    else if (!playerIn.inventory.addItemStackToInventory(itemstack2)) {
                        playerIn.dropItem(itemstack2, false);
                    }
                    else if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }
                }
                worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        }
        if (item == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER) {
            if (i < 3 && !worldIn.isRemote) {
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack itemstack3 = new ItemStack(Items.GLASS_BOTTLE);
                    playerIn.addStat(StatList.CAULDRON_USED);
                    playerIn.setHeldItem(hand, itemstack3);
                    if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }
                }
                worldIn.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                this.setWaterLevel(worldIn, pos, state, i + 1);
            }
            return true;
        }
        if (i > 0 && item instanceof ItemArmor) {
            final ItemArmor itemarmor = (ItemArmor)item;
            if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack) && !worldIn.isRemote) {
                itemarmor.removeColor(itemstack);
                this.setWaterLevel(worldIn, pos, state, i - 1);
                playerIn.addStat(StatList.ARMOR_CLEANED);
                return true;
            }
        }
        if (i > 0 && item instanceof ItemBanner) {
            if (TileEntityBanner.getPatterns(itemstack) > 0 && !worldIn.isRemote) {
                final ItemStack itemstack4 = itemstack.copy();
                itemstack4.setCount(1);
                TileEntityBanner.removeBannerData(itemstack4);
                playerIn.addStat(StatList.BANNER_CLEANED);
                if (!playerIn.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                    this.setWaterLevel(worldIn, pos, state, i - 1);
                }
                if (itemstack.isEmpty()) {
                    playerIn.setHeldItem(hand, itemstack4);
                }
                else if (!playerIn.inventory.addItemStackToInventory(itemstack4)) {
                    playerIn.dropItem(itemstack4, false);
                }
                else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }
            }
            return true;
        }
        return false;
    }
    
    public void setWaterLevel(final World worldIn, final BlockPos pos, final IBlockState state, final int level) {
        worldIn.setBlockState(pos, state.withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, MathHelper.clamp(level, 0, 3)), 2);
        worldIn.updateComparatorOutputLevel(pos, this);
    }
    
    @Override
    public void fillWithRain(final World worldIn, final BlockPos pos) {
        if (worldIn.rand.nextInt(20) == 1) {
            final float f = worldIn.getBiome(pos).getTemperature(pos);
            if (worldIn.getBiomeProvider().getTemperatureAtHeight(f, pos.getY()) >= 0.15f) {
                final IBlockState iblockstate = worldIn.getBlockState(pos);
                if (iblockstate.getValue((IProperty<Integer>)BlockCauldron.LEVEL) < 3) {
                    worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty<Comparable>)BlockCauldron.LEVEL), 2);
                }
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.CAULDRON;
    }
    
    @Override
    public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
        return new ItemStack(Items.CAULDRON);
    }
    
    @Override
    @Deprecated
    public boolean hasComparatorInputOverride(final IBlockState state) {
        return true;
    }
    
    @Override
    @Deprecated
    public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos) {
        return blockState.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockCauldron.LEVEL, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return state.getValue((IProperty<Integer>)BlockCauldron.LEVEL);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockCauldron.LEVEL });
    }
    
    @Override
    public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
        return true;
    }
    
    @Override
    @Deprecated
    public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
        if (face == EnumFacing.UP) {
            return BlockFaceShape.BOWL;
        }
        return (face == EnumFacing.DOWN) ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }
    
    static {
        LEVEL = PropertyInteger.create("level", 0, 3);
        AABB_LEGS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.3125, 1.0);
        AABB_WALL_NORTH = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.125);
        AABB_WALL_SOUTH = new AxisAlignedBB(0.0, 0.0, 0.875, 1.0, 1.0, 1.0);
        AABB_WALL_EAST = new AxisAlignedBB(0.875, 0.0, 0.0, 1.0, 1.0, 1.0);
        AABB_WALL_WEST = new AxisAlignedBB(0.0, 0.0, 0.0, 0.125, 1.0, 1.0);
    }
}
