package net.minecraft.util;

import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.Set;

public class JsonSerializableSet extends ForwardingSet<String> implements IJsonSerializable {
  private final Set<String> underlyingSet = Sets.newHashSet();
  
  public void fromJson(JsonElement json) {
    if (json.isJsonArray())
      for (JsonElement jsonelement : json.getAsJsonArray())
        add(jsonelement.getAsString());  
  }
  
  public JsonElement getSerializableElement() {
    JsonArray jsonarray = new JsonArray();
    for (String s : this)
      jsonarray.add((JsonElement)new JsonPrimitive(s)); 
    return (JsonElement)jsonarray;
  }
  
  protected Set<String> delegate() {
    return this.underlyingSet;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\JsonSerializableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */