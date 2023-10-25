package nl.matsv.viabackwards.protocol.protocol1_15_2to1_16.chat;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.regex.Pattern;
import us.myles.viaversion.libs.gson.JsonArray;
import us.myles.viaversion.libs.gson.JsonElement;
import us.myles.viaversion.libs.gson.JsonObject;
import us.myles.viaversion.libs.gson.JsonPrimitive;
import us.myles.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.ListTag;
import us.myles.viaversion.libs.opennbt.tag.builtin.Tag;

public class TagSerializer {
  private static final Pattern PLAIN_TEXT = Pattern.compile("[A-Za-z0-9._+-]+");
  
  public static String toString(JsonObject object) {
    StringBuilder builder = new StringBuilder("{");
    for (Map.Entry<String, JsonElement> entry : (Iterable<Map.Entry<String, JsonElement>>)object.entrySet()) {
      Preconditions.checkArgument(((JsonElement)entry.getValue()).isJsonPrimitive());
      if (builder.length() != 1)
        builder.append(','); 
      String escapedText = escape(((JsonElement)entry.getValue()).getAsString());
      builder.append(entry.getKey()).append(':').append(escapedText);
    } 
    return builder.append('}').toString();
  }
  
  public static JsonObject toJson(CompoundTag tag) {
    JsonObject object = new JsonObject();
    for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)tag.getValue().entrySet())
      object.add(entry.getKey(), toJson(entry.getValue())); 
    return object;
  }
  
  private static JsonElement toJson(Tag tag) {
    if (tag instanceof CompoundTag)
      return (JsonElement)toJson((CompoundTag)tag); 
    if (tag instanceof ListTag) {
      ListTag list = (ListTag)tag;
      JsonArray array = new JsonArray();
      for (Tag listEntry : list)
        array.add(toJson(listEntry)); 
      return (JsonElement)array;
    } 
    return (JsonElement)new JsonPrimitive(tag.getValue().toString());
  }
  
  public static String escape(String s) {
    if (PLAIN_TEXT.matcher(s).matches())
      return s; 
    StringBuilder builder = new StringBuilder(" ");
    char currentQuote = Character.MIN_VALUE;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '\\') {
        builder.append('\\');
      } else if (c == '"' || c == '\'') {
        if (currentQuote == '\000')
          currentQuote = (c == '"') ? '\'' : '"'; 
        if (currentQuote == c)
          builder.append('\\'); 
      } 
      builder.append(c);
    } 
    if (currentQuote == '\000')
      currentQuote = '"'; 
    builder.setCharAt(0, currentQuote);
    builder.append(currentQuote);
    return builder.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_15_2to1_16\chat\TagSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */