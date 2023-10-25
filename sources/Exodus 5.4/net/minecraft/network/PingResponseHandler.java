/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import net.minecraft.network.NetworkSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingResponseHandler
extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LogManager.getLogger();
    private NetworkSystem networkSystem;

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void channelRead(ChannelHandlerContext var1_1, Object var2_2) throws Exception {
        block11: {
            block14: {
                block12: {
                    var3_3 = (ByteBuf)var2_2;
                    var3_3.markReaderIndex();
                    var4_4 = true;
                    try {
                        if (var3_3.readUnsignedByte() != 254) break block11;
                        var5_5 = (InetSocketAddress)var1_1.channel().remoteAddress();
                        var6_7 = this.networkSystem.getServer();
                        var7_8 = var3_3.readableBytes();
                        switch (var7_8) {
                            case 0: {
                                PingResponseHandler.logger.debug("Ping: (<1.3.x) from {}:{}", new Object[]{var5_5.getAddress(), var5_5.getPort()});
                                var8_9 = String.format("%s\u00a7%d\u00a7%d", new Object[]{var6_7.getMOTD(), var6_7.getCurrentPlayerCount(), var6_7.getMaxPlayers()});
                                this.writeAndFlush(var1_1, this.getStringBuffer(var8_9));
                                ** break;
                            }
                            case 1: {
                                if (var3_3.readUnsignedByte() != 1) {
                                    if (!var4_4) break block12;
                                    break;
                                }
                                ** GOTO lbl-1000
                            }
                            default: {
                                v0 = var3_3.readUnsignedByte() == 1;
                            }
                        }
                    }
                    catch (RuntimeException var5_6) {
                        if (var4_4) {
                            var3_3.resetReaderIndex();
                            var1_1.channel().pipeline().remove("legacy_query");
                            var1_1.fireChannelRead(var2_2);
                        }
                        return;
                    }
                    var3_3.resetReaderIndex();
                    var1_1.channel().pipeline().remove("legacy_query");
                    var1_1.fireChannelRead(var2_2);
                }
                return;
lbl-1000:
                // 1 sources

                {
                    PingResponseHandler.logger.debug("Ping: (1.4-1.5.x) from {}:{}", new Object[]{var5_5.getAddress(), var5_5.getPort()});
                    var9_10 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[]{127, var6_7.getMinecraftVersion(), var6_7.getMOTD(), var6_7.getCurrentPlayerCount(), var6_7.getMaxPlayers()});
                    this.writeAndFlush(var1_1, this.getStringBuffer(var9_10));
                    ** break;
                    var10_11 = v0;
                    var10_11 &= var3_3.readUnsignedByte() == 250;
                    var10_11 &= "MC|PingHost".equals(new String(var3_3.readBytes(var3_3.readShort() * 2).array(), Charsets.UTF_16BE));
                    var11_12 = var3_3.readUnsignedShort();
                    var10_11 &= var3_3.readUnsignedByte() >= 73;
                    var10_11 &= 3 + var3_3.readBytes(var3_3.readShort() * 2).array().length + 4 == var11_12;
                    var10_11 &= var3_3.readInt() <= 65535;
                    if (var10_11 &= var3_3.readableBytes() == 0) ** GOTO lbl-1000
                    if (!var4_4) break block14;
                }
                var3_3.resetReaderIndex();
                var1_1.channel().pipeline().remove("legacy_query");
                var1_1.fireChannelRead(var2_2);
            }
            return;
lbl-1000:
            // 1 sources

            {
                PingResponseHandler.logger.debug("Ping: (1.6) from {}:{}", new Object[]{var5_5.getAddress(), var5_5.getPort()});
                var12_13 = String.format("\u00a71\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[]{127, var6_7.getMinecraftVersion(), var6_7.getMOTD(), var6_7.getCurrentPlayerCount(), var6_7.getMaxPlayers()});
                var13_14 = this.getStringBuffer(var12_13);
                this.writeAndFlush(var1_1, var13_14);
                var13_14.release();
lbl68:
                // 3 sources

                var3_3.release();
                return;
            }
        }
        if (var4_4) {
            var3_3.resetReaderIndex();
            var1_1.channel().pipeline().remove("legacy_query");
            var1_1.fireChannelRead(var2_2);
        }
    }

    private void writeAndFlush(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        channelHandlerContext.pipeline().firstContext().writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
    }

    public PingResponseHandler(NetworkSystem networkSystem) {
        this.networkSystem = networkSystem;
    }

    private ByteBuf getStringBuffer(String string) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeByte(255);
        char[] cArray = string.toCharArray();
        byteBuf.writeShort(cArray.length);
        char[] cArray2 = cArray;
        int n = cArray.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray2[n2];
            byteBuf.writeChar(c);
            ++n2;
        }
        return byteBuf;
    }
}

