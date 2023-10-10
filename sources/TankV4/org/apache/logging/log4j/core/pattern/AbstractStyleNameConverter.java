package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

public abstract class AbstractStyleNameConverter extends LogEventPatternConverter {
   private final List formatters;
   private final String style;

   protected AbstractStyleNameConverter(String var1, List var2, String var3) {
      super(var1, "style");
      this.formatters = var2;
      this.style = var3;
   }

   protected static AbstractStyleNameConverter newInstance(Class var0, String var1, Configuration var2, String[] var3) {
      List var4 = toPatternFormatterList(var2, var3);
      if (var4 == null) {
         return null;
      } else {
         try {
            Constructor var5 = var0.getConstructor(List.class, String.class);
            return (AbstractStyleNameConverter)var5.newInstance(var4, AnsiEscape.createSequence(var1));
         } catch (SecurityException var6) {
            LOGGER.error((String)var6.toString(), (Throwable)var6);
         } catch (NoSuchMethodException var7) {
            LOGGER.error((String)var7.toString(), (Throwable)var7);
         } catch (IllegalArgumentException var8) {
            LOGGER.error((String)var8.toString(), (Throwable)var8);
         } catch (InstantiationException var9) {
            LOGGER.error((String)var9.toString(), (Throwable)var9);
         } catch (IllegalAccessException var10) {
            LOGGER.error((String)var10.toString(), (Throwable)var10);
         } catch (InvocationTargetException var11) {
            LOGGER.error((String)var11.toString(), (Throwable)var11);
         }

         return null;
      }
   }

   private static List toPatternFormatterList(Configuration var0, String[] var1) {
      if (var1.length != 0 && var1[0] != null) {
         PatternParser var2 = PatternLayout.createPatternParser(var0);
         if (var2 == null) {
            LOGGER.error("No PatternParser created for config=" + var0 + ", options=" + Arrays.toString(var1));
            return null;
         } else {
            return var2.parse(var1[0]);
         }
      } else {
         LOGGER.error("No pattern supplied on style for config=" + var0);
         return null;
      }
   }

   public void format(LogEvent var1, StringBuilder var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var4 = this.formatters.iterator();

      while(var4.hasNext()) {
         PatternFormatter var5 = (PatternFormatter)var4.next();
         var5.format(var1, var3);
      }

      if (var3.length() > 0) {
         var2.append(this.style).append(var3.toString()).append(AnsiEscape.getDefaultStyle());
      }

   }

   @Plugin(
      name = "yellow",
      category = "Converter"
   )
   @ConverterKeys({"yellow"})
   public static final class Yellow extends AbstractStyleNameConverter {
      protected static final String NAME = "yellow";

      public Yellow(List var1, String var2) {
         super("yellow", var1, var2);
      }

      public static AbstractStyleNameConverter.Yellow newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Yellow)newInstance(AbstractStyleNameConverter.Yellow.class, "yellow", var0, var1);
      }
   }

   @Plugin(
      name = "white",
      category = "Converter"
   )
   @ConverterKeys({"white"})
   public static final class White extends AbstractStyleNameConverter {
      protected static final String NAME = "white";

      public White(List var1, String var2) {
         super("white", var1, var2);
      }

      public static AbstractStyleNameConverter.White newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.White)newInstance(AbstractStyleNameConverter.White.class, "white", var0, var1);
      }
   }

   @Plugin(
      name = "red",
      category = "Converter"
   )
   @ConverterKeys({"red"})
   public static final class Red extends AbstractStyleNameConverter {
      protected static final String NAME = "red";

      public Red(List var1, String var2) {
         super("red", var1, var2);
      }

      public static AbstractStyleNameConverter.Red newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Red)newInstance(AbstractStyleNameConverter.Red.class, "red", var0, var1);
      }
   }

   @Plugin(
      name = "magenta",
      category = "Converter"
   )
   @ConverterKeys({"magenta"})
   public static final class Magenta extends AbstractStyleNameConverter {
      protected static final String NAME = "magenta";

      public Magenta(List var1, String var2) {
         super("magenta", var1, var2);
      }

      public static AbstractStyleNameConverter.Magenta newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Magenta)newInstance(AbstractStyleNameConverter.Magenta.class, "magenta", var0, var1);
      }
   }

   @Plugin(
      name = "green",
      category = "Converter"
   )
   @ConverterKeys({"green"})
   public static final class Green extends AbstractStyleNameConverter {
      protected static final String NAME = "green";

      public Green(List var1, String var2) {
         super("green", var1, var2);
      }

      public static AbstractStyleNameConverter.Green newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Green)newInstance(AbstractStyleNameConverter.Green.class, "green", var0, var1);
      }
   }

   @Plugin(
      name = "cyan",
      category = "Converter"
   )
   @ConverterKeys({"cyan"})
   public static final class Cyan extends AbstractStyleNameConverter {
      protected static final String NAME = "cyan";

      public Cyan(List var1, String var2) {
         super("cyan", var1, var2);
      }

      public static AbstractStyleNameConverter.Cyan newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Cyan)newInstance(AbstractStyleNameConverter.Cyan.class, "cyan", var0, var1);
      }
   }

   @Plugin(
      name = "blue",
      category = "Converter"
   )
   @ConverterKeys({"blue"})
   public static final class Blue extends AbstractStyleNameConverter {
      protected static final String NAME = "blue";

      public Blue(List var1, String var2) {
         super("blue", var1, var2);
      }

      public static AbstractStyleNameConverter.Blue newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Blue)newInstance(AbstractStyleNameConverter.Blue.class, "blue", var0, var1);
      }
   }

   @Plugin(
      name = "black",
      category = "Converter"
   )
   @ConverterKeys({"black"})
   public static final class Black extends AbstractStyleNameConverter {
      protected static final String NAME = "black";

      public Black(List var1, String var2) {
         super("black", var1, var2);
      }

      public static AbstractStyleNameConverter.Black newInstance(Configuration var0, String[] var1) {
         return (AbstractStyleNameConverter.Black)newInstance(AbstractStyleNameConverter.Black.class, "black", var0, var1);
      }
   }
}
