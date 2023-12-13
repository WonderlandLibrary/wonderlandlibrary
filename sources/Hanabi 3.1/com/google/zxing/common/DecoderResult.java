package com.google.zxing.common;

import java.util.List;

public final class DecoderResult {
   private final byte[] rawBytes;
   private final String text;
   private final List byteSegments;
   private final String ecLevel;

   public DecoderResult(byte[] rawBytes, String text, List byteSegments, String ecLevel) {
      this.rawBytes = rawBytes;
      this.text = text;
      this.byteSegments = byteSegments;
      this.ecLevel = ecLevel;
   }

   public byte[] getRawBytes() {
      return this.rawBytes;
   }

   public String getText() {
      return this.text;
   }

   public List getByteSegments() {
      return this.byteSegments;
   }

   public String getECLevel() {
      return this.ecLevel;
   }
}
