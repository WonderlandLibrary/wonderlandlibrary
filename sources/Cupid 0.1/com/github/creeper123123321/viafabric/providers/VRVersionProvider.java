package com.github.creeper123123321.viafabric.providers;

import com.github.creeper123123321.viafabric.ViaFabric;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.base.VersionProvider;

public class VRVersionProvider extends VersionProvider {
  public int getServerProtocol(UserConnection connection) throws Exception {
    if (connection instanceof com.github.creeper123123321.viafabric.platform.VRClientSideUserConnection)
      return ViaFabric.clientSideVersion; 
    return super.getServerProtocol(connection);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabric\providers\VRVersionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */