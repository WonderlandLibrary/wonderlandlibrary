package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.List;

public class SocksInitResponseDecoder extends ReplayingDecoder {
   private static final String name = "SOCKS_INIT_RESPONSE_DECODER";
   private SocksProtocolVersion version;
   private SocksAuthScheme authScheme;
   private SocksResponse msg;

   public static String getName() {
      return "SOCKS_INIT_RESPONSE_DECODER";
   }

   public SocksInitResponseDecoder() {
      super(SocksInitResponseDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_RESPONSE;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch((SocksInitResponseDecoder.State)this.state()) {
      case CHECK_PROTOCOL_VERSION:
         this.version = SocksProtocolVersion.fromByte(var2.readByte());
         if (this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksInitResponseDecoder.State.READ_PREFFERED_AUTH_TYPE);
      case READ_PREFFERED_AUTH_TYPE:
         this.authScheme = SocksAuthScheme.fromByte(var2.readByte());
         this.msg = new SocksInitResponse(this.authScheme);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_PREFFERED_AUTH_TYPE;

      private static final SocksInitResponseDecoder.State[] $VALUES = new SocksInitResponseDecoder.State[]{CHECK_PROTOCOL_VERSION, READ_PREFFERED_AUTH_TYPE};
   }
}
