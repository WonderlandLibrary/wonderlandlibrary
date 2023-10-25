package nl.matsv.viabackwards.protocol.protocol1_16_4to1_17;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.data.BackwardsMappings;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_16_4to1_17.packets.BlockItemPackets1_17;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.MappingData;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.StatisticsRewriter;
import us.myles.ViaVersion.api.rewriters.TagRewriter;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ClientboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_16_2to1_16_1.ServerboundPackets1_16_2;
import us.myles.ViaVersion.protocols.protocol1_17to1_16_4.Protocol1_17To1_16_4;

public class Protocol1_16_4To1_17 extends BackwardsProtocol<ClientboundPackets1_16_2, ClientboundPackets1_16_2, ServerboundPackets1_16_2, ServerboundPackets1_16_2> {
  public static final BackwardsMappings MAPPINGS = new BackwardsMappings("1.17", "1.16.2", Protocol1_17To1_16_4.class, true);
  
  public Protocol1_16_4To1_17() {
    super(ClientboundPackets1_16_2.class, ClientboundPackets1_16_2.class, ServerboundPackets1_16_2.class, ServerboundPackets1_16_2.class);
  }
  
  protected void registerPackets() {
    executeAsyncAfterLoaded(Protocol1_17To1_16_4.class, MAPPINGS::load);
    (new BlockItemPackets1_17(this, null)).register();
    SoundRewriter soundRewriter = new SoundRewriter(this);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.SOUND);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_16_2.ENTITY_SOUND);
    soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_16_2.NAMED_SOUND);
    soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_16_2.STOP_SOUND);
    (new TagRewriter((Protocol)this, null)).register((ClientboundPacketType)ClientboundPackets1_16_2.TAGS);
    (new StatisticsRewriter((Protocol)this, null)).register((ClientboundPacketType)ClientboundPackets1_16_2.STATISTICS);
    registerOutgoing((ClientboundPacketType)ClientboundPackets1_16_2.RESOURCE_PACK, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.passthrough(Type.STRING);
                  wrapper.passthrough(Type.STRING);
                  wrapper.read(Type.BOOLEAN);
                });
          }
        });
  }
  
  public BackwardsMappings getMappingData() {
    return MAPPINGS;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_4to1_17\Protocol1_16_4To1_17.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */