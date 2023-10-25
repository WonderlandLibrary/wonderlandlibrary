package org.apache.logging.log4j.message;

import java.io.Serializable;

public class DefaultFlowMessageFactory implements FlowMessageFactory, Serializable {
  private static final String EXIT_DEFAULT_PREFIX = "Exit";
  
  private static final String ENTRY_DEFAULT_PREFIX = "Enter";
  
  private static final long serialVersionUID = 8578655591131397576L;
  
  private final String entryText;
  
  private final String exitText;
  
  public DefaultFlowMessageFactory() {
    this("Enter", "Exit");
  }
  
  public DefaultFlowMessageFactory(String entryText, String exitText) {
    this.entryText = entryText;
    this.exitText = exitText;
  }
  
  private static class AbstractFlowMessage implements FlowMessage {
    private static final long serialVersionUID = 1L;
    
    private final Message message;
    
    private final String text;
    
    AbstractFlowMessage(String text, Message message) {
      this.message = message;
      this.text = text;
    }
    
    public String getFormattedMessage() {
      if (this.message != null)
        return this.text + " " + this.message.getFormattedMessage(); 
      return this.text;
    }
    
    public String getFormat() {
      if (this.message != null)
        return this.text + ": " + this.message.getFormat(); 
      return this.text;
    }
    
    public Object[] getParameters() {
      if (this.message != null)
        return this.message.getParameters(); 
      return null;
    }
    
    public Throwable getThrowable() {
      if (this.message != null)
        return this.message.getThrowable(); 
      return null;
    }
    
    public Message getMessage() {
      return this.message;
    }
    
    public String getText() {
      return this.text;
    }
  }
  
  private static final class SimpleEntryMessage extends AbstractFlowMessage implements EntryMessage {
    private static final long serialVersionUID = 1L;
    
    SimpleEntryMessage(String entryText, Message message) {
      super(entryText, message);
    }
  }
  
  private static final class SimpleExitMessage extends AbstractFlowMessage implements ExitMessage {
    private static final long serialVersionUID = 1L;
    
    private final Object result;
    
    private final boolean isVoid;
    
    SimpleExitMessage(String exitText, EntryMessage message) {
      super(exitText, message.getMessage());
      this.result = null;
      this.isVoid = true;
    }
    
    SimpleExitMessage(String exitText, Object result, EntryMessage message) {
      super(exitText, message.getMessage());
      this.result = result;
      this.isVoid = false;
    }
    
    SimpleExitMessage(String exitText, Object result, Message message) {
      super(exitText, message);
      this.result = result;
      this.isVoid = false;
    }
    
    public String getFormattedMessage() {
      String formattedMessage = super.getFormattedMessage();
      if (this.isVoid)
        return formattedMessage; 
      return formattedMessage + ": " + this.result;
    }
  }
  
  public String getEntryText() {
    return this.entryText;
  }
  
  public String getExitText() {
    return this.exitText;
  }
  
  public EntryMessage newEntryMessage(Message message) {
    return new SimpleEntryMessage(this.entryText, makeImmutable(message));
  }
  
  private Message makeImmutable(Message message) {
    if (!(message instanceof ReusableMessage))
      return message; 
    return new SimpleMessage(message.getFormattedMessage());
  }
  
  public ExitMessage newExitMessage(EntryMessage message) {
    return new SimpleExitMessage(this.exitText, message);
  }
  
  public ExitMessage newExitMessage(Object result, EntryMessage message) {
    return new SimpleExitMessage(this.exitText, result, message);
  }
  
  public ExitMessage newExitMessage(Object result, Message message) {
    return new SimpleExitMessage(this.exitText, result, message);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\DefaultFlowMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */