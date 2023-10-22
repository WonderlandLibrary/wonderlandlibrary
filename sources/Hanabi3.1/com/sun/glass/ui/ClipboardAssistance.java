package com.sun.glass.ui;

import java.util.HashMap;

public class ClipboardAssistance {
   private final HashMap cacheData = new HashMap();
   private final Clipboard clipboard;
   private int supportedActions = 1342177279;

   public ClipboardAssistance(String var1) {
      Application.checkEventThread();
      this.clipboard = Clipboard.get(var1);
      this.clipboard.add(this);
   }

   public void close() {
      Application.checkEventThread();
      this.clipboard.remove(this);
   }

   public void flush() {
      Application.checkEventThread();
      this.clipboard.flush(this, this.cacheData, this.supportedActions);
   }

   public void emptyCache() {
      Application.checkEventThread();
      this.cacheData.clear();
   }

   public boolean isCacheEmpty() {
      Application.checkEventThread();
      return this.cacheData.isEmpty();
   }

   public void setData(String var1, Object var2) {
      Application.checkEventThread();
      this.cacheData.put(var1, var2);
   }

   public Object getData(String var1) {
      Application.checkEventThread();
      return this.clipboard.getData(var1);
   }

   public void setSupportedActions(int var1) {
      Application.checkEventThread();
      this.supportedActions = var1;
   }

   public int getSupportedSourceActions() {
      Application.checkEventThread();
      return this.clipboard.getSupportedSourceActions();
   }

   public void setTargetAction(int var1) {
      Application.checkEventThread();
      this.clipboard.setTargetAction(var1);
   }

   public void contentChanged() {
   }

   public void actionPerformed(int var1) {
   }

   public String[] getMimeTypes() {
      Application.checkEventThread();
      return this.clipboard.getMimeTypes();
   }

   public String toString() {
      return "ClipboardAssistance[" + this.clipboard + "]";
   }
}
