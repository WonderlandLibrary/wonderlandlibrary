package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.entityreplacements;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.metadata.MetadataRewriter;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.MetaType1_7_6_10;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.utils.math.AABB;
import de.gerrygames.viarewind.utils.math.Vector3d;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.entities.Entity1_10Types;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.type.Type;

public class ArmorStandReplacement implements EntityReplacement {
  private int entityId;
  
  public int getEntityId() {
    return this.entityId;
  }
  
  private List<Metadata> datawatcher = new ArrayList<>();
  
  private int[] entityIds = null;
  
  private double locX;
  
  private double locY;
  
  private double locZ;
  
  private State currentState = null;
  
  private boolean invisible = false;
  
  private boolean nameTagVisible = false;
  
  private String name = null;
  
  private UserConnection user;
  
  private float yaw;
  
  private float pitch;
  
  private float headYaw;
  
  private boolean small = false;
  
  private boolean marker = false;
  
  private static int ENTITY_ID = 2147467647;
  
  private enum State {
    HOLOGRAM, ZOMBIE;
  }
  
  public ArmorStandReplacement(int entityId, UserConnection user) {
    this.entityId = entityId;
    this.user = user;
  }
  
  public void setLocation(double x, double y, double z) {
    if (x != this.locX || y != this.locY || z != this.locZ) {
      this.locX = x;
      this.locY = y;
      this.locZ = z;
      updateLocation();
    } 
  }
  
  public void relMove(double x, double y, double z) {
    if (x == 0.0D && y == 0.0D && z == 0.0D)
      return; 
    this.locX += x;
    this.locY += y;
    this.locZ += z;
    updateLocation();
  }
  
