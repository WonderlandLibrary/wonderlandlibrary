// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.api.rewriters;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ChatRewriter;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.HashMap;
import java.util.Map;

public class EnchantmentRewriter
{
    private final Map<String, String> enchantmentMappings;
    private final ItemRewriter itemRewriter;
    private final boolean jsonFormat;
    
    public EnchantmentRewriter(final ItemRewriter itemRewriter, final boolean jsonFormat) {
        this.enchantmentMappings = new HashMap<String, String>();
        this.itemRewriter = itemRewriter;
        this.jsonFormat = jsonFormat;
    }
    
    public EnchantmentRewriter(final ItemRewriter itemRewriter) {
        this(itemRewriter, true);
    }
    
    public void registerEnchantment(final String key, final String replacementLore) {
        this.enchantmentMappings.put(key, replacementLore);
    }
    
    public void handleToClient(final Item item) {
        final CompoundTag tag = item.tag();
        if (tag == null) {
            return;
        }
        if (tag.get("Enchantments") instanceof ListTag) {
            this.rewriteEnchantmentsToClient(tag, false);
        }
        if (tag.get("StoredEnchantments") instanceof ListTag) {
            this.rewriteEnchantmentsToClient(tag, true);
        }
    }
    
    public void handleToServer(final Item item) {
        final CompoundTag tag = item.tag();
        if (tag == null) {
            return;
        }
        if (tag.contains(this.itemRewriter.getNbtTagName() + "|Enchantments")) {
            this.rewriteEnchantmentsToServer(tag, false);
        }
        if (tag.contains(this.itemRewriter.getNbtTagName() + "|StoredEnchantments")) {
            this.rewriteEnchantmentsToServer(tag, true);
        }
    }
    
    public void rewriteEnchantmentsToClient(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
        final ListTag enchantments = tag.get(key);
        final List<Tag> loreToAdd = new ArrayList<Tag>();
        boolean changed = false;
        final Iterator<Tag> iterator = enchantments.iterator();
        while (iterator.hasNext()) {
            final CompoundTag enchantmentEntry = iterator.next();
            final Tag idTag = enchantmentEntry.get("id");
            if (!(idTag instanceof StringTag)) {
                continue;
            }
            final String enchantmentId = ((StringTag)idTag).getValue();
            final String remappedName = this.enchantmentMappings.get(enchantmentId);
            if (remappedName == null) {
                continue;
            }
            if (!changed) {
                this.itemRewriter.saveListTag(tag, enchantments, key);
                changed = true;
            }
            iterator.remove();
            final int level = enchantmentEntry.get("lvl").asInt();
            String loreValue = remappedName + " " + getRomanNumber(level);
            if (this.jsonFormat) {
                loreValue = ChatRewriter.legacyTextToJsonString(loreValue);
            }
            loreToAdd.add(new StringTag(loreValue));
        }
        if (!loreToAdd.isEmpty()) {
            if (!storedEnchant && enchantments.size() == 0) {
                final CompoundTag dummyEnchantment = new CompoundTag();
                dummyEnchantment.put("id", new StringTag());
                dummyEnchantment.put("lvl", new ShortTag((short)0));
                enchantments.add(dummyEnchantment);
            }
            CompoundTag display = tag.get("display");
            if (display == null) {
                tag.put("display", display = new CompoundTag());
            }
            ListTag loreTag = display.get("Lore");
            if (loreTag == null) {
                display.put("Lore", loreTag = new ListTag(StringTag.class));
            }
            else {
                this.itemRewriter.saveListTag(display, loreTag, "Lore");
            }
            loreToAdd.addAll(loreTag.getValue());
            loreTag.setValue(loreToAdd);
        }
    }
    
    public void rewriteEnchantmentsToServer(final CompoundTag tag, final boolean storedEnchant) {
        final String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
        this.itemRewriter.restoreListTag(tag, key);
    }
    
    public static String getRomanNumber(final int number) {
        switch (number) {
            case 1: {
                return "I";
            }
            case 2: {
                return "II";
            }
            case 3: {
                return "III";
            }
            case 4: {
                return "IV";
            }
            case 5: {
                return "V";
            }
            case 6: {
                return "VI";
            }
            case 7: {
                return "VII";
            }
            case 8: {
                return "VIII";
            }
            case 9: {
                return "IX";
            }
            case 10: {
                return "X";
            }
            default: {
                return Integer.toString(number);
            }
        }
    }
}
