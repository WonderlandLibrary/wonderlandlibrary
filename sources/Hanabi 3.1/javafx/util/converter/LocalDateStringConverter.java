package javafx.util.converter;

import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Locale;
import javafx.util.StringConverter;

public class LocalDateStringConverter extends StringConverter {
   LocalDateTimeStringConverter.LdtConverter ldtConverter;

   public LocalDateStringConverter() {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalDate.class, (DateTimeFormatter)null, (DateTimeFormatter)null, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalDateStringConverter(FormatStyle var1) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalDate.class, (DateTimeFormatter)null, (DateTimeFormatter)null, var1, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalDateStringConverter(DateTimeFormatter var1, DateTimeFormatter var2) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalDate.class, var1, var2, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalDateStringConverter(FormatStyle var1, Locale var2, Chronology var3) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalDate.class, (DateTimeFormatter)null, (DateTimeFormatter)null, var1, (FormatStyle)null, var2, var3);
   }

   public LocalDate fromString(String var1) {
      return (LocalDate)this.ldtConverter.fromString(var1);
   }

   public String toString(LocalDate var1) {
      return this.ldtConverter.toString((Temporal)var1);
   }
}
