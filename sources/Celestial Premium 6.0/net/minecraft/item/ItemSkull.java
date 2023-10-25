/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class ItemSkull
extends Item {
    private static final String[] SKULL_TYPES = new String[]{"skeleton", "wither", "zombie", "char", "creeper", "dragon"};

    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY) {
        ItemStack itemstack;
        if (hand == EnumFacing.DOWN) {
            return EnumActionResult.FAIL;
        }
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        Block block = iblockstate.getBlock();
        boolean flag = block.isReplaceable(playerIn, worldIn);
        if (!flag) {
            if (!playerIn.getBlockState(worldIn).getMaterial().isSolid()) {
                return EnumActionResult.FAIL;
            }
            worldIn = worldIn.offset(hand);
        }
        if (stack.canPlayerEdit(worldIn, hand, itemstack = stack.getHeldItem(pos)) && Blocks.SKULL.canPlaceBlockAt(playerIn, worldIn)) {
            TileEntity tileentity;
            if (playerIn.isRemote) {
                return EnumActionResult.SUCCESS;
            }
            playerIn.setBlockState(worldIn, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, hand), 11);
            int i = 0;
            if (hand == EnumFacing.UP) {
                i = MathHelper.floor((double)(stack.rotationYaw * 16.0f / 360.0f) + 0.5) & 0xF;
            }
            if ((tileentity = playerIn.getTileEntity(worldIn)) instanceof TileEntitySkull) {
                TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                if (itemstack.getMetadata() == 3) {
                    GameProfile gameprofile = null;
                    if (itemstack.hasTagCompound()) {
                        NBTTagCompound nbttagcompound = itemstack.getTagCompound();
                        if (nbttagcompound.hasKey("SkullOwner", 10)) {
                            gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                        } else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner"))) {
                            gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
                        }
                    }
                    tileentityskull.setPlayerProfile(gameprofile);
                } else {
                    tileentityskull.setType(itemstack.getMetadata());
                }
                tileentityskull.setSkullRotation(i);
                Blocks.SKULL.checkWitherSpawn(playerIn, worldIn, tileentityskull);
            }
            if (stack instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
            }
            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (int i = 0; i < SKULL_TYPES.length; ++i) {
                tab.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = stack.getMetadata();
        if (i < 0 || i >= SKULL_TYPES.length) {
            i = 0;
        }
        return super.getUnlocalizedName() + "." + SKULL_TYPES[i];
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound;
            if (stack.getTagCompound().hasKey("SkullOwner", 8)) {
                return I18n.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }
            if (stack.getTagCompound().hasKey("SkullOwner", 10) && (nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner")).hasKey("Name", 8)) {
                return I18n.translateToLocalFormatted("item.skull.player.name", nbttagcompound.getString("Name"));
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        super.updateItemStackNBT(nbt);
        if (nbt.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbt.getString("SkullOwner"))) {
            GameProfile gameprofile = new GameProfile(null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        return false;
    }
}

