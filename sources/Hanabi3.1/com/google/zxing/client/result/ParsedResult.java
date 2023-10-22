package com.google.zxing.client.result;

public abstract class ParsedResult {
   private final ParsedResultType type;

   protected ParsedResult(ParsedResultType type) {
      this.type = type;
   }

   public final ParsedResultType getType() {
      return this.type;
   }

   public abstract String getDisplayResult();

   public final String toString() {
      return this.getDisplayResult();
   }

   public static void maybeAppend(String value, StringBuilder result) {
      if (value != null && value.length() > 0) {
         if (result.length() > 0) {
            result.append('\n');
         }

         result.append(value);
      }

   }

   public static void maybeAppend(String[] value, StringBuilder result) {
      if (value != null) {
         String[] arr$ = value;
         int len$ = value.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            if (s != null && s.length() > 0) {
               if (result.length() > 0) {
                  result.append('\n');
               }

               result.append(s);
            }
         }
      }

   }
}
