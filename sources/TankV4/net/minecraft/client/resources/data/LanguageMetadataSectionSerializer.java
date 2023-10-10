package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.resources.Language;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer {
   public LanguageMetadataSection deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      JsonObject var4 = var1.getAsJsonObject();
      HashSet var5 = Sets.newHashSet();
      Iterator var7 = var4.entrySet().iterator();

      String var8;
      String var10;
      String var11;
      boolean var12;
      do {
         if (!var7.hasNext()) {
            return new LanguageMetadataSection(var5);
         }

         Entry var6 = (Entry)var7.next();
         var8 = (String)var6.getKey();
         JsonObject var9 = JsonUtils.getJsonObject((JsonElement)var6.getValue(), "language");
         var10 = JsonUtils.getString(var9, "region");
         var11 = JsonUtils.getString(var9, "name");
         var12 = JsonUtils.getBoolean(var9, "bidirectional", false);
         if (var10.isEmpty()) {
            throw new JsonParseException("Invalid language->'" + var8 + "'->region: empty value");
         }

         if (var11.isEmpty()) {
            throw new JsonParseException("Invalid language->'" + var8 + "'->name: empty value");
         }
      } while(var5.add(new Language(var8, var10, var11, var12)));

      throw new JsonParseException("Duplicate language->'" + var8 + "' defined");
   }

   public Object deserialize(JsonElement var1, Type var2, JsonDeserializationContext var3) throws JsonParseException {
      return this.deserialize(var1, var2, var3);
   }

   public String getSectionName() {
      return "language";
   }
}
