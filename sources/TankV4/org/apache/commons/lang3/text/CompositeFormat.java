package org.apache.commons.lang3.text;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class CompositeFormat extends Format {
   private static final long serialVersionUID = -4329119827877627683L;
   private final Format parser;
   private final Format formatter;

   public CompositeFormat(Format var1, Format var2) {
      this.parser = var1;
      this.formatter = var2;
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      return this.formatter.format(var1, var2, var3);
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.parser.parseObject(var1, var2);
   }

   public Format getParser() {
      return this.parser;
   }

   public Format getFormatter() {
      return this.formatter;
   }

   public String reformat(String var1) throws ParseException {
      return this.format(this.parseObject(var1));
   }
}
