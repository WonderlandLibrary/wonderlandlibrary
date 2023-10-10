package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.protocol.HttpContext;

@Immutable
public class ResponseContentEncoding implements HttpResponseInterceptor {
   public static final String UNCOMPRESSED = "http.client.response.uncompressed";

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      HttpEntity var3 = var1.getEntity();
      if (var3 != null && var3.getContentLength() != 0L) {
         Header var4 = var3.getContentEncoding();
         if (var4 != null) {
            HeaderElement[] var5 = var4.getElements();
            boolean var6 = false;
            int var8 = var5.length;
            byte var9 = 0;
            if (var9 < var8) {
               HeaderElement var10 = var5[var9];
               String var11 = var10.getName().toLowerCase(Locale.US);
               if (!"gzip".equals(var11) && !"x-gzip".equals(var11)) {
                  if (!"deflate".equals(var11)) {
                     if ("identity".equals(var11)) {
                        return;
                     }

                     throw new HttpException("Unsupported Content-Coding: " + var10.getName());
                  }

                  var1.setEntity(new DeflateDecompressingEntity(var1.getEntity()));
                  var6 = true;
               } else {
                  var1.setEntity(new GzipDecompressingEntity(var1.getEntity()));
                  var6 = true;
               }
            }

            if (var6) {
               var1.removeHeaders("Content-Length");
               var1.removeHeaders("Content-Encoding");
               var1.removeHeaders("Content-MD5");
            }
         }
      }

   }
}
