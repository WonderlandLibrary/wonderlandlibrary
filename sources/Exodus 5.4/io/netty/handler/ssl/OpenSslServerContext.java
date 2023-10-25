/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tomcat.jni.Pool
 *  org.apache.tomcat.jni.SSL
 *  org.apache.tomcat.jni.SSLContext
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.OpenSslEngine;
import io.netty.handler.ssl.OpenSslSessionStats;
import io.netty.handler.ssl.SslContext;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import org.apache.tomcat.jni.Pool;
import org.apache.tomcat.jni.SSL;
import org.apache.tomcat.jni.SSLContext;

public final class OpenSslServerContext
extends SslContext {
    private final long sessionCacheSize;
    private final List<String> unmodifiableCiphers;
    private final long sessionTimeout;
    private final long aprPool;
    private final OpenSslSessionStats stats;
    private final long ctx;
    private final List<String> ciphers = new ArrayList<String>();
    private final List<String> nextProtocols;
    private static final List<String> DEFAULT_CIPHERS;
    private static final InternalLogger logger;

    @Override
    public List<String> nextProtocols() {
        return this.nextProtocols;
    }

    @Override
    public SSLEngine newEngine(ByteBufAllocator byteBufAllocator, String string, int n) {
        throw new UnsupportedOperationException();
    }

    public OpenSslSessionStats stats() {
        return this.stats;
    }

    @Override
    public List<String> cipherSuites() {
        return this.unmodifiableCiphers;
    }

    public void setTicketKeys(byte[] byArray) {
        if (byArray == null) {
            throw new NullPointerException("keys");
        }
        SSLContext.setSessionTicketKeys((long)this.ctx, (byte[])byArray);
    }

    @Override
    public long sessionTimeout() {
        return this.sessionTimeout;
    }

    public long context() {
        return this.ctx;
    }

    public OpenSslServerContext(File file, File file2, String string) throws SSLException {
        this(file, file2, string, null, null, 0L, 0L);
    }

    protected void finalize() throws Throwable {
        super.finalize();
        Class<OpenSslServerContext> clazz = OpenSslServerContext.class;
        synchronized (OpenSslServerContext.class) {
            if (this.ctx != 0L) {
                SSLContext.free((long)this.ctx);
            }
            // ** MonitorExit[var1_1] (shouldn't be in output)
            this.destroyPools();
            return;
        }
    }

    @Override
    public boolean isClient() {
        return false;
    }

    private void destroyPools() {
        if (this.aprPool != 0L) {
            Pool.destroy((long)this.aprPool);
        }
    }

    static {
        logger = InternalLoggerFactory.getInstance(OpenSslServerContext.class);
        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, "ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-RC4-SHA", "ECDHE-RSA-AES128-SHA", "ECDHE-RSA-AES256-SHA", "AES128-GCM-SHA256", "RC4-SHA", "RC4-MD5", "AES128-SHA", "AES256-SHA", "DES-CBC3-SHA");
        DEFAULT_CIPHERS = Collections.unmodifiableList(arrayList);
        if (logger.isDebugEnabled()) {
            logger.debug("Default cipher suite (OpenSSL): " + arrayList);
        }
    }

    public OpenSslServerContext(File file, File file2, String string, Iterable<String> iterable, Iterable<String> iterable2, long l, long l2) throws SSLException {
        Object object2;
        this.unmodifiableCiphers = Collections.unmodifiableList(this.ciphers);
        OpenSsl.ensureAvailability();
        if (file == null) {
            throw new NullPointerException("certChainFile");
        }
        if (!file.isFile()) {
            throw new IllegalArgumentException("certChainFile is not a file: " + file);
        }
        if (file2 == null) {
            throw new NullPointerException("keyPath");
        }
        if (!file2.isFile()) {
            throw new IllegalArgumentException("keyPath is not a file: " + file2);
        }
        if (iterable == null) {
            iterable = DEFAULT_CIPHERS;
        }
        if (string == null) {
            string = "";
        }
        if (iterable2 == null) {
            iterable2 = Collections.emptyList();
        }
        for (String object22 : iterable) {
            if (object22 == null) break;
            this.ciphers.add(object22);
        }
        ArrayList arrayList = new ArrayList();
        for (Object object2 : iterable2) {
            if (object2 == null) break;
            arrayList.add(object2);
        }
        this.nextProtocols = Collections.unmodifiableList(arrayList);
        this.aprPool = Pool.create((long)0L);
        boolean bl = false;
        object2 = OpenSslServerContext.class;
        synchronized (OpenSslServerContext.class) {
            CharSequence exception;
            try {
                this.ctx = SSLContext.make((long)this.aprPool, (int)6, (int)1);
            }
            catch (Exception sSLException) {
                throw new SSLException("failed to create an SSL_CTX", sSLException);
            }
            SSLContext.setOptions((long)this.ctx, (int)4095);
            SSLContext.setOptions((long)this.ctx, (int)0x1000000);
            SSLContext.setOptions((long)this.ctx, (int)0x400000);
            SSLContext.setOptions((long)this.ctx, (int)524288);
            SSLContext.setOptions((long)this.ctx, (int)0x100000);
            SSLContext.setOptions((long)this.ctx, (int)65536);
            try {
                exception = new StringBuilder();
                for (String string2 : this.ciphers) {
                    ((StringBuilder)exception).append(string2);
                    ((StringBuilder)exception).append(':');
                }
                ((StringBuilder)exception).setLength(((StringBuilder)exception).length() - 1);
                SSLContext.setCipherSuite((long)this.ctx, (String)((StringBuilder)exception).toString());
            }
            catch (SSLException sSLException) {
                throw sSLException;
            }
            catch (Exception exception2) {
                throw new SSLException("failed to set cipher suite: " + this.ciphers, exception2);
            }
            SSLContext.setVerify((long)this.ctx, (int)0, (int)10);
            try {
                if (!SSLContext.setCertificate((long)this.ctx, (String)file.getPath(), (String)file2.getPath(), (String)string, (int)0)) {
                    throw new SSLException("failed to set certificate: " + file + " and " + file2 + " (" + SSL.getLastError() + ')');
                }
            }
            catch (SSLException sSLException) {
                throw sSLException;
            }
            catch (Exception exception3) {
                throw new SSLException("failed to set certificate: " + file + " and " + file2, exception3);
            }
            if (!SSLContext.setCertificateChainFile((long)this.ctx, (String)file.getPath(), (boolean)true) && !((String)(exception = SSL.getLastError())).startsWith("error:00000000:")) {
                throw new SSLException("failed to set certificate chain: " + file + " (" + SSL.getLastError() + ')');
            }
            if (!arrayList.isEmpty()) {
                exception = new StringBuilder();
                Iterator<String> iterator = arrayList.iterator();
                while (iterator.hasNext()) {
                    String string2;
                    string2 = iterator.next();
                    ((StringBuilder)exception).append(string2);
                    ((StringBuilder)exception).append(',');
                }
                ((StringBuilder)exception).setLength(((StringBuilder)exception).length() - 1);
                SSLContext.setNextProtos((long)this.ctx, (String)((StringBuilder)exception).toString());
            }
            if (l > 0L) {
                this.sessionCacheSize = l;
                SSLContext.setSessionCacheSize((long)this.ctx, (long)l);
            } else {
                this.sessionCacheSize = l = SSLContext.setSessionCacheSize((long)this.ctx, (long)20480L);
                SSLContext.setSessionCacheSize((long)this.ctx, (long)l);
            }
            if (l2 > 0L) {
                this.sessionTimeout = l2;
                SSLContext.setSessionCacheTimeout((long)this.ctx, (long)l2);
            } else {
                this.sessionTimeout = l2 = SSLContext.setSessionCacheTimeout((long)this.ctx, (long)300L);
                SSLContext.setSessionCacheTimeout((long)this.ctx, (long)l2);
            }
            // ** MonitorExit[var12_13] (shouldn't be in output)
            boolean bl2 = true;
            if (!bl2) {
                this.destroyPools();
            }
            this.stats = new OpenSslSessionStats(this.ctx);
            return;
        }
    }

    public OpenSslServerContext(File file, File file2) throws SSLException {
        this(file, file2, null);
    }

    @Override
    public long sessionCacheSize() {
        return this.sessionCacheSize;
    }

    @Override
    public SSLEngine newEngine(ByteBufAllocator byteBufAllocator) {
        if (this.nextProtocols.isEmpty()) {
            return new OpenSslEngine(this.ctx, byteBufAllocator, null);
        }
        return new OpenSslEngine(this.ctx, byteBufAllocator, this.nextProtocols.get(this.nextProtocols.size() - 1));
    }
}

