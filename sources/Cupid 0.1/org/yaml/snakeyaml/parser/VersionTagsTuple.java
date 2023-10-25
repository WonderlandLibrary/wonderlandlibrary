package org.yaml.snakeyaml.parser;

import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;

class VersionTagsTuple {
  private DumperOptions.Version version;
  
  private Map<String, String> tags;
  
  public VersionTagsTuple(DumperOptions.Version version, Map<String, String> tags) {
    this.version = version;
    this.tags = tags;
  }
  
  public DumperOptions.Version getVersion() {
    return this.version;
  }
  
  public Map<String, String> getTags() {
    return this.tags;
  }
  
  public String toString() {
    return String.format("VersionTagsTuple<%s, %s>", new Object[] { this.version, this.tags });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\parser\VersionTagsTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */