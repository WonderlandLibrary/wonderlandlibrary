package com.sun.javafx.property.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.Property;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

public class PropertyDescriptor extends ReadOnlyPropertyDescriptor {
   private static final String ADD_VETOABLE_LISTENER_METHOD_NAME = "addVetoableChangeListener";
   private static final String REMOVE_VETOABLE_LISTENER_METHOD_NAME = "removeVetoableChangeListener";
   private static final String ADD_PREFIX = "add";
   private static final String REMOVE_PREFIX = "remove";
   private static final String SUFFIX = "Listener";
   private static final int ADD_VETOABLE_LISTENER_TAKES_NAME = 1;
   private static final int REMOVE_VETOABLE_LISTENER_TAKES_NAME = 2;
   private final Method setter;
   private final Method addVetoListener;
   private final Method removeVetoListener;
   private final int flags;

   public Method getSetter() {
      return this.setter;
   }

   public PropertyDescriptor(String var1, Class var2, Method var3, Method var4) {
      super(var1, var2, var3);
      this.setter = var4;
      Method var5 = null;
      Method var6 = null;
      int var7 = 0;
      String var8 = "add" + capitalizedName(this.name) + "Listener";

      try {
         var5 = var2.getMethod(var8, VetoableChangeListener.class);
      } catch (NoSuchMethodException var18) {
         try {
            var5 = var2.getMethod("addVetoableChangeListener", String.class, VetoableChangeListener.class);
            var7 |= 1;
         } catch (NoSuchMethodException var17) {
            try {
               var5 = var2.getMethod("addVetoableChangeListener", VetoableChangeListener.class);
            } catch (NoSuchMethodException var16) {
            }
         }
      }

      String var9 = "remove" + capitalizedName(this.name) + "Listener";

      try {
         var6 = var2.getMethod(var9, VetoableChangeListener.class);
      } catch (NoSuchMethodException var15) {
         try {
            var6 = var2.getMethod("removeVetoableChangeListener", String.class, VetoableChangeListener.class);
            var7 |= 2;
         } catch (NoSuchMethodException var14) {
            try {
               var6 = var2.getMethod("removeVetoableChangeListener", VetoableChangeListener.class);
            } catch (NoSuchMethodException var13) {
            }
         }
      }

      this.addVetoListener = var5;
      this.removeVetoListener = var6;
      this.flags = var7;
   }

   public void addListener(ReadOnlyPropertyDescriptor.ReadOnlyListener var1) {
      super.addListener(var1);
      if (this.addVetoListener != null) {
         try {
            if ((this.flags & 1) > 0) {
               this.addVetoListener.invoke(var1.getBean(), this.name, var1);
            } else {
               this.addVetoListener.invoke(var1.getBean(), var1);
            }
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
         }
      }

   }

   public void removeListener(ReadOnlyPropertyDescriptor.ReadOnlyListener var1) {
      super.removeListener(var1);
      if (this.removeVetoListener != null) {
         try {
            if ((this.flags & 2) > 0) {
               this.removeVetoListener.invoke(var1.getBean(), this.name, var1);
            } else {
               this.removeVetoListener.invoke(var1.getBean(), var1);
            }
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
         }
      }

   }

   public class Listener extends ReadOnlyPropertyDescriptor.ReadOnlyListener implements ChangeListener, VetoableChangeListener {
      private boolean updating;

      public Listener(Object var2, ReadOnlyJavaBeanProperty var3) {
         super(var2, var3);
      }

      public void changed(ObservableValue var1, Object var2, Object var3) {
         ReadOnlyJavaBeanProperty var4 = this.checkRef();
         if (var4 == null) {
            var1.removeListener(this);
         } else if (!this.updating) {
            this.updating = true;

            try {
               MethodUtil.invoke(PropertyDescriptor.this.setter, this.bean, new Object[]{var3});
               var4.fireValueChangedEvent();
            } catch (IllegalAccessException var10) {
            } catch (InvocationTargetException var11) {
            } finally {
               this.updating = false;
            }
         }

      }

      public void vetoableChange(PropertyChangeEvent var1) throws PropertyVetoException {
         if (this.bean.equals(var1.getSource()) && PropertyDescriptor.this.name.equals(var1.getPropertyName())) {
            ReadOnlyJavaBeanProperty var2 = this.checkRef();
            if (var2 instanceof Property && ((Property)var2).isBound() && !this.updating) {
               throw new PropertyVetoException("A bound value cannot be set.", var1);
            }
         }

      }
   }
}
