/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.reflect;

import com.viaversion.viaversion.libs.javassist.CannotCompileException;
import com.viaversion.viaversion.libs.javassist.ClassPool;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.tools.reflect.Reflection;

public class Loader
extends com.viaversion.viaversion.libs.javassist.Loader {
    protected Reflection reflection;

    public static void main(String[] args2) throws Throwable {
        Loader cl = new Loader();
        cl.run(args2);
    }

    public Loader() throws CannotCompileException, NotFoundException {
        this.delegateLoadingOf("com.viaversion.viaversion.libs.javassist.tools.reflect.Loader");
        this.reflection = new Reflection();
        ClassPool pool = ClassPool.getDefault();
        this.addTranslator(pool, this.reflection);
    }

    public boolean makeReflective(String clazz, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
        return this.reflection.makeReflective(clazz, metaobject, metaclass);
    }
}

