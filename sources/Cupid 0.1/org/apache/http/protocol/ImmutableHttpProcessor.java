package org.apache.http.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class ImmutableHttpProcessor implements HttpProcessor {
  private final HttpRequestInterceptor[] requestInterceptors;
  
  private final HttpResponseInterceptor[] responseInterceptors;
  
  public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
    if (requestInterceptors != null) {
      int l = requestInterceptors.length;
      this.requestInterceptors = new HttpRequestInterceptor[l];
      System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
    } else {
      this.requestInterceptors = new HttpRequestInterceptor[0];
    } 
    if (responseInterceptors != null) {
      int l = responseInterceptors.length;
      this.responseInterceptors = new HttpResponseInterceptor[l];
      System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
    } else {
      this.responseInterceptors = new HttpResponseInterceptor[0];
    } 
  }
  
  public ImmutableHttpProcessor(List<HttpRequestInterceptor> requestInterceptors, List<HttpResponseInterceptor> responseInterceptors) {
    if (requestInterceptors != null) {
      int l = requestInterceptors.size();
      this.requestInterceptors = requestInterceptors.<HttpRequestInterceptor>toArray(new HttpRequestInterceptor[l]);
    } else {
      this.requestInterceptors = new HttpRequestInterceptor[0];
    } 
    if (responseInterceptors != null) {
      int l = responseInterceptors.size();
      this.responseInterceptors = responseInterceptors.<HttpResponseInterceptor>toArray(new HttpResponseInterceptor[l]);
    } else {
      this.responseInterceptors = new HttpResponseInterceptor[0];
    } 
  }
  
  @Deprecated
  public ImmutableHttpProcessor(HttpRequestInterceptorList requestInterceptors, HttpResponseInterceptorList responseInterceptors) {
    if (requestInterceptors != null) {
      int count = requestInterceptors.getRequestInterceptorCount();
      this.requestInterceptors = new HttpRequestInterceptor[count];
      for (int i = 0; i < count; i++)
        this.requestInterceptors[i] = requestInterceptors.getRequestInterceptor(i); 
    } else {
      this.requestInterceptors = new HttpRequestInterceptor[0];
    } 
    if (responseInterceptors != null) {
      int count = responseInterceptors.getResponseInterceptorCount();
      this.responseInterceptors = new HttpResponseInterceptor[count];
      for (int i = 0; i < count; i++)
        this.responseInterceptors[i] = responseInterceptors.getResponseInterceptor(i); 
    } else {
      this.responseInterceptors = new HttpResponseInterceptor[0];
    } 
  }
  
  public ImmutableHttpProcessor(HttpRequestInterceptor... requestInterceptors) {
    this(requestInterceptors, (HttpResponseInterceptor[])null);
  }
  
  public ImmutableHttpProcessor(HttpResponseInterceptor... responseInterceptors) {
    this((HttpRequestInterceptor[])null, responseInterceptors);
  }
  
  public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
    for (HttpRequestInterceptor requestInterceptor : this.requestInterceptors)
      requestInterceptor.process(request, context); 
  }
  
  public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
    for (HttpResponseInterceptor responseInterceptor : this.responseInterceptors)
      responseInterceptor.process(response, context); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\ImmutableHttpProcessor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */