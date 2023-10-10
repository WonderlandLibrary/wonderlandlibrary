// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.InvocationTargetException;

abstract class Klass
{
    private Klass() {
    }
    
    public static <T> T newInstance(final Class<T> klass) {
        try {
            return klass.getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (IllegalAccessException e) {
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e;
            throw new IllegalArgumentException(msg, e);
        }
        catch (IllegalArgumentException e2) {
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e2;
            throw new IllegalArgumentException(msg, e2);
        }
        catch (InstantiationException e3) {
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e3;
            throw new IllegalArgumentException(msg, e3);
        }
        catch (NoSuchMethodException e4) {
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e4;
            throw new IllegalArgumentException(msg, e4);
        }
        catch (SecurityException e5) {
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e5;
            throw new IllegalArgumentException(msg, e5);
        }
        catch (InvocationTargetException e6) {
            if (e6.getCause() instanceof RuntimeException) {
                throw (RuntimeException)e6.getCause();
            }
            final String msg = "Can't create an instance of " + klass + ", requires a public no-arg constructor: " + e6;
            throw new IllegalArgumentException(msg, e6);
        }
    }
}
