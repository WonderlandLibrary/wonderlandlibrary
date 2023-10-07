// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.HashMultimap;
import net.minecraft.util.StatCollector;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.Entity;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import java.util.ArrayList;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionEffect;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import com.google.common.collect.Maps;
import java.util.Map;

public class ItemPotion extends Item
{
    private Map effectCache;
    private static final Map field_77835_b;
    private static final String __OBFID = "CL_00000055";
    
    public ItemPotion() {
        this.effectCache = Maps.newHashMap();
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }
    
    public List getEffects(final ItemStack p_77832_1_) {
        if (p_77832_1_.hasTagCompound() && p_77832_1_.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            final ArrayList var7 = Lists.newArrayList();
            final NBTTagList var8 = p_77832_1_.getTagCompound().getTagList("CustomPotionEffects", 10);
            for (int var9 = 0; var9 < var8.tagCount(); ++var9) {
                final NBTTagCompound var10 = var8.getCompoundTagAt(var9);
                final PotionEffect var11 = PotionEffect.readCustomPotionEffectFromNBT(var10);
                if (var11 != null) {
                    var7.add(var11);
                }
            }
            return var7;
        }
        List var12 = this.effectCache.get(p_77832_1_.getMetadata());
        if (var12 == null) {
            var12 = PotionHelper.getPotionEffects(p_77832_1_.getMetadata(), false);
            this.effectCache.put(p_77832_1_.getMetadata(), var12);
        }
        return var12;
    }
    
    public List getEffects(final int p_77834_1_) {
        List var2 = this.effectCache.get(p_77834_1_);
        if (var2 == null) {
            var2 = PotionHelper.getPotionEffects(p_77834_1_, false);
            this.effectCache.put(p_77834_1_, var2);
        }
        return var2;
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        if (!worldIn.isRemote) {
            final List var4 = this.getEffects(stack);
            if (var4 != null) {
                for (final PotionEffect var6 : var4) {
                    playerIn.addPotionEffect(new PotionEffect(var6));
                }
            }
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        if (!playerIn.capabilities.isCreativeMode) {
            if (stack.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }
            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
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
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (isSplash(itemStackIn.getMetadata())) {
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (Item.itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(new EntityPotion(worldIn, playerIn, itemStackIn));
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return itemStackIn;
        }
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
    
    public static boolean isSplash(final int p_77831_0_) {
        return (p_77831_0_ & 0x4000) != 0x0;
    }
    
    public int getColorFromDamage(final int p_77620_1_) {
        return PotionHelper.func_77915_a(p_77620_1_, false);
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return (renderPass > 0) ? 16777215 : this.getColorFromDamage(stack.getMetadata());
    }
    
    public boolean isEffectInstant(final int p_77833_1_) {
        final List var2 = this.getEffects(p_77833_1_);
        if (var2 != null && !var2.isEmpty()) {
            for (final PotionEffect var4 : var2) {
                if (Potion.potionTypes[var4.getPotionID()].isInstant()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 0) {
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
        }
        String var2 = "";
        if (isSplash(stack.getMetadata())) {
            var2 = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " ";
        }
        final List var3 = Items.potionitem.getEffects(stack);
        if (var3 != null && !var3.isEmpty()) {
            String var4 = var3.get(0).getEffectName();
            var4 += ".postfix";
            return var2 + StatCollector.translateToLocal(var4).trim();
        }
        String var4 = PotionHelper.func_77905_c(stack.getMetadata());
        return StatCollector.translateToLocal(var4).trim() + " " + super.getItemStackDisplayName(stack);
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        if (stack.getMetadata() != 0) {
            final List var5 = Items.potionitem.getEffects(stack);
            final HashMultimap var6 = HashMultimap.create();
            if (var5 != null && !var5.isEmpty()) {
                for (final PotionEffect var8 : var5) {
                    String var9 = StatCollector.translateToLocal(var8.getEffectName()).trim();
                    final Potion var10 = Potion.potionTypes[var8.getPotionID()];
                    final Map var11 = var10.func_111186_k();
                    if (var11 != null && var11.size() > 0) {
                        for (final Map.Entry var13 : var11.entrySet()) {
                            final AttributeModifier var14 = var13.getValue();
                            final AttributeModifier var15 = new AttributeModifier(var14.getName(), var10.func_111183_a(var8.getAmplifier(), var14), var14.getOperation());
                            var6.put((Object)var13.getKey().getAttributeUnlocalizedName(), (Object)var15);
                        }
                    }
                    if (var8.getAmplifier() > 0) {
                        var9 = var9 + " " + StatCollector.translateToLocal("potion.potency." + var8.getAmplifier()).trim();
                    }
                    if (var8.getDuration() > 20) {
                        var9 = var9 + " (" + Potion.getDurationString(var8) + ")";
                    }
                    if (var10.isBadEffect()) {
                        tooltip.add(EnumChatFormatting.RED + var9);
                    }
                    else {
                        tooltip.add(EnumChatFormatting.GRAY + var9);
                    }
                }
            }
            else {
                final String var16 = StatCollector.translateToLocal("potion.empty").trim();
                tooltip.add(EnumChatFormatting.GRAY + var16);
            }
            if (!var6.isEmpty()) {
                tooltip.add("");
                tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
                for (final Map.Entry var17 : var6.entries()) {
                    final AttributeModifier var18 = var17.getValue();
                    final double var19 = var18.getAmount();
                    double var20;
                    if (var18.getOperation() != 1 && var18.getOperation() != 2) {
                        var20 = var18.getAmount();
                    }
                    else {
                        var20 = var18.getAmount() * 100.0;
                    }
                    if (var19 > 0.0) {
                        tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + var17.getKey())));
                    }
                    else {
                        if (var19 >= 0.0) {
                            continue;
                        }
                        var20 *= -1.0;
                        tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + var17.getKey())));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        final List var2 = this.getEffects(stack);
        return var2 != null && !var2.isEmpty();
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List subItems) {
        super.getSubItems(itemIn, tab, subItems);
        if (ItemPotion.field_77835_b.isEmpty()) {
            for (int var4 = 0; var4 <= 15; ++var4) {
                for (int var5 = 0; var5 <= 1; ++var5) {
                    int var6;
                    if (var5 == 0) {
                        var6 = (var4 | 0x2000);
                    }
                    else {
                        var6 = (var4 | 0x4000);
                    }
                    for (int var7 = 0; var7 <= 2; ++var7) {
                        int var8 = var6;
                        if (var7 != 0) {
                            if (var7 == 1) {
                                var8 = (var6 | 0x20);
                            }
                            else if (var7 == 2) {
                                var8 = (var6 | 0x40);
                            }
                        }
                        final List var9 = PotionHelper.getPotionEffects(var8, false);
                        if (var9 != null && !var9.isEmpty()) {
                            ItemPotion.field_77835_b.put(var9, var8);
                        }
                    }
                }
            }
        }
        for (final int var5 : ItemPotion.field_77835_b.values()) {
            subItems.add(new ItemStack(itemIn, 1, var5));
        }
    }
    
    static {
        field_77835_b = Maps.newLinkedHashMap();
    }
}
