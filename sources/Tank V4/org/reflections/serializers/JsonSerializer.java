package org.reflections.serializers;

import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import org.reflections.Reflections;
import org.reflections.util.Utils;

public class JsonSerializer implements Serializer {
   private Gson gson;

   public Reflections read(InputStream var1) {
      return (Reflections)this.getGson().fromJson((Reader)(new InputStreamReader(var1)), (Class)Reflections.class);
   }

   public File save(Reflections var1, String var2) {
      try {
         File var3 = Utils.prepareFile(var2);
         Files.write(this.toString(var1), var3, Charset.defaultCharset());
         return var3;
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public String toString(Reflections var1) {
      return this.getGson().toJson((Object)var1);
   }

   private Gson getGson() {
      if (this.gson == null) {
         this.gson = (new GsonBuilder()).registerTypeAdapter(Multimap.class, new com.google.gson.JsonSerializer(this) {
            final JsonSerializer this$0;

            {
               this.this$0 = var1;
            }

            public JsonElement serialize(Multimap var1, Type var2, JsonSerializationContext var3) {
               return var3.serialize(var1.asMap());
            }

            public JsonElement serialize(Object var1, Type var2, JsonSerializationContext var3) {
               return this.serialize((Multimap)var1, var2, var3);
            }
         }).registerTypeAdapter(Multimap.class, new JsonDeserializer(this) {
            final JsonSerializer this$0;

            {
               this.this$0 = var1;
            }

            public Multimap deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
               SetMultimap var4 = Multimaps.newSetMultimap(new HashMap(), new Supplier(this) {
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                  }

                  public Set get() {
                     return Sets.newHashSet();
                  }

                  public Object get() {
                     return this.get();
                  }
               });
               Iterator var5 = ((JsonObject)var1).entrySet().iterator();

               while(var5.hasNext()) {
                  Entry var6 = (Entry)var5.next();
                  Iterator var7 = ((JsonArray)var6.getValue()).iterator();

                  while(var7.hasNext()) {
                     JsonElement var8 = (JsonElement)var7.next();
                     var4.get(var6.getKey()).add(var8.getAsString());
                  }
               }

               return var4;
            }

            public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
               return this.deserialize(var1, var2, var3);
            }
         }).setPrettyPrinting().create();
      }

      return this.gson;
   }
}
