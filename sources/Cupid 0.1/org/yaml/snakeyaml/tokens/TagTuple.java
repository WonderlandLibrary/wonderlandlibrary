package org.yaml.snakeyaml.tokens;

public final class TagTuple {
  private final String handle;
  
  private final String suffix;
  
  public TagTuple(String handle, String suffix) {
    if (suffix == null)
      throw new NullPointerException("Suffix must be provided."); 
    this.handle = handle;
    this.suffix = suffix;
  }
  
  public String getHandle() {
    return this.handle;
  }
  
  public String getSuffix() {
    return this.suffix;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\TagTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */