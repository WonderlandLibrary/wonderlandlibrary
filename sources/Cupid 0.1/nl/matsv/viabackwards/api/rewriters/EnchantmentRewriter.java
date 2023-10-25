package nl.matsv.viabackwards.api.rewriters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ChatRewriter;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class EnchantmentRewriter {
  private final Map<String, String> enchantmentMappings = new HashMap<>();
  
  private final String nbtTagName;
  
  private final boolean jsonFormat;
  
  public EnchantmentRewriter(String nbtTagName, boolean jsonFormat) {
    this.nbtTagName = nbtTagName;
    this.jsonFormat = jsonFormat;
  }
  
  public EnchantmentRewriter(String nbtTagName) {
    this(nbtTagName, true);
  }
  
  public void registerEnchantment(String key, String replacementLore) {
    this.enchantmentMappings.put(key, replacementLore);
  }
  
  public void handleToClient(Item item) {
    CompoundTag tag = item.getTag();
    if (tag == null)
      return; 
    if (tag.get("Enchantments") instanceof ListTag)
      rewriteEnchantmentsToClient(tag, false); 
    if (tag.get("StoredEnchantments") instanceof ListTag)
      rewriteEnchantmentsToClient(tag, true); 
  }
  
  public void handleToServer(Item item) {
    CompoundTag tag = item.getTag();
    if (tag == null)
      return; 
    if (tag.contains(this.nbtTagName + "|Enchantments"))
      rewriteEnchantmentsToServer(tag, false); 
    if (tag.contains(this.nbtTagName + "|StoredEnchantments"))
      rewriteEnchantmentsToServer(tag, true); 
  }
  
  public void rewriteEnchantmentsToClient(CompoundTag tag, boolean storedEnchant) {
    String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
    ListTag enchantments = (ListTag)tag.get(key);
    ListTag remappedEnchantments = new ListTag(this.nbtTagName + "|" + key, CompoundTag.class);
    List<Tag> lore = new ArrayList<>();
    for (Tag enchantmentEntry : enchantments.clone()) {
      String newId = (String)((CompoundTag)enchantmentEntry).get("id").getValue();
      String enchantmentName = this.enchantmentMappings.get(newId);
      if (enchantmentName != null) {
        enchantments.remove(enchantmentEntry);
        Number level = (Number)((CompoundTag)enchantmentEntry).get("lvl").getValue();
        String loreValue = enchantmentName + " " + getRomanNumber(level.intValue());
        if (this.jsonFormat)
          loreValue = ChatRewriter.legacyTextToJson(loreValue).toString(); 
        lore.add(new StringTag("", loreValue));
        remappedEnchantments.add(enchantmentEntry);
      } 
    } 
    if (!lore.isEmpty()) {
      if (!storedEnchant && enchantments.size() == 0) {
        CompoundTag dummyEnchantment = new CompoundTag("");
        dummyEnchantment.put((Tag)new StringTag("id", ""));
        dummyEnchantment.put((Tag)new ShortTag("lvl", (short)0));
        enchantments.add((Tag)dummyEnchantment);
        tag.put((Tag)new ByteTag(this.nbtTagName + "|dummyEnchant"));
      } 
      tag.put((Tag)remappedEnchantments);
      CompoundTag display = (CompoundTag)tag.get("display");
      if (display == null)
        tag.put((Tag)(display = new CompoundTag("display"))); 
      ListTag loreTag = (ListTag)display.get("Lore");
      if (loreTag == null)
        display.put((Tag)(loreTag = new ListTag("Lore", StringTag.class))); 
      lore.addAll(loreTag.getValue());
      loreTag.setValue(lore);
    } 
  }
  
  public void rewriteEnchantmentsToServer(CompoundTag tag, boolean storedEnchant) {
    String key = storedEnchant ? "StoredEnchantments" : "Enchantments";
    ListTag remappedEnchantments = (ListTag)tag.get(this.nbtTagName + "|" + key);
    ListTag enchantments = (ListTag)tag.get(key);
    if (enchantments == null)
      enchantments = new ListTag(key, CompoundTag.class); 
    if (!storedEnchant && tag.remove(this.nbtTagName + "|dummyEnchant") != null)
      for (Tag enchantment : enchantments.clone()) {
        String id = (String)((CompoundTag)enchantment).get("id").getValue();
        if (id.isEmpty())
          enchantments.remove(enchantment); 
      }  
    CompoundTag display = (CompoundTag)tag.get("display");
    ListTag lore = (display != null) ? (ListTag)display.get("Lore") : null;
    for (Tag enchantment : remappedEnchantments.clone()) {
      enchantments.add(enchantment);
      if (lore != null && lore.size() != 0)
        lore.remove(lore.get(0)); 
    } 
    if (lore != null && lore.size() == 0) {
      display.remove("Lore");
      if (display.isEmpty())
        tag.remove("display"); 
    } 
    tag.put((Tag)enchantments);
    tag.remove(remappedEnchantments.getName());
  }
  
  public static String getRomanNumber(int number) {
    switch (number) {
      case 1:
        return "I";
      case 2:
        return "II";
      case 3:
        return "III";
      case 4:
        return "IV";
      case 5:
        return "V";
      case 6:
        return "VI";
      case 7:
        return "VII";
      case 8:
        return "VIII";
      case 9:
        return "IX";
      case 10:
        return "X";
    } 
    return Integer.toString(number);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\EnchantmentRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */