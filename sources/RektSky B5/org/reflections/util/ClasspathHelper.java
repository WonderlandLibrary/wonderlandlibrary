/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.servlet.ServletContext
 */
package org.reflections.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.servlet.ServletContext;
import org.reflections.Reflections;

public abstract class ClasspathHelper {
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader staticClassLoader() {
        return Reflections.class.getClassLoader();
    }

    public static ClassLoader[] classLoaders(ClassLoader ... classLoaders) {
        ClassLoader[] classLoaderArray;
        if (classLoaders != null && classLoaders.length != 0) {
            return classLoaders;
        }
        ClassLoader contextClassLoader = ClasspathHelper.contextClassLoader();
        ClassLoader staticClassLoader = ClasspathHelper.staticClassLoader();
        if (contextClassLoader != null) {
            if (staticClassLoader != null && contextClassLoader != staticClassLoader) {
                ClassLoader[] classLoaderArray2 = new ClassLoader[2];
                classLoaderArray2[0] = contextClassLoader;
                classLoaderArray = classLoaderArray2;
                classLoaderArray2[1] = staticClassLoader;
            } else {
                ClassLoader[] classLoaderArray3 = new ClassLoader[1];
                classLoaderArray = classLoaderArray3;
                classLoaderArray3[0] = contextClassLoader;
            }
        } else {
            classLoaderArray = new ClassLoader[]{};
        }
        return classLoaderArray;
    }

    public static Collection<URL> forPackage(String name, ClassLoader ... classLoaders) {
        return ClasspathHelper.forResource(ClasspathHelper.resourceName(name), classLoaders);
    }

    public static Collection<URL> forResource(String resourceName, ClassLoader ... classLoaders) {
        ClassLoader[] loaders;
        ArrayList<URL> result = new ArrayList<URL>();
        for (ClassLoader classLoader : loaders = ClasspathHelper.classLoaders(classLoaders)) {
            try {
                Enumeration<URL> urls = classLoader.getResources(resourceName);
                while (urls.hasMoreElements()) {
                    URL url = urls.nextElement();
                    int index = url.toExternalForm().lastIndexOf(resourceName);
                    if (index != -1) {
                        result.add(new URL(url, url.toExternalForm().substring(0, index)));
                        continue;
                    }
                    result.add(url);
                }
            }
            catch (IOException e2) {
                if (Reflections.log == null) continue;
                Reflections.log.error("error getting resources for " + resourceName, (Throwable)e2);
            }
        }
        return ClasspathHelper.distinctUrls(result);
    }

    public static URL forClass(Class<?> aClass, ClassLoader ... classLoaders) {
        ClassLoader[] loaders = ClasspathHelper.classLoaders(classLoaders);
        String resourceName = aClass.getName().replace(".", "/") + ".class";
        for (ClassLoader classLoader : loaders) {
            try {
                URL url = classLoader.getResource(resourceName);
                if (url == null) continue;
                String normalizedUrl = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf(aClass.getPackage().getName().replace(".", "/")));
                return new URL(normalizedUrl);
            }
            catch (MalformedURLException e2) {
                if (Reflections.log == null) continue;
                Reflections.log.warn("Could not get URL", (Throwable)e2);
            }
        }
        return null;
    }

    public static Collection<URL> forClassLoader() {
        return ClasspathHelper.forClassLoader(ClasspathHelper.classLoaders(new ClassLoader[0]));
    }

    public static Collection<URL> forClassLoader(ClassLoader ... classLoaders) {
        ClassLoader[] loaders;
        ArrayList<URL> result = new ArrayList<URL>();
        ClassLoader[] classLoaderArray = loaders = ClasspathHelper.classLoaders(classLoaders);
        int n2 = classLoaderArray.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            for (ClassLoader classLoader = classLoaderArray[i2]; classLoader != null; classLoader = classLoader.getParent()) {
                URL[] urls;
                if (!(classLoader instanceof URLClassLoader) || (urls = ((URLClassLoader)classLoader).getURLs()) == null) continue;
                result.addAll(Arrays.asList(urls));
            }
        }
        return ClasspathHelper.distinctUrls(result);
    }

    public static Collection<URL> forJavaClassPath() {
        ArrayList<URL> urls = new ArrayList<URL>();
        String javaClassPath = System.getProperty("java.class.path");
        if (javaClassPath != null) {
            for (String path : javaClassPath.split(File.pathSeparator)) {
                try {
                    urls.add(new File(path).toURI().toURL());
                }
                catch (Exception e2) {
                    if (Reflections.log == null) continue;
                    Reflections.log.warn("Could not get URL", (Throwable)e2);
                }
            }
        }
        return ClasspathHelper.distinctUrls(urls);
    }

    public static Collection<URL> forWebInfLib(ServletContext servletContext) {
        ArrayList<URL> urls = new ArrayList<URL>();
        Set resourcePaths = servletContext.getResourcePaths("/WEB-INF/lib");
        if (resourcePaths == null) {
            return urls;
        }
        for (Object urlString : resourcePaths) {
            try {
                urls.add(servletContext.getResource((String)urlString));
            }
            catch (MalformedURLException malformedURLException) {}
        }
        return ClasspathHelper.distinctUrls(urls);
    }

    public static URL forWebInfClasses(ServletContext servletContext) {
        block4: {
            try {
                String path = servletContext.getRealPath("/WEB-INF/classes");
                if (path != null) {
                    File file = new File(path);
                    if (file.exists()) {
                        return file.toURL();
                    }
                    break block4;
                }
                return servletContext.getResource("/WEB-INF/classes");
            }
            catch (MalformedURLException malformedURLException) {
                // empty catch block
            }
        }
        return null;
    }

    public static Collection<URL> forManifest() {
        return ClasspathHelper.forManifest(ClasspathHelper.forClassLoader());
    }

    public static Collection<URL> forManifest(URL url) {
        ArrayList<URL> result = new ArrayList<URL>();
        result.add(url);
        try {
            String classPath;
            Manifest manifest;
            String part = ClasspathHelper.cleanPath(url);
            File jarFile = new File(part);
            JarFile myJar = new JarFile(part);
            URL validUrl = ClasspathHelper.tryToGetValidUrl(jarFile.getPath(), new File(part).getParent(), part);
            if (validUrl != null) {
                result.add(validUrl);
            }
            if ((manifest = myJar.getManifest()) != null && (classPath = manifest.getMainAttributes().getValue(new Attributes.Name("Class-Path"))) != null) {
                for (String jar : classPath.split(" ")) {
                    validUrl = ClasspathHelper.tryToGetValidUrl(jarFile.getPath(), new File(part).getParent(), jar);
                    if (validUrl == null) continue;
                    result.add(validUrl);
                }
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
        return ClasspathHelper.distinctUrls(result);
    }

    public static Collection<URL> forManifest(Iterable<URL> urls) {
        ArrayList<URL> result = new ArrayList<URL>();
        for (URL url : urls) {
            result.addAll(ClasspathHelper.forManifest(url));
        }
        return ClasspathHelper.distinctUrls(result);
    }

    static URL tryToGetValidUrl(String workingDir, String path, String filename) {
        try {
            if (new File(filename).exists()) {
                return new File(filename).toURI().toURL();
            }
            if (new File(path + File.separator + filename).exists()) {
                return new File(path + File.separator + filename).toURI().toURL();
            }
            if (new File(workingDir + File.separator + filename).exists()) {
                return new File(workingDir + File.separator + filename).toURI().toURL();
            }
            if (new File(new URL(filename).getFile()).exists()) {
                return new File(new URL(filename).getFile()).toURI().toURL();
            }
        }
        catch (MalformedURLException malformedURLException) {
            // empty catch block
        }
        return null;
    }

    public static String cleanPath(URL url) {
        String path = url.getPath();
        try {
            path = URLDecoder.decode(path, "UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
        if (path.startsWith("jar:")) {
            path = path.substring("jar:".length());
        }
        if (path.startsWith("file:")) {
            path = path.substring("file:".length());
        }
        if (path.endsWith("!/")) {
            path = path.substring(0, path.lastIndexOf("!/")) + "/";
        }
        return path;
    }

    private static String resourceName(String name) {
        if (name != null) {
            String resourceName = name.replace(".", "/");
            if ((resourceName = resourceName.replace("\\", "/")).startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            return resourceName;
        }
        return null;
    }

    private static Collection<URL> distinctUrls(Collection<URL> urls) {
        LinkedHashMap<String, URL> distinct = new LinkedHashMap<String, URL>(urls.size());
        for (URL url : urls) {
            distinct.put(url.toExternalForm(), url);
        }
        return distinct.values();
    }
}

