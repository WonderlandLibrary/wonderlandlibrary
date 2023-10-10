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
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
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
   private static final AtomicIntegerFieldUpdater DESTROYED_UPDATER;
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

   public OpenSslEngine(long var1, ByteBufAllocator var3, String var4) {
      OpenSsl.ensureAvailability();
      if (var1 == 0L) {
         throw new NullPointerException("sslContext");
      } else if (var3 == null) {
         throw new NullPointerException("alloc");
      } else {
         this.alloc = var3;
         this.ssl = SSL.newSSL(var1, true);
         this.networkBIO = SSL.makeNetworkBIO(this.ssl);
         this.fallbackApplicationProtocol = var4;
      }
   }

   public synchronized void shutdown() {
      if (DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
         SSL.freeSSL(this.ssl);
         SSL.freeBIO(this.networkBIO);
         this.ssl = this.networkBIO = 0L;
         this.isInboundDone = this.isOutboundDone = this.engineClosed = true;
      }

   }

   private int writePlaintextData(ByteBuffer var1) {
      int var2 = var1.position();
      int var3 = var1.limit();
      int var4 = Math.min(var3 - var2, 16384);
      int var5;
      if (var1.isDirect()) {
         long var6 = Buffer.address(var1) + (long)var2;
         var5 = SSL.writeToSSL(this.ssl, var6, var4);
         if (var5 > 0) {
            var1.position(var2 + var5);
            return var5;
         }
      } else {
         ByteBuf var11 = this.alloc.directBuffer(var4);
         long var7;
         if (var11.hasMemoryAddress()) {
            var7 = var11.memoryAddress();
         } else {
            var7 = Buffer.address(var11.nioBuffer());
         }

         var1.limit(var2 + var4);
         var11.setBytes(0, (ByteBuffer)var1);
         var1.limit(var3);
         var5 = SSL.writeToSSL(this.ssl, var7, var4);
         if (var5 > 0) {
            var1.position(var2 + var5);
            var11.release();
            return var5;
         }

         var1.position(var2);
         var11.release();
      }

      throw new IllegalStateException("SSL.writeToSSL() returned a non-positive value: " + var5);
   }

   private int writeEncryptedData(ByteBuffer var1) {
      int var2 = var1.position();
      int var3 = var1.remaining();
      if (var1.isDirect()) {
         long var4 = Buffer.address(var1) + (long)var2;
         int var6 = SSL.writeToBIO(this.networkBIO, var4, var3);
         if (var6 >= 0) {
            var1.position(var2 + var6);
            this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, var4, 0);
            return var6;
         }
      } else {
         ByteBuf var10 = this.alloc.directBuffer(var3);
         long var5;
         if (var10.hasMemoryAddress()) {
            var5 = var10.memoryAddress();
         } else {
            var5 = Buffer.address(var10.nioBuffer());
         }

         var10.setBytes(0, (ByteBuffer)var1);
         int var7 = SSL.writeToBIO(this.networkBIO, var5, var3);
         if (var7 >= 0) {
            var1.position(var2 + var7);
            this.lastPrimingReadResult = SSL.readFromSSL(this.ssl, var5, 0);
            var10.release();
            return var7;
         }

         var1.position(var2);
         var10.release();
      }

      return 0;
   }

   private int readPlaintextData(ByteBuffer var1) {
      int var2;
      if (var1.isDirect()) {
         var2 = var1.position();
         long var3 = Buffer.address(var1) + (long)var2;
         int var5 = var1.limit() - var2;
         int var6 = SSL.readFromSSL(this.ssl, var3, var5);
         if (var6 > 0) {
            var1.position(var2 + var6);
            return var6;
         }
      } else {
         var2 = var1.position();
         int var11 = var1.limit();
         int var4 = Math.min(18713, var11 - var2);
         ByteBuf var12 = this.alloc.directBuffer(var4);
         long var13;
         if (var12.hasMemoryAddress()) {
            var13 = var12.memoryAddress();
         } else {
            var13 = Buffer.address(var12.nioBuffer());
         }

         int var8 = SSL.readFromSSL(this.ssl, var13, var4);
         if (var8 > 0) {
            var1.limit(var2 + var8);
            var12.getBytes(0, (ByteBuffer)var1);
            var1.limit(var11);
            var12.release();
            return var8;
         }

         var12.release();
      }

      return 0;
   }

   private int readEncryptedData(ByteBuffer var1, int var2) {
      long var4;
      int var6;
      if (var1.isDirect() && var1.remaining() >= var2) {
         int var10 = var1.position();
         var4 = Buffer.address(var1) + (long)var10;
         var6 = SSL.readFromBIO(this.networkBIO, var4, var2);
         if (var6 > 0) {
            var1.position(var10 + var6);
            return var6;
         }
      } else {
         ByteBuf var3 = this.alloc.directBuffer(var2);
         if (var3.hasMemoryAddress()) {
            var4 = var3.memoryAddress();
         } else {
            var4 = Buffer.address(var3.nioBuffer());
         }

         var6 = SSL.readFromBIO(this.networkBIO, var4, var2);
         if (var6 > 0) {
            int var7 = var1.limit();
            var1.limit(var1.position() + var6);
            var3.getBytes(0, (ByteBuffer)var1);
            var1.limit(var7);
            var3.release();
            return var6;
         }

         var3.release();
      }

      return 0;
   }

   public synchronized SSLEngineResult wrap(ByteBuffer[] var1, int var2, int var3, ByteBuffer var4) throws SSLException {
      if (this.destroyed != 0) {
         return new SSLEngineResult(Status.CLOSED, HandshakeStatus.NOT_HANDSHAKING, 0, 0);
      } else if (var1 == null) {
         throw new NullPointerException("srcs");
      } else if (var4 == null) {
         throw new NullPointerException("dst");
      } else if (var2 < var1.length && var2 + var3 <= var1.length) {
         if (var4.isReadOnly()) {
            throw new ReadOnlyBufferException();
         } else {
            if (this.accepted == 0) {
               this.beginHandshakeImplicitly();
            }

            HandshakeStatus var5 = this.getHandshakeStatus();
            if ((!this.handshakeFinished || this.engineClosed) && var5 == HandshakeStatus.NEED_UNWRAP) {
               return new SSLEngineResult(this.getEngineStatus(), HandshakeStatus.NEED_UNWRAP, 0, 0);
            } else {
               byte var6 = 0;
               int var7 = SSL.pendingWrittenBytesInBIO(this.networkBIO);
               int var8;
               int var16;
               if (var7 > 0) {
                  var8 = var4.remaining();
                  if (var8 < var7) {
                     return new SSLEngineResult(Status.BUFFER_OVERFLOW, var5, 0, var6);
                  } else {
                     try {
                        var16 = var6 + this.readEncryptedData(var4, var7);
                     } catch (Exception var13) {
                        throw new SSLException(var13);
                     }

                     if (this.isOutboundDone) {
                        this.shutdown();
                     }

                     return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), 0, var16);
                  }
               } else {
                  var8 = 0;

                  for(int var9 = var2; var9 < var3; ++var9) {
                     ByteBuffer var10 = var1[var9];

                     while(var10.hasRemaining()) {
                        try {
                           var8 += this.writePlaintextData(var10);
                        } catch (Exception var15) {
                           throw new SSLException(var15);
                        }

                        var7 = SSL.pendingWrittenBytesInBIO(this.networkBIO);
                        if (var7 > 0) {
                           int var11 = var4.remaining();
                           if (var11 < var7) {
                              return new SSLEngineResult(Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), var8, var6);
                           }

                           try {
                              var16 = var6 + this.readEncryptedData(var4, var7);
                           } catch (Exception var14) {
                              throw new SSLException(var14);
                           }

                           return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), var8, var16);
                        }
                     }
                  }

                  return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), var8, var6);
               }
            }
         }
      } else {
         throw new IndexOutOfBoundsException("offset: " + var2 + ", length: " + var3 + " (expected: offset <= offset + length <= srcs.length (" + var1.length + "))");
      }
   }

   public synchronized SSLEngineResult unwrap(ByteBuffer var1, ByteBuffer[] var2, int var3, int var4) throws SSLException {
      if (this.destroyed != 0) {
         return new SSLEngineResult(Status.CLOSED, HandshakeStatus.NOT_HANDSHAKING, 0, 0);
      } else if (var1 == null) {
         throw new NullPointerException("src");
      } else if (var2 == null) {
         throw new NullPointerException("dsts");
      } else if (var3 < var2.length && var3 + var4 <= var2.length) {
         int var5 = 0;
         int var6 = var3 + var4;

         for(int var7 = var3; var7 < var6; ++var7) {
            ByteBuffer var8 = var2[var7];
            if (var8 == null) {
               throw new IllegalArgumentException();
            }

            if (var8.isReadOnly()) {
               throw new ReadOnlyBufferException();
            }

            var5 += var8.remaining();
         }

         if (this.accepted == 0) {
            this.beginHandshakeImplicitly();
         }

         HandshakeStatus var18 = this.getHandshakeStatus();
         if ((!this.handshakeFinished || this.engineClosed) && var18 == HandshakeStatus.NEED_WRAP) {
            return new SSLEngineResult(this.getEngineStatus(), HandshakeStatus.NEED_WRAP, 0, 0);
         } else if (var1.remaining() > 18713) {
            this.isInboundDone = true;
            this.isOutboundDone = true;
            this.engineClosed = true;
            this.shutdown();
            throw ENCRYPTED_PACKET_OVERSIZED;
         } else {
            byte var19 = 0;
            this.lastPrimingReadResult = 0;

            int var20;
            try {
               var20 = var19 + this.writeEncryptedData(var1);
            } catch (Exception var17) {
               throw new SSLException(var17);
            }

            String var9 = SSL.getLastError();
            if (var9 != null && !var9.startsWith("error:00000000:")) {
               if (logger.isInfoEnabled()) {
                  logger.info("SSL_read failed: primingReadResult: " + this.lastPrimingReadResult + "; OpenSSL error: '" + var9 + '\'');
               }

               this.shutdown();
               throw new SSLException(var9);
            } else {
               int var10 = SSL.isInInit(this.ssl) == 0 ? SSL.pendingReadableBytesInSSL(this.ssl) : 0;
               if (var5 < var10) {
                  return new SSLEngineResult(Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), var20, 0);
               } else {
                  int var11 = 0;
                  int var12 = var3;

                  while(var12 < var6) {
                     ByteBuffer var13 = var2[var12];
                     if (!var13.hasRemaining()) {
                        ++var12;
                     } else {
                        if (var10 <= 0) {
                           break;
                        }

                        int var14;
                        try {
                           var14 = this.readPlaintextData(var13);
                        } catch (Exception var16) {
                           throw new SSLException(var16);
                        }

                        if (var14 == 0) {
                           break;
                        }

                        var11 += var14;
                        var10 -= var14;
                        if (!var13.hasRemaining()) {
                           ++var12;
                        }
                     }
                  }

                  if (!this.receivedShutdown && (SSL.getShutdown(this.ssl) & 2) == 2) {
                     this.receivedShutdown = true;
                     this.closeOutbound();
                     this.closeInbound();
                  }

                  return new SSLEngineResult(this.getEngineStatus(), this.getHandshakeStatus(), var20, var11);
               }
            }
         }
      } else {
         throw new IndexOutOfBoundsException("offset: " + var3 + ", length: " + var4 + " (expected: offset <= offset + length <= dsts.length (" + var2.length + "))");
      }
   }

   public Runnable getDelegatedTask() {
      return null;
   }

   public synchronized void closeInbound() throws SSLException {
      if (!this.isInboundDone) {
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
   }

   public synchronized boolean isInboundDone() {
      return this.isInboundDone || this.engineClosed;
   }

   public synchronized void closeOutbound() {
      if (!this.isOutboundDone) {
         this.isOutboundDone = true;
         this.engineClosed = true;
         if (this.accepted != 0 && this.destroyed == 0) {
            int var1 = SSL.getShutdown(this.ssl);
            if ((var1 & 1) != 1) {
               SSL.shutdownSSL(this.ssl);
            }
         } else {
            this.shutdown();
         }

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

   public void setEnabledCipherSuites(String[] var1) {
      throw new UnsupportedOperationException();
   }

   public String[] getSupportedProtocols() {
      return EmptyArrays.EMPTY_STRINGS;
   }

   public String[] getEnabledProtocols() {
      return EmptyArrays.EMPTY_STRINGS;
   }

   public void setEnabledProtocols(String[] var1) {
      throw new UnsupportedOperationException();
   }

   public SSLSession getSession() {
      SSLSession var1 = this.session;
      if (var1 == null) {
         this.session = var1 = new SSLSession(this) {
            final OpenSslEngine this$0;

            {
               this.this$0 = var1;
            }

            public byte[] getId() {
               return String.valueOf(OpenSslEngine.access$000(this.this$0)).getBytes();
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

            public void invalidate() {
            }

            public boolean isValid() {
               return false;
            }

            public void putValue(String var1, Object var2) {
            }

            public Object getValue(String var1) {
               return null;
            }

            public void removeValue(String var1) {
            }

            public String[] getValueNames() {
               return EmptyArrays.EMPTY_STRINGS;
            }

            public Certificate[] getPeerCertificates() {
               return OpenSslEngine.access$100();
            }

            public Certificate[] getLocalCertificates() {
               return OpenSslEngine.access$100();
            }

            public X509Certificate[] getPeerCertificateChain() {
               return OpenSslEngine.access$200();
            }

            public Principal getPeerPrincipal() {
               return null;
            }

            public Principal getLocalPrincipal() {
               return null;
            }

            public String getCipherSuite() {
               return OpenSslEngine.access$300(this.this$0);
            }

            public String getProtocol() {
               String var1 = OpenSslEngine.access$400(this.this$0);
               return var1 == null ? "unknown" : "unknown:" + var1;
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
      }

      return var1;
   }

   public synchronized void beginHandshake() throws SSLException {
      if (this.engineClosed) {
         throw ENGINE_CLOSED;
      } else {
         switch(this.accepted) {
         case 0:
            SSL.doHandshake(this.ssl);
            this.accepted = 2;
            break;
         case 1:
            this.accepted = 2;
            break;
         case 2:
            throw RENEGOTIATION_UNSUPPORTED;
         default:
            throw new Error();
         }

      }
   }

   private synchronized void beginHandshakeImplicitly() throws SSLException {
      if (this.engineClosed) {
         throw ENGINE_CLOSED;
      } else {
         if (this.accepted == 0) {
            SSL.doHandshake(this.ssl);
            this.accepted = 1;
         }

      }
   }

   private Status getEngineStatus() {
      return this.engineClosed ? Status.CLOSED : Status.OK;
   }

   public synchronized HandshakeStatus getHandshakeStatus() {
      if (this.accepted != 0 && this.destroyed == 0) {
         if (!this.handshakeFinished) {
            if (SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0) {
               return HandshakeStatus.NEED_WRAP;
            } else if (SSL.isInInit(this.ssl) == 0) {
               this.handshakeFinished = true;
               this.cipher = SSL.getCipherForSSL(this.ssl);
               String var1 = SSL.getNextProtoNegotiated(this.ssl);
               if (var1 == null) {
                  var1 = this.fallbackApplicationProtocol;
               }

               if (var1 != null) {
                  this.applicationProtocol = var1.replace(':', '_');
               } else {
                  this.applicationProtocol = null;
               }

               return HandshakeStatus.FINISHED;
            } else {
               return HandshakeStatus.NEED_UNWRAP;
            }
         } else if (this.engineClosed) {
            return SSL.pendingWrittenBytesInBIO(this.networkBIO) != 0 ? HandshakeStatus.NEED_WRAP : HandshakeStatus.NEED_UNWRAP;
         } else {
            return HandshakeStatus.NOT_HANDSHAKING;
         }
      } else {
         return HandshakeStatus.NOT_HANDSHAKING;
      }
   }

   public void setUseClientMode(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException();
      }
   }

   public boolean getUseClientMode() {
      return false;
   }

   public void setNeedClientAuth(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException();
      }
   }

   public boolean getNeedClientAuth() {
      return false;
   }

   public void setWantClientAuth(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException();
      }
   }

   public boolean getWantClientAuth() {
      return false;
   }

   public void setEnableSessionCreation(boolean var1) {
      if (var1) {
         throw new UnsupportedOperationException();
      }
   }

   public boolean getEnableSessionCreation() {
      return false;
   }

   static long access$000(OpenSslEngine var0) {
      return var0.ssl;
   }

   static Certificate[] access$100() {
      return EMPTY_CERTIFICATES;
   }

   static X509Certificate[] access$200() {
      return EMPTY_X509_CERTIFICATES;
   }

   static String access$300(OpenSslEngine var0) {
      return var0.cipher;
   }

   static String access$400(OpenSslEngine var0) {
      return var0.applicationProtocol;
   }

   static {
      ENGINE_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      RENEGOTIATION_UNSUPPORTED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      ENCRYPTED_PACKET_OVERSIZED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
      DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(OpenSslEngine.class, "destroyed");
   }
}
