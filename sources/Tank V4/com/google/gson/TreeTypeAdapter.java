package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

final class TreeTypeAdapter extends TypeAdapter {
   private final JsonSerializer serializer;
   private final JsonDeserializer deserializer;
   private final Gson gson;
   private final TypeToken typeToken;
   private final TypeAdapterFactory skipPast;
   private TypeAdapter delegate;

   private TreeTypeAdapter(JsonSerializer var1, JsonDeserializer var2, Gson var3, TypeToken var4, TypeAdapterFactory var5) {
      this.serializer = var1;
      this.deserializer = var2;
      this.gson = var3;
      this.typeToken = var4;
      this.skipPast = var5;
   }

   public Object read(JsonReader var1) throws IOException {
      if (this.deserializer == null) {
         return this.delegate().read(var1);
      } else {
         JsonElement var2 = Streams.parse(var1);
         return var2.isJsonNull() ? null : this.deserializer.deserialize(var2, this.typeToken.getType(), this.gson.deserializationContext);
      }
   }

   public void write(JsonWriter var1, Object var2) throws IOException {
      if (this.serializer == null) {
         this.delegate().write(var1, var2);
      } else if (var2 == null) {
         var1.nullValue();
      } else {
         JsonElement var3 = this.serializer.serialize(var2, this.typeToken.getType(), this.gson.serializationContext);
         Streams.write(var3, var1);
      }
   }

   private TypeAdapter delegate() {
      TypeAdapter var1 = this.delegate;
      return var1 != null ? var1 : (this.delegate = this.gson.getDelegateAdapter(this.skipPast, this.typeToken));
   }

   public static TypeAdapterFactory newFactory(TypeToken var0, Object var1) {
      return new TreeTypeAdapter.SingleTypeFactory(var1, var0, false, (Class)null);
   }

   public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken var0, Object var1) {
      boolean var2 = var0.getType() == var0.getRawType();
      return new TreeTypeAdapter.SingleTypeFactory(var1, var0, var2, (Class)null);
   }

   public static TypeAdapterFactory newTypeHierarchyFactory(Class var0, Object var1) {
      return new TreeTypeAdapter.SingleTypeFactory(var1, (TypeToken)null, false, var0);
   }

   TreeTypeAdapter(JsonSerializer var1, JsonDeserializer var2, Gson var3, TypeToken var4, TypeAdapterFactory var5, Object var6) {
      this(var1, var2, var3, var4, var5);
   }

   private static class SingleTypeFactory implements TypeAdapterFactory {
      private final TypeToken exactType;
      private final boolean matchRawType;
      private final Class hierarchyType;
      private final JsonSerializer serializer;
      private final JsonDeserializer deserializer;

      private SingleTypeFactory(Object var1, TypeToken var2, boolean var3, Class var4) {
         this.serializer = var1 instanceof JsonSerializer ? (JsonSerializer)var1 : null;
         this.deserializer = var1 instanceof JsonDeserializer ? (JsonDeserializer)var1 : null;
         $Gson$Preconditions.checkArgument(this.serializer != null || this.deserializer != null);
         this.exactType = var2;
         this.matchRawType = var3;
         this.hierarchyType = var4;
      }

      public TypeAdapter create(Gson var1, TypeToken var2) {
         boolean var3 = this.exactType != null ? this.exactType.equals(var2) || this.matchRawType && this.exactType.getType() == var2.getRawType() : this.hierarchyType.isAssignableFrom(var2.getRawType());
         return var3 ? new TreeTypeAdapter(this.serializer, this.deserializer, var1, var2, this) : null;
      }

      SingleTypeFactory(Object var1, TypeToken var2, boolean var3, Class var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
