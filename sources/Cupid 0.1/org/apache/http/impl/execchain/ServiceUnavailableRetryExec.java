package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class ServiceUnavailableRetryExec implements ClientExecChain {
  private final Log log = LogFactory.getLog(getClass());
  
  private final ClientExecChain requestExecutor;
  
  private final ServiceUnavailableRetryStrategy retryStrategy;
  
  public ServiceUnavailableRetryExec(ClientExecChain requestExecutor, ServiceUnavailableRetryStrategy retryStrategy) {
    Args.notNull(requestExecutor, "HTTP request executor");
    Args.notNull(retryStrategy, "Retry strategy");
    this.requestExecutor = requestExecutor;
    this.retryStrategy = retryStrategy;
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    Header[] origheaders = request.getAllHeaders();
    for (int c = 1;; c++) {
      CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
      try {
        if (this.retryStrategy.retryRequest((HttpResponse)response, c, (HttpContext)context)) {
          response.close();
          long nextInterval = this.retryStrategy.getRetryInterval();
          if (nextInterval > 0L)
            try {
              this.log.trace("Wait for " + nextInterval);
              Thread.sleep(nextInterval);
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
              throw new InterruptedIOException();
            }  
          request.setHeaders(origheaders);
        } else {
          return response;
        } 
      } catch (RuntimeException ex) {
        response.close();
        throw ex;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\ServiceUnavailableRetryExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */