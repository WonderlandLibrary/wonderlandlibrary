package com.sun.javafx.runtime.async;

public interface AsyncOperationListener {
   void onProgress(int var1, int var2);

   void onCompletion(Object var1);

   void onCancel();

   void onException(Exception var1);
}
