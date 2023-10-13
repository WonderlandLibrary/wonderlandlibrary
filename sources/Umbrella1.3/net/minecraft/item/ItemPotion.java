/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion
extends Item {
    private Map effectCache = Maps.newHashMap();
    private static final Map field_77835_b = Maps.newLinkedHashMap();
    private static final String __OBFID = "CL_00000055";

    public ItemPotion() {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    public List getEffects(ItemStack p_77832_1_) {
        if (p_77832_1_.hasTagCompound() && p_77832_1_.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            ArrayList var7 = Lists.newArrayList();
            NBTTagList var3 = p_77832_1_.getTagCompound().getTagList("CustomPotionEffects", 10);
            for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
                NBTTagCompound var5 = var3.getCompoundTagAt(var4);
                PotionEffect var6 = PotionEffect.readCustomPotionEffectFromNBT(var5);
                if (var6 == null) continue;
                var7.add(var6);
            }
            return var7;
        }
        List var2 = (List)this.effectCache.get(p_77832_1_.getMetadata());
        if (var2 == null) {
            var2 = PotionHelper.getPotionEffects(p_77832_1_.getMetadata(), false);
            this.effectCache.put(p_77832_1_.getMetadata(), var2);
        }
        return var2;
    }

    public List getEffects(int p_77834_1_) {
        List var2 = (List)this.effectCache.get(p_77834_1_);
        if (var2 == null) {
            var2 = PotionHelper.getPotionEffects(p_77834_1_, false);
            this.effectCache.put(p_77834_1_, var2);
        }
        return var2;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        List var4;
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        if (!worldIn.isRemote && (var4 = this.getEffects(stack)) != null) {
            for (PotionEffect var6 : var4) {
                playerIn.addPotionEffect(new PotionEffect(var6));
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
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (ItemPotion.isSplash(itemStackIn.getMetadata())) {
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld(new EntityPotion(worldIn, (EntityLivingBase)playerIn, itemStackIn));
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return itemStackIn;
        }
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

    public static boolean isSplash(int p_77831_0_) {
        return (p_77831_0_ & 0x4000) != 0;
    }

    public int getColorFromDamage(int p_77620_1_) {
        return PotionHelper.func_77915_a(p_77620_1_, false);
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return renderPass > 0 ? 0xFFFFFF : this.getColorFromDamage(stack.getMetadata());
    }

    public boolean isEffectInstant(int p_77833_1_) {
        List var2 = this.getEffects(p_77833_1_);
        if (var2 != null && !var2.isEmpty()) {
            PotionEffect var4;
            Iterator var3 = var2.iterator();
            do {
                if (var3.hasNext()) continue;
                return false;
            } while (!Potion.potionTypes[(var4 = (PotionEffect)var3.next()).getPotionID()].isInstant());
            return true;
        }
        return false;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        List var3;
        if (stack.getMetadata() == 0) {
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
        }
        String var2 = "";
        if (ItemPotion.isSplash(stack.getMetadata())) {
            var2 = String.valueOf(StatCollector.translateToLocal("potion.prefix.grenade").trim()) + " ";
        }
        if ((var3 = Items.potionitem.getEffects(stack)) != null && !var3.isEmpty()) {
            String var4 = ((PotionEffect)var3.get(0)).getEffectName();
            var4 = String.valueOf(var4) + ".postfix";
            return String.valueOf(var2) + StatCollector.translateToLocal(var4).trim();
        }
        String var4 = PotionHelper.func_77905_c(stack.getMetadata());
        return String.valueOf(StatCollector.translateToLocal(var4).trim()) + " " + super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        if (stack.getMetadata() != 0) {
            List var5 = Items.potionitem.getEffects(stack);
            HashMultimap var6 = HashMultimap.create();
            if (var5 != null && !var5.isEmpty()) {
                for (PotionEffect var8 : var5) {
                    String var9 = StatCollector.translateToLocal(var8.getEffectName()).trim();
                    Potion var10 = Potion.potionTypes[var8.getPotionID()];
                    Map var11 = var10.func_111186_k();
                    if (var11 != null && var11.size() > 0) {
                        for (Map.Entry var13 : var11.entrySet()) {
                            AttributeModifier var14 = (AttributeModifier)var13.getValue();
                            AttributeModifier var15 = new AttributeModifier(var14.getName(), var10.func_111183_a(var8.getAmplifier(), var14), var14.getOperation());
                            var6.put((Object)((IAttribute)var13.getKey()).getAttributeUnlocalizedName(), (Object)var15);
                        }
                    }
                    if (var8.getAmplifier() > 0) {
                        var9 = String.valueOf(var9) + " " + StatCollector.translateToLocal("potion.potency." + var8.getAmplifier()).trim();
                    }
                    if (var8.getDuration() > 20) {
                        var9 = String.valueOf(var9) + " (" + Potion.getDurationString(var8) + ")";
                    }
                    if (var10.isBadEffect()) {
                        tooltip.add((Object)((Object)EnumChatFormatting.RED) + var9);
                        continue;
                    }
                    tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + var9);
                }
            } else {
                String var7 = StatCollector.translateToLocal("potion.empty").trim();
                tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + var7);
            }
            if (!var6.isEmpty()) {
                tooltip.add("");
                tooltip.add((Object)((Object)EnumChatFormatting.DARK_PURPLE) + StatCollector.translateToLocal("potion.effects.whenDrank"));
                for (Map.Entry var17 : var6.entries()) {
                    AttributeModifier var18 = (AttributeModifier)var17.getValue();
                    double var19 = var18.getAmount();
                    double var20 = var18.getOperation() != 1 && var18.getOperation() != 2 ? var18.getAmount() : var18.getAmount() * 100.0;
                    if (var19 > 0.0) {
                        tooltip.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())));
                        continue;
                    }
                    if (!(var19 < 0.0)) continue;
                    tooltip.add((Object)((Object)EnumChatFormatting.RED) + StatCollector.translateToLocalFormatted("attribute.modifier.take." + var18.getOperation(), ItemStack.DECIMALFORMAT.format(var20 *= -1.0), StatCollector.translateToLocal("attribute.name." + (String)var17.getKey())));
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        List var2 = this.getEffects(stack);
        return var2 != null && !var2.isEmpty();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        int var5;
        super.getSubItems(itemIn, tab, subItems);
        if (field_77835_b.isEmpty()) {
            for (int var4 = 0; var4 <= 15; ++var4) {
                for (var5 = 0; var5 <= 1; ++var5) {
                    int var6 = var5 == 0 ? var4 | 0x2000 : var4 | 0x4000;
                    for (int var7 = 0; var7 <= 2; ++var7) {
                        List var9;
                        int var8 = var6;
                        if (var7 != 0) {
                            if (var7 == 1) {
                                var8 = var6 | 0x20;
                            } else if (var7 == 2) {
                                var8 = var6 | 0x40;
                            }
                        }
                        if ((var9 = PotionHelper.getPotionEffects(var8, false)) == null || var9.isEmpty()) continue;
                        field_77835_b.put(var9, var8);
                    }
                }
            }
        }
        Iterator var10 = field_77835_b.values().iterator();
        while (var10.hasNext()) {
            var5 = (Integer)var10.next();
            subItems.add(new ItemStack(itemIn, 1, var5));
        }
    }
}

