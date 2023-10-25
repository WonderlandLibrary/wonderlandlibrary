package org.apache.commons.compress.archivers.sevenz;

public class SevenZMethodConfiguration {
  private final SevenZMethod method;
  
  private final Object options;
  
  public SevenZMethodConfiguration(SevenZMethod method) {
    this(method, null);
  }
  
  public SevenZMethodConfiguration(SevenZMethod method, Object options) {
    this.method = method;
    this.options = options;
    if (options != null && !Coders.findByMethod(method).canAcceptOptions(options))
      throw new IllegalArgumentException("The " + method + " method doesn't support options of type " + options.getClass()); 
  }
  
  public SevenZMethod getMethod() {
    return this.method;
  }
  
  public Object getOptions() {
    return this.options;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\sevenz\SevenZMethodConfiguration.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */