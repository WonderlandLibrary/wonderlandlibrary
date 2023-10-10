package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
   private final ConstructorConstructor constructorConstructor;
   private final FieldNamingStrategy fieldNamingPolicy;
   private final Excluder excluder;

   public ReflectiveTypeAdapterFactory(ConstructorConstructor var1, FieldNamingStrategy var2, Excluder var3) {
      this.constructorConstructor = var1;
      this.fieldNamingPolicy = var2;
      this.excluder = var3;
   }

   public boolean excludeField(Field var1, boolean var2) {
      return !this.excluder.excludeClass(var1.getType(), var2) && !this.excluder.excludeField(var1, var2);
   }

   private String getFieldName(Field var1) {
      SerializedName var2 = (SerializedName)var1.getAnnotation(SerializedName.class);
      return var2 == null ? this.fieldNamingPolicy.translateName(var1) : var2.value();
   }

   public TypeAdapter create(Gson var1, TypeToken var2) {
      Class var3 = var2.getRawType();
      if (!Object.class.isAssignableFrom(var3)) {
         return null;
      } else {
         ObjectConstructor var4 = this.constructorConstructor.get(var2);
         return new ReflectiveTypeAdapterFactory.Adapter(var4, this.getBoundFields(var1, var2, var3));
      }
   }

   private ReflectiveTypeAdapterFactory.BoundField createBoundField(Gson var1, Field var2, String var3, TypeToken var4, boolean var5, boolean var6) {
      boolean var7 = Primitives.isPrimitive(var4.getRawType());
      return new ReflectiveTypeAdapterFactory.BoundField(this, var3, var5, var6, var1, var4, var2, var7) {
         final TypeAdapter typeAdapter;
         final Gson val$context;
         final TypeToken val$fieldType;
         final Field val$field;
         final boolean val$isPrimitive;
         final ReflectiveTypeAdapterFactory this$0;

         {
            this.this$0 = var1;
            this.val$context = var5;
            this.val$fieldType = var6;
            this.val$field = var7;
            this.val$isPrimitive = var8;
            this.typeAdapter = this.val$context.getAdapter(this.val$fieldType);
         }

         void write(JsonWriter var1, Object var2) throws IOException, IllegalAccessException {
            Object var3 = this.val$field.get(var2);
            TypeAdapterRuntimeTypeWrapper var4 = new TypeAdapterRuntimeTypeWrapper(this.val$context, this.typeAdapter, this.val$fieldType.getType());
            var4.write(var1, var3);
         }

         void read(JsonReader var1, Object var2) throws IOException, IllegalAccessException {
            Object var3 = this.typeAdapter.read(var1);
            if (var3 != null || !this.val$isPrimitive) {
               this.val$field.set(var2, var3);
            }

         }
      };
   }

   private Map getBoundFields(Gson var1, TypeToken var2, Class var3) {
      LinkedHashMap var4 = new LinkedHashMap();
      if (var3.isInterface()) {
         return var4;
      } else {
         for(Type var5 = var2.getType(); var3 != Object.class; var3 = var2.getRawType()) {
            Field[] var6 = var3.getDeclaredFields();
            Field[] var7 = var6;
            int var8 = var6.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               Field var10 = var7[var9];
               boolean var11 = this.excludeField(var10, true);
               boolean var12 = this.excludeField(var10, false);
               if (var11 || var12) {
                  var10.setAccessible(true);
                  Type var13 = $Gson$Types.resolve(var2.getType(), var3, var10.getGenericType());
                  ReflectiveTypeAdapterFactory.BoundField var14 = this.createBoundField(var1, var10, this.getFieldName(var10), TypeToken.get(var13), var11, var12);
                  ReflectiveTypeAdapterFactory.BoundField var15 = (ReflectiveTypeAdapterFactory.BoundField)var4.put(var14.name, var14);
                  if (var15 != null) {
                     throw new IllegalArgumentException(var5 + " declares multiple JSON fields named " + var15.name);
                  }
               }
            }

            var2 = TypeToken.get($Gson$Types.resolve(var2.getType(), var3, var3.getGenericSuperclass()));
         }

         return var4;
      }
   }

   public static final class Adapter extends TypeAdapter {
      private final ObjectConstructor constructor;
      private final Map boundFields;

      private Adapter(ObjectConstructor var1, Map var2) {
         this.constructor = var1;
         this.boundFields = var2;
      }

      public Object read(JsonReader var1) throws IOException {
         if (var1.peek() == JsonToken.NULL) {
            var1.nextNull();
            return null;
         } else {
            Object var2 = this.constructor.construct();

            try {
               var1.beginObject();

               while(var1.hasNext()) {
                  String var3 = var1.nextName();
                  ReflectiveTypeAdapterFactory.BoundField var4 = (ReflectiveTypeAdapterFactory.BoundField)this.boundFields.get(var3);
                  if (var4 != null && var4.deserialized) {
                     var4.read(var1, var2);
                  } else {
                     var1.skipValue();
                  }
               }
            } catch (IllegalStateException var5) {
               throw new JsonSyntaxException(var5);
            } catch (IllegalAccessException var6) {
               throw new AssertionError(var6);
            }

            var1.endObject();
            return var2;
         }
      }

      public void write(JsonWriter var1, Object var2) throws IOException {
         if (var2 == null) {
            var1.nullValue();
         } else {
            var1.beginObject();

            try {
               Iterator var3 = this.boundFields.values().iterator();

               while(var3.hasNext()) {
                  ReflectiveTypeAdapterFactory.BoundField var4 = (ReflectiveTypeAdapterFactory.BoundField)var3.next();
                  if (var4.serialized) {
                     var1.name(var4.name);
                     var4.write(var1, var2);
                  }
               }
            } catch (IllegalAccessException var5) {
               throw new AssertionError();
            }

            var1.endObject();
         }
      }

      Adapter(ObjectConstructor var1, Map var2, Object var3) {
         this(var1, var2);
      }
   }

   abstract static class BoundField {
      final String name;
      final boolean serialized;
      final boolean deserialized;

      protected BoundField(String var1, boolean var2, boolean var3) {
         this.name = var1;
         this.serialized = var2;
         this.deserialized = var3;
      }

      abstract void write(JsonWriter var1, Object var2) throws IOException, IllegalAccessException;

      abstract void read(JsonReader var1, Object var2) throws IOException, IllegalAccessException;
   }
}
