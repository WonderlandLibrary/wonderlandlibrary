package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitSource;
import com.google.zxing.common.DecoderResult;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class DecodedBitStreamParser {
   private static final char[] C40_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
   private static final char[] C40_SHIFT2_SET_CHARS = new char[]{'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_'};
   private static final char[] TEXT_BASIC_SET_CHARS = new char[]{'*', '*', '*', ' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
   private static final char[] TEXT_SHIFT3_SET_CHARS = new char[]{'\'', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '{', '|', '}', '~', '\u007f'};

   private DecodedBitStreamParser() {
   }

   static DecoderResult decode(byte[] bytes) throws FormatException {
      BitSource bits = new BitSource(bytes);
      StringBuilder result = new StringBuilder(100);
      StringBuilder resultTrailer = new StringBuilder(0);
      List byteSegments = new ArrayList(1);
      Mode mode = DecodedBitStreamParser.Mode.ASCII_ENCODE;

      do {
         if (mode == DecodedBitStreamParser.Mode.ASCII_ENCODE) {
            mode = decodeAsciiSegment(bits, result, resultTrailer);
         } else {
            switch (mode) {
               case C40_ENCODE:
                  decodeC40Segment(bits, result);
                  break;
               case TEXT_ENCODE:
                  decodeTextSegment(bits, result);
                  break;
               case ANSIX12_ENCODE:
                  decodeAnsiX12Segment(bits, result);
                  break;
               case EDIFACT_ENCODE:
                  decodeEdifactSegment(bits, result);
                  break;
               case BASE256_ENCODE:
                  decodeBase256Segment(bits, result, byteSegments);
                  break;
               default:
                  throw FormatException.getFormatInstance();
            }

            mode = DecodedBitStreamParser.Mode.ASCII_ENCODE;
         }
      } while(mode != DecodedBitStreamParser.Mode.PAD_ENCODE && bits.available() > 0);

      if (resultTrailer.length() > 0) {
         result.append(resultTrailer.toString());
      }

      return new DecoderResult(bytes, result.toString(), byteSegments.isEmpty() ? null : byteSegments, (String)null);
   }

   private static Mode decodeAsciiSegment(BitSource bits, StringBuilder result, StringBuilder resultTrailer) throws FormatException {
      boolean upperShift = false;

      do {
         int oneByte = bits.readBits(8);
         if (oneByte == 0) {
            throw FormatException.getFormatInstance();
         }

         if (oneByte <= 128) {
            if (upperShift) {
               oneByte += 128;
            }

            result.append((char)(oneByte - 1));
            return DecodedBitStreamParser.Mode.ASCII_ENCODE;
         }

         if (oneByte == 129) {
            return DecodedBitStreamParser.Mode.PAD_ENCODE;
         }

         if (oneByte <= 229) {
            int value = oneByte - 130;
            if (value < 10) {
               result.append('0');
            }

            result.append(value);
         } else {
            if (oneByte == 230) {
               return DecodedBitStreamParser.Mode.C40_ENCODE;
            }

            if (oneByte == 231) {
               return DecodedBitStreamParser.Mode.BASE256_ENCODE;
            }

            if (oneByte == 232) {
               result.append('\u001d');
            } else if (oneByte != 233 && oneByte != 234) {
               if (oneByte == 235) {
                  upperShift = true;
               } else if (oneByte == 236) {
                  result.append("[)>\u001e05\u001d");
                  resultTrailer.insert(0, "\u001e\u0004");
               } else if (oneByte == 237) {
                  result.append("[)>\u001e06\u001d");
                  resultTrailer.insert(0, "\u001e\u0004");
               } else {
                  if (oneByte == 238) {
                     return DecodedBitStreamParser.Mode.ANSIX12_ENCODE;
                  }

                  if (oneByte == 239) {
                     return DecodedBitStreamParser.Mode.TEXT_ENCODE;
                  }

                  if (oneByte == 240) {
                     return DecodedBitStreamParser.Mode.EDIFACT_ENCODE;
                  }

                  if (oneByte != 241 && oneByte >= 242 && (oneByte != 254 || bits.available() != 0)) {
                     throw FormatException.getFormatInstance();
                  }
               }
            }
         }
      } while(bits.available() > 0);

      return DecodedBitStreamParser.Mode.ASCII_ENCODE;
   }

   private static void decodeC40Segment(BitSource bits, StringBuilder result) throws FormatException {
      boolean upperShift = false;
      int[] cValues = new int[3];
      int shift = 0;

      do {
         if (bits.available() == 8) {
            return;
         }

         int firstByte = bits.readBits(8);
         if (firstByte == 254) {
            return;
         }

         parseTwoBytes(firstByte, bits.readBits(8), cValues);

         for(int i = 0; i < 3; ++i) {
            int cValue = cValues[i];
            char c40char;
            switch (shift) {
               case 0:
                  if (cValue < 3) {
                     shift = cValue + 1;
                  } else {
                     if (cValue >= C40_BASIC_SET_CHARS.length) {
                        throw FormatException.getFormatInstance();
                     }

                     c40char = C40_BASIC_SET_CHARS[cValue];
                     if (upperShift) {
                        result.append((char)(c40char + 128));
                        upperShift = false;
                     } else {
                        result.append(c40char);
                     }
                  }
                  break;
               case 1:
                  if (upperShift) {
                     result.append((char)(cValue + 128));
                     upperShift = false;
                  } else {
                     result.append((char)cValue);
                  }

                  shift = 0;
                  break;
               case 2:
                  if (cValue < C40_SHIFT2_SET_CHARS.length) {
                     c40char = C40_SHIFT2_SET_CHARS[cValue];
                     if (upperShift) {
                        result.append((char)(c40char + 128));
                        upperShift = false;
                     } else {
                        result.append(c40char);
                     }
                  } else if (cValue == 27) {
                     result.append('\u001d');
                  } else {
                     if (cValue != 30) {
                        throw FormatException.getFormatInstance();
                     }

                     upperShift = true;
                  }

                  shift = 0;
                  break;
               case 3:
                  if (upperShift) {
                     result.append((char)(cValue + 224));
                     upperShift = false;
                  } else {
                     result.append((char)(cValue + 96));
                  }

                  shift = 0;
                  break;
               default:
                  throw FormatException.getFormatInstance();
            }
         }
      } while(bits.available() > 0);

   }

   private static void decodeTextSegment(BitSource bits, StringBuilder result) throws FormatException {
      boolean upperShift = false;
      int[] cValues = new int[3];
      int shift = 0;

      do {
         if (bits.available() == 8) {
            return;
         }

         int firstByte = bits.readBits(8);
         if (firstByte == 254) {
            return;
         }

         parseTwoBytes(firstByte, bits.readBits(8), cValues);

         for(int i = 0; i < 3; ++i) {
            int cValue = cValues[i];
            char textChar;
            switch (shift) {
               case 0:
                  if (cValue < 3) {
                     shift = cValue + 1;
                  } else {
                     if (cValue >= TEXT_BASIC_SET_CHARS.length) {
                        throw FormatException.getFormatInstance();
                     }

                     textChar = TEXT_BASIC_SET_CHARS[cValue];
                     if (upperShift) {
                        result.append((char)(textChar + 128));
                        upperShift = false;
                     } else {
                        result.append(textChar);
                     }
                  }
                  break;
               case 1:
                  if (upperShift) {
                     result.append((char)(cValue + 128));
                     upperShift = false;
                  } else {
                     result.append((char)cValue);
                  }

                  shift = 0;
                  break;
               case 2:
                  if (cValue < C40_SHIFT2_SET_CHARS.length) {
                     textChar = C40_SHIFT2_SET_CHARS[cValue];
                     if (upperShift) {
                        result.append((char)(textChar + 128));
                        upperShift = false;
                     } else {
                        result.append(textChar);
                     }
                  } else if (cValue == 27) {
                     result.append('\u001d');
                  } else {
                     if (cValue != 30) {
                        throw FormatException.getFormatInstance();
                     }

                     upperShift = true;
                  }

                  shift = 0;
                  break;
               case 3:
                  if (cValue >= TEXT_SHIFT3_SET_CHARS.length) {
                     throw FormatException.getFormatInstance();
                  }

                  textChar = TEXT_SHIFT3_SET_CHARS[cValue];
                  if (upperShift) {
                     result.append((char)(textChar + 128));
                     upperShift = false;
                  } else {
                     result.append(textChar);
                  }

                  shift = 0;
                  break;
               default:
                  throw FormatException.getFormatInstance();
            }
         }
      } while(bits.available() > 0);

   }

   private static void decodeAnsiX12Segment(BitSource bits, StringBuilder result) throws FormatException {
      int[] cValues = new int[3];

      do {
         if (bits.available() == 8) {
            return;
         }

         int firstByte = bits.readBits(8);
         if (firstByte == 254) {
            return;
         }

         parseTwoBytes(firstByte, bits.readBits(8), cValues);

         for(int i = 0; i < 3; ++i) {
            int cValue = cValues[i];
            if (cValue == 0) {
               result.append('\r');
            } else if (cValue == 1) {
               result.append('*');
            } else if (cValue == 2) {
               result.append('>');
            } else if (cValue == 3) {
               result.append(' ');
            } else if (cValue < 14) {
               result.append((char)(cValue + 44));
            } else {
               if (cValue >= 40) {
                  throw FormatException.getFormatInstance();
               }

               result.append((char)(cValue + 51));
            }
         }
      } while(bits.available() > 0);

   }

   private static void parseTwoBytes(int firstByte, int secondByte, int[] result) {
      int fullBitValue = (firstByte << 8) + secondByte - 1;
      int temp = fullBitValue / 1600;
      result[0] = temp;
      fullBitValue -= temp * 1600;
      temp = fullBitValue / 40;
      result[1] = temp;
      result[2] = fullBitValue - temp * 40;
   }

   private static void decodeEdifactSegment(BitSource bits, StringBuilder result) {
      while(bits.available() > 16) {
         for(int i = 0; i < 4; ++i) {
            int edifactValue = bits.readBits(6);
            if (edifactValue == 31) {
               int bitsLeft = 8 - bits.getBitOffset();
               if (bitsLeft != 8) {
                  bits.readBits(bitsLeft);
               }

               return;
            }

            if ((edifactValue & 32) == 0) {
               edifactValue |= 64;
            }

            result.append((char)edifactValue);
         }

         if (bits.available() <= 0) {
            return;
         }
      }

   }

   private static void decodeBase256Segment(BitSource bits, StringBuilder result, Collection byteSegments) throws FormatException {
      int codewordPosition = 1 + bits.getByteOffset();
      int d1 = unrandomize255State(bits.readBits(8), codewordPosition++);
      int count;
      if (d1 == 0) {
         count = bits.available() / 8;
      } else if (d1 < 250) {
         count = d1;
      } else {
         count = 250 * (d1 - 249) + unrandomize255State(bits.readBits(8), codewordPosition++);
      }

      if (count < 0) {
         throw FormatException.getFormatInstance();
      } else {
         byte[] bytes = new byte[count];

         for(int i = 0; i < count; ++i) {
            if (bits.available() < 8) {
               throw FormatException.getFormatInstance();
            }

            bytes[i] = (byte)unrandomize255State(bits.readBits(8), codewordPosition++);
         }

         byteSegments.add(bytes);

         try {
            result.append(new String(bytes, "ISO8859_1"));
         } catch (UnsupportedEncodingException var8) {
            throw new IllegalStateException("Platform does not support required encoding: " + var8);
         }
      }
   }

   private static int unrandomize255State(int randomizedBase256Codeword, int base256CodewordPosition) {
      int pseudoRandomNumber = 149 * base256CodewordPosition % 255 + 1;
      int tempVariable = randomizedBase256Codeword - pseudoRandomNumber;
      return tempVariable >= 0 ? tempVariable : tempVariable + 256;
   }

   private static enum Mode {
      PAD_ENCODE,
      ASCII_ENCODE,
      C40_ENCODE,
      TEXT_ENCODE,
      ANSIX12_ENCODE,
      EDIFACT_ENCODE,
      BASE256_ENCODE;
   }
}
