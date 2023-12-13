package io.netty.channel.epoll;

import io.netty.channel.ChannelException;
import io.netty.channel.DefaultFileRegion;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Locale;

final class Native {
   private static final byte[] IPV4_MAPPED_IPV6_PREFIX = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1};
   public static final int EPOLLIN = 1;
   public static final int EPOLLOUT = 2;
   public static final int EPOLLACCEPT = 4;
   public static final int EPOLLRDHUP = 8;
   public static final int IOV_MAX;

   public static native int eventFd();

   public static native void eventFdWrite(int var0, long var1);

   public static native void eventFdRead(int var0);

   public static native int epollCreate();

   public static native int epollWait(int var0, long[] var1, int var2);

   public static native void epollCtlAdd(int var0, int var1, int var2, int var3);

   public static native void epollCtlMod(int var0, int var1, int var2, int var3);

   public static native void epollCtlDel(int var0, int var1);

   public static native void close(int var0) throws IOException;

   public static native int write(int var0, ByteBuffer var1, int var2, int var3) throws IOException;

   public static native int writeAddress(int var0, long var1, int var3, int var4) throws IOException;

   public static native long writev(int var0, ByteBuffer[] var1, int var2, int var3) throws IOException;

   public static native long writevAddresses(int var0, long var1, int var3) throws IOException;

   public static native int read(int var0, ByteBuffer var1, int var2, int var3) throws IOException;

   public static native int readAddress(int var0, long var1, int var3, int var4) throws IOException;

   public static native long sendfile(int var0, DefaultFileRegion var1, long var2, long var4, long var6) throws IOException;

   public static int sendTo(int var0, ByteBuffer var1, int var2, int var3, InetAddress var4, int var5) throws IOException {
      byte[] var6;
      int var7;
      if (var4 instanceof Inet6Address) {
         var6 = var4.getAddress();
         var7 = ((Inet6Address)var4).getScopeId();
      } else {
         var7 = 0;
         var6 = ipv4MappedIpv6Address(var4.getAddress());
      }

      return sendTo(var0, var1, var2, var3, var6, var7, var5);
   }

   private static native int sendTo(int var0, ByteBuffer var1, int var2, int var3, byte[] var4, int var5, int var6) throws IOException;

   public static int sendToAddress(int var0, long var1, int var3, int var4, InetAddress var5, int var6) throws IOException {
      byte[] var7;
      int var8;
      if (var5 instanceof Inet6Address) {
         var7 = var5.getAddress();
         var8 = ((Inet6Address)var5).getScopeId();
      } else {
         var8 = 0;
         var7 = ipv4MappedIpv6Address(var5.getAddress());
      }

      return sendToAddress(var0, var1, var3, var4, var7, var8, var6);
   }

   private static native int sendToAddress(int var0, long var1, int var3, int var4, byte[] var5, int var6, int var7) throws IOException;

   public static native EpollDatagramChannel.DatagramSocketAddress recvFrom(int var0, ByteBuffer var1, int var2, int var3) throws IOException;

   public static native EpollDatagramChannel.DatagramSocketAddress recvFromAddress(int var0, long var1, int var3, int var4) throws IOException;

   public static int socketStreamFd() {
      try {
         return socketStream();
      } catch (IOException var1) {
         throw new ChannelException(var1);
      }
   }

   public static int socketDgramFd() {
      try {
         return socketDgram();
      } catch (IOException var1) {
         throw new ChannelException(var1);
      }
   }

   private static native int socketStream() throws IOException;

   private static native int socketDgram() throws IOException;

   public static void bind(int var0, InetAddress var1, int var2) throws IOException {
      Native.NativeInetAddress var3 = toNativeInetAddress(var1);
      bind(var0, var3.address, var3.scopeId, var2);
   }

   private static byte[] ipv4MappedIpv6Address(byte[] var0) {
      byte[] var1 = new byte[16];
      System.arraycopy(IPV4_MAPPED_IPV6_PREFIX, 0, var1, 0, IPV4_MAPPED_IPV6_PREFIX.length);
      System.arraycopy(var0, 0, var1, 12, var0.length);
      return var1;
   }

   public static native void bind(int var0, byte[] var1, int var2, int var3) throws IOException;

   public static native void listen(int var0, int var1) throws IOException;

   public static boolean connect(int var0, InetAddress var1, int var2) throws IOException {
      Native.NativeInetAddress var3 = toNativeInetAddress(var1);
      return connect(var0, var3.address, var3.scopeId, var2);
   }

   public static native boolean connect(int var0, byte[] var1, int var2, int var3) throws IOException;

   public static native boolean finishConnect(int var0) throws IOException;

   public static native InetSocketAddress remoteAddress(int var0);

   public static native InetSocketAddress localAddress(int var0);

   public static native int accept(int var0) throws IOException;

   public static native void shutdown(int var0, boolean var1, boolean var2) throws IOException;

   public static native int getReceiveBufferSize(int var0);

   public static native int getSendBufferSize(int var0);

   public static native int isKeepAlive(int var0);

   public static native int isReuseAddress(int var0);

   public static native int isReusePort(int var0);

   public static native int isTcpNoDelay(int var0);

   public static native int isTcpCork(int var0);

   public static native int getSoLinger(int var0);

   public static native int getTrafficClass(int var0);

   public static native int isBroadcast(int var0);

   public static native int getTcpKeepIdle(int var0);

   public static native int getTcpKeepIntvl(int var0);

   public static native int getTcpKeepCnt(int var0);

   public static native void setKeepAlive(int var0, int var1);

   public static native void setReceiveBufferSize(int var0, int var1);

   public static native void setReuseAddress(int var0, int var1);

   public static native void setReusePort(int var0, int var1);

   public static native void setSendBufferSize(int var0, int var1);

   public static native void setTcpNoDelay(int var0, int var1);

   public static native void setTcpCork(int var0, int var1);

   public static native void setSoLinger(int var0, int var1);

   public static native void setTrafficClass(int var0, int var1);

   public static native void setBroadcast(int var0, int var1);

   public static native void setTcpKeepIdle(int var0, int var1);

   public static native void setTcpKeepIntvl(int var0, int var1);

   public static native void setTcpKeepCnt(int var0, int var1);

   private static Native.NativeInetAddress toNativeInetAddress(InetAddress var0) {
      byte[] var1 = var0.getAddress();
      return var0 instanceof Inet6Address ? new Native.NativeInetAddress(var1, ((Inet6Address)var0).getScopeId()) : new Native.NativeInetAddress(ipv4MappedIpv6Address(var1));
   }

   public static native String kernelVersion();

   private static native int iovMax();

   private Native() {
   }

   static {
      String var0 = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
      if (!var0.startsWith("linux")) {
         throw new IllegalStateException("Only supported on Linux");
      } else {
         NativeLibraryLoader.load("netty-transport-native-epoll", PlatformDependent.getClassLoader(Native.class));
         IOV_MAX = iovMax();
      }
   }

   private static class NativeInetAddress {
      final byte[] address;
      final int scopeId;

      NativeInetAddress(byte[] var1, int var2) {
         this.address = var1;
         this.scopeId = var2;
      }

      NativeInetAddress(byte[] var1) {
         this(var1, 0);
      }
   }
}
