package org.apache.logging.log4j.core.appender.rolling.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public final class GzCompressAction extends AbstractAction {
  private static final int BUF_SIZE = 8192;
  
  private final File source;
  
  private final File destination;
  
  private final boolean deleteSource;
  
  private final int compressionLevel;
  
  public GzCompressAction(File source, File destination, boolean deleteSource, int compressionLevel) {
    Objects.requireNonNull(source, "source");
    Objects.requireNonNull(destination, "destination");
    this.source = source;
    this.destination = destination;
    this.deleteSource = deleteSource;
    this.compressionLevel = compressionLevel;
  }
  
  @Deprecated
  public GzCompressAction(File source, File destination, boolean deleteSource) {
    this(source, destination, deleteSource, -1);
  }
  
  public boolean execute() throws IOException {
    return execute(this.source, this.destination, this.deleteSource, this.compressionLevel);
  }
  
  @Deprecated
  public static boolean execute(File source, File destination, boolean deleteSource) throws IOException {
    return execute(source, destination, deleteSource, -1);
  }
  
  public static boolean execute(File source, File destination, boolean deleteSource, int compressionLevel) throws IOException {
    if (source.exists()) {
      try(FileInputStream fis = new FileInputStream(source); 
          OutputStream fos = new FileOutputStream(destination); 
          OutputStream gzipOut = new ConfigurableLevelGZIPOutputStream(fos, 8192, compressionLevel); 
          
          OutputStream os = new BufferedOutputStream(gzipOut, 8192)) {
        byte[] inbuf = new byte[8192];
        int n;
        while ((n = fis.read(inbuf)) != -1)
          os.write(inbuf, 0, n); 
      } 
      if (deleteSource && !source.delete())
        LOGGER.warn("Unable to delete {}.", source); 
      return true;
    } 
    return false;
  }
  
  private static final class ConfigurableLevelGZIPOutputStream extends GZIPOutputStream {
    ConfigurableLevelGZIPOutputStream(OutputStream out, int bufSize, int level) throws IOException {
      super(out, bufSize);
      this.def.setLevel(level);
    }
  }
  
  protected void reportException(Exception ex) {
    LOGGER.warn("Exception during compression of '" + this.source.toString() + "'.", ex);
  }
  
  public String toString() {
    return GzCompressAction.class.getSimpleName() + '[' + this.source + " to " + this.destination + ", deleteSource=" + this.deleteSource + ']';
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
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\rolling\action\GzCompressAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */