// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx.files;

import java.io.FileInputStream;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.io.InputStream;
import com.badlogic.gdx.Files;
import java.io.File;

public class FileHandle
{
    protected File file;
    protected Files.FileType type;
    
    protected FileHandle() {
    }
    
    public FileHandle(final String fileName) {
        this.file = new File(fileName);
        this.type = Files.FileType.Absolute;
    }
    
    public FileHandle(final File file) {
        this.file = file;
        this.type = Files.FileType.Absolute;
    }
    
    protected FileHandle(final String fileName, final Files.FileType type) {
        this.type = type;
        this.file = new File(fileName);
    }
    
    protected FileHandle(final File file, final Files.FileType type) {
        this.file = file;
        this.type = type;
    }
    
    private String path() {
        return this.file.getPath().replace('\\', '/');
    }
    
    public final String name() {
        return this.file.getName();
    }
    
    public File file() {
        if (this.type == Files.FileType.External) {
            return new File(null.getExternalStoragePath(), this.file.getPath());
        }
        return this.file;
    }
    
    public final InputStream read() {
        if (this.type == Files.FileType.Classpath || (this.type == Files.FileType.Internal && !this.file().exists()) || (this.type == Files.FileType.Local && !this.file().exists())) {
            final InputStream input;
            if ((input = FileHandle.class.getResourceAsStream("/" + this.file.getPath().replace('\\', '/'))) == null) {
                throw new GdxRuntimeException("File not found: " + this.file + " (" + this.type + ")");
            }
            return input;
        }
        else {
            try {
                return new FileInputStream(this.file());
            }
            catch (Exception ex) {
                if (this.file().isDirectory()) {
                    throw new GdxRuntimeException("Cannot open a stream to a directory: " + this.file + " (" + this.type + ")", ex);
                }
                throw new GdxRuntimeException("Error reading file: " + this.file + " (" + this.type + ")", ex);
            }
        }
    }
    
    public FileHandle child(final String name) {
        if (this.file.getPath().length() == 0) {
            return new FileHandle(new File(name), this.type);
        }
        return new FileHandle(new File(this.file, name), this.type);
    }
    
    public FileHandle parent() {
        File parent;
        if ((parent = this.file.getParentFile()) == null) {
            if (this.type == Files.FileType.Absolute) {
                parent = new File("/");
            }
            else {
                parent = new File("");
            }
        }
        return new FileHandle(parent, this.type);
    }
    
    public final void mkdirs() {
        if (this.type == Files.FileType.Classpath) {
            throw new GdxRuntimeException("Cannot mkdirs with a classpath file: " + this.file);
        }
        if (this.type == Files.FileType.Internal) {
            throw new GdxRuntimeException("Cannot mkdirs with an internal file: " + this.file);
        }
        this.file().mkdirs();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FileHandle)) {
            return false;
        }
        final FileHandle other = (FileHandle)obj;
        return this.type == other.type && this.path().equals(other.path());
    }
    
    @Override
    public int hashCode() {
        return (37 + this.type.hashCode()) * 67 + this.path().hashCode();
    }
    
    @Override
    public String toString() {
        return this.file.getPath().replace('\\', '/');
    }
}
