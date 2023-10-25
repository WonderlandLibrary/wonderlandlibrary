package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class PlayerSneakStorage extends StoredObject {
  private boolean sneaking;
  
  public PlayerSneakStorage(UserConnection user) {
    super(user);
  }
  
  public boolean isSneaking() {
    return this.sneaking;
  }
  
  public void setSneaking(boolean sneaking) {
    this.sneaking = sneaking;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\storage\PlayerSneakStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */