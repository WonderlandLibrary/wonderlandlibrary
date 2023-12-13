package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.internal.PlatformDependent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class DefaultHttpDataFactory implements HttpDataFactory {
   public static final long MINSIZE = 16384L;
   private final boolean useDisk;
   private final boolean checkSize;
   private long minSize;
   private final ConcurrentMap requestFileDeleteMap = PlatformDependent.newConcurrentHashMap();

   public DefaultHttpDataFactory() {
      this.useDisk = false;
      this.checkSize = true;
      this.minSize = 16384L;
   }

   public DefaultHttpDataFactory(boolean var1) {
      this.useDisk = var1;
      this.checkSize = false;
   }

   public DefaultHttpDataFactory(long var1) {
      this.useDisk = false;
      this.checkSize = true;
      this.minSize = var1;
   }

   private List getList(HttpRequest var1) {
      Object var2 = (List)this.requestFileDeleteMap.get(var1);
      if (var2 == null) {
         var2 = new ArrayList();
         this.requestFileDeleteMap.put(var1, var2);
      }

      return (List)var2;
   }

   public Attribute createAttribute(HttpRequest var1, String var2) {
      List var4;
      if (this.useDisk) {
         DiskAttribute var5 = new DiskAttribute(var2);
         var4 = this.getList(var1);
         var4.add(var5);
         return var5;
      } else if (this.checkSize) {
         MixedAttribute var3 = new MixedAttribute(var2, this.minSize);
         var4 = this.getList(var1);
         var4.add(var3);
         return var3;
      } else {
         return new MemoryAttribute(var2);
      }
   }

   public Attribute createAttribute(HttpRequest var1, String var2, String var3) {
      List var5;
      if (this.useDisk) {
         Object var8;
         try {
            var8 = new DiskAttribute(var2, var3);
         } catch (IOException var6) {
            var8 = new MixedAttribute(var2, var3, this.minSize);
         }

         var5 = this.getList(var1);
         var5.add(var8);
         return (Attribute)var8;
      } else if (this.checkSize) {
         MixedAttribute var4 = new MixedAttribute(var2, var3, this.minSize);
         var5 = this.getList(var1);
         var5.add(var4);
         return var4;
      } else {
         try {
            return new MemoryAttribute(var2, var3);
         } catch (IOException var7) {
            throw new IllegalArgumentException(var7);
         }
      }
   }

   public FileUpload createFileUpload(HttpRequest var1, String var2, String var3, String var4, String var5, Charset var6, long var7) {
      List var10;
      if (this.useDisk) {
         DiskFileUpload var11 = new DiskFileUpload(var2, var3, var4, var5, var6, var7);
         var10 = this.getList(var1);
         var10.add(var11);
         return var11;
      } else if (this.checkSize) {
         MixedFileUpload var9 = new MixedFileUpload(var2, var3, var4, var5, var6, var7, this.minSize);
         var10 = this.getList(var1);
         var10.add(var9);
         return var9;
      } else {
         return new MemoryFileUpload(var2, var3, var4, var5, var6, var7);
      }
   }

   public void removeHttpDataFromClean(HttpRequest var1, InterfaceHttpData var2) {
      if (var2 instanceof HttpData) {
         List var3 = this.getList(var1);
         var3.remove(var2);
      }

   }

   public void cleanRequestHttpDatas(HttpRequest var1) {
      List var2 = (List)this.requestFileDeleteMap.remove(var1);
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            HttpData var4 = (HttpData)var3.next();
            var4.delete();
         }

         var2.clear();
      }

   }

   public void cleanAllHttpDatas() {
      HttpRequest var2;
      for(Iterator var1 = this.requestFileDeleteMap.keySet().iterator(); var1.hasNext(); this.requestFileDeleteMap.remove(var2)) {
         var2 = (HttpRequest)var1.next();
         List var3 = (List)this.requestFileDeleteMap.get(var2);
         if (var3 != null) {
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
               HttpData var5 = (HttpData)var4.next();
               var5.delete();
            }

            var3.clear();
         }
      }

   }
}
