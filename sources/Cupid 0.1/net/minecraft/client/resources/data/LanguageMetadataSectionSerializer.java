package net.minecraft.client.resources.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.resources.Language;
import net.minecraft.util.JsonUtils;

public class LanguageMetadataSectionSerializer extends BaseMetadataSectionSerializer<LanguageMetadataSection> {
  public LanguageMetadataSection deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
    JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
    Set<Language> set = Sets.newHashSet();
    for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)jsonobject.entrySet()) {
      String s = entry.getKey();
      JsonObject jsonobject1 = JsonUtils.getJsonObject(entry.getValue(), "language");
      String s1 = JsonUtils.getString(jsonobject1, "region");
      String s2 = JsonUtils.getString(jsonobject1, "name");
      boolean flag = JsonUtils.getBoolean(jsonobject1, "bidirectional", false);
      if (s1.isEmpty())
        throw new JsonParseException("Invalid language->'" + s + "'->region: empty value"); 
      if (s2.isEmpty())
        throw new JsonParseException("Invalid language->'" + s + "'->name: empty value"); 
      if (!set.add(new Language(s, s1, s2, flag)))
        throw new JsonParseException("Duplicate language->'" + s + "' defined"); 
    } 
    return new LanguageMetadataSection(set);
  }
  
  public String getSectionName() {
    return "language";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\data\LanguageMetadataSectionSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */