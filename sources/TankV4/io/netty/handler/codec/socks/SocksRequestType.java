package io.netty.handler.codec.socks;

public enum SocksRequestType {
   INIT,
   AUTH,
   CMD,
   UNKNOWN;

   private static final SocksRequestType[] $VALUES = new SocksRequestType[]{INIT, AUTH, CMD, UNKNOWN};
}
