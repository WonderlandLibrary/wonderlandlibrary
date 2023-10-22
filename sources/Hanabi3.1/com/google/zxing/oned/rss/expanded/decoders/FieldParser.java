package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;

final class FieldParser {
   private static final Object VARIABLE_LENGTH = new Object();
   private static final Object[][] TWO_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_DATA_LENGTH;
   private static final Object[][] THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
   private static final Object[][] FOUR_DIGIT_DATA_LENGTH;

   private FieldParser() {
   }

   static String parseFieldsInGeneralPurpose(String rawInformation) throws NotFoundException {
      if (rawInformation.length() == 0) {
         return null;
      } else if (rawInformation.length() < 2) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         String firstTwoDigits = rawInformation.substring(0, 2);
         Object[][] arr$ = TWO_DIGIT_DATA_LENGTH;
         int len$ = arr$.length;

         int len$;
         for(len$ = 0; len$ < len$; ++len$) {
            Object[] dataLength = arr$[len$];
            if (dataLength[0].equals(firstTwoDigits)) {
               if (dataLength[1] == VARIABLE_LENGTH) {
                  return processVariableAI(2, (Integer)dataLength[2], rawInformation);
               }

               return processFixedAI(2, (Integer)dataLength[1], rawInformation);
            }
         }

         if (rawInformation.length() < 3) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String firstThreeDigits = rawInformation.substring(0, 3);
            Object[][] arr$ = THREE_DIGIT_DATA_LENGTH;
            len$ = arr$.length;

            Object[] dataLength;
            int len$;
            for(len$ = 0; len$ < len$; ++len$) {
               dataLength = arr$[len$];
               if (dataLength[0].equals(firstThreeDigits)) {
                  if (dataLength[1] == VARIABLE_LENGTH) {
                     return processVariableAI(3, (Integer)dataLength[2], rawInformation);
                  }

                  return processFixedAI(3, (Integer)dataLength[1], rawInformation);
               }
            }

            arr$ = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
            len$ = arr$.length;

            for(len$ = 0; len$ < len$; ++len$) {
               dataLength = arr$[len$];
               if (dataLength[0].equals(firstThreeDigits)) {
                  if (dataLength[1] == VARIABLE_LENGTH) {
                     return processVariableAI(4, (Integer)dataLength[2], rawInformation);
                  }

                  return processFixedAI(4, (Integer)dataLength[1], rawInformation);
               }
            }

            if (rawInformation.length() < 4) {
               throw NotFoundException.getNotFoundInstance();
            } else {
               String firstFourDigits = rawInformation.substring(0, 4);
               Object[][] arr$ = FOUR_DIGIT_DATA_LENGTH;
               len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Object[] dataLength = arr$[i$];
                  if (dataLength[0].equals(firstFourDigits)) {
                     if (dataLength[1] == VARIABLE_LENGTH) {
                        return processVariableAI(4, (Integer)dataLength[2], rawInformation);
                     }

                     return processFixedAI(4, (Integer)dataLength[1], rawInformation);
                  }
               }

               throw NotFoundException.getNotFoundInstance();
            }
         }
      }
   }

   private static String processFixedAI(int aiSize, int fieldSize, String rawInformation) throws NotFoundException {
      if (rawInformation.length() < aiSize) {
         throw NotFoundException.getNotFoundInstance();
      } else {
         String ai = rawInformation.substring(0, aiSize);
         if (rawInformation.length() < aiSize + fieldSize) {
            throw NotFoundException.getNotFoundInstance();
         } else {
            String field = rawInformation.substring(aiSize, aiSize + fieldSize);
            String remaining = rawInformation.substring(aiSize + fieldSize);
            String result = '(' + ai + ')' + field;
            String parsedAI = parseFieldsInGeneralPurpose(remaining);
            return parsedAI == null ? result : result + parsedAI;
         }
      }
   }

   private static String processVariableAI(int aiSize, int variableFieldSize, String rawInformation) throws NotFoundException {
      String ai = rawInformation.substring(0, aiSize);
      int maxSize;
      if (rawInformation.length() < aiSize + variableFieldSize) {
         maxSize = rawInformation.length();
      } else {
         maxSize = aiSize + variableFieldSize;
      }

      String field = rawInformation.substring(aiSize, maxSize);
      String remaining = rawInformation.substring(maxSize);
      String result = '(' + ai + ')' + field;
      String parsedAI = parseFieldsInGeneralPurpose(remaining);
      return parsedAI == null ? result : result + parsedAI;
   }

   static {
      TWO_DIGIT_DATA_LENGTH = new Object[][]{{"00", 18}, {"01", 14}, {"02", 14}, {"10", VARIABLE_LENGTH, 20}, {"11", 6}, {"12", 6}, {"13", 6}, {"15", 6}, {"17", 6}, {"20", 2}, {"21", VARIABLE_LENGTH, 20}, {"22", VARIABLE_LENGTH, 29}, {"30", VARIABLE_LENGTH, 8}, {"37", VARIABLE_LENGTH, 8}, {"90", VARIABLE_LENGTH, 30}, {"91", VARIABLE_LENGTH, 30}, {"92", VARIABLE_LENGTH, 30}, {"93", VARIABLE_LENGTH, 30}, {"94", VARIABLE_LENGTH, 30}, {"95", VARIABLE_LENGTH, 30}, {"96", VARIABLE_LENGTH, 30}, {"97", VARIABLE_LENGTH, 30}, {"98", VARIABLE_LENGTH, 30}, {"99", VARIABLE_LENGTH, 30}};
      THREE_DIGIT_DATA_LENGTH = new Object[][]{{"240", VARIABLE_LENGTH, 30}, {"241", VARIABLE_LENGTH, 30}, {"242", VARIABLE_LENGTH, 6}, {"250", VARIABLE_LENGTH, 30}, {"251", VARIABLE_LENGTH, 30}, {"253", VARIABLE_LENGTH, 17}, {"254", VARIABLE_LENGTH, 20}, {"400", VARIABLE_LENGTH, 30}, {"401", VARIABLE_LENGTH, 30}, {"402", 17}, {"403", VARIABLE_LENGTH, 30}, {"410", 13}, {"411", 13}, {"412", 13}, {"413", 13}, {"414", 13}, {"420", VARIABLE_LENGTH, 20}, {"421", VARIABLE_LENGTH, 15}, {"422", 3}, {"423", VARIABLE_LENGTH, 15}, {"424", 3}, {"425", 3}, {"426", 3}};
      THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = new Object[][]{{"310", 6}, {"311", 6}, {"312", 6}, {"313", 6}, {"314", 6}, {"315", 6}, {"316", 6}, {"320", 6}, {"321", 6}, {"322", 6}, {"323", 6}, {"324", 6}, {"325", 6}, {"326", 6}, {"327", 6}, {"328", 6}, {"329", 6}, {"330", 6}, {"331", 6}, {"332", 6}, {"333", 6}, {"334", 6}, {"335", 6}, {"336", 6}, {"340", 6}, {"341", 6}, {"342", 6}, {"343", 6}, {"344", 6}, {"345", 6}, {"346", 6}, {"347", 6}, {"348", 6}, {"349", 6}, {"350", 6}, {"351", 6}, {"352", 6}, {"353", 6}, {"354", 6}, {"355", 6}, {"356", 6}, {"357", 6}, {"360", 6}, {"361", 6}, {"362", 6}, {"363", 6}, {"364", 6}, {"365", 6}, {"366", 6}, {"367", 6}, {"368", 6}, {"369", 6}, {"390", VARIABLE_LENGTH, 15}, {"391", VARIABLE_LENGTH, 18}, {"392", VARIABLE_LENGTH, 15}, {"393", VARIABLE_LENGTH, 18}, {"703", VARIABLE_LENGTH, 30}};
      FOUR_DIGIT_DATA_LENGTH = new Object[][]{{"7001", 13}, {"7002", VARIABLE_LENGTH, 30}, {"7003", 10}, {"8001", 14}, {"8002", VARIABLE_LENGTH, 20}, {"8003", VARIABLE_LENGTH, 30}, {"8004", VARIABLE_LENGTH, 30}, {"8005", 6}, {"8006", 18}, {"8007", VARIABLE_LENGTH, 30}, {"8008", VARIABLE_LENGTH, 12}, {"8018", 18}, {"8020", VARIABLE_LENGTH, 25}, {"8100", 6}, {"8101", 10}, {"8102", 2}, {"8110", VARIABLE_LENGTH, 30}};
   }
}
