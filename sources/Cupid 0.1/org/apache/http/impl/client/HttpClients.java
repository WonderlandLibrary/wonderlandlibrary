package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

@Immutable
public class HttpClients {
  public static HttpClientBuilder custom() {
    return HttpClientBuilder.create();
  }
  
  public static CloseableHttpClient createDefault() {
    return HttpClientBuilder.create().build();
  }
  
  public static CloseableHttpClient createSystem() {
    return HttpClientBuilder.create().useSystemProperties().build();
  }
  
  public static CloseableHttpClient createMinimal() {
    return new MinimalHttpClient((HttpClientConnectionManager)new PoolingHttpClientConnectionManager());
  }
  
  public static CloseableHttpClient createMinimal(HttpClientConnectionManager connManager) {
    return new MinimalHttpClient(connManager);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\HttpClients.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */