// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.creativetab.CreativeTabs;

public class ItemPotion extends Item
{
    public ItemPotion() {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.BREWING);
    }
    
    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.WATER);
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving) {
        final EntityPlayer entityplayer = (entityLiving instanceof EntityPlayer) ? ((EntityPlayer)entityLiving) : null;
        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            stack.shrink(1);
        }
        if (entityplayer instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
        }
        if (!worldIn.isRemote) {
            for (final PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack)) {
                if (potioneffect.getPotion().isInstant()) {
                    potioneffect.getPotion().affectEntity(entityplayer, entityplayer, entityLiving, potioneffect.getAmplifier(), 1.0);
                }
                else {
                    entityLiving.addPotionEffect(new PotionEffect(potioneffect));
                }
            }
        }
        if (entityplayer != null) {
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }
        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            if (stack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (entityplayer != null) {
                entityplayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        return stack;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.DRINK;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("potion.effect."));
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0f);
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return super.hasEffect(stack) || !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (final PotionType potiontype : PotionType.REGISTRY) {
                if (potiontype != PotionTypes.EMPTY) {
                    items.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
                }
            }
        }
    }
}
