package com.sun.javafx.scene.web;

import javafx.util.Callback;

public interface Debugger {
   boolean isEnabled();

   void setEnabled(boolean var1);

   void sendMessage(String var1);

   Callback getMessageCallback();

   void setMessageCallback(Callback var1);
}
