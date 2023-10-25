/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.execchain.ConnectionHolder;

@NotThreadSafe
class ResponseEntityWrapper
extends HttpEntityWrapper
implements EofSensorWatcher {
    private final ConnectionHolder connReleaseTrigger;

    public ResponseEntityWrapper(HttpEntity entity, ConnectionHolder connReleaseTrigger) {
        super(entity);
        this.connReleaseTrigger = connReleaseTrigger;
    }

    private void cleanup() {
        if (this.connReleaseTrigger != null) {
            this.connReleaseTrigger.abortConnection();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseConnection() throws IOException {
        if (this.connReleaseTrigger != null) {
            try {
                if (this.connReleaseTrigger.isReusable()) {
                    this.connReleaseTrigger.releaseConnection();
                }
            }
            finally {
                this.cleanup();
            }
        }
    }

    public boolean isRepeatable() {
        return false;
    }

    public InputStream getContent() throws IOException {
        return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
    }

    @Deprecated
    public void consumeContent() throws IOException {
        this.releaseConnection();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void writeTo(OutputStream outstream) throws IOException {
        try {
            this.wrappedEntity.writeTo(outstream);
            this.releaseConnection();
        }
        finally {
            this.cleanup();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean eofDetected(InputStream wrapped) throws IOException {
        try {
            wrapped.close();
            this.releaseConnection();
        }
        finally {
            this.cleanup();
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean streamClosed(InputStream wrapped) throws IOException {
        try {
            boolean open = this.connReleaseTrigger != null && !this.connReleaseTrigger.isReleased();
            try {
                wrapped.close();
                this.releaseConnection();
            }
            catch (SocketException ex) {
                if (open) {
                    throw ex;
                }
            }
        }
        finally {
            this.cleanup();
        }
        return false;
    }

    public boolean streamAbort(InputStream wrapped) throws IOException {
        this.cleanup();
        return false;
    }
}

