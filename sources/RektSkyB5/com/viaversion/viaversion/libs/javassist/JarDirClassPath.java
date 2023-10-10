/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist;

import com.viaversion.viaversion.libs.javassist.ClassPath;
import com.viaversion.viaversion.libs.javassist.JarClassPath;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URL;

final class JarDirClassPath
implements ClassPath {
    JarClassPath[] jars;

    JarDirClassPath(String dirName) throws NotFoundException {
        File[] files = new File(dirName).listFiles(new FilenameFilter(){

            @Override
            public boolean accept(File dir, String name) {
                return (name = name.toLowerCase()).endsWith(".jar") || name.endsWith(".zip");
            }
        });
        if (files != null) {
            this.jars = new JarClassPath[files.length];
            for (int i2 = 0; i2 < files.length; ++i2) {
                this.jars[i2] = new JarClassPath(files[i2].getPath());
            }
        }
    }

    @Override
    public InputStream openClassfile(String classname) throws NotFoundException {
        if (this.jars != null) {
            for (int i2 = 0; i2 < this.jars.length; ++i2) {
                InputStream is = this.jars[i2].openClassfile(classname);
                if (is == null) continue;
                return is;
            }
        }
        return null;
    }

    @Override
    public URL find(String classname) {
        if (this.jars != null) {
            for (int i2 = 0; i2 < this.jars.length; ++i2) {
                URL url = this.jars[i2].find(classname);
                if (url == null) continue;
                return url;
            }
        }
        return null;
    }
}

