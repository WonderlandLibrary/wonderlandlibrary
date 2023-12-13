package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;

final class GeneralAppIdDecoder {
   private final BitArray information;
   private final CurrentParsingState current = new CurrentParsingState();
   private final StringBuilder buffer = new StringBuilder();

   GeneralAppIdDecoder(BitArray information) {
      this.information = information;
   }

   String decodeAllCodes(StringBuilder buff, int initialPosition) throws NotFoundException {
      int currentPosition = initialPosition;
      String remaining = null;

      while(true) {
         DecodedInformation info = this.decodeGeneralPurposeField(currentPosition, remaining);
         String parsedFields = FieldParser.parseFieldsInGeneralPurpose(info.getNewString());
         if (parsedFields != null) {
            buff.append(parsedFields);
         }

         if (info.isRemaining()) {
            remaining = String.valueOf(info.getRemainingValue());
         } else {
            remaining = null;
         }

         if (currentPosition == info.getNewPosition()) {
            return buff.toString();
         }

         currentPosition = info.getNewPosition();
      }
   }

   private boolean isStillNumeric(int pos) {
      if (pos + 7 > this.information.getSize()) {
         return pos + 4 <= this.information.getSize();
      } else {
         for(int i = pos; i < pos + 3; ++i) {
            if (this.information.get(i)) {
               return true;
            }
         }

         return this.information.get(pos + 3);
      }
   }

   private DecodedNumeric decodeNumeric(int pos) {
      int numeric;
      if (pos + 7 > this.information.getSize()) {
         numeric = this.extractNumericValueFromBitArray(pos, 4);
         return numeric == 0 ? new DecodedNumeric(this.information.getSize(), 10, 10) : new DecodedNumeric(this.information.getSize(), numeric - 1, 10);
      } else {
         numeric = this.extractNumericValueFromBitArray(pos, 7);
         int digit1 = (numeric - 8) / 11;
         int digit2 = (numeric - 8) % 11;
         return new DecodedNumeric(pos + 7, digit1, digit2);
      }
   }

   int extractNumericValueFromBitArray(int pos, int bits) {
      return extractNumericValueFromBitArray(this.information, pos, bits);
   }

   static int extractNumericValueFromBitArray(BitArray information, int pos, int bits) {
      if (bits > 32) {
         throw new IllegalArgumentException("extractNumberValueFromBitArray can't handle more than 32 bits");
      } else {
         int value = 0;

         for(int i = 0; i < bits; ++i) {
            if (information.get(pos + i)) {
               value |= 1 << bits - i - 1;
            }
         }

         return value;
      }
   }

   DecodedInformation decodeGeneralPurposeField(int pos, String remaining) {
      this.buffer.setLength(0);
      if (remaining != null) {
         this.buffer.append(remaining);
      }

      this.current.setPosition(pos);
      DecodedInformation lastDecoded = this.parseBlocks();
      return lastDecoded != null && lastDecoded.isRemaining() ? new DecodedInformation(this.current.getPosition(), this.buffer.toString(), lastDecoded.getRemainingValue()) : new DecodedInformation(this.current.getPosition(), this.buffer.toString());
   }

   private DecodedInformation parseBlocks() {
      boolean isFinished;
      BlockParsedResult result;
      boolean positionChanged;
      do {
         int initialPosition = this.current.getPosition();
         if (this.current.isAlpha()) {
            result = this.parseAlphaBlock();
            isFinished = result.isFinished();
         } else if (this.current.isIsoIec646()) {
            result = this.parseIsoIec646Block();
            isFinished = result.isFinished();
         } else {
            result = this.parseNumericBlock();
            isFinished = result.isFinished();
         }

         positionChanged = initialPosition != this.current.getPosition();
      } while((positionChanged || isFinished) && !isFinished);

      return result.getDecodedInformation();
   }

