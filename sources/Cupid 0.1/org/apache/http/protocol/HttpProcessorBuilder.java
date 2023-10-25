package org.apache.http.protocol;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

public class HttpProcessorBuilder {
  private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
  
  private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;
  
  public static HttpProcessorBuilder create() {
    return new HttpProcessorBuilder();
  }
  
  private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder() {
    if (this.requestChainBuilder == null)
      this.requestChainBuilder = new ChainBuilder<HttpRequestInterceptor>(); 
    return this.requestChainBuilder;
  }
  
  private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder() {
    if (this.responseChainBuilder == null)
      this.responseChainBuilder = new ChainBuilder<HttpResponseInterceptor>(); 
    return this.responseChainBuilder;
  }
  
  public HttpProcessorBuilder addFirst(HttpRequestInterceptor e) {
    if (e == null)
      return this; 
    getRequestChainBuilder().addFirst(e);
    return this;
  }
  
  public HttpProcessorBuilder addLast(HttpRequestInterceptor e) {
    if (e == null)
      return this; 
    getRequestChainBuilder().addLast(e);
    return this;
  }
  
  public HttpProcessorBuilder add(HttpRequestInterceptor e) {
    return addLast(e);
  }
  
  public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor... e) {
    if (e == null)
      return this; 
    getRequestChainBuilder().addAllFirst(e);
    return this;
  }
  
  public HttpProcessorBuilder addAllLast(HttpRequestInterceptor... e) {
    if (e == null)
      return this; 
    getRequestChainBuilder().addAllLast(e);
    return this;
  }
  
  public HttpProcessorBuilder addAll(HttpRequestInterceptor... e) {
    return addAllLast(e);
  }
  
  public HttpProcessorBuilder addFirst(HttpResponseInterceptor e) {
    if (e == null)
      return this; 
    getResponseChainBuilder().addFirst(e);
    return this;
  }
  
  public HttpProcessorBuilder addLast(HttpResponseInterceptor e) {
    if (e == null)
      return this; 
    getResponseChainBuilder().addLast(e);
    return this;
  }
  
  public HttpProcessorBuilder add(HttpResponseInterceptor e) {
    return addLast(e);
  }
  
  public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor... e) {
    if (e == null)
      return this; 
    getResponseChainBuilder().addAllFirst(e);
    return this;
  }
  
  public HttpProcessorBuilder addAllLast(HttpResponseInterceptor... e) {
    if (e == null)
      return this; 
    getResponseChainBuilder().addAllLast(e);
    return this;
  }
  
  public HttpProcessorBuilder addAll(HttpResponseInterceptor... e) {
    return addAllLast(e);
  }
  
  public HttpProcessor build() {
    return new ImmutableHttpProcessor((this.requestChainBuilder != null) ? this.requestChainBuilder.build() : null, (this.responseChainBuilder != null) ? this.responseChainBuilder.build() : null);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpProcessorBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */