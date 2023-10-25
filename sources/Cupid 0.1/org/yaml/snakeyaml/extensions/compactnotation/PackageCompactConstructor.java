package org.yaml.snakeyaml.extensions.compactnotation;

public class PackageCompactConstructor extends CompactConstructor {
  private String packageName;
  
  public PackageCompactConstructor(String packageName) {
    this.packageName = packageName;
  }
  
  protected Class<?> getClassForName(String name) throws ClassNotFoundException {
    if (name.indexOf('.') < 0)
      try {
        Class<?> clazz = Class.forName(this.packageName + "." + name);
        return clazz;
      } catch (ClassNotFoundException e) {} 
    return super.getClassForName(name);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\extensions\compactnotation\PackageCompactConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */