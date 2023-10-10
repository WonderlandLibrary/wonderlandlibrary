// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.action.CommonsCompressAction;
import org.apache.logging.log4j.core.appender.rolling.action.GzCompressAction;
import org.apache.logging.log4j.core.appender.rolling.action.ZipCompressAction;
import java.io.File;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import java.util.Objects;

public enum FileExtension
{
    ZIP(".zip") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new ZipCompressAction(this.source(renameTo), this.target(compressedName), deleteSource, compressionLevel);
        }
    }, 
    GZ(".gz") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new GzCompressAction(this.source(renameTo), this.target(compressedName), deleteSource, compressionLevel);
        }
    }, 
    BZIP2(".bz2") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new CommonsCompressAction("bzip2", this.source(renameTo), this.target(compressedName), deleteSource);
        }
    }, 
    DEFLATE(".deflate") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new CommonsCompressAction("deflate", this.source(renameTo), this.target(compressedName), deleteSource);
        }
    }, 
    PACK200(".pack200") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new CommonsCompressAction("pack200", this.source(renameTo), this.target(compressedName), deleteSource);
        }
    }, 
    XZ(".xz") {
        @Override
        Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel) {
            return new CommonsCompressAction("xz", this.source(renameTo), this.target(compressedName), deleteSource);
        }
    };
    
    private final String extension;
    
    public static FileExtension lookup(final String fileExtension) {
        for (final FileExtension ext : values()) {
            if (ext.isExtensionFor(fileExtension)) {
                return ext;
            }
        }
        return null;
    }
    
    public static FileExtension lookupForFile(final String fileName) {
        for (final FileExtension ext : values()) {
            if (fileName.endsWith(ext.extension)) {
                return ext;
            }
        }
        return null;
    }
    
    private FileExtension(final String extension) {
        Objects.requireNonNull(extension, "extension");
        this.extension = extension;
    }
    
    abstract Action createCompressAction(final String renameTo, final String compressedName, final boolean deleteSource, final int compressionLevel);
    
    String getExtension() {
        return this.extension;
    }
    
    boolean isExtensionFor(final String s) {
        return s.endsWith(this.extension);
    }
    
    int length() {
        return this.extension.length();
    }
    
    File source(final String fileName) {
        return new File(fileName);
    }
    
    File target(final String fileName) {
        return new File(fileName);
    }
}
