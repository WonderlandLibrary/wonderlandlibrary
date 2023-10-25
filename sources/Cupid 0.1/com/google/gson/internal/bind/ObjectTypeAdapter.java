package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class ObjectTypeAdapter extends TypeAdapter<Object> {
  public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType() == Object.class)
          return new ObjectTypeAdapter(gson); 
        return null;
      }
    };
  
  private final Gson gson;
  
  private ObjectTypeAdapter(Gson gson) {
    this.gson = gson;
  }
  
  public Object read(JsonReader in) throws IOException {
    List<Object> list;
    LinkedTreeMap<String, Object> linkedTreeMap;
    JsonToken token = in.peek();
    switch (token) {
      case BEGIN_ARRAY:
        list = new ArrayList();
        in.beginArray();
        while (in.hasNext())
          list.add(read(in)); 
        in.endArray();
        return list;
      case BEGIN_OBJECT:
        linkedTreeMap = new LinkedTreeMap();
        in.beginObject();
        while (in.hasNext())
          linkedTreeMap.put(in.nextName(), read(in)); 
        in.endObject();
        return linkedTreeMap;
      case STRING:
        return in.nextString();
      case NUMBER:
        return Double.valueOf(in.nextDouble());
      case BOOLEAN:
        return Boolean.valueOf(in.nextBoolean());
      case NULL:
        in.nextNull();
        return null;
    } 
    throw new IllegalStateException();
  }
  
  public void write(JsonWriter out, Object value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    } 
    TypeAdapter<Object> typeAdapter = this.gson.getAdapter(value.getClass());
    if (typeAdapter instanceof ObjectTypeAdapter) {
      out.beginObject();
      out.endObject();
      return;
    } 
    typeAdapter.write(out, value);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\ObjectTypeAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */