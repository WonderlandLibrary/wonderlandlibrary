package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.ItemRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.Protocol1_16_1To1_16_2;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord;
import us.myles.ViaVersion.api.minecraft.BlockChangeRecord1_8;
import us.myles.ViaVersion.api.minecraft.Position;
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
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.types.Chunk1_16_2Type;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.data.RecipeRewriter1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.types.Chunk1_16Type;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntArrayTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.IntTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.StringTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class BlockItemPackets1_16_2 extends ItemRewriter<Protocol1_16_1To1_16_2> {
  public BlockItemPackets1_16_2(Protocol1_16_1To1_16_2 protocol, TranslatableRewriter translatableRewriter) {
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
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.UNLOCK_RECIPES, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough((Type)Type.VAR_INT);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.passthrough(Type.BOOLEAN);
                  wrapper.read(Type.BOOLEAN);
                  wrapper.read(Type.BOOLEAN);
                  wrapper.read(Type.BOOLEAN);
                  wrapper.read(Type.BOOLEAN);
                });
          }
        });
    blockRewriter.registerAcknowledgePlayerDigging((ClientboundPacketType)ClientboundPackets1_16_2.ACKNOWLEDGE_PLAYER_DIGGING);
    blockRewriter.registerBlockAction((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_ACTION);
    blockRewriter.registerBlockChange((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_CHANGE);
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.CHUNK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Chunk chunk = (Chunk)wrapper.read((Type)new Chunk1_16_2Type());
                  wrapper.write((Type)new Chunk1_16Type(), chunk);
                  chunk.setIgnoreOldLightData(true);
                  for (int i = 0; i < (chunk.getSections()).length; i++) {
                    ChunkSection section = chunk.getSections()[i];
                    if (section != null)
                      for (int j = 0; j < section.getPaletteSize(); j++) {
                        int old = section.getPaletteEntry(j);
                        section.setPaletteEntry(j, ((Protocol1_16_1To1_16_2)BlockItemPackets1_16_2.this.protocol).getMappingData().getNewBlockStateId(old));
                      }  
                  } 
                  for (CompoundTag blockEntity : chunk.getBlockEntities()) {
                    if (blockEntity == null)
                      continue; 
                    IntTag x = (IntTag)blockEntity.get("x");
                    IntTag y = (IntTag)blockEntity.get("y");
                    IntTag z = (IntTag)blockEntity.get("z");
                    if (x != null && y != null && z != null)
                      BlockItemPackets1_16_2.this.handleBlockEntity(blockEntity, new Position(x.getValue().intValue(), y.getValue().shortValue(), z.getValue().intValue())); 
                  } 
                });
          }
        });
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.BLOCK_ENTITY_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  Position position = (Position)wrapper.passthrough(Type.POSITION1_14);
                  wrapper.passthrough(Type.UNSIGNED_BYTE);
                  BlockItemPackets1_16_2.this.handleBlockEntity((CompoundTag)wrapper.passthrough(Type.NBT), position);
                });
          }
        });
    ((Protocol1_16_1To1_16_2)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.MULTI_BLOCK_CHANGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  long chunkPosition = ((Long)wrapper.read(Type.LONG)).longValue();
                  wrapper.read(Type.BOOLEAN);
                  int chunkX = (int)(chunkPosition >> 42L);
                  int chunkY = (int)(chunkPosition << 44L >> 44L);
                  int chunkZ = (int)(chunkPosition << 22L >> 42L);
                  wrapper.write(Type.INT, Integer.valueOf(chunkX));
                  wrapper.write(Type.INT, Integer.valueOf(chunkZ));
                  BlockChangeRecord[] blockChangeRecord = (BlockChangeRecord[])wrapper.read(Type.VAR_LONG_BLOCK_CHANGE_RECORD_ARRAY);
                  wrapper.write(Type.BLOCK_CHANGE_RECORD_ARRAY, blockChangeRecord);
                  for (int i = 0; i < blockChangeRecord.length; i++) {
                    BlockChangeRecord record = blockChangeRecord[i];
                    int blockId = ((Protocol1_16_1To1_16_2)BlockItemPackets1_16_2.this.protocol).getMappingData().getNewBlockStateId(record.getBlockId());
                    blockChangeRecord[i] = (BlockChangeRecord)new BlockChangeRecord1_8(record.getSectionX(), record.getY(chunkY), record.getSectionZ(), blockId);
                  } 
                });
          }
        });
    blockRewriter.registerEffect((ClientboundPacketType)ClientboundPackets1_16_2.EFFECT, 1010, 2001);
    itemRewriter.registerSpawnParticle((ClientboundPacketType)ClientboundPackets1_16_2.SPAWN_PARTICLE, Type.FLAT_VAR_INT_ITEM, Type.DOUBLE);
    itemRewriter.registerClickWindow((ServerboundPacketType)ServerboundPackets1_16.CLICK_WINDOW, Type.FLAT_VAR_INT_ITEM);
    itemRewriter.registerCreativeInvAction((ServerboundPacketType)ServerboundPackets1_16.CREATIVE_INVENTORY_ACTION, Type.FLAT_VAR_INT_ITEM);
    ((Protocol1_16_1To1_16_2)this.protocol).registerIncoming((ServerboundPacketType)ServerboundPackets1_16.EDIT_BOOK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> BlockItemPackets1_16_2.this.handleItemToServer((Item)wrapper.passthrough(Type.FLAT_VAR_INT_ITEM)));
          }
        });
  }
  
  private void handleBlockEntity(CompoundTag tag, Position position) {
    StringTag idTag = (StringTag)tag.get("id");
    if (idTag == null)
      return; 
    if (idTag.getValue().equals("minecraft:skull")) {
      Tag skullOwnerTag = tag.get("SkullOwner");
      if (!(skullOwnerTag instanceof CompoundTag))
        return; 
      CompoundTag skullOwnerCompoundTag = (CompoundTag)skullOwnerTag;
      if (!skullOwnerCompoundTag.contains("Id"))
        return; 
      CompoundTag properties = (CompoundTag)skullOwnerCompoundTag.get("Properties");
      if (properties == null)
        return; 
      ListTag textures = (ListTag)properties.get("textures");
      if (textures == null)
        return; 
      CompoundTag first = (textures.size() > 0) ? (CompoundTag)textures.get(0) : null;
      if (first == null)
        return; 
      int hashCode = first.get("Value").getValue().hashCode();
      int[] uuidIntArray = { hashCode, 0, 0, 0 };
      skullOwnerCompoundTag.put((Tag)new IntArrayTag("Id", uuidIntArray));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_1to1_16_2\packets\BlockItemPackets1_16_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */