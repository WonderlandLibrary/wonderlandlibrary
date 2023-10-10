// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist;

import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import java.io.File;
import java.util.jar.JarEntry;
import java.util.Collections;
import java.util.HashSet;
import java.util.jar.JarFile;
import java.util.Set;

final class JarClassPath implements ClassPath
{
    Set<String> jarfileEntries;
    String jarfileURL;
    
    JarClassPath(final String pathname) throws NotFoundException {
        JarFile jarfile = null;
        try {
            jarfile = new JarFile(pathname);
            this.jarfileEntries = new HashSet<String>();
            for (final JarEntry je : Collections.list(jarfile.entries())) {
                if (je.getName().endsWith(".class")) {
                    this.jarfileEntries.add(je.getName());
                }
            }
            this.jarfileURL = new File(pathname).getCanonicalFile().toURI().toURL().toString();
            return;
        }
        catch (IOException ex) {}
        finally {
            if (null != jarfile) {
                try {
                    jarfile.close();
                }
                catch (IOException ex2) {}
            }
        }
        throw new NotFoundException(pathname);
    }
    
    @Override
    public InputStream openClassfile(final String classname) throws NotFoundException {
        final URL jarURL = this.find(classname);
        if (null != jarURL) {
            try {
                if (ClassPool.cacheOpenedJarFile) {
                    return jarURL.openConnection().getInputStream();
                }
                final URLConnection con = jarURL.openConnection();
                con.setUseCaches(false);
                return con.getInputStream();
            }
            catch (IOException e) {
                throw new NotFoundException("broken jar file?: " + classname);
            }
        }
        return null;
    }
    
    @Override
    public URL find(final String classname) {
        final String jarname = classname.replace('.', '/') + ".class";
        if (this.jarfileEntries.contains(jarname)) {
            try {
                return new URL(String.format("jar:%s!/%s", this.jarfileURL, jarname));
            }
            catch (MalformedURLException ex) {}
        }
        return null;
    }
    
    @Override
    public String toString() {
        return (this.jarfileURL == null) ? "<null>" : this.jarfileURL.toString();
    }
}
