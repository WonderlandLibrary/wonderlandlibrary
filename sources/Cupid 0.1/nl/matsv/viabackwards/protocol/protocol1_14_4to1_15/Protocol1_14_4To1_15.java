package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.api.rewriters.TranslatableRewriter;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.EntityTypeMapping;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data.ImmediateRespawn;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.BlockItemPackets1_15;
import nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.packets.EntityPackets1_15;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ServerboundPackets1_14;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.ClientboundPackets1_15;
import us.myles.ViaVersion.protocols.protocol1_15to1_14_4.Protocol1_15To1_14_4;

public class Protocol1_14_4To1_15 extends BackwardsProtocol<ClientboundPackets1_15, ClientboundPackets1_14, ServerboundPackets1_14, ServerboundPackets1_14> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.15", "1.14", Protocol1_15To1_14_4.class, true);
  
  private BlockItemPackets1_15 blockItemPackets;
  
  public Protocol1_14_4To1_15() {
    super(ClientboundPackets1_15.class, ClientboundPackets1_14.class, ServerboundPackets1_14.class, ServerboundPackets1_14.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_15To1_14_4.class, MAPPINGS::load);
    TranslatableRewriter translatableRewriter = new TranslatableRewriter(this);
    translatableRewriter.registerBossBar((ClientboundPacketType)ClientboundPackets1_15.BOSSBAR);
    translatableRewriter.registerChatMessage((ClientboundPacketType)ClientboundPackets1_15.CHAT_MESSAGE);
    translatableRewriter.registerCombatEvent((ClientboundPacketType)ClientboundPackets1_15.COMBAT_EVENT);
    translatableRewriter.registerDisconnect((ClientboundPacketType)ClientboundPackets1_15.DISCONNECT);
    translatableRewriter.registerOpenWindow((ClientboundPacketType)ClientboundPackets1_15.OPEN_WINDOW);
    translatableRewriter.registerTabList((ClientboundPacketType)ClientboundPackets1_15.TAB_LIST);
    translatableRewriter.registerTitle((ClientboundPacketType)ClientboundPackets1_15.TITLE);
    translatableRewriter.registerPing();
    (this.blockItemPackets = new BlockItemPackets1_15(this, translatableRewriter)).register();
    (new EntityPackets1_15(this)).register();
    SoundRewriter soundRewriter = new SoundRewriter(this);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_15.SOUND);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_15.ENTITY_SOUND);
    soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_15.NAMED_SOUND);
    soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_15.STOP_SOUND);
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_15.EXPLOSION, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(wrapper -> {
                  PacketWrapper soundPacket = wrapper.create(81);
                  soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(243));
                  soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(4));
                  soundPacket.write(Type.INT, Integer.valueOf(toEffectCoordinate(((Float)wrapper.get((Type)Type.FLOAT, 0)).floatValue())));
                  soundPacket.write(Type.INT, Integer.valueOf(toEffectCoordinate(((Float)wrapper.get((Type)Type.FLOAT, 1)).floatValue())));
                  soundPacket.write(Type.INT, Integer.valueOf(toEffectCoordinate(((Float)wrapper.get((Type)Type.FLOAT, 2)).floatValue())));
                  soundPacket.write((Type)Type.FLOAT, Float.valueOf(4.0F));
                  soundPacket.write((Type)Type.FLOAT, Float.valueOf(1.0F));
                  soundPacket.send(Protocol1_14_4To1_15.class);
                });
          }
          
          private int toEffectCoordinate(float coordinate) {
            return (int)(coordinate * 8.0F);
          }
        });
    (new TagRewriter((Protocol)this, EntityTypeMapping::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_15.TAGS);
    (new StatisticsRewriter((Protocol)this, EntityTypeMapping::getOldEntityId)).register((ClientboundPacketType)ClientboundPackets1_15.STATISTICS);
  }
  
  public void init(UserConnection user) {
    if (!user.has(ImmediateRespawn.class))
      user.put((StoredObject)new ImmediateRespawn(user)); 
    if (!user.has(EntityTracker.class))
      user.put((StoredObject)new EntityTracker(user)); 
    ((EntityTracker)user.get(EntityTracker.class)).initProtocol(this);
  }
  
  public BlockItemPackets1_15 getBlockItemPackets() {
    return this.blockItemPackets;
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14_4to1_15\Protocol1_14_4To1_15.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */