package net.minecraft.network;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingResponseHandler extends ChannelInboundHandlerAdapter {
   private static final Logger logger = LogManager.getLogger();
   private NetworkSystem networkSystem;

   private void writeAndFlush(ChannelHandlerContext var1, ByteBuf var2) {
      var1.pipeline().firstContext().writeAndFlush(var2).addListener(ChannelFutureListener.CLOSE);
   }

   private ByteBuf getStringBuffer(String var1) {
      ByteBuf var2 = Unpooled.buffer();
      var2.writeByte(255);
      char[] var3 = var1.toCharArray();
      var2.writeShort(var3.length);
      char[] var7 = var3;
      int var6 = var3.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         char var4 = var7[var5];
         var2.writeChar(var4);
      }

      return var2;
   }

   public PingResponseHandler(NetworkSystem var1) {
      this.networkSystem = var1;
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
      ByteBuf var3 = (ByteBuf)var2;
      var3.markReaderIndex();
      boolean var4 = true;

      label98: {
         label99: {
            label100: {
               try {
                  if (var3.readUnsignedByte() != 254) {
                     break label99;
                  }

                  InetSocketAddress var5 = (InetSocketAddress)var1.channel().remoteAddress();
                  MinecraftServer var6 = this.networkSystem.getServer();
                  int var7 = var3.readableBytes();
                  switch(var7) {
                  case 0:
                     logger.debug("Ping: (<1.3.x) from {}:{}", var5.getAddress(), var5.getPort());
                     String var8 = String.format("%s§%d§%d", var6.getMOTD(), var6.getCurrentPlayerCount(), var6.getMaxPlayers());
                     this.writeAndFlush(var1, this.getStringBuffer(var8));
                     break;
                  case 1:
                     if (var3.readUnsignedByte() != 1) {
                        break label98;
                     }

                     logger.debug("Ping: (1.4-1.5.x) from {}:{}", var5.getAddress(), var5.getPort());
                     String var9 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, var6.getMinecraftVersion(), var6.getMOTD(), var6.getCurrentPlayerCount(), var6.getMaxPlayers());
                     this.writeAndFlush(var1, this.getStringBuffer(var9));
                     break;
                  default:
                     boolean var10 = var3.readUnsignedByte() == 1;
                     var10 &= var3.readUnsignedByte() == 250;
                     var10 &= "MC|PingHost".equals(new String(var3.readBytes(var3.readShort() * 2).array(), Charsets.UTF_16BE));
                     int var11 = var3.readUnsignedShort();
                     var10 &= var3.readUnsignedByte() >= 73;
                     var10 &= 3 + var3.readBytes(var3.readShort() * 2).array().length + 4 == var11;
                     var10 &= var3.readInt() <= 65535;
                     var10 &= var3.readableBytes() == 0;
                     if (!var10) {
                        break label100;
                     }

                     logger.debug("Ping: (1.6) from {}:{}", var5.getAddress(), var5.getPort());
                     String var12 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", 127, var6.getMinecraftVersion(), var6.getMOTD(), var6.getCurrentPlayerCount(), var6.getMaxPlayers());
                     ByteBuf var13 = this.getStringBuffer(var12);
                     this.writeAndFlush(var1, var13);
                     var13.release();
                  }

                  var3.release();
                  var4 = false;
               } catch (RuntimeException var16) {
                  if (var4) {
                     var3.resetReaderIndex();
                     var1.channel().pipeline().remove("legacy_query");
                     var1.fireChannelRead(var2);
                  }

                  return;
               }

               if (var4) {
                  var3.resetReaderIndex();
                  var1.channel().pipeline().remove("legacy_query");
                  var1.fireChannelRead(var2);
               }

               return;
            }

            if (var4) {
               var3.resetReaderIndex();
               var1.channel().pipeline().remove("legacy_query");
               var1.fireChannelRead(var2);
            }

            return;
         }

         if (var4) {
            var3.resetReaderIndex();
            var1.channel().pipeline().remove("legacy_query");
            var1.fireChannelRead(var2);
         }

         return;
      }

      if (var4) {
         var3.resetReaderIndex();
         var1.channel().pipeline().remove("legacy_query");
         var1.fireChannelRead(var2);
      }

   }
}
