package io.netty.handler.codec.socks;

public enum SocksResponseType {
   INIT,
   AUTH,
   CMD,
   UNKNOWN;

   private static final SocksResponseType[] $VALUES = new SocksResponseType[]{INIT, AUTH, CMD, UNKNOWN};
}
