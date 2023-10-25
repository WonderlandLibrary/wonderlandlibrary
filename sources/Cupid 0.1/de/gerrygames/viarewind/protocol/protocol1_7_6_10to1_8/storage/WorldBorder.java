package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.Protocol1_7_6_10TO1_8;
import de.gerrygames.viarewind.utils.PacketUtil;
import de.gerrygames.viarewind.utils.Tickable;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;

public class WorldBorder extends StoredObject implements Tickable {
  private double x;
  
  private double z;
  
  private double oldDiameter;
  
  private double newDiameter;
  
  private long lerpTime;
  
  private long lerpStartTime;
  
  private int portalTeleportBoundary;
  
  private int warningTime;
  
  private int warningBlocks;
  
  private boolean init = false;
  
  private final int VIEW_DISTANCE = 16;
  
  public WorldBorder(UserConnection user) {
    super(user);
  }
  
  public void tick() {
    if (!isInit())
      return; 
    sendPackets();
  }
  
  private enum Side {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);
    
    private int modX;
    
    private int modZ;
    
    Side(int modX, int modZ) {
      this.modX = modX;
      this.modZ = modZ;
    }
  }
  
  private void sendPackets() {
    PlayerPosition position = (PlayerPosition)getUser().get(PlayerPosition.class);
    double radius = getSize() / 2.0D;
    for (Side side : Side.values()) {
      double d;
      double pos;
      double center;
      if (side.modX != 0) {
        pos = position.getPosZ();
        center = this.z;
        d = Math.abs(this.x + radius * side.modX - position.getPosX());
      } else {
        center = this.x;
        pos = position.getPosX();
        d = Math.abs(this.z + radius * side.modZ - position.getPosZ());
      } 
      if (d < 16.0D) {
        double r = Math.sqrt(256.0D - d * d);
        double minH = Math.ceil(pos - r);
        double maxH = Math.floor(pos + r);
        double minV = Math.ceil(position.getPosY() - r);
        double maxV = Math.floor(position.getPosY() + r);
        if (minH < center - radius)
          minH = Math.ceil(center - radius); 
        if (maxH > center + radius)
          maxH = Math.floor(center + radius); 
        if (minV < 0.0D)
          minV = 0.0D; 
        double centerH = (minH + maxH) / 2.0D;
        double centerV = (minV + maxV) / 2.0D;
        int a = (int)Math.floor((maxH - minH) * (maxV - minV) * 0.5D);
        double b = 2.5D;
        PacketWrapper particles = new PacketWrapper(42, null, getUser());
        particles.write(Type.STRING, "fireworksSpark");
        particles.write((Type)Type.FLOAT, Float.valueOf((float)((side.modX != 0) ? (this.x + radius * side.modX) : centerH)));
        particles.write((Type)Type.FLOAT, Float.valueOf((float)centerV));
        particles.write((Type)Type.FLOAT, Float.valueOf((float)((side.modX == 0) ? (this.z + radius * side.modZ) : centerH)));
        particles.write((Type)Type.FLOAT, Float.valueOf((float)((side.modX != 0) ? 0.0D : ((maxH - minH) / b))));
        particles.write((Type)Type.FLOAT, Float.valueOf((float)((maxV - minV) / b)));
        particles.write((Type)Type.FLOAT, Float.valueOf((float)((side.modX == 0) ? 0.0D : ((maxH - minH) / b))));
        particles.write((Type)Type.FLOAT, Float.valueOf(0.0F));
        particles.write(Type.INT, Integer.valueOf(a));
        PacketUtil.sendPacket(particles, Protocol1_7_6_10TO1_8.class, true, true);
      } 
    } 
  }
  
  private boolean isInit() {
    return this.init;
  }
  
  public void init(double x, double z, double oldDiameter, double newDiameter, long lerpTime, int portalTeleportBoundary, int warningTime, int warningBlocks) {
    this.x = x;
    this.z = z;
    this.oldDiameter = oldDiameter;
    this.newDiameter = newDiameter;
    this.lerpTime = lerpTime;
    this.portalTeleportBoundary = portalTeleportBoundary;
    this.warningTime = warningTime;
    this.warningBlocks = warningBlocks;
    this.init = true;
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getZ() {
    return this.z;
  }
  
  public void setCenter(double x, double z) {
    this.x = x;
    this.z = z;
  }
  
  public double getOldDiameter() {
    return this.oldDiameter;
  }
  
  public double getNewDiameter() {
    return this.newDiameter;
  }
  
  public long getLerpTime() {
    return this.lerpTime;
  }
  
  public void lerpSize(double oldDiameter, double newDiameter, long lerpTime) {
    this.oldDiameter = oldDiameter;
    this.newDiameter = newDiameter;
    this.lerpTime = lerpTime;
    this.lerpStartTime = System.currentTimeMillis();
  }
  
  public void setSize(double size) {
    this.oldDiameter = size;
    this.newDiameter = size;
    this.lerpTime = 0L;
  }
  
  public double getSize() {
    if (this.lerpTime == 0L)
      return this.newDiameter; 
    long time = System.currentTimeMillis() - this.lerpStartTime;
    double percent = time / this.lerpTime;
    if (percent > 1.0D) {
      percent = 1.0D;
    } else if (percent < 0.0D) {
      percent = 0.0D;
    } 
    return this.oldDiameter + (this.newDiameter - this.oldDiameter) * percent;
  }
  
  public int getPortalTeleportBoundary() {
    return this.portalTeleportBoundary;
  }
  
  public void setPortalTeleportBoundary(int portalTeleportBoundary) {
    this.portalTeleportBoundary = portalTeleportBoundary;
  }
  
  public int getWarningTime() {
    return this.warningTime;
  }
  
  public void setWarningTime(int warningTime) {
    this.warningTime = warningTime;
  }
  
  public int getWarningBlocks() {
    return this.warningBlocks;
  }
  
  public void setWarningBlocks(int warningBlocks) {
    this.warningBlocks = warningBlocks;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\WorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */