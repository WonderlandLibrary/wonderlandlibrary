package com.google.gson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class JsonArray extends JsonElement implements Iterable<JsonElement> {
  private final List<JsonElement> elements = new ArrayList<JsonElement>();
  
  JsonArray deepCopy() {
    JsonArray result = new JsonArray();
    for (JsonElement element : this.elements)
      result.add(element.deepCopy()); 
    return result;
  }
  
  public void add(JsonElement element) {
    if (element == null)
      element = JsonNull.INSTANCE; 
    this.elements.add(element);
  }
  
  public void addAll(JsonArray array) {
    this.elements.addAll(array.elements);
  }
  
  public int size() {
    return this.elements.size();
  }
  
  public Iterator<JsonElement> iterator() {
    return this.elements.iterator();
  }
  
  public JsonElement get(int i) {
    return this.elements.get(i);
  }
  
  public Number getAsNumber() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsNumber(); 
    throw new IllegalStateException();
  }
  
  public String getAsString() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsString(); 
    throw new IllegalStateException();
  }
  
  public double getAsDouble() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsDouble(); 
    throw new IllegalStateException();
  }
  
  public BigDecimal getAsBigDecimal() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsBigDecimal(); 
    throw new IllegalStateException();
  }
  
  public BigInteger getAsBigInteger() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsBigInteger(); 
    throw new IllegalStateException();
  }
  
  public float getAsFloat() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsFloat(); 
    throw new IllegalStateException();
  }
  
  public long getAsLong() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsLong(); 
    throw new IllegalStateException();
  }
  
  public int getAsInt() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsInt(); 
    throw new IllegalStateException();
  }
  
  public byte getAsByte() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsByte(); 
    throw new IllegalStateException();
  }
  
  public char getAsCharacter() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsCharacter(); 
    throw new IllegalStateException();
  }
  
  public short getAsShort() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsShort(); 
    throw new IllegalStateException();
  }
  
  public boolean getAsBoolean() {
    if (this.elements.size() == 1)
      return ((JsonElement)this.elements.get(0)).getAsBoolean(); 
    throw new IllegalStateException();
  }
  
  public boolean equals(Object o) {
    return (o == this || (o instanceof JsonArray && ((JsonArray)o).elements.equals(this.elements)));
  }
  
  public int hashCode() {
    return this.elements.hashCode();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */