package io.netty.handler.codec.socks;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import java.util.List;

public class SocksCmdRequestDecoder extends ReplayingDecoder {
   private static final String name = "SOCKS_CMD_REQUEST_DECODER";
   private SocksProtocolVersion version;
   private int fieldLength;
   private SocksCmdType cmdType;
   private SocksAddressType addressType;
   private byte reserved;
   private String host;
   private int port;
   private SocksRequest msg;

   public static String getName() {
      return "SOCKS_CMD_REQUEST_DECODER";
   }

   public SocksCmdRequestDecoder() {
      super(SocksCmdRequestDecoder.State.CHECK_PROTOCOL_VERSION);
      this.msg = SocksCommonUtils.UNKNOWN_SOCKS_REQUEST;
   }

   protected void decode(ChannelHandlerContext var1, ByteBuf var2, List var3) throws Exception {
      switch((SocksCmdRequestDecoder.State)this.state()) {
      case CHECK_PROTOCOL_VERSION:
         this.version = SocksProtocolVersion.fromByte(var2.readByte());
         if (this.version != SocksProtocolVersion.SOCKS5) {
            break;
         }

         this.checkpoint(SocksCmdRequestDecoder.State.READ_CMD_HEADER);
      case READ_CMD_HEADER:
         this.cmdType = SocksCmdType.fromByte(var2.readByte());
         this.reserved = var2.readByte();
         this.addressType = SocksAddressType.fromByte(var2.readByte());
         this.checkpoint(SocksCmdRequestDecoder.State.READ_CMD_ADDRESS);
      case READ_CMD_ADDRESS:
         switch(this.addressType) {
         case IPv4:
            this.host = SocksCommonUtils.intToIp(var2.readInt());
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
            break;
         case DOMAIN:
            this.fieldLength = var2.readByte();
            this.host = var2.readBytes(this.fieldLength).toString(CharsetUtil.US_ASCII);
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
            break;
         case IPv6:
            this.host = SocksCommonUtils.ipv6toStr(var2.readBytes(16).array());
            this.port = var2.readUnsignedShort();
            this.msg = new SocksCmdRequest(this.cmdType, this.addressType, this.host, this.port);
         case UNKNOWN:
         }
      }

      var1.pipeline().remove((ChannelHandler)this);
      var3.add(this.msg);
   }

   static enum State {
      CHECK_PROTOCOL_VERSION,
      READ_CMD_HEADER,
      READ_CMD_ADDRESS;

      private static final SocksCmdRequestDecoder.State[] $VALUES = new SocksCmdRequestDecoder.State[]{CHECK_PROTOCOL_VERSION, READ_CMD_HEADER, READ_CMD_ADDRESS};
   }
}
