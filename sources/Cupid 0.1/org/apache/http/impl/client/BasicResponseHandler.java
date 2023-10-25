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
public class BasicResponseHandler implements ResponseHandler<String> {
  public String handleResponse(HttpResponse response) throws HttpResponseException, IOException {
    StatusLine statusLine = response.getStatusLine();
    HttpEntity entity = response.getEntity();
    if (statusLine.getStatusCode() >= 300) {
      EntityUtils.consume(entity);
      throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
    } 
    return (entity == null) ? null : EntityUtils.toString(entity);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\BasicResponseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */