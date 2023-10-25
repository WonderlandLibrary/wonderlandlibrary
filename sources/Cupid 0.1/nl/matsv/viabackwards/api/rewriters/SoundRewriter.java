package nl.matsv.viabackwards.api.rewriters;

import nl.matsv.viabackwards.api.BackwardsProtocol;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.protocol.Protocol;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.rewriters.SoundRewriter;
import us.myles.ViaVersion.api.type.Type;

public class SoundRewriter extends SoundRewriter {
  private final BackwardsProtocol protocol;
  
  public SoundRewriter(BackwardsProtocol protocol) {
    super((Protocol)protocol);
    this.protocol = protocol;
  }
  
  public void registerNamedSound(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(wrapper -> {
                  String soundId = (String)wrapper.get(Type.STRING, 0);
                  if (soundId.startsWith("minecraft:"))
                    soundId = soundId.substring(10); 
                  String mappedId = SoundRewriter.this.protocol.getMappingData().getMappedNamedSound(soundId);
                  if (mappedId == null)
                    return; 
                  if (!mappedId.isEmpty()) {
                    wrapper.set(Type.STRING, 0, mappedId);
                  } else {
                    wrapper.cancel();
                  } 
                });
          }
        });
  }
  
  public void registerStopSound(ClientboundPacketType packetType) {
    this.protocol.registerOutgoing(packetType, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  byte flags = ((Byte)wrapper.passthrough(Type.BYTE)).byteValue();
                  if ((flags & 0x2) == 0)
                    return; 
                  if ((flags & 0x1) != 0)
                    wrapper.passthrough((Type)Type.VAR_INT); 
                  String soundId = (String)wrapper.read(Type.STRING);
                  if (soundId.startsWith("minecraft:"))
                    soundId = soundId.substring(10); 
                  String mappedId = SoundRewriter.this.protocol.getMappingData().getMappedNamedSound(soundId);
                  if (mappedId == null) {
                    wrapper.write(Type.STRING, soundId);
                    return;
                  } 
                  if (!mappedId.isEmpty()) {
                    wrapper.write(Type.STRING, mappedId);
                  } else {
                    wrapper.cancel();
                  } 
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\rewriters\SoundRewriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */