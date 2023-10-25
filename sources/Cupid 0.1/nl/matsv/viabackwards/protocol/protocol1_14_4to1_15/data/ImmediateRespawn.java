package nl.matsv.viabackwards.protocol.protocol1_14_4to1_15.data;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class ImmediateRespawn extends StoredObject {
  private boolean immediateRespawn;
  
  public ImmediateRespawn(UserConnection user) {
    super(user);
  }
  
  public boolean isImmediateRespawn() {
    return this.immediateRespawn;
  }
  
  public void setImmediateRespawn(boolean immediateRespawn) {
    this.immediateRespawn = immediateRespawn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_14_4to1_15\data\ImmediateRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */