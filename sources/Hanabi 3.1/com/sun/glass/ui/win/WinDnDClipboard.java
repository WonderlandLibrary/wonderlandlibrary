package com.sun.glass.ui.win;

final class WinDnDClipboard extends WinSystemClipboard {
   private static int dragButton = 0;
   private int sourceSupportedActions = 0;

   public WinDnDClipboard(String var1) {
      super(var1);
   }

   protected void create() {
   }

   protected native void dispose();

   protected boolean isOwner() {
      return this.getDragButton() != 0;
   }

   protected void pushTargetActionToSystem(int var1) {
      throw new UnsupportedOperationException("[Target Action] not supported! Override View.handleDragDrop instead.");
   }

   protected native void push(Object[] var1, int var2);

   protected boolean pop() {
      return this.getPtr() != 0L;
   }

   private static WinDnDClipboard getInstance() {
      return (WinDnDClipboard)get("DND");
   }

   public String toString() {
      return "Windows DnD Clipboard";
   }

   public int getDragButton() {
      return dragButton;
   }

   private void setDragButton(int var1) {
      dragButton = var1;
   }

   protected final int supportedSourceActionsFromSystem() {
      return this.sourceSupportedActions != 0 ? this.sourceSupportedActions : super.supportedSourceActionsFromSystem();
   }

   private void setSourceSupportedActions(int var1) {
      this.sourceSupportedActions = var1;
   }
}
