/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnection;

public interface HttpConnectionFactory<T extends HttpConnection> {
    public T createConnection(Socket var1) throws IOException;
}

