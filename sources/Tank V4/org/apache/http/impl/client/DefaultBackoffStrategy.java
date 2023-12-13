package org.apache.http.impl.client;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ConnectionBackoffStrategy;

public class DefaultBackoffStrategy implements ConnectionBackoffStrategy {
   public boolean shouldBackoff(Throwable var1) {
      return var1 instanceof SocketTimeoutException || var1 instanceof ConnectException;
   }

   public boolean shouldBackoff(HttpResponse var1) {
      return var1.getStatusLine().getStatusCode() == 503;
   }
}
