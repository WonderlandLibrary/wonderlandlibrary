package com.sun.javafx.runtime.async;

public interface AsyncOperation {
   void start();

   void cancel();

   boolean isCancelled();

   boolean isDone();
}
