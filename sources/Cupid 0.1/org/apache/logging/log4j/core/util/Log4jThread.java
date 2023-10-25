package org.apache.logging.log4j.core.util;

import java.util.concurrent.atomic.AtomicLong;

public class Log4jThread extends Thread {
  static final String PREFIX = "Log4j2-";
  
  private static final AtomicLong threadInitNumber = new AtomicLong();
  
  private static long nextThreadNum() {
    return threadInitNumber.getAndIncrement();
  }
  
  private static String toThreadName(Object name) {
    return "Log4j2-" + name;
  }
  
  public Log4jThread() {
    super(toThreadName(Long.valueOf(nextThreadNum())));
  }
  
  public Log4jThread(Runnable target) {
    super(target, toThreadName(Long.valueOf(nextThreadNum())));
  }
  
  public Log4jThread(Runnable target, String name) {
    super(target, toThreadName(name));
  }
  
  public Log4jThread(String name) {
    super(toThreadName(name));
  }
  
  public Log4jThread(ThreadGroup group, Runnable target) {
    super(group, target, toThreadName(Long.valueOf(nextThreadNum())));
  }
  
  public Log4jThread(ThreadGroup group, Runnable target, String name) {
    super(group, target, toThreadName(name));
  }
  
  public Log4jThread(ThreadGroup group, Runnable target, String name, long stackSize) {
    super(group, target, toThreadName(name), stackSize);
  }
  
  public Log4jThread(ThreadGroup group, String name) {
    super(group, toThreadName(name));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\Log4jThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */