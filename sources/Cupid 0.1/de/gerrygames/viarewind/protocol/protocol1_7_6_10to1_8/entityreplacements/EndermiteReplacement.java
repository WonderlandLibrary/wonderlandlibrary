package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;

public class EndermiteReplacement implements EntityReplacement {
  private int entityId;
  
  public int getEntityId() {
    return this.entityId;
  }
  
  private List<Metadata> datawatcher = new ArrayList<>();
  
  private double locX;
  
  private double locY;
  
  private double locZ;
  
  private float yaw;
  
  private float pitch;
  
  private float headYaw;
  
  private UserConnection user;
  
  public EndermiteReplacement(int entityId, UserConnection user) {
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
  
  public void setYawPitch(float yaw, float pitch) {
    if (this.yaw != yaw || this.pitch != pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
      updateLocation();
    } 
  }
  
  public void setHeadYaw(float yaw) {
    if (this.headYaw != yaw) {
      this.headYaw = yaw;
      updateLocation();
    } 
  }
  
  public void updateMetadata(List<Metadata> metadataList) {
    for (Metadata metadata : metadataList) {
      this.datawatcher.removeIf(m -> (m.getId() == metadata.getId()));
      this.datawatcher.add(metadata);
    } 
    updateMetadata();
  }
  
  public void updateLocation() {
    PacketWrapper teleport = new PacketWrapper(24, null, this.user);
    teleport.write(Type.INT, Integer.valueOf(this.entityId));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
    teleport.write(Type.BYTE, Byte.valueOf((byte)(int)(this.yaw / 360.0F * 256.0F)));
    teleport.write(Type.BYTE, Byte.valueOf((byte)(int)(this.pitch / 360.0F * 256.0F)));
    PacketWrapper head = new PacketWrapper(25, null, this.user);
    head.write(Type.INT, Integer.valueOf(this.entityId));
    head.write(Type.BYTE, Byte.valueOf((byte)(int)(this.headYaw / 360.0F * 256.0F)));
    PacketUtil.sendPacket(teleport, Protocol1_7_6_10TO1_8.class, true, true);
    PacketUtil.sendPacket(head, Protocol1_7_6_10TO1_8.class, true, true);
  }
  
  public void updateMetadata() {
    PacketWrapper metadataPacket = new PacketWrapper(28, null, this.user);
    metadataPacket.write(Type.INT, Integer.valueOf(this.entityId));
    List<Metadata> metadataList = new ArrayList<>();
    for (Metadata metadata : this.datawatcher)
      metadataList.add(new Metadata(metadata.getId(), metadata.getMetaType(), metadata.getValue())); 
    MetadataRewriter.transform(Entity1_10Types.EntityType.SQUID, metadataList);
    metadataPacket.write(Types1_7_6_10.METADATA_LIST, metadataList);
    PacketUtil.sendPacket(metadataPacket, Protocol1_7_6_10TO1_8.class);
  }
  
  public void spawn() {
    PacketWrapper spawn = new PacketWrapper(15, null, this.user);
    spawn.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    spawn.write(Type.UNSIGNED_BYTE, Short.valueOf((short)60));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
    spawn.write(Types1_7_6_10.METADATA_LIST, new ArrayList());
    PacketUtil.sendPacket(spawn, Protocol1_7_6_10TO1_8.class, true, true);
  }
  
  public void despawn() {
    PacketWrapper despawn = new PacketWrapper(19, null, this.user);
    despawn.write(Types1_7_6_10.INT_ARRAY, new int[] { this.entityId });
    PacketUtil.sendPacket(despawn, Protocol1_7_6_10TO1_8.class, true, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\entityreplacements\EndermiteReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */