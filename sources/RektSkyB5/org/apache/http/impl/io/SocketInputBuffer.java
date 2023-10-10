/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class SocketInputBuffer
extends AbstractSessionInputBuffer
implements EofSensor {
    private final Socket socket;
    private boolean eof;

    public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        Args.notNull(socket, "Socket");
        this.socket = socket;
        this.eof = false;
        int n2 = buffersize;
        if (n2 < 0) {
            n2 = socket.getReceiveBufferSize();
        }
        if (n2 < 1024) {
            n2 = 1024;
        }
        this.init(socket.getInputStream(), n2, params);
    }

    @Override
    protected int fillBuffer() throws IOException {
        int i2 = super.fillBuffer();
        this.eof = i2 == -1;
        return i2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean isDataAvailable(int timeout) throws IOException {
        boolean result = this.hasBufferedData();
        if (!result) {
            int oldtimeout = this.socket.getSoTimeout();
            try {
                this.socket.setSoTimeout(timeout);
                this.fillBuffer();
                result = this.hasBufferedData();
            }
            finally {
                this.socket.setSoTimeout(oldtimeout);
            }
        }
        return result;
    }

    @Override
    public boolean isEof() {
        return this.eof;
    }
}

