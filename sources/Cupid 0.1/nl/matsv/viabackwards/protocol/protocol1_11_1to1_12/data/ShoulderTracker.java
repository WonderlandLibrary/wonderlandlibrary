package nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.data;

import nl.matsv.viabackwards.ViaBackwards;
import nl.matsv.viabackwards.protocol.protocol1_11_1to1_12.Protocol1_11_1To1_12;
import us.myles.ViaVersion.api.PacketWrapper;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.type.Type;
import us.myles.ViaVersion.protocols.protocol1_9to1_8.Protocol1_9To1_8;
import us.myles.viaversion.libs.bungeecordchat.api.ChatColor;

public class ShoulderTracker extends StoredObject {
  private int entityId;
  
  private String leftShoulder;
  
  private String rightShoulder;
  
  public ShoulderTracker(UserConnection user) {
    super(user);
  }
  
  public void update() {
    PacketWrapper wrapper = new PacketWrapper(15, null, getUser());
    wrapper.write(Type.COMPONENT, Protocol1_9To1_8.fixJson(generateString()));
    wrapper.write(Type.BYTE, Byte.valueOf((byte)2));
    try {
      wrapper.send(Protocol1_11_1To1_12.class);
    } catch (Exception e) {
      ViaBackwards.getPlatform().getLogger().severe("Failed to send the shoulder indication");
      e.printStackTrace();
    } 
  }
  
  private String generateString() {
    StringBuilder builder = new StringBuilder();
    builder.append("  ");
    if (this.leftShoulder == null) {
      builder.append(ChatColor.RED).append(ChatColor.BOLD).append("Nothing");
    } else {
      builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(getName(this.leftShoulder));
    } 
    builder.append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(" <- ")
      .append(ChatColor.GRAY).append(ChatColor.BOLD).append("Shoulders")
      .append(ChatColor.DARK_GRAY).append(ChatColor.BOLD).append(" -> ");
    if (this.rightShoulder == null) {
      builder.append(ChatColor.RED).append(ChatColor.BOLD).append("Nothing");
    } else {
      builder.append(ChatColor.DARK_GREEN).append(ChatColor.BOLD).append(getName(this.rightShoulder));
    } 
    return builder.toString();
  }
  
  private String getName(String current) {
    if (current.startsWith("minecraft:"))
      current = current.substring(10); 
    String[] array = current.split("_");
    StringBuilder builder = new StringBuilder();
    for (String s : array)
      builder.append(s.substring(0, 1).toUpperCase())
        .append(s.substring(1))
        .append(" "); 
    return builder.toString();
  }
  
  public int getEntityId() {
    return this.entityId;
  }
  
  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }
  
  public String getLeftShoulder() {
    return this.leftShoulder;
  }
  
  public void setLeftShoulder(String leftShoulder) {
    this.leftShoulder = leftShoulder;
  }
  
  public String getRightShoulder() {
    return this.rightShoulder;
  }
  
  public void setRightShoulder(String rightShoulder) {
    this.rightShoulder = rightShoulder;
  }
  
  public String toString() {
    return "ShoulderTracker{entityId=" + this.entityId + ", leftShoulder='" + this.leftShoulder + '\'' + ", rightShoulder='" + this.rightShoulder + '\'' + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_11_1to1_12\data\ShoulderTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */