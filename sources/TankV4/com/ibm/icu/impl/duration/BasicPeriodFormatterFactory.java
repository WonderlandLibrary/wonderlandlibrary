package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterData;
import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import java.util.Locale;

public class BasicPeriodFormatterFactory implements PeriodFormatterFactory {
   private final PeriodFormatterDataService ds;
   private PeriodFormatterData data;
   private BasicPeriodFormatterFactory.Customizations customizations;
   private boolean customizationsInUse;
   private String localeName;

   BasicPeriodFormatterFactory(PeriodFormatterDataService var1) {
      this.ds = var1;
      this.customizations = new BasicPeriodFormatterFactory.Customizations();
      this.localeName = Locale.getDefault().toString();
   }

   public static BasicPeriodFormatterFactory getDefault() {
      return (BasicPeriodFormatterFactory)BasicPeriodFormatterService.getInstance().newPeriodFormatterFactory();
   }

   public PeriodFormatterFactory setLocale(String var1) {
      this.data = null;
      this.localeName = var1;
      return this;
   }

   public PeriodFormatterFactory setDisplayLimit(boolean var1) {
      this.updateCustomizations().displayLimit = var1;
      return this;
   }

   public boolean getDisplayLimit() {
      return this.customizations.displayLimit;
   }

   public PeriodFormatterFactory setDisplayPastFuture(boolean var1) {
      this.updateCustomizations().displayDirection = var1;
      return this;
   }

   public boolean getDisplayPastFuture() {
      return this.customizations.displayDirection;
   }

   public PeriodFormatterFactory setSeparatorVariant(int var1) {
      this.updateCustomizations().separatorVariant = (byte)var1;
      return this;
   }

   public int getSeparatorVariant() {
      return this.customizations.separatorVariant;
   }

   public PeriodFormatterFactory setUnitVariant(int var1) {
      this.updateCustomizations().unitVariant = (byte)var1;
      return this;
   }

   public int getUnitVariant() {
      return this.customizations.unitVariant;
   }

   public PeriodFormatterFactory setCountVariant(int var1) {
      this.updateCustomizations().countVariant = (byte)var1;
      return this;
   }

   public int getCountVariant() {
      return this.customizations.countVariant;
   }

   public PeriodFormatter getFormatter() {
      this.customizationsInUse = true;
      return new BasicPeriodFormatter(this, this.localeName, this.getData(), this.customizations);
   }

   private BasicPeriodFormatterFactory.Customizations updateCustomizations() {
      if (this.customizationsInUse) {
         this.customizations = this.customizations.copy();
         this.customizationsInUse = false;
      }

      return this.customizations;
   }

   PeriodFormatterData getData() {
      if (this.data == null) {
         this.data = this.ds.get(this.localeName);
      }

      return this.data;
   }

   PeriodFormatterData getData(String var1) {
      return this.ds.get(var1);
   }

   static class Customizations {
      boolean displayLimit = true;
      boolean displayDirection = true;
      byte separatorVariant = 2;
      byte unitVariant = 0;
      byte countVariant = 0;

      public BasicPeriodFormatterFactory.Customizations copy() {
         BasicPeriodFormatterFactory.Customizations var1 = new BasicPeriodFormatterFactory.Customizations();
         var1.displayLimit = this.displayLimit;
         var1.displayDirection = this.displayDirection;
         var1.separatorVariant = this.separatorVariant;
         var1.unitVariant = this.unitVariant;
         var1.countVariant = this.countVariant;
         return var1;
      }
   }
}
