package io.netty.handler.codec.socks;

public enum SocksAuthStatus {
   SUCCESS((byte)0),
   FAILURE((byte)-1);

   private final byte b;
   private static final SocksAuthStatus[] $VALUES = new SocksAuthStatus[]{SUCCESS, FAILURE};

   private SocksAuthStatus(byte var3) {
      this.b = var3;
   }

   public static SocksAuthStatus fromByte(byte var0) {
      SocksAuthStatus[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SocksAuthStatus var4 = var1[var3];
         if (var4.b == var0) {
            return var4;
         }
      }

      return FAILURE;
   }

   public byte byteValue() {
      return this.b;
   }
}
