// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket.packets;

import java.io.IOException;
import java.io.DataOutputStream;

public interface Packet
{
    void write(final DataOutputStream p0) throws IOException;
}
