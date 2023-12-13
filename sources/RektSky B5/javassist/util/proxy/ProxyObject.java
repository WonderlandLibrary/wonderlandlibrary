/*
 * Decompiled with CFR 0.152.
 */
package javassist.util.proxy;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;

public interface ProxyObject
extends Proxy {
    @Override
    public void setHandler(MethodHandler var1);

    public MethodHandler getHandler();
}

