package org.apache.logging.log4j.message;

public final class ParameterizedNoReferenceMessageFactory extends AbstractMessageFactory {
  private static final long serialVersionUID = 5027639245636870500L;
  
  static class StatusMessage implements Message {
    private static final long serialVersionUID = 4199272162767841280L;
    
    private final String formattedMessage;
    
    private final Throwable throwable;
    
    public StatusMessage(String formattedMessage, Throwable throwable) {
      this.formattedMessage = formattedMessage;
      this.throwable = throwable;
    }
    
    public String getFormattedMessage() {
      return this.formattedMessage;
    }
    
    public String getFormat() {
      return this.formattedMessage;
    }
    
    public Object[] getParameters() {
      return null;
    }
    
    public Throwable getThrowable() {
      return this.throwable;
    }
  }
  
  public static final ParameterizedNoReferenceMessageFactory INSTANCE = new ParameterizedNoReferenceMessageFactory();
  
  public Message newMessage(String message, Object... params) {
    if (params == null)
      return new SimpleMessage(message); 
    ParameterizedMessage msg = new ParameterizedMessage(message, params);
    return new StatusMessage(msg.getFormattedMessage(), msg.getThrowable());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\ParameterizedNoReferenceMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */