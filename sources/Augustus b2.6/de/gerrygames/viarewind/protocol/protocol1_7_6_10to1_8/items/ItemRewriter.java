// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items;

import java.util.Iterator;
import java.util.List;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ByteTag;
import de.gerrygames.viarewind.utils.Enchantments;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.NumberTag;
import java.util.Collection;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import java.util.ArrayList;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ShortTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;

public class ItemRewriter
{
    public static Item toClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.tag();
        if (tag == null) {
            item.setTag(tag = new CompoundTag());
        }
        final CompoundTag viaVersionTag = new CompoundTag();
        tag.put("ViaRewind1_7_6_10to1_8", viaVersionTag);
        viaVersionTag.put("id", new ShortTag((short)item.identifier()));
        viaVersionTag.put("data", new ShortTag(item.data()));
        CompoundTag display = tag.get("display");
        if (display != null && display.contains("Name")) {
            viaVersionTag.put("displayName", new StringTag((String)display.get("Name").getValue()));
        }
        if (display != null && display.contains("Lore")) {
            viaVersionTag.put("lore", new ListTag(display.get("Lore").getValue()));
        }
        if (tag.contains("ench") || tag.contains("StoredEnchantments")) {
            final ListTag enchTag = tag.contains("ench") ? tag.get("ench") : tag.get("StoredEnchantments");
            final List<Tag> lore = new ArrayList<Tag>();
            for (final Tag ench : new ArrayList<Tag>(enchTag.getValue())) {
                final short id = ((CompoundTag)ench).get("id").asShort();
                final short lvl = ((CompoundTag)ench).get("lvl").asShort();
                if (id == 8) {
                    String s = "§r§7Depth Strider ";
                    enchTag.remove(ench);
                    s += Enchantments.ENCHANTMENTS.getOrDefault(lvl, "enchantment.level." + lvl);
                    lore.add(new StringTag(s));
                }
            }
            if (!lore.isEmpty()) {
                if (display == null) {
                    tag.put("display", display = new CompoundTag());
                    viaVersionTag.put("noDisplay", new ByteTag());
                }
                ListTag loreTag = display.get("Lore");
                if (loreTag == null) {
                    display.put("Lore", loreTag = new ListTag(StringTag.class));
                }
                lore.addAll(loreTag.getValue());
                loreTag.setValue(lore);
            }
        }
        if (item.identifier() == 387 && tag.contains("pages")) {
            final ListTag pages = tag.get("pages");
            final ListTag oldPages = new ListTag(StringTag.class);
            viaVersionTag.put("pages", oldPages);
            for (int i = 0; i < pages.size(); ++i) {
                final StringTag page = pages.get(i);
                String value = page.getValue();
                oldPages.add(new StringTag(value));
                value = ChatUtil.jsonToLegacy(value);
                page.setValue(value);
            }
        }
        ReplacementRegistry1_7_6_10to1_8.replace(item);
        if (viaVersionTag.size() == 2 && (short)viaVersionTag.get("id").getValue() == item.identifier() && (short)viaVersionTag.get("data").getValue() == item.data()) {
            item.tag().remove("ViaRewind1_7_6_10to1_8");
            if (item.tag().isEmpty()) {
                item.setTag(null);
            }
        }
        return item;
    }
    
    public static Item toServer(final Item item) {
        if (item == null) {
            return null;
        }
        final CompoundTag tag = item.tag();
        if (tag == null || !item.tag().contains("ViaRewind1_7_6_10to1_8")) {
            return item;
        }
        final CompoundTag viaVersionTag = tag.remove("ViaRewind1_7_6_10to1_8");
        item.setIdentifier((short)viaVersionTag.get("id").getValue());
        item.setData((short)viaVersionTag.get("data").getValue());
        if (viaVersionTag.contains("noDisplay")) {
            tag.remove("display");
        }
        if (viaVersionTag.contains("displayName")) {
            CompoundTag display = tag.get("display");
            if (display == null) {
                tag.put("display", display = new CompoundTag());
            }
            final StringTag name = display.get("Name");
            if (name == null) {
                display.put("Name", new StringTag((String)viaVersionTag.get("displayName").getValue()));
            }
            else {
                name.setValue((String)viaVersionTag.get("displayName").getValue());
            }
        }
        else if (tag.contains("display")) {
            tag.get("display").remove("Name");
        }
        if (item.identifier() == 387) {
            final ListTag oldPages = viaVersionTag.get("pages");
            tag.remove("pages");
            tag.put("pages", oldPages);
        }
        return item;
    }
}
