/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.util.plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PluginLoader
extends URLClassLoader {
    static final boolean DEBUG = false;
    File parentDir;
    boolean localDLLs = true;
    static /* synthetic */ Class class$net$java$games$util$plugins$Plugin;

    public PluginLoader(File jf) throws MalformedURLException {
        super(new URL[]{jf.toURL()}, Thread.currentThread().getContextClassLoader());
        this.parentDir = jf.getParentFile();
        if (System.getProperty("net.java.games.util.plugins.nolocalnative") != null) {
            this.localDLLs = false;
        }
    }

    protected String findLibrary(String libname) {
        if (this.localDLLs) {
            String libpath = this.parentDir.getPath() + File.separator + System.mapLibraryName(libname);
            return libpath;
        }
        return super.findLibrary(libname);
    }

    public boolean attemptPluginDefine(Class pc) {
        return !pc.isInterface() && this.classImplementsPlugin(pc);
    }

    private boolean classImplementsPlugin(Class testClass) {
        int i;
        if (testClass == null) {
            return false;
        }
        Class<?>[] implementedInterfaces = testClass.getInterfaces();
        for (i = 0; i < implementedInterfaces.length; ++i) {
            if (implementedInterfaces[i] != (class$net$java$games$util$plugins$Plugin == null ? PluginLoader.class$("net.java.games.util.plugins.Plugin") : class$net$java$games$util$plugins$Plugin)) continue;
            return true;
        }
        for (i = 0; i < implementedInterfaces.length; ++i) {
            if (!this.classImplementsPlugin(implementedInterfaces[i])) continue;
            return true;
        }
        return this.classImplementsPlugin(testClass.getSuperclass());
    }
}

