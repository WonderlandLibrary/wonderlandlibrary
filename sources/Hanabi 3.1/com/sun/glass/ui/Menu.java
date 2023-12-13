package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Menu {
   private final MenuDelegate delegate;
   private String title;
   private boolean enabled;
   private final List items;
   private EventHandler eventHandler;

   public EventHandler getEventHandler() {
      Application.checkEventThread();
      return this.eventHandler;
   }

   public void setEventHandler(EventHandler var1) {
      Application.checkEventThread();
      this.eventHandler = var1;
   }

   protected Menu(String var1) {
      this(var1, true);
   }

   protected Menu(String var1, boolean var2) {
      this.items = new ArrayList();
      Application.checkEventThread();
      this.title = var1;
      this.enabled = var2;
      this.delegate = PlatformFactory.getPlatformFactory().createMenuDelegate(this);
      if (!this.delegate.createMenu(var1, var2)) {
         throw new RuntimeException("Menu creation error.");
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

   public boolean setPixels(Pixels var1) {
      Application.checkEventThread();
      return this.delegate.setPixels(var1);
   }

   public List getItems() {
      Application.checkEventThread();
      return Collections.unmodifiableList(this.items);
   }

   public void add(Menu var1) {
      Application.checkEventThread();
      this.insert(var1, this.items.size());
   }

   public void add(MenuItem var1) {
      Application.checkEventThread();
      this.insert(var1, this.items.size());
   }

   public void insert(Menu var1, int var2) throws IndexOutOfBoundsException {
      Application.checkEventThread();
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         synchronized(this.items) {
            if (var2 >= 0 && var2 <= this.items.size()) {
               MenuDelegate var4 = var1.getDelegate();
               if (this.delegate.insert(var4, var2)) {
                  this.items.add(var2, var1);
               }

            } else {
               throw new IndexOutOfBoundsException();
            }
         }
      }
   }

   public void insert(MenuItem var1, int var2) throws IndexOutOfBoundsException {
      Application.checkEventThread();
      synchronized(this.items) {
         if (var2 >= 0 && var2 <= this.items.size()) {
            MenuItemDelegate var4 = var1 != null ? var1.getDelegate() : null;
            if (this.delegate.insert(var4, var2)) {
               this.items.add(var2, var1);
            }

         } else {
            throw new IndexOutOfBoundsException();
         }
      }
   }

   public void remove(int var1) throws IndexOutOfBoundsException {
      Application.checkEventThread();
      synchronized(this.items) {
         Object var3 = this.items.get(var1);
         boolean var4 = false;
         if (var3 == MenuItem.Separator) {
            var4 = this.delegate.remove((MenuItemDelegate)null, var1);
         } else if (var3 instanceof MenuItem) {
            var4 = this.delegate.remove(((MenuItem)var3).getDelegate(), var1);
         } else {
            var4 = this.delegate.remove(((Menu)var3).getDelegate(), var1);
         }

         if (var4) {
            this.items.remove(var1);
         }

      }
   }

   MenuDelegate getDelegate() {
      return this.delegate;
   }

   protected void notifyMenuOpening() {
      if (this.eventHandler != null) {
         this.eventHandler.handleMenuOpening(this, System.nanoTime());
      }

   }

   protected void notifyMenuClosed() {
      if (this.eventHandler != null) {
         this.eventHandler.handleMenuClosed(this, System.nanoTime());
      }

   }

   public static class EventHandler {
      public void handleMenuOpening(Menu var1, long var2) {
      }

      public void handleMenuClosed(Menu var1, long var2) {
      }
   }
}
