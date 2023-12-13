package io.netty.handler.logging;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;

@ChannelHandler.Sharable
public class LoggingHandler extends ChannelDuplexHandler {
   private static final LogLevel DEFAULT_LEVEL;
   private static final String NEWLINE;
   private static final String[] BYTE2HEX;
   private static final String[] HEXPADDING;
   private static final String[] BYTEPADDING;
   private static final char[] BYTE2CHAR;
   protected final InternalLogger logger;
   protected final InternalLogLevel internalLevel;
   private final LogLevel level;

   public LoggingHandler() {
      this(DEFAULT_LEVEL);
   }

   public LoggingHandler(LogLevel var1) {
      if (var1 == null) {
         throw new NullPointerException("level");
      } else {
         this.logger = InternalLoggerFactory.getInstance(this.getClass());
         this.level = var1;
         this.internalLevel = var1.toInternalLevel();
      }
   }

   public LoggingHandler(Class var1) {
      this(var1, DEFAULT_LEVEL);
   }

   public LoggingHandler(Class var1, LogLevel var2) {
      if (var1 == null) {
         throw new NullPointerException("clazz");
      } else if (var2 == null) {
         throw new NullPointerException("level");
      } else {
         this.logger = InternalLoggerFactory.getInstance(var1);
         this.level = var2;
         this.internalLevel = var2.toInternalLevel();
      }
   }

   public LoggingHandler(String var1) {
      this(var1, DEFAULT_LEVEL);
   }

   public LoggingHandler(String var1, LogLevel var2) {
      if (var1 == null) {
         throw new NullPointerException("name");
      } else if (var2 == null) {
         throw new NullPointerException("level");
      } else {
         this.logger = InternalLoggerFactory.getInstance(var1);
         this.level = var2;
         this.internalLevel = var2.toInternalLevel();
      }
   }

   public LogLevel level() {
      return this.level;
   }

   protected String format(ChannelHandlerContext var1, String var2) {
      String var3 = var1.channel().toString();
      StringBuilder var4 = new StringBuilder(var3.length() + var2.length() + 1);
      var4.append(var3);
      var4.append(' ');
      var4.append(var2);
      return var4.toString();
   }

   public void channelRegistered(ChannelHandlerContext var1) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "REGISTERED"));
      }

      super.channelRegistered(var1);
   }

   public void channelUnregistered(ChannelHandlerContext var1) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "UNREGISTERED"));
      }

      super.channelUnregistered(var1);
   }

   public void channelActive(ChannelHandlerContext var1) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "ACTIVE"));
      }

      super.channelActive(var1);
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "INACTIVE"));
      }

      super.channelInactive(var1);
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "EXCEPTION: " + var2), var2);
      }

      super.exceptionCaught(var1, var2);
   }

   public void userEventTriggered(ChannelHandlerContext var1, Object var2) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "USER_EVENT: " + var2));
      }

      super.userEventTriggered(var1, var2);
   }

   public void bind(ChannelHandlerContext var1, SocketAddress var2, ChannelPromise var3) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "BIND(" + var2 + ')'));
      }

      super.bind(var1, var2, var3);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "CONNECT(" + var2 + ", " + var3 + ')'));
      }

      super.connect(var1, var2, var3, var4);
   }

   public void disconnect(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "DISCONNECT()"));
      }

      super.disconnect(var1, var2);
   }

   public void close(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "CLOSE()"));
      }

      super.close(var1, var2);
   }

   public void deregister(ChannelHandlerContext var1, ChannelPromise var2) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "DEREGISTER()"));
      }

      super.deregister(var1, var2);
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      this.logMessage(var1, "RECEIVED", var2);
      var1.fireChannelRead(var2);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) throws Exception {
      this.logMessage(var1, "WRITE", var2);
      var1.write(var2, var3);
   }

   public void flush(ChannelHandlerContext var1) throws Exception {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, "FLUSH"));
      }

      var1.flush();
   }

   private void logMessage(ChannelHandlerContext var1, String var2, Object var3) {
      if (this.logger.isEnabled(this.internalLevel)) {
         this.logger.log(this.internalLevel, this.format(var1, this.formatMessage(var2, var3)));
      }

   }

   protected String formatMessage(String var1, Object var2) {
      if (var2 instanceof ByteBuf) {
         return this.formatByteBuf(var1, (ByteBuf)var2);
      } else {
         return var2 instanceof ByteBufHolder ? this.formatByteBufHolder(var1, (ByteBufHolder)var2) : this.formatNonByteBuf(var1, var2);
      }
   }

   protected String formatByteBuf(String var1, ByteBuf var2) {
      int var3 = var2.readableBytes();
      int var4 = var3 / 16 + (var3 % 15 == 0 ? 0 : 1) + 4;
      StringBuilder var5 = new StringBuilder(var4 * 80 + var1.length() + 16);
      var5.append(var1).append('(').append(var3).append('B').append(')');
      var5.append(NEWLINE + "         +-------------------------------------------------+" + NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + NEWLINE + "+--------+-------------------------------------------------+----------------+");
      int var6 = var2.readerIndex();
      int var7 = var2.writerIndex();

      int var8;
      int var9;
      int var10;
      for(var8 = var6; var8 < var7; ++var8) {
         var9 = var8 - var6;
         var10 = var9 & 15;
         if (var10 == 0) {
            var5.append(NEWLINE);
            var5.append(Long.toHexString((long)var9 & 4294967295L | 4294967296L));
            var5.setCharAt(var5.length() - 9, '|');
            var5.append('|');
         }

         var5.append(BYTE2HEX[var2.getUnsignedByte(var8)]);
         if (var10 == 15) {
            var5.append(" |");

            for(int var11 = var8 - 15; var11 <= var8; ++var11) {
               var5.append(BYTE2CHAR[var2.getUnsignedByte(var11)]);
            }

            var5.append('|');
         }
      }

      if ((var8 - var6 & 15) != 0) {
         var9 = var3 & 15;
         var5.append(HEXPADDING[var9]);
         var5.append(" |");

         for(var10 = var8 - var9; var10 < var8; ++var10) {
            var5.append(BYTE2CHAR[var2.getUnsignedByte(var10)]);
         }

         var5.append(BYTEPADDING[var9]);
         var5.append('|');
      }

      var5.append(NEWLINE + "+--------+-------------------------------------------------+----------------+");
      return var5.toString();
   }

   protected String formatNonByteBuf(String var1, Object var2) {
      return var1 + ": " + var2;
   }

   protected String formatByteBufHolder(String var1, ByteBufHolder var2) {
      return this.formatByteBuf(var1, var2.content());
   }

   static {
      DEFAULT_LEVEL = LogLevel.DEBUG;
      NEWLINE = String.format("%n");
      BYTE2HEX = new String[256];
      HEXPADDING = new String[16];
      BYTEPADDING = new String[16];
      BYTE2CHAR = new char[256];

      int var0;
      StringBuilder var1;
      for(var0 = 0; var0 < 10; ++var0) {
         var1 = new StringBuilder(3);
         var1.append(" 0");
         var1.append(var0);
         BYTE2HEX[var0] = var1.toString();
      }

      while(var0 < 16) {
         var1 = new StringBuilder(3);
         var1.append(" 0");
         var1.append((char)(97 + var0 - 10));
         BYTE2HEX[var0] = var1.toString();
         ++var0;
      }

      while(var0 < BYTE2HEX.length) {
         var1 = new StringBuilder(3);
         var1.append(' ');
         var1.append(Integer.toHexString(var0));
         BYTE2HEX[var0] = var1.toString();
         ++var0;
      }

      StringBuilder var2;
      int var3;
      int var4;
      for(var0 = 0; var0 < HEXPADDING.length; ++var0) {
         var4 = HEXPADDING.length - var0;
         var2 = new StringBuilder(var4 * 3);

         for(var3 = 0; var3 < var4; ++var3) {
            var2.append("   ");
         }

         HEXPADDING[var0] = var2.toString();
      }

      for(var0 = 0; var0 < BYTEPADDING.length; ++var0) {
         var4 = BYTEPADDING.length - var0;
         var2 = new StringBuilder(var4);

         for(var3 = 0; var3 < var4; ++var3) {
            var2.append(' ');
         }

         BYTEPADDING[var0] = var2.toString();
      }

      for(var0 = 0; var0 < BYTE2CHAR.length; ++var0) {
         if (var0 > 31 && var0 < 127) {
            BYTE2CHAR[var0] = (char)var0;
         } else {
            BYTE2CHAR[var0] = '.';
         }
      }

   }
}
