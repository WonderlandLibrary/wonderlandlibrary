// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.items;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
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
import java.util.Map;

public class ItemRewriter
{
    private static Map<String, Integer> ENTTIY_NAME_TO_ID;
    private static Map<Integer, String> ENTTIY_ID_TO_NAME;
    private static Map<String, Integer> POTION_NAME_TO_ID;
    private static Map<Integer, String> POTION_ID_TO_NAME;
    private static Map<String, String> POTION_NAME_INDEX;
    
    public static Item toClient(final Item item) {
        if (item == null) {
            return null;
        }
        CompoundTag tag = item.tag();
        if (tag == null) {
            item.setTag(tag = new CompoundTag());
        }
        final CompoundTag viaVersionTag = new CompoundTag();
        tag.put("ViaRewind1_8to1_9", viaVersionTag);
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
                String s;
                if (id == 70) {
                    s = "§r§7Mending ";
                }
                else {
                    if (id != 9) {
                        continue;
                    }
                    s = "§r§7Frost Walker ";
                }
                enchTag.remove(ench);
                s += Enchantments.ENCHANTMENTS.getOrDefault(lvl, "enchantment.level." + lvl);
                lore.add(new StringTag(s));
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
        if (item.data() != 0 && tag.contains("Unbreakable")) {
            final ByteTag unbreakable = tag.get("Unbreakable");
            if (unbreakable.asByte() != 0) {
                viaVersionTag.put("Unbreakable", new ByteTag(unbreakable.asByte()));
                tag.remove("Unbreakable");
                if (display == null) {
                    tag.put("display", display = new CompoundTag());
                    viaVersionTag.put("noDisplay", new ByteTag());
                }
                ListTag loreTag2 = display.get("Lore");
                if (loreTag2 == null) {
                    display.put("Lore", loreTag2 = new ListTag(StringTag.class));
                }
                loreTag2.add(new StringTag("§9Unbreakable"));
            }
        }
        if (tag.contains("AttributeModifiers")) {
            viaVersionTag.put("AttributeModifiers", tag.get("AttributeModifiers").clone());
        }
        if (item.identifier() == 383 && item.data() == 0) {
            int data = 0;
            if (tag.contains("EntityTag")) {
                final CompoundTag entityTag = tag.get("EntityTag");
                if (entityTag.contains("id")) {
                    final StringTag id2 = entityTag.get("id");
                    if (ItemRewriter.ENTTIY_NAME_TO_ID.containsKey(id2.getValue())) {
                        data = ItemRewriter.ENTTIY_NAME_TO_ID.get(id2.getValue());
                    }
                    else if (display == null) {
                        tag.put("display", display = new CompoundTag());
                        viaVersionTag.put("noDisplay", new ByteTag());
                        display.put("Name", new StringTag("§rSpawn " + id2.getValue()));
                    }
                }
            }
            item.setData((short)data);
        }
        ReplacementRegistry1_8to1_9.replace(item);
        if (item.identifier() == 373 || item.identifier() == 438 || item.identifier() == 441) {
            int data = 0;
            if (tag.contains("Potion")) {
                final StringTag potion = tag.get("Potion");
                String potionName = potion.getValue().replace("minecraft:", "");
                if (ItemRewriter.POTION_NAME_TO_ID.containsKey(potionName)) {
                    data = ItemRewriter.POTION_NAME_TO_ID.get(potionName);
                }
                if (item.identifier() == 438) {
                    potionName += "_splash";
                }
                else if (item.identifier() == 441) {
                    potionName += "_lingering";
                }
                if ((display == null || !display.contains("Name")) && ItemRewriter.POTION_NAME_INDEX.containsKey(potionName)) {
                    if (display == null) {
                        tag.put("display", display = new CompoundTag());
                        viaVersionTag.put("noDisplay", new ByteTag());
                    }
                    display.put("Name", new StringTag(ItemRewriter.POTION_NAME_INDEX.get(potionName)));
                }
            }
            if (item.identifier() == 438 || item.identifier() == 441) {
                item.setIdentifier(373);
                data += 8192;
            }
            item.setData((short)data);
        }
        if (tag.contains("AttributeModifiers")) {
            final ListTag attributes = tag.get("AttributeModifiers");
            for (int i = 0; i < attributes.size(); ++i) {
                final CompoundTag attribute = attributes.get(i);
                final String name = (String)attribute.get("AttributeName").getValue();
                if (!Protocol1_8TO1_9.VALID_ATTRIBUTES.contains(attribute)) {
                    attributes.remove(attribute);
                    --i;
                }
            }
        }
        if (viaVersionTag.size() == 2 && (short)viaVersionTag.get("id").getValue() == item.identifier() && (short)viaVersionTag.get("data").getValue() == item.data()) {
            item.tag().remove("ViaRewind1_8to1_9");
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
        CompoundTag tag = item.tag();
        if (item.identifier() == 383 && item.data() != 0) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag());
            }
            if (!tag.contains("EntityTag") && ItemRewriter.ENTTIY_ID_TO_NAME.containsKey((int)item.data())) {
                final CompoundTag entityTag = new CompoundTag();
                entityTag.put("id", new StringTag(ItemRewriter.ENTTIY_ID_TO_NAME.get((int)item.data())));
                tag.put("EntityTag", entityTag);
            }
            item.setData((short)0);
        }
        if (item.identifier() == 373 && (tag == null || !tag.contains("Potion"))) {
            if (tag == null) {
                item.setTag(tag = new CompoundTag());
            }
            if (item.data() >= 16384) {
                item.setIdentifier(438);
                item.setData((short)(item.data() - 8192));
            }
            final String name = (item.data() == 8192) ? "water" : com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter.potionNameFromDamage(item.data());
            tag.put("Potion", new StringTag("minecraft:" + name));
            item.setData((short)0);
        }
        if (tag == null || !item.tag().contains("ViaRewind1_8to1_9")) {
            return item;
        }
        final CompoundTag viaVersionTag = tag.remove("ViaRewind1_8to1_9");
        item.setIdentifier((short)viaVersionTag.get("id").getValue());
        item.setData((short)viaVersionTag.get("data").getValue());
        if (viaVersionTag.contains("noDisplay")) {
            tag.remove("display");
        }
        if (viaVersionTag.contains("Unbreakable")) {
            tag.put("Unbreakable", viaVersionTag.get("Unbreakable").clone());
        }
        if (viaVersionTag.contains("displayName")) {
            CompoundTag display = tag.get("display");
            if (display == null) {
                tag.put("display", display = new CompoundTag());
            }
            final StringTag name2 = display.get("Name");
            if (name2 == null) {
                display.put("Name", new StringTag((String)viaVersionTag.get("displayName").getValue()));
            }
            else {
                name2.setValue((String)viaVersionTag.get("displayName").getValue());
            }
        }
        else if (tag.contains("display")) {
            tag.get("display").remove("Name");
        }
        if (viaVersionTag.contains("lore")) {
            CompoundTag display = tag.get("display");
            if (display == null) {
                tag.put("display", display = new CompoundTag());
            }
            final ListTag lore = display.get("Lore");
            if (lore == null) {
                display.put("Lore", new ListTag((List<Tag>)viaVersionTag.get("lore").getValue()));
            }
            else {
                lore.setValue((List<Tag>)viaVersionTag.get("lore").getValue());
            }
        }
        else if (tag.contains("display")) {
            tag.get("display").remove("Lore");
        }
        tag.remove("AttributeModifiers");
        if (viaVersionTag.contains("AttributeModifiers")) {
            tag.put("AttributeModifiers", viaVersionTag.get("AttributeModifiers"));
        }
        return item;
    }
    
    static {
        ItemRewriter.POTION_NAME_INDEX = new HashMap<String, String>();
        for (final Field field : ItemRewriter.class.getDeclaredFields()) {
            try {
                final Field other = com.viaversion.viaversion.protocols.protocol1_9to1_8.ItemRewriter.class.getDeclaredField(field.getName());
                other.setAccessible(true);
                field.setAccessible(true);
                field.set(null, other.get(null));
            }
            catch (Exception ex) {}
        }
        ItemRewriter.POTION_NAME_TO_ID.put("luck", 8203);
        ItemRewriter.POTION_NAME_INDEX.put("water", "§rWater Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane", "§rMundane Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick", "§rThick Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward", "§rAwkward Potion");
        ItemRewriter.POTION_NAME_INDEX.put("water_splash", "§rSplash Water Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane_splash", "§rMundane Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick_splash", "§rThick Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward_splash", "§rAwkward Splash Potion");
        ItemRewriter.POTION_NAME_INDEX.put("water_lingering", "§rLingering Water Bottle");
        ItemRewriter.POTION_NAME_INDEX.put("mundane_lingering", "§rMundane Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("thick_lingering", "§rThick Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("awkward_lingering", "§rAwkward Lingering Potion");
        ItemRewriter.POTION_NAME_INDEX.put("night_vision_lingering", "§rLingering Potion of Night Vision");
        ItemRewriter.POTION_NAME_INDEX.put("long_night_vision_lingering", "§rLingering Potion of Night Vision");
        ItemRewriter.POTION_NAME_INDEX.put("invisibility_lingering", "§rLingering Potion of Invisibility");
        ItemRewriter.POTION_NAME_INDEX.put("long_invisibility_lingering", "§rLingering Potion of Invisibility");
        ItemRewriter.POTION_NAME_INDEX.put("leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("long_leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("strong_leaping_lingering", "§rLingering Potion of Leaping");
        ItemRewriter.POTION_NAME_INDEX.put("fire_resistance_lingering", "§rLingering Potion of Fire Resistance");
        ItemRewriter.POTION_NAME_INDEX.put("long_fire_resistance_lingering", "§rLingering Potion of Fire Resistance");
        ItemRewriter.POTION_NAME_INDEX.put("swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("long_swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("strong_swiftness_lingering", "§rLingering Potion of Swiftness");
        ItemRewriter.POTION_NAME_INDEX.put("slowness_lingering", "§rLingering Potion of Slowness");
        ItemRewriter.POTION_NAME_INDEX.put("long_slowness_lingering", "§rLingering Potion of Slowness");
        ItemRewriter.POTION_NAME_INDEX.put("water_breathing_lingering", "§rLingering Potion of Water Breathing");
        ItemRewriter.POTION_NAME_INDEX.put("long_water_breathing_lingering", "§rLingering Potion of Water Breathing");
        ItemRewriter.POTION_NAME_INDEX.put("healing_lingering", "§rLingering Potion of Healing");
        ItemRewriter.POTION_NAME_INDEX.put("strong_healing_lingering", "§rLingering Potion of Healing");
        ItemRewriter.POTION_NAME_INDEX.put("harming_lingering", "§rLingering Potion of Harming");
        ItemRewriter.POTION_NAME_INDEX.put("strong_harming_lingering", "§rLingering Potion of Harming");
        ItemRewriter.POTION_NAME_INDEX.put("poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("long_poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("strong_poison_lingering", "§rLingering Potion of Poisen");
        ItemRewriter.POTION_NAME_INDEX.put("regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("long_regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("strong_regeneration_lingering", "§rLingering Potion of Regeneration");
        ItemRewriter.POTION_NAME_INDEX.put("strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("long_strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("strong_strength_lingering", "§rLingering Potion of Strength");
        ItemRewriter.POTION_NAME_INDEX.put("weakness_lingering", "§rLingering Potion of Weakness");
        ItemRewriter.POTION_NAME_INDEX.put("long_weakness_lingering", "§rLingering Potion of Weakness");
        ItemRewriter.POTION_NAME_INDEX.put("luck_lingering", "§rLingering Potion of Luck");
        ItemRewriter.POTION_NAME_INDEX.put("luck", "§rPotion of Luck");
        ItemRewriter.POTION_NAME_INDEX.put("luck_splash", "§rSplash Potion of Luck");
    }
}
