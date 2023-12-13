package com.sun.javafx.scene.control.skin;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.chrono.IsoChronology;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoField;
import java.util.Iterator;
import java.util.Locale;
import javafx.geometry.Pos;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

class DatePickerHijrahContent extends DatePickerContent {
   private Label hijrahMonthYearLabel;

   DatePickerHijrahContent(DatePicker var1) {
      super(var1);
   }

   protected Chronology getPrimaryChronology() {
      return IsoChronology.INSTANCE;
   }

   protected BorderPane createMonthYearPane() {
      BorderPane var1 = super.createMonthYearPane();
      this.hijrahMonthYearLabel = new Label();
      this.hijrahMonthYearLabel.getStyleClass().add("secondary-label");
      var1.setBottom(this.hijrahMonthYearLabel);
      BorderPane.setAlignment(this.hijrahMonthYearLabel, Pos.CENTER);
      return var1;
   }

   protected void updateMonthYearPane() {
      super.updateMonthYearPane();
      Locale var1 = this.getLocale();
      HijrahChronology var2 = HijrahChronology.INSTANCE;
      long var3 = -1L;
      long var5 = -1L;
      String var7 = null;
      String var8 = null;
      String var9 = null;
      YearMonth var10 = (YearMonth)this.displayedYearMonthProperty().get();
      Iterator var11 = this.dayCells.iterator();

      while(var11.hasNext()) {
         DateCell var12 = (DateCell)var11.next();
         LocalDate var13 = this.dayCellDate(var12);
         if (var10.equals(YearMonth.from(var13))) {
            try {
               HijrahDate var14 = var2.date(var13);
               long var15 = var14.getLong(ChronoField.MONTH_OF_YEAR);
               long var17 = var14.getLong(ChronoField.YEAR);
               if (var9 == null || var15 != var3) {
                  String var19 = this.monthFormatter.withLocale(var1).withChronology(var2).withDecimalStyle(DecimalStyle.of(var1)).format(var14);
                  String var20 = this.yearFormatter.withLocale(var1).withChronology(var2).withDecimalStyle(DecimalStyle.of(var1)).format(var14);
                  if (var9 != null) {
                     if (var17 > var5) {
                        var9 = var7 + " " + var8 + " - " + var19 + " " + var20;
                     } else {
                        var9 = var7 + " - " + var19 + " " + var8;
                     }
                     break;
                  }

                  var3 = var15;
                  var5 = var17;
                  var7 = var19;
                  var8 = var20;
                  var9 = var19 + " " + var20;
               }
            } catch (DateTimeException var21) {
            }
         }
      }

      this.hijrahMonthYearLabel.setText(var9);
   }

   protected void createDayCells() {
      super.createDayCells();
      Iterator var1 = this.dayCells.iterator();

      while(var1.hasNext()) {
         DateCell var2 = (DateCell)var1.next();
         Text var3 = new Text();
         var2.getProperties().put("DateCell.secondaryText", var3);
      }

   }

   void updateDayCells() {
      super.updateDayCells();
      Locale var1 = this.getLocale();
      HijrahChronology var2 = HijrahChronology.INSTANCE;
      boolean var3 = true;
      boolean var4 = true;
      boolean var5 = true;
      boolean var6 = false;
      Iterator var7 = this.dayCells.iterator();

      while(var7.hasNext()) {
         DateCell var8 = (DateCell)var7.next();
         Text var9 = (Text)var8.getProperties().get("DateCell.secondaryText");
         var8.getStyleClass().add("hijrah-day-cell");
         var9.getStyleClass().setAll((Object[])("text", "secondary-text"));

         try {
            HijrahDate var10 = var2.date(this.dayCellDate(var8));
            String var11 = this.dayCellFormatter.withLocale(var1).withChronology(var2).withDecimalStyle(DecimalStyle.of(var1)).format(var10);
            var9.setText(var11);
            var8.requestLayout();
         } catch (DateTimeException var12) {
            var9.setText(" ");
            var8.setDisable(true);
         }
      }

   }
}
