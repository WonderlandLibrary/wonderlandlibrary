package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MapTypeAdapterFactory implements TypeAdapterFactory {
  private final ConstructorConstructor constructorConstructor;
  
  private final boolean complexMapKeySerialization;
  
  public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
    this.constructorConstructor = constructorConstructor;
    this.complexMapKeySerialization = complexMapKeySerialization;
  }
  
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Type type = typeToken.getType();
    Class<? super T> rawType = typeToken.getRawType();
    if (!Map.class.isAssignableFrom(rawType))
      return null; 
    Class<?> rawTypeOfSrc = .Gson.Types.getRawType(type);
    Type[] keyAndValueTypes = .Gson.Types.getMapKeyAndValueTypes(type, rawTypeOfSrc);
    TypeAdapter<?> keyAdapter = getKeyAdapter(gson, keyAndValueTypes[0]);
    TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(keyAndValueTypes[1]));
    ObjectConstructor<T> constructor = this.constructorConstructor.get(typeToken);
    TypeAdapter<T> result = (TypeAdapter)new Adapter<Object, Object>(gson, keyAndValueTypes[0], keyAdapter, keyAndValueTypes[1], valueAdapter, (ObjectConstructor)constructor);
    return result;
  }
  
  private TypeAdapter<?> getKeyAdapter(Gson context, Type keyType) {
    return (keyType == boolean.class || keyType == Boolean.class) ? TypeAdapters.BOOLEAN_AS_STRING : context.getAdapter(TypeToken.get(keyType));
  }
  
  private final class Adapter<K, V> extends TypeAdapter<Map<K, V>> {
    private final TypeAdapter<K> keyTypeAdapter;
    
    private final TypeAdapter<V> valueTypeAdapter;
    
    private final ObjectConstructor<? extends Map<K, V>> constructor;
    
    public Adapter(Gson context, Type keyType, TypeAdapter<K> keyTypeAdapter, Type valueType, TypeAdapter<V> valueTypeAdapter, ObjectConstructor<? extends Map<K, V>> constructor) {
      this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(context, keyTypeAdapter, keyType);
      this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(context, valueTypeAdapter, valueType);
      this.constructor = constructor;
    }
    
    public Map<K, V> read(JsonReader in) throws IOException {
      JsonToken peek = in.peek();
      if (peek == JsonToken.NULL) {
        in.nextNull();
        return null;
      } 
      Map<K, V> map = (Map<K, V>)this.constructor.construct();
      if (peek == JsonToken.BEGIN_ARRAY) {
        in.beginArray();
        while (in.hasNext()) {
          in.beginArray();
          K key = (K)this.keyTypeAdapter.read(in);
          V value = (V)this.valueTypeAdapter.read(in);
          V replaced = map.put(key, value);
          if (replaced != null)
            throw new JsonSyntaxException("duplicate key: " + key); 
          in.endArray();
        } 
        in.endArray();
      } else {
        in.beginObject();
        while (in.hasNext()) {
          JsonReaderInternalAccess.INSTANCE.promoteNameToValue(in);
          K key = (K)this.keyTypeAdapter.read(in);
          V value = (V)this.valueTypeAdapter.read(in);
          V replaced = map.put(key, value);
          if (replaced != null)
            throw new JsonSyntaxException("duplicate key: " + key); 
        } 
        in.endObject();
      } 
      return map;
    }
    
    public void write(JsonWriter out, Map<K, V> map) throws IOException {
      int i;
      if (map == null) {
        out.nullValue();
        return;
      } 
      if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
        out.beginObject();
        for (Map.Entry<K, V> entry : map.entrySet()) {
          out.name(String.valueOf(entry.getKey()));
          this.valueTypeAdapter.write(out, entry.getValue());
        } 
        out.endObject();
        return;
      } 
      boolean hasComplexKeys = false;
      List<JsonElement> keys = new ArrayList<JsonElement>(map.size());
      List<V> values = new ArrayList<V>(map.size());
      for (Map.Entry<K, V> entry : map.entrySet()) {
        JsonElement keyElement = this.keyTypeAdapter.toJsonTree(entry.getKey());
        keys.add(keyElement);
        values.add(entry.getValue());
        i = hasComplexKeys | ((keyElement.isJsonArray() || keyElement.isJsonObject()) ? 1 : 0);
      } 
      if (i != 0) {
        out.beginArray();
        for (int j = 0; j < keys.size(); j++) {
          out.beginArray();
          Streams.write(keys.get(j), out);
          this.valueTypeAdapter.write(out, values.get(j));
          out.endArray();
        } 
        out.endArray();
      } else {
        out.beginObject();
        for (int j = 0; j < keys.size(); j++) {
          JsonElement keyElement = keys.get(j);
          out.name(keyToString(keyElement));
          this.valueTypeAdapter.write(out, values.get(j));
        } 
        out.endObject();
      } 
    }
    
    private String keyToString(JsonElement keyElement) {
      if (keyElement.isJsonPrimitive()) {
        JsonPrimitive primitive = keyElement.getAsJsonPrimitive();
        if (primitive.isNumber())
          return String.valueOf(primitive.getAsNumber()); 
        if (primitive.isBoolean())
          return Boolean.toString(primitive.getAsBoolean()); 
        if (primitive.isString())
          return primitive.getAsString(); 
        throw new AssertionError();
      } 
      if (keyElement.isJsonNull())
        return "null"; 
      throw new AssertionError();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\MapTypeAdapterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */