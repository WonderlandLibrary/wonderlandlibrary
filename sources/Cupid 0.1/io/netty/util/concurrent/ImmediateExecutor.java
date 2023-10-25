package io.netty.util.concurrent;

import java.util.concurrent.Executor;

public final class ImmediateExecutor implements Executor {
  public static final ImmediateExecutor INSTANCE = new ImmediateExecutor();
  
  public void execute(Runnable command) {
    if (command == null)
      throw new NullPointerException("command"); 
    command.run();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\concurrent\ImmediateExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */