package viaversion.viarewind.utils;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.protocols.base.ProtocolInfo;

import java.util.UUID;

public class Utils {

    public static UUID getUUID(UserConnection user) {
        return user.get(ProtocolInfo.class).getUuid();
    }
}
