package net.minecraft.network;

public final class ThreadQuickExitException
  extends RuntimeException
{
  public static final ThreadQuickExitException field_179886_a = new ThreadQuickExitException();
  private static final String __OBFID = "CL_00002274";
  
  private ThreadQuickExitException()
  {
    setStackTrace(new StackTraceElement[0]);
  }
  
  public synchronized Throwable fillInStackTrace()
  {
    setStackTrace(new StackTraceElement[0]);
    return this;
  }
}
