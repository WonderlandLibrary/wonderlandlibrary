package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuItemDelegate;

public final class MenuItem {
   public static final MenuItem Separator = null;
   private final MenuItemDelegate delegate;
   private String title;
   private Callback callback;
   private boolean enabled;
   private boolean checked;
   private int shortcutKey;
   private int shortcutModifiers;

   protected MenuItem(String var1) {
      this(var1, (Callback)null);
   }

   protected MenuItem(String var1, Callback var2) {
      this(var1, var2, 0, 0);
   }

   protected MenuItem(String var1, Callback var2, int var3, int var4) {
      this(var1, var2, var3, var4, (Pixels)null);
   }

   protected MenuItem(String var1, Callback var2, int var3, int var4, Pixels var5) {
      Application.checkEventThread();
      this.title = var1;
      this.callback = var2;
      this.shortcutKey = var3;
      this.shortcutModifiers = var4;
      this.enabled = true;
      this.checked = false;
      this.delegate = PlatformFactory.getPlatformFactory().createMenuItemDelegate(this);
      if (!this.delegate.createMenuItem(var1, var2, var3, var4, var5, this.enabled, this.checked)) {
         throw new RuntimeException("MenuItem creation error.");
      }
   }

   public String getTitle() {
      Application.checkEventThread();
      return this.title;
   }

   public void setTitle(String var1) {
      Application.checkEventThread();
      if (this.delegate.setTitle(var1)) {
         this.title = var1;
      }

   }

   public Callback getCallback() {
      Application.checkEventThread();
      return this.callback;
   }

   public void setCallback(Callback var1) {
      Application.checkEventThread();
      if (this.delegate.setCallback(var1)) {
         this.callback = var1;
      }

   }

   public boolean isEnabled() {
      Application.checkEventThread();
      return this.enabled;
   }

   public void setEnabled(boolean var1) {
      Application.checkEventThread();
      if (this.delegate.setEnabled(var1)) {
         this.enabled = var1;
      }

   }

   public boolean isChecked() {
      Application.checkEventThread();
      return this.checked;
   }

   public void setChecked(boolean var1) {
      Application.checkEventThread();
      if (this.delegate.setChecked(var1)) {
         this.checked = var1;
      }

   }

   public int getShortcutKey() {
      Application.checkEventThread();
      return this.shortcutKey;
   }

   public int getShortcutModifiers() {
      Application.checkEventThread();
      return this.shortcutModifiers;
   }

   public void setShortcut(int var1, int var2) {
      Application.checkEventThread();
      if (this.delegate.setShortcut(var1, var2)) {
         this.shortcutKey = var1;
         this.shortcutModifiers = var2;
      }

   }

   public boolean setPixels(Pixels var1) {
      Application.checkEventThread();
      return this.delegate.setPixels(var1);
   }

   MenuItemDelegate getDelegate() {
      return this.delegate;
   }

   public interface Callback {
      void action();

      void validate();
   }
}
