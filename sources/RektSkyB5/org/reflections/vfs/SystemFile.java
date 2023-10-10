/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import org.reflections.vfs.SystemDir;
import org.reflections.vfs.Vfs;

public class SystemFile
implements Vfs.File {
    private final SystemDir root;
    private final File file;

    public SystemFile(SystemDir root, File file) {
        this.root = root;
        this.file = file;
    }

    @Override
    public String getName() {
        return this.file.getName();
    }

    @Override
    public String getRelativePath() {
        String filepath = this.file.getPath().replace("\\", "/");
        if (filepath.startsWith(this.root.getPath())) {
            return filepath.substring(this.root.getPath().length() + 1);
        }
        return null;
    }

    @Override
    public InputStream openInputStream() {
        try {
            return new FileInputStream(this.file);
        }
        catch (FileNotFoundException e2) {
            throw new RuntimeException(e2);
        }
    }

    public String toString() {
        return this.file.toString();
    }
}

