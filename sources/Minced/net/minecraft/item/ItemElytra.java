// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.creativetab.CreativeTabs;

public class ItemElytra extends Item
{
    public ItemElytra() {
        this.maxStackSize = 1;
        this.setMaxDamage(432);
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.addPropertyOverride(new ResourceLocation("broken"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                return ItemElytra.isUsable(stack) ? 0.0f : 1.0f;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }
    
    public static boolean isUsable(final ItemStack stack) {
        return stack.getItemDamage() < stack.getMaxDamage() - 1;
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return repair.getItem() == Items.LEATHER;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        final ItemStack itemstack2 = playerIn.getItemStackFromSlot(entityequipmentslot);
        if (itemstack2.isEmpty()) {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }
}
