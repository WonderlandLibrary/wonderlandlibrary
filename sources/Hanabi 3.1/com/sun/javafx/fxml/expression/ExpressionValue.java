package com.sun.javafx.fxml.expression;

import com.sun.javafx.fxml.BeanAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class ExpressionValue extends ObservableValueBase {
   private Object namespace;
   private Expression expression;
   private Class type;
   private ArrayList argumentMonitors;
   private int listenerCount = 0;

   public ExpressionValue(Object var1, Expression var2, Class var3) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (var2 == null) {
         throw new NullPointerException();
      } else if (var3 == null) {
         throw new NullPointerException();
      } else {
         this.namespace = var1;
         this.expression = var2;
         this.type = var3;
         List var4 = var2.getArguments();
         this.argumentMonitors = new ArrayList(var4.size());
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            KeyPath var6 = (KeyPath)var5.next();
            this.argumentMonitors.add(new KeyPathMonitor(var6.iterator()));
         }

      }
   }

   public Object getValue() {
      return BeanAdapter.coerce(this.expression.evaluate(this.namespace), this.type);
   }

   public void addListener(InvalidationListener var1) {
      if (this.listenerCount == 0) {
         this.monitorArguments();
      }

      super.addListener(var1);
      ++this.listenerCount;
   }

   public void removeListener(InvalidationListener var1) {
      super.removeListener(var1);
      --this.listenerCount;
      if (this.listenerCount == 0) {
         this.unmonitorArguments();
      }

   }

   public void addListener(ChangeListener var1) {
      if (this.listenerCount == 0) {
         this.monitorArguments();
      }

      super.addListener(var1);
      ++this.listenerCount;
   }

   public void removeListener(ChangeListener var1) {
      super.removeListener(var1);
      --this.listenerCount;
      if (this.listenerCount == 0) {
         this.unmonitorArguments();
      }

   }

   private void monitorArguments() {
      Iterator var1 = this.argumentMonitors.iterator();

      while(var1.hasNext()) {
         KeyPathMonitor var2 = (KeyPathMonitor)var1.next();
         var2.monitor(this.namespace);
      }

   }

   private void unmonitorArguments() {
      Iterator var1 = this.argumentMonitors.iterator();

      while(var1.hasNext()) {
         KeyPathMonitor var2 = (KeyPathMonitor)var1.next();
         var2.unmonitor();
      }

   }

   private class KeyPathMonitor {
      private String key;
      private KeyPathMonitor next;
      private Object namespace = null;
      private ListChangeListener listChangeListener = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               int var2 = Integer.parseInt(KeyPathMonitor.this.key);
               if (var2 >= var1.getFrom() && var2 < var1.getTo()) {
                  ExpressionValue.this.fireValueChangedEvent();
                  KeyPathMonitor.this.remonitor();
               }
            }

         }
      };
      private MapChangeListener mapChangeListener = new MapChangeListener() {
         public void onChanged(MapChangeListener.Change var1) {
            if (KeyPathMonitor.this.key.equals(var1.getKey())) {
               ExpressionValue.this.fireValueChangedEvent();
               KeyPathMonitor.this.remonitor();
            }

         }
      };
      private ChangeListener propertyChangeListener = new ChangeListener() {
         public void changed(ObservableValue var1, Object var2, Object var3) {
            ExpressionValue.this.fireValueChangedEvent();
            KeyPathMonitor.this.remonitor();
         }
      };

      public KeyPathMonitor(Iterator var2) {
         this.key = (String)var2.next();
         if (var2.hasNext()) {
            this.next = ExpressionValue.this.new KeyPathMonitor(var2);
         } else {
            this.next = null;
         }

      }

      public void monitor(Object var1) {
         if (var1 instanceof ObservableList) {
            ((ObservableList)var1).addListener(this.listChangeListener);
         } else if (var1 instanceof ObservableMap) {
            ((ObservableMap)var1).addListener(this.mapChangeListener);
         } else {
            BeanAdapter var2 = new BeanAdapter(var1);
            ObservableValue var3 = var2.getPropertyModel(this.key);
            if (var3 != null) {
               var3.addListener(this.propertyChangeListener);
            }

            var1 = var2;
         }

         this.namespace = var1;
         if (this.next != null) {
            Object var4 = Expression.get(var1, this.key);
            if (var4 != null) {
               this.next.monitor(var4);
            }
         }

      }

      public void unmonitor() {
         if (this.namespace instanceof ObservableList) {
            ((ObservableList)this.namespace).removeListener(this.listChangeListener);
         } else if (this.namespace instanceof ObservableMap) {
            ((ObservableMap)this.namespace).removeListener(this.mapChangeListener);
         } else if (this.namespace != null) {
            BeanAdapter var1 = (BeanAdapter)this.namespace;
            ObservableValue var2 = var1.getPropertyModel(this.key);
            if (var2 != null) {
               var2.removeListener(this.propertyChangeListener);
            }
         }

         this.namespace = null;
         if (this.next != null) {
            this.next.unmonitor();
         }

      }

      public void remonitor() {
         if (this.next != null) {
            this.next.unmonitor();
            Object var1 = Expression.get(this.namespace, this.key);
            if (var1 != null) {
               this.next.monitor(var1);
            }
         }

      }
   }
}
