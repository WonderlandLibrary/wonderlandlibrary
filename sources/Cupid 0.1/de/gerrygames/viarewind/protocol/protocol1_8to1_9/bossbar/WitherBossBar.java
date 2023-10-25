package de.gerrygames.viarewind.protocol.protocol1_8to1_9.bossbar;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.Protocol1_8TO1_9;
import de.gerrygames.viarewind.utils.PacketUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.boss.BossBar;
import us.myles.ViaVersion.api.boss.BossColor;
import us.myles.ViaVersion.api.boss.BossFlag;
import us.myles.ViaVersion.api.boss.BossStyle;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.MetaType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;
import us.myles.ViaVersion.api.minecraft.metadata.types.MetaType1_8;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.api.type.types.version.Types1_8;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;

public class WitherBossBar extends BossBar {
  private static int highestId = 2147473647;
  
  private final UUID uuid;
  
  private String title;
  
  private float health;
  
  private boolean visible = false;
  
  private UserConnection connection;
  
  private final int entityId = highestId++;
  
  private double locX;
  
  private double locY;
  
  private double locZ;
  
  public WitherBossBar(UserConnection connection, UUID uuid, String title, float health) {
    this.connection = connection;
    this.uuid = uuid;
    this.title = title;
    this.health = health;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public BossBar setTitle(String title) {
    this.title = title;
    if (this.visible)
      updateMetadata(); 
    return this;
  }
  
  public float getHealth() {
    return this.health;
  }
  
  public BossBar setHealth(float health) {
    this.health = health;
    if (this.health <= 0.0F)
      this.health = 1.0E-4F; 
    if (this.visible)
      updateMetadata(); 
    return this;
  }
  
  public BossColor getColor() {
    return null;
  }
  
  public BossBar setColor(BossColor bossColor) {
    throw new UnsupportedOperationException(getClass().getName() + " does not support color");
  }
  
  public BossStyle getStyle() {
    return null;
  }
  
  public BossBar setStyle(BossStyle bossStyle) {
    throw new UnsupportedOperationException(getClass().getName() + " does not support styles");
  }
  
  public BossBar addPlayer(UUID uuid) {
    throw new UnsupportedOperationException(getClass().getName() + " is only for one UserConnection!");
  }
  
  public BossBar addConnection(UserConnection userConnection) {
    throw new UnsupportedOperationException(getClass().getName() + " is only for one UserConnection!");
  }
  
  public BossBar removePlayer(UUID uuid) {
    throw new UnsupportedOperationException(getClass().getName() + " is only for one UserConnection!");
  }
  
  public BossBar removeConnection(UserConnection userConnection) {
    throw new UnsupportedOperationException(getClass().getName() + " is only for one UserConnection!");
  }
  
  public BossBar addFlag(BossFlag bossFlag) {
    throw new UnsupportedOperationException(getClass().getName() + " does not support flags");
  }
  
  public BossBar removeFlag(BossFlag bossFlag) {
    throw new UnsupportedOperationException(getClass().getName() + " does not support flags");
  }
  
  public boolean hasFlag(BossFlag bossFlag) {
    return false;
  }
  
  public Set<UUID> getPlayers() {
    return Collections.singleton(((ProtocolInfo)this.connection.get(ProtocolInfo.class)).getUuid());
  }
  
  public Set<UserConnection> getConnections() {
    throw new UnsupportedOperationException(getClass().getName() + " is only for one UserConnection!");
  }
  
  public BossBar show() {
    if (!this.visible) {
      this.visible = true;
      spawnWither();
    } 
    return this;
  }
  
  public BossBar hide() {
    if (this.visible) {
      this.visible = false;
      despawnWither();
    } 
    return this;
  }
  
  public boolean isVisible() {
    return this.visible;
  }
  
  public UUID getId() {
    return this.uuid;
  }
  
  public void setLocation(double x, double y, double z) {
    this.locX = x;
    this.locY = y;
    this.locZ = z;
    updateLocation();
  }
  
  private void spawnWither() {
    PacketWrapper packetWrapper = new PacketWrapper(15, null, this.connection);
    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    packetWrapper.write(Type.UNSIGNED_BYTE, Short.valueOf((short)64));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)0));
    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)0));
    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)0));
    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
    packetWrapper.write((Type)Type.SHORT, Short.valueOf((short)0));
    List<Metadata> metadata = new ArrayList<>();
    metadata.add(new Metadata(0, (MetaType)MetaType1_8.Byte, Byte.valueOf((byte)32)));
    metadata.add(new Metadata(2, (MetaType)MetaType1_8.String, this.title));
    metadata.add(new Metadata(3, (MetaType)MetaType1_8.Byte, Byte.valueOf((byte)1)));
    metadata.add(new Metadata(6, (MetaType)MetaType1_8.Float, Float.valueOf(this.health * 300.0F)));
    packetWrapper.write(Types1_8.METADATA_LIST, metadata);
    PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
  }
  
  private void updateLocation() {
    PacketWrapper packetWrapper = new PacketWrapper(24, null, this.connection);
    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locX * 32.0D)));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locY * 32.0D)));
    packetWrapper.write(Type.INT, Integer.valueOf((int)(this.locZ * 32.0D)));
    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)0));
    packetWrapper.write(Type.BYTE, Byte.valueOf((byte)0));
    packetWrapper.write(Type.BOOLEAN, Boolean.valueOf(false));
    PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
  }
  
  private void updateMetadata() {
    PacketWrapper packetWrapper = new PacketWrapper(28, null, this.connection);
    packetWrapper.write((Type)Type.VAR_INT, Integer.valueOf(this.entityId));
    List<Metadata> metadata = new ArrayList<>();
    metadata.add(new Metadata(2, (MetaType)MetaType1_8.String, this.title));
    metadata.add(new Metadata(6, (MetaType)MetaType1_8.Float, Float.valueOf(this.health * 300.0F)));
    packetWrapper.write(Types1_8.METADATA_LIST, metadata);
    PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
  }
  
  private void despawnWither() {
    PacketWrapper packetWrapper = new PacketWrapper(19, null, this.connection);
    packetWrapper.write(Type.VAR_INT_ARRAY_PRIMITIVE, new int[] { this.entityId });
    PacketUtil.sendPacket(packetWrapper, Protocol1_8TO1_9.class, true, true);
  }
  
  public void setPlayerLocation(double posX, double posY, double posZ, float yaw, float pitch) {
    double yawR = Math.toRadians(yaw);
    double pitchR = Math.toRadians(pitch);
    posX -= Math.cos(pitchR) * Math.sin(yawR) * 48.0D;
    posY -= Math.sin(pitchR) * 48.0D;
    posZ += Math.cos(pitchR) * Math.cos(yawR) * 48.0D;
    setLocation(posX, posY, posZ);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\bossbar\WitherBossBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */