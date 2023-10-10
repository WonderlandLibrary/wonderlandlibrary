package com.ibm.icu.text;

import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;

public class RuleBasedNumberFormat extends NumberFormat {
   static final long serialVersionUID = -7664252765575395068L;
   public static final int SPELLOUT = 1;
   public static final int ORDINAL = 2;
   public static final int DURATION = 3;
   public static final int NUMBERING_SYSTEM = 4;
   private transient NFRuleSet[] ruleSets;
   private transient String[] ruleSetDescriptions;
   private transient NFRuleSet defaultRuleSet;
   private ULocale locale;
   private transient RbnfLenientScannerProvider scannerProvider;
   private transient boolean lookedForScanner;
   private transient DecimalFormatSymbols decimalFormatSymbols;
   private transient DecimalFormat decimalFormat;
   private boolean lenientParse;
   private transient String lenientParseRules;
   private transient String postProcessRules;
   private transient RBNFPostProcessor postProcessor;
   private Map ruleSetDisplayNames;
   private String[] publicRuleSetNames;
   private static final boolean DEBUG = ICUDebug.enabled("rbnf");
   private static final String[] rulenames = new String[]{"SpelloutRules", "OrdinalRules", "DurationRules", "NumberingSystemRules"};
   private static final String[] locnames = new String[]{"SpelloutLocalizations", "OrdinalLocalizations", "DurationLocalizations", "NumberingSystemLocalizations"};

   public RuleBasedNumberFormat(String var1) {
      this.ruleSets = null;
      this.ruleSetDescriptions = null;
      this.defaultRuleSet = null;
      this.locale = null;
      this.scannerProvider = null;
      this.decimalFormatSymbols = null;
      this.decimalFormat = null;
      this.lenientParse = false;
      this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
      this.init(var1, (String[][])null);
   }

   public RuleBasedNumberFormat(String var1, String[][] var2) {
      this.ruleSets = null;
      this.ruleSetDescriptions = null;
      this.defaultRuleSet = null;
      this.locale = null;
      this.scannerProvider = null;
      this.decimalFormatSymbols = null;
      this.decimalFormat = null;
      this.lenientParse = false;
      this.locale = ULocale.getDefault(ULocale.Category.FORMAT);
      this.init(var1, var2);
   }

   public RuleBasedNumberFormat(String var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   public RuleBasedNumberFormat(String var1, ULocale var2) {
      this.ruleSets = null;
      this.ruleSetDescriptions = null;
      this.defaultRuleSet = null;
      this.locale = null;
      this.scannerProvider = null;
      this.decimalFormatSymbols = null;
      this.decimalFormat = null;
      this.lenientParse = false;
      this.locale = var2;
      this.init(var1, (String[][])null);
   }

   public RuleBasedNumberFormat(String var1, String[][] var2, ULocale var3) {
      this.ruleSets = null;
      this.ruleSetDescriptions = null;
      this.defaultRuleSet = null;
      this.locale = null;
      this.scannerProvider = null;
      this.decimalFormatSymbols = null;
      this.decimalFormat = null;
      this.lenientParse = false;
      this.locale = var3;
      this.init(var1, var2);
   }

   public RuleBasedNumberFormat(Locale var1, int var2) {
      this(ULocale.forLocale(var1), var2);
   }

   public RuleBasedNumberFormat(ULocale var1, int var2) {
      this.ruleSets = null;
      this.ruleSetDescriptions = null;
      this.defaultRuleSet = null;
      this.locale = null;
      this.scannerProvider = null;
      this.decimalFormatSymbols = null;
      this.decimalFormat = null;
      this.lenientParse = false;
      this.locale = var1;
      ICUResourceBundle var3 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/rbnf", var1);
      ULocale var4 = var3.getULocale();
      this.setLocale(var4, var4);
      String var5 = "";
      String[][] var6 = (String[][])null;

      try {
         var5 = var3.getString(rulenames[var2 - 1]);
      } catch (MissingResourceException var12) {
         try {
            ICUResourceBundle var8 = var3.getWithFallback("RBNFRules/" + rulenames[var2 - 1]);

            for(UResourceBundleIterator var9 = var8.getIterator(); var9.hasNext(); var5 = var5.concat(var9.nextString())) {
            }
         } catch (MissingResourceException var11) {
         }
      }

      try {
         UResourceBundle var7 = var3.get(locnames[var2 - 1]);
         var6 = new String[var7.getSize()][];

         for(int var13 = 0; var13 < var6.length; ++var13) {
            var6[var13] = var7.get(var13).getStringArray();
         }
      } catch (MissingResourceException var10) {
      }

      this.init(var5, var6);
   }

   public RuleBasedNumberFormat(int var1) {
      this(ULocale.getDefault(ULocale.Category.FORMAT), var1);
   }

   public Object clone() {
      return super.clone();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof RuleBasedNumberFormat)) {
         return false;
      } else {
         RuleBasedNumberFormat var2 = (RuleBasedNumberFormat)var1;
         if (this.locale.equals(var2.locale) && this.lenientParse == var2.lenientParse) {
            if (this.ruleSets.length != var2.ruleSets.length) {
               return false;
            } else {
               for(int var3 = 0; var3 < this.ruleSets.length; ++var3) {
                  if (!this.ruleSets[var3].equals(var2.ruleSets[var3])) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }

   /** @deprecated */
   public int hashCode() {
      return super.hashCode();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for(int var2 = 0; var2 < this.ruleSets.length; ++var2) {
         var1.append(this.ruleSets[var2].toString());
      }

      return var1.toString();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeUTF(this.toString());
      var1.writeObject(this.locale);
   }

   private void readObject(ObjectInputStream var1) throws IOException {
      String var2 = var1.readUTF();

      ULocale var3;
      try {
         var3 = (ULocale)var1.readObject();
      } catch (Exception var5) {
         var3 = ULocale.getDefault(ULocale.Category.FORMAT);
      }

      RuleBasedNumberFormat var4 = new RuleBasedNumberFormat(var2, var3);
      this.ruleSets = var4.ruleSets;
      this.defaultRuleSet = var4.defaultRuleSet;
      this.publicRuleSetNames = var4.publicRuleSetNames;
      this.decimalFormatSymbols = var4.decimalFormatSymbols;
      this.decimalFormat = var4.decimalFormat;
      this.locale = var4.locale;
   }

   public String[] getRuleSetNames() {
      return (String[])this.publicRuleSetNames.clone();
   }

   public ULocale[] getRuleSetDisplayNameLocales() {
      if (this.ruleSetDisplayNames == null) {
         return null;
      } else {
         Set var1 = this.ruleSetDisplayNames.keySet();
         String[] var2 = (String[])var1.toArray(new String[var1.size()]);
         Arrays.sort(var2, String.CASE_INSENSITIVE_ORDER);
         ULocale[] var3 = new ULocale[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3[var4] = new ULocale(var2[var4]);
         }

         return var3;
      }
   }

   private String[] getNameListForLocale(ULocale var1) {
      if (var1 != null && this.ruleSetDisplayNames != null) {
         String[] var2 = new String[]{var1.getBaseName(), ULocale.getDefault(ULocale.Category.DISPLAY).getBaseName()};

         for(int var3 = 0; var3 < var2.length; ++var3) {
            for(String var4 = var2[var3]; var4.length() > 0; var4 = ULocale.getFallback(var4)) {
               String[] var5 = (String[])this.ruleSetDisplayNames.get(var4);
               if (var5 != null) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   public String[] getRuleSetDisplayNames(ULocale var1) {
      String[] var2 = this.getNameListForLocale(var1);
      if (var2 != null) {
         return (String[])var2.clone();
      } else {
         var2 = this.getRuleSetNames();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3] = var2[var3].substring(1);
         }

         return var2;
      }
   }

   public String[] getRuleSetDisplayNames() {
      return this.getRuleSetDisplayNames(ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String getRuleSetDisplayName(String var1, ULocale var2) {
      String[] var3 = this.publicRuleSetNames;

      for(int var4 = 0; var4 < var3.length; ++var4) {
         if (var3[var4].equals(var1)) {
            String[] var5 = this.getNameListForLocale(var2);
            if (var5 != null) {
               return var5[var4];
            }

            return var3[var4].substring(1);
         }
      }

      throw new IllegalArgumentException("unrecognized rule set name: " + var1);
   }

   public String getRuleSetDisplayName(String var1) {
      return this.getRuleSetDisplayName(var1, ULocale.getDefault(ULocale.Category.DISPLAY));
   }

   public String format(double var1, String var3) throws IllegalArgumentException {
      if (var3.startsWith("%%")) {
         throw new IllegalArgumentException("Can't use internal rule set");
      } else {
         return this.format(var1, this.findRuleSet(var3));
      }
   }

   public String format(long var1, String var3) throws IllegalArgumentException {
      if (var3.startsWith("%%")) {
         throw new IllegalArgumentException("Can't use internal rule set");
      } else {
         return this.format(var1, this.findRuleSet(var3));
      }
   }

   public StringBuffer format(double var1, StringBuffer var3, FieldPosition var4) {
      var3.append(this.format(var1, this.defaultRuleSet));
      return var3;
   }

   public StringBuffer format(long var1, StringBuffer var3, FieldPosition var4) {
      var3.append(this.format(var1, this.defaultRuleSet));
      return var3;
   }

   public StringBuffer format(BigInteger var1, StringBuffer var2, FieldPosition var3) {
      return this.format(new BigDecimal(var1), var2, var3);
   }

   public StringBuffer format(java.math.BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      return this.format(new BigDecimal(var1), var2, var3);
   }

   public StringBuffer format(BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      return this.format(var1.doubleValue(), var2, var3);
   }

   public Number parse(String var1, ParsePosition var2) {
      String var3 = var1.substring(var2.getIndex());
      ParsePosition var4 = new ParsePosition(0);
      Number var5 = null;
      Object var6 = 0L;
      ParsePosition var7 = new ParsePosition(var4.getIndex());

      for(int var8 = this.ruleSets.length - 1; var8 >= 0; --var8) {
         if (this.ruleSets[var8].isPublic() && this.ruleSets[var8].isParseable()) {
            var5 = this.ruleSets[var8].parse(var3, var4, Double.MAX_VALUE);
            if (var4.getIndex() > var7.getIndex()) {
               var6 = var5;
               var7.setIndex(var4.getIndex());
            }

            if (var7.getIndex() == var3.length()) {
               break;
            }

            var4.setIndex(0);
         }
      }

      var2.setIndex(var2.getIndex() + var7.getIndex());
      return (Number)var6;
   }

   public void setLenientParseMode(boolean var1) {
      this.lenientParse = var1;
   }

   public boolean lenientParseEnabled() {
      return this.lenientParse;
   }

   public void setLenientScannerProvider(RbnfLenientScannerProvider var1) {
      this.scannerProvider = var1;
   }

   public RbnfLenientScannerProvider getLenientScannerProvider() {
      if (this.scannerProvider == null && this.lenientParse && !this.lookedForScanner) {
         try {
            this.lookedForScanner = true;
            Class var1 = Class.forName("com.ibm.icu.text.RbnfScannerProviderImpl");
            RbnfLenientScannerProvider var2 = (RbnfLenientScannerProvider)var1.newInstance();
            this.setLenientScannerProvider(var2);
         } catch (Exception var3) {
         }
      }

      return this.scannerProvider;
   }

   public void setDefaultRuleSet(String var1) {
      if (var1 != null) {
         if (var1.startsWith("%%")) {
            throw new IllegalArgumentException("cannot use private rule set: " + var1);
         }

         this.defaultRuleSet = this.findRuleSet(var1);
      } else if (this.publicRuleSetNames.length > 0) {
         this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
      } else {
         this.defaultRuleSet = null;
         int var2 = this.ruleSets.length;

         String var3;
         do {
            --var2;
            if (var2 < 0) {
               var2 = this.ruleSets.length;

               do {
                  --var2;
                  if (var2 < 0) {
                     return;
                  }
               } while(!this.ruleSets[var2].isPublic());

               this.defaultRuleSet = this.ruleSets[var2];
               return;
            }

            var3 = this.ruleSets[var2].getName();
         } while(!var3.equals("%spellout-numbering") && !var3.equals("%digits-ordinal") && !var3.equals("%duration"));

         this.defaultRuleSet = this.ruleSets[var2];
         return;
      }

   }

   public String getDefaultRuleSetName() {
      return this.defaultRuleSet != null && this.defaultRuleSet.isPublic() ? this.defaultRuleSet.getName() : "";
   }

   public void setDecimalFormatSymbols(DecimalFormatSymbols var1) {
      if (var1 != null) {
         this.decimalFormatSymbols = (DecimalFormatSymbols)var1.clone();
         if (this.decimalFormat != null) {
            this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
         }

         for(int var2 = 0; var2 < this.ruleSets.length; ++var2) {
            this.ruleSets[var2].parseRules(this.ruleSetDescriptions[var2], this);
         }
      }

   }

   NFRuleSet getDefaultRuleSet() {
      return this.defaultRuleSet;
   }

   RbnfLenientScanner getLenientScanner() {
      if (this.lenientParse) {
         RbnfLenientScannerProvider var1 = this.getLenientScannerProvider();
         if (var1 != null) {
            return var1.get(this.locale, this.lenientParseRules);
         }
      }

      return null;
   }

   DecimalFormatSymbols getDecimalFormatSymbols() {
      if (this.decimalFormatSymbols == null) {
         this.decimalFormatSymbols = new DecimalFormatSymbols(this.locale);
      }

      return this.decimalFormatSymbols;
   }

   DecimalFormat getDecimalFormat() {
      if (this.decimalFormat == null) {
         this.decimalFormat = (DecimalFormat)NumberFormat.getInstance(this.locale);
         if (this.decimalFormatSymbols != null) {
            this.decimalFormat.setDecimalFormatSymbols(this.decimalFormatSymbols);
         }
      }

      return this.decimalFormat;
   }

   private String extractSpecial(StringBuilder var1, String var2) {
      String var3 = null;
      int var4 = var1.indexOf(var2);
      if (var4 != -1 && (var4 == 0 || var1.charAt(var4 - 1) == ';')) {
         int var5 = var1.indexOf(";%", var4);
         if (var5 == -1) {
            var5 = var1.length() - 1;
         }

         int var6;
         for(var6 = var4 + var2.length(); var6 < var5 && PatternProps.isWhiteSpace(var1.charAt(var6)); ++var6) {
         }

         var3 = var1.substring(var6, var5);
         var1.delete(var4, var5 + 1);
      }

      return var3;
   }

   private void init(String var1, String[][] var2) {
      this.initLocalizations(var2);
      StringBuilder var3 = this.stripWhitespace(var1);
      this.lenientParseRules = this.extractSpecial(var3, "%%lenient-parse:");
      this.postProcessRules = this.extractSpecial(var3, "%%post-process:");
      int var4 = 0;

      int var5;
      for(var5 = var3.indexOf(";%"); var5 != -1; var5 = var3.indexOf(";%", var5)) {
         ++var4;
         ++var5;
      }

      ++var4;
      this.ruleSets = new NFRuleSet[var4];
      this.ruleSetDescriptions = new String[var4];
      var5 = 0;
      int var6 = 0;

      for(int var7 = var3.indexOf(";%"); var7 != -1; var7 = var3.indexOf(";%", var6)) {
         this.ruleSetDescriptions[var5] = var3.substring(var6, var7 + 1);
         this.ruleSets[var5] = new NFRuleSet(this.ruleSetDescriptions, var5);
         ++var5;
         var6 = var7 + 1;
      }

      this.ruleSetDescriptions[var5] = var3.substring(var6);
      this.ruleSets[var5] = new NFRuleSet(this.ruleSetDescriptions, var5);
      boolean var14 = false;
      int var8 = this.ruleSets.length;
      this.defaultRuleSet = this.ruleSets[this.ruleSets.length - 1];

      while(true) {
         --var8;
         if (var8 < 0) {
            break;
         }

         String var9 = this.ruleSets[var8].getName();
         if (var9.equals("%spellout-numbering") || var9.equals("%digits-ordinal") || var9.equals("%duration")) {
            this.defaultRuleSet = this.ruleSets[var8];
            var14 = true;
            break;
         }
      }

      int var15;
      if (!var14) {
         for(var15 = this.ruleSets.length - 1; var15 >= 0; --var15) {
            if (!this.ruleSets[var15].getName().startsWith("%%")) {
               this.defaultRuleSet = this.ruleSets[var15];
               break;
            }
         }
      }

      for(var15 = 0; var15 < this.ruleSets.length; ++var15) {
         this.ruleSets[var15].parseRules(this.ruleSetDescriptions[var15], this);
      }

      var15 = 0;

      for(int var10 = 0; var10 < this.ruleSets.length; ++var10) {
         if (!this.ruleSets[var10].getName().startsWith("%%")) {
            ++var15;
         }
      }

      String[] var16 = new String[var15];
      var15 = 0;

      int var11;
      for(var11 = this.ruleSets.length - 1; var11 >= 0; --var11) {
         if (!this.ruleSets[var11].getName().startsWith("%%")) {
            var16[var15++] = this.ruleSets[var11].getName();
         }
      }

      if (this.publicRuleSetNames != null) {
         label64:
         for(var11 = 0; var11 < this.publicRuleSetNames.length; ++var11) {
            String var12 = this.publicRuleSetNames[var11];

            for(int var13 = 0; var13 < var16.length; ++var13) {
               if (var12.equals(var16[var13])) {
                  continue label64;
               }
            }

            throw new IllegalArgumentException("did not find public rule set: " + var12);
         }

         this.defaultRuleSet = this.findRuleSet(this.publicRuleSetNames[0]);
      } else {
         this.publicRuleSetNames = var16;
      }

   }

   private void initLocalizations(String[][] var1) {
      if (var1 != null) {
         this.publicRuleSetNames = (String[])var1[0].clone();
         HashMap var2 = new HashMap();

         for(int var3 = 1; var3 < var1.length; ++var3) {
            String[] var4 = var1[var3];
            String var5 = var4[0];
            String[] var6 = new String[var4.length - 1];
            if (var6.length != this.publicRuleSetNames.length) {
               throw new IllegalArgumentException("public name length: " + this.publicRuleSetNames.length + " != localized names[" + var3 + "] length: " + var6.length);
            }

            System.arraycopy(var4, 1, var6, 0, var6.length);
            var2.put(var5, var6);
         }

         if (!var2.isEmpty()) {
            this.ruleSetDisplayNames = var2;
         }
      }

   }

   private StringBuilder stripWhitespace(String var1) {
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;

      while(var3 != -1 && var3 < var1.length()) {
         while(var3 < var1.length() && PatternProps.isWhiteSpace(var1.charAt(var3))) {
            ++var3;
         }

         if (var3 < var1.length() && var1.charAt(var3) == ';') {
            ++var3;
         } else {
            int var4 = var1.indexOf(59, var3);
            if (var4 == -1) {
               var2.append(var1.substring(var3));
               var3 = -1;
            } else if (var4 < var1.length()) {
               var2.append(var1.substring(var3, var4 + 1));
               var3 = var4 + 1;
            } else {
               var3 = -1;
            }
         }
      }

      return var2;
   }

   private String format(double var1, NFRuleSet var3) {
      StringBuffer var4 = new StringBuffer();
      var3.format(var1, var4, 0);
      this.postProcess(var4, var3);
      return var4.toString();
   }

   private String format(long var1, NFRuleSet var3) {
      StringBuffer var4 = new StringBuffer();
      var3.format(var1, var4, 0);
      this.postProcess(var4, var3);
      return var4.toString();
   }

   private void postProcess(StringBuffer var1, NFRuleSet var2) {
      if (this.postProcessRules != null) {
         if (this.postProcessor == null) {
            int var3 = this.postProcessRules.indexOf(";");
            if (var3 == -1) {
               var3 = this.postProcessRules.length();
            }

            String var4 = this.postProcessRules.substring(0, var3).trim();

            try {
               Class var5 = Class.forName(var4);
               this.postProcessor = (RBNFPostProcessor)var5.newInstance();
               this.postProcessor.init(this, this.postProcessRules);
            } catch (Exception var6) {
               if (DEBUG) {
                  System.out.println("could not locate " + var4 + ", error " + var6.getClass().getName() + ", " + var6.getMessage());
               }

               this.postProcessor = null;
               this.postProcessRules = null;
               return;
            }
         }

         this.postProcessor.process(var1, var2);
      }

   }

   NFRuleSet findRuleSet(String var1) throws IllegalArgumentException {
      for(int var2 = 0; var2 < this.ruleSets.length; ++var2) {
         if (this.ruleSets[var2].getName().equals(var1)) {
            return this.ruleSets[var2];
         }
      }

      throw new IllegalArgumentException("No rule set named " + var1);
   }
}
