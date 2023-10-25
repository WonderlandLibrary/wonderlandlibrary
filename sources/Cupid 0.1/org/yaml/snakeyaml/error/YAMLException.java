package org.yaml.snakeyaml.error;

public class YAMLException extends RuntimeException {
  private static final long serialVersionUID = -4738336175050337570L;
  
  public YAMLException(String message) {
    super(message);
  }
  
  public YAMLException(Throwable cause) {
    super(cause);
  }
  
  public YAMLException(String message, Throwable cause) {
    super(message, cause);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\error\YAMLException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */