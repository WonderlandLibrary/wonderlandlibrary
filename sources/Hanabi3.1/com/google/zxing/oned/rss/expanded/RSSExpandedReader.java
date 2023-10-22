package com.google.zxing.oned.rss.expanded;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.BitArray;
import com.google.zxing.oned.rss.AbstractRSSReader;
import com.google.zxing.oned.rss.DataCharacter;
import com.google.zxing.oned.rss.FinderPattern;
import com.google.zxing.oned.rss.RSSUtils;
import com.google.zxing.oned.rss.expanded.decoders.AbstractExpandedDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class RSSExpandedReader extends AbstractRSSReader {
   private static final int[] SYMBOL_WIDEST = new int[]{7, 5, 4, 3, 1};
   private static final int[] EVEN_TOTAL_SUBSET = new int[]{4, 20, 52, 104, 204};
   private static final int[] GSUM = new int[]{0, 348, 1388, 2948, 3988};
   private static final int[][] FINDER_PATTERNS = new int[][]{{1, 8, 4, 1}, {3, 6, 4, 1}, {3, 4, 6, 1}, {3, 2, 8, 1}, {2, 6, 5, 1}, {2, 2, 9, 1}};
   private static final int[][] WEIGHTS = new int[][]{{1, 3, 9, 27, 81, 32, 96, 77}, {20, 60, 180, 118, 143, 7, 21, 63}, {189, 145, 13, 39, 117, 140, 209, 205}, {193, 157, 49, 147, 19, 57, 171, 91}, {62, 186, 136, 197, 169, 85, 44, 132}, {185, 133, 188, 142, 4, 12, 36, 108}, {113, 128, 173, 97, 80, 29, 87, 50}, {150, 28, 84, 41, 123, 158, 52, 156}, {46, 138, 203, 187, 139, 206, 196, 166}, {76, 17, 51, 153, 37, 111, 122, 155}, {43, 129, 176, 106, 107, 110, 119, 146}, {16, 48, 144, 10, 30, 90, 59, 177}, {109, 116, 137, 200, 178, 112, 125, 164}, {70, 210, 208, 202, 184, 130, 179, 115}, {134, 191, 151, 31, 93, 68, 204, 190}, {148, 22, 66, 198, 172, 94, 71, 2}, {6, 18, 54, 162, 64, 192, 154, 40}, {120, 149, 25, 75, 14, 42, 126, 167}, {79, 26, 78, 23, 69, 207, 199, 175}, {103, 98, 83, 38, 114, 131, 182, 124}, {161, 61, 183, 127, 170, 88, 53, 159}, {55, 165, 73, 8, 24, 72, 5, 15}, {45, 135, 194, 160, 58, 174, 100, 89}};
   private static final int FINDER_PAT_A = 0;
   private static final int FINDER_PAT_B = 1;
   private static final int FINDER_PAT_C = 2;
   private static final int FINDER_PAT_D = 3;
   private static final int FINDER_PAT_E = 4;
   private static final int FINDER_PAT_F = 5;
   private static final int[][] FINDER_PATTERN_SEQUENCES = new int[][]{{0, 0}, {0, 1, 1}, {0, 2, 1, 3}, {0, 4, 1, 3, 2}, {0, 4, 1, 3, 3, 5}, {0, 4, 1, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 2, 3, 3}, {0, 0, 1, 1, 2, 2, 3, 4, 4}, {0, 0, 1, 1, 2, 2, 3, 4, 5, 5}, {0, 0, 1, 1, 2, 3, 3, 4, 4, 5, 5}};
   private static final int LONGEST_SEQUENCE_SIZE;
   private static final int MAX_PAIRS = 11;
   private final List pairs = new ArrayList(11);
   private final int[] startEnd = new int[2];
   private final int[] currentSequence;

   public RSSExpandedReader() {
      this.currentSequence = new int[LONGEST_SEQUENCE_SIZE];
   }

   public Result decodeRow(int rowNumber, BitArray row, Map hints) throws NotFoundException {
      this.reset();
      this.decodeRow2pairs(rowNumber, row);
      return constructResult(this.pairs);
   }

   public void reset() {
      this.pairs.clear();
   }

   List decodeRow2pairs(int rowNumber, BitArray row) throws NotFoundException {
      while(true) {
         ExpandedPair nextPair = this.retrieveNextPair(row, this.pairs, rowNumber);
         this.pairs.add(nextPair);
         if (nextPair.mayBeLast()) {
            if (this.checkChecksum()) {
               return this.pairs;
            }

            if (nextPair.mustBeLast()) {
               throw NotFoundException.getNotFoundInstance();
            }
         }
      }
   }

   private static Result constructResult(List pairs) throws NotFoundException {
      BitArray binary = BitArrayBuilder.buildBitArray(pairs);
      AbstractExpandedDecoder decoder = AbstractExpandedDecoder.createDecoder(binary);
      String resultingString = decoder.parseInformation();
      ResultPoint[] firstPoints = ((ExpandedPair)pairs.get(0)).getFinderPattern().getResultPoints();
      ResultPoint[] lastPoints = ((ExpandedPair)pairs.get(pairs.size() - 1)).getFinderPattern().getResultPoints();
      return new Result(resultingString, (byte[])null, new ResultPoint[]{firstPoints[0], firstPoints[1], lastPoints[0], lastPoints[1]}, BarcodeFormat.RSS_EXPANDED);
   }

   private boolean checkChecksum() {
      ExpandedPair firstPair = (ExpandedPair)this.pairs.get(0);
      DataCharacter checkCharacter = firstPair.getLeftChar();
      DataCharacter firstCharacter = firstPair.getRightChar();
      int checksum = firstCharacter.getChecksumPortion();
      int s = 2;

      int checkCharacterValue;
      for(checkCharacterValue = 1; checkCharacterValue < this.pairs.size(); ++checkCharacterValue) {
         ExpandedPair currentPair = (ExpandedPair)this.pairs.get(checkCharacterValue);
         checksum += currentPair.getLeftChar().getChecksumPortion();
         ++s;
         DataCharacter currentRightChar = currentPair.getRightChar();
         if (currentRightChar != null) {
            checksum += currentRightChar.getChecksumPortion();
            ++s;
         }
      }

      checksum %= 211;
      checkCharacterValue = 211 * (s - 4) + checksum;
      return checkCharacterValue == checkCharacter.getValue();
   }

   private static int getNextSecondBar(BitArray row, int initialPos) {
      int currentPos;
      if (row.get(initialPos)) {
         currentPos = row.getNextUnset(initialPos);
         currentPos = row.getNextSet(currentPos);
      } else {
         currentPos = row.getNextSet(initialPos);
         currentPos = row.getNextUnset(currentPos);
      }

      return currentPos;
   }

   ExpandedPair retrieveNextPair(BitArray row, List previousPairs, int rowNumber) throws NotFoundException {
      boolean isOddPattern = previousPairs.size() % 2 == 0;
      boolean keepFinding = true;
      int forcedOffset = -1;

      FinderPattern pattern;
      do {
         this.findNextPair(row, previousPairs, forcedOffset);
         pattern = this.parseFoundFinderPattern(row, rowNumber, isOddPattern);
         if (pattern == null) {
            forcedOffset = getNextSecondBar(row, this.startEnd[0]);
         } else {
            keepFinding = false;
         }
      } while(keepFinding);

      boolean mayBeLast = this.checkPairSequence(previousPairs, pattern);
      DataCharacter leftChar = this.decodeDataCharacter(row, pattern, isOddPattern, true);

      DataCharacter rightChar;
      try {
         rightChar = this.decodeDataCharacter(row, pattern, isOddPattern, false);
      } catch (NotFoundException var12) {
         if (!mayBeLast) {
            throw var12;
         }

         rightChar = null;
      }

      return new ExpandedPair(leftChar, rightChar, pattern, mayBeLast);
   }

   private boolean checkPairSequence(List previousPairs, FinderPattern pattern) throws NotFoundException {
      int currentSequenceLength = previousPairs.size() + 1;
      if (currentSequenceLength > this.currentSequence.length) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         for(int pos = 0; pos < previousPairs.size(); ++pos) {
            this.currentSequence[pos] = ((ExpandedPair)previousPairs.get(pos)).getFinderPattern().getValue();
         }

         this.currentSequence[currentSequenceLength - 1] = pattern.getValue();
         int[][] arr$ = FINDER_PATTERN_SEQUENCES;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int[] validSequence = arr$[i$];
            if (validSequence.length >= currentSequenceLength) {
               boolean valid = true;

               for(int pos = 0; pos < currentSequenceLength; ++pos) {
                  if (this.currentSequence[pos] != validSequence[pos]) {
                     valid = false;
                     break;
                  }
               }

               if (valid) {
                  return currentSequenceLength == validSequence.length;
               }
            }
         }

         throw NotFoundException.getNotFoundInstance();
      }
   }

   private void findNextPair(BitArray row, List previousPairs, int forcedOffset) throws NotFoundException {
      int[] counters = this.getDecodeFinderCounters();
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      int width = row.getSize();
      int rowOffset;
      if (forcedOffset >= 0) {
         rowOffset = forcedOffset;
      } else if (previousPairs.isEmpty()) {
         rowOffset = 0;
      } else {
         ExpandedPair lastPair = (ExpandedPair)previousPairs.get(previousPairs.size() - 1);
         rowOffset = lastPair.getFinderPattern().getStartEnd()[1];
      }

      boolean searchingEvenPair = previousPairs.size() % 2 != 0;

      boolean isWhite;
      for(isWhite = false; rowOffset < width; ++rowOffset) {
         isWhite = !row.get(rowOffset);
         if (!isWhite) {
            break;
         }
      }

      int counterPosition = 0;
      int patternStart = rowOffset;

      for(int x = rowOffset; x < width; ++x) {
         if (row.get(x) ^ isWhite) {
            int var10002 = counters[counterPosition]++;
         } else {
            if (counterPosition == 3) {
               if (searchingEvenPair) {
                  reverseCounters(counters);
               }

               if (isFinderPattern(counters)) {
                  this.startEnd[0] = patternStart;
                  this.startEnd[1] = x;
                  return;
               }

               if (searchingEvenPair) {
                  reverseCounters(counters);
               }

               patternStart += counters[0] + counters[1];
               counters[0] = counters[2];
               counters[1] = counters[3];
               counters[2] = 0;
               counters[3] = 0;
               --counterPosition;
            } else {
               ++counterPosition;
            }

            counters[counterPosition] = 1;
            isWhite = !isWhite;
         }
      }

      throw NotFoundException.getNotFoundInstance();
   }

   private static void reverseCounters(int[] counters) {
      int length = counters.length;

      for(int i = 0; i < length / 2; ++i) {
         int tmp = counters[i];
         counters[i] = counters[length - i - 1];
         counters[length - i - 1] = tmp;
      }

   }

   private FinderPattern parseFoundFinderPattern(BitArray row, int rowNumber, boolean oddPattern) {
      int firstCounter;
      int start;
      int end;
      if (oddPattern) {
         int firstElementStart;
         for(firstElementStart = this.startEnd[0] - 1; firstElementStart >= 0 && !row.get(firstElementStart); --firstElementStart) {
         }

         ++firstElementStart;
         firstCounter = this.startEnd[0] - firstElementStart;
         start = firstElementStart;
         end = this.startEnd[1];
      } else {
         start = this.startEnd[0];
         end = row.getNextUnset(this.startEnd[1] + 1);
         firstCounter = end - this.startEnd[1];
      }

      int[] counters = this.getDecodeFinderCounters();
      System.arraycopy(counters, 0, counters, 1, counters.length - 1);
      counters[0] = firstCounter;

      int value;
      try {
         value = parseFinderValue(counters, FINDER_PATTERNS);
      } catch (NotFoundException var10) {
         return null;
      }

      return new FinderPattern(value, new int[]{start, end}, start, end, rowNumber);
   }

   DataCharacter decodeDataCharacter(BitArray row, FinderPattern pattern, boolean isOddPattern, boolean leftChar) throws NotFoundException {
      int[] counters = this.getDataCharacterCounters();
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      counters[4] = 0;
      counters[5] = 0;
      counters[6] = 0;
      counters[7] = 0;
      if (leftChar) {
         recordPatternInReverse(row, pattern.getStartEnd()[0], counters);
      } else {
         recordPattern(row, pattern.getStartEnd()[1] + 1, counters);
         int i = 0;

         for(int j = counters.length - 1; i < j; --j) {
            int temp = counters[i];
            counters[i] = counters[j];
            counters[j] = temp;
            ++i;
         }
      }

      int numModules = 17;
      float elementWidth = (float)count(counters) / (float)numModules;
      int[] oddCounts = this.getOddCounts();
      int[] evenCounts = this.getEvenCounts();
      float[] oddRoundingErrors = this.getOddRoundingErrors();
      float[] evenRoundingErrors = this.getEvenRoundingErrors();

      int weightRowNumber;
      int oddChecksumPortion;
      int evenChecksumPortion;
      for(weightRowNumber = 0; weightRowNumber < counters.length; ++weightRowNumber) {
         float value = 1.0F * (float)counters[weightRowNumber] / elementWidth;
         oddChecksumPortion = (int)(value + 0.5F);
         if (oddChecksumPortion < 1) {
            oddChecksumPortion = 1;
         } else if (oddChecksumPortion > 8) {
            oddChecksumPortion = 8;
         }

         evenChecksumPortion = weightRowNumber >> 1;
         if ((weightRowNumber & 1) == 0) {
            oddCounts[evenChecksumPortion] = oddChecksumPortion;
            oddRoundingErrors[evenChecksumPortion] = value - (float)oddChecksumPortion;
         } else {
            evenCounts[evenChecksumPortion] = oddChecksumPortion;
            evenRoundingErrors[evenChecksumPortion] = value - (float)oddChecksumPortion;
         }
      }

      this.adjustOddEvenCounts(numModules);
      weightRowNumber = 4 * pattern.getValue() + (isOddPattern ? 0 : 2) + (leftChar ? 0 : 1) - 1;
      int oddSum = 0;
      oddChecksumPortion = 0;

      int evenSum;
      for(evenChecksumPortion = oddCounts.length - 1; evenChecksumPortion >= 0; --evenChecksumPortion) {
         if (isNotA1left(pattern, isOddPattern, leftChar)) {
            evenSum = WEIGHTS[weightRowNumber][2 * evenChecksumPortion];
            oddChecksumPortion += oddCounts[evenChecksumPortion] * evenSum;
         }

         oddSum += oddCounts[evenChecksumPortion];
      }

      evenChecksumPortion = 0;
      evenSum = 0;

      int checksumPortion;
      int group;
      for(checksumPortion = evenCounts.length - 1; checksumPortion >= 0; --checksumPortion) {
         if (isNotA1left(pattern, isOddPattern, leftChar)) {
            group = WEIGHTS[weightRowNumber][2 * checksumPortion + 1];
            evenChecksumPortion += evenCounts[checksumPortion] * group;
         }

         evenSum += evenCounts[checksumPortion];
      }

      checksumPortion = oddChecksumPortion + evenChecksumPortion;
      if ((oddSum & 1) == 0 && oddSum <= 13 && oddSum >= 4) {
         group = (13 - oddSum) / 2;
         int oddWidest = SYMBOL_WIDEST[group];
         int evenWidest = 9 - oddWidest;
         int vOdd = RSSUtils.getRSSvalue(oddCounts, oddWidest, true);
         int vEven = RSSUtils.getRSSvalue(evenCounts, evenWidest, false);
         int tEven = EVEN_TOTAL_SUBSET[group];
         int gSum = GSUM[group];
         int value = vOdd * tEven + vEven + gSum;
         return new DataCharacter(value, checksumPortion);
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private static boolean isNotA1left(FinderPattern pattern, boolean isOddPattern, boolean leftChar) {
      return pattern.getValue() != 0 || !isOddPattern || !leftChar;
   }

   private void adjustOddEvenCounts(int numModules) throws NotFoundException {
      int oddSum = count(this.getOddCounts());
      int evenSum = count(this.getEvenCounts());
      int mismatch = oddSum + evenSum - numModules;
      boolean oddParityBad = (oddSum & 1) == 1;
      boolean evenParityBad = (evenSum & 1) == 0;
      boolean incrementOdd = false;
      boolean decrementOdd = false;
      if (oddSum > 13) {
         decrementOdd = true;
      } else if (oddSum < 4) {
         incrementOdd = true;
      }

      boolean incrementEven = false;
      boolean decrementEven = false;
      if (evenSum > 13) {
         decrementEven = true;
      } else if (evenSum < 4) {
         incrementEven = true;
      }

      if (mismatch == 1) {
         if (oddParityBad) {
            if (evenParityBad) {
               throw NotFoundException.getNotFoundInstance();
            }

            decrementOdd = true;
         } else {
            if (!evenParityBad) {
               throw NotFoundException.getNotFoundInstance();
            }

            decrementEven = true;
         }
      } else if (mismatch == -1) {
         if (oddParityBad) {
            if (evenParityBad) {
               throw NotFoundException.getNotFoundInstance();
            }

            incrementOdd = true;
         } else {
            if (!evenParityBad) {
               throw NotFoundException.getNotFoundInstance();
            }

            incrementEven = true;
         }
      } else {
         if (mismatch != 0) {
            throw NotFoundException.getNotFoundInstance();
         }

         if (oddParityBad) {
            if (!evenParityBad) {
               throw NotFoundException.getNotFoundInstance();
            }

            if (oddSum < evenSum) {
               incrementOdd = true;
               decrementEven = true;
            } else {
               decrementOdd = true;
               incrementEven = true;
            }
         } else if (evenParityBad) {
            throw NotFoundException.getNotFoundInstance();
         }
      }

      if (incrementOdd) {
         if (decrementOdd) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (decrementOdd) {
         decrement(this.getOddCounts(), this.getOddRoundingErrors());
      }

      if (incrementEven) {
         if (decrementEven) {
            throw NotFoundException.getNotFoundInstance();
         }

         increment(this.getEvenCounts(), this.getOddRoundingErrors());
      }

      if (decrementEven) {
         decrement(this.getEvenCounts(), this.getEvenRoundingErrors());
      }

   }

   static {
      LONGEST_SEQUENCE_SIZE = FINDER_PATTERN_SEQUENCES[FINDER_PATTERN_SEQUENCES.length - 1].length;
   }
}
