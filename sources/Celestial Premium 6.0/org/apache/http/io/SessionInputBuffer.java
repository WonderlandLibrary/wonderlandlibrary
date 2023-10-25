/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.io;

import java.io.IOException;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.util.CharArrayBuffer;

public interface SessionInputBuffer {
    public int read(byte[] var1, int var2, int var3) throws IOException;

    public int read(byte[] var1) throws IOException;

    public int read() throws IOException;

    public int readLine(CharArrayBuffer var1) throws IOException;

    public String readLine() throws IOException;

    @Deprecated
    public boolean isDataAvailable(int var1) throws IOException;

    public HttpTransportMetrics getMetrics();
}

