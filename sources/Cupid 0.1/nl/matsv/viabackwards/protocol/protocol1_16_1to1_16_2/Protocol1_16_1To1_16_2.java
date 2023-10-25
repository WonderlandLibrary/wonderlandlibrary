package nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets.BlockItemPackets1_16_2;
import nl.matsv.viabackwards.protocol.protocol1_16_1to1_16_2.packets.EntityPackets1_16_2;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.protocol.ServerboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.Protocol1_16_2To1_16_1;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ClientboundPackets1_16;
import us.myles.ViaVersion.protocols.protocol1_16to1_15_2.ServerboundPackets1_16;
import us.myles.viaversion.libs.gson.JsonElement;

public class Protocol1_16_1To1_16_2 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16, ServerboundPackets1_16_2, ServerboundPackets1_16> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.16.2", "1.16", Protocol1_16_2To1_16_1.class, true);
  
  private BlockItemPackets1_16_2 blockItemPackets;
  
  private TranslatableRewriter translatableRewriter;
  
  public Protocol1_16_1To1_16_2() {
    super(ClientboundPackets1_16_2.class, ClientboundPackets1_16.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_16_2To1_16_1.class, MAPPINGS::load);
    this.translatableRewriter = new TranslatableRewriter(this);
    this.translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_16_2.BOSSBAR);
    this.translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_16_2.COMBAT_EVENT);
    this.translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_16_2.DISCONNECT);
    this.translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_16_2.TAB_LIST);
    this.translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_16_2.TITLE);
    this.translatableRewriter.registerOpenWindow((ClientboundPacketType)ClientboundPackets1_16_2.OPEN_WINDOW);
    this.translatableRewriter.registerPing();
    (this.blockItemPackets = new BlockItemPackets1_16_2(this, this.translatableRewriter)).register();
    EntityPackets1_16_2 entityPackets = new EntityPackets1_16_2(this);
    entityPackets.register();
    SoundRewriter soundRewriter = new SoundRewriter(this);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.SOUND);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_SOUND);
    soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16_2.NAMED_SOUND);
    soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16_2.STOP_SOUND);
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.CHAT_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  JsonElement message = (JsonElement)wrapper.passthrough(Type.COMPONENT);
                  Protocol1_16_1To1_16_2.this.translatableRewriter.processText(message);
                  byte position = ((Byte)wrapper.passthrough(Type.BYTE)).byteValue();
                  if (position == 2) {
                    wrapper.clearPacket();
                    wrapper.setId(ClientboundPackets1_16.TITLE.ordinal());
                    wrapper.write((Type)Type.VAR_INT, Integer.valueOf(2));
                    wrapper.write(Type.COMPONENT, message);
                  } 
                });
          }
        });
    registerIncoming((ServerboundPacketType)ServerboundPackets1_16.RECIPE_BOOK_DATA, new PacketRemapper() {
          public void registerMap() {
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int type = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                    if (type == 0) {
                      wrapper.passthrough(Type.STRING);
                      wrapper.setId(ServerboundPackets1_16_2.SEEN_RECIPE.ordinal());
                    } else {
                      wrapper.cancel();
                      for (int i = 0; i < 3; i++)
                        sendSeenRecipePacket(i, wrapper); 
                    } 
                  }
                  
                  private void sendSeenRecipePacket(int recipeType, PacketWrapper wrapper) throws Exception {
                    boolean open = ((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue();
                    boolean filter = ((Boolean)wrapper.read(Type.BOOLEAN)).booleanValue();
                    PacketWrapper newPacket = wrapper.create(ServerboundPackets1_16_2.RECIPE_BOOK_DATA.ordinal());
                    newPacket.write((Type)Type.VAR_INT, Integer.valueOf(recipeType));
                    newPacket.write(Type.BOOLEAN, Boolean.valueOf(open));
                    newPacket.write(Type.BOOLEAN, Boolean.valueOf(filter));
                    newPacket.sendToServer(Protocol1_16_1To1_16_2.class);
                  }
                });
          }
        });
    (new TagRewriter((Protocol)this, entityPackets::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_16_2.TAGS);
    (new StatisticsRewriter((Protocol)this, entityPackets::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_16_2.STATISTICS);
  }
  
  public void init(UserConnection user) {
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
  }
  
  public BlockItemPackets1_16_2 getBlockItemPackets() {
    return this.blockItemPackets;
  }
  
  public TranslatableRewriter getTranslatableRewriter() {
    return this.translatableRewriter;
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_1to1_16_2\Protocol1_16_1To1_16_2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */