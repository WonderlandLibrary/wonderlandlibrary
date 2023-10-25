/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Java7Support {
    private static final boolean IS_JAVA7;
    private static Method isSymbolicLink;
    private static Method delete;
    private static Method toPath;
    private static Method exists;
    private static Method toFile;
    private static Method readSymlink;
    private static Method createSymlink;
    private static Object emptyLinkOpts;
    private static Object emptyFileAttributes;

    Java7Support() {
    }

    public static boolean isSymLink(File file) {
        try {
            Object path = toPath.invoke(file, new Object[0]);
            Boolean result = (Boolean)isSymbolicLink.invoke(null, path);
            return result;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static File readSymbolicLink(File symlink) throws IOException {
        try {
            Object path = toPath.invoke(symlink, new Object[0]);
            Object resultPath = readSymlink.invoke(null, path);
            return (File)toFile.invoke(resultPath, new Object[0]);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean exists(File file) throws IOException {
        try {
            Object path = toPath.invoke(file, new Object[0]);
            Boolean result = (Boolean)exists.invoke(null, path, emptyLinkOpts);
            return result;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw (RuntimeException)e.getTargetException();
        }
    }

    public static File createSymbolicLink(File symlink, File target) throws IOException {
        try {
            if (!Java7Support.exists(symlink)) {
                Object link = toPath.invoke(symlink, new Object[0]);
                Object path = createSymlink.invoke(null, link, toPath.invoke(target, new Object[0]), emptyFileAttributes);
                return (File)toFile.invoke(path, new Object[0]);
            }
            return symlink;
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            throw (IOException)targetException;
        }
    }

    public static void delete(File file) throws IOException {
        try {
            Object path = toPath.invoke(file, new Object[0]);
            delete.invoke(null, path);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e) {
            throw (IOException)e.getTargetException();
        }
    }

    public static boolean isAtLeastJava7() {
        return IS_JAVA7;
    }

    static {
        boolean isJava7x = true;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class<?> files = cl.loadClass("java.nio.file.Files");
            Class<?> path = cl.loadClass("java.nio.file.Path");
            Class<?> fa = cl.loadClass("java.nio.file.attribute.FileAttribute");
            Class<?> linkOption = cl.loadClass("java.nio.file.LinkOption");
            isSymbolicLink = files.getMethod("isSymbolicLink", path);
            delete = files.getMethod("delete", path);
            readSymlink = files.getMethod("readSymbolicLink", path);
            emptyFileAttributes = Array.newInstance(fa, 0);
            createSymlink = files.getMethod("createSymbolicLink", path, path, emptyFileAttributes.getClass());
            emptyLinkOpts = Array.newInstance(linkOption, 0);
            exists = files.getMethod("exists", path, emptyLinkOpts.getClass());
            toPath = File.class.getMethod("toPath", new Class[0]);
            toFile = path.getMethod("toFile", new Class[0]);
        }
        catch (ClassNotFoundException e) {
            isJava7x = false;
        }
        catch (NoSuchMethodException e) {
            isJava7x = false;
        }
        IS_JAVA7 = isJava7x;
    }
}

