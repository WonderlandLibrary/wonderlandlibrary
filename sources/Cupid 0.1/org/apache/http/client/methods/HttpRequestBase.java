package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.params.HttpProtocolParams;

@NotThreadSafe
public abstract class HttpRequestBase extends AbstractExecutionAwareRequest implements HttpUriRequest, Configurable {
  private ProtocolVersion version;
  
  private URI uri;
  
  private RequestConfig config;
  
  public abstract String getMethod();
  
  public void setProtocolVersion(ProtocolVersion version) {
    this.version = version;
  }
  
  public ProtocolVersion getProtocolVersion() {
    return (this.version != null) ? this.version : HttpProtocolParams.getVersion(getParams());
  }
  
  public URI getURI() {
    return this.uri;
  }
  
  public RequestLine getRequestLine() {
    String method = getMethod();
    ProtocolVersion ver = getProtocolVersion();
    URI uri = getURI();
    String uritext = null;
    if (uri != null)
      uritext = uri.toASCIIString(); 
    if (uritext == null || uritext.length() == 0)
      uritext = "/"; 
    return (RequestLine)new BasicRequestLine(method, uritext, ver);
  }
  
  public RequestConfig getConfig() {
    return this.config;
  }
  
  public void setConfig(RequestConfig config) {
    this.config = config;
  }
  
  public void setURI(URI uri) {
    this.uri = uri;
  }
  
  public void started() {}
  
  public void releaseConnection() {
    reset();
  }
  
  public String toString() {
    return getMethod() + " " + getURI() + " " + getProtocolVersion();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\client\methods\HttpRequestBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */