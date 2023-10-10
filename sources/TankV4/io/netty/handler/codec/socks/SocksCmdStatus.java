package io.netty.handler.codec.socks;

public enum SocksCmdStatus {
   SUCCESS((byte)0),
   FAILURE((byte)1),
   FORBIDDEN((byte)2),
   NETWORK_UNREACHABLE((byte)3),
   HOST_UNREACHABLE((byte)4),
   REFUSED((byte)5),
   TTL_EXPIRED((byte)6),
   COMMAND_NOT_SUPPORTED((byte)7),
   ADDRESS_NOT_SUPPORTED((byte)8),
   UNASSIGNED((byte)-1);

   private final byte b;
   private static final SocksCmdStatus[] $VALUES = new SocksCmdStatus[]{SUCCESS, FAILURE, FORBIDDEN, NETWORK_UNREACHABLE, HOST_UNREACHABLE, REFUSED, TTL_EXPIRED, COMMAND_NOT_SUPPORTED, ADDRESS_NOT_SUPPORTED, UNASSIGNED};

   private SocksCmdStatus(byte var3) {
      this.b = var3;
   }

   public static SocksCmdStatus fromByte(byte var0) {
      SocksCmdStatus[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SocksCmdStatus var4 = var1[var3];
         if (var4.b == var0) {
            return var4;
         }
      }

      return UNASSIGNED;
   }

   public byte byteValue() {
      return this.b;
   }
}
