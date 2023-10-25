/*
 * Decompiled with CFR 0.152.
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
    private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE = Maps.newLinkedHashMap();
    private Map<Integer, List<PotionEffect>> effectCache = Maps.newHashMap();

    public static boolean isSplash(int n) {
        return (n & 0x4000) != 0;
    }

    public boolean isEffectInstant(int n) {
        List<PotionEffect> list = this.getEffects(n);
        if (list != null && !list.isEmpty()) {
            for (PotionEffect potionEffect : list) {
                if (!Potion.potionTypes[potionEffect.getPotionID()].isInstant()) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    public List<PotionEffect> getEffects(int n) {
        List<PotionEffect> list = this.effectCache.get(n);
        if (list == null) {
            list = PotionHelper.getPotionEffects(n, false);
            this.effectCache.put(n, list);
        }
        return list;
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        List<PotionEffect> list;
        if (itemStack.getMetadata() == 0) {
            return StatCollector.translateToLocal("item.emptyPotion.name").trim();
        }
        String string = "";
        if (ItemPotion.isSplash(itemStack.getMetadata())) {
            string = String.valueOf(StatCollector.translateToLocal("potion.prefix.grenade").trim()) + " ";
        }
        if ((list = Items.potionitem.getEffects(itemStack)) != null && !list.isEmpty()) {
            String string2 = list.get(0).getEffectName();
            string2 = String.valueOf(string2) + ".postfix";
            return String.valueOf(string) + StatCollector.translateToLocal(string2).trim();
        }
        String string3 = PotionHelper.getPotionPrefix(itemStack.getMetadata());
        return String.valueOf(StatCollector.translateToLocal(string3).trim()) + " " + super.getItemStackDisplayName(itemStack);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        if (itemStack.getMetadata() != 0) {
            Object object;
            List<PotionEffect> list2 = Items.potionitem.getEffects(itemStack);
            HashMultimap hashMultimap = HashMultimap.create();
            if (list2 != null && !list2.isEmpty()) {
                for (PotionEffect object2 : list2) {
                    object = StatCollector.translateToLocal(object2.getEffectName()).trim();
                    Potion potion = Potion.potionTypes[object2.getPotionID()];
                    Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
                    if (map != null && map.size() > 0) {
                        for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
                            AttributeModifier attributeModifier = entry.getValue();
                            AttributeModifier attributeModifier2 = new AttributeModifier(attributeModifier.getName(), potion.getAttributeModifierAmount(object2.getAmplifier(), attributeModifier), attributeModifier.getOperation());
                            hashMultimap.put((Object)entry.getKey().getAttributeUnlocalizedName(), (Object)attributeModifier2);
                        }
                    }
                    if (object2.getAmplifier() > 0) {
                        object = String.valueOf(object) + " " + StatCollector.translateToLocal("potion.potency." + object2.getAmplifier()).trim();
                    }
                    if (object2.getDuration() > 20) {
                        object = String.valueOf(object) + " (" + Potion.getDurationString(object2) + ")";
                    }
                    if (potion.isBadEffect()) {
                        list.add((Object)((Object)EnumChatFormatting.RED) + (String)object);
                        continue;
                    }
                    list.add((Object)((Object)EnumChatFormatting.GRAY) + (String)object);
                }
            } else {
                String string = StatCollector.translateToLocal("potion.empty").trim();
                list.add((Object)((Object)EnumChatFormatting.GRAY) + string);
            }
            if (!hashMultimap.isEmpty()) {
                list.add("");
                list.add((Object)((Object)EnumChatFormatting.DARK_PURPLE) + StatCollector.translateToLocal("potion.effects.whenDrank"));
                for (Map.Entry entry : hashMultimap.entries()) {
                    object = (AttributeModifier)entry.getValue();
                    double d = ((AttributeModifier)object).getAmount();
                    double d2 = ((AttributeModifier)object).getOperation() != 1 && ((AttributeModifier)object).getOperation() != 2 ? ((AttributeModifier)object).getAmount() : ((AttributeModifier)object).getAmount() * 100.0;
                    if (d > 0.0) {
                        list.add((Object)((Object)EnumChatFormatting.BLUE) + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + ((AttributeModifier)object).getOperation(), ItemStack.DECIMALFORMAT.format(d2), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey())));
                        continue;
                    }
                    if (!(d < 0.0)) continue;
                    list.add((Object)((Object)EnumChatFormatting.RED) + StatCollector.translateToLocalFormatted("attribute.modifier.take." + ((AttributeModifier)object).getOperation(), ItemStack.DECIMALFORMAT.format(d2 *= -1.0), StatCollector.translateToLocal("attribute.name." + (String)entry.getKey())));
                }
            }
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List<ItemStack> list) {
        super.getSubItems(item, creativeTabs, list);
        if (SUB_ITEMS_CACHE.isEmpty()) {
            int n = 0;
            while (n <= 15) {
                int n2 = 0;
                while (n2 <= 1) {
                    int n3 = n2 == 0 ? n | 0x2000 : n | 0x4000;
                    int n4 = 0;
                    while (n4 <= 2) {
                        List<PotionEffect> list2;
                        int n5 = n3;
                        if (n4 != 0) {
                            if (n4 == 1) {
                                n5 = n3 | 0x20;
                            } else if (n4 == 2) {
                                n5 = n3 | 0x40;
                            }
                        }
                        if ((list2 = PotionHelper.getPotionEffects(n5, false)) != null && !list2.isEmpty()) {
                            SUB_ITEMS_CACHE.put(list2, n5);
                        }
                        ++n4;
                    }
                    ++n2;
                }
                ++n;
            }
        }
        for (int n2 : SUB_ITEMS_CACHE.values()) {
            list.add(new ItemStack(item, 1, n2));
        }
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        List<PotionEffect> list;
        if (!entityPlayer.capabilities.isCreativeMode) {
            --itemStack.stackSize;
        }
        if (!world.isRemote && (list = this.getEffects(itemStack)) != null) {
            for (PotionEffect potionEffect : list) {
                entityPlayer.addPotionEffect(new PotionEffect(potionEffect));
            }
        }
        entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        if (!entityPlayer.capabilities.isCreativeMode) {
            if (itemStack.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }
            entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }
        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    public ItemPotion() {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        List<PotionEffect> list = this.getEffects(itemStack);
        return list != null && !list.isEmpty();
    }

    public List<PotionEffect> getEffects(ItemStack itemStack) {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            ArrayList arrayList = Lists.newArrayList();
            NBTTagList nBTTagList = itemStack.getTagCompound().getTagList("CustomPotionEffects", 10);
            int n = 0;
            while (n < nBTTagList.tagCount()) {
                NBTTagCompound nBTTagCompound = nBTTagList.getCompoundTagAt(n);
                PotionEffect potionEffect = PotionEffect.readCustomPotionEffectFromNBT(nBTTagCompound);
                if (potionEffect != null) {
                    arrayList.add(potionEffect);
                }
                ++n;
            }
            return arrayList;
        }
        List<PotionEffect> list = this.effectCache.get(itemStack.getMetadata());
        if (list == null) {
            list = PotionHelper.getPotionEffects(itemStack.getMetadata(), false);
            this.effectCache.put(itemStack.getMetadata(), list);
        }
        return list;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (ItemPotion.isSplash(itemStack.getMetadata())) {
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            world.playSoundAtEntity(entityPlayer, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!world.isRemote) {
                world.spawnEntityInWorld(new EntityPotion(world, (EntityLivingBase)entityPlayer, itemStack));
            }
            entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            return itemStack;
        }
        entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public int getColorFromItemStack(ItemStack itemStack, int n) {
        return n > 0 ? 0xFFFFFF : this.getColorFromDamage(itemStack.getMetadata());
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.DRINK;
    }

    public int getColorFromDamage(int n) {
        return PotionHelper.getLiquidColor(n, false);
    }
}

