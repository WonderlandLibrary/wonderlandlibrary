package org.yaml.snakeyaml.constructor;

public class CustomClassLoaderConstructor extends Constructor {
  private ClassLoader loader = CustomClassLoaderConstructor.class.getClassLoader();
  
  public CustomClassLoaderConstructor(ClassLoader cLoader) {
    this(Object.class, cLoader);
  }
  
  public CustomClassLoaderConstructor(Class<? extends Object> theRoot, ClassLoader theLoader) {
    super(theRoot);
    if (theLoader == null)
      throw new NullPointerException("Loader must be provided."); 
    this.loader = theLoader;
  }
  
  protected Class<?> getClassForName(String name) throws ClassNotFoundException {
    return Class.forName(name, true, this.loader);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\constructor\CustomClassLoaderConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */