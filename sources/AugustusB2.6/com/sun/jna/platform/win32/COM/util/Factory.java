// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM.util;

import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.OaIdl;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeoutException;
import com.sun.jna.platform.win32.COM.IDispatchCallback;
import com.sun.jna.platform.win32.COM.COMException;
import java.util.concurrent.Callable;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.COM.util.annotation.ComObject;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import com.sun.jna.platform.win32.COM.IDispatch;

public class Factory extends ObjectFactory
{
    private ComThread comThread;
    
    public Factory() {
        this(new ComThread("Default Factory COM Thread", 5000L, new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
            }
        }));
    }
    
    public Factory(final ComThread comThread) {
        this.comThread = comThread;
    }
    
    @Override
    public <T> T createProxy(final Class<T> comInterface, final IDispatch dispatch) {
        final T result = super.createProxy(comInterface, dispatch);
        final ProxyObject2 po2 = new ProxyObject2(result);
        final Object proxy = Proxy.newProxyInstance(comInterface.getClassLoader(), new Class[] { comInterface }, po2);
        return (T)proxy;
    }
    
    @Override
    Guid.GUID discoverClsId(final ComObject annotation) {
        return this.runInComThread((Callable<Guid.GUID>)new Callable<Guid.GUID>() {
            @Override
            public Guid.GUID call() throws Exception {
                return ObjectFactory.this.discoverClsId(annotation);
            }
        });
    }
    
    @Override
    public <T> T fetchObject(final Class<T> comInterface) throws COMException {
        return this.runInComThread((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return (T)ObjectFactory.this.fetchObject((Class<Object>)comInterface);
            }
        });
    }
    
    @Override
    public <T> T createObject(final Class<T> comInterface) {
        return this.runInComThread((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return (T)ObjectFactory.this.createObject((Class<Object>)comInterface);
            }
        });
    }
    
    @Override
    IDispatchCallback createDispatchCallback(final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
        return new CallbackProxy2(this, comEventCallbackInterface, comEventCallbackListener);
    }
    
    @Override
    public IRunningObjectTable getRunningObjectTable() {
        return super.getRunningObjectTable();
    }
    
    private <T> T runInComThread(final Callable<T> callable) {
        try {
            return this.comThread.execute(callable);
        }
        catch (TimeoutException ex) {
            throw new RuntimeException(ex);
        }
        catch (InterruptedException ex2) {
            throw new RuntimeException(ex2);
        }
        catch (ExecutionException ex3) {
            Throwable cause = ex3.getCause();
            if (cause instanceof RuntimeException) {
                appendStacktrace(ex3, cause);
                throw (RuntimeException)cause;
            }
            if (cause instanceof InvocationTargetException) {
                cause = ((InvocationTargetException)cause).getTargetException();
                if (cause instanceof RuntimeException) {
                    appendStacktrace(ex3, cause);
                    throw (RuntimeException)cause;
                }
            }
            throw new RuntimeException(ex3);
        }
    }
    
    private static void appendStacktrace(final Exception caughtException, final Throwable toBeThrown) {
        final StackTraceElement[] upperTrace = caughtException.getStackTrace();
        final StackTraceElement[] lowerTrace = toBeThrown.getStackTrace();
        final StackTraceElement[] trace = new StackTraceElement[upperTrace.length + lowerTrace.length];
        System.arraycopy(upperTrace, 0, trace, lowerTrace.length, upperTrace.length);
        System.arraycopy(lowerTrace, 0, trace, 0, lowerTrace.length);
        toBeThrown.setStackTrace(trace);
    }
    
    public ComThread getComThread() {
        return this.comThread;
    }
    
    private class ProxyObject2 implements InvocationHandler
    {
        private final Object delegate;
        
        public ProxyObject2(final Object delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            if (args != null) {
                for (int i = 0; i < args.length; ++i) {
                    if (args[i] != null && Proxy.isProxyClass(args[i].getClass())) {
                        final InvocationHandler ih = Proxy.getInvocationHandler(args[i]);
                        if (ih instanceof ProxyObject2) {
                            args[i] = ((ProxyObject2)ih).delegate;
                        }
                    }
                }
            }
            return Factory.this.runInComThread((Callable<Object>)new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    return method.invoke(ProxyObject2.this.delegate, args);
                }
            });
        }
    }
    
    private class CallbackProxy2 extends CallbackProxy
    {
        public CallbackProxy2(final ObjectFactory factory, final Class<?> comEventCallbackInterface, final IComEventCallbackListener comEventCallbackListener) {
            super(factory, comEventCallbackInterface, comEventCallbackListener);
        }
        
        @Override
        public WinNT.HRESULT Invoke(final OaIdl.DISPID dispIdMember, final Guid.REFIID riid, final WinDef.LCID lcid, final WinDef.WORD wFlags, final OleAuto.DISPPARAMS.ByReference pDispParams, final Variant.VARIANT.ByReference pVarResult, final OaIdl.EXCEPINFO.ByReference pExcepInfo, final IntByReference puArgErr) {
            ComThread.setComThread(true);
            try {
                return super.Invoke(dispIdMember, riid, lcid, wFlags, pDispParams, pVarResult, pExcepInfo, puArgErr);
            }
            finally {
                ComThread.setComThread(false);
            }
        }
    }
}
