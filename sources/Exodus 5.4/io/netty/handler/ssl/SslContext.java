/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.JdkSslClientContext;
import io.netty.handler.ssl.JdkSslServerContext;
import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.OpenSslServerContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;
import java.io.File;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;

public abstract class SslContext {
    public static SslContext newClientContext(SslProvider sslProvider, TrustManagerFactory trustManagerFactory) throws SSLException {
        return SslContext.newClientContext(sslProvider, null, trustManagerFactory, null, null, 0L, 0L);
    }

    public static SslContext newClientContext(SslProvider sslProvider, File file, TrustManagerFactory trustManagerFactory) throws SSLException {
        return SslContext.newClientContext(sslProvider, file, trustManagerFactory, null, null, 0L, 0L);
    }

    SslContext() {
    }

    public final SslHandler newHandler(ByteBufAllocator byteBufAllocator) {
        return SslContext.newHandler(this.newEngine(byteBufAllocator));
    }

    public static SslContext newServerContext(SslProvider sslProvider, File file, File file2) throws SSLException {
        return SslContext.newServerContext(sslProvider, file, file2, null, null, null, 0L, 0L);
    }

    public static SslContext newClientContext() throws SSLException {
        return SslContext.newClientContext(null, null, null, null, null, 0L, 0L);
    }

    private static SslHandler newHandler(SSLEngine sSLEngine) {
        return new SslHandler(sSLEngine);
    }

    public abstract List<String> nextProtocols();

    public static SslContext newClientContext(File file) throws SSLException {
        return SslContext.newClientContext(null, file, null, null, null, 0L, 0L);
    }

    public abstract SSLEngine newEngine(ByteBufAllocator var1);

    public static SslContext newServerContext(SslProvider sslProvider, File file, File file2, String string, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        if (sslProvider == null) {
            sslProvider = OpenSsl.isAvailable() ? SslProvider.OPENSSL : SslProvider.JDK;
        }
        switch (sslProvider) {
            case JDK: {
                return new JdkSslServerContext(file, file2, string, iterable, iterable2, l, l2);
            }
            case OPENSSL: {
                return new OpenSslServerContext(file, file2, string, iterable, iterable2, l, l2);
            }
        }
        throw new Error(sslProvider.toString());
    }

    public static SslContext newServerContext(File file, File file2) throws SSLException {
        return SslContext.newServerContext(null, file, file2, null, null, null, 0L, 0L);
    }

    public abstract long sessionCacheSize();

    public static SslContext newClientContext(File file, TrustManagerFactory trustManagerFactory, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        return SslContext.newClientContext(null, file, trustManagerFactory, iterable, iterable2, l, l2);
    }

    public static SslContext newServerContext(File file, File file2, String string) throws SSLException {
        return SslContext.newServerContext(null, file, file2, string, null, null, 0L, 0L);
    }

    public abstract boolean isClient();

    public static SslContext newClientContext(TrustManagerFactory trustManagerFactory) throws SSLException {
        return SslContext.newClientContext(null, null, trustManagerFactory, null, null, 0L, 0L);
    }

    public static SslContext newClientContext(File file, TrustManagerFactory trustManagerFactory) throws SSLException {
        return SslContext.newClientContext(null, file, trustManagerFactory, null, null, 0L, 0L);
    }

    public static SslContext newServerContext(File file, File file2, String string, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        return SslContext.newServerContext(null, file, file2, string, iterable, iterable2, l, l2);
    }

    public static SslContext newClientContext(SslProvider sslProvider, File file) throws SSLException {
        return SslContext.newClientContext(sslProvider, file, null, null, null, 0L, 0L);
    }

    public final boolean isServer() {
        return !this.isClient();
    }

    public static SslProvider defaultServerProvider() {
        if (OpenSsl.isAvailable()) {
            return SslProvider.OPENSSL;
        }
        return SslProvider.JDK;
    }

    public static SslContext newClientContext(SslProvider sslProvider) throws SSLException {
        return SslContext.newClientContext(sslProvider, null, null, null, null, 0L, 0L);
    }

    public static SslContext newClientContext(SslProvider sslProvider, File file, TrustManagerFactory trustManagerFactory, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        if (sslProvider != null && sslProvider != SslProvider.JDK) {
            throw new SSLException("client context unsupported for: " + (Object)((Object)sslProvider));
        }
        return new JdkSslClientContext(file, trustManagerFactory, iterable, iterable2, l, l2);
    }

    public final SslHandler newHandler(ByteBufAllocator byteBufAllocator, String string, int n) {
        return SslContext.newHandler(this.newEngine(byteBufAllocator, string, n));
    }

    public static SslProvider defaultClientProvider() {
        return SslProvider.JDK;
    }

    public abstract SSLEngine newEngine(ByteBufAllocator var1, String var2, int var3);

    public abstract long sessionTimeout();

    public static SslContext newServerContext(SslProvider sslProvider, File file, File file2, String string) throws SSLException {
        return SslContext.newServerContext(sslProvider, file, file2, string, null, null, 0L, 0L);
    }

    public abstract List<String> cipherSuites();
}

