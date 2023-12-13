package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

public class SocksAuthRequestDecoder extends ReplayingDecoder {
   private static final String name = "SOCKS_AUTH_REQUEST_DECODER";
   private SocksSubnegotiationVersion version;
   private int fieldLength;
   private String username;
   private String password;
   private SocksRequest msg;

   public static String getName() {
      return "SOCKS_AUTH_REQUEST_DECODER";
   }

   public SocksAuthRequestDecoder() {
      super(SocksAuthRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch((SocksAuthRequestDecoder.State)this.state()) {
      case CHECK_PROTOCOL_VERSION:
         this.version = SocksSubnegotiationVersion.fromByte(var2.readByte());
         if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
            break;
         }

         this.checkpoint(SocksAuthRequestDecoder.State.READ_USERNAME);
      case READ_USERNAME:
         this.fieldLength = var2.readByte();
         this.username = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
         this.checkpoint(SocksAuthRequestDecoder.State.READ_PASSWORD);
      case READ_PASSWORD:
         this.fieldLength = var2.readByte();
         this.password = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
         this.msg = new SocksAuthRequest(this.username, this.password);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_USERNAME,
      READ_PASSWORD;

      private static final SocksAuthRequestDecoder.State[] $VALUES = new SocksAuthRequestDecoder.State[]{CHECK_PROTOCOL_VERSION, READ_USERNAME, READ_PASSWORD};
   }
}
