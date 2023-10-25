package net.minecraft.network;

public final class ThreadQuickExitException extends RuntimeException {
  public static final ThreadQuickExitException field_179886_a = new ThreadQuickExitException();
  
  private ThreadQuickExitException() {
    setStackTrace(new StackTraceElement[0]);
  }
  
  public synchronized Throwable fillInStackTrace() {
    setStackTrace(new StackTraceElement[0]);
    return this;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\network\ThreadQuickExitException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */