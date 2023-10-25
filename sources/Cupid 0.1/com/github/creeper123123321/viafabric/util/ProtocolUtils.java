package com.github.creeper123123321.viafabric.util;

import us.myles.ViaVersion.api.protocol.ProtocolVersion;

public class ProtocolUtils {
  public static String getProtocolName(int id) {
    if (!ProtocolVersion.isRegistered(id))
      return Integer.toString(id); 
    return ProtocolVersion.getProtocol(id).getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\github\creeper123123321\viafabri\\util\ProtocolUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */