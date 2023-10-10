/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl.io;

import org.apache.http.io.HttpTransportMetrics;

public class HttpTransportMetricsImpl
implements HttpTransportMetrics {
    private long bytesTransferred = 0L;

    @Override
    public long getBytesTransferred() {
        return this.bytesTransferred;
    }

    public void setBytesTransferred(long count) {
        this.bytesTransferred = count;
    }

    public void incrementBytesTransferred(long count) {
        this.bytesTransferred += count;
    }

    @Override
    public void reset() {
        this.bytesTransferred = 0L;
    }
}

