package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpRequestInterceptor;

@Deprecated
public interface HttpRequestInterceptorList {
  void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor);
  
  void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor, int paramInt);
  
  int getRequestInterceptorCount();
  
  HttpRequestInterceptor getRequestInterceptor(int paramInt);
  
  void clearRequestInterceptors();
  
  void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> paramClass);
  
  void setInterceptors(List<?> paramList);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpRequestInterceptorList.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */