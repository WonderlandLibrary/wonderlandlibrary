package org.apache.http.impl.execchain;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;

@NotThreadSafe
class Proxies {
  static void enhanceEntity(HttpEntityEnclosingRequest request) {
    HttpEntity entity = request.getEntity();
    if (entity != null && !entity.isRepeatable() && !isEnhanced(entity)) {
      HttpEntity proxy = (HttpEntity)Proxy.newProxyInstance(HttpEntity.class.getClassLoader(), new Class[] { HttpEntity.class }, new RequestEntityExecHandler(entity));
      request.setEntity(proxy);
    } 
  }
  
  static boolean isEnhanced(HttpEntity entity) {
    if (entity != null && Proxy.isProxyClass(entity.getClass())) {
      InvocationHandler handler = Proxy.getInvocationHandler(entity);
      return handler instanceof RequestEntityExecHandler;
    } 
    return false;
  }
  
  static boolean isRepeatable(HttpRequest request) {
    if (request instanceof HttpEntityEnclosingRequest) {
      HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
      if (entity != null) {
        if (isEnhanced(entity)) {
          RequestEntityExecHandler handler = (RequestEntityExecHandler)Proxy.getInvocationHandler(entity);
          if (!handler.isConsumed())
            return true; 
        } 
        return entity.isRepeatable();
      } 
    } 
    return true;
  }
  
  public static CloseableHttpResponse enhanceResponse(HttpResponse original, ConnectionHolder connHolder) {
    return (CloseableHttpResponse)Proxy.newProxyInstance(ResponseProxyHandler.class.getClassLoader(), new Class[] { CloseableHttpResponse.class }, new ResponseProxyHandler(original, connHolder));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\Proxies.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */