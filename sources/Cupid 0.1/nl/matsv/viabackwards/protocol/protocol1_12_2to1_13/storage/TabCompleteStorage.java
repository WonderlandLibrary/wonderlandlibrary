package nl.matsv.viabackwards.protocol.protocol1_12_2to1_13.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class TabCompleteStorage extends StoredObject {
  public int lastId;
  
  public String lastRequest;
  
  public boolean lastAssumeCommand;
  
  public Map<UUID, String> usernames = new HashMap<>();
  
  public TabCompleteStorage(UserConnection user) {
    super(user);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_12_2to1_13\storage\TabCompleteStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */