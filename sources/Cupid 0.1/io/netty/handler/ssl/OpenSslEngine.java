package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
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

public final class OpenSslEngine extends SSLEngine {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslEngine.class);
  
  private static final Certificate[] EMPTY_CERTIFICATES = new Certificate[0];
  
  private static final X509Certificate[] EMPTY_X509_CERTIFICATES = new X509Certificate[0];
  
  private static final SSLException ENGINE_CLOSED = new SSLException("engine closed");
  
  private static final SSLException RENEGOTIATION_UNSUPPORTED = new SSLException("renegotiation unsupported");
  
  private static final SSLException ENCRYPTED_PACKET_OVERSIZED = new SSLException("encrypted packet oversized");
  
  private static final int MAX_PLAINTEXT_LENGTH = 16384;
  
  private static final int MAX_COMPRESSED_LENGTH = 17408;
  
  private static final int MAX_CIPHERTEXT_LENGTH = 18432;
  
  static final int MAX_ENCRYPTED_PACKET_LENGTH = 18713;
  
  static final int MAX_ENCRYPTION_OVERHEAD_LENGTH = 2329;
  
  static {
    ENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    RENEGOTIATION_UNSUPPORTED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    ENCRYPTED_PACKET_OVERSIZED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
  }
  
  private static final AtomicIntegerFieldUpdater<OpenSslEngine> DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(OpenSslEngine.class, "destroyed");
  
  private long ssl;
  
  private long networkBIO;
  
  private int accepted;
  
  private boolean handshakeFinished;
  
  private boolean receivedShutdown;
  
  private volatile int destroyed;
  
  private String cipher;
  
  private volatile String applicationProtocol;
  
  private boolean isInboundDone;
  
  private boolean isOutboundDone;
  
  private boolean engineClosed;
  
  private int lastPrimingReadResult;
  
  private final ByteBufAllocator alloc;
  
  private final String fallbackApplicationProtocol;
  
  private SSLSession session;
  
  public OpenSslEngine(long sslCtx, ByteBufAllocator alloc, String fallbackApplicationProtocol) {
    OpenSsl.ensureAvailability();
    if (sslCtx == 0L)
      throw new NullPointerException("sslContext"); 
    if (alloc == null)
      throw new NullPointerException("alloc"); 
    this.alloc = alloc;
    this.ssl = SSL.newSSL(sslCtx, true);
    this.networkBIO = SSL.makeNetworkBIO(this.ssl);
    this.fallbackApplicationProtocol = fallbackApplicationProtocol;
  }
  
  public synchronized void shutdown() {
    if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
      SSL.freeSSL(this.ssl);
      SSL.freeBIO(this.networkBIO);
      this.ssl = this.networkBIO = 0L;
      this.isInboundDone = this.isOutboundDone = this.engineClosed = true;
    } 
  }
  
  private int writePlaintextData(ByteBuffer src) {
    int sslWrote, pos = src.position();
    int limit = src.limit();
    int len = Math.min(limit - pos, 16384);
    if (src.isDirect()) {
      long addr = Buffer.address(src) + pos;
      sslWrote = SSL.writeToSSL(this.ssl, addr, len);
      if (sslWrote > 0) {
        src.position(pos + sslWrote);
        return sslWrote;
      } 
    } else {
      ByteBuf buf = this.alloc.directBuffer(len);
      try {
        long addr;
        if (buf.hasMemoryAddress()) {
          addr = buf.memoryAddress();
        } else {
          addr = Buffer.address(buf.nioBuffer());
        } 
        src.limit(pos + len);
        buf.setBytes(0, src);
        src.limit(limit);
        sslWrote = SSL.writeToSSL(this.ssl, addr, len);
        if (sslWrote > 0) {
          src.position(pos + sslWrote);
          return sslWrote;
        } 
        src.position(pos);
      } finally {
        buf.release();
      } 
    } 
    throw new IllegalStateException("SSL.writeToSSL() returned a non-positive value: " + sslWrote);
  }
  
  private int writeEncryptedData(ByteBuffer src) {
    int pos = src.position();
    int len = src.remaining();
    if (src.isDirect()) {
      long addr = Buffer.address(src) + pos;
      int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
      if (netWrote >= 0) {
        src.position(pos + netWrote);
        this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, addr, 0);
        return netWrote;
      } 
    } else {
      ByteBuf buf = this.alloc.directBuffer(len);
      try {
        long addr;
        if (buf.hasMemoryAddress()) {
          addr = buf.memoryAddress();
        } else {
          addr = Buffer.address(buf.nioBuffer());
        } 
        buf.setBytes(0, src);
        int netWrote = SSL.writeToBIO(this.networkBIO, addr, len);
        if (netWrote >= 0) {
          src.position(pos + netWrote);
          this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, addr, 0);
          return netWrote;
        } 
        src.position(pos);
      } finally {
        buf.release();
      } 
    } 
    return 0;
  }
  
  private int readPlaintextData(ByteBuffer dst) {
    if (dst.isDirect()) {
      int pos = dst.position();
      long addr = Buffer.address(dst) + pos;
      int len = dst.limit() - pos;
      int sslRead = SSL.readFromSSL(this.ssl, addr, len);
      if (sslRead > 0) {
        dst.position(pos + sslRead);
        return sslRead;
      } 
    } else {
      int pos = dst.position();
      int limit = dst.limit();
      int len = Math.min(18713, limit - pos);
      ByteBuf buf = this.alloc.directBuffer(len);
      try {
        long addr;
        if (buf.hasMemoryAddress()) {
          addr = buf.memoryAddress();
        } else {
          addr = Buffer.address(buf.nioBuffer());
        } 
        int sslRead = SSL.readFromSSL(this.ssl, addr, len);
        if (sslRead > 0) {
          dst.limit(pos + sslRead);
          buf.getBytes(0, dst);
          dst.limit(limit);
          return sslRead;
        } 
      } finally {
        buf.release();
      } 
    } 
    return 0;
  }
  
  private int readEncryptedData(ByteBuffer dst, int pending) {
    if (dst.isDirect() && dst.remaining() >= pending) {
      int pos = dst.position();
      long addr = Buffer.address(dst) + pos;
      int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
      if (bioRead > 0) {
        dst.position(pos + bioRead);
        return bioRead;
      } 
    } else {
      ByteBuf buf = this.alloc.directBuffer(pending);
      try {
        long addr;
        if (buf.hasMemoryAddress()) {
          addr = buf.memoryAddress();
        } else {
          addr = Buffer.address(buf.nioBuffer());
        } 
        int bioRead = SSL.readFromBIO(this.networkBIO, addr, pending);
        if (bioRead > 0) {
          int oldLimit = dst.limit();
          dst.limit(dst.position() + bioRead);
          buf.getBytes(0, dst);
          dst.limit(oldLimit);
          return bioRead;
        } 
      } finally {
        buf.release();
      } 
    } 
    return 0;
  }
  
  public synchronized SSLEngineResult wrap(ByteBuffer[] srcs, int offset, int length, ByteBuffer dst) throws SSLException {
    if (this.destroyed != 0)
      return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0); 
    if (srcs == null)
      throw new NullPointerException("srcs"); 
    if (dst == null)
      throw new NullPointerException("dst"); 
    if (offset >= srcs.length || offset + length > srcs.length)
      throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= srcs.length (" + srcs.length + "))"); 
    if (dst.isReadOnly())
      throw new ReadOnlyBufferException(); 
    if (this.accepted == 0)
      beginHandshakeImplicitly(); 
    SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
    if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
      return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0); 
    int bytesProduced = 0;
    int pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
    if (pendingNet > 0) {
      int capacity = dst.remaining();
      if (capacity < pendingNet)
        return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, handshakeStatus, 0, bytesProduced); 
      try {
        bytesProduced += readEncryptedData(dst, pendingNet);
      } catch (Exception e) {
        throw new SSLException(e);
      } 
      if (this.isOutboundDone)
        shutdown(); 
      return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), 0, bytesProduced);
    } 
    int bytesConsumed = 0;
    for (int i = offset; i < length; i++) {
      ByteBuffer src = srcs[i];
      while (src.hasRemaining()) {
        try {
          bytesConsumed += writePlaintextData(src);
        } catch (Exception e) {
          throw new SSLException(e);
        } 
        pendingNet = SSL.pendingWrittenBytesInBIO(this.networkBIO);
        if (pendingNet > 0) {
          int capacity = dst.remaining();
          if (capacity < pendingNet)
            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, bytesProduced); 
          try {
            bytesProduced += readEncryptedData(dst, pendingNet);
          } catch (Exception e) {
            throw new SSLException(e);
          } 
          return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
        } 
      } 
    } 
    return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
  }
  
  public synchronized SSLEngineResult unwrap(ByteBuffer src, ByteBuffer[] dsts, int offset, int length) throws SSLException {
    if (this.destroyed != 0)
      return new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0); 
    if (src == null)
      throw new NullPointerException("src"); 
    if (dsts == null)
      throw new NullPointerException("dsts"); 
    if (offset >= dsts.length || offset + length > dsts.length)
      throw new IndexOutOfBoundsException("offset: " + offset + ", length: " + length + " (expected: offset <= offset + length <= dsts.length (" + dsts.length + "))"); 
    int capacity = 0;
    int endOffset = offset + length;
    for (int i = offset; i < endOffset; i++) {
      ByteBuffer dst = dsts[i];
      if (dst == null)
        throw new IllegalArgumentException(); 
      if (dst.isReadOnly())
        throw new ReadOnlyBufferException(); 
      capacity += dst.remaining();
    } 
    if (this.accepted == 0)
      beginHandshakeImplicitly(); 
    SSLEngineResult.HandshakeStatus handshakeStatus = getHandshakeStatus();
    if ((!this.handshakeFinished || this.engineClosed) && handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP)
      return new SSLEngineResult(getEngineStatus(), SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0); 
    if (src.remaining() > 18713) {
      this.isInboundDone = true;
      this.isOutboundDone = true;
      this.engineClosed = true;
      shutdown();
      throw ENCRYPTED_PACKET_OVERSIZED;
    } 
    int bytesConsumed = 0;
    this.lastPrimingReadResult = 0;
    try {
      bytesConsumed += writeEncryptedData(src);
    } catch (Exception e) {
      throw new SSLException(e);
    } 
    String error = SSL.getLastError();
    if (error != null && !error.startsWith("error:00000000:")) {
      if (logger.isInfoEnabled())
        logger.info("SSL_read failed: primingReadResult: " + this.lastPrimingReadResult + "; OpenSSL error: '" + error + '\''); 
      shutdown();
      throw new SSLException(error);
    } 
    int pendingApp = (SSL.isInInit(this.ssl) == 0) ? SSL.pendingReadableBytesInSSL(this.ssl) : 0;
    if (capacity < pendingApp)
      return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, getHandshakeStatus(), bytesConsumed, 0); 
    int bytesProduced = 0;
    int idx = offset;
    while (idx < endOffset) {
      int bytesRead;
      ByteBuffer dst = dsts[idx];
      if (!dst.hasRemaining()) {
        idx++;
        continue;
      } 
      if (pendingApp <= 0)
        break; 
      try {
        bytesRead = readPlaintextData(dst);
      } catch (Exception e) {
        throw new SSLException(e);
      } 
      if (bytesRead == 0)
        break; 
      bytesProduced += bytesRead;
      pendingApp -= bytesRead;
      if (!dst.hasRemaining())
        idx++; 
    } 
    if (!this.receivedShutdown && (SSL.getShutdown(this.ssl) & 0x2) == 2) {
      this.receivedShutdown = true;
      closeOutbound();
      closeInbound();
    } 
    return new SSLEngineResult(getEngineStatus(), getHandshakeStatus(), bytesConsumed, bytesProduced);
  }
  
  public Runnable getDelegatedTask() {
    return null;
  }
  
  public synchronized void closeInbound() throws SSLException {
    if (this.isInboundDone)
      return; 
    this.isInboundDone = true;
    this.engineClosed = true;
    if (this.accepted != 0) {
      if (!this.receivedShutdown) {
        shutdown();
        throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
      } 
    } else {
      shutdown();
    } 
  }
  
  public synchronized boolean isInboundDone() {
    return (this.isInboundDone || this.engineClosed);
  }
  
  public synchronized void closeOutbound() {
    if (this.isOutboundDone)
      return; 
    this.isOutboundDone = true;
    this.engineClosed = true;
    if (this.accepted != 0 && this.destroyed == 0) {
      int mode = SSL.getShutdown(this.ssl);
      if ((mode & 0x1) != 1)
        SSL.shutdownSSL(this.ssl); 
    } else {
      shutdown();
    } 
  }
  
  public synchronized boolean isOutboundDone() {
    return this.isOutboundDone;
  }
  
  public String[] getSupportedCipherSuites() {
    return EmptyArrays.EMPTY_STRINGS;
  }
  
  public String[] getEnabledCipherSuites() {
    return EmptyArrays.EMPTY_STRINGS;
  }
  
  public void setEnabledCipherSuites(String[] strings) {
    throw new UnsupportedOperationException();
  }
  
  public String[] getSupportedProtocols() {
    return EmptyArrays.EMPTY_STRINGS;
  }
  
  public String[] getEnabledProtocols() {
    return EmptyArrays.EMPTY_STRINGS;
  }
  
  public void setEnabledProtocols(String[] strings) {
    throw new UnsupportedOperationException();
  }
  
  public SSLSession getSession() {
    SSLSession session = this.session;
    if (session == null)
      this.session = session = new SSLSession() {
          public byte[] getId() {
            return String.valueOf(OpenSslEngine.this.ssl).getBytes();
          }
          
          public SSLSessionContext getSessionContext() {
            return null;
          }
          
          public long getCreationTime() {
            return 0L;
          }
          
          public long getLastAccessedTime() {
            return 0L;
          }
          
          public void invalidate() {}
          
          public boolean isValid() {
            return false;
          }
          
          public void putValue(String s, Object o) {}
          
          public Object getValue(String s) {
            return null;
          }
          
          public void removeValue(String s) {}
          
          public String[] getValueNames() {
            return EmptyArrays.EMPTY_STRINGS;
          }
          
          public Certificate[] getPeerCertificates() {
            return OpenSslEngine.EMPTY_CERTIFICATES;
          }
          
          public Certificate[] getLocalCertificates() {
            return OpenSslEngine.EMPTY_CERTIFICATES;
          }
          
          public X509Certificate[] getPeerCertificateChain() {
            return OpenSslEngine.EMPTY_X509_CERTIFICATES;
          }
          
          public Principal getPeerPrincipal() {
            return null;
          }
          
          public Principal getLocalPrincipal() {
            return null;
          }
          
          public String getCipherSuite() {
            return OpenSslEngine.this.cipher;
          }
          
          public String getProtocol() {
            String applicationProtocol = OpenSslEngine.this.applicationProtocol;
            if (applicationProtocol == null)
              return "unknown"; 
            return "unknown:" + applicationProtocol;
          }
          
          public String getPeerHost() {
            return null;
          }
          
          public int getPeerPort() {
            return 0;
          }
          
          public int getPacketBufferSize() {
            return 18713;
          }
          
          public int getApplicationBufferSize() {
            return 16384;
          }
        }; 
    return session;
  }
  
  public synchronized void beginHandshake() throws SSLException {
    if (this.engineClosed)
      throw ENGINE_CLOSED; 
    switch (this.accepted) {
      case 0:
        SSL.doHandshake(this.ssl);
        this.accepted = 2;
        return;
      case 1:
        this.accepted = 2;
        return;
      case 2:
        throw RENEGOTIATION_UNSUPPORTED;
    } 
    throw new Error();
  }
  
  private synchronized void beginHandshakeImplicitly() throws SSLException {
    if (this.engineClosed)
      throw ENGINE_CLOSED; 
    if (this.accepted == 0) {
      SSL.doHandshake(this.ssl);
      this.accepted = 1;
    } 
  }
  
  private SSLEngineResult.Status getEngineStatus() {
    return this.engineClosed ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK;
  }
  
  public synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
    if (this.accepted == 0 || this.destroyed != 0)
      return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING; 
    if (!this.handshakeFinished) {
      if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0)
        return SSLEngineResult.HandshakeStatus.NEED_WRAP; 
      if (SSL.isInInit(this.ssl) == 0) {
        this.handshakeFinished = true;
        this.cipher = SSL.getCipherForSSL(this.ssl);
        String applicationProtocol = SSL.getNextProtoNegotiated(this.ssl);
        if (applicationProtocol == null)
          applicationProtocol = this.fallbackApplicationProtocol; 
        if (applicationProtocol != null) {
          this.applicationProtocol = applicationProtocol.replace(':', '_');
        } else {
          this.applicationProtocol = null;
        } 
        return SSLEngineResult.HandshakeStatus.FINISHED;
      } 
      return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    } 
    if (this.engineClosed) {
      if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0)
        return SSLEngineResult.HandshakeStatus.NEED_WRAP; 
      return SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    } 
    return SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
  }
  
  public void setUseClientMode(boolean clientMode) {
    if (clientMode)
      throw new UnsupportedOperationException(); 
  }
  
  public boolean getUseClientMode() {
    return false;
  }
  
  public void setNeedClientAuth(boolean b) {
    if (b)
      throw new UnsupportedOperationException(); 
  }
  
  public boolean getNeedClientAuth() {
    return false;
  }
  
  public void setWantClientAuth(boolean b) {
    if (b)
      throw new UnsupportedOperationException(); 
  }
  
  public boolean getWantClientAuth() {
    return false;
  }
  
  public void setEnableSessionCreation(boolean b) {
    if (b)
      throw new UnsupportedOperationException(); 
  }
  
  public boolean getEnableSessionCreation() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\ssl\OpenSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */