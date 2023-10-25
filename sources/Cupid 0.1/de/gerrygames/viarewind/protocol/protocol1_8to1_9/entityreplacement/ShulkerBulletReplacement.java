package de.gerrygames.viarewind.protocol.protocol1_8to1_9.entityreplacement;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.replacement.EntityReplacement;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.List;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.type.Type;

public class ShulkerBulletReplacement implements EntityReplacement {
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
  
  public ShulkerBulletReplacement(int entityId, UserConnection user) {
    this.entityId = entityId;
    this.user = user;
    spawn();
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
    if (this.yaw != yaw && this.pitch != pitch) {
      this.yaw = yaw;
      this.pitch = pitch;
      updateLocation();
    } 
  }
  
  public void setHeadYaw(float yaw) {
    this.headYaw = yaw;
  }
  
  public void updateMetadata(List<Metadata> metadataList) {}
  
  public void updateLocation() {
    PacketWrapper teleport = new PacketWrapper(24, null, this.user);
    teleport.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
    teleport.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
    teleport.write(Type.BYTE, Byte.valueOf((byte)(int)(this.yaw / 360.0F * 256.0F)));
    teleport.write(Type.BYTE, Byte.valueOf((byte)(int)(this.pitch / 360.0F * 256.0F)));
    teleport.write(Type.BOOLEAN, Boolean.valueOf(true));
    PacketWrapper head = new PacketWrapper(25, null, this.user);
    head.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    head.write(Type.BYTE, Byte.valueOf((byte)(int)(this.headYaw / 360.0F * 256.0F)));
    PacketUtil.sendPacket(teleport, Protocol1_8TO1_9.class, true, true);
    PacketUtil.sendPacket(head, Protocol1_8TO1_9.class, true, true);
  }
  
  public void spawn() {
    PacketWrapper spawn = new PacketWrapper(14, null, this.user);
    spawn.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    spawn.write(Type.BYTE, Byte.valueOf((byte)66));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.INT, Integer.valueOf(0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.BYTE, Byte.valueOf((byte)0));
    spawn.write(Type.INT, Integer.valueOf(0));
    PacketUtil.sendPacket(spawn, Protocol1_8TO1_9.class, true, true);
  }
  
  public void despawn() {
    PacketWrapper despawn = new PacketWrapper(19, null, this.user);
    despawn.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
    PacketUtil.sendPacket(despawn, Protocol1_8TO1_9.class, true, true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\entityreplacement\ShulkerBulletReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */