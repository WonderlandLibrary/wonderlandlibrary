package com.sun.scenario.effect;

public interface LockableResource {
   void lock();

   void unlock();

   boolean isLost();
}
