package viaversion.viaversion.protocols.base;

import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.platform.providers.Provider;
import viaversion.viaversion.api.protocol.ProtocolRegistry;

public class VersionProvider implements Provider {

    public int getServerProtocol(UserConnection connection) throws Exception {
        return ProtocolRegistry.SERVER_PROTOCOL;
    }
}
