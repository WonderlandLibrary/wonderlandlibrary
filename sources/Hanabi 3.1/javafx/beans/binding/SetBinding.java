package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.SetExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public abstract class SetBinding extends SetExpression implements Binding {
   private final SetChangeListener setChangeListener = new SetChangeListener() {
      public void onChanged(SetChangeListener.Change var1) {
         SetBinding.this.invalidateProperties();
         SetBinding.this.onInvalidating();
         SetExpressionHelper.fireValueChangedEvent(SetBinding.this.helper, var1);
      }
   };
   private ObservableSet value;
   private boolean valid = false;
   private BindingHelperObserver observer;
   private SetExpressionHelper helper = null;
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
      this.helper = SetExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = SetExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(SetChangeListener var1) {
      this.helper = SetExpressionHelper.addListener(this.helper, this, (SetChangeListener)var1);
   }

   public void removeListener(SetChangeListener var1) {
      this.helper = SetExpressionHelper.removeListener(this.helper, var1);
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

   public final ObservableSet get() {
      if (!this.valid) {
         this.value = this.computeValue();
         this.valid = true;
         if (this.value != null) {
            this.value.addListener(this.setChangeListener);
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
            this.value.removeListener(this.setChangeListener);
         }

         this.valid = false;
         this.invalidateProperties();
         this.onInvalidating();
         SetExpressionHelper.fireValueChangedEvent(this.helper);
      }

   }

   public final boolean isValid() {
      return this.valid;
   }

   protected abstract ObservableSet computeValue();

   public String toString() {
      return this.valid ? "SetBinding [value: " + this.get() + "]" : "SetBinding [invalid]";
   }

   private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
      private EmptyProperty() {
      }

      public boolean get() {
         return SetBinding.this.isEmpty();
      }

      public Object getBean() {
         return SetBinding.this;
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
         return SetBinding.this.size();
      }

      public Object getBean() {
         return SetBinding.this;
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
