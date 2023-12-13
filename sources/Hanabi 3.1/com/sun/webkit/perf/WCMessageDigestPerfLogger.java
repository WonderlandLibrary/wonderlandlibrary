package com.sun.webkit.perf;

import com.sun.webkit.security.WCMessageDigest;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class WCMessageDigestPerfLogger extends WCMessageDigest {
   private static final Logger log = Logger.getLogger(WCMessageDigestPerfLogger.class.getName());
   private static final PerfLogger logger;
   private final WCMessageDigest digest;

   public WCMessageDigestPerfLogger(WCMessageDigest var1) {
      this.digest = var1;
   }

   public static synchronized boolean isEnabled() {
      return logger.isEnabled();
   }

   public void addBytes(ByteBuffer var1) {
      logger.resumeCount("ADDBYTES");
      this.digest.addBytes(var1);
      logger.suspendCount("ADDBYTES");
   }

   public byte[] computeHash() {
      logger.resumeCount("COMPUTEHASH");
      byte[] var1 = this.digest.computeHash();
      logger.suspendCount("COMPUTEHASH");
      return var1;
   }

   static {
      logger = PerfLogger.getLogger(log);
   }
}
