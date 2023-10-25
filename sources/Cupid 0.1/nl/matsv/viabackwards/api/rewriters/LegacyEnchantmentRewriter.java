package nl.matsv.viabackwards.api.rewriters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ShortTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class LegacyEnchantmentRewriter {
  private final Map<Short, String> enchantmentMappings = new HashMap<>();
  
  private final String nbtTagName;
  
  private Set<Short> hideLevelForEnchants;
  
  public LegacyEnchantmentRewriter(String nbtTagName) {
    this.nbtTagName = nbtTagName;
  }
  
  public void registerEnchantment(int id, String replacementLore) {
    this.enchantmentMappings.put(Short.valueOf((short)id), replacementLore);
  }
  
  public void rewriteEnchantmentsToClient(CompoundTag tag, boolean storedEnchant) {
    String key = storedEnchant ? "StoredEnchantments" : "ench";
    ListTag enchantments = (ListTag)tag.get(key);
    ListTag remappedEnchantments = new ListTag(this.nbtTagName + "|" + key, CompoundTag.class);
    List<Tag> lore = new ArrayList<>();
    for (Tag enchantmentEntry : enchantments.clone()) {
      Short newId = (Short)((CompoundTag)enchantmentEntry).get("id").getValue();
      String enchantmentName = this.enchantmentMappings.get(newId);
      if (enchantmentName != null) {
        enchantments.remove(enchantmentEntry);
        Number level = (Number)((CompoundTag)enchantmentEntry).get("lvl").getValue();
        if (this.hideLevelForEnchants != null && this.hideLevelForEnchants.contains(newId)) {
          lore.add(new StringTag("", enchantmentName));
        } else {
          lore.add(new StringTag("", enchantmentName + " " + EnchantmentRewriter.getRomanNumber(level.shortValue())));
        } 
        remappedEnchantments.add(enchantmentEntry);
      } 
    } 
    if (!lore.isEmpty()) {
      if (!storedEnchant && enchantments.size() == 0) {
        CompoundTag dummyEnchantment = new CompoundTag("");
        dummyEnchantment.put((Tag)new ShortTag("id", (short)0));
        dummyEnchantment.put((Tag)new ShortTag("lvl", (short)0));
        enchantments.add((Tag)dummyEnchantment);
        tag.put((Tag)new ByteTag(this.nbtTagName + "|dummyEnchant"));
        IntTag hideFlags = (IntTag)tag.get("HideFlags");
        if (hideFlags == null) {
          hideFlags = new IntTag("HideFlags");
        } else {
          tag.put((Tag)new IntTag(this.nbtTagName + "|oldHideFlags", hideFlags.getValue().intValue()));
        } 
        int flags = hideFlags.getValue().intValue() | 0x1;
        hideFlags.setValue(flags);
        tag.put((Tag)hideFlags);
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
    String key = storedEnchant ? "StoredEnchantments" : "ench";
    ListTag remappedEnchantments = (ListTag)tag.get(this.nbtTagName + "|" + key);
    ListTag enchantments = (ListTag)tag.get(key);
    if (enchantments == null)
      enchantments = new ListTag(key, CompoundTag.class); 
    if (!storedEnchant && tag.remove(this.nbtTagName + "|dummyEnchant") != null) {
      for (Tag enchantment : enchantments.clone()) {
        Short id = (Short)((CompoundTag)enchantment).get("id").getValue();
        Short level = (Short)((CompoundTag)enchantment).get("lvl").getValue();
        if (id.shortValue() == 0 && level.shortValue() == 0)
          enchantments.remove(enchantment); 
      } 
      IntTag hideFlags = (IntTag)tag.remove(this.nbtTagName + "|oldHideFlags");
      if (hideFlags != null) {
        tag.put((Tag)new IntTag("HideFlags", hideFlags.getValue().intValue()));
      } else {
        tag.remove("HideFlags");
      } 
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
  
  public void setHideLevelForEnchants(int... enchants) {
    this.hideLevelForEnchants = new HashSet<>();
    for (int enchant : enchants)
      this.hideLevelForEnchants.add(Short.valueOf((short)enchant)); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\LegacyEnchantmentRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */