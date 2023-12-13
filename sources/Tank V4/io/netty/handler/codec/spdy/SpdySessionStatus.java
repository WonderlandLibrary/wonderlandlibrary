package io.netty.handler.codec.spdy;

public class SpdySessionStatus implements Comparable {
   public static final SpdySessionStatus OK = new SpdySessionStatus(0, "OK");
   public static final SpdySessionStatus PROTOCOL_ERROR = new SpdySessionStatus(1, "PROTOCOL_ERROR");
   public static final SpdySessionStatus INTERNAL_ERROR = new SpdySessionStatus(2, "INTERNAL_ERROR");
   private final int code;
   private final String statusPhrase;

   public static SpdySessionStatus valueOf(int var0) {
      switch(var0) {
      case 0:
         return OK;
      case 1:
         return PROTOCOL_ERROR;
      case 2:
         return INTERNAL_ERROR;
      default:
         return new SpdySessionStatus(var0, "UNKNOWN (" + var0 + ')');
      }
   }

   public SpdySessionStatus(int var1, String var2) {
      if (var2 == null) {
         throw new NullPointerException("statusPhrase");
      } else {
         this.code = var1;
         this.statusPhrase = var2;
      }
   }

   public int getCode() {
      return this.code;
   }

   public String getStatusPhrase() {
      return this.statusPhrase;
   }

   public int hashCode() {
      return this.getCode();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof SpdySessionStatus)) {
         return false;
      } else {
         return this.getCode() == ((SpdySessionStatus)var1).getCode();
      }
   }

   public String toString() {
      return this.getStatusPhrase();
   }

   public int compareTo(SpdySessionStatus var1) {
      return this.getCode() - var1.getCode();
   }

   public int compareTo(Object var1) {
      return this.compareTo((SpdySessionStatus)var1);
   }
}
