// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.creativetab.CreativeTabs;

public class ItemShield extends Item
{
    public ItemShield() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.setMaxDamage(336);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                return (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) ? 1.0f : 0.0f;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getSubCompound("BlockEntityTag") != null) {
            final EnumDyeColor enumdyecolor = TileEntityBanner.getColor(stack);
            return I18n.translateToLocal("item.shield." + enumdyecolor.getTranslationKey() + ".name");
        }
        return I18n.translateToLocal("item.shield.name");
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        ItemBanner.appendHoverTextFromTileEntityTag(stack, tooltip);
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.BLOCK;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return repair.getItem() == Item.getItemFromBlock(Blocks.PLANKS) || super.getIsRepairable(toRepair, repair);
    }
}
