// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public final class JsonArray extends JsonElement implements Iterable<JsonElement>
{
    private final List<JsonElement> elements;
    
    public JsonArray() {
        this.elements = new ArrayList<JsonElement>();
    }
    
    public JsonArray(final int capacity) {
        this.elements = new ArrayList<JsonElement>(capacity);
    }
    
    @Override
    public JsonArray deepCopy() {
        if (!this.elements.isEmpty()) {
            final JsonArray result = new JsonArray(this.elements.size());
            for (final JsonElement element : this.elements) {
                result.add(element.deepCopy());
            }
            return result;
        }
        return new JsonArray();
    }
    
    public void add(final Boolean bool) {
        this.elements.add((bool == null) ? JsonNull.INSTANCE : new JsonPrimitive(bool));
    }
    
    public void add(final Character character) {
        this.elements.add((character == null) ? JsonNull.INSTANCE : new JsonPrimitive(character));
    }
    
    public void add(final Number number) {
        this.elements.add((number == null) ? JsonNull.INSTANCE : new JsonPrimitive(number));
    }
    
    public void add(final String string) {
        this.elements.add((string == null) ? JsonNull.INSTANCE : new JsonPrimitive(string));
    }
    
    public void add(JsonElement element) {
        if (element == null) {
            element = JsonNull.INSTANCE;
        }
        this.elements.add(element);
    }
    
    public void addAll(final JsonArray array) {
        this.elements.addAll(array.elements);
    }
    
    public JsonElement set(final int index, final JsonElement element) {
        return this.elements.set(index, element);
    }
    
    public boolean remove(final JsonElement element) {
        return this.elements.remove(element);
    }
    
    public JsonElement remove(final int index) {
        return this.elements.remove(index);
    }
    
    public boolean contains(final JsonElement element) {
        return this.elements.contains(element);
    }
    
    public int size() {
        return this.elements.size();
    }
    
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
    
    @Override
    public Iterator<JsonElement> iterator() {
        return this.elements.iterator();
    }
    
    public JsonElement get(final int i) {
        return this.elements.get(i);
    }
    
    @Override
    public Number getAsNumber() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsNumber();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public String getAsString() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsString();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public double getAsDouble() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsDouble();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public BigDecimal getAsBigDecimal() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBigDecimal();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public BigInteger getAsBigInteger() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBigInteger();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public float getAsFloat() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsFloat();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public long getAsLong() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsLong();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public int getAsInt() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsInt();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public byte getAsByte() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsByte();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public char getAsCharacter() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsCharacter();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public short getAsShort() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsShort();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public boolean getAsBoolean() {
        if (this.elements.size() == 1) {
            return this.elements.get(0).getAsBoolean();
        }
        throw new IllegalStateException();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements));
    }
    
    @Override
    public int hashCode() {
        return this.elements.hashCode();
    }
}
