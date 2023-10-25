package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
  private final Gson context;
  
  private final TypeAdapter<T> delegate;
  
  private final Type type;
  
  TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type) {
    this.context = context;
    this.delegate = delegate;
    this.type = type;
  }
  
  public T read(JsonReader in) throws IOException {
    return (T)this.delegate.read(in);
  }
  
  public void write(JsonWriter out, T value) throws IOException {
    TypeAdapter<T> chosen = this.delegate;
    Type runtimeType = getRuntimeTypeIfMoreSpecific(this.type, value);
    if (runtimeType != this.type) {
      TypeAdapter<T> runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
      if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
        chosen = runtimeTypeAdapter;
      } else if (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
        chosen = this.delegate;
      } else {
        chosen = runtimeTypeAdapter;
      } 
    } 
    chosen.write(out, value);
  }
  
  private Type getRuntimeTypeIfMoreSpecific(Type<?> type, Object value) {
    if (value != null && (type == Object.class || type instanceof java.lang.reflect.TypeVariable || type instanceof Class))
      type = value.getClass(); 
    return type;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\TypeAdapterRuntimeTypeWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */