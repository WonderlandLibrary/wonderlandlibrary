package nl.matsv.viabackwards.protocol.protocol1_16_4to1_17.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_4to1_17.Protocol1_16_4To1_17;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.types.Chunk1_17Type;

public class BlockItemPackets1_17 extends ItemRewriter<Protocol1_16_4To1_17> {
  public BlockItemPackets1_17(Protocol1_16_4To1_17 protocol, TranslatableRewriter translatableRewriter) {
    super((BackwardsProtocol)protocol, translatableRewriter);
  }
  
  protected void registerPackets() {
    ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
    (new RecipeRewriter1_16((Protocol)this.protocol, this::handleItemToClient)).registerDefaultHandler((ClientboundPacketType)ClientboundPackets1_16_2.DECLARE_RECIPES);
    itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_16_2.COOLDOWN);
    itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_16_2.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
    itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_16_2.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerEntityEquipmentArray((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_16_2.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_16_2.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
    blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_ACTION);
    blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_CHANGE);
    blockRewriter.registerVarLongMultiBlockChange((ClientboundPacketType)ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE);
    blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    ((Protocol1_16_4To1_17)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.UPDATE_LIGHT, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough((Type)Type.VAR_INT);
                  wrapper.passthrough((Type)Type.VAR_INT);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Long)wrapper.read((Type)Type.VAR_LONG)).intValue()));
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Long)wrapper.read((Type)Type.VAR_LONG)).intValue()));
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Long)wrapper.read((Type)Type.VAR_LONG)).intValue()));
                  wrapper.write((Type)Type.VAR_INT, Integer.valueOf(((Long)wrapper.read((Type)Type.VAR_LONG)).intValue()));
                });
          }
        });
    ((Protocol1_16_4To1_17)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_17Type());
                  wrapper.write((Type)new Chunk1_16_2Type(), chunk);
                  for (int i = 0; i < (chunk.getSections()).length; i++) {
                    ChunkSection section = chunk.getSections()[i];
                    if (section != null)
                      for (int j = 0; j < section.getPaletteSize(); j++) {
                        int old = section.getPaletteEntry(j);
                        section.setPaletteEntry(j, ((Protocol1_16_4To1_17)BlockItemPackets1_17.this.protocol).getMappingData().getNewBlockStateId(old));
                      }  
                  } 
                });
          }
        });
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_16_2.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_16_2.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    ((Protocol1_16_4To1_17)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_16_2.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> BlockItemPackets1_17.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_4to1_17\packets\BlockItemPackets1_17.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */