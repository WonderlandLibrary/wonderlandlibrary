package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.ListExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ListBinding extends ListExpression implements Binding {
   private final ListChangeListener listChangeListener = new ListChangeListener() {
      public void onChanged(ListChangeListener.Change var1) {
         ListBinding.this.invalidateProperties();
         ListBinding.this.onInvalidating();
         ListExpressionHelper.fireValueChangedEvent(ListBinding.this.helper, var1);
      }
   };
   private ObservableList value;
   private boolean valid = false;
   private BindingHelperObserver observer;
   private ListExpressionHelper helper = null;
   private SizeProperty size0;
   private EmptyProperty empty0;

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

   protected final void bind(Observable... var1) {
      if (var1 != null && var1.length > 0) {
         if (this.observer == null) {
            this.observer = new BindingHelperObserver(this);
         }

         Observable[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Observable var5 = var2[var4];
            if (var5 != null) {
               var5.addListener(this.observer);
            }
         }
      }

   }

   protected final void unbind(Observable... var1) {
      if (this.observer != null) {
         Observable[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Observable var5 = var2[var4];
            if (var5 != null) {
               var5.removeListener(this.observer);
            }
         }

         this.observer = null;
      }

   }

   public void dispose() {
   }

   public ObservableList getDependencies() {
      return FXCollections.emptyObservableList();
   }

   public final ObservableList get() {
      if (!this.valid) {
         this.value = this.computeValue();
         this.valid = true;
         if (this.value != null) {
            this.value.addListener(this.listChangeListener);
         }
      }

      return this.value;
   }

   protected void onInvalidating() {
   }

   private void invalidateProperties() {
      if (this.size0 != null) {
         this.size0.fireValueChangedEvent();
      }

      if (this.empty0 != null) {
         this.empty0.fireValueChangedEvent();
      }

   }

   public final void invalidate() {
      if (this.valid) {
         if (this.value != null) {
            this.value.removeListener(this.listChangeListener);
         }

         this.valid = false;
         this.invalidateProperties();
         this.onInvalidating();
         ListExpressionHelper.fireValueChangedEvent(this.helper);
      }

   }

   public final boolean isValid() {
      return this.valid;
   }

   protected abstract ObservableList computeValue();

   public String toString() {
      return this.valid ? "ListBinding [value: " + this.get() + "]" : "ListBinding [invalid]";
   }

   private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
      private EmptyProperty() {
      }

      public boolean get() {
         return ListBinding.this.isEmpty();
      }

      public Object getBean() {
         return ListBinding.this;
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
         return ListBinding.this.size();
      }

      public Object getBean() {
         return ListBinding.this;
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
