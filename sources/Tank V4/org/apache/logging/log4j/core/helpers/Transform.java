package org.apache.logging.log4j.core.helpers;

public final class Transform {
   private static final String CDATA_START = "<![CDATA[";
   private static final String CDATA_END = "]]>";
   private static final String CDATA_PSEUDO_END = "]]&gt;";
   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
   private static final int CDATA_END_LEN = 3;

   private Transform() {
   }

   public static String escapeHtmlTags(String var0) {
      if (Strings.isEmpty(var0) || var0.indexOf(34) == -1 && var0.indexOf(38) == -1 && var0.indexOf(60) == -1 && var0.indexOf(62) == -1) {
         return var0;
      } else {
         StringBuilder var1 = new StringBuilder(var0.length() + 6);
         boolean var2 = true;
         int var3 = var0.length();

         for(int var4 = 0; var4 < var3; ++var4) {
            char var5 = var0.charAt(var4);
            if (var5 > '>') {
               var1.append(var5);
            } else if (var5 == '<') {
               var1.append("&lt;");
            } else if (var5 == '>') {
               var1.append("&gt;");
            } else if (var5 == '&') {
               var1.append("&amp;");
            } else if (var5 == '"') {
               var1.append("&quot;");
            } else {
               var1.append(var5);
            }
         }

         return var1.toString();
      }
   }

   public static void appendEscapingCDATA(StringBuilder var0, String var1) {
      if (var1 != null) {
         int var2 = var1.indexOf("]]>");
         if (var2 < 0) {
            var0.append(var1);
         } else {
            int var3;
            for(var3 = 0; var2 > -1; var2 = var1.indexOf("]]>", var3)) {
               var0.append(var1.substring(var3, var2));
               var0.append("]]>]]&gt;<![CDATA[");
               var3 = var2 + CDATA_END_LEN;
               if (var3 >= var1.length()) {
                  return;
               }
            }

            var0.append(var1.substring(var3));
         }
      }

   }

   public static String escapeJsonControlCharacters(String var0) {
      if (Strings.isEmpty(var0) || var0.indexOf(34) == -1 && var0.indexOf(92) == -1 && var0.indexOf(47) == -1 && var0.indexOf(8) == -1 && var0.indexOf(12) == -1 && var0.indexOf(10) == -1 && var0.indexOf(13) == -1 && var0.indexOf(9) == -1) {
         return var0;
      } else {
         StringBuilder var1 = new StringBuilder(var0.length() + 6);
         int var2 = var0.length();

         for(int var3 = 0; var3 < var2; ++var3) {
            char var4 = var0.charAt(var3);
            String var5 = "\\\\";
            switch(var4) {
            case '\b':
               var1.append("\\\\");
               var1.append('b');
               break;
            case '\t':
               var1.append("\\\\");
               var1.append('t');
               break;
            case '\n':
               var1.append("\\\\");
               var1.append('n');
               break;
            case '\f':
               var1.append("\\\\");
               var1.append('f');
               break;
            case '\r':
               var1.append("\\\\");
               var1.append('r');
               break;
            case '"':
               var1.append("\\\\");
               var1.append(var4);
               break;
            case '/':
               var1.append("\\\\");
               var1.append(var4);
               break;
            case '\\':
               var1.append("\\\\");
               var1.append(var4);
               break;
            default:
               var1.append(var4);
            }
         }

         return var1.toString();
      }
   }
}
