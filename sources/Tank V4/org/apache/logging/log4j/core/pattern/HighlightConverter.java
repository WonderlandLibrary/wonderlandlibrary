package org.apache.logging.log4j.core.pattern;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Plugin(
   name = "highlight",
   category = "Converter"
)
@ConverterKeys({"highlight"})
public final class HighlightConverter extends LogEventPatternConverter {
   private static final EnumMap DEFAULT_STYLES = new EnumMap(Level.class);
   private static final EnumMap LOGBACK_STYLES = new EnumMap(Level.class);
   private static final String STYLE_KEY = "STYLE";
   private static final String STYLE_KEY_DEFAULT = "DEFAULT";
   private static final String STYLE_KEY_LOGBACK = "LOGBACK";
   private static final Map STYLES = new HashMap();
   private final EnumMap levelStyles;
   private final List patternFormatters;

   private static EnumMap createLevelStyleMap(String[] var0) {
      if (var0.length < 2) {
         return DEFAULT_STYLES;
      } else {
         Map var1 = AnsiEscape.createMap(var0[1], new String[]{"STYLE"});
         EnumMap var2 = new EnumMap(DEFAULT_STYLES);
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            String var5 = ((String)var4.getKey()).toUpperCase(Locale.ENGLISH);
            String var6 = (String)var4.getValue();
            if ("STYLE".equalsIgnoreCase(var5)) {
               EnumMap var7 = (EnumMap)STYLES.get(var6.toUpperCase(Locale.ENGLISH));
               if (var7 == null) {
                  LOGGER.error("Unknown level style: " + var6 + ". Use one of " + Arrays.toString(STYLES.keySet().toArray()));
               } else {
                  var2.putAll(var7);
               }
            } else {
               Level var8 = Level.valueOf(var5);
               if (var8 == null) {
                  LOGGER.error("Unknown level name: " + var5 + ". Use one of " + Arrays.toString(DEFAULT_STYLES.keySet().toArray()));
               } else {
                  var2.put(var8, var6);
               }
            }
         }

         return var2;
      }
   }

   public static HighlightConverter newInstance(Configuration var0, String[] var1) {
      if (var1.length < 1) {
         LOGGER.error("Incorrect number of options on style. Expected at least 1, received " + var1.length);
         return null;
      } else if (var1[0] == null) {
         LOGGER.error("No pattern supplied on style");
         return null;
      } else {
         PatternParser var2 = PatternLayout.createPatternParser(var0);
         List var3 = var2.parse(var1[0]);
         return new HighlightConverter(var3, createLevelStyleMap(var1));
      }
   }

   private HighlightConverter(List var1, EnumMap var2) {
      super("style", "style");
      this.patternFormatters = var1;
      this.levelStyles = var2;
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = this.patternFormatters.iterator();

      while(var4.hasNext()) {
         PatternFormatter var5 = (PatternFormatter)var4.next();
         var5.format(var1, var3);
      }

      if (var3.length() > 0) {
         var2.append((String)this.levelStyles.get(var1.getLevel())).append(var3.toString()).append(AnsiEscape.getDefaultStyle());
      }

   }

   public boolean handlesThrowable() {
      Iterator var1 = this.patternFormatters.iterator();

      PatternFormatter var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (PatternFormatter)var1.next();
      } while(!var2.handlesThrowable());

      return true;
   }

   static {
      DEFAULT_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BRIGHT", "RED"));
      DEFAULT_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
      DEFAULT_STYLES.put(Level.WARN, AnsiEscape.createSequence("YELLOW"));
      DEFAULT_STYLES.put(Level.INFO, AnsiEscape.createSequence("GREEN"));
      DEFAULT_STYLES.put(Level.DEBUG, AnsiEscape.createSequence("CYAN"));
      DEFAULT_STYLES.put(Level.TRACE, AnsiEscape.createSequence("BLACK"));
      LOGBACK_STYLES.put(Level.FATAL, AnsiEscape.createSequence("BLINK", "BRIGHT", "RED"));
      LOGBACK_STYLES.put(Level.ERROR, AnsiEscape.createSequence("BRIGHT", "RED"));
      LOGBACK_STYLES.put(Level.WARN, AnsiEscape.createSequence("RED"));
      LOGBACK_STYLES.put(Level.INFO, AnsiEscape.createSequence("BLUE"));
      LOGBACK_STYLES.put(Level.DEBUG, AnsiEscape.createSequence((String[])null));
      LOGBACK_STYLES.put(Level.TRACE, AnsiEscape.createSequence((String[])null));
      STYLES.put("DEFAULT", DEFAULT_STYLES);
      STYLES.put("LOGBACK", LOGBACK_STYLES);
   }
}
