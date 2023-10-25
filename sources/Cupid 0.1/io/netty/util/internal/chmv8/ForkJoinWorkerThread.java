package io.netty.util.internal.chmv8;

public class ForkJoinWorkerThread extends Thread {
  final ForkJoinPool pool;
  
  final ForkJoinPool.WorkQueue workQueue;
  
  protected ForkJoinWorkerThread(ForkJoinPool pool) {
    super("aForkJoinWorkerThread");
    this.pool = pool;
    this.workQueue = pool.registerWorker(this);
  }
  
  public ForkJoinPool getPool() {
    return this.pool;
  }
  
  public int getPoolIndex() {
    return this.workQueue.poolIndex >>> 1;
  }
  
  protected void onStart() {}
  
  protected void onTermination(Throwable exception) {}
  
  public void run() {
    Throwable exception = null;
    try {
      onStart();
      this.pool.runWorker(this.workQueue);
    } catch (Throwable ex) {
      exception = ex;
    } finally {
      try {
        onTermination(exception);
      } catch (Throwable ex) {
        if (exception == null)
          exception = ex; 
      } finally {
        this.pool.deregisterWorker(this, exception);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\chmv8\ForkJoinWorkerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */