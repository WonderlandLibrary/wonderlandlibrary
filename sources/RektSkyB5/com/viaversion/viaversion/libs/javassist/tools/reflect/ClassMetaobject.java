/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.reflect;

import com.viaversion.viaversion.libs.javassist.tools.reflect.CannotCreateException;
import com.viaversion.viaversion.libs.javassist.tools.reflect.CannotInvokeException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassMetaobject
implements Serializable {
    private static final long serialVersionUID = 1L;
    static final String methodPrefix = "_m_";
    static final int methodPrefixLen = 3;
    private Class<?> javaClass;
    private Constructor<?>[] constructors;
    private Method[] methods;
    public static boolean useContextClassLoader = false;

    public ClassMetaobject(String[] params) {
        try {
            this.javaClass = this.getClassObject(params[0]);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("not found: " + params[0] + ", useContextClassLoader: " + Boolean.toString(useContextClassLoader), e2);
        }
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(this.javaClass.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.javaClass = this.getClassObject(in.readUTF());
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }

    private Class<?> getClassObject(String name) throws ClassNotFoundException {
        if (useContextClassLoader) {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        }
        return Class.forName(name);
    }

    public final Class<?> getJavaClass() {
        return this.javaClass;
    }

    public final String getName() {
        return this.javaClass.getName();
    }

    public final boolean isInstance(Object obj) {
        return this.javaClass.isInstance(obj);
    }

    public final Object newInstance(Object[] args) throws CannotCreateException {
        int n2 = this.constructors.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            try {
                return this.constructors[i2].newInstance(args);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                continue;
            }
            catch (InstantiationException e2) {
                throw new CannotCreateException(e2);
            }
            catch (IllegalAccessException e3) {
                throw new CannotCreateException(e3);
            }
            catch (InvocationTargetException e4) {
                throw new CannotCreateException(e4);
            }
        }
        throw new CannotCreateException("no constructor matches");
    }

    public Object trapFieldRead(String name) {
        Class<?> jc = this.getJavaClass();
        try {
            return jc.getField(name).get(null);
        }
        catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2.toString());
        }
        catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.toString());
        }
    }

    public void trapFieldWrite(String name, Object value) {
        Class<?> jc = this.getJavaClass();
        try {
            jc.getField(name).set(null, value);
        }
        catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2.toString());
        }
        catch (IllegalAccessException e3) {
            throw new RuntimeException(e3.toString());
        }
    }

    public static Object invoke(Object target, int identifier, Object[] args) throws Throwable {
        Method[] allmethods = target.getClass().getMethods();
        int n2 = allmethods.length;
        String head = methodPrefix + identifier;
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!allmethods[i2].getName().startsWith(head)) continue;
            try {
                return allmethods[i2].invoke(target, args);
            }
            catch (InvocationTargetException e2) {
                throw e2.getTargetException();
            }
            catch (IllegalAccessException e3) {
                throw new CannotInvokeException(e3);
            }
        }
        throw new CannotInvokeException("cannot find a method");
    }

    public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
        try {
            Method[] m2 = this.getReflectiveMethods();
            return m2[identifier].invoke(null, args);
        }
        catch (InvocationTargetException e2) {
            throw e2.getTargetException();
        }
        catch (IllegalAccessException e3) {
            throw new CannotInvokeException(e3);
        }
    }

    public final Method[] getReflectiveMethods() {
        int i2;
        if (this.methods != null) {
            return this.methods;
        }
        Class<?> baseclass = this.getJavaClass();
        Method[] allmethods = baseclass.getDeclaredMethods();
        int n2 = allmethods.length;
        int[] index = new int[n2];
        int max = 0;
        for (i2 = 0; i2 < n2; ++i2) {
            char c2;
            Method m2 = allmethods[i2];
            String mname = m2.getName();
            if (!mname.startsWith(methodPrefix)) continue;
            int k2 = 0;
            int j2 = 3;
            while ('0' <= (c2 = mname.charAt(j2)) && c2 <= '9') {
                k2 = k2 * 10 + c2 - 48;
                ++j2;
            }
            index[i2] = ++k2;
            if (k2 <= max) continue;
            max = k2;
        }
        this.methods = new Method[max];
        for (i2 = 0; i2 < n2; ++i2) {
            if (index[i2] <= 0) continue;
            this.methods[index[i2] - 1] = allmethods[i2];
        }
        return this.methods;
    }

    public final Method getMethod(int identifier) {
        return this.getReflectiveMethods()[identifier];
    }

    public final String getMethodName(int identifier) {
        char c2;
        String mname = this.getReflectiveMethods()[identifier].getName();
        int j2 = 3;
        while ((c2 = mname.charAt(j2++)) >= '0' && '9' >= c2) {
        }
        return mname.substring(j2);
    }

    public final Class<?>[] getParameterTypes(int identifier) {
        return this.getReflectiveMethods()[identifier].getParameterTypes();
    }

    public final Class<?> getReturnType(int identifier) {
        return this.getReflectiveMethods()[identifier].getReturnType();
    }

    public final int getMethodIndex(String originalName, Class<?>[] argTypes) throws NoSuchMethodException {
        Method[] mthds = this.getReflectiveMethods();
        for (int i2 = 0; i2 < mthds.length; ++i2) {
            if (mthds[i2] == null || !this.getMethodName(i2).equals(originalName) || !Arrays.equals(argTypes, mthds[i2].getParameterTypes())) continue;
            return i2;
        }
        throw new NoSuchMethodException("Method " + originalName + " not found");
    }
}

