package com.sun.media.jfxmedia.locator;

import com.sun.media.jfxmedia.logging.Logger;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class LocatorCache {
   private final Map uriCache;
   private final CacheDisposer cacheDisposer;

   public static LocatorCache locatorCache() {
      return LocatorCache.CacheInitializer.globalInstance;
   }

   private LocatorCache() {
      this.uriCache = new HashMap();
      this.cacheDisposer = new CacheDisposer();
   }

   public CacheReference registerURICache(URI var1, ByteBuffer var2, String var3) {
      if (Logger.canLog(1)) {
         Logger.logMsg(1, "New cache entry: URI " + var1 + ", buffer " + var2 + ", MIME type " + var3);
      }

      if (!var2.isDirect()) {
         var2.rewind();
         ByteBuffer var4 = ByteBuffer.allocateDirect(var2.capacity());
         var4.put(var2);
         var2 = var4;
      }

      CacheReference var8 = new CacheReference(var2, var3);
      synchronized(this.uriCache) {
         this.uriCache.put(var1, new WeakReference(var8));
      }

      MediaDisposer.addResourceDisposer(var8, var1, this.cacheDisposer);
      return var8;
   }

   public CacheReference fetchURICache(URI var1) {
      synchronized(this.uriCache) {
         WeakReference var3 = (WeakReference)this.uriCache.get(var1);
         if (null == var3) {
            return null;
         } else {
            CacheReference var4 = (CacheReference)var3.get();
            if (null != var4) {
               if (Logger.canLog(1)) {
                  Logger.logMsg(1, "Fetched cache entry: URI " + var1 + ", buffer " + var4.getBuffer() + ", MIME type " + var4.getMIMEType());
               }

               return var4;
            } else {
               return null;
            }
         }
      }
   }

   public boolean isCached(URI var1) {
      synchronized(this.uriCache) {
         return this.uriCache.containsKey(var1);
      }
   }

   // $FF: synthetic method
   LocatorCache(Object var1) {
      this();
   }

   private class CacheDisposer implements MediaDisposer.ResourceDisposer {
      private CacheDisposer() {
      }

      public void disposeResource(Object var1) {
         if (var1 instanceof URI) {
            synchronized(LocatorCache.this.uriCache) {
               LocatorCache.this.uriCache.remove((URI)var1);
            }
         }

      }

      // $FF: synthetic method
      CacheDisposer(Object var2) {
         this();
      }
   }

   public static class CacheReference {
      private final ByteBuffer buffer;
      private String mimeType;

      public CacheReference(ByteBuffer var1, String var2) {
         this.buffer = var1;
         this.mimeType = var2;
      }

      public ByteBuffer getBuffer() {
         return this.buffer;
      }

      public String getMIMEType() {
         return this.mimeType;
      }
   }

   private static class CacheInitializer {
      private static final LocatorCache globalInstance = new LocatorCache();
   }
}
