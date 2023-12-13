package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.ICUData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

public class ResourceBasedPeriodFormatterDataService extends PeriodFormatterDataService {
   private Collection availableLocales;
   private PeriodFormatterData lastData = null;
   private String lastLocale = null;
   private Map cache = new HashMap();
   private static final String PATH = "data/";
   private static final ResourceBasedPeriodFormatterDataService singleton = new ResourceBasedPeriodFormatterDataService();

   public static ResourceBasedPeriodFormatterDataService getInstance() {
      return singleton;
   }

   private ResourceBasedPeriodFormatterDataService() {
      ArrayList var1 = new ArrayList();
      InputStream var2 = ICUData.getRequiredStream(this.getClass(), "data/index.txt");

      try {
         BufferedReader var3 = new BufferedReader(new InputStreamReader(var2, "UTF-8"));
         String var4 = null;

         while(true) {
            if (null == (var4 = var3.readLine())) {
               var3.close();
               break;
            }

            var4 = var4.trim();
            if (!var4.startsWith("#") && var4.length() != 0) {
               var1.add(var4);
            }
         }
      } catch (IOException var5) {
         throw new IllegalStateException("IO Error reading data/index.txt: " + var5.toString());
      }

      this.availableLocales = Collections.unmodifiableList(var1);
   }

   public PeriodFormatterData get(String var1) {
      int var2 = var1.indexOf(64);
      if (var2 != -1) {
         var1 = var1.substring(0, var2);
      }

      synchronized(this){}
      if (this.lastLocale != null && this.lastLocale.equals(var1)) {
         return this.lastData;
      } else {
         PeriodFormatterData var4 = (PeriodFormatterData)this.cache.get(var1);
         if (var4 == null) {
            String var5 = var1;

            while(!this.availableLocales.contains(var5)) {
               int var6 = var5.lastIndexOf("_");
               if (var6 > -1) {
                  var5 = var5.substring(0, var6);
               } else {
                  if ("test".equals(var5)) {
                     var5 = null;
                     break;
                  }

                  var5 = "test";
               }
            }

            if (var5 == null) {
               throw new MissingResourceException("Duration data not found for  " + var1, "data/", var1);
            }

            String var11 = "data/pfd_" + var5 + ".xml";

            try {
               InputStream var7 = ICUData.getStream(this.getClass(), var11);
               if (var7 == null) {
                  throw new MissingResourceException("no resource named " + var11, var11, "");
               }

               DataRecord var8 = DataRecord.read(var5, new XMLRecordReader(new InputStreamReader(var7, "UTF-8")));
               if (var8 != null) {
                  var4 = new PeriodFormatterData(var1, var8);
               }
            } catch (UnsupportedEncodingException var10) {
               throw new MissingResourceException("Unhandled Encoding for resource " + var11, var11, "");
            }

            this.cache.put(var1, var4);
         }

         this.lastData = var4;
         this.lastLocale = var1;
         return var4;
      }
   }

   public Collection getAvailableLocales() {
      return this.availableLocales;
   }
}
