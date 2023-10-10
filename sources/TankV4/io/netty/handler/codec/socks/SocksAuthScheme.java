package io.netty.handler.codec.socks;

public enum SocksAuthScheme {
   NO_AUTH((byte)0),
   AUTH_GSSAPI((byte)1),
   AUTH_PASSWORD((byte)2),
   UNKNOWN((byte)-1);

   private final byte b;
   private static final SocksAuthScheme[] $VALUES = new SocksAuthScheme[]{NO_AUTH, AUTH_GSSAPI, AUTH_PASSWORD, UNKNOWN};

   private SocksAuthScheme(byte var3) {
      this.b = var3;
   }

   public static SocksAuthScheme fromByte(byte var0) {
      SocksAuthScheme[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SocksAuthScheme var4 = var1[var3];
         if (var4.b == var0) {
            return var4;
         }
      }

      return UNKNOWN;
   }

   public byte byteValue() {
      return this.b;
   }
}