  public void setYawPitch(float yaw, float pitch) {
    if ((this.yaw != yaw && this.pitch != pitch) || this.headYaw != yaw) {
      this.yaw = yaw;
      this.headYaw = yaw;
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
    updateState();
  }
  
  public void updateState() {
    byte flags = 0;
    byte armorStandFlags = 0;
    for (Metadata metadata : this.datawatcher) {
      if (metadata.getId() == 0 && metadata.getMetaType() == MetaType1_8.Byte) {
        flags = ((Byte)metadata.getValue()).byteValue();
        continue;
      } 
      if (metadata.getId() == 2 && metadata.getMetaType() == MetaType1_8.String) {
        this.name = (String)metadata.getValue();
        if (this.name != null && this.name.equals(""))
          this.name = null; 
        continue;
      } 
      if (metadata.getId() == 10 && metadata.getMetaType() == MetaType1_8.Byte) {
        armorStandFlags = ((Byte)metadata.getValue()).byteValue();
        continue;
      } 
      if (metadata.getId() == 3 && metadata.getMetaType() == MetaType1_8.Byte)
        this.nameTagVisible = ((byte)metadata.getId() != 0); 
    } 
    this.invisible = ((flags & 0x20) != 0);
    this.small = ((armorStandFlags & 0x1) != 0);
    this.marker = ((armorStandFlags & 0x10) != 0);
    State prevState = this.currentState;
    if (this.invisible && this.name != null) {
      this.currentState = State.HOLOGRAM;
    } else {
      this.currentState = State.ZOMBIE;
    } 
    if (this.currentState != prevState) {
      despawn();
      spawn();
    } else {
      updateMetadata();
      updateLocation();
    } 
  }
  
  public void updateLocation() {
    if (this.entityIds == null)
      return; 
    if (this.currentState == State.ZOMBIE) {
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
    } else if (this.currentState == State.HOLOGRAM) {
      PacketWrapper detach = new PacketWrapper(27, null, this.user);
      detach.write(Type.INT, Integer.valueOf(this.entityIds[1]));
      detach.write(Type.INT, Integer.valueOf(-1));
      detach.write(Type.BOOLEAN, Boolean.valueOf(false));
      PacketWrapper teleportSkull = new PacketWrapper(24, null, this.user);
      teleportSkull.write(Type.INT, Integer.valueOf(this.entityIds[0]));
      teleportSkull.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
      teleportSkull.write(Type.INT, Integer.valueOf((int)((this.locY + (this.marker ? 54.85D : (this.small ? 56.0D : 57.0D))) * 32.0D)));
      teleportSkull.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
      teleportSkull.write(Type.BYTE, Byte.valueOf((byte)0));
      teleportSkull.write(Type.BYTE, Byte.valueOf((byte)0));
      PacketWrapper teleportHorse = new PacketWrapper(24, null, this.user);
      teleportHorse.write(Type.INT, Integer.valueOf(this.entityIds[1]));
      teleportHorse.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
      teleportHorse.write(Type.INT, Integer.valueOf((int)((this.locY + 56.75D) * 32.0D)));
      teleportHorse.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
      teleportHorse.write(Type.BYTE, Byte.valueOf((byte)0));
      teleportHorse.write(Type.BYTE, Byte.valueOf((byte)0));
      PacketWrapper attach = new PacketWrapper(27, null, this.user);
      attach.write(Type.INT, Integer.valueOf(this.entityIds[1]));
      attach.write(Type.INT, Integer.valueOf(this.entityIds[0]));
      attach.write(Type.BOOLEAN, Boolean.valueOf(false));
      PacketUtil.sendPacket(detach, Protocol1_7_6_10TO1_8.class, true, true);
      PacketUtil.sendPacket(teleportSkull, Protocol1_7_6_10TO1_8.class, true, true);
      PacketUtil.sendPacket(teleportHorse, Protocol1_7_6_10TO1_8.class, true, true);
      PacketUtil.sendPacket(attach, Protocol1_7_6_10TO1_8.class, true, true);
    } 
  }
  
  public void updateMetadata() {
    if (this.entityIds == null)
      return; 
    PacketWrapper metadataPacket = new PacketWrapper(28, null, this.user);
    if (this.currentState == State.ZOMBIE) {
      metadataPacket.write(Type.INT, Integer.valueOf(this.entityIds[0]));
      List<Metadata> metadataList = new ArrayList<>();
      for (Metadata metadata : this.datawatcher) {
        if (metadata.getId() < 0 || metadata.getId() > 9)
          continue; 
        metadataList.add(new Metadata(metadata.getId(), metadata.getMetaType(), metadata.getValue()));
      } 
      if (this.small)
        metadataList.add(new Metadata(12, (MetaType)MetaType1_8.Byte, Byte.valueOf((byte)1))); 
      MetadataRewriter.transform(Entity1_10Types.EntityType.ZOMBIE, metadataList);
      metadataPacket.write(Types1_7_6_10.METADATA_LIST, metadataList);
    } else if (this.currentState == State.HOLOGRAM) {
      metadataPacket.write(Type.INT, Integer.valueOf(this.entityIds[1]));
      List<Metadata> metadataList = new ArrayList<>();
      metadataList.add(new Metadata(12, (MetaType)MetaType1_7_6_10.Int, Integer.valueOf(-1700000)));
      metadataList.add(new Metadata(10, (MetaType)MetaType1_7_6_10.String, this.name));
      metadataList.add(new Metadata(11, (MetaType)MetaType1_7_6_10.Byte, Byte.valueOf((byte)1)));
      metadataPacket.write(Types1_7_6_10.METADATA_LIST, metadataList);
    } else {
      return;
    } 
    PacketUtil.sendPacket(metadataPacket, Protocol1_7_6_10TO1_8.class);
  }
  
  public void spawn() {
    if (this.entityIds != null)
      despawn(); 
    if (this.currentState == State.ZOMBIE) {
      PacketWrapper spawn = new PacketWrapper(15, null, this.user);
      spawn.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
      spawn.write(Type.UNSIGNED_BYTE, Short.valueOf((short)54));
      spawn.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
      spawn.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
      spawn.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
      spawn.write(Type.BYTE, Byte.valueOf((byte)0));
      spawn.write(Type.BYTE, Byte.valueOf((byte)0));
      spawn.write(Type.BYTE, Byte.valueOf((byte)0));
      spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawn.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawn.write(Types1_7_6_10.METADATA_LIST, new ArrayList());
      PacketUtil.sendPacket(spawn, Protocol1_7_6_10TO1_8.class, true, true);
      this.entityIds = new int[] { this.entityId };
    } else if (this.currentState == State.HOLOGRAM) {
      int[] entityIds = { this.entityId, ENTITY_ID-- };
      PacketWrapper spawnSkull = new PacketWrapper(14, null, this.user);
      spawnSkull.write((Type)Type.VAR_INT, Integer.valueOf(entityIds[0]));
      spawnSkull.write(Type.BYTE, Byte.valueOf((byte)66));
      spawnSkull.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
      spawnSkull.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
      spawnSkull.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
      spawnSkull.write(Type.BYTE, Byte.valueOf((byte)0));
      spawnSkull.write(Type.BYTE, Byte.valueOf((byte)0));
      spawnSkull.write(Type.INT, Integer.valueOf(0));
      PacketWrapper spawnHorse = new PacketWrapper(15, null, this.user);
      spawnHorse.write((Type)Type.VAR_INT, Integer.valueOf(entityIds[1]));
      spawnHorse.write(Type.UNSIGNED_BYTE, Short.valueOf((short)100));
      spawnHorse.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
      spawnHorse.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
      spawnHorse.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
      spawnHorse.write(Type.BYTE, Byte.valueOf((byte)0));
      spawnHorse.write(Type.BYTE, Byte.valueOf((byte)0));
      spawnHorse.write(Type.BYTE, Byte.valueOf((byte)0));
      spawnHorse.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawnHorse.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawnHorse.write((Type)Type.SHORT, Short.valueOf((short)0));
      spawnHorse.write(Types1_7_6_10.METADATA_LIST, new ArrayList());
      PacketUtil.sendPacket(spawnSkull, Protocol1_7_6_10TO1_8.class, true, true);
      PacketUtil.sendPacket(spawnHorse, Protocol1_7_6_10TO1_8.class, true, true);
      this.entityIds = entityIds;
    } 
    updateMetadata();
    updateLocation();
  }
  
  public AABB getBoundingBox() {
    double w = this.small ? 0.25D : 0.5D;
    double h = this.small ? 0.9875D : 1.975D;
    Vector3d min = new Vector3d(this.locX - w / 2.0D, this.locY, this.locZ - w / 2.0D);
    Vector3d max = new Vector3d(this.locX + w / 2.0D, this.locY + h, this.locZ + w / 2.0D);
    return new AABB(min, max);
  }
  
  public void despawn() {
    if (this.entityIds == null)
      return; 
    PacketWrapper despawn = new PacketWrapper(19, null, this.user);
    despawn.write(Type.BYTE, Byte.valueOf((byte)this.entityIds.length));
    for (int id : this.entityIds)
      despawn.write(Type.INT, Integer.valueOf(id)); 
    this.entityIds = null;
    PacketUtil.sendPacket(despawn, Protocol1_7_6_10TO1_8.class, true, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\entityreplacements\ArmorStandReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */