package javafx.beans.property;

import com.sun.javafx.binding.ListExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ListPropertyBase extends ListProperty {
   private final ListChangeListener listChangeListener = (var1x) -> {
      this.invalidateProperties();
      this.invalidated();
      this.fireValueChangedEvent(var1x);
   };
   private ObservableList value;
   private ObservableValue observable = null;
   private InvalidationListener listener = null;
   private boolean valid = true;
   private ListExpressionHelper helper = null;
   private SizeProperty size0;
   private EmptyProperty empty0;

   public ListPropertyBase() {
   }

   public ListPropertyBase(ObservableList var1) {
      this.value = var1;
      if (var1 != null) {
         var1.addListener(this.listChangeListener);
      }

   }

   public ReadOnlyIntegerProperty sizeProperty() {
      if (this.size0 == null) {
         this.size0 = new SizeProperty();
      }

      return this.size0;
   }

   public ReadOnlyBooleanProperty emptyProperty() {
      if (this.empty0 == null) {
         this.empty0 = new EmptyProperty();
      }

      return this.empty0;
   }

   public void addListener(InvalidationListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ListChangeListener var1) {
      this.helper = ListExpressionHelper.addListener(this.helper, this, (ListChangeListener)var1);
   }

   public void removeListener(ListChangeListener var1) {
      this.helper = ListExpressionHelper.removeListener(this.helper, var1);
   }

   protected void fireValueChangedEvent() {
      ListExpressionHelper.fireValueChangedEvent(this.helper);
   }

   protected void fireValueChangedEvent(ListChangeListener.Change var1) {
      ListExpressionHelper.fireValueChangedEvent(this.helper, var1);
   }

   private void invalidateProperties() {
      if (this.size0 != null) {
         this.size0.fireValueChangedEvent();
      }

      if (this.empty0 != null) {
         this.empty0.fireValueChangedEvent();
      }

   }

   private void markInvalid(ObservableList var1) {
      if (this.valid) {
         if (var1 != null) {
            var1.removeListener(this.listChangeListener);
         }

         this.valid = false;
         this.invalidateProperties();
         this.invalidated();
         this.fireValueChangedEvent();
      }

   }

   protected void invalidated() {
   }

   public ObservableList get() {
      if (!this.valid) {
         this.value = this.observable == null ? this.value : (ObservableList)this.observable.getValue();
         this.valid = true;
         if (this.value != null) {
            this.value.addListener(this.listChangeListener);
         }
      }

      return this.value;
   }

   public void set(ObservableList var1) {
      if (!this.isBound()) {
         if (this.value != var1) {
            ObservableList var2 = this.value;
            this.value = var1;
            this.markInvalid(var2);
         }

      } else {
         throw new RuntimeException((this.getBean() != null && this.getName() != null ? this.getBean().getClass().getSimpleName() + "." + this.getName() + " : " : "") + "A bound value cannot be set.");
      }
   }

   public boolean isBound() {
      return this.observable != null;
   }

   public void bind(ObservableValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot bind to null");
      } else {
         if (!var1.equals(this.observable)) {
            this.unbind();
            this.observable = var1;
            if (this.listener == null) {
               this.listener = new Listener(this);
            }

            this.observable.addListener(this.listener);
            this.markInvalid(this.value);
         }

      }
   }

   public void unbind() {
      if (this.observable != null) {
         this.value = (ObservableList)this.observable.getValue();
         this.observable.removeListener(this.listener);
         this.observable = null;
      }

   }

   public String toString() {
      Object var1 = this.getBean();
      String var2 = this.getName();
      StringBuilder var3 = new StringBuilder("ListProperty [");
      if (var1 != null) {
         var3.append("bean: ").append(var1).append(", ");
      }

      if (var2 != null && !var2.equals("")) {
         var3.append("name: ").append(var2).append(", ");
      }

      if (this.isBound()) {
         var3.append("bound, ");
         if (this.valid) {
            var3.append("value: ").append(this.get());
         } else {
            var3.append("invalid");
         }
      } else {
         var3.append("value: ").append(this.get());
      }

      var3.append("]");
      return var3.toString();
   }

   private static class Listener implements InvalidationListener {
      private final WeakReference wref;

      public Listener(ListPropertyBase var1) {
         this.wref = new WeakReference(var1);
      }

      public void invalidated(Observable var1) {
         ListPropertyBase var2 = (ListPropertyBase)this.wref.get();
         if (var2 == null) {
            var1.removeListener(this);
         } else {
            var2.markInvalid(var2.value);
         }

      }
   }

   private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
      private EmptyProperty() {
      }

      public boolean get() {
         return ListPropertyBase.this.isEmpty();
      }

      public Object getBean() {
         return ListPropertyBase.this;
      }

      public String getName() {
         return "empty";
      }

      protected void fireValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      // $FF: synthetic method
      EmptyProperty(Object var2) {
         this();
      }
   }

   private class SizeProperty extends ReadOnlyIntegerPropertyBase {
      private SizeProperty() {
      }

      public int get() {
         return ListPropertyBase.this.size();
      }

      public Object getBean() {
         return ListPropertyBase.this;
      }

      public String getName() {
         return "size";
      }

      protected void fireValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      // $FF: synthetic method
      SizeProperty(Object var2) {
         this();
      }
   }
}
