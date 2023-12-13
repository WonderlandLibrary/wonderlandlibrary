// 
// Decompiled by Procyon v0.6.0
// 

package javassist.util.proxy;

import java.lang.reflect.Method;

public interface MethodHandler
{
    Object invoke(final Object p0, final Method p1, final Method p2, final Object[] p3) throws Throwable;
}
