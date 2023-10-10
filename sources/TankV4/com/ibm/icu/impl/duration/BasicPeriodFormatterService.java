package com.ibm.icu.impl.duration;

import com.ibm.icu.impl.duration.impl.PeriodFormatterDataService;
import com.ibm.icu.impl.duration.impl.ResourceBasedPeriodFormatterDataService;
import java.util.Collection;

public class BasicPeriodFormatterService implements PeriodFormatterService {
   private static BasicPeriodFormatterService instance;
   private PeriodFormatterDataService ds;

   public static BasicPeriodFormatterService getInstance() {
      if (instance == null) {
         ResourceBasedPeriodFormatterDataService var0 = ResourceBasedPeriodFormatterDataService.getInstance();
         instance = new BasicPeriodFormatterService(var0);
      }

      return instance;
   }

   public BasicPeriodFormatterService(PeriodFormatterDataService var1) {
      this.ds = var1;
   }

   public DurationFormatterFactory newDurationFormatterFactory() {
      return new BasicDurationFormatterFactory(this);
   }

   public PeriodFormatterFactory newPeriodFormatterFactory() {
      return new BasicPeriodFormatterFactory(this.ds);
   }

   public PeriodBuilderFactory newPeriodBuilderFactory() {
      return new BasicPeriodBuilderFactory(this.ds);
   }

   public Collection getAvailableLocaleNames() {
      return this.ds.getAvailableLocales();
   }
}
