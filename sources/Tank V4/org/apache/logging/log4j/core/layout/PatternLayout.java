package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.OptionConverter;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;

@Plugin(
   name = "PatternLayout",
   category = "Core",
   elementType = "layout",
   printObject = true
)
public final class PatternLayout extends AbstractStringLayout {
   public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";
   public static final String TTCC_CONVERSION_PATTERN = "%r [%t] %p %c %x - %m%n";
   public static final String SIMPLE_CONVERSION_PATTERN = "%d [%t] %p %c - %m%n";
   public static final String KEY = "Converter";
   private List formatters;
   private final String conversionPattern;
   private final Configuration config;
   private final RegexReplacement replace;
   private final boolean alwaysWriteExceptions;

   private PatternLayout(Configuration var1, RegexReplacement var2, String var3, Charset var4, boolean var5) {
      super(var4);
      this.replace = var2;
      this.conversionPattern = var3;
      this.config = var1;
      this.alwaysWriteExceptions = var5;
      PatternParser var6 = createPatternParser(var1);
      this.formatters = var6.parse(var3 == null ? "%m%n" : var3, this.alwaysWriteExceptions);
   }

   public void setConversionPattern(String var1) {
      String var2 = OptionConverter.convertSpecialChars(var1);
      if (var2 != null) {
         PatternParser var3 = createPatternParser(this.config);
         this.formatters = var3.parse(var2, this.alwaysWriteExceptions);
      }
   }

   public String getConversionPattern() {
      return this.conversionPattern;
   }

   public Map getContentFormat() {
      HashMap var1 = new HashMap();
      var1.put("structured", "false");
      var1.put("formatType", "conversion");
      var1.put("format", this.conversionPattern);
      return var1;
   }

   public String toSerializable(LogEvent var1) {
      StringBuilder var2 = new StringBuilder();
      Iterator var3 = this.formatters.iterator();

      while(var3.hasNext()) {
         PatternFormatter var4 = (PatternFormatter)var3.next();
         var4.format(var1, var2);
      }

      String var5 = var2.toString();
      if (this.replace != null) {
         var5 = this.replace.format(var5);
      }

      return var5;
   }

   public static PatternParser createPatternParser(Configuration var0) {
      if (var0 == null) {
         return new PatternParser(var0, "Converter", LogEventPatternConverter.class);
      } else {
         PatternParser var1 = (PatternParser)var0.getComponent("Converter");
         if (var1 == null) {
            var1 = new PatternParser(var0, "Converter", LogEventPatternConverter.class);
            var0.addComponent("Converter", var1);
            var1 = (PatternParser)var0.getComponent("Converter");
         }

         return var1;
      }
   }

   public String toString() {
      return this.conversionPattern;
   }

   @PluginFactory
   public static PatternLayout createLayout(@PluginAttribute("pattern") String var0, @PluginConfiguration Configuration var1, @PluginElement("Replace") RegexReplacement var2, @PluginAttribute("charset") String var3, @PluginAttribute("alwaysWriteExceptions") String var4) {
      Charset var5 = Charsets.getSupportedCharset(var3);
      boolean var6 = Booleans.parseBoolean(var4, true);
      return new PatternLayout(var1, var2, var0 == null ? "%m%n" : var0, var5, var6);
   }

   public Serializable toSerializable(LogEvent var1) {
      return this.toSerializable(var1);
   }
}
