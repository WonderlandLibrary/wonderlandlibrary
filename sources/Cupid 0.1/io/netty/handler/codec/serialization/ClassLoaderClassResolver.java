package io.netty.handler.codec.serialization;

class ClassLoaderClassResolver implements ClassResolver {
  private final ClassLoader classLoader;
  
  ClassLoaderClassResolver(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }
  
  public Class<?> resolve(String className) throws ClassNotFoundException {
    try {
      return this.classLoader.loadClass(className);
    } catch (ClassNotFoundException ignored) {
      return Class.forName(className, false, this.classLoader);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\handler\codec\serialization\ClassLoaderClassResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */