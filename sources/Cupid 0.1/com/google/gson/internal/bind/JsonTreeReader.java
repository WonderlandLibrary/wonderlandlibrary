package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class JsonTreeReader extends JsonReader {
  private static final Reader UNREADABLE_READER = new Reader() {
      public int read(char[] buffer, int offset, int count) throws IOException {
        throw new AssertionError();
      }
      
      public void close() throws IOException {
        throw new AssertionError();
      }
    };
  
  private static final Object SENTINEL_CLOSED = new Object();
  
  private final List<Object> stack = new ArrayList();
  
  public JsonTreeReader(JsonElement element) {
    super(UNREADABLE_READER);
    this.stack.add(element);
  }
  
  public void beginArray() throws IOException {
    expect(JsonToken.BEGIN_ARRAY);
    JsonArray array = (JsonArray)peekStack();
    this.stack.add(array.iterator());
  }
  
  public void endArray() throws IOException {
    expect(JsonToken.END_ARRAY);
    popStack();
    popStack();
  }
  
  public void beginObject() throws IOException {
    expect(JsonToken.BEGIN_OBJECT);
    JsonObject object = (JsonObject)peekStack();
    this.stack.add(object.entrySet().iterator());
  }
  
  public void endObject() throws IOException {
    expect(JsonToken.END_OBJECT);
    popStack();
    popStack();
  }
  
  public boolean hasNext() throws IOException {
    JsonToken token = peek();
    return (token != JsonToken.END_OBJECT && token != JsonToken.END_ARRAY);
  }
  
  public JsonToken peek() throws IOException {
    if (this.stack.isEmpty())
      return JsonToken.END_DOCUMENT; 
    Object o = peekStack();
    if (o instanceof Iterator) {
      boolean isObject = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
      Iterator<?> iterator = (Iterator)o;
      if (iterator.hasNext()) {
        if (isObject)
          return JsonToken.NAME; 
        this.stack.add(iterator.next());
        return peek();
      } 
      return isObject ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
    } 
    if (o instanceof JsonObject)
      return JsonToken.BEGIN_OBJECT; 
    if (o instanceof JsonArray)
      return JsonToken.BEGIN_ARRAY; 
    if (o instanceof JsonPrimitive) {
      JsonPrimitive primitive = (JsonPrimitive)o;
      if (primitive.isString())
        return JsonToken.STRING; 
      if (primitive.isBoolean())
        return JsonToken.BOOLEAN; 
      if (primitive.isNumber())
        return JsonToken.NUMBER; 
      throw new AssertionError();
    } 
    if (o instanceof com.google.gson.JsonNull)
      return JsonToken.NULL; 
    if (o == SENTINEL_CLOSED)
      throw new IllegalStateException("JsonReader is closed"); 
    throw new AssertionError();
  }
  
  private Object peekStack() {
    return this.stack.get(this.stack.size() - 1);
  }
  
  private Object popStack() {
    return this.stack.remove(this.stack.size() - 1);
  }
  
  private void expect(JsonToken expected) throws IOException {
    if (peek() != expected)
      throw new IllegalStateException("Expected " + expected + " but was " + peek()); 
  }
  
  public String nextName() throws IOException {
    expect(JsonToken.NAME);
    Iterator<?> i = (Iterator)peekStack();
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
    this.stack.add(entry.getValue());
    return (String)entry.getKey();
  }
  
  public String nextString() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.STRING && token != JsonToken.NUMBER)
      throw new IllegalStateException("Expected " + JsonToken.STRING + " but was " + token); 
    return ((JsonPrimitive)popStack()).getAsString();
  }
  
  public boolean nextBoolean() throws IOException {
    expect(JsonToken.BOOLEAN);
    return ((JsonPrimitive)popStack()).getAsBoolean();
  }
  
  public void nextNull() throws IOException {
    expect(JsonToken.NULL);
    popStack();
  }
  
  public double nextDouble() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token); 
    double result = ((JsonPrimitive)peekStack()).getAsDouble();
    if (!isLenient() && (Double.isNaN(result) || Double.isInfinite(result)))
      throw new NumberFormatException("JSON forbids NaN and infinities: " + result); 
    popStack();
    return result;
  }
  
  public long nextLong() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token); 
    long result = ((JsonPrimitive)peekStack()).getAsLong();
    popStack();
    return result;
  }
  
  public int nextInt() throws IOException {
    JsonToken token = peek();
    if (token != JsonToken.NUMBER && token != JsonToken.STRING)
      throw new IllegalStateException("Expected " + JsonToken.NUMBER + " but was " + token); 
    int result = ((JsonPrimitive)peekStack()).getAsInt();
    popStack();
    return result;
  }
  
  public void close() throws IOException {
    this.stack.clear();
    this.stack.add(SENTINEL_CLOSED);
  }
  
  public void skipValue() throws IOException {
    if (peek() == JsonToken.NAME) {
      nextName();
    } else {
      popStack();
    } 
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
  
  public void promoteNameToValue() throws IOException {
    expect(JsonToken.NAME);
    Iterator<?> i = (Iterator)peekStack();
    Map.Entry<?, ?> entry = (Map.Entry<?, ?>)i.next();
    this.stack.add(entry.getValue());
    this.stack.add(new JsonPrimitive((String)entry.getKey()));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\JsonTreeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */