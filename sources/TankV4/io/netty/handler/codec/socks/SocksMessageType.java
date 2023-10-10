package io.netty.handler.codec.socks;

public enum SocksMessageType {
   REQUEST,
   RESPONSE,
   UNKNOWN;

   private static final SocksMessageType[] $VALUES = new SocksMessageType[]{REQUEST, RESPONSE, UNKNOWN};
}
