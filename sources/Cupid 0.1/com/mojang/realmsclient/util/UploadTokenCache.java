package com.mojang.realmsclient.util;

import java.util.HashMap;
import java.util.Map;

public class UploadTokenCache {
  private static Map<Long, String> tokenCache = new HashMap<Long, String>();
  
  public static String get(long worldId) {
    return tokenCache.get(Long.valueOf(worldId));
  }
  
  public static void invalidate(long world) {
    tokenCache.remove(Long.valueOf(world));
  }
  
  public static void put(long wid, String token) {
    tokenCache.put(Long.valueOf(wid), token);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\mojang\realmsclien\\util\UploadTokenCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */