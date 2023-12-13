package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class TypeAdapterRuntimeTypeWrapper extends TypeAdapter {
   private final Gson context;
   private final TypeAdapter delegate;
   private final Type type;

   TypeAdapterRuntimeTypeWrapper(Gson var1, TypeAdapter var2, Type var3) {
      this.context = var1;
      this.delegate = var2;
      this.type = var3;
   }

   public Object read(JsonReader var1) throws IOException {
      return this.delegate.read(var1);
   }

   public void write(JsonWriter var1, Object var2) throws IOException {
      TypeAdapter var3 = this.delegate;
      Type var4 = this.getRuntimeTypeIfMoreSpecific(this.type, var2);
      if (var4 != this.type) {
         TypeAdapter var5 = this.context.getAdapter(TypeToken.get(var4));
         if (!(var5 instanceof ReflectiveTypeAdapterFactory.Adapter)) {
            var3 = var5;
         } else if (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
            var3 = this.delegate;
         } else {
            var3 = var5;
         }
      }

      var3.write(var1, var2);
   }

   private Type getRuntimeTypeIfMoreSpecific(Type var1, Object var2) {
      if (var2 != null && (var1 == Object.class || var1 instanceof TypeVariable || var1 instanceof Class)) {
         var1 = var2.getClass();
      }

      return (Type)var1;
   }
}
