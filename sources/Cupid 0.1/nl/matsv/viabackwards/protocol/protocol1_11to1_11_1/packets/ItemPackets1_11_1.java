package nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.meta.MetaHandlerEvent;
import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import nl.matsv.viabackwards.api.rewriters.LegacyBlockItemRewriter;
import nl.matsv.viabackwards.api.rewriters.LegacyEnchantmentRewriter;
import nl.matsv.viabackwards.protocol.protocol1_11to1_11_1.Protocol1_11To1_11_1;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ServerboundPackets1_9_3;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;

public class ItemPackets1_11_1 extends LegacyBlockItemRewriter<Protocol1_11To1_11_1> {
  private LegacyEnchantmentRewriter enchantmentRewriter;
  
  public ItemPackets1_11_1(Protocol1_11To1_11_1 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_9_3.SET_SLOT, Type.ITEM);
    itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_9_3.WINDOW_ITEMS, Type.ITEM_ARRAY);
    itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_9_3.ENTITY_EQUIPMENT, Type.ITEM);
    ((Protocol1_11To1_11_1)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    if (((String)wrapper.get(Type.STRING, 0)).equalsIgnoreCase("MC|TrList")) {
                      wrapper.passthrough(Type.INT);
                      int size = ((Short)wrapper.passthrough(Type.UNSIGNED_BYTE)).shortValue();
                      for (int i = 0; i < size; i++) {
                        wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                        wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM)));
                        boolean secondItem = ((Boolean)wrapper.passthrough(Type.BOOLEAN)).booleanValue();
                        if (secondItem)
                          wrapper.write(Type.ITEM, ItemPackets1_11_1.this.handleItemToClient((Item)wrapper.read(Type.ITEM))); 
                        wrapper.passthrough(Type.BOOLEAN);
                        wrapper.passthrough(Type.INT);
                        wrapper.passthrough(Type.INT);
                      } 
                    } 
                  }
                });
          }
        });
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_9_3.CLICK_WINDOW, Type.ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_9_3.CREATIVE_INVENTORY_ACTION, Type.ITEM);
    ((Protocol1_11To1_11_1)this.protocol).getEntityPackets().registerMetaHandler().handle(e -> {
          Metadata data = e.getData();
          if (data.getMetaType().getType().equals(Type.ITEM))
            data.setValue(handleItemToClient((Item)data.getValue())); 
          return data;
        });
  }
  
  protected void registerRewrites() {
    this.enchantmentRewriter = new LegacyEnchantmentRewriter(this.nbtTagName);
    this.enchantmentRewriter.registerEnchantment(22, "§7Sweeping Edge");
  }
  
  public Item handleItemToClient(Item item) {
    if (item == null)
      return null; 
    super.handleItemToClient(item);
    CompoundTag tag = item.getTag();
    if (tag == null)
      return item; 
    if (tag.get("ench") instanceof us.myles.viaversion.libs.opennbt.tag.builtin.ListTag)
      this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, false); 
    if (tag.get("StoredEnchantments") instanceof us.myles.viaversion.libs.opennbt.tag.builtin.ListTag)
      this.enchantmentRewriter.rewriteEnchantmentsToClient(tag, true); 
    return item;
  }
  
  public Item handleItemToServer(Item item) {
    if (item == null)
      return null; 
    super.handleItemToServer(item);
    CompoundTag tag = item.getTag();
    if (tag == null)
      return item; 
    if (tag.contains(this.nbtTagName + "|ench"))
      this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, false); 
    if (tag.contains(this.nbtTagName + "|StoredEnchantments"))
      this.enchantmentRewriter.rewriteEnchantmentsToServer(tag, true); 
    return item;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11to1_11_1\packets\ItemPackets1_11_1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */