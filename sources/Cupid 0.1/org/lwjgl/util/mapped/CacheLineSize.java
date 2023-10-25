package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;

final class CacheLineSize {
  static int getCacheLineSize() {
    int THREADS = 2;
    int REPEATS = 200000;
    int LOCAL_REPEATS = 100000;
    int MAX_SIZE = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineMaxSize", 1024).intValue() / 4;
    double TIME_THRESHOLD = 1.0D + LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineTimeThreshold", 50).intValue() / 100.0D;
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);
    try {
      IntBuffer memory = getMemory(MAX_SIZE);
      int WARMUP = 10;
      for (int i = 0; i < 10; i++)
        doTest(2, 100000, 0, memory, completionService); 
      long totalTime = 0L;
      int count = 0;
      int cacheLineSize = 64;
      boolean found = false;
      int j;
      for (j = MAX_SIZE; j >= 1; j >>= 1) {
        long time = doTest(2, 100000, j, memory, completionService);
        if (totalTime > 0L) {
          long avgTime = totalTime / count;
          if (time / avgTime > TIME_THRESHOLD) {
            cacheLineSize = (j << 1) * 4;
            found = true;
            break;
          } 
        } 
        totalTime += time;
        count++;
      } 
      if (LWJGLUtil.DEBUG)
        if (found) {
          LWJGLUtil.log("Cache line size detected: " + cacheLineSize + " bytes");
        } else {
          LWJGLUtil.log("Failed to detect cache line size, assuming " + cacheLineSize + " bytes");
        }  
      j = cacheLineSize;
      return j;
    } finally {
      executorService.shutdown();
    } 
  }
  
  public static void main(String[] args) {
    CacheUtil.getCacheLineSize();
  }
  
  static long memoryLoop(int index, int repeats, IntBuffer memory, int padding) {
    long address = MemoryUtil.getAddress(memory) + (index * padding * 4);
    long time = System.nanoTime();
    for (int i = 0; i < repeats; i++)
      MappedHelper.ivput(MappedHelper.ivget(address) + 1, address); 
    return System.nanoTime() - time;
  }
  
  private static IntBuffer getMemory(int START_SIZE) {
    int PAGE_SIZE = MappedObjectUnsafe.INSTANCE.pageSize();
    ByteBuffer buffer = ByteBuffer.allocateDirect(START_SIZE * 4 + PAGE_SIZE).order(ByteOrder.nativeOrder());
    if (MemoryUtil.getAddress(buffer) % PAGE_SIZE != 0L)
      buffer.position(PAGE_SIZE - (int)(MemoryUtil.getAddress(buffer) & (PAGE_SIZE - 1))); 
    return buffer.asIntBuffer();
  }
  
  private static long doTest(int threads, int repeats, int padding, IntBuffer memory, ExecutorCompletionService<Long> completionService) {
    for (int i = 0; i < threads; i++)
      submitTest(completionService, i, repeats, memory, padding); 
    return waitForResults(threads, completionService);
  }
  
  private static void submitTest(ExecutorCompletionService<Long> completionService, final int index, final int repeats, final IntBuffer memory, final int padding) {
    completionService.submit(new Callable<Long>() {
          public Long call() throws Exception {
            return Long.valueOf(CacheLineSize.memoryLoop(index, repeats, memory, padding));
          }
        });
  }
  
  private static long waitForResults(int count, ExecutorCompletionService<Long> completionService) {
    try {
      long totalTime = 0L;
      for (int i = 0; i < count; i++)
        totalTime += ((Long)completionService.take().get()).longValue(); 
      return totalTime;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\CacheLineSize.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */