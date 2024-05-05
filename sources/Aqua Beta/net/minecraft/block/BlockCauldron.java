package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCauldron
extends Block {
    public static final PropertyInteger LEVEL = PropertyInteger.create((String)"level", (int)0, (int)3);

    public BlockCauldron() {
        super(Material.iron, MapColor.stoneColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)LEVEL, (Comparable)Integer.valueOf((int)0)));
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.3125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        float f = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(1.0f - f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 1.0f - f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
    }

    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        int i = (Integer)state.getValue((IProperty)LEVEL);
        float f = (float)pos.getY() + (6.0f + (float)(3 * i)) / 16.0f;
        if (!worldIn.isRemote && entityIn.isBurning() && i > 0 && entityIn.getEntityBoundingBox().minY <= (double)f) {
            entityIn.extinguish();
            this.setWaterLevel(worldIn, pos, state, i - 1);
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemArmor itemarmor;
        if (worldIn.isRemote) {
            return true;
        }
        ItemStack itemstack = playerIn.inventory.getCurrentItem();
        if (itemstack == null) {
            return true;
        }
        int i = (Integer)state.getValue((IProperty)LEVEL);
        Item item = itemstack.getItem();
        if (item == Items.water_bucket) {
            if (i < 3) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
                }
                playerIn.triggerAchievement(StatList.field_181725_I);
                this.setWaterLevel(worldIn, pos, state, 3);
            }
            return true;
        }
        if (item == Items.glass_bottle) {
            if (i > 0) {
                if (!playerIn.capabilities.isCreativeMode) {
                    ItemStack itemstack2 = new ItemStack((Item)Items.potionitem, 1, 0);
                    if (!playerIn.inventory.addItemStackToInventory(itemstack2)) {
                        worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, (double)pos.getX() + 0.5, (double)pos.getY() + 1.5, (double)pos.getZ() + 0.5, itemstack2));
                    } else if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }
                    playerIn.triggerAchievement(StatList.field_181726_J);
                    --itemstack.stackSize;
                    if (itemstack.stackSize <= 0) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
                    }
                }
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        }
        if (i > 0 && item instanceof ItemArmor && (itemarmor = (ItemArmor)item).getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack)) {
            itemarmor.removeColor(itemstack);
            this.setWaterLevel(worldIn, pos, state, i - 1);
            playerIn.triggerAchievement(StatList.field_181727_K);
            return true;
        }
        if (i > 0 && item instanceof ItemBanner && TileEntityBanner.getPatterns((ItemStack)itemstack) > 0) {
            ItemStack itemstack1 = itemstack.copy();
            itemstack1.stackSize = 1;
            TileEntityBanner.removeBannerData((ItemStack)itemstack1);
            if (itemstack.stackSize <= 1 && !playerIn.capabilities.isCreativeMode) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemstack1);
            } else {
                if (!playerIn.inventory.addItemStackToInventory(itemstack1)) {
                    worldIn.spawnEntityInWorld((Entity)new EntityItem(worldIn, (double)pos.getX() + 0.5, (double)pos.getY() + 1.5, (double)pos.getZ() + 0.5, itemstack1));
                } else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }
                playerIn.triggerAchievement(StatList.field_181728_L);
                if (!playerIn.capabilities.isCreativeMode) {
                    --itemstack.stackSize;
                }
            }
            if (!playerIn.capabilities.isCreativeMode) {
                this.setWaterLevel(worldIn, pos, state, i - 1);
            }
            return true;
        }
        return false;
    }

    public void setWaterLevel(World worldIn, BlockPos pos, IBlockState state, int level) {
        worldIn.setBlockState(pos, state.withProperty((IProperty)LEVEL, (Comparable)Integer.valueOf((int)MathHelper.clamp_int((int)level, (int)0, (int)3))), 2);
        worldIn.updateComparatorOutputLevel(pos, (Block)this);
    }

    public void fillWithRain(World worldIn, BlockPos pos) {
        IBlockState iblockstate;
        if (worldIn.rand.nextInt(20) == 1 && (Integer)(iblockstate = worldIn.getBlockState(pos)).getValue((IProperty)LEVEL) < 3) {
            worldIn.setBlockState(pos, iblockstate.cycleProperty((IProperty)LEVEL), 2);
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.cauldron;
    }

    public Item getItem(World worldIn, BlockPos pos) {
        return Items.cauldron;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, BlockPos pos) {
        return (Integer)worldIn.getBlockState(pos).getValue((IProperty)LEVEL);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty((IProperty)LEVEL, (Comparable)Integer.valueOf((int)meta));
    }

    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue((IProperty)LEVEL);
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{LEVEL});
    }
}
