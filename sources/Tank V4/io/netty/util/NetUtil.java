package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class NetUtil {
   public static final Inet4Address LOCALHOST4;
   public static final Inet6Address LOCALHOST6;
   public static final InetAddress LOCALHOST;
   public static final NetworkInterface LOOPBACK_IF;
   public static final int SOMAXCONN;
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(NetUtil.class);

   public static byte[] createByteArrayFromIpAddressString(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static void convertToBytes(String var0, byte[] var1, int var2) {
      int var3 = var0.length();
      int var4 = 0;
      var1[var2] = 0;
      var1[var2 + 1] = 0;
      int var5;
      if (var3 > 3) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2] = (byte)(var1[var2] | var5 << 4);
      }

      if (var3 > 2) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2] = (byte)(var1[var2] | var5);
      }

      if (var3 > 1) {
         var5 = getIntValue(var0.charAt(var4++));
         var1[var2 + 1] = (byte)(var1[var2 + 1] | var5 << 4);
      }

      var5 = getIntValue(var0.charAt(var4));
      var1[var2 + 1] = (byte)(var1[var2 + 1] | var5 & 15);
   }

   static int getIntValue(char var0) {
      switch(var0) {
      case '0':
         return 0;
      case '1':
         return 1;
      case '2':
         return 2;
      case '3':
         return 3;
      case '4':
         return 4;
      case '5':
         return 5;
      case '6':
         return 6;
      case '7':
         return 7;
      case '8':
         return 8;
      case '9':
         return 9;
      default:
         var0 = Character.toLowerCase(var0);
         switch(var0) {
         case 'a':
            return 10;
         case 'b':
            return 11;
         case 'c':
            return 12;
         case 'd':
            return 13;
         case 'e':
            return 14;
         case 'f':
            return 15;
         default:
            return 0;
         }
      }
   }

   private NetUtil() {
   }

   static {
      NetworkInterface var0 = null;

      try {
         Enumeration var1 = NetworkInterface.getNetworkInterfaces();

         while(var1.hasMoreElements()) {
            NetworkInterface var2 = (NetworkInterface)var1.nextElement();
            if (var2.isLoopback()) {
               var0 = var2;
               break;
            }
         }

         if (var0 == null) {
            logger.warn("Failed to find the loopback interface");
         }
      } catch (SocketException var15) {
         logger.warn("Failed to find the loopback interface", (Throwable)var15);
      }

      LOOPBACK_IF = var0;
      Object var16 = null;
      if (LOOPBACK_IF != null) {
         logger.debug("Loopback interface: {}", (Object)LOOPBACK_IF.getDisplayName());
         Enumeration var17 = LOOPBACK_IF.getInetAddresses();

         while(var17.hasMoreElements()) {
            InetAddress var3 = (InetAddress)var17.nextElement();
            if (var16 == null) {
               logger.debug("Loopback address: {} (primary)", (Object)var3);
               var16 = var3;
            } else {
               logger.debug("Loopback address: {}", (Object)var3);
            }
         }
      }

      Inet4Address var18 = null;

      try {
         var18 = (Inet4Address)InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
      } catch (Exception var12) {
         PlatformDependent.throwException(var12);
      }

      LOCALHOST4 = var18;
      Inet6Address var19 = null;

      try {
         var19 = (Inet6Address)InetAddress.getByAddress(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1});
      } catch (Exception var11) {
         PlatformDependent.throwException(var11);
      }

      LOCALHOST6 = var19;
      if (var16 == null) {
         label90: {
            try {
               if (NetworkInterface.getByInetAddress(LOCALHOST6) != null) {
                  logger.debug("Using hard-coded IPv6 localhost address: {}", (Object)var19);
                  var16 = var19;
               }
            } catch (Exception var14) {
               if (var16 == null) {
                  logger.debug("Using hard-coded IPv4 localhost address: {}", (Object)var18);
                  var16 = var18;
               }
               break label90;
            }

            if (var16 == null) {
               logger.debug("Using hard-coded IPv4 localhost address: {}", (Object)var18);
               var16 = var18;
            }
         }
      }

      LOCALHOST = (InetAddress)var16;
      int var4 = 3072;
      BufferedReader var5 = null;

      label79: {
         try {
            var5 = new BufferedReader(new FileReader("/proc/sys/net/core/somaxconn"));
            var4 = Integer.parseInt(var5.readLine());
            logger.debug("/proc/sys/net/core/somaxconn: {}", (Object)var4);
         } catch (Exception var13) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Exception var9) {
               }
            }
            break label79;
         }

         if (var5 != null) {
            try {
               var5.close();
            } catch (Exception var10) {
            }
         }
      }

      SOMAXCONN = var4;
   }
}
