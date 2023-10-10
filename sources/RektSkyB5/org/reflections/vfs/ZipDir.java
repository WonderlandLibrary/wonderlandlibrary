/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.vfs;

import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.reflections.Reflections;
import org.reflections.vfs.Vfs;
import org.reflections.vfs.ZipFile;

public class ZipDir
implements Vfs.Dir {
    final java.util.zip.ZipFile jarFile;

    public ZipDir(JarFile jarFile) {
        this.jarFile = jarFile;
    }

    @Override
    public String getPath() {
        return this.jarFile.getName();
    }

    @Override
    public Iterable<Vfs.File> getFiles() {
        return () -> this.jarFile.stream().filter(entry -> !entry.isDirectory()).map(entry -> new ZipFile(this, (ZipEntry)entry)).iterator();
    }

    @Override
    public void close() {
        block2: {
            try {
                this.jarFile.close();
            }
            catch (IOException e2) {
                if (Reflections.log == null) break block2;
                Reflections.log.warn("Could not close JarFile", (Throwable)e2);
            }
        }
    }

    public String toString() {
        return this.jarFile.getName();
    }
}

