package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public final class JsonTreeWriter extends JsonWriter {
  private static final Writer UNWRITABLE_WRITER = new Writer() {
      public void write(char[] buffer, int offset, int counter) {
        throw new AssertionError();
      }
      
      public void flush() throws IOException {
        throw new AssertionError();
      }
      
      public void close() throws IOException {
        throw new AssertionError();
      }
    };
  
  private static final JsonPrimitive SENTINEL_CLOSED = new JsonPrimitive("closed");
  
  private final List<JsonElement> stack = new ArrayList<JsonElement>();
  
  private String pendingName;
  
  private JsonElement product = (JsonElement)JsonNull.INSTANCE;
  
  public JsonTreeWriter() {
    super(UNWRITABLE_WRITER);
  }
  
  public JsonElement get() {
    if (!this.stack.isEmpty())
      throw new IllegalStateException("Expected one JSON element but was " + this.stack); 
    return this.product;
  }
  
  private JsonElement peek() {
    return this.stack.get(this.stack.size() - 1);
  }
  
  private void put(JsonElement value) {
    if (this.pendingName != null) {
      if (!value.isJsonNull() || getSerializeNulls()) {
        JsonObject object = (JsonObject)peek();
        object.add(this.pendingName, value);
      } 
      this.pendingName = null;
    } else if (this.stack.isEmpty()) {
      this.product = value;
    } else {
      JsonElement element = peek();
      if (element instanceof JsonArray) {
        ((JsonArray)element).add(value);
      } else {
        throw new IllegalStateException();
      } 
    } 
  }
  
  public JsonWriter beginArray() throws IOException {
    JsonArray array = new JsonArray();
    put((JsonElement)array);
    this.stack.add(array);
    return this;
  }
  
  public JsonWriter endArray() throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    JsonElement element = peek();
    if (element instanceof JsonArray) {
      this.stack.remove(this.stack.size() - 1);
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter beginObject() throws IOException {
    JsonObject object = new JsonObject();
    put((JsonElement)object);
    this.stack.add(object);
    return this;
  }
  
  public JsonWriter endObject() throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    JsonElement element = peek();
    if (element instanceof JsonObject) {
      this.stack.remove(this.stack.size() - 1);
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter name(String name) throws IOException {
    if (this.stack.isEmpty() || this.pendingName != null)
      throw new IllegalStateException(); 
    JsonElement element = peek();
    if (element instanceof JsonObject) {
      this.pendingName = name;
      return this;
    } 
    throw new IllegalStateException();
  }
  
  public JsonWriter value(String value) throws IOException {
    if (value == null)
      return nullValue(); 
    put((JsonElement)new JsonPrimitive(value));
    return this;
  }
  
  public JsonWriter nullValue() throws IOException {
    put((JsonElement)JsonNull.INSTANCE);
    return this;
  }
  
  public JsonWriter value(boolean value) throws IOException {
    put((JsonElement)new JsonPrimitive(Boolean.valueOf(value)));
    return this;
  }
  
  public JsonWriter value(double value) throws IOException {
    if (!isLenient() && (Double.isNaN(value) || Double.isInfinite(value)))
      throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value); 
    put((JsonElement)new JsonPrimitive(Double.valueOf(value)));
    return this;
  }
  
  public JsonWriter value(long value) throws IOException {
    put((JsonElement)new JsonPrimitive(Long.valueOf(value)));
    return this;
  }
  
  public JsonWriter value(Number value) throws IOException {
    if (value == null)
      return nullValue(); 
    if (!isLenient()) {
      double d = value.doubleValue();
      if (Double.isNaN(d) || Double.isInfinite(d))
        throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value); 
    } 
    put((JsonElement)new JsonPrimitive(value));
    return this;
  }
  
  public void flush() throws IOException {}
  
  public void close() throws IOException {
    if (!this.stack.isEmpty())
      throw new IOException("Incomplete document"); 
    this.stack.add(SENTINEL_CLOSED);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\JsonTreeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */