package org.apache.logging.log4j.core.util;

import java.io.File;
import java.net.URI;
import java.util.Objects;
import org.apache.logging.log4j.core.config.ConfigurationSource;

public class Source {
  private final File file;
  
  private final URI uri;
  
  private final String location;
  
  public Source(ConfigurationSource source) {
    this.file = source.getFile();
    this.uri = source.getURI();
    this.location = source.getLocation();
  }
  
  public Source(File file) {
    this.file = Objects.<File>requireNonNull(file, "file is null");
    this.location = file.getAbsolutePath();
    this.uri = null;
  }
  
  public Source(URI uri, long lastModified) {
    this.uri = Objects.<URI>requireNonNull(uri, "URI is null");
    this.location = uri.toString();
    this.file = null;
  }
  
  public File getFile() {
    return this.file;
  }
  
  public URI getURI() {
    return this.uri;
  }
  
  public String getLocation() {
    return this.location;
  }
  
  public String toString() {
    return this.location;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (!(o instanceof Source))
      return false; 
    Source source = (Source)o;
    return Objects.equals(this.location, source.location);
  }
  
  public int hashCode() {
    return 31 + Objects.hashCode(this.location);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Source.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */