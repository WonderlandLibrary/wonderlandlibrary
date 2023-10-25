package com.ibm.icu.impl;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Iterator;
import java.util.List;

public abstract class ICUNotifier {
  private final Object notifyLock = new Object();
  
  private NotifyThread notifyThread;
  
  private List<EventListener> listeners;
  
  public void addListener(EventListener l) {
    if (l == null)
      throw new NullPointerException(); 
    if (acceptsListener(l)) {
      synchronized (this.notifyLock) {
        if (this.listeners == null) {
          this.listeners = new ArrayList<EventListener>();
        } else {
          for (EventListener ll : this.listeners) {
            if (ll == l)
              return; 
          } 
        } 
        this.listeners.add(l);
      } 
    } else {
      throw new IllegalStateException("Listener invalid for this notifier.");
    } 
  }
  
  public void removeListener(EventListener l) {
    if (l == null)
      throw new NullPointerException(); 
    synchronized (this.notifyLock) {
      if (this.listeners != null) {
        Iterator<EventListener> iter = this.listeners.iterator();
        while (iter.hasNext()) {
          if (iter.next() == l) {
            iter.remove();
            if (this.listeners.size() == 0)
              this.listeners = null; 
            return;
          } 
        } 
      } 
    } 
  }
  
  public void notifyChanged() {
    if (this.listeners != null)
      synchronized (this.notifyLock) {
        if (this.listeners != null) {
          if (this.notifyThread == null) {
            this.notifyThread = new NotifyThread(this);
            this.notifyThread.setDaemon(true);
            this.notifyThread.start();
          } 
          this.notifyThread.queue(this.listeners.<EventListener>toArray(new EventListener[this.listeners.size()]));
        } 
      }  
  }
  
  protected abstract boolean acceptsListener(EventListener paramEventListener);
  
  protected abstract void notifyListener(EventListener paramEventListener);
  
  private static class NotifyThread extends Thread {
    private final ICUNotifier notifier;
    
    private final List<EventListener[]> queue = (List)new ArrayList<EventListener>();
    
    NotifyThread(ICUNotifier notifier) {
      this.notifier = notifier;
    }
    
    public void queue(EventListener[] list) {
      synchronized (this) {
        this.queue.add(list);
        notify();
      } 
    }
    
    public void run() {
      while (true) {
        try {
          EventListener[] list;
          synchronized (this) {
            while (this.queue.isEmpty())
              wait(); 
            list = this.queue.remove(0);
          } 
          for (int i = 0; i < list.length; i++)
            this.notifier.notifyListener(list[i]); 
        } catch (InterruptedException e) {}
      } 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUNotifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */