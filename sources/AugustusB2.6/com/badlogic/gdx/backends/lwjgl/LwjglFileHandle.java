// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.backends.lwjgl;

import java.io.File;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;

public final class LwjglFileHandle extends FileHandle
{
    public LwjglFileHandle(final String fileName, final Files.FileType type) {
        super(fileName, type);
    }
    
    public LwjglFileHandle(final File file, final Files.FileType type) {
        super(file, type);
    }
    
    @Override
    public final FileHandle child(final String name) {
        if (this.file.getPath().length() == 0) {
            return new LwjglFileHandle(new File(name), this.type);
        }
        return new LwjglFileHandle(new File(this.file, name), this.type);
    }
    
    @Override
    public final FileHandle parent() {
        File parent;
        if ((parent = this.file.getParentFile()) == null) {
            if (this.type == Files.FileType.Absolute) {
                parent = new File("/");
            }
            else {
                parent = new File("");
            }
        }
        return new LwjglFileHandle(parent, this.type);
    }
    
    @Override
    public final File file() {
        if (this.type == Files.FileType.External) {
            return new File(LwjglFiles.externalPath, this.file.getPath());
        }
        if (this.type == Files.FileType.Local) {
            return new File(LwjglFiles.localPath, this.file.getPath());
        }
        return this.file;
    }
}
