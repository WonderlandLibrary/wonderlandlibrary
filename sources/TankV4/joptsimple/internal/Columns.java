package joptsimple.internal;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class Columns {
   private static final int INDENT_WIDTH = 2;
   private final int optionWidth;
   private final int descriptionWidth;

   Columns(int var1, int var2) {
      this.optionWidth = var1;
      this.descriptionWidth = var2;
   }

   List fit(Row var1) {
      List var2 = this.piecesOf(var1.option, this.optionWidth);
      List var3 = this.piecesOf(var1.description, this.descriptionWidth);
      ArrayList var4 = new ArrayList();

      for(int var5 = 0; var5 < Math.max(var2.size(), var3.size()); ++var5) {
         var4.add(new Row(itemOrEmpty(var2, var5), itemOrEmpty(var3, var5)));
      }

      return var4;
   }

   private static String itemOrEmpty(List var0, int var1) {
      return var1 >= var0.size() ? "" : (String)var0.get(var1);
   }

   private List piecesOf(String var1, int var2) {
      ArrayList var3 = new ArrayList();
      String[] var4 = var1.trim().split(Strings.LINE_SEPARATOR);
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         var3.addAll(this.piecesOfEmbeddedLine(var7, var2));
      }

      return var3;
   }

   private List piecesOfEmbeddedLine(String var1, int var2) {
      ArrayList var3 = new ArrayList();
      BreakIterator var4 = BreakIterator.getLineInstance(Locale.US);
      var4.setText(var1);
      StringBuilder var5 = new StringBuilder();
      int var6 = var4.first();

      for(int var7 = var4.next(); var7 != -1; var7 = var4.next()) {
         var5 = this.processNextWord(var1, var5, var6, var7, var2, var3);
         var6 = var7;
      }

      if (var5.length() > 0) {
         var3.add(var5.toString());
      }

      return var3;
   }

   private StringBuilder processNextWord(String var1, StringBuilder var2, int var3, int var4, int var5, List var6) {
      StringBuilder var7 = var2;
      String var8 = var1.substring(var3, var4);
      if (var2.length() + var8.length() > var5) {
         var6.add(var2.toString().replaceAll("\\s+$", ""));
         var7 = (new StringBuilder(Strings.repeat(' ', 2))).append(var8);
      } else {
         var2.append(var8);
      }

      return var7;
   }
}
