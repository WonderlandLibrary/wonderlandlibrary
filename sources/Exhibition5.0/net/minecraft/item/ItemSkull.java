// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSkull extends Item
{
    private static final String[] skullTypes;
    private static final String __OBFID = "CL_00000067";
    
    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        final IBlockState var9 = worldIn.getBlockState(pos);
        final Block var10 = var9.getBlock();
        final boolean var11 = var10.isReplaceable(worldIn, pos);
        if (!var11) {
            if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) {
                return false;
            }
            pos = pos.offset(side);
        }
        if (!playerIn.func_175151_a(pos, side, stack)) {
            return false;
        }
        if (!Blocks.skull.canPlaceBlockAt(worldIn, pos)) {
            return false;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, Blocks.skull.getDefaultState().withProperty(BlockSkull.field_176418_a, side), 3);
            int var12 = 0;
            if (side == EnumFacing.UP) {
                var12 = (MathHelper.floor_double(playerIn.rotationYaw * 16.0f / 360.0f + 0.5) & 0xF);
            }
            final TileEntity var13 = worldIn.getTileEntity(pos);
            if (var13 instanceof TileEntitySkull) {
                final TileEntitySkull var14 = (TileEntitySkull)var13;
                if (stack.getMetadata() == 3) {
                    GameProfile var15 = null;
                    if (stack.hasTagCompound()) {
                        final NBTTagCompound var16 = stack.getTagCompound();
                        if (var16.hasKey("SkullOwner", 10)) {
                            var15 = NBTUtil.readGameProfileFromNBT(var16.getCompoundTag("SkullOwner"));
                        }
                        else if (var16.hasKey("SkullOwner", 8) && var16.getString("SkullOwner").length() > 0) {
                            var15 = new GameProfile((UUID)null, var16.getString("SkullOwner"));
                        }
                    }
                    var14.setPlayerProfile(var15);
                }
                else {
                    var14.setType(stack.getMetadata());
                }
                var14.setSkullRotation(var12);
                Blocks.skull.func_180679_a(worldIn, pos, var14);
            }
            --stack.stackSize;
        }
        return true;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
        for (int var4 = 0; var4 < ItemSkull.skullTypes.length; ++var4) {
            subItems.add(new ItemStack(itemIn, 1, var4));
        }
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        int var2 = stack.getMetadata();
        if (var2 < 0 || var2 >= ItemSkull.skullTypes.length) {
            var2 = 0;
        }
        return super.getUnlocalizedName() + "." + ItemSkull.skullTypes[var2];
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("SkullOwner", 8)) {
                return StatCollector.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }
            if (stack.getTagCompound().hasKey("SkullOwner", 10)) {
                final NBTTagCompound var2 = stack.getTagCompound().getCompoundTag("SkullOwner");
                if (var2.hasKey("Name", 8)) {
                    return StatCollector.translateToLocalFormatted("item.skull.player.name", var2.getString("Name"));
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public boolean updateItemStackNBT(final NBTTagCompound nbt) {
        super.updateItemStackNBT(nbt);
        if (nbt.hasKey("SkullOwner", 8) && nbt.getString("SkullOwner").length() > 0) {
            GameProfile var2 = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
            var2 = TileEntitySkull.updateGameprofile(var2);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var2));
            return true;
        }
        return false;
    }
    
    static {
        skullTypes = new String[] { "skeleton", "wither", "zombie", "char", "creeper" };
    }
}
