/*
 * Decompiled with CFR 0.152.
 */
package net.java.games.input;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import net.java.games.input.DefaultControllerEnvironment;

class PluginClassLoader
extends ClassLoader {
    private static String pluginDirectory;
    private static final FileFilter JAR_FILTER;

    public PluginClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b2 = this.loadClassData(name);
        return this.defineClass(name, b2, 0, b2.length);
    }

    private byte[] loadClassData(String name) throws ClassNotFoundException {
        if (pluginDirectory == null) {
            pluginDirectory = DefaultControllerEnvironment.libPath + File.separator + "controller";
        }
        try {
            return this.loadClassFromDirectory(name);
        }
        catch (Exception e2) {
            try {
                return this.loadClassFromJAR(name);
            }
            catch (IOException e22) {
                throw new ClassNotFoundException(name, e22);
            }
        }
    }

    private byte[] loadClassFromDirectory(String name) throws ClassNotFoundException, IOException {
        StringTokenizer tokenizer = new StringTokenizer(name, ".");
        StringBuffer path = new StringBuffer(pluginDirectory);
        while (tokenizer.hasMoreTokens()) {
            path.append(File.separator);
            path.append(tokenizer.nextToken());
        }
        path.append(".class");
        File file = new File(path.toString());
        if (!file.exists()) {
            throw new ClassNotFoundException(name);
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        assert (file.length() <= Integer.MAX_VALUE);
        int length = (int)file.length();
        byte[] bytes = new byte[length];
        int length2 = fileInputStream.read(bytes);
        assert (length == length2);
        return bytes;
    }

    private byte[] loadClassFromJAR(String name) throws ClassNotFoundException, IOException {
        File dir = new File(pluginDirectory);
        File[] jarFiles = dir.listFiles(JAR_FILTER);
        if (jarFiles == null) {
            throw new ClassNotFoundException("Could not find class " + name);
        }
        for (int i2 = 0; i2 < jarFiles.length; ++i2) {
            JarFile jarfile = new JarFile(jarFiles[i2]);
            JarEntry jarentry = jarfile.getJarEntry(name + ".class");
            if (jarentry == null) continue;
            InputStream jarInputStream = jarfile.getInputStream(jarentry);
            assert (jarentry.getSize() <= Integer.MAX_VALUE);
            int length = (int)jarentry.getSize();
            assert (length >= 0);
            byte[] bytes = new byte[length];
            int length2 = jarInputStream.read(bytes);
            assert (length == length2);
            return bytes;
        }
        throw new FileNotFoundException(name);
    }

    static {
        JAR_FILTER = new JarFileFilter();
    }

    private static class JarFileFilter
    implements FileFilter {
        private JarFileFilter() {
        }

        @Override
        public boolean accept(File file) {
            return file.getName().toUpperCase().endsWith(".JAR");
        }
    }
}

