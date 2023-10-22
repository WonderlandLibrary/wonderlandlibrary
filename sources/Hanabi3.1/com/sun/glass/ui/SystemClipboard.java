package com.sun.glass.ui;

import java.util.HashMap;

public abstract class SystemClipboard extends Clipboard {
   protected SystemClipboard(String var1) {
      super(var1);
      Application.checkEventThread();
   }

   protected abstract boolean isOwner();

   protected abstract void pushToSystem(HashMap var1, int var2);

   protected abstract void pushTargetActionToSystem(int var1);

   protected abstract Object popFromSystem(String var1);

   protected abstract int supportedSourceActionsFromSystem();

   protected abstract String[] mimesFromSystem();

   public void flush(ClipboardAssistance var1, HashMap var2, int var3) {
      Application.checkEventThread();
      this.setSharedData(var1, var2, var3);
      this.pushToSystem(var2, var3);
   }

   public int getSupportedSourceActions() {
      Application.checkEventThread();
      return this.isOwner() ? super.getSupportedSourceActions() : this.supportedSourceActionsFromSystem();
   }

   public void setTargetAction(int var1) {
      Application.checkEventThread();
      this.pushTargetActionToSystem(var1);
   }

   public Object getLocalData(String var1) {
      return super.getData(var1);
   }

   public Object getData(String var1) {
      Application.checkEventThread();
      return this.isOwner() ? this.getLocalData(var1) : this.popFromSystem(var1);
   }

   public String[] getMimeTypes() {
      Application.checkEventThread();
      return this.isOwner() ? super.getMimeTypes() : this.mimesFromSystem();
   }

   public String toString() {
      Application.checkEventThread();
      return "System Clipboard";
   }
}
