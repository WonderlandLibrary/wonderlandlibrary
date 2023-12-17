// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.util.Tuple;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import java.util.Map;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.nbt.NBTBase;
import com.google.common.base.MoreObjects;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import net.optifine.CustomColors;
import net.minecraft.src.Config;
import net.minecraft.init.PotionTypes;
import net.minecraft.nbt.NBTTagList;
import javax.annotation.Nullable;
import net.minecraft.nbt.NBTTagCompound;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.item.ItemStack;

public class PotionUtils
{
    public static List<PotionEffect> getEffectsFromStack(final ItemStack stack) {
        return getEffectsFromTag(stack.getTagCompound());
    }
    
    public static List<PotionEffect> mergeEffects(final PotionType potionIn, final Collection<PotionEffect> effects) {
        final List<PotionEffect> list = (List<PotionEffect>)Lists.newArrayList();
        list.addAll(potionIn.getEffects());
        list.addAll(effects);
        return list;
    }
    
    public static List<PotionEffect> getEffectsFromTag(@Nullable final NBTTagCompound tag) {
        final List<PotionEffect> list = (List<PotionEffect>)Lists.newArrayList();
        list.addAll(getPotionTypeFromNBT(tag).getEffects());
        addCustomPotionEffectToList(tag, list);
        return list;
    }
    
    public static List<PotionEffect> getFullEffectsFromItem(final ItemStack itemIn) {
        return getFullEffectsFromTag(itemIn.getTagCompound());
    }
    
    public static List<PotionEffect> getFullEffectsFromTag(@Nullable final NBTTagCompound tag) {
        final List<PotionEffect> list = (List<PotionEffect>)Lists.newArrayList();
        addCustomPotionEffectToList(tag, list);
        return list;
    }
    
    public static void addCustomPotionEffectToList(@Nullable final NBTTagCompound tag, final List<PotionEffect> effectList) {
        if (tag != null && tag.hasKey("CustomPotionEffects", 9)) {
            final NBTTagList nbttaglist = tag.getTagList("CustomPotionEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                final PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                if (potioneffect != null) {
                    effectList.add(potioneffect);
                }
            }
        }
    }
    
    public static int getColor(final ItemStack p_190932_0_) {
        final NBTTagCompound nbttagcompound = p_190932_0_.getTagCompound();
        if (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99)) {
            return nbttagcompound.getInteger("CustomPotionColor");
        }
        return (getPotionFromItem(p_190932_0_) == PotionTypes.EMPTY) ? 16253176 : getPotionColorFromEffectList(getEffectsFromStack(p_190932_0_));
    }
    
    public static int getPotionColor(final PotionType potionIn) {
        return (potionIn == PotionTypes.EMPTY) ? 16253176 : getPotionColorFromEffectList(potionIn.getEffects());
    }
    
    public static int getPotionColorFromEffectList(final Collection<PotionEffect> effects) {
        final int i = 3694022;
        if (effects.isEmpty()) {
            return Config.isCustomColors() ? CustomColors.getPotionColor(null, i) : 3694022;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int j = 0;
        for (final PotionEffect potioneffect : effects) {
            if (potioneffect.doesShowParticles()) {
                int k = potioneffect.getPotion().getLiquidColor();
                if (Config.isCustomColors()) {
                    k = CustomColors.getPotionColor(potioneffect.getPotion(), k);
                }
                final int l = potioneffect.getAmplifier() + 1;
                f += l * (k >> 16 & 0xFF) / 255.0f;
                f2 += l * (k >> 8 & 0xFF) / 255.0f;
                f3 += l * (k >> 0 & 0xFF) / 255.0f;
                j += l;
            }
        }
        if (j == 0) {
            return 0;
        }
        f = f / j * 255.0f;
        f2 = f2 / j * 255.0f;
        f3 = f3 / j * 255.0f;
        return (int)f << 16 | (int)f2 << 8 | (int)f3;
    }
    
    public static PotionType getPotionFromItem(final ItemStack itemIn) {
        return getPotionTypeFromNBT(itemIn.getTagCompound());
    }
    
    public static PotionType getPotionTypeFromNBT(@Nullable final NBTTagCompound tag) {
        return (tag == null) ? PotionTypes.EMPTY : PotionType.getPotionTypeForName(tag.getString("Potion"));
    }
    
    public static ItemStack addPotionToItemStack(final ItemStack itemIn, final PotionType potionIn) {
        final ResourceLocation resourcelocation = PotionType.REGISTRY.getNameForObject(potionIn);
        if (potionIn == PotionTypes.EMPTY) {
            if (itemIn.hasTagCompound()) {
                final NBTTagCompound nbttagcompound = itemIn.getTagCompound();
                nbttagcompound.removeTag("Potion");
                if (nbttagcompound.isEmpty()) {
                    itemIn.setTagCompound(null);
                }
            }
        }
        else {
            final NBTTagCompound nbttagcompound2 = itemIn.hasTagCompound() ? itemIn.getTagCompound() : new NBTTagCompound();
            nbttagcompound2.setString("Potion", resourcelocation.toString());
            itemIn.setTagCompound(nbttagcompound2);
        }
        return itemIn;
    }
    
    public static ItemStack appendEffects(final ItemStack itemIn, final Collection<PotionEffect> effects) {
        if (effects.isEmpty()) {
            return itemIn;
        }
        final NBTTagCompound nbttagcompound = (NBTTagCompound)MoreObjects.firstNonNull((Object)itemIn.getTagCompound(), (Object)new NBTTagCompound());
        final NBTTagList nbttaglist = nbttagcompound.getTagList("CustomPotionEffects", 9);
        for (final PotionEffect potioneffect : effects) {
            nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
        }
        nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
        itemIn.setTagCompound(nbttagcompound);
        return itemIn;
    }
    
    public static void addPotionTooltip(final ItemStack itemIn, final List<String> lores, final float durationFactor) {
        final List<PotionEffect> list = getEffectsFromStack(itemIn);
        final List<Tuple<String, AttributeModifier>> list2 = (List<Tuple<String, AttributeModifier>>)Lists.newArrayList();
        if (list.isEmpty()) {
            final String s = I18n.translateToLocal("effect.none").trim();
            lores.add(TextFormatting.GRAY + s);
        }
        else {
            for (final PotionEffect potioneffect : list) {
                String s2 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
                final Potion potion = potioneffect.getPotion();
                final Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
                if (!map.isEmpty()) {
                    for (final Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                        final AttributeModifier attributemodifier = entry.getValue();
                        final AttributeModifier attributemodifier2 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list2.add(new Tuple<String, AttributeModifier>(entry.getKey().getName(), attributemodifier2));
                    }
                }
                if (potioneffect.getAmplifier() > 0) {
                    s2 = s2 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }
                if (potioneffect.getDuration() > 20) {
                    s2 = s2 + " (" + Potion.getPotionDurationString(potioneffect, durationFactor) + ")";
                }
                if (potion.isBadEffect()) {
                    lores.add(TextFormatting.RED + s2);
                }
                else {
                    lores.add(TextFormatting.BLUE + s2);
                }
            }
        }
        if (!list2.isEmpty()) {
            lores.add("");
            lores.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));
            for (final Tuple<String, AttributeModifier> tuple : list2) {
                final AttributeModifier attributemodifier3 = tuple.getSecond();
                final double d0 = attributemodifier3.getAmount();
                double d2;
                if (attributemodifier3.getOperation() != 1 && attributemodifier3.getOperation() != 2) {
                    d2 = attributemodifier3.getAmount();
                }
                else {
                    d2 = attributemodifier3.getAmount() * 100.0;
                }
                if (d0 > 0.0) {
                    lores.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier3.getOperation(), ItemStack.DECIMALFORMAT.format(d2), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
                else {
                    if (d0 >= 0.0) {
                        continue;
                    }
                    d2 *= -1.0;
                    lores.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier3.getOperation(), ItemStack.DECIMALFORMAT.format(d2), I18n.translateToLocal("attribute.name." + tuple.getFirst())));
                }
            }
        }
    }
}
