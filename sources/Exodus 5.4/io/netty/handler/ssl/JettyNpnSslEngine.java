/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.eclipse.jetty.npn.NextProtoNego
 *  org.eclipse.jetty.npn.NextProtoNego$ClientProvider
 *  org.eclipse.jetty.npn.NextProtoNego$Provider
 *  org.eclipse.jetty.npn.NextProtoNego$ServerProvider
 */
package io.netty.handler.ssl;

import io.netty.handler.ssl.JettyNpnSslSession;
import java.nio.ByteBuffer;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import org.eclipse.jetty.npn.NextProtoNego;

final class JettyNpnSslEngine
extends SSLEngine {
    private static boolean available;
    private final JettyNpnSslSession session;
    private final SSLEngine engine;

    @Override
    public SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        return this.engine.getHandshakeStatus();
    }

    @Override
    public void closeInbound() throws SSLException {
        NextProtoNego.remove((SSLEngine)this.engine);
        this.engine.closeInbound();
    }

    @Override
    public String[] getSupportedProtocols() {
        return this.engine.getSupportedProtocols();
    }

    @Override
    public void beginHandshake() throws SSLException {
        this.engine.beginHandshake();
    }

    private static void updateAvailability() {
        if (available) {
            return;
        }
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader().getParent();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            Class.forName("sun.security.ssl.NextProtoNegoExtension", true, classLoader);
            available = true;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    public boolean getUseClientMode() {
        return this.engine.getUseClientMode();
    }

    @Override
    public void setEnabledCipherSuites(String[] stringArray) {
        this.engine.setEnabledCipherSuites(stringArray);
    }

    @Override
    public boolean getNeedClientAuth() {
        return this.engine.getNeedClientAuth();
    }

    @Override
    public boolean isOutboundDone() {
        return this.engine.isOutboundDone();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return this.engine.getSupportedCipherSuites();
    }

    @Override
    public String[] getEnabledProtocols() {
        return this.engine.getEnabledProtocols();
    }

    @Override
    public void closeOutbound() {
        NextProtoNego.remove((SSLEngine)this.engine);
        this.engine.closeOutbound();
    }

    @Override
    public SSLParameters getSSLParameters() {
        return this.engine.getSSLParameters();
    }

    @Override
    public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBuffer2);
    }

    @Override
    public void setEnableSessionCreation(boolean bl) {
        this.engine.setEnableSessionCreation(bl);
    }

    @Override
    public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArray, int n, int n2) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBufferArray, n, n2);
    }

    @Override
    public boolean getEnableSessionCreation() {
        return this.engine.getEnableSessionCreation();
    }

    @Override
    public void setSSLParameters(SSLParameters sSLParameters) {
        this.engine.setSSLParameters(sSLParameters);
    }

    @Override
    public boolean getWantClientAuth() {
        return this.engine.getWantClientAuth();
    }

    @Override
    public void setNeedClientAuth(boolean bl) {
        this.engine.setNeedClientAuth(bl);
    }

    @Override
    public SSLEngineResult wrap(ByteBuffer[] byteBufferArray, int n, int n2, ByteBuffer byteBuffer) throws SSLException {
        return this.engine.wrap(byteBufferArray, n, n2, byteBuffer);
    }

    @Override
    public SSLSession getHandshakeSession() {
        return this.engine.getHandshakeSession();
    }

    @Override
    public SSLEngineResult wrap(ByteBuffer[] byteBufferArray, ByteBuffer byteBuffer) throws SSLException {
        return this.engine.wrap(byteBufferArray, byteBuffer);
    }

    @Override
    public JettyNpnSslSession getSession() {
        return this.session;
    }

    @Override
    public int getPeerPort() {
        return this.engine.getPeerPort();
    }

    @Override
    public void setEnabledProtocols(String[] stringArray) {
        this.engine.setEnabledProtocols(stringArray);
    }

    static boolean isAvailable() {
        JettyNpnSslEngine.updateAvailability();
        return available;
    }

    @Override
    public void setUseClientMode(boolean bl) {
        this.engine.setUseClientMode(bl);
    }

    JettyNpnSslEngine(SSLEngine sSLEngine, final List<String> list, boolean bl) {
        assert (!list.isEmpty());
        this.engine = sSLEngine;
        this.session = new JettyNpnSslSession(sSLEngine);
        if (bl) {
            NextProtoNego.put((SSLEngine)sSLEngine, (NextProtoNego.Provider)new NextProtoNego.ServerProvider(){

                public void unsupported() {
                    ((JettyNpnSslSession)JettyNpnSslEngine.this.getSession()).setApplicationProtocol((String)list.get(list.size() - 1));
                }

                public List<String> protocols() {
                    return list;
                }

                public void protocolSelected(String string) {
                    ((JettyNpnSslSession)JettyNpnSslEngine.this.getSession()).setApplicationProtocol(string);
                }
            });
        } else {
            final String[] stringArray = list.toArray(new String[list.size()]);
            final String string = stringArray[stringArray.length - 1];
            NextProtoNego.put((SSLEngine)sSLEngine, (NextProtoNego.Provider)new NextProtoNego.ClientProvider(){

                public String selectProtocol(List<String> list) {
                    for (String string2 : stringArray) {
                        if (!list.contains(string2)) continue;
                        return string2;
                    }
                    return string;
                }

                public boolean supports() {
                    return true;
                }

                public void unsupported() {
                    JettyNpnSslEngine.this.session.setApplicationProtocol(null);
                }
            });
        }
    }

    @Override
    public SSLEngineResult wrap(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws SSLException {
        return this.engine.wrap(byteBuffer, byteBuffer2);
    }

    @Override
    public String[] getEnabledCipherSuites() {
        return this.engine.getEnabledCipherSuites();
    }

    @Override
    public String getPeerHost() {
        return this.engine.getPeerHost();
    }

    @Override
    public SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArray) throws SSLException {
        return this.engine.unwrap(byteBuffer, byteBufferArray);
    }

    @Override
    public Runnable getDelegatedTask() {
        return this.engine.getDelegatedTask();
    }

    @Override
    public void setWantClientAuth(boolean bl) {
        this.engine.setWantClientAuth(bl);
    }

    @Override
    public boolean isInboundDone() {
        return this.engine.isInboundDone();
    }
}

