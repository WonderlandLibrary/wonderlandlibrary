package nl.matsv.viabackwards.protocol.protocol1_10to1_11.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.LegacySoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_10to1_11.Protocol1_10To1_11;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;

public class SoundPackets1_11 extends LegacySoundRewriter<Protocol1_10To1_11> {
  public SoundPackets1_11(Protocol1_10To1_11 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.NAMED_SOUND, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map((Type)Type.VAR_INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
          }
        });
    ((Protocol1_10To1_11)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SOUND, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int oldId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int newId = SoundPackets1_11.this.handleSounds(oldId);
                    if (newId == -1) {
                      wrapper.cancel();
                    } else {
                      if (SoundPackets1_11.this.hasPitch(oldId))
                        wrapper.set((Type)Type.FLOAT, 1, Float.valueOf(SoundPackets1_11.this.handlePitch(oldId))); 
                      wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newId));
                    } 
                  }
                });
          }
        });
  }
  
  protected void registerRewrites() {
    added(85, 121, 0.5F);
    added(86, 122, 0.5F);
    added(176, 227);
    this.soundRewrites.put(196, new LegacySoundRewriter.SoundData(193, false, -1.0F, false));
    added(197, 402, 1.8F);
    added(198, 370, 0.4F);
    added(199, 255, 1.3F);
    added(200, 418, 1.3F);
    added(201, 372, 1.3F);
    added(202, 137, 0.8F);
    added(203, 78, 2.0F);
    added(204, 376, 0.6F);
    added(279, 230, 1.5F);
    added(280, 231, 1.6F);
    added(281, 164);
    added(282, 165, 1.2F);
    added(283, 235, 1.1F);
    added(284, 166);
    added(285, 323, 1.7F);
    added(286, 241, 0.8F);
    added(287, 423, 0.5F);
    added(296, 164);
    added(390, 233, 0.1F);
    added(391, 168, 2.0F);
    added(392, 144, 0.5F);
    added(393, 146, 2.0F);
    added(400, 370, 0.7F);
    added(401, 371, 0.8F);
    added(402, 372, 0.7F);
    added(450, 423, 1.1F);
    added(455, 427, 1.1F);
    added(470, 2, 0.5F);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\packets\SoundPackets1_11.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */