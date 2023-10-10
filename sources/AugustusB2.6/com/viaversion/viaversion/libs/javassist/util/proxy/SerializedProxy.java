// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.util.proxy;

import java.io.ObjectStreamException;
import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.io.InvalidClassException;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.io.Serializable;

class SerializedProxy implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String superClass;
    private String[] interfaces;
    private byte[] filterSignature;
    private MethodHandler handler;
    
    SerializedProxy(final Class<?> proxy, final byte[] sig, final MethodHandler h) {
        this.filterSignature = sig;
        this.handler = h;
        this.superClass = proxy.getSuperclass().getName();
        final Class<?>[] infs = proxy.getInterfaces();
        final int n = infs.length;
        this.interfaces = new String[n - 1];
        final String setterInf = ProxyObject.class.getName();
        final String setterInf2 = Proxy.class.getName();
        for (int i = 0; i < n; ++i) {
            final String name = infs[i].getName();
            if (!name.equals(setterInf) && !name.equals(setterInf2)) {
                this.interfaces[i] = name;
            }
        }
    }
    
    protected Class<?> loadClass(final String className) throws ClassNotFoundException {
        try {
            return AccessController.doPrivileged((PrivilegedExceptionAction<Class<?>>)new PrivilegedExceptionAction<Class<?>>() {
                @Override
                public Class<?> run() throws Exception {
                    final ClassLoader cl = Thread.currentThread().getContextClassLoader();
                    return Class.forName(className, true, cl);
                }
            });
        }
        catch (PrivilegedActionException pae) {
            throw new RuntimeException("cannot load the class: " + className, pae.getException());
        }
    }
    
    Object readResolve() throws ObjectStreamException {
        try {
            final int n = this.interfaces.length;
            final Class<?>[] infs = (Class<?>[])new Class[n];
            for (int i = 0; i < n; ++i) {
                infs[i] = this.loadClass(this.interfaces[i]);
            }
            final ProxyFactory f = new ProxyFactory();
            f.setSuperclass(this.loadClass(this.superClass));
            f.setInterfaces(infs);
            final Proxy proxy = (Proxy)f.createClass(this.filterSignature).getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
            proxy.setHandler(this.handler);
            return proxy;
        }
        catch (NoSuchMethodException e) {
            throw new InvalidClassException(e.getMessage());
        }
        catch (InvocationTargetException e2) {
            throw new InvalidClassException(e2.getMessage());
        }
        catch (ClassNotFoundException e3) {
            throw new InvalidClassException(e3.getMessage());
        }
        catch (InstantiationException e4) {
            throw new InvalidObjectException(e4.getMessage());
        }
        catch (IllegalAccessException e5) {
            throw new InvalidClassException(e5.getMessage());
        }
    }
}
