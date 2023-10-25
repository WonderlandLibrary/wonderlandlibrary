package org.apache.http.impl.execchain;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class RetryExec implements ClientExecChain {
  private final Log log = LogFactory.getLog(getClass());
  
  private final ClientExecChain requestExecutor;
  
  private final HttpRequestRetryHandler retryHandler;
  
  public RetryExec(ClientExecChain requestExecutor, HttpRequestRetryHandler retryHandler) {
    Args.notNull(requestExecutor, "HTTP request executor");
    Args.notNull(retryHandler, "HTTP request retry handler");
    this.requestExecutor = requestExecutor;
    this.retryHandler = retryHandler;
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    Args.notNull(route, "HTTP route");
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    Header[] origheaders = request.getAllHeaders();
    for (int execCount = 1;; execCount++) {
      try {
        return this.requestExecutor.execute(route, request, context, execAware);
      } catch (IOException ex) {
        if (execAware != null && execAware.isAborted()) {
          this.log.debug("Request has been aborted");
          throw ex;
        } 
        if (this.retryHandler.retryRequest(ex, execCount, (HttpContext)context)) {
          if (this.log.isInfoEnabled())
            this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage()); 
          if (this.log.isDebugEnabled())
            this.log.debug(ex.getMessage(), ex); 
          if (!Proxies.isRepeatable((HttpRequest)request)) {
            this.log.debug("Cannot retry non-repeatable request");
            throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity", ex);
          } 
          request.setHeaders(origheaders);
          if (this.log.isInfoEnabled())
            this.log.info("Retrying request to " + route); 
        } else {
          if (ex instanceof NoHttpResponseException) {
            NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
            updatedex.setStackTrace(ex.getStackTrace());
            throw updatedex;
          } 
          throw ex;
        } 
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\RetryExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */