// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.math.BigInteger;
import java.math.BigDecimal;
import com.viaversion.viaversion.libs.gson.internal.LazilyParsedNumber;
import com.viaversion.viaversion.libs.gson.internal.$Gson$Preconditions;

public final class JsonPrimitive extends JsonElement
{
    private final Object value;
    
    public JsonPrimitive(final Boolean bool) {
        this.value = $Gson$Preconditions.checkNotNull(bool);
    }
    
    public JsonPrimitive(final Number number) {
        this.value = $Gson$Preconditions.checkNotNull(number);
    }
    
    public JsonPrimitive(final String string) {
        this.value = $Gson$Preconditions.checkNotNull(string);
    }
    
    public JsonPrimitive(final Character c) {
        this.value = $Gson$Preconditions.checkNotNull(c).toString();
    }
    
    @Override
    public JsonPrimitive deepCopy() {
        return this;
    }
    
    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return (boolean)this.value;
        }
        return Boolean.parseBoolean(this.getAsString());
    }
    
    public boolean isNumber() {
        return this.value instanceof Number;
    }
    
    @Override
    public Number getAsNumber() {
        return (this.value instanceof String) ? new LazilyParsedNumber((String)this.value) : this.value;
    }
    
    public boolean isString() {
        return this.value instanceof String;
    }
    
    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return ((Boolean)this.value).toString();
        }
        return (String)this.value;
    }
    
    @Override
    public double getAsDouble() {
        return this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        return (BigDecimal)((this.value instanceof BigDecimal) ? this.value : new BigDecimal(this.value.toString()));
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        return (BigInteger)((this.value instanceof BigInteger) ? this.value : new BigInteger(this.value.toString()));
    }
    
    @Override
    public float getAsFloat() {
        return this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
    }
    
    @Override
    public long getAsLong() {
        return this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
    }
    
    @Override
    public short getAsShort() {
        return this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
    }
    
    @Override
    public int getAsInt() {
        return this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
    }
    
    @Override
    public byte getAsByte() {
        return this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
    }
    
    @Override
    public char getAsCharacter() {
        return this.getAsString().charAt(0);
    }
    
    @Override
    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (isIntegral(this)) {
            final long value = this.getAsNumber().longValue();
            return (int)(value ^ value >>> 32);
        }
        if (this.value instanceof Number) {
            final long value = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(value ^ value >>> 32);
        }
        return this.value.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final JsonPrimitive other = (JsonPrimitive)obj;
        if (this.value == null) {
            return other.value == null;
        }
        if (isIntegral(this) && isIntegral(other)) {
            return this.getAsNumber().longValue() == other.getAsNumber().longValue();
        }
        if (this.value instanceof Number && other.value instanceof Number) {
            final double a = this.getAsNumber().doubleValue();
            final double b = other.getAsNumber().doubleValue();
            return a == b || (Double.isNaN(a) && Double.isNaN(b));
        }
        return this.value.equals(other.value);
    }
    
    private static boolean isIntegral(final JsonPrimitive primitive) {
        if (primitive.value instanceof Number) {
            final Number number = (Number)primitive.value;
            return number instanceof BigInteger || number instanceof Long || number instanceof Integer || number instanceof Short || number instanceof Byte;
        }
        return false;
    }
}
