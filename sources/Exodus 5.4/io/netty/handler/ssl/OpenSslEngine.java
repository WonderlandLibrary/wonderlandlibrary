/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.tomcat.jni.Buffer
 *  org.apache.tomcat.jni.SSL
 */
package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.OpenSsl;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSessionContext;
import javax.security.cert.X509Certificate;
import org.apache.tomcat.jni.Buffer;
import org.apache.tomcat.jni.SSL;

public final class OpenSslEngine
extends SSLEngine {
    private static final int MAX_PLAINTEXT_LENGTH = 16384;
    private boolean isInboundDone;
    private volatile int destroyed;
    private static final SSLException ENGINE_CLOSED;
    private long networkBIO;
    private SSLSession session;
    private static final SSLException RENEGOTIATION_UNSUPPORTED;
    private static final int MAX_COMPRESSED_LENGTH = 17408;
    private boolean handshakeFinished;
    static final int MAX_ENCRYPTED_PACKET_LENGTH = 18713;
    private boolean engineClosed;
    private int lastPrimingReadResult;
    private static final AtomicIntegerFieldUpdater<OpenSslEngine> DESTROYED_UPDATER;
    private final ByteBufAllocator alloc;
    private static final SSLException ENCRYPTED_PACKET_OVERSIZED;
    private static final Certificate[] EMPTY_CERTIFICATES;
    private static final int MAX_CIPHERTEXT_LENGTH = 18432;
    private long ssl;
    private boolean receivedShutdown;
    private volatile String applicationProtocol;
    private final String fallbackApplicationProtocol;
    private boolean isOutboundDone;
    private static final InternalLogger logger;
    static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 2329;
    private String cipher;
    private int accepted;
    private static final X509Certificate[] EMPTY_X509_CERTIFICATES;

    @Override
    public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        if (this.accepted == 0 || this.destroyed != 0) {
            return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        }
        if (!this.handshakeFinished) {
            if (SSL.pendingWrittenBytesInBIO((long)this.networkBIO) != 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            if (SSL.isInInit((long)this.ssl) == 0) {
                this.handshakeFinished = true;
                this.cipher = SSL.getCipherForSSL((long)this.ssl);
                String string = SSL.getNextProtoNegotiated((long)this.ssl);
                if (string == null) {
                    string = this.fallbackApplicationProtocol;
                }
                this.applicationProtocol = string != null ? string.replace(':', '_') : null;
                return SSLEngineResult.HandshakeStatus.FINISHED;
            }
            return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        }
        if (this.engineClosed) {
            if (SSL.pendingWrittenBytesInBIO((long)this.networkBIO) != 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
        }
        return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }

    @Override
    public String[] getEnabledProtocols() {
        return EmptyArrays.EMPTY_STRINGS;
    }

    private int writeEncryptedData(ByteBuffer byteBuffer) {
        int n = byteBuffer.position();
        int n2 = byteBuffer.remaining();
        if (byteBuffer.isDirect()) {
            long l = Buffer.address((ByteBuffer)byteBuffer) + (long)n;
            int n3 = SSL.writeToBIO((long)this.networkBIO, (long)l, (int)n2);
            if (n3 >= 0) {
                byteBuffer.position(n + n3);
                this.lastPrimingReadResult = SSL.readFromSSL((long)this.ssl, (long)l, (int)0);
                return n3;
            }
        } else {
            ByteBuf byteBuf = this.alloc.directBuffer(n2);
            long l = byteBuf.hasMemoryAddress() ? byteBuf.memoryAddress() : Buffer.address((ByteBuffer)byteBuf.nioBuffer());
            byteBuf.setBytes(0, byteBuffer);
            int n4 = SSL.writeToBIO((long)this.networkBIO, (long)l, (int)n2);
            if (n4 >= 0) {
                byteBuffer.position(n + n4);
                this.lastPrimingReadResult = SSL.readFromSSL((long)this.ssl, (long)l, (int)0);
                int n5 = n4;
                byteBuf.release();
                return n5;
            }
            byteBuffer.position(n);
            byteBuf.release();
        }
        return 0;
    }

    @Override
    public synchronized void closeInbound() throws SSLException {
        if (this.isInboundDone) {
            return;
        }
        this.isInboundDone = true;
        this.engineClosed = true;
        if (this.accepted != 0) {
            if (!this.receivedShutdown) {
                this.shutdown();
                throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
            }
        } else {
            this.shutdown();
        }
    }

    public synchronized void shutdown() {
        if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
            SSL.freeSSL((long)this.ssl);
            SSL.freeBIO((long)this.networkBIO);
            this.networkBIO = 0L;
            this.ssl = 0L;
            this.engineClosed = true;
            this.isOutboundDone = true;
            this.isInboundDone = true;
        }
    }

    @Override
    public boolean getNeedClientAuth() {
        return false;
    }

    @Override
    public void setEnableSessionCreation(boolean bl) {
        if (bl) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public synchronized boolean isInboundDone() {
        return this.isInboundDone || this.engineClosed;
    }

    @Override
    public boolean getEnableSessionCreation() {
        return false;
    }

    @Override
    public void setUseClientMode(boolean bl) {
        if (bl) {
            throw new UnsupportedOperationException();
        }
    }

    static {
        logger = InternalLoggerFactory.getInstance(OpenSslEngine.class);
        EMPTY_CERTIFICATES = new Certificate[0];
        EMPTY_X509_CERTIFICATES = new X509Certificate[0];
        ENGINE_CLOSED = new SSLException("engine closed");
        RENEGOTIATION_UNSUPPORTED = new SSLException("renegotiation unsupported");
        ENCRYPTED_PACKET_OVERSIZED = new SSLException("encrypted packet oversized");
        ENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        RENEGOTIATION_UNSUPPORTED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        ENCRYPTED_PACKET_OVERSIZED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(OpenSslEngine.class, "destroyed");
    }

    @Override
    public boolean getWantClientAuth() {
        return false;
    }

    @Override
    public void setEnabledProtocols(String[] stringArray) {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized SSLEngineResult wrap(ByteBuffer[] byteBufferArray, int n, int n2, ByteBuffer byteBuffer) throws SSLException {
        if (this.destroyed != 0) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
        }
        if (byteBufferArray == null) {
            throw new NullPointerException("srcs");
        }
        if (byteBuffer == null) {
            throw new NullPointerException("dst");
        }
        if (n >= byteBufferArray.length || n + n2 > byteBufferArray.length) {
            throw new IndexOutOfBoundsException("offset: " + n + ", length: " + n2 + " (expected: offset <= offset + length <= srcs.length (" + byteBufferArray.length + "))");
        }
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (this.accepted == 0) {
            this.beginHandshakeImplicitly();
        }
        SSLEngineResult.HandshakeStatus handshakeStatus = this.getHandshakeStatus();
        if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
            return new SSLEngineResult(this.getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
        }
        int n3 = 0;
        int n4 = SSL.pendingWrittenBytesInBIO((long)this.networkBIO);
        if (n4 > 0) {
            int n5 = byteBuffer.remaining();
            if (n5 < n4) {
                return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus, 0, n3);
            }
            try {
                n3 += this.readEncryptedData(byteBuffer, n4);
            }
            catch (Exception exception) {
                throw new SSLException(exception);
            }
            if (this.isOutboundDone) {
                this.shutdown();
            }
            return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), 0, n3);
        }
        int n6 = 0;
        for (int i = n; i < n2; ++i) {
            ByteBuffer byteBuffer2 = byteBufferArray[i];
            while (byteBuffer2.hasRemaining()) {
                try {
                    n6 += this.writePlaintextData(byteBuffer2);
                }
                catch (Exception exception) {
                    throw new SSLException(exception);
                }
                n4 = SSL.pendingWrittenBytesInBIO((long)this.networkBIO);
                if (n4 <= 0) continue;
                int n7 = byteBuffer.remaining();
                if (n7 < n4) {
                    return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), n6, n3);
                }
                try {
                }
                catch (Exception exception) {
                    throw new SSLException(exception);
                }
                return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), n6, n3 += this.readEncryptedData(byteBuffer, n4));
            }
        }
        return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), n6, n3);
    }

    @Override
    public synchronized boolean isOutboundDone() {
        return this.isOutboundDone;
    }

    @Override
    public String[] getSupportedProtocols() {
        return EmptyArrays.EMPTY_STRINGS;
    }

    @Override
    public synchronized SSLEngineResult unwrap(ByteBuffer byteBuffer, ByteBuffer[] byteBufferArray, int n, int n2) throws SSLException {
        int n3;
        if (this.destroyed != 0) {
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
        }
        if (byteBuffer == null) {
            throw new NullPointerException("src");
        }
        if (byteBufferArray == null) {
            throw new NullPointerException("dsts");
        }
        if (n >= byteBufferArray.length || n + n2 > byteBufferArray.length) {
            throw new IndexOutOfBoundsException("offset: " + n + ", length: " + n2 + " (expected: offset <= offset + length <= dsts.length (" + byteBufferArray.length + "))");
        }
        int n4 = 0;
        int n5 = n + n2;
        for (int i = n; i < n5; ++i) {
            ByteBuffer byteBuffer2 = byteBufferArray[i];
            if (byteBuffer2 == null) {
                throw new IllegalArgumentException();
            }
            if (byteBuffer2.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            n4 += byteBuffer2.remaining();
        }
        if (this.accepted == 0) {
            this.beginHandshakeImplicitly();
        }
        SSLEngineResult.HandshakeStatus handshakeStatus = this.getHandshakeStatus();
        if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
            return new SSLEngineResult(this.getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
        }
        if (byteBuffer.remaining() > 18713) {
            this.isInboundDone = true;
            this.isOutboundDone = true;
            this.engineClosed = true;
            this.shutdown();
            throw ENCRYPTED_PACKET_OVERSIZED;
        }
        int n6 = 0;
        this.lastPrimingReadResult = 0;
        try {
            n6 += this.writeEncryptedData(byteBuffer);
        }
        catch (Exception exception) {
            throw new SSLException(exception);
        }
        String string = SSL.getLastError();
        if (string != null && !string.startsWith("error:00000000:")) {
            if (logger.isInfoEnabled()) {
                logger.info("SSL_read failed: primingReadResult: " + this.lastPrimingReadResult + "; OpenSSL error: '" + string + '\'');
            }
            this.shutdown();
            throw new SSLException(string);
        }
        int n7 = n3 = SSL.isInInit((long)this.ssl) == 0 ? SSL.pendingReadableBytesInSSL((long)this.ssl) : 0;
        if (n4 < n3) {
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), n6, 0);
        }
        int n8 = 0;
        int n9 = n;
        while (n9 < n5) {
            int n10;
            ByteBuffer byteBuffer3 = byteBufferArray[n9];
            if (!byteBuffer3.hasRemaining()) {
                ++n9;
                continue;
            }
            if (n3 <= 0) break;
            try {
                n10 = this.readPlaintextData(byteBuffer3);
            }
            catch (Exception exception) {
                throw new SSLException(exception);
            }
            if (n10 == 0) break;
            n8 += n10;
            n3 -= n10;
            if (byteBuffer3.hasRemaining()) continue;
            ++n9;
        }
        if (!this.receivedShutdown && (SSL.getShutdown((long)this.ssl) & 2) == 2) {
            this.receivedShutdown = true;
            this.closeOutbound();
            this.closeInbound();
        }
        return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), n6, n8);
    }

    @Override
    public boolean getUseClientMode() {
        return false;
    }

    @Override
    public Runnable getDelegatedTask() {
        return null;
    }

    @Override
    public synchronized void beginHandshake() throws SSLException {
        if (this.engineClosed) {
            throw ENGINE_CLOSED;
        }
        switch (this.accepted) {
            case 0: {
                SSL.doHandshake((long)this.ssl);
                this.accepted = 2;
                break;
            }
            case 1: {
                this.accepted = 2;
                break;
            }
            case 2: {
                throw RENEGOTIATION_UNSUPPORTED;
            }
            default: {
                throw new Error();
            }
        }
    }

    private int writePlaintextData(ByteBuffer byteBuffer) {
        int n;
        int n2 = byteBuffer.position();
        int n3 = byteBuffer.limit();
        int n4 = Math.min(n3 - n2, 16384);
        if (byteBuffer.isDirect()) {
            long l = Buffer.address((ByteBuffer)byteBuffer) + (long)n2;
            n = SSL.writeToSSL((long)this.ssl, (long)l, (int)n4);
            if (n > 0) {
                byteBuffer.position(n2 + n);
                return n;
            }
        } else {
            ByteBuf byteBuf = this.alloc.directBuffer(n4);
            long l = byteBuf.hasMemoryAddress() ? byteBuf.memoryAddress() : Buffer.address((ByteBuffer)byteBuf.nioBuffer());
            byteBuffer.limit(n2 + n4);
            byteBuf.setBytes(0, byteBuffer);
            byteBuffer.limit(n3);
            n = SSL.writeToSSL((long)this.ssl, (long)l, (int)n4);
            if (n > 0) {
                byteBuffer.position(n2 + n);
                int n5 = n;
                byteBuf.release();
                return n5;
            }
            byteBuffer.position(n2);
            byteBuf.release();
        }
        throw new IllegalStateException("SSL.writeToSSL() returned a non-positive value: " + n);
    }

    @Override
    public SSLSession getSession() {
        SSLSession sSLSession = this.session;
        if (sSLSession == null) {
            this.session = sSLSession = new SSLSession(){

                @Override
                public Object getValue(String string) {
                    return null;
                }

                @Override
                public String[] getValueNames() {
                    return EmptyArrays.EMPTY_STRINGS;
                }

                @Override
                public int getPeerPort() {
                    return 0;
                }

                @Override
                public void putValue(String string, Object object) {
                }

                @Override
                public Principal getLocalPrincipal() {
                    return null;
                }

                @Override
                public String getPeerHost() {
                    return null;
                }

                @Override
                public Certificate[] getPeerCertificates() {
                    return EMPTY_CERTIFICATES;
                }

                @Override
                public int getApplicationBufferSize() {
                    return 16384;
                }

                @Override
                public boolean isValid() {
                    return false;
                }

                @Override
                public SSLSessionContext getSessionContext() {
                    return null;
                }

                @Override
                public X509Certificate[] getPeerCertificateChain() {
                    return EMPTY_X509_CERTIFICATES;
                }

                @Override
                public byte[] getId() {
                    return String.valueOf(OpenSslEngine.this.ssl).getBytes();
                }

                @Override
                public long getCreationTime() {
                    return 0L;
                }

                @Override
                public void removeValue(String string) {
                }

                @Override
                public int getPacketBufferSize() {
                    return 18713;
                }

                @Override
                public void invalidate() {
                }

                @Override
                public String getProtocol() {
                    String string = OpenSslEngine.this.applicationProtocol;
                    if (string == null) {
                        return "unknown";
                    }
                    return "unknown:" + string;
                }

                @Override
                public long getLastAccessedTime() {
                    return 0L;
                }

                @Override
                public String getCipherSuite() {
                    return OpenSslEngine.this.cipher;
                }

                @Override
                public Principal getPeerPrincipal() {
                    return null;
                }

                @Override
                public Certificate[] getLocalCertificates() {
                    return EMPTY_CERTIFICATES;
                }
            };
        }
        return sSLSession;
    }

    @Override
    public void setEnabledCipherSuites(String[] stringArray) {
        throw new UnsupportedOperationException();
    }

    private int readPlaintextData(ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            int n;
            int n2 = byteBuffer.position();
            long l = Buffer.address((ByteBuffer)byteBuffer) + (long)n2;
            int n3 = SSL.readFromSSL((long)this.ssl, (long)l, (int)(n = byteBuffer.limit() - n2));
            if (n3 > 0) {
                byteBuffer.position(n2 + n3);
                return n3;
            }
        } else {
            int n = byteBuffer.position();
            int n4 = byteBuffer.limit();
            int n5 = Math.min(18713, n4 - n);
            ByteBuf byteBuf = this.alloc.directBuffer(n5);
            long l = byteBuf.hasMemoryAddress() ? byteBuf.memoryAddress() : Buffer.address((ByteBuffer)byteBuf.nioBuffer());
            int n6 = SSL.readFromSSL((long)this.ssl, (long)l, (int)n5);
            if (n6 > 0) {
                byteBuffer.limit(n + n6);
                byteBuf.getBytes(0, byteBuffer);
                byteBuffer.limit(n4);
                int n7 = n6;
                byteBuf.release();
                return n7;
            }
            byteBuf.release();
        }
        return 0;
    }

    @Override
    public void setWantClientAuth(boolean bl) {
        if (bl) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public String[] getEnabledCipherSuites() {
        return EmptyArrays.EMPTY_STRINGS;
    }

    @Override
    public synchronized void closeOutbound() {
        if (this.isOutboundDone) {
            return;
        }
        this.isOutboundDone = true;
        this.engineClosed = true;
        if (this.accepted != 0 && this.destroyed == 0) {
            int n = SSL.getShutdown((long)this.ssl);
            if ((n & 1) != 1) {
                SSL.shutdownSSL((long)this.ssl);
            }
        } else {
            this.shutdown();
        }
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return EmptyArrays.EMPTY_STRINGS;
    }

    @Override
    public void setNeedClientAuth(boolean bl) {
        if (bl) {
            throw new UnsupportedOperationException();
        }
    }

    private synchronized void beginHandshakeImplicitly() throws SSLException {
        if (this.engineClosed) {
            throw ENGINE_CLOSED;
        }
        if (this.accepted == 0) {
            SSL.doHandshake((long)this.ssl);
            this.accepted = 1;
        }
    }

    private int readEncryptedData(ByteBuffer byteBuffer, int n) {
        if (byteBuffer.isDirect() && byteBuffer.remaining() >= n) {
            int n2 = byteBuffer.position();
            long l = Buffer.address((ByteBuffer)byteBuffer) + (long)n2;
            int n3 = SSL.readFromBIO((long)this.networkBIO, (long)l, (int)n);
            if (n3 > 0) {
                byteBuffer.position(n2 + n3);
                return n3;
            }
        } else {
            ByteBuf byteBuf = this.alloc.directBuffer(n);
            long l = byteBuf.hasMemoryAddress() ? byteBuf.memoryAddress() : Buffer.address((ByteBuffer)byteBuf.nioBuffer());
            int n4 = SSL.readFromBIO((long)this.networkBIO, (long)l, (int)n);
            if (n4 > 0) {
                int n5 = byteBuffer.limit();
                byteBuffer.limit(byteBuffer.position() + n4);
                byteBuf.getBytes(0, byteBuffer);
                byteBuffer.limit(n5);
                int n6 = n4;
                byteBuf.release();
                return n6;
            }
            byteBuf.release();
        }
        return 0;
    }

    private SSLEngineResult.Status getEngineStatus() {
        return this.engineClosed ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
    }

    public OpenSslEngine(long l, ByteBufAllocator byteBufAllocator, String string) {
        OpenSsl.ensureAvailability();
        if (l == 0L) {
            throw new NullPointerException("sslContext");
        }
        if (byteBufAllocator == null) {
            throw new NullPointerException("alloc");
        }
        this.alloc = byteBufAllocator;
        this.ssl = SSL.newSSL((long)l, (boolean)true);
        this.networkBIO = SSL.makeNetworkBIO((long)this.ssl);
        this.fallbackApplicationProtocol = string;
    }
}

