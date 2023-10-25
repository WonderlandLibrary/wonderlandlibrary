package de.gerrygames.viarewind.utils;

import java.util.UUID;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.protocols.base.ProtocolInfo;

public class Utils {
  public static UUID getUUID(UserConnection user) {
    return ((ProtocolInfo)user.get(ProtocolInfo.class)).getUuid();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */