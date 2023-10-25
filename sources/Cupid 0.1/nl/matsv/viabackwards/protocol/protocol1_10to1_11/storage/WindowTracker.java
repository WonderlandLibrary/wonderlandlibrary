package nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class WindowTracker extends StoredObject {
  private String inventory;
  
  private int entityId = -1;
  
  public WindowTracker(UserConnection user) {
    super(user);
  }
  
  public String getInventory() {
    return this.inventory;
  }
  
  public void setInventory(String inventory) {
    this.inventory = inventory;
  }
  
  public int getEntityId() {
    return this.entityId;
  }
  
  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }
  
  public String toString() {
    return "WindowTracker{inventory='" + this.inventory + '\'' + ", entityId=" + this.entityId + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\storage\WindowTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */