// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import java.util.Random;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemArmor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;

public class BlockCauldron extends Block
{
    public static final PropertyInteger field_176591_a;
    private static final String __OBFID = "CL_00000213";
    
    public BlockCauldron() {
        super(Material.iron);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCauldron.field_176591_a, 0));
    }
    
    @Override
    public void addCollisionBoxesToList(final World worldIn, final BlockPos pos, final IBlockState state, final AxisAlignedBB mask, final List list, final Entity collidingEntity) {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.3125f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        final float var7 = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, var7, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, var7);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(1.0f - var7, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBounds(0.0f, 0.0f, 1.0f - var7, 1.0f, 1.0f, 1.0f);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        this.setBlockBoundsForItemRender();
    }
    
    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void onEntityCollidedWithBlock(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
        final int var5 = (int)state.getValue(BlockCauldron.field_176591_a);
        final float var6 = pos.getY() + (6.0f + 3 * var5) / 16.0f;
        if (!worldIn.isRemote && entityIn.isBurning() && var5 > 0 && entityIn.getEntityBoundingBox().minY <= var6) {
            entityIn.extinguish();
            this.func_176590_a(worldIn, pos, state, var5 - 1);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final ItemStack var9 = playerIn.inventory.getCurrentItem();
        if (var9 == null) {
            return true;
        }
        final int var10 = (int)state.getValue(BlockCauldron.field_176591_a);
        final Item var11 = var9.getItem();
        if (var11 == Items.water_bucket) {
            if (var10 < 3) {
                if (!playerIn.capabilities.isCreativeMode) {
                    playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(Items.bucket));
                }
                this.func_176590_a(worldIn, pos, state, 3);
            }
            return true;
        }
        if (var11 == Items.glass_bottle) {
            if (var10 > 0) {
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack var12 = new ItemStack(Items.potionitem, 1, 0);
                    if (!playerIn.inventory.addItemStackToInventory(var12)) {
                        worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, var12));
                    }
                    else if (playerIn instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                    }
                    final ItemStack itemStack = var9;
                    --itemStack.stackSize;
                    if (var9.stackSize <= 0) {
                        playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
                    }
                }
                this.func_176590_a(worldIn, pos, state, var10 - 1);
            }
            return true;
        }
        if (var10 > 0 && var11 instanceof ItemArmor) {
            final ItemArmor var13 = (ItemArmor)var11;
            if (var13.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && var13.hasColor(var9)) {
                var13.removeColor(var9);
                this.func_176590_a(worldIn, pos, state, var10 - 1);
                return true;
            }
        }
        if (var10 > 0 && var11 instanceof ItemBanner && TileEntityBanner.func_175113_c(var9) > 0) {
            final ItemStack var12 = var9.copy();
            var12.stackSize = 1;
            TileEntityBanner.func_175117_e(var12);
            if (var9.stackSize <= 1 && !playerIn.capabilities.isCreativeMode) {
                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, var12);
            }
            else {
                if (!playerIn.inventory.addItemStackToInventory(var12)) {
                    worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, var12));
                }
                else if (playerIn instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)playerIn).sendContainerToPlayer(playerIn.inventoryContainer);
                }
                if (!playerIn.capabilities.isCreativeMode) {
                    final ItemStack itemStack2 = var9;
                    --itemStack2.stackSize;
                }
            }
            if (!playerIn.capabilities.isCreativeMode) {
                this.func_176590_a(worldIn, pos, state, var10 - 1);
            }
            return true;
        }
        return false;
    }
    
    public void func_176590_a(final World worldIn, final BlockPos p_176590_2_, final IBlockState p_176590_3_, final int p_176590_4_) {
        worldIn.setBlockState(p_176590_2_, p_176590_3_.withProperty(BlockCauldron.field_176591_a, MathHelper.clamp_int(p_176590_4_, 0, 3)), 2);
        worldIn.updateComparatorOutputLevel(p_176590_2_, this);
    }
    
    @Override
    public void fillWithRain(final World worldIn, final BlockPos pos) {
        if (worldIn.rand.nextInt(20) == 1) {
            final IBlockState var3 = worldIn.getBlockState(pos);
            if ((int)var3.getValue(BlockCauldron.field_176591_a) < 3) {
                worldIn.setBlockState(pos, var3.cycleProperty(BlockCauldron.field_176591_a), 2);
            }
        }
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.cauldron;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return Items.cauldron;
    }
    
    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    @Override
    public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
        return (int)worldIn.getBlockState(pos).getValue(BlockCauldron.field_176591_a);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockCauldron.field_176591_a, meta);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return (int)state.getValue(BlockCauldron.field_176591_a);
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockCauldron.field_176591_a });
    }
    
    static {
        field_176591_a = PropertyInteger.create("level", 0, 3);
    }
}