   private BlockParsedResult parseNumericBlock() {
      while(this.isStillNumeric(this.current.getPosition())) {
         DecodedNumeric numeric = this.decodeNumeric(this.current.getPosition());
         this.current.setPosition(numeric.getNewPosition());
         DecodedInformation information;
         if (numeric.isFirstDigitFNC1()) {
            if (numeric.isSecondDigitFNC1()) {
               information = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
            } else {
               information = new DecodedInformation(this.current.getPosition(), this.buffer.toString(), numeric.getSecondDigit());
            }

            return new BlockParsedResult(information, true);
         }

         this.buffer.append(numeric.getFirstDigit());
         if (numeric.isSecondDigitFNC1()) {
            information = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
            return new BlockParsedResult(information, true);
         }

         this.buffer.append(numeric.getSecondDigit());
      }

      if (this.isNumericToAlphaNumericLatch(this.current.getPosition())) {
         this.current.setAlpha();
         this.current.incrementPosition(4);
      }

      return new BlockParsedResult(false);
   }

   private BlockParsedResult parseIsoIec646Block() {
      while(this.isStillIsoIec646(this.current.getPosition())) {
         DecodedChar iso = this.decodeIsoIec646(this.current.getPosition());
         this.current.setPosition(iso.getNewPosition());
         if (iso.isFNC1()) {
            DecodedInformation information = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
            return new BlockParsedResult(information, true);
         }

         this.buffer.append(iso.getValue());
      }

      if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
         this.current.incrementPosition(3);
         this.current.setNumeric();
      } else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
         if (this.current.getPosition() + 5 < this.information.getSize()) {
            this.current.incrementPosition(5);
         } else {
            this.current.setPosition(this.information.getSize());
         }

