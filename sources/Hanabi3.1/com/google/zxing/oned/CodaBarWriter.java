package com.google.zxing.oned;

import java.util.Arrays;

public final class CodaBarWriter extends OneDimensionalCodeWriter {
   private static final char[] START_CHARS = new char[]{'A', 'B', 'C', 'D'};
   private static final char[] END_CHARS = new char[]{'T', 'N', '*', 'E'};

   public boolean[] encode(String contents) {
      if (!CodaBarReader.arrayContains(START_CHARS, Character.toUpperCase(contents.charAt(0)))) {
         throw new IllegalArgumentException("Codabar should start with one of the following: " + Arrays.toString(START_CHARS));
      } else if (!CodaBarReader.arrayContains(END_CHARS, Character.toUpperCase(contents.charAt(contents.length() - 1)))) {
         throw new IllegalArgumentException("Codabar should end with one of the following: " + Arrays.toString(END_CHARS));
      } else {
         int resultLength = 20;
         char[] charsWhichAreTenLengthEachAfterDecoded = new char[]{'/', ':', '+', '.'};

         for(int i = 1; i < contents.length() - 1; ++i) {
            if (!Character.isDigit(contents.charAt(i)) && contents.charAt(i) != '-' && contents.charAt(i) != '$') {
               if (!CodaBarReader.arrayContains(charsWhichAreTenLengthEachAfterDecoded, contents.charAt(i))) {
                  throw new IllegalArgumentException("Cannot encode : '" + contents.charAt(i) + '\'');
               }

               resultLength += 10;
            } else {
               resultLength += 9;
            }
         }

         resultLength += contents.length() - 1;
         boolean[] result = new boolean[resultLength];
         int position = 0;

         for(int index = 0; index < contents.length(); ++index) {
            char c = Character.toUpperCase(contents.charAt(index));
            if (index == contents.length() - 1) {
               switch (c) {
                  case '*':
                     c = 'C';
                     break;
                  case 'E':
                     c = 'D';
                     break;
                  case 'N':
                     c = 'B';
                     break;
                  case 'T':
                     c = 'A';
               }
            }

            int code = 0;

            for(int i = 0; i < CodaBarReader.ALPHABET.length; ++i) {
               if (c == CodaBarReader.ALPHABET[i]) {
                  code = CodaBarReader.CHARACTER_ENCODINGS[i];
                  break;
               }
            }

            boolean color = true;
            int counter = 0;
            int bit = 0;

            while(true) {
               while(bit < 7) {
                  result[position] = color;
                  ++position;
                  if ((code >> 6 - bit & 1) != 0 && counter != 1) {
                     ++counter;
                  } else {
                     color = !color;
                     ++bit;
                     counter = 0;
                  }
               }

               if (index < contents.length() - 1) {
                  result[position] = false;
                  ++position;
               }
               break;
            }
         }

         return result;
      }
   }
}
