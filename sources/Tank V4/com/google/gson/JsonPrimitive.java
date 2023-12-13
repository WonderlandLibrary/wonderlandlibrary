package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
   private static final Class[] PRIMITIVE_TYPES;
   private Object value;

   public JsonPrimitive(Boolean var1) {
      this.setValue(var1);
   }

   public JsonPrimitive(Number var1) {
      this.setValue(var1);
   }

   public JsonPrimitive(String var1) {
      this.setValue(var1);
   }

   public JsonPrimitive(Character var1) {
      this.setValue(var1);
   }

   JsonPrimitive(Object var1) {
      this.setValue(var1);
   }

   JsonPrimitive deepCopy() {
      return this;
   }

   void setValue(Object var1) {
      if (var1 instanceof Character) {
         char var2 = (Character)var1;
         this.value = String.valueOf(var2);
      } else {
         $Gson$Preconditions.checkArgument(var1 instanceof Number || var1 != false);
         this.value = var1;
      }

   }

   public boolean isBoolean() {
      return this.value instanceof Boolean;
   }

   Boolean getAsBooleanWrapper() {
      return (Boolean)this.value;
   }

   public boolean getAsBoolean() {
      return this.isBoolean() ? this.getAsBooleanWrapper() : Boolean.parseBoolean(this.getAsString());
   }

   public boolean isNumber() {
      return this.value instanceof Number;
   }

   public Number getAsNumber() {
      return (Number)(this.value instanceof String ? new LazilyParsedNumber((String)this.value) : (Number)this.value);
   }

   public boolean isString() {
      return this.value instanceof String;
   }

   public String getAsString() {
      if (this.isNumber()) {
         return this.getAsNumber().toString();
      } else {
         return this.isBoolean() ? this.getAsBooleanWrapper().toString() : (String)this.value;
      }
   }

   public double getAsDouble() {
      return this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
   }

   public BigDecimal getAsBigDecimal() {
      return this.value instanceof BigDecimal ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
   }

   public BigInteger getAsBigInteger() {
      return this.value instanceof BigInteger ? (BigInteger)this.value : new BigInteger(this.value.toString());
   }

   public float getAsFloat() {
      return this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
   }

   public long getAsLong() {
      return this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
   }

   public short getAsShort() {
      return this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
   }

   public int getAsInt() {
      return this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
   }

   public byte getAsByte() {
      return this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
   }

   public char getAsCharacter() {
      return this.getAsString().charAt(0);
   }

   public int hashCode() {
      if (this.value == null) {
         return 31;
      } else {
         long var1;
         if (this != false) {
            var1 = this.getAsNumber().longValue();
            return (int)(var1 ^ var1 >>> 32);
         } else if (this.value instanceof Number) {
            var1 = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(var1 ^ var1 >>> 32);
         } else {
            return this.value.hashCode();
         }
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         JsonPrimitive var2 = (JsonPrimitive)var1;
         if (this.value == null) {
            return var2.value == null;
         } else if (this != false && var2 != false) {
            return this.getAsNumber().longValue() == var2.getAsNumber().longValue();
         } else if (this.value instanceof Number && var2.value instanceof Number) {
            double var3 = this.getAsNumber().doubleValue();
            double var5 = var2.getAsNumber().doubleValue();
            return var3 == var5 || Double.isNaN(var3) && Double.isNaN(var5);
         } else {
            return this.value.equals(var2.value);
         }
      } else {
         return false;
      }
   }

   JsonElement deepCopy() {
      return this.deepCopy();
   }

   static {
      PRIMITIVE_TYPES = new Class[]{Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
   }
}
