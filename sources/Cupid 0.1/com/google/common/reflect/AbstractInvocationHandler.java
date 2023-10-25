package com.google.common.reflect;

import com.google.common.annotations.Beta;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import javax.annotation.Nullable;

@Beta
public abstract class AbstractInvocationHandler implements InvocationHandler {
  private static final Object[] NO_ARGS = new Object[0];
  
  public final Object invoke(Object proxy, Method method, @Nullable Object[] args) throws Throwable {
    if (args == null)
      args = NO_ARGS; 
    if (args.length == 0 && method.getName().equals("hashCode"))
      return Integer.valueOf(hashCode()); 
    if (args.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
      Object arg = args[0];
      if (arg == null)
        return Boolean.valueOf(false); 
      if (proxy == arg)
        return Boolean.valueOf(true); 
      return Boolean.valueOf((isProxyOfSameInterfaces(arg, proxy.getClass()) && equals(Proxy.getInvocationHandler(arg))));
    } 
    if (args.length == 0 && method.getName().equals("toString"))
      return toString(); 
    return handleInvocation(proxy, method, args);
  }
  
  protected abstract Object handleInvocation(Object paramObject, Method paramMethod, Object[] paramArrayOfObject) throws Throwable;
  
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
  
  public int hashCode() {
    return super.hashCode();
  }
  
  public String toString() {
    return super.toString();
  }
  
  private static boolean isProxyOfSameInterfaces(Object arg, Class<?> proxyClass) {
    return (proxyClass.isInstance(arg) || (Proxy.isProxyClass(arg.getClass()) && Arrays.equals((Object[])arg.getClass().getInterfaces(), (Object[])proxyClass.getInterfaces())));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\reflect\AbstractInvocationHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */