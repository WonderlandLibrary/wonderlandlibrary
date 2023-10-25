/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.logging;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.SocketAddress;

@ChannelHandler.Sharable
public class LoggingHandler
extends ChannelDuplexHandler {
    private static final String[] BYTEPADDING;
    private static final String NEWLINE;
    private static final LogLevel DEFAULT_LEVEL;
    protected final InternalLogger logger;
    private final LogLevel level;
    private static final String[] BYTE2HEX;
    private static final String[] HEXPADDING;
    private static final char[] BYTE2CHAR;
    protected final InternalLogLevel internalLevel;

    @Override
    public void deregister(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "DEREGISTER()"));
        }
        super.deregister(channelHandlerContext, channelPromise);
    }

    protected String formatNonByteBuf(String string, Object object) {
        return string + ": " + object;
    }

    public LoggingHandler(String string) {
        this(string, DEFAULT_LEVEL);
    }

    static {
        int n;
        StringBuilder stringBuilder;
        int n2;
        int n3;
        DEFAULT_LEVEL = LogLevel.DEBUG;
        NEWLINE = StringUtil.NEWLINE;
        BYTE2HEX = new String[256];
        HEXPADDING = new String[16];
        BYTEPADDING = new String[16];
        BYTE2CHAR = new char[256];
        for (n3 = 0; n3 < BYTE2HEX.length; ++n3) {
            LoggingHandler.BYTE2HEX[n3] = ' ' + StringUtil.byteToHexStringPadded(n3);
        }
        for (n3 = 0; n3 < HEXPADDING.length; ++n3) {
            n2 = HEXPADDING.length - n3;
            stringBuilder = new StringBuilder(n2 * 3);
            for (n = 0; n < n2; ++n) {
                stringBuilder.append("   ");
            }
            LoggingHandler.HEXPADDING[n3] = stringBuilder.toString();
        }
        for (n3 = 0; n3 < BYTEPADDING.length; ++n3) {
            n2 = BYTEPADDING.length - n3;
            stringBuilder = new StringBuilder(n2);
            for (n = 0; n < n2; ++n) {
                stringBuilder.append(' ');
            }
            LoggingHandler.BYTEPADDING[n3] = stringBuilder.toString();
        }
        for (n3 = 0; n3 < BYTE2CHAR.length; ++n3) {
            LoggingHandler.BYTE2CHAR[n3] = n3 <= 31 || n3 >= 127 ? 46 : (char)n3;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "INACTIVE"));
        }
        super.channelInactive(channelHandlerContext);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "EXCEPTION: " + throwable), throwable);
        }
        super.exceptionCaught(channelHandlerContext, throwable);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "REGISTERED"));
        }
        super.channelRegistered(channelHandlerContext);
    }

    public LoggingHandler(LogLevel logLevel) {
        if (logLevel == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(this.getClass());
        this.level = logLevel;
        this.internalLevel = logLevel.toInternalLevel();
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "ACTIVE"));
        }
        super.channelActive(channelHandlerContext);
    }

    private void logMessage(ChannelHandlerContext channelHandlerContext, String string, Object object) {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, this.formatMessage(string, object)));
        }
    }

    protected String formatByteBufHolder(String string, ByteBufHolder byteBufHolder) {
        return this.formatByteBuf(string, byteBufHolder.content());
    }

    @Override
    public void bind(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, ChannelPromise channelPromise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "BIND(" + socketAddress + ')'));
        }
        super.bind(channelHandlerContext, socketAddress, channelPromise);
    }

    @Override
    public void close(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "CLOSE()"));
        }
        super.close(channelHandlerContext, channelPromise);
    }

    protected String formatMessage(String string, Object object) {
        if (object instanceof ByteBuf) {
            return this.formatByteBuf(string, (ByteBuf)object);
        }
        if (object instanceof ByteBufHolder) {
            return this.formatByteBufHolder(string, (ByteBufHolder)object);
        }
        return this.formatNonByteBuf(string, object);
    }

    public LoggingHandler() {
        this(DEFAULT_LEVEL);
    }

    protected String formatByteBuf(String string, ByteBuf byteBuf) {
        int n;
        int n2;
        int n3;
        int n4 = byteBuf.readableBytes();
        int n5 = n4 / 16 + (n4 % 15 == 0 ? 0 : 1) + 4;
        StringBuilder stringBuilder = new StringBuilder(n5 * 80 + string.length() + 16);
        stringBuilder.append(string).append('(').append(n4).append('B').append(')');
        stringBuilder.append(NEWLINE + "         +-------------------------------------------------+" + NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" + NEWLINE + "+--------+-------------------------------------------------+----------------+");
        int n6 = byteBuf.readerIndex();
        int n7 = byteBuf.writerIndex();
        for (n3 = n6; n3 < n7; ++n3) {
            n2 = n3 - n6;
            n = n2 & 0xF;
            if (n == 0) {
                stringBuilder.append(NEWLINE);
                stringBuilder.append(Long.toHexString((long)n2 & 0xFFFFFFFFL | 0x100000000L));
                stringBuilder.setCharAt(stringBuilder.length() - 9, '|');
                stringBuilder.append('|');
            }
            stringBuilder.append(BYTE2HEX[byteBuf.getUnsignedByte(n3)]);
            if (n != 15) continue;
            stringBuilder.append(" |");
            for (int i = n3 - 15; i <= n3; ++i) {
                stringBuilder.append(BYTE2CHAR[byteBuf.getUnsignedByte(i)]);
            }
            stringBuilder.append('|');
        }
        if ((n3 - n6 & 0xF) != 0) {
            n2 = n4 & 0xF;
            stringBuilder.append(HEXPADDING[n2]);
            stringBuilder.append(" |");
            for (n = n3 - n2; n < n3; ++n) {
                stringBuilder.append(BYTE2CHAR[byteBuf.getUnsignedByte(n)]);
            }
            stringBuilder.append(BYTEPADDING[n2]);
            stringBuilder.append('|');
        }
        stringBuilder.append(NEWLINE + "+--------+-------------------------------------------------+----------------+");
        return stringBuilder.toString();
    }

    public LoggingHandler(String string, LogLevel logLevel) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        if (logLevel == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(string);
        this.level = logLevel;
        this.internalLevel = logLevel.toInternalLevel();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "UNREGISTERED"));
        }
        super.channelUnregistered(channelHandlerContext);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "USER_EVENT: " + object));
        }
        super.userEventTriggered(channelHandlerContext, object);
    }

    protected String format(ChannelHandlerContext channelHandlerContext, String string) {
        String string2 = channelHandlerContext.channel().toString();
        StringBuilder stringBuilder = new StringBuilder(string2.length() + string.length() + 1);
        stringBuilder.append(string2);
        stringBuilder.append(' ');
        stringBuilder.append(string);
        return stringBuilder.toString();
    }

    public LogLevel level() {
        return this.level;
    }

    @Override
    public void connect(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress, SocketAddress socketAddress2, ChannelPromise channelPromise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "CONNECT(" + socketAddress + ", " + socketAddress2 + ')'));
        }
        super.connect(channelHandlerContext, socketAddress, socketAddress2, channelPromise);
    }

    @Override
    public void flush(ChannelHandlerContext channelHandlerContext) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "FLUSH"));
        }
        channelHandlerContext.flush();
    }

    @Override
    public void write(ChannelHandlerContext channelHandlerContext, Object object, ChannelPromise channelPromise) throws Exception {
        this.logMessage(channelHandlerContext, "WRITE", object);
        channelHandlerContext.write(object, channelPromise);
    }

    public LoggingHandler(Class<?> clazz) {
        this(clazz, DEFAULT_LEVEL);
    }

    public LoggingHandler(Class<?> clazz, LogLevel logLevel) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }
        if (logLevel == null) {
            throw new NullPointerException("level");
        }
        this.logger = InternalLoggerFactory.getInstance(clazz);
        this.level = logLevel;
        this.internalLevel = logLevel.toInternalLevel();
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {
        this.logMessage(channelHandlerContext, "RECEIVED", object);
        channelHandlerContext.fireChannelRead(object);
    }

    @Override
    public void disconnect(ChannelHandlerContext channelHandlerContext, ChannelPromise channelPromise) throws Exception {
        if (this.logger.isEnabled(this.internalLevel)) {
            this.logger.log(this.internalLevel, this.format(channelHandlerContext, "DISCONNECT()"));
        }
        super.disconnect(channelHandlerContext, channelPromise);
    }
}

