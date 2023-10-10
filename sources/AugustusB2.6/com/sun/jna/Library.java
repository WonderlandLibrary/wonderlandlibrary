// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import com.sun.jna.internal.ReflectionUtils;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

public interface Library
{
    public static final String OPTION_TYPE_MAPPER = "type-mapper";
    public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
    public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
    public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
    public static final String OPTION_STRING_ENCODING = "string-encoding";
    public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
    public static final String OPTION_CALLING_CONVENTION = "calling-convention";
    public static final String OPTION_OPEN_FLAGS = "open-flags";
    public static final String OPTION_CLASSLOADER = "classloader";
    
    public static class Handler implements InvocationHandler
    {
        static final Method OBJECT_TOSTRING;
        static final Method OBJECT_HASHCODE;
        static final Method OBJECT_EQUALS;
        private final NativeLibrary nativeLibrary;
        private final Class<?> interfaceClass;
        private final Map<String, Object> options;
        private final InvocationMapper invocationMapper;
        private final Map<Method, FunctionInfo> functions;
        
        public Handler(final String libname, final Class<?> interfaceClass, final Map<String, ?> options) {
            this.functions = new WeakHashMap<Method, FunctionInfo>();
            if (libname != null && "".equals(libname.trim())) {
                throw new IllegalArgumentException("Invalid library name \"" + libname + "\"");
            }
            if (!interfaceClass.isInterface()) {
                throw new IllegalArgumentException(libname + " does not implement an interface: " + interfaceClass.getName());
            }
            this.interfaceClass = interfaceClass;
            this.options = new HashMap<String, Object>(options);
            final int callingConvention = AltCallingConvention.class.isAssignableFrom(interfaceClass) ? 63 : 0;
            if (this.options.get("calling-convention") == null) {
                this.options.put("calling-convention", callingConvention);
            }
            if (this.options.get("classloader") == null) {
                this.options.put("classloader", interfaceClass.getClassLoader());
            }
            this.nativeLibrary = NativeLibrary.getInstance(libname, this.options);
            this.invocationMapper = this.options.get("invocation-mapper");
        }
        
        public NativeLibrary getNativeLibrary() {
            return this.nativeLibrary;
        }
        
        public String getLibraryName() {
            return this.nativeLibrary.getName();
        }
        
        public Class<?> getInterfaceClass() {
            return this.interfaceClass;
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, Object[] inArgs) throws Throwable {
            if (Handler.OBJECT_TOSTRING.equals(method)) {
                return "Proxy interface to " + this.nativeLibrary;
            }
            if (Handler.OBJECT_HASHCODE.equals(method)) {
                return this.hashCode();
            }
            if (Handler.OBJECT_EQUALS.equals(method)) {
                final Object o = inArgs[0];
                if (o != null && Proxy.isProxyClass(o.getClass())) {
                    return Function.valueOf(Proxy.getInvocationHandler(o) == this);
                }
                return Boolean.FALSE;
            }
            else {
                FunctionInfo f = this.functions.get(method);
                if (f == null) {
                    synchronized (this.functions) {
                        f = this.functions.get(method);
                        if (f == null) {
                            final boolean isDefault = ReflectionUtils.isDefault(method);
                            if (!isDefault) {
                                final boolean isVarArgs = Function.isVarArgs(method);
                                InvocationHandler handler = null;
                                if (this.invocationMapper != null) {
                                    handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
                                }
                                Function function = null;
                                Class<?>[] parameterTypes = null;
                                Map<String, Object> options = null;
                                if (handler == null) {
                                    function = this.nativeLibrary.getFunction(method.getName(), method);
                                    parameterTypes = method.getParameterTypes();
                                    options = new HashMap<String, Object>(this.options);
                                    options.put("invoking-method", method);
                                }
                                f = new FunctionInfo(handler, function, parameterTypes, isVarArgs, options);
                            }
                            else {
                                f = new FunctionInfo(ReflectionUtils.getMethodHandle(method));
                            }
                            this.functions.put(method, f);
                        }
                    }
                }
                if (f.methodHandle != null) {
                    return ReflectionUtils.invokeDefaultMethod(proxy, f.methodHandle, inArgs);
                }
                if (f.isVarArgs) {
                    inArgs = Function.concatenateVarArgs(inArgs);
                }
                if (f.handler != null) {
                    return f.handler.invoke(proxy, method, inArgs);
                }
                return f.function.invoke(method, f.parameterTypes, method.getReturnType(), inArgs, f.options);
            }
        }
        
        static {
            try {
                OBJECT_TOSTRING = Object.class.getMethod("toString", (Class<?>[])new Class[0]);
                OBJECT_HASHCODE = Object.class.getMethod("hashCode", (Class<?>[])new Class[0]);
                OBJECT_EQUALS = Object.class.getMethod("equals", Object.class);
            }
            catch (Exception e) {
                throw new Error("Error retrieving Object.toString() method");
            }
        }
        
        private static final class FunctionInfo
        {
            final InvocationHandler handler;
            final Function function;
            final boolean isVarArgs;
            final Object methodHandle;
            final Map<String, ?> options;
            final Class<?>[] parameterTypes;
            
            FunctionInfo(final Object mh) {
                this.handler = null;
                this.function = null;
                this.isVarArgs = false;
                this.options = null;
                this.parameterTypes = null;
                this.methodHandle = mh;
            }
            
            FunctionInfo(final InvocationHandler handler, final Function function, final Class<?>[] parameterTypes, final boolean isVarArgs, final Map<String, ?> options) {
                this.handler = handler;
                this.function = function;
                this.isVarArgs = isVarArgs;
                this.options = options;
                this.parameterTypes = parameterTypes;
                this.methodHandle = null;
            }
        }
    }
}
