/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.util.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.java.games.util.plugins.PluginLoader;

public class Plugins {
    static final boolean DEBUG = true;
    List pluginList = new ArrayList();

    public Plugins(File pluginRoot) throws IOException {
        this.scanPlugins(pluginRoot);
    }

    private void scanPlugins(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new FileNotFoundException("Plugin directory " + dir.getName() + " not found.");
        }
        for (int i2 = 0; i2 < files.length; ++i2) {
            File f2 = files[i2];
            if (f2.getName().endsWith(".jar")) {
                this.processJar(f2);
                continue;
            }
            if (!f2.isDirectory()) continue;
            this.scanPlugins(f2);
        }
    }

    private void processJar(File f2) {
        try {
            System.out.println("Scanning jar: " + f2.getName());
            PluginLoader loader = new PluginLoader(f2);
            JarFile jf = new JarFile(f2);
            Enumeration<JarEntry> en = jf.entries();
            while (en.hasMoreElements()) {
                JarEntry je = en.nextElement();
                System.out.println("Examining file : " + je.getName());
                if (!je.getName().endsWith("Plugin.class")) continue;
                System.out.println("Found candidate class: " + je.getName());
                String cname = je.getName();
                cname = cname.substring(0, cname.length() - 6);
                Class<?> pc = loader.loadClass(cname = cname.replace('/', '.'));
                if (!loader.attemptPluginDefine(pc)) continue;
                System.out.println("Adding class to plugins:" + pc.getName());
                this.pluginList.add(pc);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public Class[] get() {
        Class[] pluginArray = new Class[this.pluginList.size()];
        return this.pluginList.toArray(pluginArray);
    }

    public Class[] getImplementsAny(Class[] interfaces) {
        ArrayList<Class> matchList = new ArrayList<Class>(this.pluginList.size());
        HashSet<Class> interfaceSet = new HashSet<Class>();
        for (int i2 = 0; i2 < interfaces.length; ++i2) {
            interfaceSet.add(interfaces[i2]);
        }
        Iterator i3 = this.pluginList.iterator();
        while (i3.hasNext()) {
            Class pluginClass = (Class)i3.next();
            if (!this.classImplementsAny(pluginClass, interfaceSet)) continue;
            matchList.add(pluginClass);
        }
        Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }

    private boolean classImplementsAny(Class testClass, Set interfaces) {
        int i2;
        if (testClass == null) {
            return false;
        }
        Class<?>[] implementedInterfaces = testClass.getInterfaces();
        for (i2 = 0; i2 < implementedInterfaces.length; ++i2) {
            if (!interfaces.contains(implementedInterfaces[i2])) continue;
            return true;
        }
        for (i2 = 0; i2 < implementedInterfaces.length; ++i2) {
            if (!this.classImplementsAny(implementedInterfaces[i2], interfaces)) continue;
            return true;
        }
        return this.classImplementsAny(testClass.getSuperclass(), interfaces);
    }

    public Class[] getImplementsAll(Class[] interfaces) {
        ArrayList<Class> matchList = new ArrayList<Class>(this.pluginList.size());
        HashSet<Class> interfaceSet = new HashSet<Class>();
        for (int i2 = 0; i2 < interfaces.length; ++i2) {
            interfaceSet.add(interfaces[i2]);
        }
        Iterator i3 = this.pluginList.iterator();
        while (i3.hasNext()) {
            Class pluginClass = (Class)i3.next();
            if (!this.classImplementsAll(pluginClass, interfaceSet)) continue;
            matchList.add(pluginClass);
        }
        Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }

    private boolean classImplementsAll(Class testClass, Set interfaces) {
        int i2;
        if (testClass == null) {
            return false;
        }
        Class<?>[] implementedInterfaces = testClass.getInterfaces();
        for (i2 = 0; i2 < implementedInterfaces.length; ++i2) {
            if (!interfaces.contains(implementedInterfaces[i2])) continue;
            interfaces.remove(implementedInterfaces[i2]);
            if (interfaces.size() != 0) continue;
            return true;
        }
        for (i2 = 0; i2 < implementedInterfaces.length; ++i2) {
            if (!this.classImplementsAll(implementedInterfaces[i2], interfaces)) continue;
            return true;
        }
        return this.classImplementsAll(testClass.getSuperclass(), interfaces);
    }

    public Class[] getExtends(Class superclass) {
        ArrayList<Class> matchList = new ArrayList<Class>(this.pluginList.size());
        Iterator i2 = this.pluginList.iterator();
        while (i2.hasNext()) {
            Class pluginClass = (Class)i2.next();
            if (!this.classExtends(pluginClass, superclass)) continue;
            matchList.add(pluginClass);
        }
        Class[] pluginArray = new Class[matchList.size()];
        return matchList.toArray(pluginArray);
    }

    private boolean classExtends(Class testClass, Class superclass) {
        if (testClass == null) {
            return false;
        }
        if (testClass == superclass) {
            return true;
        }
        return this.classExtends(testClass.getSuperclass(), superclass);
    }
}

