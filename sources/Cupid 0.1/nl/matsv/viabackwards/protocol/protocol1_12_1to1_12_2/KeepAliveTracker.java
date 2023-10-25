package nl.matsv.viabackwards.protocol.protocol1_12_1to1_12_2;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class KeepAliveTracker extends StoredObject {
  private long keepAlive = 2147483647L;
  
  public KeepAliveTracker(UserConnection user) {
    super(user);
  }
  
  public long getKeepAlive() {
    return this.keepAlive;
  }
  
  public void setKeepAlive(long keepAlive) {
    this.keepAlive = keepAlive;
  }
  
  public String toString() {
    return "KeepAliveTracker{keepAlive=" + this.keepAlive + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_1to1_12_2\KeepAliveTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */