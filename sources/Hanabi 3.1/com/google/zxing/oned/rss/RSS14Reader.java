package com.google.zxing.oned.rss;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.BitArray;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class RSS14Reader extends AbstractRSSReader {
   private static final int[] OUTSIDE_EVEN_TOTAL_SUBSET = new int[]{1, 10, 34, 70, 126};
   private static final int[] INSIDE_ODD_TOTAL_SUBSET = new int[]{4, 20, 48, 81};
   private static final int[] OUTSIDE_GSUM = new int[]{0, 161, 961, 2015, 2715};
   private static final int[] INSIDE_GSUM = new int[]{0, 336, 1036, 1516};
   private static final int[] OUTSIDE_ODD_WIDEST = new int[]{8, 6, 4, 3, 1};
   private static final int[] INSIDE_ODD_WIDEST = new int[]{2, 4, 6, 8};
   private static final int[][] FINDER_PATTERNS = new int[][]{{3, 8, 2, 1}, {3, 5, 5, 1}, {3, 3, 7, 1}, {3, 1, 9, 1}, {2, 7, 4, 1}, {2, 5, 6, 1}, {2, 3, 8, 1}, {1, 5, 7, 1}, {1, 3, 9, 1}};
   private final List possibleLeftPairs = new ArrayList();
   private final List possibleRightPairs = new ArrayList();

   public Result decodeRow(int rowNumber, BitArray row, Map hints) throws NotFoundException {
      Pair leftPair = this.decodePair(row, false, rowNumber, hints);
      addOrTally(this.possibleLeftPairs, leftPair);
      row.reverse();
      Pair rightPair = this.decodePair(row, true, rowNumber, hints);
      addOrTally(this.possibleRightPairs, rightPair);
      row.reverse();
      Iterator i$ = this.possibleLeftPairs.iterator();

      while(true) {
         Pair left;
         do {
            if (!i$.hasNext()) {
               throw NotFoundException.getNotFoundInstance();
            }

            left = (Pair)i$.next();
         } while(left.getCount() <= 1);

         Iterator i$ = this.possibleRightPairs.iterator();

         while(i$.hasNext()) {
            Pair right = (Pair)i$.next();
            if (right.getCount() > 1 && checkChecksum(left, right)) {
               return constructResult(left, right);
            }
         }
      }
   }

   private static void addOrTally(Collection possiblePairs, Pair pair) {
      if (pair != null) {
         boolean found = false;
         Iterator i$ = possiblePairs.iterator();

         while(i$.hasNext()) {
            Pair other = (Pair)i$.next();
            if (other.getValue() == pair.getValue()) {
               other.incrementCount();
               found = true;
               break;
            }
         }

         if (!found) {
            possiblePairs.add(pair);
         }

      }
   }

   public void reset() {
      this.possibleLeftPairs.clear();
      this.possibleRightPairs.clear();
   }

   private static Result constructResult(Pair leftPair, Pair rightPair) {
      long symbolValue = 4537077L * (long)leftPair.getValue() + (long)rightPair.getValue();
      String text = String.valueOf(symbolValue);
      StringBuilder buffer = new StringBuilder(14);

      int checkDigit;
      for(checkDigit = 13 - text.length(); checkDigit > 0; --checkDigit) {
         buffer.append('0');
      }

      buffer.append(text);
      checkDigit = 0;

      for(int i = 0; i < 13; ++i) {
         int digit = buffer.charAt(i) - 48;
         checkDigit += (i & 1) == 0 ? 3 * digit : digit;
      }

      checkDigit = 10 - checkDigit % 10;
      if (checkDigit == 10) {
         checkDigit = 0;
      }

      buffer.append(checkDigit);
      ResultPoint[] leftPoints = leftPair.getFinderPattern().getResultPoints();
      ResultPoint[] rightPoints = rightPair.getFinderPattern().getResultPoints();
      return new Result(String.valueOf(buffer.toString()), (byte[])null, new ResultPoint[]{leftPoints[0], leftPoints[1], rightPoints[0], rightPoints[1]}, BarcodeFormat.RSS_14);
   }

   private static boolean checkChecksum(Pair leftPair, Pair rightPair) {
      int leftFPValue = leftPair.getFinderPattern().getValue();
      int rightFPValue = rightPair.getFinderPattern().getValue();
      if ((leftFPValue != 0 || rightFPValue != 8) && leftFPValue == 8 && rightFPValue == 0) {
      }

      int checkValue = (leftPair.getChecksumPortion() + 16 * rightPair.getChecksumPortion()) % 79;
      int targetCheckValue = 9 * leftPair.getFinderPattern().getValue() + rightPair.getFinderPattern().getValue();
      if (targetCheckValue > 72) {
         --targetCheckValue;
      }

      if (targetCheckValue > 8) {
         --targetCheckValue;
      }

      return checkValue == targetCheckValue;
   }

   private Pair decodePair(BitArray row, boolean right, int rowNumber, Map hints) {
      try {
         int[] startEnd = this.findFinderPattern(row, 0, right);
         FinderPattern pattern = this.parseFoundFinderPattern(row, rowNumber, right, startEnd);
         ResultPointCallback resultPointCallback = hints == null ? null : (ResultPointCallback)hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
         if (resultPointCallback != null) {
            float center = (float)(startEnd[0] + startEnd[1]) / 2.0F;
            if (right) {
               center = (float)(row.getSize() - 1) - center;
            }

            resultPointCallback.foundPossibleResultPoint(new ResultPoint(center, (float)rowNumber));
         }

         DataCharacter outside = this.decodeDataCharacter(row, pattern, true);
         DataCharacter inside = this.decodeDataCharacter(row, pattern, false);
         return new Pair(1597 * outside.getValue() + inside.getValue(), outside.getChecksumPortion() + 4 * inside.getChecksumPortion(), pattern);
      } catch (NotFoundException var10) {
         return null;
      }
   }

   private DataCharacter decodeDataCharacter(BitArray row, FinderPattern pattern, boolean outsideChar) throws NotFoundException {
      int[] counters = this.getDataCharacterCounters();
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      counters[4] = 0;
      counters[5] = 0;
      counters[6] = 0;
      counters[7] = 0;
      int numModules;
      if (outsideChar) {
         recordPatternInReverse(row, pattern.getStartEnd()[0], counters);
      } else {
         recordPattern(row, pattern.getStartEnd()[1] + 1, counters);
         numModules = 0;

         for(int j = counters.length - 1; numModules < j; --j) {
            int temp = counters[numModules];
            counters[numModules] = counters[j];
            counters[j] = temp;
            ++numModules;
         }
      }

      numModules = outsideChar ? 16 : 15;
      float elementWidth = (float)count(counters) / (float)numModules;
      int[] oddCounts = this.getOddCounts();
      int[] evenCounts = this.getEvenCounts();
      float[] oddRoundingErrors = this.getOddRoundingErrors();
      float[] evenRoundingErrors = this.getEvenRoundingErrors();

      int oddSum;
      int evenChecksumPortion;
      int evenSum;
      for(oddSum = 0; oddSum < counters.length; ++oddSum) {
         float value = (float)counters[oddSum] / elementWidth;
         evenChecksumPortion = (int)(value + 0.5F);
         if (evenChecksumPortion < 1) {
            evenChecksumPortion = 1;
         } else if (evenChecksumPortion > 8) {
            evenChecksumPortion = 8;
         }

         evenSum = oddSum >> 1;
         if ((oddSum & 1) == 0) {
            oddCounts[evenSum] = evenChecksumPortion;
            oddRoundingErrors[evenSum] = value - (float)evenChecksumPortion;
         } else {
            evenCounts[evenSum] = evenChecksumPortion;
            evenRoundingErrors[evenSum] = value - (float)evenChecksumPortion;
         }
      }

      this.adjustOddEvenCounts(outsideChar, numModules);
      oddSum = 0;
      int oddChecksumPortion = 0;

      for(evenChecksumPortion = oddCounts.length - 1; evenChecksumPortion >= 0; --evenChecksumPortion) {
         oddChecksumPortion *= 9;
         oddChecksumPortion += oddCounts[evenChecksumPortion];
         oddSum += oddCounts[evenChecksumPortion];
      }

      evenChecksumPortion = 0;
      evenSum = 0;

      int checksumPortion;
      for(checksumPortion = evenCounts.length - 1; checksumPortion >= 0; --checksumPortion) {
         evenChecksumPortion *= 9;
         evenChecksumPortion += evenCounts[checksumPortion];
         evenSum += evenCounts[checksumPortion];
      }

      checksumPortion = oddChecksumPortion + 3 * evenChecksumPortion;
      int group;
      int oddWidest;
      int evenWidest;
      int vOdd;
      int vEven;
      int tOdd;
      int gSum;
      if (outsideChar) {
         if ((oddSum & 1) == 0 && oddSum <= 12 && oddSum >= 4) {
            group = (12 - oddSum) / 2;
            oddWidest = OUTSIDE_ODD_WIDEST[group];
            evenWidest = 9 - oddWidest;
            vOdd = RSSUtils.getRSSvalue(oddCounts, oddWidest, false);
            vEven = RSSUtils.getRSSvalue(evenCounts, evenWidest, true);
            tOdd = OUTSIDE_EVEN_TOTAL_SUBSET[group];
            gSum = OUTSIDE_GSUM[group];
            return new DataCharacter(vOdd * tOdd + vEven + gSum, checksumPortion);
         } else {
            throw NotFoundException.getNotFoundInstance();
         }
      } else if ((evenSum & 1) == 0 && evenSum <= 10 && evenSum >= 4) {
         group = (10 - evenSum) / 2;
         oddWidest = INSIDE_ODD_WIDEST[group];
         evenWidest = 9 - oddWidest;
         vOdd = RSSUtils.getRSSvalue(oddCounts, oddWidest, true);
         vEven = RSSUtils.getRSSvalue(evenCounts, evenWidest, false);
         tOdd = INSIDE_ODD_TOTAL_SUBSET[group];
         gSum = INSIDE_GSUM[group];
         return new DataCharacter(vEven * tOdd + vOdd + gSum, checksumPortion);
      } else {
         throw NotFoundException.getNotFoundInstance();
      }
   }

   private int[] findFinderPattern(BitArray row, int rowOffset, boolean rightFinderPattern) throws NotFoundException {
      int[] counters = this.getDecodeFinderCounters();
      counters[0] = 0;
      counters[1] = 0;
      counters[2] = 0;
      counters[3] = 0;
      int width = row.getSize();

      boolean isWhite;
      for(isWhite = false; rowOffset < width; ++rowOffset) {
         isWhite = !row.get(rowOffset);
         if (rightFinderPattern == isWhite) {
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
               if (isFinderPattern(counters)) {
                  return new int[]{patternStart, x};
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

   private FinderPattern parseFoundFinderPattern(BitArray row, int rowNumber, boolean right, int[] startEnd) throws NotFoundException {
      boolean firstIsBlack = row.get(startEnd[0]);

      int firstElementStart;
      for(firstElementStart = startEnd[0] - 1; firstElementStart >= 0 && firstIsBlack ^ row.get(firstElementStart); --firstElementStart) {
      }

      ++firstElementStart;
      int firstCounter = startEnd[0] - firstElementStart;
      int[] counters = this.getDecodeFinderCounters();
      System.arraycopy(counters, 0, counters, 1, counters.length - 1);
      counters[0] = firstCounter;
      int value = parseFinderValue(counters, FINDER_PATTERNS);
      int start = firstElementStart;
      int end = startEnd[1];
      if (right) {
         start = row.getSize() - 1 - firstElementStart;
         end = row.getSize() - 1 - end;
      }

      return new FinderPattern(value, new int[]{firstElementStart, startEnd[1]}, start, end, rowNumber);
   }

   private void adjustOddEvenCounts(boolean outsideChar, int numModules) throws NotFoundException {
      int oddSum = count(this.getOddCounts());
      int evenSum = count(this.getEvenCounts());
      int mismatch = oddSum + evenSum - numModules;
      boolean oddParityBad = (oddSum & 1) == (outsideChar ? 1 : 0);
      boolean evenParityBad = (evenSum & 1) == 1;
      boolean incrementOdd = false;
      boolean decrementOdd = false;
      boolean incrementEven = false;
      boolean decrementEven = false;
      if (outsideChar) {
         if (oddSum > 12) {
            decrementOdd = true;
         } else if (oddSum < 4) {
            incrementOdd = true;
         }

         if (evenSum > 12) {
            decrementEven = true;
         } else if (evenSum < 4) {
            incrementEven = true;
         }
      } else {
         if (oddSum > 11) {
            decrementOdd = true;
         } else if (oddSum < 5) {
            incrementOdd = true;
         }

         if (evenSum > 10) {
            decrementEven = true;
         } else if (evenSum < 4) {
            incrementEven = true;
         }
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
}
