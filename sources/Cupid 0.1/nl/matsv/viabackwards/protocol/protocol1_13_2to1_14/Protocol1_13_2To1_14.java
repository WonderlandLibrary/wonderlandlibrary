package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.BlockItemPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.EntityPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.PlayerPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets.SoundPackets1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.ChunkLightStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.Protocol1_14To1_13_2;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;

public class Protocol1_13_2To1_14 extends BackwardsProtocol<ClientboundPackets1_14, ClientboundPackets1_13, ServerboundPackets1_14, ServerboundPackets1_13> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.14", "1.13.2", Protocol1_14To1_13_2.class, true);
  
  private BlockItemPackets1_14 blockItemPackets;
  
  private EntityPackets1_14 entityPackets;
  
  public Protocol1_13_2To1_14() {
    super(ClientboundPackets1_14.class, ClientboundPackets1_13.class, ServerboundPackets1_14.class, ServerboundPackets1_13.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_14To1_13_2.class, MAPPINGS::load);
    TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
    translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_14.BOSSBAR);
    translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_14.CHAT_MESSAGE);
    translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_14.COMBAT_EVENT);
    translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_14.DISCONNECT);
    translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_14.TAB_LIST);
    translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_14.TITLE);
    translatableRewriter.registerPing();
    this.blockItemPackets = new BlockItemPackets1_14(this, translatableRewriter);
    this.blockItemPackets.register();
    this.entityPackets = new EntityPackets1_14(this);
    this.entityPackets.register();
    (new PlayerPackets1_14(this)).register();
    (new SoundPackets1_14(this)).register();
    (new StatisticsRewriter((Protocol)this, this.entityPackets::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_14.STATISTICS);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_VIEW_POSITION);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_VIEW_DISTANCE);
    cancelOutgoing((ClientboundPacketType)ClientboundPackets1_14.ACKNOWLEDGE_PLAYER_DIGGING);
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.TAGS, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int blockTagsSize = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int i = 0; i < blockTagsSize; i++) {
                      wrapper.passthrough(Type.STRING);
                      int[] blockIds = (int[])wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                      for (int n = 0; n < blockIds.length; n++) {
                        int id = blockIds[n];
                        int blockId = Protocol1_13_2To1_14.this.getMappingData().getNewBlockId(id);
                        blockIds[n] = blockId;
                      } 
                    } 
                    int itemTagsSize = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int j = 0; j < itemTagsSize; j++) {
                      wrapper.passthrough(Type.STRING);
                      int[] itemIds = (int[])wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                      for (int n = 0; n < itemIds.length; n++) {
                        int itemId = itemIds[n];
                        int oldId = Protocol1_13_2To1_14.this.getMappingData().getItemMappings().get(itemId);
                        itemIds[n] = oldId;
                      } 
                    } 
                    int fluidTagsSize = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                    for (int k = 0; k < fluidTagsSize; k++) {
                      wrapper.passthrough(Type.STRING);
                      wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                    } 
                    int entityTagsSize = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    for (int m = 0; m < entityTagsSize; m++) {
                      wrapper.read(Type.STRING);
                      wrapper.read(Type.VAR_INT_ARRAY_PRIMITIVE);
                    } 
                  }
                });
          }
        });
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.UPDATE_LIGHT, null, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int x = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int z = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int skyLightMask = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int blockLightMask = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int emptySkyLightMask = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    int emptyBlockLightMask = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    byte[][] skyLight = new byte[16][];
                    if (isSet(skyLightMask, 0))
                      wrapper.read(Type.BYTE_ARRAY_PRIMITIVE); 
                    for (int i = 0; i < 16; i++) {
                      if (isSet(skyLightMask, i + 1)) {
                        skyLight[i] = (byte[])wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                      } else if (isSet(emptySkyLightMask, i + 1)) {
                        skyLight[i] = ChunkLightStorage.EMPTY_LIGHT;
                      } 
                    } 
                    if (isSet(skyLightMask, 17))
                      wrapper.read(Type.BYTE_ARRAY_PRIMITIVE); 
                    byte[][] blockLight = new byte[16][];
                    if (isSet(blockLightMask, 0))
                      wrapper.read(Type.BYTE_ARRAY_PRIMITIVE); 
                    for (int j = 0; j < 16; j++) {
                      if (isSet(blockLightMask, j + 1)) {
                        blockLight[j] = (byte[])wrapper.read(Type.BYTE_ARRAY_PRIMITIVE);
                      } else if (isSet(emptyBlockLightMask, j + 1)) {
                        blockLight[j] = ChunkLightStorage.EMPTY_LIGHT;
                      } 
                    } 
                    if (isSet(blockLightMask, 17))
                      wrapper.read(Type.BYTE_ARRAY_PRIMITIVE); 
                    ((ChunkLightStorage)wrapper.user().get(ChunkLightStorage.class)).setStoredLight(skyLight, blockLight, x, z);
                    wrapper.cancel();
                  }
                  
                  private boolean isSet(int mask, int i) {
                    return ((mask & 1 << i) != 0);
                  }
                });
          }
        });
  }
  
  public void init(UserConnection user) {
    if (!user.has(ClientWorld.class))
      user.put((StoredObject)new ClientWorld(user)); 
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
    if (!user.has(ChunkLightStorage.class))
      user.put((StoredObject)new ChunkLightStorage(user)); 
  }
  
  public BlockItemPackets1_14 getBlockItemPackets() {
    return this.blockItemPackets;
  }
  
  public EntityPackets1_14 getEntityPackets() {
    return this.entityPackets;
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\Protocol1_13_2To1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */