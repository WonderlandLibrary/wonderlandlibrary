package com.sun.jna;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

class CallbackReference extends WeakReference {
  static final Map callbackMap = new WeakHashMap();
  
  static final Map allocations = new WeakHashMap();
  
  private static final Method PROXY_CALLBACK_METHOD;
  
  static {
    try {
      PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[] { (array$Ljava$lang$Object == null) ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object });
    } catch (Exception e) {
      throw new Error("Error looking up CallbackProxy.callback() method");
    } 
  }
  
  private static final Map initializers = new WeakHashMap();
  
  Pointer cbstruct;
  
  CallbackProxy proxy;
  
  Method method;
  
  static Class array$Ljava$lang$Object;
  
  static Class array$Ljava$lang$String;
  
  static Class array$Lcom$sun$jna$WString;
  
  static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
    synchronized (callbackMap) {
      if (initializer != null) {
        initializers.put(cb, initializer);
      } else {
        initializers.remove(cb);
      } 
    } 
  }
  
  static class AttachOptions extends Structure {
    public boolean daemon;
    
    public boolean detach;
    
    public String name;
  }
  
  private static ThreadGroup initializeThread(Callback cb, AttachOptions args) {
    CallbackThreadInitializer init = null;
    if (cb instanceof DefaultCallbackProxy)
      cb = ((DefaultCallbackProxy)cb).getCallback(); 
    synchronized (initializers) {
      init = (CallbackThreadInitializer)initializers.get(cb);
    } 
    ThreadGroup group = null;
    if (init != null) {
      group = init.getThreadGroup(cb);
      args.name = init.getName(cb);
      args.daemon = init.isDaemon(cb);
      args.detach = init.detach(cb);
      args.write();
    } 
    return group;
  }
  
  public static Callback getCallback(Class type, Pointer p) {
    return getCallback(type, p, false);
  }
  
  private static Callback getCallback(Class type, Pointer p, boolean direct) {
    if (p == null)
      return null; 
    if (!type.isInterface())
      throw new IllegalArgumentException("Callback type must be an interface"); 
    Map map = callbackMap;
    synchronized (map) {
      for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
        Callback callback = i.next();
        if (type.isAssignableFrom(callback.getClass())) {
          CallbackReference cbref = (CallbackReference)map.get(callback);
          Pointer cbp = (cbref != null) ? cbref.getTrampoline() : getNativeFunctionPointer(callback);
          if (p.equals(cbp))
            return callback; 
        } 
      } 
      int ctype = AltCallingConvention.class.isAssignableFrom(type) ? 1 : 0;
      Map foptions = new HashMap();
      Map options = Native.getLibraryOptions(type);
      if (options != null)
        foptions.putAll(options); 
      foptions.put("invoking-method", getCallbackMethod(type));
      NativeFunctionHandler h = new NativeFunctionHandler(p, ctype, foptions);
      Callback cb = (Callback)Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, h);
      map.put(cb, null);
      return cb;
    } 
  }
  
  private CallbackReference(Callback callback, int callingConvention, boolean direct) {
    super((T)callback);
    TypeMapper mapper = Native.getTypeMapper(callback.getClass());
    String arch = System.getProperty("os.arch").toLowerCase();
    boolean ppc = ("ppc".equals(arch) || "powerpc".equals(arch));
    if (direct) {
      Method m = getCallbackMethod(callback);
      Class[] ptypes = m.getParameterTypes();
      for (int i = 0; i < ptypes.length; i++) {
        if (ppc && (ptypes[i] == float.class || ptypes[i] == double.class)) {
          direct = false;
          break;
        } 
        if (mapper != null && mapper.getFromNativeConverter(ptypes[i]) != null) {
          direct = false;
          break;
        } 
      } 
      if (mapper != null && mapper.getToNativeConverter(m.getReturnType()) != null)
        direct = false; 
    } 
    if (direct) {
      this.method = getCallbackMethod(callback);
      Class[] nativeParamTypes = this.method.getParameterTypes();
      Class returnType = this.method.getReturnType();
      long peer = Native.createNativeCallback(callback, this.method, nativeParamTypes, returnType, callingConvention, true);
      this.cbstruct = (peer != 0L) ? new Pointer(peer) : null;
    } else {
      if (callback instanceof CallbackProxy) {
        this.proxy = (CallbackProxy)callback;
      } else {
        this.proxy = new DefaultCallbackProxy(getCallbackMethod(callback), mapper);
      } 
      Class[] nativeParamTypes = this.proxy.getParameterTypes();
      Class returnType = this.proxy.getReturnType();
      if (mapper != null) {
        for (int j = 0; j < nativeParamTypes.length; j++) {
          FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[j]);
          if (rc != null)
            nativeParamTypes[j] = rc.nativeType(); 
        } 
        ToNativeConverter tn = mapper.getToNativeConverter(returnType);
        if (tn != null)
          returnType = tn.nativeType(); 
      } 
      for (int i = 0; i < nativeParamTypes.length; i++) {
        nativeParamTypes[i] = getNativeType(nativeParamTypes[i]);
        if (!isAllowableNativeType(nativeParamTypes[i])) {
          String msg = "Callback argument " + nativeParamTypes[i] + " requires custom type conversion";
          throw new IllegalArgumentException(msg);
        } 
      } 
      returnType = getNativeType(returnType);
      if (!isAllowableNativeType(returnType)) {
        String msg = "Callback return type " + returnType + " requires custom type conversion";
        throw new IllegalArgumentException(msg);
      } 
      long peer = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType, callingConvention, false);
      this.cbstruct = (peer != 0L) ? new Pointer(peer) : null;
    } 
  }
  
  private Class getNativeType(Class cls) {
    if (Structure.class.isAssignableFrom(cls)) {
      Structure.newInstance(cls);
      if (!Structure.ByValue.class.isAssignableFrom(cls))
        return Pointer.class; 
    } else {
      if (NativeMapped.class.isAssignableFrom(cls))
        return NativeMappedConverter.getInstance(cls).nativeType(); 
      if (cls == String.class || cls == WString.class || cls == ((array$Ljava$lang$String == null) ? (array$Ljava$lang$String = class$("[Ljava.lang.String;")) : array$Ljava$lang$String) || cls == ((array$Lcom$sun$jna$WString == null) ? (array$Lcom$sun$jna$WString = class$("[Lcom.sun.jna.WString;")) : array$Lcom$sun$jna$WString) || Callback.class.isAssignableFrom(cls))
        return Pointer.class; 
    } 
    return cls;
  }
  
  private static Method checkMethod(Method m) {
    if ((m.getParameterTypes()).length > 256) {
      String msg = "Method signature exceeds the maximum parameter count: " + m;
      throw new UnsupportedOperationException(msg);
    } 
    return m;
  }
  
  static Class findCallbackClass(Class type) {
    if (!Callback.class.isAssignableFrom(type))
      throw new IllegalArgumentException(type.getName() + " is not derived from com.sun.jna.Callback"); 
    if (type.isInterface())
      return type; 
    Class[] ifaces = type.getInterfaces();
    for (int i = 0; i < ifaces.length; i++) {
      if (Callback.class.isAssignableFrom(ifaces[i]))
        try {
          getCallbackMethod(ifaces[i]);
          return ifaces[i];
        } catch (IllegalArgumentException e) {
          break;
        }  
    } 
    if (Callback.class.isAssignableFrom(type.getSuperclass()))
      return findCallbackClass(type.getSuperclass()); 
    return type;
  }
  
  private static Method getCallbackMethod(Callback callback) {
    return getCallbackMethod(findCallbackClass(callback.getClass()));
  }
  
  private static Method getCallbackMethod(Class cls) {
    Method[] pubMethods = cls.getDeclaredMethods();
    Method[] classMethods = cls.getMethods();
    Set pmethods = new HashSet(Arrays.asList((Object[])pubMethods));
    pmethods.retainAll(Arrays.asList((Object[])classMethods));
    for (Iterator i = pmethods.iterator(); i.hasNext(); ) {
      Method m = i.next();
      if (Callback.FORBIDDEN_NAMES.contains(m.getName()))
        i.remove(); 
    } 
    Method[] methods = (Method[])pmethods.toArray((Object[])new Method[pmethods.size()]);
    if (methods.length == 1)
      return checkMethod(methods[0]); 
    for (int j = 0; j < methods.length; j++) {
      Method m = methods[j];
      if ("callback".equals(m.getName()))
        return checkMethod(m); 
    } 
    String msg = "Callback must implement a single public method, or one public method named 'callback'";
    throw new IllegalArgumentException(msg);
  }
  
  private void setCallbackOptions(int options) {
    this.cbstruct.setInt(Pointer.SIZE, options);
  }
  
  public Pointer getTrampoline() {
    return this.cbstruct.getPointer(0L);
  }
  
  protected void finalize() {
    dispose();
  }
  
  protected synchronized void dispose() {
    if (this.cbstruct != null) {
      Native.freeNativeCallback(this.cbstruct.peer);
      this.cbstruct.peer = 0L;
      this.cbstruct = null;
    } 
  }
  
  private Callback getCallback() {
    return (Callback)get();
  }
  
  private static Pointer getNativeFunctionPointer(Callback cb) {
    if (Proxy.isProxyClass(cb.getClass())) {
      Object handler = Proxy.getInvocationHandler(cb);
      if (handler instanceof NativeFunctionHandler)
        return ((NativeFunctionHandler)handler).getPointer(); 
    } 
    return null;
  }
  
  public static Pointer getFunctionPointer(Callback cb) {
    return getFunctionPointer(cb, false);
  }
  
  private static Pointer getFunctionPointer(Callback cb, boolean direct) {
    Pointer fp = null;
    if (cb == null)
      return null; 
    if ((fp = getNativeFunctionPointer(cb)) != null)
      return fp; 
    int callingConvention = (cb instanceof AltCallingConvention) ? 1 : 0;
    Map map = callbackMap;
    synchronized (map) {
      CallbackReference cbref = (CallbackReference)map.get(cb);
      if (cbref == null) {
        cbref = new CallbackReference(cb, callingConvention, direct);
        map.put(cb, cbref);
        if (initializers.containsKey(cb))
          cbref.setCallbackOptions(1); 
      } 
      return cbref.getTrampoline();
    } 
  }
  
  private class DefaultCallbackProxy implements CallbackProxy {
    private Method callbackMethod;
    
    private ToNativeConverter toNative;
    
    private FromNativeConverter[] fromNative;
    
    private final CallbackReference this$0;
    
    public DefaultCallbackProxy(Method callbackMethod, TypeMapper mapper) {
      this.callbackMethod = callbackMethod;
      Class[] argTypes = callbackMethod.getParameterTypes();
      Class returnType = callbackMethod.getReturnType();
      this.fromNative = new FromNativeConverter[argTypes.length];
      if (((CallbackReference.class$com$sun$jna$NativeMapped == null) ? (CallbackReference.class$com$sun$jna$NativeMapped = CallbackReference.class$("com.sun.jna.NativeMapped")) : CallbackReference.class$com$sun$jna$NativeMapped).isAssignableFrom(returnType)) {
        this.toNative = NativeMappedConverter.getInstance(returnType);
      } else if (mapper != null) {
        this.toNative = mapper.getToNativeConverter(returnType);
      } 
      for (int i = 0; i < this.fromNative.length; i++) {
        if (((CallbackReference.class$com$sun$jna$NativeMapped == null) ? (CallbackReference.class$com$sun$jna$NativeMapped = CallbackReference.class$("com.sun.jna.NativeMapped")) : CallbackReference.class$com$sun$jna$NativeMapped).isAssignableFrom(argTypes[i])) {
          this.fromNative[i] = new NativeMappedConverter(argTypes[i]);
        } else if (mapper != null) {
          this.fromNative[i] = mapper.getFromNativeConverter(argTypes[i]);
        } 
      } 
      if (!callbackMethod.isAccessible())
        try {
          callbackMethod.setAccessible(true);
        } catch (SecurityException e) {
          throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + callbackMethod);
        }  
    }
    
    public Callback getCallback() {
      return CallbackReference.this.getCallback();
    }
    
    private Object invokeCallback(Object[] args) {
      Class[] paramTypes = this.callbackMethod.getParameterTypes();
      Object[] callbackArgs = new Object[args.length];
      for (int i = 0; i < args.length; i++) {
        Class type = paramTypes[i];
        Object arg = args[i];
        if (this.fromNative[i] != null) {
          FromNativeContext context = new CallbackParameterContext(type, this.callbackMethod, args, i);
          callbackArgs[i] = this.fromNative[i].fromNative(arg, context);
        } else {
          callbackArgs[i] = convertArgument(arg, type);
        } 
      } 
      Object result = null;
      Callback cb = getCallback();
      if (cb != null)
        try {
          result = convertResult(this.callbackMethod.invoke(cb, callbackArgs));
        } catch (IllegalArgumentException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e);
        } catch (IllegalAccessException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e);
        } catch (InvocationTargetException e) {
          Native.getCallbackExceptionHandler().uncaughtException(cb, e.getTargetException());
        }  
      for (int j = 0; j < callbackArgs.length; j++) {
        if (callbackArgs[j] instanceof Structure && !(callbackArgs[j] instanceof Structure.ByValue))
          ((Structure)callbackArgs[j]).autoWrite(); 
      } 
      return result;
    }
    
    public Object callback(Object[] args) {
      try {
        return invokeCallback(args);
      } catch (Throwable t) {
        Native.getCallbackExceptionHandler().uncaughtException(getCallback(), t);
        return null;
      } 
    }
    
    private Object convertArgument(Object value, Class dstType) {
      if (value instanceof Pointer) {
        if (dstType == ((CallbackReference.class$java$lang$String == null) ? (CallbackReference.class$java$lang$String = CallbackReference.class$("java.lang.String")) : CallbackReference.class$java$lang$String)) {
          value = ((Pointer)value).getString(0L);
        } else if (dstType == ((CallbackReference.class$com$sun$jna$WString == null) ? (CallbackReference.class$com$sun$jna$WString = CallbackReference.class$("com.sun.jna.WString")) : CallbackReference.class$com$sun$jna$WString)) {
          value = new WString(((Pointer)value).getString(0L, true));
        } else if (dstType == ((CallbackReference.array$Ljava$lang$String == null) ? (CallbackReference.array$Ljava$lang$String = CallbackReference.class$("[Ljava.lang.String;")) : CallbackReference.array$Ljava$lang$String) || dstType == ((CallbackReference.array$Lcom$sun$jna$WString == null) ? (CallbackReference.array$Lcom$sun$jna$WString = CallbackReference.class$("[Lcom.sun.jna.WString;")) : CallbackReference.array$Lcom$sun$jna$WString)) {
          value = ((Pointer)value).getStringArray(0L, (dstType == ((CallbackReference.array$Lcom$sun$jna$WString == null) ? (CallbackReference.array$Lcom$sun$jna$WString = CallbackReference.class$("[Lcom.sun.jna.WString;")) : CallbackReference.array$Lcom$sun$jna$WString)));
        } else if (((CallbackReference.class$com$sun$jna$Callback == null) ? (CallbackReference.class$com$sun$jna$Callback = CallbackReference.class$("com.sun.jna.Callback")) : CallbackReference.class$com$sun$jna$Callback).isAssignableFrom(dstType)) {
          value = CallbackReference.getCallback(dstType, (Pointer)value);
        } else if (((CallbackReference.class$com$sun$jna$Structure == null) ? (CallbackReference.class$com$sun$jna$Structure = CallbackReference.class$("com.sun.jna.Structure")) : CallbackReference.class$com$sun$jna$Structure).isAssignableFrom(dstType)) {
          Structure s = Structure.newInstance(dstType);
          if (((CallbackReference.class$com$sun$jna$Structure$ByValue == null) ? (CallbackReference.class$com$sun$jna$Structure$ByValue = CallbackReference.class$("com.sun.jna.Structure$ByValue")) : CallbackReference.class$com$sun$jna$Structure$ByValue).isAssignableFrom(dstType)) {
            byte[] buf = new byte[s.size()];
            ((Pointer)value).read(0L, buf, 0, buf.length);
            s.getPointer().write(0L, buf, 0, buf.length);
          } else {
            s.useMemory((Pointer)value);
          } 
          s.read();
          value = s;
        } 
      } else if ((boolean.class == dstType || ((CallbackReference.class$java$lang$Boolean == null) ? (CallbackReference.class$java$lang$Boolean = CallbackReference.class$("java.lang.Boolean")) : CallbackReference.class$java$lang$Boolean) == dstType) && value instanceof Number) {
        value = Function.valueOf((((Number)value).intValue() != 0));
      } 
      return value;
    }
    
    private Object convertResult(Object value) {
      if (this.toNative != null)
        value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod)); 
      if (value == null)
        return null; 
      Class cls = value.getClass();
      if (((CallbackReference.class$com$sun$jna$Structure == null) ? (CallbackReference.class$com$sun$jna$Structure = CallbackReference.class$("com.sun.jna.Structure")) : CallbackReference.class$com$sun$jna$Structure).isAssignableFrom(cls)) {
        if (((CallbackReference.class$com$sun$jna$Structure$ByValue == null) ? (CallbackReference.class$com$sun$jna$Structure$ByValue = CallbackReference.class$("com.sun.jna.Structure$ByValue")) : CallbackReference.class$com$sun$jna$Structure$ByValue).isAssignableFrom(cls))
          return value; 
        return ((Structure)value).getPointer();
      } 
      if (cls == boolean.class || cls == ((CallbackReference.class$java$lang$Boolean == null) ? (CallbackReference.class$java$lang$Boolean = CallbackReference.class$("java.lang.Boolean")) : CallbackReference.class$java$lang$Boolean))
        return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE; 
      if (cls == ((CallbackReference.class$java$lang$String == null) ? (CallbackReference.class$java$lang$String = CallbackReference.class$("java.lang.String")) : CallbackReference.class$java$lang$String) || cls == ((CallbackReference.class$com$sun$jna$WString == null) ? (CallbackReference.class$com$sun$jna$WString = CallbackReference.class$("com.sun.jna.WString")) : CallbackReference.class$com$sun$jna$WString))
        return CallbackReference.getNativeString(value, (cls == ((CallbackReference.class$com$sun$jna$WString == null) ? (CallbackReference.class$com$sun$jna$WString = CallbackReference.class$("com.sun.jna.WString")) : CallbackReference.class$com$sun$jna$WString))); 
      if (cls == ((CallbackReference.array$Ljava$lang$String == null) ? (CallbackReference.array$Ljava$lang$String = CallbackReference.class$("[Ljava.lang.String;")) : CallbackReference.array$Ljava$lang$String) || cls == ((CallbackReference.class$com$sun$jna$WString == null) ? (CallbackReference.class$com$sun$jna$WString = CallbackReference.class$("com.sun.jna.WString")) : CallbackReference.class$com$sun$jna$WString)) {
        StringArray sa = (cls == ((CallbackReference.array$Ljava$lang$String == null) ? (CallbackReference.array$Ljava$lang$String = CallbackReference.class$("[Ljava.lang.String;")) : CallbackReference.array$Ljava$lang$String)) ? new StringArray((String[])value) : new StringArray((WString[])value);
        CallbackReference.allocations.put(value, sa);
        return sa;
      } 
      if (((CallbackReference.class$com$sun$jna$Callback == null) ? (CallbackReference.class$com$sun$jna$Callback = CallbackReference.class$("com.sun.jna.Callback")) : CallbackReference.class$com$sun$jna$Callback).isAssignableFrom(cls))
        return CallbackReference.getFunctionPointer((Callback)value); 
      return value;
    }
    
    public Class[] getParameterTypes() {
      return this.callbackMethod.getParameterTypes();
    }
    
    public Class getReturnType() {
      return this.callbackMethod.getReturnType();
    }
  }
  
  private static class NativeFunctionHandler implements InvocationHandler {
    private Function function;
    
    private Map options;
    
    public NativeFunctionHandler(Pointer address, int callingConvention, Map options) {
      this.function = new Function(address, callingConvention);
      this.options = options;
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
        String str = "Proxy interface to " + this.function;
        Method m = (Method)this.options.get("invoking-method");
        Class cls = CallbackReference.findCallbackClass(m.getDeclaringClass());
        str = str + " (" + cls.getName() + ")";
        return str;
      } 
      if (Library.Handler.OBJECT_HASHCODE.equals(method))
        return new Integer(hashCode()); 
      if (Library.Handler.OBJECT_EQUALS.equals(method)) {
        Object o = args[0];
        if (o != null && Proxy.isProxyClass(o.getClass()))
          return Function.valueOf((Proxy.getInvocationHandler(o) == this)); 
        return Boolean.FALSE;
      } 
      if (Function.isVarArgs(method))
        args = Function.concatenateVarArgs(args); 
      return this.function.invoke(method.getReturnType(), args, this.options);
    }
    
    public Pointer getPointer() {
      return this.function;
    }
  }
  
  private static boolean isAllowableNativeType(Class cls) {
    return (cls == void.class || cls == Void.class || cls == boolean.class || cls == Boolean.class || cls == byte.class || cls == Byte.class || cls == short.class || cls == Short.class || cls == char.class || cls == Character.class || cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == float.class || cls == Float.class || cls == double.class || cls == Double.class || (Structure.ByValue.class.isAssignableFrom(cls) && Structure.class.isAssignableFrom(cls)) || Pointer.class.isAssignableFrom(cls));
  }
  
  private static Pointer getNativeString(Object value, boolean wide) {
    if (value != null) {
      NativeString ns = new NativeString(value.toString(), wide);
      allocations.put(value, ns);
      return ns.getPointer();
    } 
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\CallbackReference.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */