package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.LegacySoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_12to1_11_1.ClientboundPackets1_12;

public class SoundPackets1_12 extends LegacySoundRewriter<Protocol1_11_1To1_12> {
  public SoundPackets1_12(Protocol1_11_1To1_12 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.NAMED_SOUND, new PacketRemapper() {
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
    ((Protocol1_11_1To1_12)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_12.SOUND, new PacketRemapper() {
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
                    int newId = SoundPackets1_12.this.handleSounds(oldId);
                    if (newId == -1) {
                      wrapper.cancel();
                      return;
                    } 
                    if (SoundPackets1_12.this.hasPitch(oldId))
                      wrapper.set((Type)Type.FLOAT, 1, Float.valueOf(SoundPackets1_12.this.handlePitch(oldId))); 
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newId));
                  }
                });
          }
        });
  }
  
  protected void registerRewrites() {
    added(26, 277, 1.4F);
    added(27, -1);
    added(72, 70);
    added(73, 70);
    added(74, 70);
    added(75, 70);
    added(80, 70);
    added(150, -1);
    added(151, -1);
    added(152, -1);
    added(195, -1);
    added(274, 198, 0.8F);
    added(275, 199, 0.8F);
    added(276, 200, 0.8F);
    added(277, 201, 0.8F);
    added(278, 191, 0.9F);
    added(279, 203, 1.5F);
    added(280, 202, 0.8F);
    added(319, 133, 0.6F);
    added(320, 134, 1.7F);
    added(321, 219, 1.5F);
    added(322, 136, 0.7F);
    added(323, 135, 1.6F);
    added(324, 138, 1.5F);
    added(325, 163, 1.5F);
    added(326, 170, 1.5F);
    added(327, 178, 1.5F);
    added(328, 186, 1.5F);
    added(329, 192, 1.5F);
    added(330, 198, 1.5F);
    added(331, 226, 1.5F);
    added(332, 259, 1.5F);
    added(333, 198, 1.3F);
    added(334, 291, 1.5F);
    added(335, 321, 1.5F);
    added(336, 337, 1.5F);
    added(337, 347, 1.5F);
    added(338, 351, 1.5F);
    added(339, 363, 1.5F);
    added(340, 376, 1.5F);
    added(341, 385, 1.5F);
    added(342, 390, 1.5F);
    added(343, 400, 1.5F);
    added(344, 403, 1.5F);
    added(345, 408, 1.5F);
    added(346, 414, 1.5F);
    added(347, 418, 1.5F);
    added(348, 427, 1.5F);
    added(349, 438, 1.5F);
    added(350, 442, 1.5F);
    added(351, 155);
    added(368, 316);
    added(369, 316);
    added(544, -1);
    added(545, -1);
    added(546, 317, 1.5F);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11_1to1_12\packets\SoundPackets1_12.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */