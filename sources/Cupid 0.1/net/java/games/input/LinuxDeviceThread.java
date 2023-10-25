package net.java.games.input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class LinuxDeviceThread extends Thread {
  private final List tasks = new ArrayList();
  
  public LinuxDeviceThread() {
    setDaemon(true);
    start();
  }
  
  public final synchronized void run() {
    while (true) {
      while (!this.tasks.isEmpty()) {
        LinuxDeviceTask task = this.tasks.remove(0);
        task.doExecute();
        synchronized (task) {
          task.notify();
        } 
      } 
      try {
        wait();
      } catch (InterruptedException e) {}
    } 
  }
  
  public final Object execute(LinuxDeviceTask task) throws IOException {
    synchronized (this) {
      this.tasks.add(task);
      notify();
    } 
    synchronized (task) {
      while (task.getState() == 1) {
        try {
          task.wait();
        } catch (InterruptedException e) {}
      } 
    } 
    switch (task.getState()) {
      case 2:
        return task.getResult();
      case 3:
        throw task.getException();
    } 
    throw new RuntimeException("Invalid task state: " + task.getState());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\LinuxDeviceThread.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */