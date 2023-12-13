package org.apache.http.impl.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.ConnectionBackoffStrategy;

public class NullBackoffStrategy implements ConnectionBackoffStrategy {
   public boolean shouldBackoff(Throwable var1) {
      return false;
   }

   public boolean shouldBackoff(HttpResponse var1) {
      return false;
   }
}
