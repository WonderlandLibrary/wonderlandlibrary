package com.google.gson;

import com.google.gson.internal.;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive extends JsonElement {
  private static final Class<?>[] PRIMITIVE_TYPES = new Class[] { 
      int.class, long.class, short.class, float.class, double.class, byte.class, boolean.class, char.class, Integer.class, Long.class, 
      Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class };
  
  private Object value;
  
  public JsonPrimitive(Boolean bool) {
    setValue(bool);
  }
  
  public JsonPrimitive(Number number) {
    setValue(number);
  }
  
  public JsonPrimitive(String string) {
    setValue(string);
  }
  
  public JsonPrimitive(Character c) {
    setValue(c);
  }
  
  JsonPrimitive(Object primitive) {
    setValue(primitive);
  }
  
  JsonPrimitive deepCopy() {
    return this;
  }
  
  void setValue(Object primitive) {
    if (primitive instanceof Character) {
      char c = ((Character)primitive).charValue();
      this.value = String.valueOf(c);
    } else {
      .Gson.Preconditions.checkArgument((primitive instanceof Number || isPrimitiveOrString(primitive)));
      this.value = primitive;
    } 
  }
  
  public boolean isBoolean() {
    return this.value instanceof Boolean;
  }
  
  Boolean getAsBooleanWrapper() {
    return (Boolean)this.value;
  }
  
  public boolean getAsBoolean() {
    if (isBoolean())
      return getAsBooleanWrapper().booleanValue(); 
    return Boolean.parseBoolean(getAsString());
  }
  
  public boolean isNumber() {
    return this.value instanceof Number;
  }
  
  public Number getAsNumber() {
    return (this.value instanceof String) ? (Number)new LazilyParsedNumber((String)this.value) : (Number)this.value;
  }
  
  public boolean isString() {
    return this.value instanceof String;
  }
  
  public String getAsString() {
    if (isNumber())
      return getAsNumber().toString(); 
    if (isBoolean())
      return getAsBooleanWrapper().toString(); 
    return (String)this.value;
  }
  
  public double getAsDouble() {
    return isNumber() ? getAsNumber().doubleValue() : Double.parseDouble(getAsString());
  }
  
  public BigDecimal getAsBigDecimal() {
    return (this.value instanceof BigDecimal) ? (BigDecimal)this.value : new BigDecimal(this.value.toString());
  }
  
  public BigInteger getAsBigInteger() {
    return (this.value instanceof BigInteger) ? (BigInteger)this.value : new BigInteger(this.value.toString());
  }
  
  public float getAsFloat() {
    return isNumber() ? getAsNumber().floatValue() : Float.parseFloat(getAsString());
  }
  
  public long getAsLong() {
    return isNumber() ? getAsNumber().longValue() : Long.parseLong(getAsString());
  }
  
  public short getAsShort() {
    return isNumber() ? getAsNumber().shortValue() : Short.parseShort(getAsString());
  }
  
  public int getAsInt() {
    return isNumber() ? getAsNumber().intValue() : Integer.parseInt(getAsString());
  }
  
  public byte getAsByte() {
    return isNumber() ? getAsNumber().byteValue() : Byte.parseByte(getAsString());
  }
  
  public char getAsCharacter() {
    return getAsString().charAt(0);
  }
  
  private static boolean isPrimitiveOrString(Object target) {
    if (target instanceof String)
      return true; 
    Class<?> classOfPrimitive = target.getClass();
    for (Class<?> standardPrimitive : PRIMITIVE_TYPES) {
      if (standardPrimitive.isAssignableFrom(classOfPrimitive))
        return true; 
    } 
    return false;
  }
  
  public int hashCode() {
    if (this.value == null)
      return 31; 
    if (isIntegral(this)) {
      long value = getAsNumber().longValue();
      return (int)(value ^ value >>> 32L);
    } 
    if (this.value instanceof Number) {
      long value = Double.doubleToLongBits(getAsNumber().doubleValue());
      return (int)(value ^ value >>> 32L);
    } 
    return this.value.hashCode();
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null || getClass() != obj.getClass())
      return false; 
    JsonPrimitive other = (JsonPrimitive)obj;
    if (this.value == null)
      return (other.value == null); 
    if (isIntegral(this) && isIntegral(other))
      return (getAsNumber().longValue() == other.getAsNumber().longValue()); 
    if (this.value instanceof Number && other.value instanceof Number) {
      double a = getAsNumber().doubleValue();
      double b = other.getAsNumber().doubleValue();
      return (a == b || (Double.isNaN(a) && Double.isNaN(b)));
    } 
    return this.value.equals(other.value);
  }
  
  private static boolean isIntegral(JsonPrimitive primitive) {
    if (primitive.value instanceof Number) {
      Number number = (Number)primitive.value;
      return (number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte);
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonPrimitive.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */