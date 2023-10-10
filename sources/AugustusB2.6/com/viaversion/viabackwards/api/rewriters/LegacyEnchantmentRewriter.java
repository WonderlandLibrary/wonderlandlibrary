// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.IntTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class LegacyEnchantmentRewriter
{
    private final Map<Short, String> enchantmentMappings;
    private final String nbtTagName;
    private Set<Short> hideLevelForEnchants;
    
    public LegacyEnchantmentRewriter(final String nbtTagName) {
        this.enchantmentMappings = new HashMap<Short, String>();
        this.nbtTagName = nbtTagName;
    }
    
    public void registerEnchantment(final int id, final String replacementLore) {
        this.enchantmentMappings.put((short)id, replacementLore);
    }
    
    public void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "ench";
        final ListTag enchantments = tag.get(key);
        final ListTag remappedEnchantments = new ListTag(CompoundTag.class);
        final List<Tag> lore = new ArrayList<Tag>();
        for (final Tag enchantmentEntry : enchantments.clone()) {
            final Tag idTag = ((CompoundTag)enchantmentEntry).get("id");
            if (idTag == null) {
                continue;
            }
            final short newId = ((NumberTag)idTag).asShort();
            final String enchantmentName = this.enchantmentMappings.get(newId);
            if (enchantmentName == null) {
                continue;
            }
            enchantments.remove(enchantmentEntry);
            final short level = ((CompoundTag)enchantmentEntry).get("lvl").asShort();
            if (this.hideLevelForEnchants != null && this.hideLevelForEnchants.contains(newId)) {
                lore.add(new StringTag(enchantmentName));
            }
            else {
                lore.add(new StringTag(enchantmentName + " " + EnchantmentRewriter.getRomanNumber(level)));
            }
            remappedEnchantments.add(enchantmentEntry);
        }
        if (!lore.isEmpty()) {
            if (!storedEnchant && enchantments.size() == 0) {
                final CompoundTag dummyEnchantment = new CompoundTag();
                dummyEnchantment.put("id", new ShortTag((short)0));
                dummyEnchantment.put("lvl", new ShortTag((short)0));
                enchantments.add(dummyEnchantment);
                tag.put(this.nbtTagName + "|dummyEnchant", new ByteTag());
                IntTag hideFlags = tag.get("HideFlags");
                if (hideFlags == null) {
                    hideFlags = new IntTag();
                }
                else {
                    tag.put(this.nbtTagName + "|oldHideFlags", new IntTag(hideFlags.asByte()));
                }
                final int flags = hideFlags.asByte() | 0x1;
                hideFlags.setValue(flags);
                tag.put("HideFlags", hideFlags);
            }
            tag.put(this.nbtTagName + "|" + key, remappedEnchantments);
            CompoundTag display = tag.get("display");
            if (display == null) {
                tag.put("display", display = new CompoundTag());
            }
            ListTag loreTag = display.get("Lore");
            if (loreTag == null) {
                display.put("Lore", loreTag = new ListTag(StringTag.class));
            }
            lore.addAll(loreTag.getValue());
            loreTag.setValue(lore);
        }
    }
    
    public void rewriteEnchantmentsToServer(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "ench";
        final ListTag remappedEnchantments = tag.remove(this.nbtTagName + "|" + key);
        ListTag enchantments = tag.get(key);
        if (enchantments == null) {
            enchantments = new ListTag(CompoundTag.class);
        }
        if (!storedEnchant && tag.remove(this.nbtTagName + "|dummyEnchant") != null) {
            for (final Tag enchantment : enchantments.clone()) {
                final short id = ((CompoundTag)enchantment).get("id").asShort();
                final short level = ((CompoundTag)enchantment).get("lvl").asShort();
                if (id == 0 && level == 0) {
                    enchantments.remove(enchantment);
                }
            }
            final IntTag hideFlags = tag.remove(this.nbtTagName + "|oldHideFlags");
            if (hideFlags != null) {
                tag.put("HideFlags", new IntTag(hideFlags.asByte()));
            }
            else {
                tag.remove("HideFlags");
            }
        }
        final CompoundTag display = tag.get("display");
        final ListTag lore = (display != null) ? display.get("Lore") : null;
        for (final Tag enchantment2 : remappedEnchantments.clone()) {
            enchantments.add(enchantment2);
            if (lore != null && lore.size() != 0) {
                lore.remove(lore.get(0));
            }
        }
        if (lore != null && lore.size() == 0) {
            display.remove("Lore");
            if (display.isEmpty()) {
                tag.remove("display");
            }
        }
        tag.put(key, enchantments);
    }
    
    public void setHideLevelForEnchants(final int... enchants) {
        this.hideLevelForEnchants = new HashSet<Short>();
        for (final int enchant : enchants) {
            this.hideLevelForEnchants.add((short)enchant);
        }
    }
}
