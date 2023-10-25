/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.scheme.SchemeSocketFactory;

@Deprecated
public interface LayeredSchemeSocketFactory
extends SchemeSocketFactory {
    public Socket createLayeredSocket(Socket var1, String var2, int var3, boolean var4) throws IOException, UnknownHostException;
}

