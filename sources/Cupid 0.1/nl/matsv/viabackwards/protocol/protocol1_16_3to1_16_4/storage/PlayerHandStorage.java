package nl.matsv.viabackwards.protocol.protocol1_16_3to1_16_4.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class PlayerHandStorage extends StoredObject {
  private int currentHand;
  
  public PlayerHandStorage(UserConnection user) {
    super(user);
  }
  
  public int getCurrentHand() {
    return this.currentHand;
  }
  
  public void setCurrentHand(int currentHand) {
    this.currentHand = currentHand;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_16_3to1_16_4\storage\PlayerHandStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */