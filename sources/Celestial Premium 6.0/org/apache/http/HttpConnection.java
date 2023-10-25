/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpConnectionMetrics;

public interface HttpConnection
extends Closeable {
    public void close() throws IOException;

    public boolean isOpen();

    public boolean isStale();

    public void setSocketTimeout(int var1);

    public int getSocketTimeout();

    public void shutdown() throws IOException;

    public HttpConnectionMetrics getMetrics();
}

