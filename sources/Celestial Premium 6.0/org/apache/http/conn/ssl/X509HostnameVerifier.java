/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn.ssl;

import java.io.IOException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;

public interface X509HostnameVerifier
extends HostnameVerifier {
    public void verify(String var1, SSLSocket var2) throws IOException;

    public void verify(String var1, X509Certificate var2) throws SSLException;

    public void verify(String var1, String[] var2, String[] var3) throws SSLException;
}

