package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

public class IntegerArgumentType implements ArgumentType {
   private static final Collection EXAMPLES = Arrays.asList("0", "123", "-123");
   private final int minimum;
   private final int maximum;

   private IntegerArgumentType(int minimum, int maximum) {
      this.minimum = minimum;
      this.maximum = maximum;
   }

   public static IntegerArgumentType integer() {
      return integer(Integer.MIN_VALUE);
   }

   public static IntegerArgumentType integer(int min) {
      return integer(min, Integer.MAX_VALUE);
   }

   public static IntegerArgumentType integer(int min, int max) {
      return new IntegerArgumentType(min, max);
   }

   public static int getInteger(CommandContext context, String name) {
      return (Integer)context.getArgument(name, Integer.TYPE);
   }

   public int getMinimum() {
      return this.minimum;
   }

   public int getMaximum() {
      return this.maximum;
   }

   public Integer parse(StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      int result = reader.readInt();
      if (result < this.minimum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooLow().createWithContext(reader, result, this.minimum);
      } else if (result > this.maximum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.integerTooHigh().createWithContext(reader, result, this.maximum);
      } else {
         return result;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof IntegerArgumentType)) {
         return false;
      } else {
         IntegerArgumentType that = (IntegerArgumentType)o;
         return this.maximum == that.maximum && this.minimum == that.minimum;
      }
   }

   public int hashCode() {
      return 31 * this.minimum + this.maximum;
   }

   public String toString() {
      if (this.minimum == Integer.MIN_VALUE && this.maximum == Integer.MAX_VALUE) {
         return "integer()";
      } else {
         return this.maximum == Integer.MAX_VALUE ? "integer(" + this.minimum + ")" : "integer(" + this.minimum + ", " + this.maximum + ")";
      }
   }

   public Collection getExamples() {
      return EXAMPLES;
   }
}
