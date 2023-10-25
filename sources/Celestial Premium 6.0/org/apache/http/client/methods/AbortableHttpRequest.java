/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client.methods;

import java.io.IOException;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;

@Deprecated
public interface AbortableHttpRequest {
    public void setConnectionRequest(ClientConnectionRequest var1) throws IOException;

    public void setReleaseTrigger(ConnectionReleaseTrigger var1) throws IOException;

    public void abort();
}

