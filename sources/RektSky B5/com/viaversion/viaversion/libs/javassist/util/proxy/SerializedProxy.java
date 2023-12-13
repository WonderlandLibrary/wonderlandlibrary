/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.util.proxy;

import com.viaversion.viaversion.libs.javassist.util.proxy.MethodHandler;
import com.viaversion.viaversion.libs.javassist.util.proxy.Proxy;
import com.viaversion.viaversion.libs.javassist.util.proxy.ProxyFactory;
import com.viaversion.viaversion.libs.javassist.util.proxy.ProxyObject;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SerializedProxy
implements Serializable {
    private static final long serialVersionUID = 1L;
    private String superClass;
    private String[] interfaces;
    private byte[] filterSignature;
    private MethodHandler handler;

    SerializedProxy(Class<?> proxy, byte[] sig, MethodHandler h2) {
        this.filterSignature = sig;
        this.handler = h2;
        this.superClass = proxy.getSuperclass().getName();
        Class<?>[] infs = proxy.getInterfaces();
        int n2 = infs.length;
        this.interfaces = new String[n2 - 1];
        String setterInf = ProxyObject.class.getName();
        String setterInf2 = Proxy.class.getName();
        for (int i2 = 0; i2 < n2; ++i2) {
            String name = infs[i2].getName();
            if (name.equals(setterInf) || name.equals(setterInf2)) continue;
            this.interfaces[i2] = name;
        }
    }

    protected Class<?> loadClass(final String className) throws ClassNotFoundException {
        try {
            return (Class)AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>(){

                @Override
                public Class<?> run() throws Exception {
                    ClassLoader cl = Thread.currentThread().getContextClassLoader();
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
            int n2 = this.interfaces.length;
            Class[] infs = new Class[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                infs[i2] = this.loadClass(this.interfaces[i2]);
            }
            ProxyFactory f2 = new ProxyFactory();
            f2.setSuperclass(this.loadClass(this.superClass));
            f2.setInterfaces(infs);
            Proxy proxy = (Proxy)f2.createClass(this.filterSignature).getConstructor(new Class[0]).newInstance(new Object[0]);
            proxy.setHandler(this.handler);
            return proxy;
        }
        catch (NoSuchMethodException e2) {
            throw new InvalidClassException(e2.getMessage());
        }
        catch (InvocationTargetException e3) {
            throw new InvalidClassException(e3.getMessage());
        }
        catch (ClassNotFoundException e4) {
            throw new InvalidClassException(e4.getMessage());
        }
        catch (InstantiationException e2) {
            throw new InvalidObjectException(e2.getMessage());
        }
        catch (IllegalAccessException e3) {
            throw new InvalidClassException(e3.getMessage());
        }
    }
}

