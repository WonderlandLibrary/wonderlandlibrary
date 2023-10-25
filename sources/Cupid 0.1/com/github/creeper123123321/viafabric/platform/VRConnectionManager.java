package com.github.creeper123123321.viafabric.platform;

import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.platform.ViaConnectionManager;

public class VRConnectionManager extends ViaConnectionManager {
  public boolean isFrontEnd(UserConnection connection) {
    return !(connection instanceof VRClientSideUserConnection);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\platform\VRConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */