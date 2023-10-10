package joptsimple.internal;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Rows {
   private final int overallWidth;
   private final int columnSeparatorWidth;
   private final Set rows = new LinkedHashSet();
   private int widthOfWidestOption;
   private int widthOfWidestDescription;

   public Rows(int var1, int var2) {
      this.overallWidth = var1;
      this.columnSeparatorWidth = var2;
   }

   public void add(String var1, String var2) {
      this.add(new Row(var1, var2));
   }

   private void add(Row var1) {
      this.rows.add(var1);
      this.widthOfWidestOption = Math.max(this.widthOfWidestOption, var1.option.length());
      this.widthOfWidestDescription = Math.max(this.widthOfWidestDescription, var1.description.length());
   }

   private void reset() {
      this.rows.clear();
      this.widthOfWidestOption = 0;
      this.widthOfWidestDescription = 0;
   }

   public void fitToWidth() {
      Columns var1 = new Columns(this.optionWidth(), this.descriptionWidth());
      LinkedHashSet var2 = new LinkedHashSet();
      Iterator var3 = this.rows.iterator();

      Row var4;
      while(var3.hasNext()) {
         var4 = (Row)var3.next();
         var2.addAll(var1.fit(var4));
      }

      this.reset();
      var3 = var2.iterator();

      while(var3.hasNext()) {
         var4 = (Row)var3.next();
         this.add(var4);
      }

   }

   public String render() {
      StringBuilder var1 = new StringBuilder();
      Iterator var2 = this.rows.iterator();

      while(var2.hasNext()) {
         Row var3 = (Row)var2.next();
         this.pad(var1, var3.option, this.optionWidth()).append(Strings.repeat(' ', this.columnSeparatorWidth));
         this.pad(var1, var3.description, this.descriptionWidth()).append(Strings.LINE_SEPARATOR);
      }

      return var1.toString();
   }

   private int optionWidth() {
      return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestOption);
   }

   private int descriptionWidth() {
      return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestDescription);
   }

   private StringBuilder pad(StringBuilder var1, String var2, int var3) {
      var1.append(var2).append(Strings.repeat(' ', var3 - var2.length()));
      return var1;
   }
}
