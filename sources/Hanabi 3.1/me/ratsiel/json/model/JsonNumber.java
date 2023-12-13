package me.ratsiel.json.model;

import java.math.BigDecimal;
import me.ratsiel.json.abstracts.JsonValue;

public class JsonNumber extends JsonValue {
   protected String value;

   public JsonNumber() {
   }

   public JsonNumber(String key, String value) {
      super(key);
      this.value = value;
   }

   public JsonNumber(String value) {
      this.value = value;
   }

   public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      String space = this.createSpace();
      stringBuilder.append(space);
      if (this.getKey() != null && !this.getKey().isEmpty()) {
         stringBuilder.append("\"").append(this.getKey()).append("\"").append(" : ");
      }

      stringBuilder.append(this.getValue());
      return stringBuilder.toString();
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public byte byteValue() {
      return this.getBigDecimal().byteValue();
   }

   public int intValue() {
      return this.getBigDecimal().intValue();
   }

   public short shortValue() {
      return this.getBigDecimal().shortValue();
   }

   public double doubleValue() {
      return this.getBigDecimal().doubleValue();
   }

   public float floatValue() {
      return this.getBigDecimal().floatValue();
   }

   public long longValue() {
      return this.getBigDecimal().longValue();
   }

   public Object getNumber(Class clazz) {
      if (!clazz.equals(Byte.class) && !clazz.equals(Byte.TYPE)) {
         if (!clazz.equals(Integer.class) && !clazz.equals(Integer.TYPE)) {
            if (!clazz.equals(Short.class) && !clazz.equals(Short.TYPE)) {
               if (!clazz.equals(Double.class) && !clazz.equals(Double.TYPE)) {
                  if (!clazz.equals(Float.class) && !clazz.equals(Float.TYPE)) {
                     return !clazz.equals(Long.class) && !clazz.equals(Long.TYPE) ? null : this.longValue();
                  } else {
                     return this.floatValue();
                  }
               } else {
                  return this.doubleValue();
               }
            } else {
               return this.shortValue();
            }
         } else {
            return this.intValue();
         }
      } else {
         return this.byteValue();
      }
   }

   public BigDecimal getBigDecimal() {
      return new BigDecimal(this.getValue());
   }

   public boolean isNumeric(String value) {
      if (value == null) {
         return false;
      } else {
         boolean isNumber = value.matches("[+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)");
         if (isNumber) {
            String testValue;
            if (this.isInteger(value)) {
               int integerValue = this.intValue();
               testValue = String.valueOf(integerValue);
            } else if (this.isDouble(value)) {
               double doubleValue = this.doubleValue();
               if (doubleValue > Double.MAX_VALUE || doubleValue < -1022.0) {
                  return false;
               }

               testValue = String.valueOf(Math.round(doubleValue));
            } else if (this.isFloat(value)) {
               float floatValue = this.floatValue();
               if (floatValue > Float.MAX_VALUE || floatValue < -126.0F) {
                  return false;
               }

               testValue = String.valueOf(Math.round(floatValue));
            } else {
               testValue = value;
            }

            try {
               Long.parseLong(testValue);
            } catch (Exception var6) {
               return false;
            }
         }

         return isNumber;
      }
   }

   public boolean isFloat(String value) {
      try {
         Float.parseFloat(value);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public boolean isInteger(String value) {
      try {
         Integer.parseInt(value);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public boolean isDouble(String value) {
      try {
         Double.parseDouble(value);
         return true;
      } catch (Exception var3) {
         return false;
      }
   }
}
