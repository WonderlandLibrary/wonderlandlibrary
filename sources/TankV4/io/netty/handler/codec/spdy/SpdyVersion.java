package io.netty.handler.codec.spdy;

public enum SpdyVersion {
   SPDY_3(3, 0, false),
   SPDY_3_1(3, 1, true);

   private final int version;
   private final int minorVersion;
   private final boolean sessionFlowControl;
   private static final SpdyVersion[] $VALUES = new SpdyVersion[]{SPDY_3, SPDY_3_1};

   private SpdyVersion(int var3, int var4, boolean var5) {
      this.version = var3;
      this.minorVersion = var4;
      this.sessionFlowControl = var5;
   }

   int getVersion() {
      return this.version;
   }

   int getMinorVersion() {
      return this.minorVersion;
   }

   boolean useSessionFlowControl() {
      return this.sessionFlowControl;
   }
}
