package viaversion.viaversion.protocols.base;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.type.Type;

import java.util.UUID;

public class BaseProtocol1_16 extends BaseProtocol1_7 {

    @Override
    protected UUID passthroughLoginUUID(final PacketWrapper wrapper) throws Exception {
        return wrapper.passthrough(Type.UUID_INT_ARRAY);
    }
}
