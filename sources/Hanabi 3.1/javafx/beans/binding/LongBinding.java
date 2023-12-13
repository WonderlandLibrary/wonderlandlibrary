package javafx.beans.binding;

import com.sun.javafx.binding.BindingHelperObserver;
import com.sun.javafx.binding.ExpressionHelper;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class LongBinding extends LongExpression implements NumberBinding {
   private long value;
   private boolean valid = false;
   private BindingHelperObserver observer;
   private ExpressionHelper helper = null;

   public void addListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (InvalidationListener)var1);
   }

   public void removeListener(InvalidationListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
   }

   public void addListener(ChangeListener var1) {
      this.helper = ExpressionHelper.addListener(this.helper, this, (ChangeListener)var1);
   }

   public void removeListener(ChangeListener var1) {
      this.helper = ExpressionHelper.removeListener(this.helper, var1);
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
            var5.addListener(this.observer);
         }
      }

   }

   protected final void unbind(Observable... var1) {
      if (this.observer != null) {
         Observable[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Observable var5 = var2[var4];
            var5.removeListener(this.observer);
         }

         this.observer = null;
      }

   }

   public void dispose() {
   }

   public ObservableList getDependencies() {
      return FXCollections.emptyObservableList();
   }

   public final long get() {
      if (!this.valid) {
         this.value = this.computeValue();
         this.valid = true;
      }

      return this.value;
   }

   protected void onInvalidating() {
   }

   public final void invalidate() {
      if (this.valid) {
         this.valid = false;
         this.onInvalidating();
         ExpressionHelper.fireValueChangedEvent(this.helper);
      }

   }

   public final boolean isValid() {
      return this.valid;
   }

   protected abstract long computeValue();

   public String toString() {
      return this.valid ? "LongBinding [value: " + this.get() + "]" : "LongBinding [invalid]";
   }
}
