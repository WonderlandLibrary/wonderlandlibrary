package org.apache.http.impl.execchain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.util.Args;

@Immutable
public class ProtocolExec implements ClientExecChain {
  private final Log log = LogFactory.getLog(getClass());
  
  private final ClientExecChain requestExecutor;
  
  private final HttpProcessor httpProcessor;
  
  public ProtocolExec(ClientExecChain requestExecutor, HttpProcessor httpProcessor) {
    Args.notNull(requestExecutor, "HTTP client request executor");
    Args.notNull(httpProcessor, "HTTP protocol processor");
    this.requestExecutor = requestExecutor;
    this.httpProcessor = httpProcessor;
  }
  
  void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route) throws ProtocolException {
    try {
      URI uri = request.getURI();
      if (uri != null) {
        if (route.getProxyHost() != null && !route.isTunnelled()) {
          if (!uri.isAbsolute()) {
            HttpHost target = route.getTargetHost();
            uri = URIUtils.rewriteURI(uri, target, true);
          } else {
            uri = URIUtils.rewriteURI(uri);
          } 
        } else if (uri.isAbsolute()) {
          uri = URIUtils.rewriteURI(uri, null, true);
        } else {
          uri = URIUtils.rewriteURI(uri);
        } 
        request.setURI(uri);
      } 
    } catch (URISyntaxException ex) {
      throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
    } 
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    Args.notNull(route, "HTTP route");
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    HttpRequest original = request.getOriginal();
    URI uri = null;
    if (original instanceof HttpUriRequest) {
      uri = ((HttpUriRequest)original).getURI();
    } else {
      String uriString = original.getRequestLine().getUri();
      try {
        uri = URI.create(uriString);
      } catch (IllegalArgumentException ex) {
        if (this.log.isDebugEnabled())
          this.log.debug("Unable to parse '" + uriString + "' as a valid URI; " + "request URI and Host header may be inconsistent", ex); 
      } 
    } 
    request.setURI(uri);
    rewriteRequestURI(request, route);
    HttpParams params = request.getParams();
    HttpHost virtualHost = (HttpHost)params.getParameter("http.virtual-host");
    if (virtualHost != null && virtualHost.getPort() == -1) {
      int port = route.getTargetHost().getPort();
      if (port != -1)
        virtualHost = new HttpHost(virtualHost.getHostName(), port, virtualHost.getSchemeName()); 
      if (this.log.isDebugEnabled())
        this.log.debug("Using virtual host" + virtualHost); 
    } 
    HttpHost target = null;
    if (virtualHost != null) {
      target = virtualHost;
    } else if (uri != null && uri.isAbsolute() && uri.getHost() != null) {
      target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    } 
    if (target == null)
      target = route.getTargetHost(); 
    if (uri != null) {
      String userinfo = uri.getUserInfo();
      if (userinfo != null) {
        BasicCredentialsProvider basicCredentialsProvider;
        CredentialsProvider credsProvider = context.getCredentialsProvider();
        if (credsProvider == null) {
          basicCredentialsProvider = new BasicCredentialsProvider();
          context.setCredentialsProvider((CredentialsProvider)basicCredentialsProvider);
        } 
        basicCredentialsProvider.setCredentials(new AuthScope(target), (Credentials)new UsernamePasswordCredentials(userinfo));
      } 
    } 
    context.setAttribute("http.target_host", target);
    context.setAttribute("http.route", route);
    context.setAttribute("http.request", request);
    this.httpProcessor.process((HttpRequest)request, (HttpContext)context);
    CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
    try {
      context.setAttribute("http.response", response);
      this.httpProcessor.process((HttpResponse)response, (HttpContext)context);
      return response;
    } catch (RuntimeException ex) {
      response.close();
      throw ex;
    } catch (IOException ex) {
      response.close();
      throw ex;
    } catch (HttpException ex) {
      response.close();
      throw ex;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\ProtocolExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */