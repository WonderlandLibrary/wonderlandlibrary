/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.SecureClassLoader;

abstract class NashornLoader
extends SecureClassLoader {
    private static final String OBJECTS_PKG = "jdk.nashorn.internal.objects";
    private static final String RUNTIME_PKG = "jdk.nashorn.internal.runtime";
    private static final String RUNTIME_ARRAYS_PKG = "jdk.nashorn.internal.runtime.arrays";
    private static final String RUNTIME_LINKER_PKG = "jdk.nashorn.internal.runtime.linker";
    private static final String SCRIPTS_PKG = "jdk.nashorn.internal.scripts";
    private static final Permission[] SCRIPT_PERMISSIONS = new Permission[]{new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.linker"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.objects"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.scripts"), new RuntimePermission("accessClassInPackage.jdk.nashorn.internal.runtime.arrays")};

    NashornLoader(ClassLoader parent) {
        super(parent);
    }

    protected static void checkPackageAccess(String name) {
        SecurityManager sm;
        int i = name.lastIndexOf(46);
        if (i != -1 && (sm = System.getSecurityManager()) != null) {
            String pkgName;
            switch (pkgName = name.substring(0, i)) {
                case "jdk.nashorn.internal.runtime": 
                case "jdk.nashorn.internal.runtime.arrays": 
                case "jdk.nashorn.internal.runtime.linker": 
                case "jdk.nashorn.internal.objects": 
                case "jdk.nashorn.internal.scripts": {
                    break;
                }
                default: {
                    sm.checkPackageAccess(pkgName);
                }
            }
        }
    }

    @Override
    protected PermissionCollection getPermissions(CodeSource codesource) {
        Permissions permCollection = new Permissions();
        for (Permission perm : SCRIPT_PERMISSIONS) {
            permCollection.add(perm);
        }
        return permCollection;
    }

    static ClassLoader createClassLoader(String classPath, ClassLoader parent) {
        URL[] urls = NashornLoader.pathToURLs(classPath);
        return URLClassLoader.newInstance(urls, parent);
    }

    private static URL[] pathToURLs(String path) {
        String[] components = path.split(File.pathSeparator);
        URL[] urls = new URL[components.length];
        int count = 0;
        while (count < components.length) {
            URL url = NashornLoader.fileToURL(new File(components[count]));
            if (url == null) continue;
            urls[count++] = url;
        }
        if (urls.length != count) {
            URL[] tmp = new URL[count];
            System.arraycopy(urls, 0, tmp, 0, count);
            urls = tmp;
        }
        return urls;
    }

    private static URL fileToURL(File file) {
        String name;
        try {
            name = file.getCanonicalPath();
        }
        catch (IOException e) {
            name = file.getAbsolutePath();
        }
        name = name.replace(File.separatorChar, '/');
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        if (!file.isFile()) {
            name = name + "/";
        }
        try {
            return new URL("file", "", name);
        }
        catch (MalformedURLException e) {
            throw new IllegalArgumentException("file");
        }
    }
}

