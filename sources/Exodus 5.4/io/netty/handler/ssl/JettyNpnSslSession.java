/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

import java.security.Principal;
import java.security.cert.Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;

final class JettyNpnSslSession
implements SSLSession {
    private volatile String applicationProtocol;
    private final SSLEngine engine;

    @Override
    public Object getValue(String string) {
        return this.unwrap().getValue(string);
    }

    private SSLSession unwrap() {
        return this.engine.getSession();
    }

    @Override
    public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerPrincipal();
    }

    @Override
    public void invalidate() {
        this.unwrap().invalidate();
    }

    @Override
    public void putValue(String string, Object object) {
        this.unwrap().putValue(string, object);
    }

    @Override
    public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerCertificates();
    }

    @Override
    public boolean isValid() {
        return this.unwrap().isValid();
    }

    @Override
    public String[] getValueNames() {
        return this.unwrap().getValueNames();
    }

    @Override
    public String getPeerHost() {
        return this.unwrap().getPeerHost();
    }

    @Override
    public String getCipherSuite() {
        return this.unwrap().getCipherSuite();
    }

    @Override
    public void removeValue(String string) {
        this.unwrap().removeValue(string);
    }

    JettyNpnSslSession(SSLEngine sSLEngine) {
        this.engine = sSLEngine;
    }

    @Override
    public byte[] getId() {
        return this.unwrap().getId();
    }

    @Override
    public Principal getLocalPrincipal() {
        return this.unwrap().getLocalPrincipal();
    }

    @Override
    public String getProtocol() {
        String string = this.unwrap().getProtocol();
        String string2 = this.applicationProtocol;
        if (string2 == null) {
            if (string != null) {
                return string.replace(':', '_');
            }
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(32);
        if (string != null) {
            stringBuilder.append(string.replace(':', '_'));
            stringBuilder.append(':');
        } else {
            stringBuilder.append("null:");
        }
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }

    @Override
    public int getApplicationBufferSize() {
        return this.unwrap().getApplicationBufferSize();
    }

    @Override
    public long getCreationTime() {
        return this.unwrap().getCreationTime();
    }

    void setApplicationProtocol(String string) {
        if (string != null) {
            string = string.replace(':', '_');
        }
        this.applicationProtocol = string;
    }

    @Override
    public int getPeerPort() {
        return this.unwrap().getPeerPort();
    }

    @Override
    public SSLSessionContext getSessionContext() {
        return this.unwrap().getSessionContext();
    }

    @Override
    public Certificate[] getLocalCertificates() {
        return this.unwrap().getLocalCertificates();
    }

    @Override
    public long getLastAccessedTime() {
        return this.unwrap().getLastAccessedTime();
    }

    @Override
    public int getPacketBufferSize() {
        return this.unwrap().getPacketBufferSize();
    }

    @Override
    public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
        return this.unwrap().getPeerCertificateChain();
    }
}

