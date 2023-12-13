package javafx.util.converter;

import com.sun.javafx.binding.Logging;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Locale.Category;
import javafx.util.StringConverter;

public class LocalDateTimeStringConverter extends StringConverter {
   LdtConverter ldtConverter;

   public LocalDateTimeStringConverter() {
      this.ldtConverter = new LdtConverter(LocalDateTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalDateTimeStringConverter(FormatStyle var1, FormatStyle var2) {
      this.ldtConverter = new LdtConverter(LocalDateTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, var1, var2, (Locale)null, (Chronology)null);
   }

   public LocalDateTimeStringConverter(DateTimeFormatter var1, DateTimeFormatter var2) {
      this.ldtConverter = new LdtConverter(LocalDateTime.class, var1, var2, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalDateTimeStringConverter(FormatStyle var1, FormatStyle var2, Locale var3, Chronology var4) {
      this.ldtConverter = new LdtConverter(LocalDateTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, var1, var2, var3, var4);
   }

   public LocalDateTime fromString(String var1) {
      return (LocalDateTime)this.ldtConverter.fromString(var1);
   }

   public String toString(LocalDateTime var1) {
      return this.ldtConverter.toString((Temporal)var1);
   }

   static class LdtConverter extends StringConverter {
      private Class type;
      Locale locale;
      Chronology chronology;
      DateTimeFormatter formatter;
      DateTimeFormatter parser;
      FormatStyle dateStyle;
      FormatStyle timeStyle;

      LdtConverter(Class var1, DateTimeFormatter var2, DateTimeFormatter var3, FormatStyle var4, FormatStyle var5, Locale var6, Chronology var7) {
         this.type = var1;
         this.formatter = var2;
         this.parser = var3 != null ? var3 : var2;
         this.locale = var6 != null ? var6 : Locale.getDefault(Category.FORMAT);
         this.chronology = (Chronology)(var7 != null ? var7 : IsoChronology.INSTANCE);
         if (var1 == LocalDate.class || var1 == LocalDateTime.class) {
            this.dateStyle = var4 != null ? var4 : FormatStyle.SHORT;
         }

         if (var1 == LocalTime.class || var1 == LocalDateTime.class) {
            this.timeStyle = var5 != null ? var5 : FormatStyle.SHORT;
         }

      }

      public Temporal fromString(String var1) {
         if (var1 != null && !var1.isEmpty()) {
            var1 = var1.trim();
            if (this.parser == null) {
               this.parser = this.getDefaultParser();
            }

            TemporalAccessor var2 = this.parser.parse(var1);
            if (this.type == LocalDate.class) {
               return LocalDate.from(this.chronology.date(var2));
            } else {
               return (Temporal)(this.type == LocalTime.class ? LocalTime.from(var2) : LocalDateTime.from(this.chronology.localDateTime(var2)));
            }
         } else {
            return null;
         }
      }

      public String toString(Temporal var1) {
         if (var1 == null) {
            return "";
         } else {
            if (this.formatter == null) {
               this.formatter = this.getDefaultFormatter();
            }

            Object var2;
            if (var1 instanceof LocalDate) {
               try {
                  var2 = this.chronology.date(var1);
               } catch (DateTimeException var4) {
                  Logging.getLogger().warning("Converting LocalDate " + var1 + " to " + this.chronology + " failed, falling back to IsoChronology.", var4);
                  this.chronology = IsoChronology.INSTANCE;
                  var2 = (LocalDate)var1;
               }

               return this.formatter.format((TemporalAccessor)var2);
            } else if (var1 instanceof LocalDateTime) {
               try {
                  var2 = this.chronology.localDateTime(var1);
               } catch (DateTimeException var5) {
                  Logging.getLogger().warning("Converting LocalDateTime " + var1 + " to " + this.chronology + " failed, falling back to IsoChronology.", var5);
                  this.chronology = IsoChronology.INSTANCE;
                  var2 = (LocalDateTime)var1;
               }

               return this.formatter.format((TemporalAccessor)var2);
            } else {
               return this.formatter.format(var1);
            }
         }
      }

      private DateTimeFormatter getDefaultParser() {
         String var1 = DateTimeFormatterBuilder.getLocalizedDateTimePattern(this.dateStyle, this.timeStyle, this.chronology, this.locale);
         return (new DateTimeFormatterBuilder()).parseLenient().appendPattern(var1).toFormatter().withChronology(this.chronology).withDecimalStyle(DecimalStyle.of(this.locale));
      }

      private DateTimeFormatter getDefaultFormatter() {
         DateTimeFormatter var1;
         if (this.dateStyle != null && this.timeStyle != null) {
            var1 = DateTimeFormatter.ofLocalizedDateTime(this.dateStyle, this.timeStyle);
         } else if (this.dateStyle != null) {
            var1 = DateTimeFormatter.ofLocalizedDate(this.dateStyle);
         } else {
            var1 = DateTimeFormatter.ofLocalizedTime(this.timeStyle);
         }

         var1 = var1.withLocale(this.locale).withChronology(this.chronology).withDecimalStyle(DecimalStyle.of(this.locale));
         if (this.dateStyle != null) {
            var1 = this.fixFourDigitYear(var1, this.dateStyle, this.timeStyle, this.chronology, this.locale);
         }

         return var1;
      }

      private DateTimeFormatter fixFourDigitYear(DateTimeFormatter var1, FormatStyle var2, FormatStyle var3, Chronology var4, Locale var5) {
         String var6 = DateTimeFormatterBuilder.getLocalizedDateTimePattern(var2, var3, var4, var5);
         if (var6.contains("yy") && !var6.contains("yyy")) {
            String var7 = var6.replace("yy", "yyyy");
            var1 = DateTimeFormatter.ofPattern(var7).withDecimalStyle(DecimalStyle.of(var5));
         }

         return var1;
      }
   }
}
