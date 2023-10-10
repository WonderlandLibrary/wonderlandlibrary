package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

@ThreadSafe
public class ResponseDate implements HttpResponseInterceptor {
   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      int var3 = var1.getStatusLine().getStatusCode();
      if (var3 >= 200 && !var1.containsHeader("Date")) {
         String var4 = DATE_GENERATOR.getCurrentDate();
         var1.setHeader("Date", var4);
      }

   }
}
