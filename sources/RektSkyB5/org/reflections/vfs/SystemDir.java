/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.vfs;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Collections;
import org.reflections.ReflectionsException;
import org.reflections.vfs.SystemFile;
import org.reflections.vfs.Vfs;

public class SystemDir
implements Vfs.Dir {
    private final File file;

    public SystemDir(File file) {
        if (!(file == null || file.isDirectory() && file.canRead())) {
            throw new RuntimeException("cannot use dir " + file);
        }
        this.file = file;
    }

    @Override
    public String getPath() {
        if (this.file == null) {
            return "/NO-SUCH-DIRECTORY/";
        }
        return this.file.getPath().replace("\\", "/");
    }

    @Override
    public Iterable<Vfs.File> getFiles() {
        if (this.file == null || !this.file.exists()) {
            return Collections.emptyList();
        }
        return () -> {
            try {
                return Files.walk(this.file.toPath(), new FileVisitOption[0]).filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).map(path -> new SystemFile(this, path.toFile())).iterator();
            }
            catch (IOException e2) {
                throw new ReflectionsException("could not get files for " + this.file, e2);
            }
        };
    }

    @Override
    public void close() {
    }

    public String toString() {
        return this.getPath();
    }
}

