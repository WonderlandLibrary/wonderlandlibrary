package com.sun.webkit.security;

import com.sun.javafx.webkit.WCMessageDigestImpl;
import com.sun.webkit.perf.WCMessageDigestPerfLogger;
import java.nio.ByteBuffer;

public abstract class WCMessageDigest {
   protected static WCMessageDigest getInstance(String var0) {
      try {
         WCMessageDigestImpl var1 = new WCMessageDigestImpl(var0);
         return (WCMessageDigest)(WCMessageDigestPerfLogger.isEnabled() ? new WCMessageDigestPerfLogger(var1) : var1);
      } catch (Exception var2) {
         return null;
      }
   }

   public abstract void addBytes(ByteBuffer var1);

   public abstract byte[] computeHash();
}
