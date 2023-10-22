package com.sun.javafx.property.adapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import javafx.beans.WeakListener;
import javafx.beans.property.adapter.ReadOnlyJavaBeanProperty;
import sun.reflect.misc.ReflectUtil;

public class ReadOnlyPropertyDescriptor {
   private static final String ADD_LISTENER_METHOD_NAME = "addPropertyChangeListener";
   private static final String REMOVE_LISTENER_METHOD_NAME = "removePropertyChangeListener";
   private static final String ADD_PREFIX = "add";
   private static final String REMOVE_PREFIX = "remove";
   private static final String SUFFIX = "Listener";
   private static final int ADD_LISTENER_TAKES_NAME = 1;
   private static final int REMOVE_LISTENER_TAKES_NAME = 2;
   protected final String name;
   protected final Class beanClass;
   private final Method getter;
   private final Class type;
   private final Method addChangeListener;
   private final Method removeChangeListener;
   private final int flags;

   public String getName() {
      return this.name;
   }

   public Method getGetter() {
      return this.getter;
   }

   public Class getType() {
      return this.type;
   }

   public ReadOnlyPropertyDescriptor(String var1, Class var2, Method var3) {
      ReflectUtil.checkPackageAccess(var2);
      this.name = var1;
      this.beanClass = var2;
      this.getter = var3;
      this.type = var3.getReturnType();
      Method var4 = null;
      Method var5 = null;
      int var6 = 0;

      String var7;
      try {
         var7 = "add" + capitalizedName(this.name) + "Listener";
         var4 = var2.getMethod(var7, PropertyChangeListener.class);
      } catch (NoSuchMethodException var15) {
         try {
            var4 = var2.getMethod("addPropertyChangeListener", String.class, PropertyChangeListener.class);
            var6 |= 1;
         } catch (NoSuchMethodException var14) {
            try {
               var4 = var2.getMethod("addPropertyChangeListener", PropertyChangeListener.class);
            } catch (NoSuchMethodException var13) {
            }
         }
      }

      try {
         var7 = "remove" + capitalizedName(this.name) + "Listener";
         var5 = var2.getMethod(var7, PropertyChangeListener.class);
      } catch (NoSuchMethodException var12) {
         try {
            var5 = var2.getMethod("removePropertyChangeListener", String.class, PropertyChangeListener.class);
            var6 |= 2;
         } catch (NoSuchMethodException var11) {
            try {
               var5 = var2.getMethod("removePropertyChangeListener", PropertyChangeListener.class);
            } catch (NoSuchMethodException var10) {
            }
         }
      }

      this.addChangeListener = var4;
      this.removeChangeListener = var5;
      this.flags = var6;
   }

   public static String capitalizedName(String var0) {
      return var0 != null && var0.length() != 0 ? var0.substring(0, 1).toUpperCase(Locale.ENGLISH) + var0.substring(1) : var0;
   }

   public void addListener(ReadOnlyListener var1) {
      if (this.addChangeListener != null) {
         try {
            if ((this.flags & 1) > 0) {
               this.addChangeListener.invoke(var1.getBean(), this.name, var1);
            } else {
               this.addChangeListener.invoke(var1.getBean(), var1);
            }
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
         }
      }

   }

   public void removeListener(ReadOnlyListener var1) {
      if (this.removeChangeListener != null) {
         try {
            if ((this.flags & 2) > 0) {
               this.removeChangeListener.invoke(var1.getBean(), this.name, var1);
            } else {
               this.removeChangeListener.invoke(var1.getBean(), var1);
            }
         } catch (IllegalAccessException var3) {
         } catch (InvocationTargetException var4) {
         }
      }

   }

   public class ReadOnlyListener implements PropertyChangeListener, WeakListener {
      protected final Object bean;
      private final WeakReference propertyRef;

      public Object getBean() {
         return this.bean;
      }

      public ReadOnlyListener(Object var2, ReadOnlyJavaBeanProperty var3) {
         this.bean = var2;
         this.propertyRef = new WeakReference(var3);
      }

      protected ReadOnlyJavaBeanProperty checkRef() {
         ReadOnlyJavaBeanProperty var1 = (ReadOnlyJavaBeanProperty)this.propertyRef.get();
         if (var1 == null) {
            ReadOnlyPropertyDescriptor.this.removeListener(this);
         }

         return var1;
      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (this.bean.equals(var1.getSource()) && ReadOnlyPropertyDescriptor.this.name.equals(var1.getPropertyName())) {
            ReadOnlyJavaBeanProperty var2 = this.checkRef();
            if (var2 != null) {
               var2.fireValueChangedEvent();
            }
         }

      }

      public boolean wasGarbageCollected() {
         return this.checkRef() == null;
      }
   }
}
