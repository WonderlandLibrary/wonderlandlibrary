package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.protocol.protocol1_8to1_9.metadata.MetadataRewriter;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_9;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;

public class ShulkerReplacement implements EntityReplacement {
  private int entityId;
  
  public int getEntityId() {
    return this.entityId;
  }
  
  private List<Metadata> datawatcher = new ArrayList<>();
  
  private double locX;
  
  private double locY;
  
  private double locZ;
  
  private UserConnection user;
  
  public ShulkerReplacement(int entityId, UserConnection user) {
    this.entityId = entityId;
    this.user = user;
    spawn();
  }
  
  public void setLocation(double x, double y, double z) {
    this.locX = x;
    this.locY = y;
    this.locZ = z;
    updateLocation();
  }
  
  public void relMove(double x, double y, double z) {
    this.locX += x;
    this.locY += y;
    this.locZ += z;
    updateLocation();
  }
  
  public void setYawPitch(float yaw, float pitch) {}
  
  public void setHeadYaw(float yaw) {}
  
  public void updateMetadata(List<Metadata> metadataList) {
    for (Metadata metadata : metadataList) {
      this.datawatcher.removeIf(m -> (m.getId() == metadata.getId()));
      this.datawatcher.add(metadata);
    } 
    updateMetadata();
  }
  
  public void updateLocation() {
    PacketWrapper teleport = new PacketWrapper(24, null, this.user);
    teleport.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
    teleport.write(Type.BYTE, Byte.valueOf((byte)0));
    teleport.write(Type.BYTE, Byte.valueOf((byte)0));
    teleport.write(Type.BOOLEAN, Boolean.valueOf(true));
    PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
  }
  
  public void updateMetadata() {
    PacketWrapper metadataPacket = new PacketWrapper(28, null, this.user);
    metadataPacket.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    List<Metadata> metadataList = new ArrayList<>();
    for (Metadata metadata : this.datawatcher) {
      if (metadata.getId() == 11 || metadata.getId() == 12 || metadata.getId() == 13)
        continue; 
      metadataList.add(new Metadata(metadata.getId(), metadata.getMetaType(), metadata.getValue()));
    } 
    metadataList.add(new Metadata(11, (MetaType)MetaType1_9.VarInt, Integer.valueOf(2)));
    MetadataRewriter.transform(Entity1_10Types.EntityType.MAGMA_CUBE, metadataList);
    metadataPacket.write(Types1_8.METADATA_LIST, metadataList);
    PacketUtil.sendPacket(metadataPacket, Protocol1_8TO1_9.class);
  }
  
  public void spawn() {
    PacketWrapper spawn = new PacketWrapper(15, null, this.user);
    spawn.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    spawn.write(Type.UNSIGNED_BYTE, Short.valueOf((short)62));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write(Types1_8.METADATA_LIST, new ArrayList());
    PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
  }
  
  public void despawn() {
    PacketWrapper despawn = new PacketWrapper(19, null, this.user);
    despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
    PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\entityreplacement\ShulkerReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */