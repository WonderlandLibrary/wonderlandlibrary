package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.MappedItem;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.viaversion.libs.opennbt.tag.builtin.ByteTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public abstract class ItemRewriter<T extends BackwardsProtocol> extends ItemRewriterBase<T> {
  private final TranslatableRewriter translatableRewriter;
  
  protected ItemRewriter(T protocol, @Nullable TranslatableRewriter translatableRewriter) {
    super(protocol, true);
    this.translatableRewriter = translatableRewriter;
  }
  
  @Nullable
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    CompoundTag display = (item.getTag() != null) ? (CompoundTag)item.getTag().get("display") : null;
    if (this.translatableRewriter != null && display != null) {
      StringTag name = (StringTag)display.get("Name");
      if (name != null) {
        String newValue = this.translatableRewriter.processText(name.getValue()).toString();
        if (!newValue.equals(name.getValue()))
          saveNameTag(display, name); 
        name.setValue(newValue);
      } 
      ListTag lore = (ListTag)display.get("Lore");
      if (lore != null) {
        ListTag original = null;
        boolean changed = false;
        for (Tag loreEntryTag : lore) {
          if (!(loreEntryTag instanceof StringTag))
            continue; 
          StringTag loreEntry = (StringTag)loreEntryTag;
          String newValue = this.translatableRewriter.processText(loreEntry.getValue()).toString();
          if (!changed && !newValue.equals(loreEntry.getValue())) {
            changed = true;
            original = lore.clone();
          } 
          loreEntry.setValue(newValue);
        } 
        if (changed)
          saveLoreTag(display, original); 
      } 
    } 
    MappedItem data = this.protocol.getMappingData().getMappedItem(item.getIdentifier());
    if (data == null)
      return super.handleItemToClient(item); 
    if (item.getTag() == null)
      item.setTag(new CompoundTag("")); 
    item.getTag().put((Tag)new IntTag(this.nbtTagName + "|id", item.getIdentifier()));
    item.setIdentifier(data.getId());
    if (display == null)
      item.getTag().put((Tag)(display = new CompoundTag("display"))); 
    if (!display.contains("Name")) {
      display.put((Tag)new StringTag("Name", data.getJsonName()));
      display.put((Tag)new ByteTag(this.nbtTagName + "|customName"));
    } 
    return item;
  }
  
  @Nullable
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    super.handleItemToServer(item);
    if (item.getTag() != null) {
      IntTag originalId = (IntTag)item.getTag().remove(this.nbtTagName + "|id");
      if (originalId != null)
        item.setIdentifier(originalId.getValue().intValue()); 
    } 
    return item;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\ItemRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */