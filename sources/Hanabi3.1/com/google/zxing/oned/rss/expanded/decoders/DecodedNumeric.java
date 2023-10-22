package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedNumeric extends DecodedObject {
   private final int firstDigit;
   private final int secondDigit;
   static final int FNC1 = 10;

   DecodedNumeric(int newPosition, int firstDigit, int secondDigit) {
      super(newPosition);
      this.firstDigit = firstDigit;
      this.secondDigit = secondDigit;
      if (this.firstDigit >= 0 && this.firstDigit <= 10) {
         if (this.secondDigit < 0 || this.secondDigit > 10) {
            throw new IllegalArgumentException("Invalid secondDigit: " + secondDigit);
         }
      } else {
         throw new IllegalArgumentException("Invalid firstDigit: " + firstDigit);
      }
   }

   int getFirstDigit() {
      return this.firstDigit;
   }

   int getSecondDigit() {
      return this.secondDigit;
   }

   int getValue() {
      return this.firstDigit * 10 + this.secondDigit;
   }

   boolean isFirstDigitFNC1() {
      return this.firstDigit == 10;
   }

   boolean isSecondDigitFNC1() {
      return this.secondDigit == 10;
   }

   boolean isAnyFNC1() {
      return this.firstDigit == 10 || this.secondDigit == 10;
   }
}
