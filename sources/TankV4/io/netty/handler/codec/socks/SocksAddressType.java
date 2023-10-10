package io.netty.handler.codec.socks;

public enum SocksAddressType {
   IPv4((byte)1),
   DOMAIN((byte)3),
   IPv6((byte)4),
   UNKNOWN((byte)-1);

   private final byte b;
   private static final SocksAddressType[] $VALUES = new SocksAddressType[]{IPv4, DOMAIN, IPv6, UNKNOWN};

   private SocksAddressType(byte var3) {
      this.b = var3;
   }

   public static SocksAddressType fromByte(byte var0) {
      SocksAddressType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SocksAddressType var4 = var1[var3];
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
