package io.netty.channel.socket.nio;

import io.netty.channel.ChannelException;
import io.netty.channel.socket.DatagramChannelConfig;
import io.netty.channel.socket.DefaultDatagramChannelConfig;
import io.netty.util.internal.PlatformDependent;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.util.Enumeration;

class NioDatagramChannelConfig extends DefaultDatagramChannelConfig {
   private static final Object IP_MULTICAST_TTL;
   private static final Object IP_MULTICAST_IF;
   private static final Object IP_MULTICAST_LOOP;
   private static final Method GET_OPTION;
   private static final Method SET_OPTION;
   private final DatagramChannel javaChannel;

   NioDatagramChannelConfig(NioDatagramChannel var1, DatagramChannel var2) {
      super(var1, var2.socket());
      this.javaChannel = var2;
   }

   public int getTimeToLive() {
      return (Integer)this.getOption0(IP_MULTICAST_TTL);
   }

   public DatagramChannelConfig setTimeToLive(int var1) {
      this.setOption0(IP_MULTICAST_TTL, var1);
      return this;
   }

   public InetAddress getInterface() {
      NetworkInterface var1 = this.getNetworkInterface();
      if (var1 == null) {
         return null;
      } else {
         Enumeration var2 = var1.getInetAddresses();
         return var2.hasMoreElements() ? (InetAddress)var2.nextElement() : null;
      }
   }

   public DatagramChannelConfig setInterface(InetAddress var1) {
      try {
         this.setNetworkInterface(NetworkInterface.getByInetAddress(var1));
         return this;
      } catch (SocketException var3) {
         throw new ChannelException(var3);
      }
   }

   public NetworkInterface getNetworkInterface() {
      return (NetworkInterface)this.getOption0(IP_MULTICAST_IF);
   }

   public DatagramChannelConfig setNetworkInterface(NetworkInterface var1) {
      this.setOption0(IP_MULTICAST_IF, var1);
      return this;
   }

   public boolean isLoopbackModeDisabled() {
      return (Boolean)this.getOption0(IP_MULTICAST_LOOP);
   }

   public DatagramChannelConfig setLoopbackModeDisabled(boolean var1) {
      this.setOption0(IP_MULTICAST_LOOP, var1);
      return this;
   }

   private Object getOption0(Object var1) {
      if (PlatformDependent.javaVersion() < 7) {
         throw new UnsupportedOperationException();
      } else {
         try {
            return GET_OPTION.invoke(this.javaChannel, var1);
         } catch (Exception var3) {
            throw new ChannelException(var3);
         }
      }
   }

   private void setOption0(Object var1, Object var2) {
      if (PlatformDependent.javaVersion() < 7) {
         throw new UnsupportedOperationException();
      } else {
         try {
            SET_OPTION.invoke(this.javaChannel, var1, var2);
         } catch (Exception var4) {
            throw new ChannelException(var4);
         }
      }
   }

   static {
      ClassLoader var0 = DatagramChannel.class.getClassLoader();
      Class var1 = null;

      try {
         var1 = Class.forName("java.net.SocketOption", true, var0);
      } catch (Exception var15) {
      }

      Class var2 = null;

      try {
         var2 = Class.forName("java.net.StandardSocketOptions", true, var0);
      } catch (Exception var14) {
      }

      Object var3 = null;
      Object var4 = null;
      Object var5 = null;
      Method var6 = null;
      Method var7 = null;
      if (var1 != null) {
         try {
            var3 = var2.getDeclaredField("IP_MULTICAST_TTL").get((Object)null);
         } catch (Exception var13) {
            throw new Error("cannot locate the IP_MULTICAST_TTL field", var13);
         }

         try {
            var4 = var2.getDeclaredField("IP_MULTICAST_IF").get((Object)null);
         } catch (Exception var12) {
            throw new Error("cannot locate the IP_MULTICAST_IF field", var12);
         }

         try {
            var5 = var2.getDeclaredField("IP_MULTICAST_LOOP").get((Object)null);
         } catch (Exception var11) {
            throw new Error("cannot locate the IP_MULTICAST_LOOP field", var11);
         }

         try {
            var6 = NetworkChannel.class.getDeclaredMethod("getOption", var1);
         } catch (Exception var10) {
            throw new Error("cannot locate the getOption() method", var10);
         }

         try {
            var7 = NetworkChannel.class.getDeclaredMethod("setOption", var1, Object.class);
         } catch (Exception var9) {
            throw new Error("cannot locate the setOption() method", var9);
         }
      }

      IP_MULTICAST_TTL = var3;
      IP_MULTICAST_IF = var4;
      IP_MULTICAST_LOOP = var5;
      GET_OPTION = var6;
      SET_OPTION = var7;
   }
}
