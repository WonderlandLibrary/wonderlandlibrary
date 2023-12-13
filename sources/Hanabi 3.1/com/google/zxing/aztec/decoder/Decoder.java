package com.google.zxing.aztec.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.aztec.AztecDetectorResult;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.DecoderResult;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonDecoder;
import com.google.zxing.common.reedsolomon.ReedSolomonException;
import java.util.List;

public final class Decoder {
   private static final int[] NB_BITS_COMPACT = new int[]{0, 104, 240, 408, 608};
   private static final int[] NB_BITS = new int[]{0, 128, 288, 480, 704, 960, 1248, 1568, 1920, 2304, 2720, 3168, 3648, 4160, 4704, 5280, 5888, 6528, 7200, 7904, 8640, 9408, 10208, 11040, 11904, 12800, 13728, 14688, 15680, 16704, 17760, 18848, 19968};
   private static final int[] NB_DATABLOCK_COMPACT = new int[]{0, 17, 40, 51, 76};
   private static final int[] NB_DATABLOCK = new int[]{0, 21, 48, 60, 88, 120, 156, 196, 240, 230, 272, 316, 364, 416, 470, 528, 588, 652, 720, 790, 864, 940, 1020, 920, 992, 1066, 1144, 1224, 1306, 1392, 1480, 1570, 1664};
   private static final String[] UPPER_TABLE = new String[]{"CTRL_PS", " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "CTRL_LL", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
   private static final String[] LOWER_TABLE = new String[]{"CTRL_PS", " ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "CTRL_US", "CTRL_ML", "CTRL_DL", "CTRL_BS"};
   private static final String[] MIXED_TABLE = new String[]{"CTRL_PS", " ", "\u0001", "\u0002", "\u0003", "\u0004", "\u0005", "\u0006", "\u0007", "\b", "\t", "\n", "\u000b", "\f", "\r", "\u001b", "\u001c", "\u001d", "\u001e", "\u001f", "@", "\\", "^", "_", "`", "|", "~", "\u007f", "CTRL_LL", "CTRL_UL", "CTRL_PL", "CTRL_BS"};
   private static final String[] PUNCT_TABLE = new String[]{"", "\r", "\r\n", ". ", ", ", ": ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/", ":", ";", "<", "=", ">", "?", "[", "]", "{", "}", "CTRL_UL"};
   private static final String[] DIGIT_TABLE = new String[]{"CTRL_PS", " ", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ",", ".", "CTRL_UL", "CTRL_US"};
   private int numCodewords;
   private int codewordSize;
   private AztecDetectorResult ddata;
   private int invertedBitCount;

   public DecoderResult decode(AztecDetectorResult detectorResult) throws FormatException {
      this.ddata = detectorResult;
      BitMatrix matrix = detectorResult.getBits();
      if (!this.ddata.isCompact()) {
         matrix = removeDashedLines(this.ddata.getBits());
      }

      boolean[] rawbits = this.extractBits(matrix);
      boolean[] correctedBits = this.correctBits(rawbits);
      String result = this.getEncodedData(correctedBits);
      return new DecoderResult((byte[])null, result, (List)null, (String)null);
   }

   private String getEncodedData(boolean[] correctedBits) throws FormatException {
      int endIndex = this.codewordSize * this.ddata.getNbDatablocks() - this.invertedBitCount;
      if (endIndex > correctedBits.length) {
         throw FormatException.getFormatInstance();
      } else {
         Table lastTable = Decoder.Table.UPPER;
         Table table = Decoder.Table.UPPER;
         int startIndex = 0;
         StringBuilder result = new StringBuilder(20);
         boolean end = false;
         boolean shift = false;
         boolean switchShift = false;
         boolean binaryShift = false;

         while(!end) {
            if (shift) {
               switchShift = true;
            } else {
               lastTable = table;
            }

            int code;
            if (binaryShift) {
               if (endIndex - startIndex < 5) {
                  break;
               }

               int length = readCode(correctedBits, startIndex, 5);
               startIndex += 5;
               if (length == 0) {
                  if (endIndex - startIndex < 11) {
                     break;
                  }

                  length = readCode(correctedBits, startIndex, 11) + 31;
                  startIndex += 11;
               }

               for(int charCount = 0; charCount < length; ++charCount) {
                  if (endIndex - startIndex < 8) {
                     end = true;
                     break;
                  }

                  code = readCode(correctedBits, startIndex, 8);
                  result.append((char)code);
                  startIndex += 8;
               }

               binaryShift = false;
            } else if (table == Decoder.Table.BINARY) {
               if (endIndex - startIndex < 8) {
                  break;
               }

               code = readCode(correctedBits, startIndex, 8);
               startIndex += 8;
               result.append((char)code);
            } else {
               int size = 5;
               if (table == Decoder.Table.DIGIT) {
                  size = 4;
               }

               if (endIndex - startIndex < size) {
                  break;
               }

               code = readCode(correctedBits, startIndex, size);
               startIndex += size;
               String str = getCharacter(table, code);
               if (str.startsWith("CTRL_")) {
                  table = getTable(str.charAt(5));
                  if (str.charAt(6) == 'S') {
                     shift = true;
                     if (str.charAt(5) == 'B') {
                        binaryShift = true;
                     }
                  }
               } else {
                  result.append(str);
               }
            }

            if (switchShift) {
               table = lastTable;
               shift = false;
               switchShift = false;
            }
         }

         return result.toString();
      }
   }

   private static Table getTable(char t) {
      switch (t) {
         case 'B':
            return Decoder.Table.BINARY;
         case 'C':
         case 'E':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'N':
         case 'O':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         default:
            return Decoder.Table.UPPER;
         case 'D':
            return Decoder.Table.DIGIT;
         case 'L':
            return Decoder.Table.LOWER;
         case 'M':
            return Decoder.Table.MIXED;
         case 'P':
            return Decoder.Table.PUNCT;
      }
   }

   private static String getCharacter(Table table, int code) {
      switch (table) {
         case UPPER:
            return UPPER_TABLE[code];
         case LOWER:
            return LOWER_TABLE[code];
         case MIXED:
            return MIXED_TABLE[code];
         case PUNCT:
            return PUNCT_TABLE[code];
         case DIGIT:
            return DIGIT_TABLE[code];
         default:
            return "";
      }
   }

   private boolean[] correctBits(boolean[] rawbits) throws FormatException {
      GenericGF gf;
      if (this.ddata.getNbLayers() <= 2) {
         this.codewordSize = 6;
         gf = GenericGF.AZTEC_DATA_6;
      } else if (this.ddata.getNbLayers() <= 8) {
         this.codewordSize = 8;
         gf = GenericGF.AZTEC_DATA_8;
      } else if (this.ddata.getNbLayers() <= 22) {
         this.codewordSize = 10;
         gf = GenericGF.AZTEC_DATA_10;
      } else {
         this.codewordSize = 12;
         gf = GenericGF.AZTEC_DATA_12;
      }

      int numDataCodewords = this.ddata.getNbDatablocks();
      int numECCodewords;
      int offset;
      if (this.ddata.isCompact()) {
         offset = NB_BITS_COMPACT[this.ddata.getNbLayers()] - this.numCodewords * this.codewordSize;
         numECCodewords = NB_DATABLOCK_COMPACT[this.ddata.getNbLayers()] - numDataCodewords;
      } else {
         offset = NB_BITS[this.ddata.getNbLayers()] - this.numCodewords * this.codewordSize;
         numECCodewords = NB_DATABLOCK[this.ddata.getNbLayers()] - numDataCodewords;
      }

      int[] dataWords = new int[this.numCodewords];

      int i;
      for(int i = 0; i < this.numCodewords; ++i) {
         i = 1;

         for(int j = 1; j <= this.codewordSize; ++j) {
            if (rawbits[this.codewordSize * i + this.codewordSize - j + offset]) {
               dataWords[i] += i;
            }

            i <<= 1;
         }
      }

      try {
         ReedSolomonDecoder rsDecoder = new ReedSolomonDecoder(gf);
         rsDecoder.decode(dataWords, numECCodewords);
      } catch (ReedSolomonException var14) {
         throw FormatException.getFormatInstance();
      }

      offset = 0;
      this.invertedBitCount = 0;
      boolean[] correctedBits = new boolean[numDataCodewords * this.codewordSize];

      for(i = 0; i < numDataCodewords; ++i) {
         boolean seriesColor = false;
         int seriesCount = 0;
         int flag = 1 << this.codewordSize - 1;

         for(int j = 0; j < this.codewordSize; ++j) {
            boolean color = (dataWords[i] & flag) == flag;
            if (seriesCount == this.codewordSize - 1) {
               if (color == seriesColor) {
                  throw FormatException.getFormatInstance();
               }

               seriesColor = false;
               seriesCount = 0;
               ++offset;
               ++this.invertedBitCount;
            } else {
               if (seriesColor == color) {
                  ++seriesCount;
               } else {
                  seriesCount = 1;
                  seriesColor = color;
               }

               correctedBits[i * this.codewordSize + j - offset] = color;
            }

            flag >>>= 1;
         }
      }

      return correctedBits;
   }

   private boolean[] extractBits(BitMatrix matrix) throws FormatException {
      boolean[] rawbits;
      if (this.ddata.isCompact()) {
         if (this.ddata.getNbLayers() > NB_BITS_COMPACT.length) {
            throw FormatException.getFormatInstance();
         }

         rawbits = new boolean[NB_BITS_COMPACT[this.ddata.getNbLayers()]];
         this.numCodewords = NB_DATABLOCK_COMPACT[this.ddata.getNbLayers()];
      } else {
         if (this.ddata.getNbLayers() > NB_BITS.length) {
            throw FormatException.getFormatInstance();
         }

         rawbits = new boolean[NB_BITS[this.ddata.getNbLayers()]];
         this.numCodewords = NB_DATABLOCK[this.ddata.getNbLayers()];
      }

      int layer = this.ddata.getNbLayers();
      int size = matrix.getHeight();
      int rawbitsOffset = 0;

      for(int matrixOffset = 0; layer != 0; size -= 4) {
         int flip = 0;

         int i;
         for(i = 0; i < 2 * size - 4; ++i) {
            rawbits[rawbitsOffset + i] = matrix.get(matrixOffset + flip, matrixOffset + i / 2);
            rawbits[rawbitsOffset + 2 * size - 4 + i] = matrix.get(matrixOffset + i / 2, matrixOffset + size - 1 - flip);
            flip = (flip + 1) % 2;
         }

         flip = 0;

         for(i = 2 * size + 1; i > 5; --i) {
            rawbits[rawbitsOffset + 4 * size - 8 + (2 * size - i) + 1] = matrix.get(matrixOffset + size - 1 - flip, matrixOffset + i / 2 - 1);
            rawbits[rawbitsOffset + 6 * size - 12 + (2 * size - i) + 1] = matrix.get(matrixOffset + i / 2 - 1, matrixOffset + flip);
            flip = (flip + 1) % 2;
         }

         matrixOffset += 2;
         rawbitsOffset += 8 * size - 16;
         --layer;
      }

      return rawbits;
   }

   private static BitMatrix removeDashedLines(BitMatrix matrix) {
      int nbDashed = 1 + 2 * ((matrix.getWidth() - 1) / 2 / 16);
      BitMatrix newMatrix = new BitMatrix(matrix.getWidth() - nbDashed, matrix.getHeight() - nbDashed);
      int nx = 0;

      for(int x = 0; x < matrix.getWidth(); ++x) {
         if ((matrix.getWidth() / 2 - x) % 16 != 0) {
            int ny = 0;

            for(int y = 0; y < matrix.getHeight(); ++y) {
               if ((matrix.getWidth() / 2 - y) % 16 != 0) {
                  if (matrix.get(x, y)) {
                     newMatrix.set(nx, ny);
                  }

                  ++ny;
               }
            }

            ++nx;
         }
      }

      return newMatrix;
   }

   private static int readCode(boolean[] rawbits, int startIndex, int length) {
      int res = 0;

      for(int i = startIndex; i < startIndex + length; ++i) {
         res <<= 1;
         if (rawbits[i]) {
            ++res;
         }
      }

      return res;
   }

   private static enum Table {
      UPPER,
      LOWER,
      MIXED,
      DIGIT,
      PUNCT,
      BINARY;
   }
}
