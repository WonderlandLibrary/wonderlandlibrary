package org.yaml.snakeyaml.util;

public class PlatformFeatureDetector {
  private Boolean isRunningOnAndroid = null;
  
  public boolean isRunningOnAndroid() {
    if (this.isRunningOnAndroid == null) {
      String name = System.getProperty("java.runtime.name");
      this.isRunningOnAndroid = Boolean.valueOf((name != null && name.startsWith("Android Runtime")));
    } 
    return this.isRunningOnAndroid.booleanValue();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyam\\util\PlatformFeatureDetector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */