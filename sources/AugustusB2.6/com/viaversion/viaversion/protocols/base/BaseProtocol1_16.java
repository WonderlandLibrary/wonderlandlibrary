// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.base;

import com.viaversion.viaversion.api.type.Type;
import java.util.UUID;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public class BaseProtocol1_16 extends BaseProtocol1_7
{
    @Override
    protected UUID passthroughLoginUUID(final PacketWrapper wrapper) throws Exception {
        return wrapper.passthrough(Type.UUID_INT_ARRAY);
    }
}
