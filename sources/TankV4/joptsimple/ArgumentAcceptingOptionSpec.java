package joptsimple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import joptsimple.internal.Objects;
import joptsimple.internal.Reflection;
import joptsimple.internal.Strings;

public abstract class ArgumentAcceptingOptionSpec extends AbstractOptionSpec {
   private static final char NIL_VALUE_SEPARATOR = '\u0000';
   private boolean optionRequired;
   private final boolean argumentRequired;
   private ValueConverter converter;
   private String argumentDescription = "";
   private String valueSeparator = String.valueOf('\u0000');
   private final List defaultValues = new ArrayList();

   ArgumentAcceptingOptionSpec(String var1, boolean var2) {
      super(var1);
      this.argumentRequired = var2;
   }

   ArgumentAcceptingOptionSpec(Collection var1, boolean var2, String var3) {
      super(var1, var3);
      this.argumentRequired = var2;
   }

   public final ArgumentAcceptingOptionSpec ofType(Class var1) {
      return this.withValuesConvertedBy(Reflection.findConverter(var1));
   }

   public final ArgumentAcceptingOptionSpec withValuesConvertedBy(ValueConverter var1) {
      if (var1 == null) {
         throw new NullPointerException("illegal null converter");
      } else {
         this.converter = var1;
         return this;
      }
   }

   public final ArgumentAcceptingOptionSpec describedAs(String var1) {
      this.argumentDescription = var1;
      return this;
   }

   public final ArgumentAcceptingOptionSpec withValuesSeparatedBy(char var1) {
      if (var1 == 0) {
         throw new IllegalArgumentException("cannot use U+0000 as separator");
      } else {
         this.valueSeparator = String.valueOf(var1);
         return this;
      }
   }

   public final ArgumentAcceptingOptionSpec withValuesSeparatedBy(String var1) {
      if (var1.indexOf(0) != -1) {
         throw new IllegalArgumentException("cannot use U+0000 in separator");
      } else {
         this.valueSeparator = var1;
         return this;
      }
   }

   public ArgumentAcceptingOptionSpec defaultsTo(Object var1, Object... var2) {
      this.addDefaultValue(var1);
      this.defaultsTo(var2);
      return this;
   }

   public ArgumentAcceptingOptionSpec defaultsTo(Object[] var1) {
      Object[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         this.addDefaultValue(var5);
      }

      return this;
   }

   public ArgumentAcceptingOptionSpec required() {
      this.optionRequired = true;
      return this;
   }

   public boolean isRequired() {
      return this.optionRequired;
   }

   private void addDefaultValue(Object var1) {
      Objects.ensureNotNull(var1);
      this.defaultValues.add(var1);
   }

   final void handleOption(OptionParser var1, ArgumentList var2, OptionSet var3, String var4) {
      if (Strings.isNullOrEmpty(var4)) {
         this.detectOptionArgument(var1, var2, var3);
      } else {
         this.addArguments(var3, var4);
      }

   }

   protected void addArguments(OptionSet var1, String var2) {
      StringTokenizer var3 = new StringTokenizer(var2, this.valueSeparator);
      if (!var3.hasMoreTokens()) {
         var1.addWithArgument(this, var2);
      } else {
         while(var3.hasMoreTokens()) {
            var1.addWithArgument(this, var3.nextToken());
         }
      }

   }

   protected abstract void detectOptionArgument(OptionParser var1, ArgumentList var2, OptionSet var3);

   protected final Object convert(String var1) {
      return this.convertWith(this.converter, var1);
   }

   protected boolean canConvertArgument(String var1) {
      StringTokenizer var2 = new StringTokenizer(var1, this.valueSeparator);

      try {
         while(var2.hasMoreTokens()) {
            this.convert(var2.nextToken());
         }

         return true;
      } catch (OptionException var4) {
         return false;
      }
   }

   protected boolean isArgumentOfNumberType() {
      return this.converter != null && Number.class.isAssignableFrom(this.converter.valueType());
   }

   public boolean acceptsArguments() {
      return true;
   }

   public boolean requiresArgument() {
      return this.argumentRequired;
   }

   public String argumentDescription() {
      return this.argumentDescription;
   }

   public String argumentTypeIndicator() {
      return this.argumentTypeIndicatorFrom(this.converter);
   }

   public List defaultValues() {
      return Collections.unmodifiableList(this.defaultValues);
   }

   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         ArgumentAcceptingOptionSpec var2 = (ArgumentAcceptingOptionSpec)var1;
         return this.requiresArgument() == var2.requiresArgument();
      }
   }

   public int hashCode() {
      return super.hashCode() ^ (this.argumentRequired ? 0 : 1);
   }

   public String toString() {
      return super.toString();
   }

   public boolean representsNonOptions() {
      return super.representsNonOptions();
   }

   public String description() {
      return super.description();
   }
}
