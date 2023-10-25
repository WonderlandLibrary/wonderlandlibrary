package org.apache.logging.log4j.message;

public final class ParameterizedMessageFactory extends AbstractMessageFactory {
  public static final ParameterizedMessageFactory INSTANCE = new ParameterizedMessageFactory();
  
  private static final long serialVersionUID = -8970940216592525651L;
  
  public Message newMessage(String message, Object... params) {
    return new ParameterizedMessage(message, params);
  }
  
  public Message newMessage(String message, Object p0) {
    return new ParameterizedMessage(message, p0);
  }
  
  public Message newMessage(String message, Object p0, Object p1) {
    return new ParameterizedMessage(message, p0, p1);
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return new ParameterizedMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ParameterizedMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */