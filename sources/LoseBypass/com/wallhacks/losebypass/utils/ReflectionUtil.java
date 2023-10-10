/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ReflectionUtil {
    private static void checkDirectory(File directory, String pckgname, ArrayList<Class<?>> classes) throws ClassNotFoundException {
        String[] files;
        if (!directory.exists()) return;
        if (!directory.isDirectory()) return;
        String[] stringArray = files = directory.list();
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String file = stringArray[n2];
            if (file.endsWith(".class")) {
                try {
                    classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
                }
                catch (NoClassDefFoundError noClassDefFoundError) {}
            } else {
                File tmpDirectory = new File(directory, file);
                if (tmpDirectory.isDirectory()) {
                    ReflectionUtil.checkDirectory(tmpDirectory, pckgname + "." + file, classes);
                }
            }
            ++n2;
        }
    }

    private static void checkJarFile(JarURLConnection connection, String pckgname, ArrayList<Class<?>> classes) throws ClassNotFoundException, IOException {
        JarFile jarFile = connection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        JarEntry jarEntry = null;
        while (entries.hasMoreElements()) {
            jarEntry = entries.nextElement();
            if (jarEntry == null) return;
            String name = jarEntry.getName();
            if (!name.contains(".class") || !(name = name.substring(0, name.length() - 6).replace('/', '.')).contains(pckgname)) continue;
            try {
                classes.add(Class.forName(name));
            }
            catch (NoClassDefFoundError noClassDefFoundError) {}
        }
    }

    public static ArrayList<Class<?>> getClassesForPackage(String pckgname) throws ClassNotFoundException {
        ArrayList classes = new ArrayList();
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("Can't get class loader.");
            }
            Enumeration<URL> resources = cld.getResources(pckgname.replace('.', '/'));
            URL url = null;
            while (resources.hasMoreElements()) {
                url = resources.nextElement();
                if (url == null) return classes;
                try {
                    URLConnection connection = url.openConnection();
                    if (connection instanceof JarURLConnection) {
                        ReflectionUtil.checkJarFile((JarURLConnection)connection, pckgname, classes);
                        continue;
                    }
                    if (connection == null) throw new ClassNotFoundException(pckgname + " (" + url.getPath() + ") does not appear to be a valid package");
                    try {
                        ReflectionUtil.checkDirectory(new File(URLDecoder.decode(url.getPath(), "UTF-8")), pckgname, classes);
                    }
                    catch (UnsupportedEncodingException ex) {
                        throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Unsupported encoding)", ex);
                    }
                }
                catch (IOException ioex) {
                    throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, ioex);
                }
            }
            return classes;
        }
        catch (NullPointerException ex) {
            throw new ClassNotFoundException(pckgname + " does not appear to be a valid package (Null pointer exception)", ex);
        }
        catch (IOException ioex) {
            throw new ClassNotFoundException("IOException was thrown when trying to get all resources for " + pckgname, ioex);
        }
    }

    public static void setFieldValue(Object object, String fieldName, Object valueTobeSet) throws NoSuchFieldException, IllegalAccessException {
        Field field = ReflectionUtil.getField(object.getClass(), fieldName);
        field.setAccessible(true);
        field.set(object, valueTobeSet);
    }

    public static Object getPrivateFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = ReflectionUtil.getField(object.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    private static Field getField(Class<?> mClass, String fieldName) throws NoSuchFieldException {
        try {
            return mClass.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class<?> superClass = mClass.getSuperclass();
            if (superClass != null) return ReflectionUtil.getField(superClass, fieldName);
            throw e;
        }
    }
}

