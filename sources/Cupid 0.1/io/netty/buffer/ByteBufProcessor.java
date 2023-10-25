package io.netty.buffer;

public interface ByteBufProcessor {
  public static final ByteBufProcessor FIND_NUL = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value != 0);
      }
    };
  
  public static final ByteBufProcessor FIND_NON_NUL = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value == 0);
      }
    };
  
  public static final ByteBufProcessor FIND_CR = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value != 13);
      }
    };
  
  public static final ByteBufProcessor FIND_NON_CR = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value == 13);
      }
    };
  
  public static final ByteBufProcessor FIND_LF = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value != 10);
      }
    };
  
  public static final ByteBufProcessor FIND_NON_LF = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value == 10);
      }
    };
  
  public static final ByteBufProcessor FIND_CRLF = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value != 13 && value != 10);
      }
    };
  
  public static final ByteBufProcessor FIND_NON_CRLF = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value == 13 || value == 10);
      }
    };
  
  public static final ByteBufProcessor FIND_LINEAR_WHITESPACE = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value != 32 && value != 9);
      }
    };
  
  public static final ByteBufProcessor FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor() {
      public boolean process(byte value) throws Exception {
        return (value == 32 || value == 9);
      }
    };
  
  boolean process(byte paramByte) throws Exception;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\ByteBufProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */