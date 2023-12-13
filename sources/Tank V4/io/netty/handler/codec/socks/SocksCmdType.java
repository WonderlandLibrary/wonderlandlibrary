package io.netty.handler.codec.socks;

public enum SocksCmdType {
   CONNECT((byte)1),
   BIND((byte)2),
   UDP((byte)3),
   UNKNOWN((byte)-1);

   private final byte b;
   private static final SocksCmdType[] $VALUES = new SocksCmdType[]{CONNECT, BIND, UDP, UNKNOWN};

   private SocksCmdType(byte var3) {
      this.b = var3;
   }

   public static SocksCmdType fromByte(byte var0) {
      SocksCmdType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SocksCmdType var4 = var1[var3];
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
