package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
class CloseableHttpResponseProxy implements InvocationHandler {
  private final HttpResponse original;
  
  CloseableHttpResponseProxy(HttpResponse original) {
    this.original = original;
  }
  
  public void close() throws IOException {
    HttpEntity entity = this.original.getEntity();
    EntityUtils.consume(entity);
  }
  
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String mname = method.getName();
    if (mname.equals("close")) {
      close();
      return null;
    } 
    try {
      return method.invoke(this.original, args);
    } catch (InvocationTargetException ex) {
      Throwable cause = ex.getCause();
      if (cause != null)
        throw cause; 
      throw ex;
    } 
  }
  
  public static CloseableHttpResponse newProxy(HttpResponse original) {
    return (CloseableHttpResponse)Proxy.newProxyInstance(CloseableHttpResponseProxy.class.getClassLoader(), new Class[] { CloseableHttpResponse.class }, new CloseableHttpResponseProxy(original));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\CloseableHttpResponseProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */