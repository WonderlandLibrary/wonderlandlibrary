package io.netty.channel.rxtx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public interface RxtxChannelConfig extends ChannelConfig {
   RxtxChannelConfig setBaudrate(int var1);

   RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits var1);

   RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits var1);

   RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit var1);

   int getBaudrate();

   RxtxChannelConfig.Stopbits getStopbits();

   RxtxChannelConfig.Databits getDatabits();

   RxtxChannelConfig.Paritybit getParitybit();

   boolean isDtr();

   RxtxChannelConfig setDtr(boolean var1);

   boolean isRts();

   RxtxChannelConfig setRts(boolean var1);

   int getWaitTimeMillis();

   RxtxChannelConfig setWaitTimeMillis(int var1);

   RxtxChannelConfig setReadTimeout(int var1);

   int getReadTimeout();

   RxtxChannelConfig setConnectTimeoutMillis(int var1);

   RxtxChannelConfig setMaxMessagesPerRead(int var1);

   RxtxChannelConfig setWriteSpinCount(int var1);

   RxtxChannelConfig setAllocator(ByteBufAllocator var1);

   RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator var1);

   RxtxChannelConfig setAutoRead(boolean var1);

   RxtxChannelConfig setAutoClose(boolean var1);

   RxtxChannelConfig setWriteBufferHighWaterMark(int var1);

   RxtxChannelConfig setWriteBufferLowWaterMark(int var1);

   RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator var1);

   public static enum Paritybit {
      NONE(0),
      ODD(1),
      EVEN(2),
      MARK(3),
      SPACE(4);

      private final int value;
      private static final RxtxChannelConfig.Paritybit[] $VALUES = new RxtxChannelConfig.Paritybit[]{NONE, ODD, EVEN, MARK, SPACE};

      private Paritybit(int var3) {
         this.value = var3;
      }

      public int value() {
         return this.value;
      }

      public static RxtxChannelConfig.Paritybit valueOf(int var0) {
         RxtxChannelConfig.Paritybit[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RxtxChannelConfig.Paritybit var4 = var1[var3];
            if (var4.value == var0) {
               return var4;
            }
         }

         throw new IllegalArgumentException("unknown " + RxtxChannelConfig.Paritybit.class.getSimpleName() + " value: " + var0);
      }
   }

   public static enum Databits {
      DATABITS_5(5),
      DATABITS_6(6),
      DATABITS_7(7),
      DATABITS_8(8);

      private final int value;
      private static final RxtxChannelConfig.Databits[] $VALUES = new RxtxChannelConfig.Databits[]{DATABITS_5, DATABITS_6, DATABITS_7, DATABITS_8};

      private Databits(int var3) {
         this.value = var3;
      }

      public int value() {
         return this.value;
      }

      public static RxtxChannelConfig.Databits valueOf(int var0) {
         RxtxChannelConfig.Databits[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RxtxChannelConfig.Databits var4 = var1[var3];
            if (var4.value == var0) {
               return var4;
            }
         }

         throw new IllegalArgumentException("unknown " + RxtxChannelConfig.Databits.class.getSimpleName() + " value: " + var0);
      }
   }

   public static enum Stopbits {
      STOPBITS_1(1),
      STOPBITS_2(2),
      STOPBITS_1_5(3);

      private final int value;
      private static final RxtxChannelConfig.Stopbits[] $VALUES = new RxtxChannelConfig.Stopbits[]{STOPBITS_1, STOPBITS_2, STOPBITS_1_5};

      private Stopbits(int var3) {
         this.value = var3;
      }

      public int value() {
         return this.value;
      }

      public static RxtxChannelConfig.Stopbits valueOf(int var0) {
         RxtxChannelConfig.Stopbits[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RxtxChannelConfig.Stopbits var4 = var1[var3];
            if (var4.value == var0) {
               return var4;
            }
         }

         throw new IllegalArgumentException("unknown " + RxtxChannelConfig.Stopbits.class.getSimpleName() + " value: " + var0);
      }
   }
}
