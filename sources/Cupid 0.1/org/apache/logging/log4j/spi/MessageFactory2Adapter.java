package org.apache.logging.log4j.spi;

import java.util.Objects;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.MessageFactory2;
import org.apache.logging.log4j.message.SimpleMessage;

public class MessageFactory2Adapter implements MessageFactory2 {
  private final MessageFactory wrapped;
  
  public MessageFactory2Adapter(MessageFactory wrapped) {
    this.wrapped = Objects.<MessageFactory>requireNonNull(wrapped);
  }
  
  public MessageFactory getOriginal() {
    return this.wrapped;
  }
  
  public Message newMessage(CharSequence charSequence) {
    return (Message)new SimpleMessage(charSequence);
  }
  
  public Message newMessage(String message, Object p0) {
    return this.wrapped.newMessage(message, new Object[] { p0 });
  }
  
  public Message newMessage(String message, Object p0, Object p1) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8 });
  }
  
  public Message newMessage(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    return this.wrapped.newMessage(message, new Object[] { p0, p1, p2, p3, p4, p5, p6, p7, p8, p9 });
  }
  
  public Message newMessage(Object message) {
    return this.wrapped.newMessage(message);
  }
  
  public Message newMessage(String message) {
    return this.wrapped.newMessage(message);
  }
  
  public Message newMessage(String message, Object... params) {
    return this.wrapped.newMessage(message, params);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\MessageFactory2Adapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */