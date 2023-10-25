package com.mojang.realmsclient.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

public class JsonUtils {
  public static String getStringOr(String key, JsonObject node, String defaultValue) {
    JsonElement element = node.get(key);
    if (element != null)
      return element.isJsonNull() ? defaultValue : element.getAsString(); 
    return defaultValue;
  }
  
  public static int getIntOr(String key, JsonObject node, int defaultValue) {
    JsonElement element = node.get(key);
    if (element != null)
      return element.isJsonNull() ? defaultValue : element.getAsInt(); 
    return defaultValue;
  }
  
  public static long getLongOr(String key, JsonObject node, long defaultValue) {
    JsonElement element = node.get(key);
    if (element != null)
      return element.isJsonNull() ? defaultValue : element.getAsLong(); 
    return defaultValue;
  }
  
  public static boolean getBooleanOr(String key, JsonObject node, boolean defaultValue) {
    JsonElement element = node.get(key);
    if (element != null)
      return element.isJsonNull() ? defaultValue : element.getAsBoolean(); 
    return defaultValue;
  }
  
  public static Date getDateOr(String key, JsonObject node) {
    JsonElement element = node.get(key);
    if (element != null)
      return new Date(Long.parseLong(element.getAsString())); 
    return new Date();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclien\\util\JsonUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */