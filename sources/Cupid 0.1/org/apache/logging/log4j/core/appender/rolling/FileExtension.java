package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.util.Objects;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.CommonsCompressAction;
import org.apache.logging.log4j.core.appender.rolling.action.GzCompressAction;
import org.apache.logging.log4j.core.appender.rolling.action.ZipCompressAction;

public enum FileExtension {
  ZIP(".zip") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new ZipCompressAction(source(renameTo), target(compressedName), deleteSource, compressionLevel);
    }
  },
  GZ(".gz") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new GzCompressAction(source(renameTo), target(compressedName), deleteSource, compressionLevel);
    }
  },
  BZIP2(".bz2") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new CommonsCompressAction("bzip2", source(renameTo), target(compressedName), deleteSource);
    }
  },
  DEFLATE(".deflate") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new CommonsCompressAction("deflate", source(renameTo), target(compressedName), deleteSource);
    }
  },
  PACK200(".pack200") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new CommonsCompressAction("pack200", source(renameTo), target(compressedName), deleteSource);
    }
  },
  XZ(".xz") {
    Action createCompressAction(String renameTo, String compressedName, boolean deleteSource, int compressionLevel) {
      return (Action)new CommonsCompressAction("xz", source(renameTo), target(compressedName), deleteSource);
    }
  };
  
  private final String extension;
  
  public static FileExtension lookup(String fileExtension) {
    for (FileExtension ext : values()) {
      if (ext.isExtensionFor(fileExtension))
        return ext; 
    } 
    return null;
  }
  
  public static FileExtension lookupForFile(String fileName) {
    for (FileExtension ext : values()) {
      if (fileName.endsWith(ext.extension))
        return ext; 
    } 
    return null;
  }
  
  FileExtension(String extension) {
    Objects.requireNonNull(extension, "extension");
    this.extension = extension;
  }
  
  String getExtension() {
    return this.extension;
  }
  
  boolean isExtensionFor(String s) {
    return s.endsWith(this.extension);
  }
  
  int length() {
    return this.extension.length();
  }
  
  File source(String fileName) {
    return new File(fileName);
  }
  
  File target(String fileName) {
    return new File(fileName);
  }
  
  abstract Action createCompressAction(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\FileExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */