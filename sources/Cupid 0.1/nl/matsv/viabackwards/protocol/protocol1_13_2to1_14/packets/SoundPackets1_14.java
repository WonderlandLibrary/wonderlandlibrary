package nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.packets;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.api.BackwardsProtocol;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.rewriters.Rewriter;
import nl.matsv.viabackwards.api.rewriters.SoundRewriter;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import nl.matsv.viabackwards.protocol.protocol1_13_2to1_14.storage.EntityPositionStorage1_14;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.protocol.ClientboundPacketType;
import us.myles.ViaVersion.api.remapper.PacketRemapper;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;

public class SoundPackets1_14 extends Rewriter<Protocol1_13_2To1_14> {
  public SoundPackets1_14(Protocol1_13_2To1_14 protocol) {
    super((BackwardsProtocol)protocol);
  }
  
  protected void registerPackets() {
    SoundRewriter soundRewriter = new SoundRewriter(this.protocol);
    soundRewriter.registerSound((ClientboundPacketType)ClientboundPackets1_14.SOUND);
    soundRewriter.registerNamedSound((ClientboundPacketType)ClientboundPackets1_14.NAMED_SOUND);
    soundRewriter.registerStopSound((ClientboundPacketType)ClientboundPackets1_14.STOP_SOUND);
    ((Protocol1_13_2To1_14)this.protocol).registerOutgoing((ClientboundPacketType)ClientboundPackets1_14.ENTITY_SOUND, null, new PacketRemapper() {
          public void registerMap() {
            handler(wrapper -> {
                  wrapper.cancel();
                  int soundId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                  int newId = ((Protocol1_13_2To1_14)SoundPackets1_14.this.protocol).getMappingData().getSoundMappings().getNewId(soundId);
                  if (newId == -1)
                    return; 
                  int category = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                  int entityId = ((Integer)wrapper.read((Type)Type.VAR_INT)).intValue();
                  EntityTracker.StoredEntity storedEntity = ((EntityTracker)wrapper.user().get(EntityTracker.class)).get(SoundPackets1_14.this.protocol).getEntity(entityId);
                  EntityPositionStorage1_14 entityStorage;
                  if (storedEntity == null || (entityStorage = (EntityPositionStorage1_14)storedEntity.get(EntityPositionStorage1_14.class)) == null) {
                    ViaBackwards.getPlatform().getLogger().warning("Untracked entity with id " + entityId);
                    return;
                  } 
                  float volume = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
                  float pitch = ((Float)wrapper.read((Type)Type.FLOAT)).floatValue();
                  int x = (int)(entityStorage.getX() * 8.0D);
                  int y = (int)(entityStorage.getY() * 8.0D);
                  int z = (int)(entityStorage.getZ() * 8.0D);
                  PacketWrapper soundPacket = wrapper.create(77);
                  soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(newId));
                  soundPacket.write((Type)Type.VAR_INT, Integer.valueOf(category));
                  soundPacket.write(Type.INT, Integer.valueOf(x));
                  soundPacket.write(Type.INT, Integer.valueOf(y));
                  soundPacket.write(Type.INT, Integer.valueOf(z));
                  soundPacket.write((Type)Type.FLOAT, Float.valueOf(volume));
                  soundPacket.write((Type)Type.FLOAT, Float.valueOf(pitch));
                  soundPacket.send(Protocol1_13_2To1_14.class);
                });
          }
        });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_13_2to1_14\packets\SoundPackets1_14.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */