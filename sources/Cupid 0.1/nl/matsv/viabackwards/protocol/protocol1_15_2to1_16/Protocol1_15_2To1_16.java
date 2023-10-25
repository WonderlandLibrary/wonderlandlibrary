package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16;

import java.util.UUID;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat.TranslatableRewriter1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.data.BackwardsMappings;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.BlockItemPackets1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.packets.EntityPackets1_16;
import nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.storage.PlayerSneakStorage;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.packets.State;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.Protocol1_16To1_15_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import us.myles.ViaVersion.util.GsonUtil;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;

public class Protocol1_15_2To1_16 extends BackwardsProtocol<ClientboundPackets1_16, ClientboundPackets1_15, ServerboundPackets1_16, ServerboundPackets1_14> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings();
  
  private BlockItemPackets1_16 blockItemPackets;
  
  private TranslatableRewriter translatableRewriter;
  
  public Protocol1_15_2To1_16() {
    super(ClientboundPackets1_16.class, ClientboundPackets1_15.class, ServerboundPackets1_16.class, ServerboundPackets1_14.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_16To1_15_2.class, MAPPINGS::load);
    this.translatableRewriter = (TranslatableRewriter)new TranslatableRewriter1_16(this);
    this.translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_16.BOSSBAR);
    this.translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_16.COMBAT_EVENT);
    this.translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_16.DISCONNECT);
    this.translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_16.TAB_LIST);
    this.translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_16.TITLE);
    this.translatableRewriter.registerPing();
    (this.blockItemPackets = new BlockItemPackets1_16(this, this.translatableRewriter)).register();
    EntityPackets1_16 entityPackets = new EntityPackets1_16(this);
    entityPackets.register();
    registerOutgoing(State.STATUS, 0, 0, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  String original = (String)wrapper.passthrough(Type.STRING);
                  JsonObject object = (JsonObject)GsonUtil.getGson().fromJson(original, JsonObject.class);
                  JsonElement description = object.get("description");
                  if (description == null)
                    return; 
                  Protocol1_15_2To1_16.this.translatableRewriter.processText(description);
                  wrapper.set(Type.STRING, 0, object.toString());
                });
          }
        });
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.CHAT_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            map(Type.BYTE);
            map(Type.UUID, Type.NOTHING);
          }
        });
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_16.OPEN_WINDOW, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            handler(wrapper -> Protocol1_15_2To1_16.this.translatableRewriter.processText((JsonElement)wrapper.passthrough(Type.COMPONENT)));
            handler(wrapper -> {
                  int windowType = ((Integer)wrapper.get((Type)Type.VAR_INT, 1)).intValue();
                  if (windowType == 20) {
                    wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(7));
                  } else if (windowType > 20) {
                    wrapper.set((Type)Type.VAR_INT, 1, Integer.valueOf(--windowType));
                  } 
                });
          }
        });
    SoundRewriter soundRewriter = new SoundRewriter(this);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16.SOUND);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16.ENTITY_SOUND);
    soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16.NAMED_SOUND);
    soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16.STOP_SOUND);
    registerOutgoing(State.LOGIN, 2, 2, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  UUID uuid = (UUID)wrapper.read(Type.UUID_INT_ARRAY);
                  wrapper.write(Type.STRING, uuid.toString());
                });
          }
        });
    (new TagRewriter((Protocol)this, entityPackets::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_16.TAGS);
    (new StatisticsRewriter((Protocol)this, entityPackets::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_16.STATISTICS);
    registerIncoming((ServerboundPacketType)ServerboundPackets1_14.ENTITY_ACTION, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough((Type)Type.VAR_INT);
                  int action = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  if (action == 0) {
                    ((PlayerSneakStorage)wrapper.user().get(PlayerSneakStorage.class)).setSneaking(true);
                  } else if (action == 1) {
                    ((PlayerSneakStorage)wrapper.user().get(PlayerSneakStorage.class)).setSneaking(false);
                  } 
                });
          }
        });
    registerIncoming((ServerboundPacketType)ServerboundPackets1_14.INTERACT_ENTITY, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough((Type)Type.VAR_INT);
                  int action = ((Integer)wrapper.passthrough((Type)Type.VAR_INT)).intValue();
                  if (action == 0 || action == 2) {
                    if (action == 2) {
                      wrapper.passthrough((Type)Type.FLOAT);
                      wrapper.passthrough((Type)Type.FLOAT);
                      wrapper.passthrough((Type)Type.FLOAT);
                    } 
                    wrapper.passthrough((Type)Type.VAR_INT);
                  } 
                  wrapper.write(Type.BOOLEAN, Boolean.valueOf(((PlayerSneakStorage)wrapper.user().get(PlayerSneakStorage.class)).isSneaking()));
                });
          }
        });
    registerIncoming((ServerboundPacketType)ServerboundPackets1_14.PLAYER_ABILITIES, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  byte flags = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                  flags = (byte)(flags & 0x2);
                  wrapper.write(Type.BYTE, Byte.valueOf(flags));
                  wrapper.read((Type)Type.FLOAT);
                  wrapper.read((Type)Type.FLOAT);
                });
          }
        });
    cancelIncoming((ServerboundPacketType)ServerboundPackets1_14.UPDATE_JIGSAW_BLOCK);
  }
  
  public void init(UserConnection user) {
    if (!user.has(ClientWorld.class))
      user.put((StoredObject)new ClientWorld(user)); 
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    user.put((StoredObject)new PlayerSneakStorage(user));
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
  }
  
  public BlockItemPackets1_16 getBlockItemPackets() {
    return this.blockItemPackets;
  }
  
  public TranslatableRewriter getTranslatableRewriter() {
    return this.translatableRewriter;
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\Protocol1_15_2To1_16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */