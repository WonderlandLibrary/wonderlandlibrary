package netscape.javascript;

public class JSException extends RuntimeException {
   /** @deprecated */
   public static final int EXCEPTION_TYPE_EMPTY = -1;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_VOID = 0;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_OBJECT = 1;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_FUNCTION = 2;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_STRING = 3;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_NUMBER = 4;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_BOOLEAN = 5;
   /** @deprecated */
   public static final int EXCEPTION_TYPE_ERROR = 6;
   /** @deprecated */
   protected String message;
   /** @deprecated */
   protected String filename;
   /** @deprecated */
   protected int lineno;
   /** @deprecated */
   protected String source;
   /** @deprecated */
   protected int tokenIndex;
   /** @deprecated */
   private int wrappedExceptionType;
   /** @deprecated */
   private Object wrappedException;

   public JSException() {
      this((String)null);
   }

   public JSException(String var1) {
      this(var1, (String)null, -1, (String)null, -1);
   }

   /** @deprecated */
   public JSException(String var1, String var2, int var3, String var4, int var5) {
      super(var1);
      this.message = null;
      this.filename = null;
      this.lineno = -1;
      this.source = null;
      this.tokenIndex = -1;
      this.wrappedExceptionType = -1;
      this.wrappedException = null;
      this.message = var1;
      this.filename = var2;
      this.lineno = var3;
      this.source = var4;
      this.tokenIndex = var5;
      this.wrappedExceptionType = -1;
   }

   /** @deprecated */
   public JSException(int var1, Object var2) {
      this();
      this.wrappedExceptionType = var1;
      this.wrappedException = var2;
   }

   /** @deprecated */
   public int getWrappedExceptionType() {
      return this.wrappedExceptionType;
   }

   /** @deprecated */
   public Object getWrappedException() {
      return this.wrappedException;
   }
}
