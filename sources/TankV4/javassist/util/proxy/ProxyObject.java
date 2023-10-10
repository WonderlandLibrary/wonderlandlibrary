package javassist.util.proxy;

public interface ProxyObject extends Proxy {
   void setHandler(MethodHandler var1);

   MethodHandler getHandler();
}
