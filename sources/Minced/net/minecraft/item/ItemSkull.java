// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.NonNullList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSkull extends Item
{
    private static final String[] SKULL_TYPES;
    
    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        if (facing == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(worldIn, pos);
        if (!flag) {
            if (!worldIn.getBlockState(pos).getMaterial().isSolid()) {
                return EnumActionResult.FAIL;
            }
            pos = pos.offset(facing);
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, itemstack) || !Blocks.SKULL.canPlaceBlockAt(worldIn, pos)) {
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        worldIn.setBlockState(pos, Blocks.SKULL.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, facing), 11);
        int i = 0;
        if (facing == EnumFacing.UP) {
            i = (MathHelper.floor(player.rotationYaw * 16.0f / 360.0f + 0.5) & 0xF);
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntitySkull) {
            final TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
            if (itemstack.getMetadata() == 3) {
                GameProfile gameprofile = null;
                if (itemstack.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                    }
                    else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)nbttagcompound.getString("SkullOwner"))) {
                        gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                    }
                }
                tileentityskull.setPlayerProfile(gameprofile);
            }
            else {
                tileentityskull.setType(itemstack.getMetadata());
            }
            tileentityskull.setSkullRotation(i);
            Blocks.SKULL.checkWitherSpawn(worldIn, pos, tileentityskull);
        }
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < ItemSkull.SKULL_TYPES.length; ++i) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        int i = stack.getMetadata();
        if (i < 0 || i >= ItemSkull.SKULL_TYPES.length) {
            i = 0;
        }
        return super.getTranslationKey() + "." + ItemSkull.SKULL_TYPES[i];
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("SkullOwner", 8)) {
                return I18n.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }
            if (stack.getTagCompound().hasKey("SkullOwner", 10)) {
                final NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");
                if (nbttagcompound.hasKey("Name", 8)) {
                    return I18n.translateToLocalFormatted("item.skull.player.name", nbttagcompound.getString("Name"));
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public boolean updateItemStackNBT(final NBTTagCompound nbt) {
        super.updateItemStackNBT(nbt);
        if (nbt.hasKey("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)nbt.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameProfile(gameprofile);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        return false;
    }
    
    static {
        SKULL_TYPES = new String[] { "skeleton", "wither", "zombie", "char", "creeper", "dragon" };
    }
}
