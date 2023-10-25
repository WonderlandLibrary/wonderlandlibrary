package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "MessageLayout", category = "Core", elementType = "layout", printObject = true)
public class MessageLayout extends AbstractLayout<Message> {
  public MessageLayout() {
    super(null, null, null);
  }
  
  public MessageLayout(Configuration configuration, byte[] header, byte[] footer) {
    super(configuration, header, footer);
  }
  
  public byte[] toByteArray(LogEvent event) {
    return null;
  }
  
  public Message toSerializable(LogEvent event) {
    return event.getMessage();
  }
  
  public String getContentType() {
    return null;
  }
  
  @PluginFactory
  public static Layout<?> createLayout() {
    return new MessageLayout();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\MessageLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */