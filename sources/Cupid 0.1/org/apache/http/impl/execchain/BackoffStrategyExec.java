package org.apache.http.impl.execchain;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.util.Args;

@Immutable
public class BackoffStrategyExec implements ClientExecChain {
  private final ClientExecChain requestExecutor;
  
  private final ConnectionBackoffStrategy connectionBackoffStrategy;
  
  private final BackoffManager backoffManager;
  
  public BackoffStrategyExec(ClientExecChain requestExecutor, ConnectionBackoffStrategy connectionBackoffStrategy, BackoffManager backoffManager) {
    Args.notNull(requestExecutor, "HTTP client request executor");
    Args.notNull(connectionBackoffStrategy, "Connection backoff strategy");
    Args.notNull(backoffManager, "Backoff manager");
    this.requestExecutor = requestExecutor;
    this.connectionBackoffStrategy = connectionBackoffStrategy;
    this.backoffManager = backoffManager;
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    Args.notNull(route, "HTTP route");
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    CloseableHttpResponse out = null;
    try {
      out = this.requestExecutor.execute(route, request, context, execAware);
    } catch (Exception ex) {
      if (out != null)
        out.close(); 
      if (this.connectionBackoffStrategy.shouldBackoff(ex))
        this.backoffManager.backOff(route); 
      if (ex instanceof RuntimeException)
        throw (RuntimeException)ex; 
      if (ex instanceof HttpException)
        throw (HttpException)ex; 
      if (ex instanceof IOException)
        throw (IOException)ex; 
      throw new UndeclaredThrowableException(ex);
    } 
    if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)out)) {
      this.backoffManager.backOff(route);
    } else {
      this.backoffManager.probe(route);
    } 
    return out;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\BackoffStrategyExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */