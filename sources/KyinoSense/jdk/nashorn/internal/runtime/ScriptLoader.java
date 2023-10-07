/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.security.CodeSource;
import java.util.Objects;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.NashornLoader;

final class ScriptLoader
extends NashornLoader {
    private static final String NASHORN_PKG_PREFIX = "jdk.nashorn.internal.";
    private final Context context;

    Context getContext() {
        return this.context;
    }

    ScriptLoader(Context context) {
        super(context.getStructLoader());
        this.context = context;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        ScriptLoader.checkPackageAccess(name);
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ClassLoader appLoader = this.context.getAppLoader();
        if (appLoader == null || name.startsWith(NASHORN_PKG_PREFIX)) {
            throw new ClassNotFoundException(name);
        }
        return appLoader.loadClass(name);
    }

    synchronized Class<?> installClass(String name, byte[] data, CodeSource cs) {
        return this.defineClass(name, data, 0, data.length, Objects.requireNonNull(cs));
    }
}

