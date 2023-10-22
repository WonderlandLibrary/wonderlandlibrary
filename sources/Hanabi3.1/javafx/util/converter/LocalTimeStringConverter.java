package javafx.util.converter;

import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;
import java.util.Locale;
import javafx.util.StringConverter;

public class LocalTimeStringConverter extends StringConverter {
   LocalDateTimeStringConverter.LdtConverter ldtConverter;

   public LocalTimeStringConverter() {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalTimeStringConverter(FormatStyle var1) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, (FormatStyle)null, var1, (Locale)null, (Chronology)null);
   }

   public LocalTimeStringConverter(FormatStyle var1, Locale var2) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalTime.class, (DateTimeFormatter)null, (DateTimeFormatter)null, (FormatStyle)null, var1, var2, (Chronology)null);
   }

   public LocalTimeStringConverter(DateTimeFormatter var1, DateTimeFormatter var2) {
      this.ldtConverter = new LocalDateTimeStringConverter.LdtConverter(LocalTime.class, var1, var2, (FormatStyle)null, (FormatStyle)null, (Locale)null, (Chronology)null);
   }

   public LocalTime fromString(String var1) {
      return (LocalTime)this.ldtConverter.fromString(var1);
   }

   public String toString(LocalTime var1) {
      return this.ldtConverter.toString((Temporal)var1);
   }
}
