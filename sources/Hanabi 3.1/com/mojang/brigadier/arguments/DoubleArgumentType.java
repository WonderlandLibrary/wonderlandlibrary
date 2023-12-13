package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

public class DoubleArgumentType implements ArgumentType {
   private static final Collection EXAMPLES = Arrays.asList("0", "1.2", ".5", "-1", "-.5", "-1234.56");
   private final double minimum;
   private final double maximum;

   private DoubleArgumentType(double minimum, double maximum) {
      this.minimum = minimum;
      this.maximum = maximum;
   }

   public static DoubleArgumentType doubleArg() {
      return doubleArg(-1.7976931348623157E308);
   }

   public static DoubleArgumentType doubleArg(double min) {
      return doubleArg(min, Double.MAX_VALUE);
   }

   public static DoubleArgumentType doubleArg(double min, double max) {
      return new DoubleArgumentType(min, max);
   }

   public static double getDouble(CommandContext context, String name) {
      return (Double)context.getArgument(name, Double.class);
   }

   public double getMinimum() {
      return this.minimum;
   }

   public double getMaximum() {
      return this.maximum;
   }

   public Double parse(StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      double result = reader.readDouble();
      if (result < this.minimum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooLow().createWithContext(reader, result, this.minimum);
      } else if (result > this.maximum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.doubleTooHigh().createWithContext(reader, result, this.maximum);
      } else {
         return result;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof DoubleArgumentType)) {
         return false;
      } else {
         DoubleArgumentType that = (DoubleArgumentType)o;
         return this.maximum == that.maximum && this.minimum == that.minimum;
      }
   }

   public int hashCode() {
      return (int)(31.0 * this.minimum + this.maximum);
   }

   public String toString() {
      if (this.minimum == -1.7976931348623157E308 && this.maximum == Double.MAX_VALUE) {
         return "double()";
      } else {
         return this.maximum == Double.MAX_VALUE ? "double(" + this.minimum + ")" : "double(" + this.minimum + ", " + this.maximum + ")";
      }
   }

   public Collection getExamples() {
      return EXAMPLES;
   }
}
