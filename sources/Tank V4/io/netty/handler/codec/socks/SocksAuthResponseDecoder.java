package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

public class SocksAuthResponseDecoder extends ReplayingDecoder {
   private static final String name = "SOCKS_AUTH_RESPONSE_DECODER";
   private SocksSubnegotiationVersion version;
   private SocksAuthStatus authStatus;
   private SocksResponse msg;

   public static String getName() {
      return "SOCKS_AUTH_RESPONSE_DECODER";
   }

   public SocksAuthResponseDecoder() {
      super(SocksAuthResponseDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch((SocksAuthResponseDecoder.State)this.state()) {
      case CHECK_PROTOCOL_VERSION:
         this.version = SocksSubnegotiationVersion.fromByte(var2.readByte());
         if (this.version != SocksSubnegotiationVersion.AUTH_PASSWORD) {
            break;
         }

         this.checkpoint(SocksAuthResponseDecoder.State.READ_AUTH_RESPONSE);
      case READ_AUTH_RESPONSE:
         this.authStatus = SocksAuthStatus.fromByte(var2.readByte());
         this.msg = new SocksAuthResponse(this.authStatus);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_AUTH_RESPONSE;

      private static final SocksAuthResponseDecoder.State[] $VALUES = new SocksAuthResponseDecoder.State[]{CHECK_PROTOCOL_VERSION, READ_AUTH_RESPONSE};
   }
}
