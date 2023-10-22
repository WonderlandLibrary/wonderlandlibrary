package com.mojang.brigadier.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;

public class FloatArgumentType implements ArgumentType {
   private static final Collection EXAMPLES = Arrays.asList("0", "1.2", ".5", "-1", "-.5", "-1234.56");
   private final float minimum;
   private final float maximum;

   private FloatArgumentType(float minimum, float maximum) {
      this.minimum = minimum;
      this.maximum = maximum;
   }

   public static FloatArgumentType floatArg() {
      return floatArg(-3.4028235E38F);
   }

   public static FloatArgumentType floatArg(float min) {
      return floatArg(min, Float.MAX_VALUE);
   }

   public static FloatArgumentType floatArg(float min, float max) {
      return new FloatArgumentType(min, max);
   }

   public static float getFloat(CommandContext context, String name) {
      return (Float)context.getArgument(name, Float.class);
   }

   public float getMinimum() {
      return this.minimum;
   }

   public float getMaximum() {
      return this.maximum;
   }

   public Float parse(StringReader reader) throws CommandSyntaxException {
      int start = reader.getCursor();
      float result = reader.readFloat();
      if (result < this.minimum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooLow().createWithContext(reader, result, this.minimum);
      } else if (result > this.maximum) {
         reader.setCursor(start);
         throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.floatTooHigh().createWithContext(reader, result, this.maximum);
      } else {
         return result;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof FloatArgumentType)) {
         return false;
      } else {
         FloatArgumentType that = (FloatArgumentType)o;
         return this.maximum == that.maximum && this.minimum == that.minimum;
      }
   }

   public int hashCode() {
      return (int)(31.0F * this.minimum + this.maximum);
   }

   public String toString() {
      if (this.minimum == -3.4028235E38F && this.maximum == Float.MAX_VALUE) {
         return "float()";
      } else {
         return this.maximum == Float.MAX_VALUE ? "float(" + this.minimum + ")" : "float(" + this.minimum + ", " + this.maximum + ")";
      }
   }

   public Collection getExamples() {
      return EXAMPLES;
   }
}
