package com.google.zxing.oned.rss.expanded.decoders;

final class DecodedChar extends DecodedObject {
   private final char value;
   static final char FNC1 = '$';

   DecodedChar(int newPosition, char value) {
      super(newPosition);
      this.value = value;
   }

   char getValue() {
      return this.value;
   }

   boolean isFNC1() {
      return this.value == '$';
   }
}
