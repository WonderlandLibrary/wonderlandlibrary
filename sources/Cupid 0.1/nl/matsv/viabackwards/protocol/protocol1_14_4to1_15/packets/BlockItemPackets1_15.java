package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.Protocol1_14_4To1_15;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.chunks.Chunk;
import us.myles.ViaVersion.api.minecraft.chunks.ChunkSection;
import us.myles.ViaVersion.api.minecraft.item.Item;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.BlockRewriter;
import us.myles.ViaVersion.api.rewriters.ItemRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.data.RecipeRewriter1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.types.Chunk1_14Type;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.types.Chunk1_15Type;

public class BlockItemPackets1_15 extends ItemRewriter<Protocol1_14_4To1_15> {
  public BlockItemPackets1_15(Protocol1_14_4To1_15 protocol, TranslatableRewriter translatableRewriter) {
    super((BackwardsProtocol)protocol, translatableRewriter);
  }
  
  protected void registerPackets() {
    ItemRewriter itemRewriter = new ItemRewriter((Protocol)this.protocol, this::handleItemToClient, this::handleItemToServer);
    BlockRewriter blockRewriter = new BlockRewriter((Protocol)this.protocol, Type.POSITION1_14);
    (new RecipeRewriter1_14((Protocol)this.protocol, this::handleItemToClient)).registerDefaultHandler((ClientboundPacketType)ClientboundPackets1_15.DECLARE_RECIPES);
    ((Protocol1_14_4To1_15)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_14.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> BlockItemPackets1_15.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
          }
        });
    itemRewriter.registerSetCooldown((ClientboundPacketType)ClientboundPackets1_15.COOLDOWN);
    itemRewriter.registerWindowItems((ClientboundPacketType)ClientboundPackets1_15.WINDOW_ITEMS, Type.FLAT_VAR_INT_ITEM_ARRAY);
    itemRewriter.registerSetSlot((ClientboundPacketType)ClientboundPackets1_15.SET_SLOT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerTradeList((ClientboundPacketType)ClientboundPackets1_15.TRADE_LIST, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerEntityEquipment((ClientboundPacketType)ClientboundPackets1_15.ENTITY_EQUIPMENT, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerAdvancements((ClientboundPacketType)ClientboundPackets1_15.ADVANCEMENTS, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_14.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_14.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_15.ACKNOWLEDGE_PLAYER_DIGGING);
    blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_15.BLOCK_ACTION);
    blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_15.BLOCK_CHANGE);
    blockRewriter.registerMultiBlockChange((ClientboundPacketType)ClientboundPackets1_15.MULTI_BLOCK_CHANGE);
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_15Type());
                    wrapper.write((Type)new Chunk1_14Type(), chunk);
                    if (chunk.isFullChunk()) {
                      int[] biomeData = chunk.getBiomeData();
                      int[] newBiomeData = new int[256];
                      for (int j = 0; j < 4; j++) {
                        for (int k = 0; k < 4; k++) {
                          int x = k << 2;
                          int z = j << 2;
                          int newIndex = z << 4 | x;
                          int oldIndex = j << 2 | k;
                          int biome = biomeData[oldIndex];
                          for (int m = 0; m < 4; m++) {
                            int offX = newIndex + (m << 4);
                            for (int l = 0; l < 4; l++)
                              newBiomeData[offX + l] = biome; 
                          } 
                        } 
                      } 
                      chunk.setBiomeData(newBiomeData);
                    } 
                    for (int i = 0; i < (chunk.getSections()).length; i++) {
                      ChunkSection section = chunk.getSections()[i];
                      if (section != null)
                        for (int j = 0; j < section.getPaletteSize(); j++) {
                          int old = section.getPaletteEntry(j);
                          int newId = ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewBlockStateId(old);
                          section.setPaletteEntry(j, newId);
                        }  
                    } 
                  }
                });
          }
        });
    blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_15.EFFECT, 1010, 2001);
    ((Protocol1_14_4To1_15)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.SPAWN_PARTICLE, new PacketRemapper() {
          public void registerMap() {
            map(Type.INT);
            map(Type.BOOLEAN);
            map(Type.DOUBLE, (Type)Type.FLOAT);
            map(Type.DOUBLE, (Type)Type.FLOAT);
            map(Type.DOUBLE, (Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map(Type.INT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int id = ((Integer)wrapper.get(Type.INT, 0)).intValue();
                    if (id == 3 || id == 23) {
                      int data = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                      wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewBlockStateId(data)));
                    } else if (id == 32) {
                      Item item = BlockItemPackets1_15.this.handleItemToClient((Item)wrapper.read(Type.FLAT_VAR_INT_ITEM));
                      wrapper.write(Type.FLAT_VAR_INT_ITEM, item);
                    } 
                    int mappedId = ((Protocol1_14_4To1_15)BlockItemPackets1_15.this.protocol).getMappingData().getNewParticleId(id);
                    if (id != mappedId)
                      wrapper.set(Type.INT, 0, Integer.valueOf(mappedId)); 
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14_4to1_15\packets\BlockItemPackets1_15.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */