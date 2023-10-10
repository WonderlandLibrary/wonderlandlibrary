package com.ibm.icu.text;

final class RBNFChinesePostProcessor implements RBNFPostProcessor {
   private boolean longForm;
   private int format;
   private static final String[] rulesetNames = new String[]{"%traditional", "%simplified", "%accounting", "%time"};

   public void init(RuleBasedNumberFormat var1, String var2) {
   }

   public void process(StringBuffer var1, NFRuleSet var2) {
      String var3 = var2.getName();

      int var4;
      for(var4 = 0; var4 < rulesetNames.length; ++var4) {
         if (rulesetNames[var4].equals(var3)) {
            this.format = var4;
            this.longForm = var4 == 1 || var4 == 3;
            break;
         }
      }

      if (this.longForm) {
         for(var4 = var1.indexOf("*"); var4 != -1; var4 = var1.indexOf("*", var4)) {
            var1.delete(var4, var4 + 1);
         }

      } else {
         String var13 = "點";
         String[][] var5 = new String[][]{{"萬", "億", "兆", "〇"}, {"万", "亿", "兆", "〇"}, {"萬", "億", "兆", "零"}};
         String[] var6 = var5[this.format];

         int var7;
         int var8;
         for(var7 = 0; var7 < var6.length - 1; ++var7) {
            var8 = var1.indexOf(var6[var7]);
            if (var8 != -1) {
               var1.insert(var8 + var6[var7].length(), '|');
            }
         }

         int var14 = var1.indexOf("點");
         if (var14 == -1) {
            var14 = var1.length();
         }

         var7 = 0;
         var8 = -1;
         String var9 = var5[this.format][3];

         int var10;
         while(var14 >= 0) {
            var10 = var1.lastIndexOf("|", var14);
            int var11 = var1.lastIndexOf(var9, var14);
            int var12 = 0;
            if (var11 > var10) {
               var12 = var11 > 0 && var1.charAt(var11 - 1) != '*' ? 2 : 1;
            }

            var14 = var10 - 1;
            switch(var7 * 3 + var12) {
            case 0:
               var7 = var12;
               var8 = -1;
               break;
            case 1:
               var7 = var12;
               var8 = var11;
               break;
            case 2:
               var7 = var12;
               var8 = -1;
               break;
            case 3:
               var7 = var12;
               var8 = -1;
               break;
            case 4:
               var1.delete(var11 - 1, var11 + var9.length());
               var7 = 0;
               var8 = -1;
               break;
            case 5:
               var1.delete(var8 - 1, var8 + var9.length());
               var7 = var12;
               var8 = -1;
               break;
            case 6:
               var7 = var12;
               var8 = -1;
               break;
            case 7:
               var1.delete(var11 - 1, var11 + var9.length());
               var7 = 0;
               var8 = -1;
               break;
            case 8:
               var7 = var12;
               var8 = -1;
               break;
            default:
               throw new IllegalStateException();
            }
         }

         var10 = var1.length();

         while(true) {
            char var15;
            do {
               --var10;
               if (var10 < 0) {
                  return;
               }

               var15 = var1.charAt(var10);
            } while(var15 != '*' && var15 != '|');

            var1.delete(var10, var10 + 1);
         }
      }
   }
}