         this.current.setAlpha();
      }

      return new BlockParsedResult(false);
   }

   private BlockParsedResult parseAlphaBlock() {
      while(this.isStillAlpha(this.current.getPosition())) {
         DecodedChar alpha = this.decodeAlphanumeric(this.current.getPosition());
         this.current.setPosition(alpha.getNewPosition());
         if (alpha.isFNC1()) {
            DecodedInformation information = new DecodedInformation(this.current.getPosition(), this.buffer.toString());
            return new BlockParsedResult(information, true);
         }

         this.buffer.append(alpha.getValue());
      }

      if (this.isAlphaOr646ToNumericLatch(this.current.getPosition())) {
         this.current.incrementPosition(3);
         this.current.setNumeric();
      } else if (this.isAlphaTo646ToAlphaLatch(this.current.getPosition())) {
         if (this.current.getPosition() + 5 < this.information.getSize()) {
            this.current.incrementPosition(5);
         } else {
            this.current.setPosition(this.information.getSize());
         }

         this.current.setIsoIec646();
      }

      return new BlockParsedResult(false);
   }

   private boolean isStillIsoIec646(int pos) {
      if (pos + 5 > this.information.getSize()) {
         return false;
      } else {
         int fiveBitValue = this.extractNumericValueFromBitArray(pos, 5);
         if (fiveBitValue >= 5 && fiveBitValue < 16) {
            return true;
         } else if (pos + 7 > this.information.getSize()) {
            return false;
         } else {
            int sevenBitValue = this.extractNumericValueFromBitArray(pos, 7);
            if (sevenBitValue >= 64 && sevenBitValue < 116) {
               return true;
            } else if (pos + 8 > this.information.getSize()) {
               return false;
            } else {
               int eightBitValue = this.extractNumericValueFromBitArray(pos, 8);
               return eightBitValue >= 232 && eightBitValue < 253;
            }
         }
      }
   }

   private DecodedChar decodeIsoIec646(int pos) {
      int fiveBitValue = this.extractNumericValueFromBitArray(pos, 5);
      if (fiveBitValue == 15) {
         return new DecodedChar(pos + 5, '$');
      } else if (fiveBitValue >= 5 && fiveBitValue < 15) {
         return new DecodedChar(pos + 5, (char)(48 + fiveBitValue - 5));
      } else {
         int sevenBitValue = this.extractNumericValueFromBitArray(pos, 7);
         if (sevenBitValue >= 64 && sevenBitValue < 90) {
            return new DecodedChar(pos + 7, (char)(sevenBitValue + 1));
         } else if (sevenBitValue >= 90 && sevenBitValue < 116) {
            return new DecodedChar(pos + 7, (char)(sevenBitValue + 7));
         } else {
            int eightBitValue = this.extractNumericValueFromBitArray(pos, 8);
            char c;
            switch (eightBitValue) {
               case 232:
                  c = '!';
                  break;
               case 233:
                  c = '"';
                  break;
               case 234:
                  c = '%';
                  break;
               case 235:
                  c = '&';
                  break;
               case 236:
                  c = '\'';
                  break;
               case 237:
                  c = '(';
                  break;
               case 238:
                  c = ')';
                  break;
               case 239:
                  c = '*';
                  break;
               case 240:
                  c = '+';
                  break;
               case 241:
                  c = ',';
                  break;
               case 242:
                  c = '-';
                  break;
               case 243:
                  c = '.';
                  break;
               case 244:
                  c = '/';
                  break;
               case 245:
                  c = ':';
                  break;
               case 246:
                  c = ';';
                  break;
               case 247:
                  c = '<';
                  break;
               case 248:
                  c = '=';
                  break;
               case 249:
                  c = '>';
                  break;
               case 250:
                  c = '?';
                  break;
               case 251:
                  c = '_';
                  break;
               case 252:
                  c = ' ';
                  break;
               default:
                  throw new IllegalArgumentException("Decoding invalid ISO/IEC 646 value: " + eightBitValue);
            }

            return new DecodedChar(pos + 8, c);
         }
      }
   }

   private boolean isStillAlpha(int pos) {
      if (pos + 5 > this.information.getSize()) {
         return false;
      } else {
         int fiveBitValue = this.extractNumericValueFromBitArray(pos, 5);
         if (fiveBitValue >= 5 && fiveBitValue < 16) {
            return true;
         } else if (pos + 6 > this.information.getSize()) {
            return false;
         } else {
            int sixBitValue = this.extractNumericValueFromBitArray(pos, 6);
            return sixBitValue >= 16 && sixBitValue < 63;
         }
      }
   }

   private DecodedChar decodeAlphanumeric(int pos) {
      int fiveBitValue = this.extractNumericValueFromBitArray(pos, 5);
      if (fiveBitValue == 15) {
         return new DecodedChar(pos + 5, '$');
      } else if (fiveBitValue >= 5 && fiveBitValue < 15) {
         return new DecodedChar(pos + 5, (char)(48 + fiveBitValue - 5));
      } else {
         int sixBitValue = this.extractNumericValueFromBitArray(pos, 6);
         if (sixBitValue >= 32 && sixBitValue < 58) {
            return new DecodedChar(pos + 6, (char)(sixBitValue + 33));
         } else {
            char c;
            switch (sixBitValue) {
               case 58:
                  c = '*';
                  break;
               case 59:
                  c = ',';
                  break;
               case 60:
                  c = '-';
                  break;
               case 61:
                  c = '.';
                  break;
               case 62:
                  c = '/';
                  break;
               default:
                  throw new IllegalStateException("Decoding invalid alphanumeric value: " + sixBitValue);
            }

            return new DecodedChar(pos + 6, c);
         }
      }
   }

   private boolean isAlphaTo646ToAlphaLatch(int pos) {
      if (pos + 1 > this.information.getSize()) {
         return false;
      } else {
         for(int i = 0; i < 5 && i + pos < this.information.getSize(); ++i) {
            if (i == 2) {
               if (!this.information.get(pos + 2)) {
                  return false;
               }
            } else if (this.information.get(pos + i)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean isAlphaOr646ToNumericLatch(int pos) {
      if (pos + 3 > this.information.getSize()) {
         return false;
      } else {
         for(int i = pos; i < pos + 3; ++i) {
            if (this.information.get(i)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean isNumericToAlphaNumericLatch(int pos) {
      if (pos + 1 > this.information.getSize()) {
         return false;
      } else {
         for(int i = 0; i < 4 && i + pos < this.information.getSize(); ++i) {
            if (this.information.get(pos + i)) {
               return false;
            }
         }

         return true;
      }
   }
}
