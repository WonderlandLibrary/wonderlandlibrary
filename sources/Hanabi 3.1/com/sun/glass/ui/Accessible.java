package com.sun.glass.ui;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;

public abstract class Accessible {
   private EventHandler eventHandler;
   private View view;
   private GetAttribute getAttribute = new GetAttribute();
   private ExecuteAction executeAction = new ExecuteAction();

   public EventHandler getEventHandler() {
      return this.eventHandler;
   }

   public void setEventHandler(EventHandler var1) {
      this.eventHandler = var1;
   }

   public void setView(View var1) {
      this.view = var1;
   }

   public View getView() {
      return this.view;
   }

   public void dispose() {
      this.eventHandler = null;
      this.view = null;
   }

   public boolean isDisposed() {
      return this.getNativeAccessible() == 0L;
   }

   public String toString() {
      return this.getClass().getSimpleName() + " (" + this.eventHandler + ")";
   }

   protected boolean isIgnored() {
      AccessibleRole var1 = (AccessibleRole)this.getAttribute(AccessibleAttribute.ROLE);
      if (var1 == null) {
         return true;
      } else {
         return var1 == AccessibleRole.NODE || var1 == AccessibleRole.PARENT;
      }
   }

   protected abstract long getNativeAccessible();

   protected Accessible getAccessible(Scene var1) {
      return var1 == null ? null : SceneHelper.getAccessible(var1);
   }

   protected Accessible getAccessible(Node var1) {
      return var1 == null ? null : NodeHelper.getAccessible(var1);
   }

   protected long getNativeAccessible(Node var1) {
      if (var1 == null) {
         return 0L;
      } else {
         Accessible var2 = this.getAccessible(var1);
         return var2 == null ? 0L : var2.getNativeAccessible();
      }
   }

   protected Accessible getContainerAccessible(AccessibleRole var1) {
      Accessible var3;
      for(Node var2 = (Node)this.getAttribute(AccessibleAttribute.PARENT); var2 != null; var2 = (Node)var3.getAttribute(AccessibleAttribute.PARENT)) {
         var3 = this.getAccessible(var2);
         AccessibleRole var4 = (AccessibleRole)var3.getAttribute(AccessibleAttribute.ROLE);
         if (var4 == var1) {
            return var3;
         }
      }

      return null;
   }

   private final AccessControlContext getAccessControlContext() {
      AccessControlContext var1 = null;

      try {
         var1 = this.eventHandler.getAccessControlContext();
      } catch (Exception var3) {
      }

      return var1;
   }

   public Object getAttribute(AccessibleAttribute var1, Object... var2) {
      AccessControlContext var3 = this.getAccessControlContext();
      return var3 == null ? null : QuantumToolkit.runWithoutRenderLock(() -> {
         this.getAttribute.attribute = var1;
         this.getAttribute.parameters = var2;
         return AccessController.doPrivileged(this.getAttribute, var3);
      });
   }

   public void executeAction(AccessibleAction var1, Object... var2) {
      AccessControlContext var3 = this.getAccessControlContext();
      if (var3 != null) {
         QuantumToolkit.runWithoutRenderLock(() -> {
            this.executeAction.action = var1;
            this.executeAction.parameters = var2;
            return (Void)AccessController.doPrivileged(this.executeAction, var3);
         });
      }
   }

   public abstract void sendNotification(AccessibleAttribute var1);

   private class ExecuteAction implements PrivilegedAction {
      AccessibleAction action;
      Object[] parameters;

      private ExecuteAction() {
      }

      public Void run() {
         Accessible.this.eventHandler.executeAction(this.action, this.parameters);
         return null;
      }

      // $FF: synthetic method
      ExecuteAction(Object var2) {
         this();
      }
   }

   private class GetAttribute implements PrivilegedAction {
      AccessibleAttribute attribute;
      Object[] parameters;

      private GetAttribute() {
      }

      public Object run() {
         Object var1 = Accessible.this.eventHandler.getAttribute(this.attribute, this.parameters);
         if (var1 != null) {
            Class var2 = this.attribute.getReturnType();
            if (var2 != null) {
               try {
                  var2.cast(var1);
               } catch (Exception var5) {
                  String var4 = "The expected return type for the " + this.attribute + " attribute is " + var2.getSimpleName() + " but found " + var1.getClass().getSimpleName();
                  System.err.println(var4);
                  return null;
               }
            }
         }

         return var1;
      }

      // $FF: synthetic method
      GetAttribute(Object var2) {
         this();
      }
   }

   public abstract static class EventHandler {
      public Object getAttribute(AccessibleAttribute var1, Object... var2) {
         return null;
      }

      public void executeAction(AccessibleAction var1, Object... var2) {
      }

      public abstract AccessControlContext getAccessControlContext();
   }
}
