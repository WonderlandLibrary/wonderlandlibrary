package com.sun.javafx.webkit;

import com.sun.webkit.security.WCMessageDigest;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WCMessageDigestImpl extends WCMessageDigest {
   private final MessageDigest digest;

   public WCMessageDigestImpl(String var1) throws NoSuchAlgorithmException {
      this.digest = MessageDigest.getInstance(var1);
   }

   public void addBytes(ByteBuffer var1) {
      this.digest.update(var1);
   }

   public byte[] computeHash() {
      return this.digest.digest();
   }
}
