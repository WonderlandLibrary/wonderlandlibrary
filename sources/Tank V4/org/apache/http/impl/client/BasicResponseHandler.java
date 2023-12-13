package org.apache.http.impl.client;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

@Immutable
public class BasicResponseHandler implements ResponseHandler {
   public String handleResponse(HttpResponse var1) throws HttpResponseException, IOException {
      StatusLine var2 = var1.getStatusLine();
      HttpEntity var3 = var1.getEntity();
      if (var2.getStatusCode() >= 300) {
         EntityUtils.consume(var3);
         throw new HttpResponseException(var2.getStatusCode(), var2.getReasonPhrase());
      } else {
         return var3 == null ? null : EntityUtils.toString(var3);
      }
   }

   public Object handleResponse(HttpResponse var1) throws ClientProtocolException, IOException {
      return this.handleResponse(var1);
   }
}
