package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.packets;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.rewriters.Rewriter;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;
import nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.data.NamedSoundMapping;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_12_1to1_12.ClientboundPackets1_12_1;
import us.myles.ViaVersion.protocols.protocol1_13to1_12_2.ClientboundPackets1_13;

public class SoundPackets1_13 extends Rewriter<Protocol1_12_2To1_13> {
  private static final String[] SOUND_SOURCES = new String[] { "master", "music", "record", "weather", "block", "hostile", "neutral", "player", "ambient", "voice" };
  
  public SoundPackets1_13(Protocol1_12_2To1_13 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.NAMED_SOUND, new PacketRemapper() {
          public void registerMap() {
            map(Type.STRING);
            handler(wrapper -> {
                  String newSound = (String)wrapper.get(Type.STRING, 0);
                  if (newSound.startsWith("minecraft:"))
                    newSound = newSound.substring(10); 
                  String oldSound = NamedSoundMapping.getOldId(newSound);
                  if (oldSound != null || (oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound)) != null) {
                    wrapper.set(Type.STRING, 0, oldSound);
                  } else if (!Via.getConfig().isSuppressConversionWarnings()) {
                    ViaBackwards.getPlatform().getLogger().warning("Unknown named sound in 1.13->1.12 protocol: " + newSound);
                  } 
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.STOP_SOUND, (ClientboundPacketType)ClientboundPackets1_12_1.PLUGIN_MESSAGE, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  String source;
                  String sound;
                  wrapper.write(Type.STRING, "MC|StopSound");
                  byte flags = ((Byte)wrapper.read(Type.BYTE)).byteValue();
                  if ((flags & 0x1) != 0) {
                    source = SoundPackets1_13.SOUND_SOURCES[((Integer)wrapper.read((Type)Type.VAR_INT)).intValue()];
                  } else {
                    source = "";
                  } 
                  if ((flags & 0x2) != 0) {
                    String newSound = (String)wrapper.read(Type.STRING);
                    if (newSound.startsWith("minecraft:"))
                      newSound = newSound.substring(10); 
                    sound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getMappedNamedSound(newSound);
                    if (sound == null)
                      sound = ""; 
                  } else {
                    sound = "";
                  } 
                  wrapper.write(Type.STRING, source);
                  wrapper.write(Type.STRING, sound);
                });
          }
        });
    ((Protocol1_12_2To1_13)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_13.SOUND, new PacketRemapper() {
          public void registerMap() {
            map((Type)Type.VAR_INT);
            handler(wrapper -> {
                  int newSound = ((Integer)wrapper.get((Type)Type.VAR_INT, 0)).intValue();
                  int oldSound = ((Protocol1_12_2To1_13)SoundPackets1_13.this.protocol).getMappingData().getSoundMappings().getNewId(newSound);
                  if (oldSound == -1) {
                    wrapper.cancel();
                  } else {
                    wrapper.set((Type)Type.VAR_INT, 0, Integer.valueOf(oldSound));
                  } 
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\packets\SoundPackets1_13.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */