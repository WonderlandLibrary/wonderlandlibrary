package org.apache.logging.log4j.message;

public class MessageFormatMessageFactory extends AbstractMessageFactory {
  private static final long serialVersionUID = 3584821740584192453L;
  
  public Message newMessage(String message, Object... params) {
    return new MessageFormatMessage(message, params);
  }
  
  public Message newMessage(String message, Object p0) {
    return new MessageFormatMessage(message, new Object[] { p0 });
  }
  
  public Message newMessage(String message, Object p0, Object p1) {
    return new MessageFormatMessage(message, new Object[] { p0, p1 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return new MessageFormatMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\MessageFormatMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */