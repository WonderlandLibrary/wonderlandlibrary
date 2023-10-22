package com.google.zxing.oned.rss.expanded;

import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;

final class ExpandedPair {
   private final boolean mayBeLast;
   private final DataCharacter leftChar;
   private final DataCharacter rightChar;
   private final FinderPattern finderPattern;

   ExpandedPair(DataCharacter leftChar, DataCharacter rightChar, FinderPattern finderPattern, boolean mayBeLast) {
      this.leftChar = leftChar;
      this.rightChar = rightChar;
      this.finderPattern = finderPattern;
      this.mayBeLast = mayBeLast;
   }

   boolean mayBeLast() {
      return this.mayBeLast;
   }

   DataCharacter getLeftChar() {
      return this.leftChar;
   }

   DataCharacter getRightChar() {
      return this.rightChar;
   }

   FinderPattern getFinderPattern() {
      return this.finderPattern;
   }

   public boolean mustBeLast() {
      return this.rightChar == null;
   }
}
