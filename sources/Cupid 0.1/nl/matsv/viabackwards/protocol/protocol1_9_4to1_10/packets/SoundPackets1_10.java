package nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.packets;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.LegacySoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_9_4to1_10.Protocol1_9_4To1_10;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketHandler;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.remapper.ValueTransformer;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9_3to1_9_1_2.ClientboundPackets1_9_3;

public class SoundPackets1_10 extends LegacySoundRewriter<Protocol1_9_4To1_10> {
  protected static ValueTransformer<Float, Short> toOldPitch = new ValueTransformer<Float, Short>(Type.UNSIGNED_BYTE) {
      public Short transform(PacketWrapper packetWrapper, Float inputValue) throws Exception {
        return Short.valueOf((short)Math.round(inputValue.floatValue() * 63.5F));
      }
    };
  
  public SoundPackets1_10(Protocol1_9_4To1_10 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.NAMED_SOUND, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            map((Type)Type.VAR_INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT, SoundPackets1_10.toOldPitch);
          }
        });
    ((Protocol1_9_4To1_10)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_9_3.SOUND, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            map((Type)Type.VAR_INT);
            map(Type.INT);
            map(Type.INT);
            map(Type.INT);
            map((Type)Type.FLOAT);
            map((Type)Type.FLOAT, SoundPackets1_10.toOldPitch);
            handler(new PacketHandler() {
                  public void handle(PacketWrapper wrapper) throws Exception {
                    int oldId = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                    int newId = SoundPackets1_10.this.handleSounds(oldId);
                    if (newId == -1) {
                      wrapper.cancel();
                      return;
                    } 
                    if (SoundPackets1_10.this.hasPitch(oldId))
                      wrapper.set(Type.UNSIGNED_BYTE, 0, Short.valueOf((short)Math.round(SoundPackets1_10.this.handlePitch(oldId) * 63.5F))); 
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(newId));
                  }
                });
          }
        });
  }
  
  protected void registerRewrites() {
    added(24, -1);
    added(249, 381);
    added(250, 385);
    added(251, 386);
    added(252, 388);
    added(301, 381, 0.6F);
    added(302, 381, 1.9F);
    added(303, 385, 0.7F);
    added(304, 309, 0.6F);
    added(305, 240, 0.6F);
    added(306, 374, 1.2F);
    added(365, 320);
    added(366, 321);
    added(367, 322);
    added(368, 324);
    added(387, 320);
    added(388, 321);
    added(389, 322);
    added(390, 324);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_9_4to1_10\packets\SoundPackets1_10.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */