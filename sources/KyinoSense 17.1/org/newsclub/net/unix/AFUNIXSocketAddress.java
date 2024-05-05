/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.newsclub.net.unix.NativeUnixSocket;

public class AFUNIXSocketAddress
extends InetSocketAddress {
    private static final long serialVersionUID = 1L;
    private final String socketFile;

    public AFUNIXSocketAddress(File socketFile) throws IOException {
        this(socketFile, 0);
    }

    public AFUNIXSocketAddress(File socketFile, int port) throws IOException {
        super(0);
        if (port != 0) {
            NativeUnixSocket.setPort1(this, port);
        }
        this.socketFile = socketFile.getCanonicalPath();
    }

    public String getSocketFile() {
        return this.socketFile;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[host=" + this.getHostName() + ";port=" + this.getPort() + ";file=" + this.socketFile + "]";
    }
}

