/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.JettyNpnSslEngine;
import io.netty.handler.ssl.PemReader;
import java.io.File;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

public final class JdkSslClientContext
extends JdkSslContext {
    private final List<String> nextProtocols;
    private final SSLContext ctx;

    @Override
    public List<String> nextProtocols() {
        return this.nextProtocols;
    }

    public JdkSslClientContext(File file) throws SSLException {
        this(file, null);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public SSLContext context() {
        return this.ctx;
    }

    public JdkSslClientContext(TrustManagerFactory trustManagerFactory) throws SSLException {
        this(null, trustManagerFactory);
    }

    public JdkSslClientContext() throws SSLException {
        this(null, null, null, null, 0L, 0L);
    }

    public JdkSslClientContext(File file, TrustManagerFactory trustManagerFactory, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        super(iterable);
        Object object;
        Object object2;
        Object object3;
        if (iterable2 != null && iterable2.iterator().hasNext()) {
            if (!JettyNpnSslEngine.isAvailable()) {
                throw new SSLException("NPN/ALPN unsupported: " + iterable2);
            }
            object3 = new ArrayList();
            object2 = iterable2.iterator();
            while (object2.hasNext() && (object = (String)object2.next()) != null) {
                object3.add(object);
            }
            this.nextProtocols = Collections.unmodifiableList(object3);
        } else {
            this.nextProtocols = Collections.emptyList();
        }
        try {
            if (file == null) {
                this.ctx = SSLContext.getInstance("TLS");
                if (trustManagerFactory == null) {
                    this.ctx.init(null, null, null);
                } else {
                    trustManagerFactory.init((KeyStore)null);
                    this.ctx.init(null, trustManagerFactory.getTrustManagers(), null);
                }
            } else {
                object3 = KeyStore.getInstance("JKS");
                ((KeyStore)object3).load(null, null);
                object2 = CertificateFactory.getInstance("X.509");
                for (ByteBuf byteBuf : object = PemReader.readCertificates(file)) {
                    X509Certificate x509Certificate = (X509Certificate)((CertificateFactory)object2).generateCertificate(new ByteBufInputStream(byteBuf));
                    X500Principal x500Principal = x509Certificate.getSubjectX500Principal();
                    ((KeyStore)object3).setCertificateEntry(x500Principal.getName("RFC2253"), x509Certificate);
                }
                for (ByteBuf byteBuf : object) {
                    byteBuf.release();
                }
                if (trustManagerFactory == null) {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                }
                trustManagerFactory.init((KeyStore)object3);
                this.ctx = SSLContext.getInstance("TLS");
                this.ctx.init(null, trustManagerFactory.getTrustManagers(), null);
            }
            object3 = this.ctx.getClientSessionContext();
            if (l > 0L) {
                object3.setSessionCacheSize((int)Math.min(l, Integer.MAX_VALUE));
            }
            if (l2 > 0L) {
                object3.setSessionTimeout((int)Math.min(l2, Integer.MAX_VALUE));
            }
        }
        catch (Exception exception) {
            throw new SSLException("failed to initialize the server-side SSL context", exception);
        }
    }

    public JdkSslClientContext(File file, TrustManagerFactory trustManagerFactory) throws SSLException {
        this(file, trustManagerFactory, null, null, 0L, 0L);
    }
}

