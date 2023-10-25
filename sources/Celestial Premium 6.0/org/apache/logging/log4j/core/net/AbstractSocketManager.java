/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public abstract class AbstractSocketManager
extends OutputStreamManager {
    protected final InetAddress address;
    protected final String host;
    protected final int port;

    public AbstractSocketManager(String name, OutputStream os, InetAddress addr, String host, int port, Layout<? extends Serializable> layout) {
        super(os, name, layout);
        this.address = addr;
        this.host = host;
        this.port = port;
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("port", Integer.toString(this.port));
        result.put("address", this.address.getHostAddress());
        return result;
    }
}

