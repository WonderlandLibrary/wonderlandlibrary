package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.items;

import de.gerrygames.viarewind.utils.ChatUtil;
import de.gerrygames.viarewind.utils.Enchantments;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class ItemRewriter {
  public static Item toClient(Item item) {
    if (item == null)
      return null; 
    CompoundTag tag = item.getTag();
    if (tag == null)
      item.setTag(tag = new CompoundTag("")); 
    CompoundTag viaVersionTag = new CompoundTag("ViaRewind1_7_6_10to1_8");
    tag.put((Tag)viaVersionTag);
    viaVersionTag.put((Tag)new ShortTag("id", (short)item.getIdentifier()));
    viaVersionTag.put((Tag)new ShortTag("data", item.getData()));
    CompoundTag display = (CompoundTag)tag.get("display");
    if (display != null && display.contains("Name"))
      viaVersionTag.put((Tag)new StringTag("displayName", (String)display.get("Name").getValue())); 
    if (display != null && display.contains("Lore"))
      viaVersionTag.put((Tag)new ListTag("lore", ((ListTag)display.get("Lore")).getValue())); 
    if (tag.contains("ench") || tag.contains("StoredEnchantments")) {
      ListTag enchTag = tag.contains("ench") ? (ListTag)tag.get("ench") : (ListTag)tag.get("StoredEnchantments");
      List<Tag> enchants = enchTag.getValue();
      List<Tag> lore = new ArrayList<>();
      for (Tag ench : enchants) {
        short id = ((Short)((CompoundTag)ench).get("id").getValue()).shortValue();
        short lvl = ((Short)((CompoundTag)ench).get("lvl").getValue()).shortValue();
        if (id == 8) {
          String s = "§r§7Depth Strider ";
          enchTag.remove(ench);
          s = s + (String)Enchantments.ENCHANTMENTS.getOrDefault(Short.valueOf(lvl), "enchantment.level." + lvl);
          lore.add(new StringTag("", s));
        } 
      } 
      if (!lore.isEmpty()) {
        if (display == null) {
          tag.put((Tag)(display = new CompoundTag("display")));
          viaVersionTag.put((Tag)new ByteTag("noDisplay"));
        } 
        ListTag loreTag = (ListTag)display.get("Lore");
        if (loreTag == null)
          display.put((Tag)(loreTag = new ListTag("Lore", StringTag.class))); 
        lore.addAll(loreTag.getValue());
        loreTag.setValue(lore);
      } 
    } 
    if (item.getIdentifier() == 387 && tag.contains("pages")) {
      ListTag pages = (ListTag)tag.get("pages");
      ListTag oldPages = new ListTag("pages", StringTag.class);
      viaVersionTag.put((Tag)oldPages);
      for (int i = 0; i < pages.size(); i++) {
        StringTag page = (StringTag)pages.get(i);
        String value = page.getValue();
        oldPages.add((Tag)new StringTag(page.getName(), value));
        value = ChatUtil.jsonToLegacy(value);
        page.setValue(value);
      } 
    } 
    ReplacementRegistry1_7_6_10to1_8.replace(item);
    if (viaVersionTag.size() == 2 && ((Short)viaVersionTag.get("id").getValue()).shortValue() == item.getIdentifier() && ((Short)viaVersionTag.get("data").getValue()).shortValue() == item.getData()) {
      item.getTag().remove("ViaRewind1_7_6_10to1_8");
      if (item.getTag().isEmpty())
        item.setTag(null); 
    } 
    return item;
  }
  
  public static Item toServer(Item item) {
    if (item == null)
      return null; 
    CompoundTag tag = item.getTag();
    if (tag == null || !item.getTag().contains("ViaRewind1_7_6_10to1_8"))
      return item; 
    CompoundTag viaVersionTag = (CompoundTag)tag.remove("ViaRewind1_7_6_10to1_8");
    item.setIdentifier(((Short)viaVersionTag.get("id").getValue()).shortValue());
    item.setData(((Short)viaVersionTag.get("data").getValue()).shortValue());
    if (viaVersionTag.contains("noDisplay"))
      tag.remove("display"); 
    if (viaVersionTag.contains("displayName")) {
      CompoundTag display = (CompoundTag)tag.get("display");
      if (display == null)
        tag.put((Tag)(display = new CompoundTag("display"))); 
      StringTag name = (StringTag)display.get("Name");
      if (name == null) {
        display.put((Tag)new StringTag("Name", (String)viaVersionTag.get("displayName").getValue()));
      } else {
        name.setValue((String)viaVersionTag.get("displayName").getValue());
      } 
    } else if (tag.contains("display")) {
      ((CompoundTag)tag.get("display")).remove("Name");
    } 
    if (item.getIdentifier() == 387) {
      ListTag oldPages = (ListTag)viaVersionTag.get("pages");
      tag.remove("pages");
      tag.put((Tag)oldPages);
    } 
    return item;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\items\ItemRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */