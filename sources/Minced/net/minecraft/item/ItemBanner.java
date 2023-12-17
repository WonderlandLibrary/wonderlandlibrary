// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.init.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.client.util.ITooltipFlag;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.BannerPattern;
import java.util.List;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

public class ItemBanner extends ItemBlock
{
    public ItemBanner() {
        super(Blocks.STANDING_BANNER);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final boolean flag = iblockstate.getBlock().isReplaceable(worldIn, pos);
        if (facing == EnumFacing.DOWN || (!iblockstate.getMaterial().isSolid() && !flag) || (flag && facing != EnumFacing.UP)) {
            return EnumActionResult.FAIL;
        }
        pos = pos.offset(facing);
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!player.canPlayerEdit(pos, facing, itemstack) || !Blocks.STANDING_BANNER.canPlaceBlockAt(worldIn, pos)) {
            return EnumActionResult.FAIL;
        }
        if (worldIn.isRemote) {
            return EnumActionResult.SUCCESS;
        }
        pos = (flag ? pos.down() : pos);
        if (facing == EnumFacing.UP) {
            final int i = MathHelper.floor((player.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            worldIn.setBlockState(pos, Blocks.STANDING_BANNER.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 3);
        }
        else {
            worldIn.setBlockState(pos, Blocks.WALL_BANNER.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, facing), 3);
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBanner) {
            ((TileEntityBanner)tileentity).setItemValues(itemstack, false);
        }
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemstack);
        }
        itemstack.shrink(1);
        return EnumActionResult.SUCCESS;
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        String s = "item.banner.";
        final EnumDyeColor enumdyecolor = getBaseColor(stack);
        s = s + enumdyecolor.getTranslationKey() + ".name";
        return I18n.translateToLocal(s);
    }
    
    public static void appendHoverTextFromTileEntityTag(final ItemStack stack, final List<String> p_185054_1_) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i) {
                final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound2.getInteger("Color"));
                final BannerPattern bannerpattern = BannerPattern.byHash(nbttagcompound2.getString("Pattern"));
                if (bannerpattern != null) {
                    p_185054_1_.add(I18n.translateToLocal("item.banner." + bannerpattern.getFileName() + "." + enumdyecolor.getTranslationKey()));
                }
            }
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        appendHoverTextFromTileEntityTag(stack, tooltip);
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (final EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
                items.add(makeBanner(enumdyecolor, null));
            }
        }
    }
    
    public static ItemStack makeBanner(final EnumDyeColor color, @Nullable final NBTTagList patterns) {
        final ItemStack itemstack = new ItemStack(Items.BANNER, 1, color.getDyeDamage());
        if (patterns != null && !patterns.isEmpty()) {
            itemstack.getOrCreateSubCompound("BlockEntityTag").setTag("Patterns", patterns.copy());
        }
        return itemstack;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.DECORATIONS;
    }
    
    public static EnumDyeColor getBaseColor(final ItemStack stack) {
        return EnumDyeColor.byDyeDamage(stack.getMetadata() & 0xF);
    }
}
