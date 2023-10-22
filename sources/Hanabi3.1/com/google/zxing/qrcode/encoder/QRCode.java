package com.google.zxing.qrcode.encoder;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.decoder.Version;

public final class QRCode {
   public static final int NUM_MASK_PATTERNS = 8;
   private Mode mode;
   private ErrorCorrectionLevel ecLevel;
   private Version version;
   private int maskPattern = -1;
   private ByteMatrix matrix;

   public Mode getMode() {
      return this.mode;
   }

   public ErrorCorrectionLevel getECLevel() {
      return this.ecLevel;
   }

   public Version getVersion() {
      return this.version;
   }

   public int getMaskPattern() {
      return this.maskPattern;
   }

   public ByteMatrix getMatrix() {
      return this.matrix;
   }

   public String toString() {
      StringBuilder result = new StringBuilder(200);
      result.append("<<\n");
      result.append(" mode: ");
      result.append(this.mode);
      result.append("\n ecLevel: ");
      result.append(this.ecLevel);
      result.append("\n version: ");
      result.append(this.version);
      result.append("\n maskPattern: ");
      result.append(this.maskPattern);
      if (this.matrix == null) {
         result.append("\n matrix: null\n");
      } else {
         result.append("\n matrix:\n");
         result.append(this.matrix.toString());
      }

      result.append(">>\n");
      return result.toString();
   }

   public void setMode(Mode value) {
      this.mode = value;
   }

   public void setECLevel(ErrorCorrectionLevel value) {
      this.ecLevel = value;
   }

   public void setVersion(Version version) {
      this.version = version;
   }

   public void setMaskPattern(int value) {
      this.maskPattern = value;
   }

   public void setMatrix(ByteMatrix value) {
      this.matrix = value;
   }

   public static boolean isValidMaskPattern(int maskPattern) {
      return maskPattern >= 0 && maskPattern < 8;
   }
}
