package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;

public interface HttpResponseInterceptor {
   void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException;
}
