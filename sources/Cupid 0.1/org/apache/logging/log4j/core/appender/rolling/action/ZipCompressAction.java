package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipCompressAction extends AbstractAction {
  private static final int BUF_SIZE = 8192;
  
  private final File source;
  
  private final File destination;
  
  private final boolean deleteSource;
  
  private final int level;
  
  public ZipCompressAction(File source, File destination, boolean deleteSource, int level) {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(destination, "destination");
    this.source = source;
    this.destination = destination;
    this.deleteSource = deleteSource;
    this.level = level;
  }
  
  public boolean execute() throws IOException {
    return execute(this.source, this.destination, this.deleteSource, this.level);
  }
  
  public static boolean execute(File source, File destination, boolean deleteSource, int level) throws IOException {
    if (source.exists()) {
      try(FileInputStream fis = new FileInputStream(source); 
          ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination))) {
        zos.setLevel(level);
        ZipEntry zipEntry = new ZipEntry(source.getName());
        zos.putNextEntry(zipEntry);
        byte[] inbuf = new byte[8192];
        int n;
        while ((n = fis.read(inbuf)) != -1)
          zos.write(inbuf, 0, n); 
      } 
      if (deleteSource && !source.delete())
        LOGGER.warn("Unable to delete " + source.toString() + '.'); 
      return true;
    } 
    return false;
  }
  
  protected void reportException(Exception ex) {
    LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
  }
  
  public String toString() {
    return ZipCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", level=" + this.level + ", deleteSource=" + this.deleteSource + ']';
  }
  
  public File getSource() {
    return this.source;
  }
  
  public File getDestination() {
    return this.destination;
  }
  
  public boolean isDeleteSource() {
    return this.deleteSource;
  }
  
  public int getLevel() {
    return this.level;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\ZipCompressAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */