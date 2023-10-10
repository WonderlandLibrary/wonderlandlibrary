package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import java.util.ArrayList;
import java.util.List;

public class SocksInitRequestDecoder extends ReplayingDecoder {
   private static final String name = "SOCKS_INIT_REQUEST_DECODER";
   private final List authSchemes = new ArrayList();
   private SocksProtocolVersion version;
   private byte authSchemeNum;
   private SocksRequest msg;

   public static String getName() {
      return "SOCKS_INIT_REQUEST_DECODER";
   }

   public SocksInitRequestDecoder() {
      super(SocksInitRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch((SocksInitRequestDecoder.State)this.state()) {
      case CHECK_PROTOCOL_VERSION:
         this.version = SocksProtocolVersion.fromByte(var2.readByte());
         if (this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksInitRequestDecoder.State.READ_AUTH_SCHEMES);
      case READ_AUTH_SCHEMES:
         this.authSchemes.clear();
         this.authSchemeNum = var2.readByte();

         for(int var4 = 0; var4 < this.authSchemeNum; ++var4) {
            this.authSchemes.add(SocksAuthScheme.fromByte(var2.readByte()));
         }

         this.msg = new SocksInitRequest(this.authSchemes);
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_AUTH_SCHEMES;

      private static final SocksInitRequestDecoder.State[] $VALUES = new SocksInitRequestDecoder.State[]{CHECK_PROTOCOL_VERSION, READ_AUTH_SCHEMES};
   }
}
