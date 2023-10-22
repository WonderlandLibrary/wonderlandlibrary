package com.sun.javafx.binding;

import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.collections.SourceAdapterChange;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class ListExpressionHelper extends ExpressionHelperBase {
   protected final ObservableListValue observable;

   public static ListExpressionHelper addListener(ListExpressionHelper var0, ObservableListValue var1, InvalidationListener var2) {
      if (var1 != null && var2 != null) {
         var1.getValue();
         return (ListExpressionHelper)(var0 == null ? new SingleInvalidation(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static ListExpressionHelper removeListener(ListExpressionHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static ListExpressionHelper addListener(ListExpressionHelper var0, ObservableListValue var1, ChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (ListExpressionHelper)(var0 == null ? new SingleChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static ListExpressionHelper removeListener(ListExpressionHelper var0, ChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static ListExpressionHelper addListener(ListExpressionHelper var0, ObservableListValue var1, ListChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (ListExpressionHelper)(var0 == null ? new SingleListChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static ListExpressionHelper removeListener(ListExpressionHelper var0, ListChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static void fireValueChangedEvent(ListExpressionHelper var0) {
      if (var0 != null) {
         var0.fireValueChangedEvent();
      }

   }

   public static void fireValueChangedEvent(ListExpressionHelper var0, ListChangeListener.Change var1) {
      if (var0 != null) {
         var0.fireValueChangedEvent(var1);
      }

   }

   protected ListExpressionHelper(ObservableListValue var1) {
      this.observable = var1;
   }

   protected abstract ListExpressionHelper addListener(InvalidationListener var1);

   protected abstract ListExpressionHelper removeListener(InvalidationListener var1);

   protected abstract ListExpressionHelper addListener(ChangeListener var1);

   protected abstract ListExpressionHelper removeListener(ChangeListener var1);

   protected abstract ListExpressionHelper addListener(ListChangeListener var1);

   protected abstract ListExpressionHelper removeListener(ListChangeListener var1);

   protected abstract void fireValueChangedEvent();

   protected abstract void fireValueChangedEvent(ListChangeListener.Change var1);

   private static class Generic extends ListExpressionHelper {
      private InvalidationListener[] invalidationListeners;
      private ChangeListener[] changeListeners;
      private ListChangeListener[] listChangeListeners;
      private int invalidationSize;
      private int changeSize;
      private int listChangeSize;
      private boolean locked;
      private ObservableList currentValue;

      private Generic(ObservableListValue var1, InvalidationListener var2, InvalidationListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2, var3};
         this.invalidationSize = 2;
      }

      private Generic(ObservableListValue var1, ChangeListener var2, ChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2, var3};
         this.changeSize = 2;
         this.currentValue = (ObservableList)var1.getValue();
      }

      private Generic(ObservableListValue var1, ListChangeListener var2, ListChangeListener var3) {
         super(var1);
         this.listChangeListeners = new ListChangeListener[]{var2, var3};
         this.listChangeSize = 2;
         this.currentValue = (ObservableList)var1.getValue();
      }

      private Generic(ObservableListValue var1, InvalidationListener var2, ChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.changeListeners = new ChangeListener[]{var3};
         this.changeSize = 1;
         this.currentValue = (ObservableList)var1.getValue();
      }

      private Generic(ObservableListValue var1, InvalidationListener var2, ListChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.listChangeListeners = new ListChangeListener[]{var3};
         this.listChangeSize = 1;
         this.currentValue = (ObservableList)var1.getValue();
      }

      private Generic(ObservableListValue var1, ChangeListener var2, ListChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2};
         this.changeSize = 1;
         this.listChangeListeners = new ListChangeListener[]{var3};
         this.listChangeSize = 1;
         this.currentValue = (ObservableList)var1.getValue();
      }

      protected ListExpressionHelper addListener(InvalidationListener var1) {
         if (this.invalidationListeners == null) {
            this.invalidationListeners = new InvalidationListener[]{var1};
            this.invalidationSize = 1;
         } else {
            int var2 = this.invalidationListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.invalidationSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.invalidationListeners = (InvalidationListener[])Arrays.copyOf(this.invalidationListeners, var3);
            } else if (this.invalidationSize == var2) {
               this.invalidationSize = trim(this.invalidationSize, this.invalidationListeners);
               if (this.invalidationSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.invalidationListeners = (InvalidationListener[])Arrays.copyOf(this.invalidationListeners, var3);
               }
            }

            this.invalidationListeners[this.invalidationSize++] = var1;
         }

         return this;
      }

      protected ListExpressionHelper removeListener(InvalidationListener var1) {
         if (this.invalidationListeners != null) {
            for(int var2 = 0; var2 < this.invalidationSize; ++var2) {
               if (var1.equals(this.invalidationListeners[var2])) {
                  if (this.invalidationSize == 1) {
                     if (this.changeSize == 1 && this.listChangeSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     if (this.changeSize == 0 && this.listChangeSize == 1) {
                        return new SingleListChange(this.observable, this.listChangeListeners[0]);
                     }

                     this.invalidationListeners = null;
                     this.invalidationSize = 0;
                  } else {
                     if (this.invalidationSize == 2 && this.changeSize == 0 && this.listChangeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[1 - var2]);
                     }

                     int var3 = this.invalidationSize - var2 - 1;
                     InvalidationListener[] var4 = this.invalidationListeners;
                     if (this.locked) {
                        this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                        System.arraycopy(var4, 0, this.invalidationListeners, 0, var2 + 1);
                     }

                     if (var3 > 0) {
                        System.arraycopy(var4, var2 + 1, this.invalidationListeners, var2, var3);
                     }

                     --this.invalidationSize;
                     if (!this.locked) {
                        this.invalidationListeners[this.invalidationSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected ListExpressionHelper addListener(ChangeListener var1) {
         if (this.changeListeners == null) {
            this.changeListeners = new ChangeListener[]{var1};
            this.changeSize = 1;
         } else {
            int var2 = this.changeListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.changeSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.changeListeners = (ChangeListener[])Arrays.copyOf(this.changeListeners, var3);
            } else if (this.changeSize == var2) {
               this.changeSize = trim(this.changeSize, this.changeListeners);
               if (this.changeSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.changeListeners = (ChangeListener[])Arrays.copyOf(this.changeListeners, var3);
               }
            }

            this.changeListeners[this.changeSize++] = var1;
         }

         if (this.changeSize == 1) {
            this.currentValue = (ObservableList)this.observable.getValue();
         }

         return this;
      }

      protected ListExpressionHelper removeListener(ChangeListener var1) {
         if (this.changeListeners != null) {
            for(int var2 = 0; var2 < this.changeSize; ++var2) {
               if (var1.equals(this.changeListeners[var2])) {
                  if (this.changeSize == 1) {
                     if (this.invalidationSize == 1 && this.listChangeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.listChangeSize == 1) {
                        return new SingleListChange(this.observable, this.listChangeListeners[0]);
                     }

                     this.changeListeners = null;
                     this.changeSize = 0;
                  } else {
                     if (this.changeSize == 2 && this.invalidationSize == 0 && this.listChangeSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[1 - var2]);
                     }

                     int var3 = this.changeSize - var2 - 1;
                     ChangeListener[] var4 = this.changeListeners;
                     if (this.locked) {
                        this.changeListeners = new ChangeListener[this.changeListeners.length];
                        System.arraycopy(var4, 0, this.changeListeners, 0, var2 + 1);
                     }

                     if (var3 > 0) {
                        System.arraycopy(var4, var2 + 1, this.changeListeners, var2, var3);
                     }

                     --this.changeSize;
                     if (!this.locked) {
                        this.changeListeners[this.changeSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected ListExpressionHelper addListener(ListChangeListener var1) {
         if (this.listChangeListeners == null) {
            this.listChangeListeners = new ListChangeListener[]{var1};
            this.listChangeSize = 1;
         } else {
            int var2 = this.listChangeListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.listChangeSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.listChangeListeners = (ListChangeListener[])Arrays.copyOf(this.listChangeListeners, var3);
            } else if (this.listChangeSize == var2) {
               this.listChangeSize = trim(this.listChangeSize, this.listChangeListeners);
               if (this.listChangeSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.listChangeListeners = (ListChangeListener[])Arrays.copyOf(this.listChangeListeners, var3);
               }
            }

            this.listChangeListeners[this.listChangeSize++] = var1;
         }

         if (this.listChangeSize == 1) {
            this.currentValue = (ObservableList)this.observable.getValue();
         }

         return this;
      }

      protected ListExpressionHelper removeListener(ListChangeListener var1) {
         if (this.listChangeListeners != null) {
            for(int var2 = 0; var2 < this.listChangeSize; ++var2) {
               if (var1.equals(this.listChangeListeners[var2])) {
                  if (this.listChangeSize == 1) {
                     if (this.invalidationSize == 1 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.changeSize == 1) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     this.listChangeListeners = null;
                     this.listChangeSize = 0;
                  } else {
                     if (this.listChangeSize == 2 && this.invalidationSize == 0 && this.changeSize == 0) {
                        return new SingleListChange(this.observable, this.listChangeListeners[1 - var2]);
                     }

                     int var3 = this.listChangeSize - var2 - 1;
                     ListChangeListener[] var4 = this.listChangeListeners;
                     if (this.locked) {
                        this.listChangeListeners = new ListChangeListener[this.listChangeListeners.length];
                        System.arraycopy(var4, 0, this.listChangeListeners, 0, var2 + 1);
                     }

                     if (var3 > 0) {
                        System.arraycopy(var4, var2 + 1, this.listChangeListeners, var2, var3);
                     }

                     --this.listChangeSize;
                     if (!this.locked) {
                        this.listChangeListeners[this.listChangeSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected void fireValueChangedEvent() {
         if (this.changeSize == 0 && this.listChangeSize == 0) {
            this.notifyListeners(this.currentValue, (ListChangeListener.Change)null, false);
         } else {
            ObservableList var1 = this.currentValue;
            this.currentValue = (ObservableList)this.observable.getValue();
            if (this.currentValue != var1) {
               NonIterableChange.GenericAddRemoveChange var2 = null;
               if (this.listChangeSize > 0) {
                  int var3 = this.currentValue == null ? 0 : this.currentValue.size();
                  ObservableList var4 = var1 == null ? FXCollections.emptyObservableList() : FXCollections.unmodifiableObservableList(var1);
                  var2 = new NonIterableChange.GenericAddRemoveChange(0, var3, var4, this.observable);
               }

               this.notifyListeners(var1, var2, false);
            } else {
               this.notifyListeners(this.currentValue, (ListChangeListener.Change)null, true);
            }
         }

      }

      protected void fireValueChangedEvent(ListChangeListener.Change var1) {
         SourceAdapterChange var2 = this.listChangeSize == 0 ? null : new SourceAdapterChange(this.observable, var1);
         this.notifyListeners(this.currentValue, var2, false);
      }

      private void notifyListeners(ObservableList var1, ListChangeListener.Change var2, boolean var3) {
         InvalidationListener[] var4 = this.invalidationListeners;
         int var5 = this.invalidationSize;
         ChangeListener[] var6 = this.changeListeners;
         int var7 = this.changeSize;
         ListChangeListener[] var8 = this.listChangeListeners;
         int var9 = this.listChangeSize;

         try {
            this.locked = true;

            int var10;
            for(var10 = 0; var10 < var5; ++var10) {
               var4[var10].invalidated(this.observable);
            }

            if (!var3) {
               for(var10 = 0; var10 < var7; ++var10) {
                  var6[var10].changed(this.observable, var1, this.currentValue);
               }

               if (var2 != null) {
                  for(var10 = 0; var10 < var9; ++var10) {
                     var2.reset();
                     var8[var10].onChanged(var2);
                  }
               }
            }
         } finally {
            this.locked = false;
         }

      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, InvalidationListener var2, InvalidationListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, InvalidationListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, InvalidationListener var2, ListChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, ChangeListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, ChangeListener var2, ListChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableListValue var1, ListChangeListener var2, ListChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class SingleListChange extends ListExpressionHelper {
      private final ListChangeListener listener;
      private ObservableList currentValue;

      private SingleListChange(ObservableListValue var1, ListChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableList)var1.getValue();
      }

      protected ListExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected ListExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected ListExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected ListExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected ListExpressionHelper addListener(ListChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(ListChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected void fireValueChangedEvent() {
         ObservableList var1 = this.currentValue;
         this.currentValue = (ObservableList)this.observable.getValue();
         if (this.currentValue != var1) {
            int var2 = this.currentValue == null ? 0 : this.currentValue.size();
            ObservableList var3 = var1 == null ? FXCollections.emptyObservableList() : FXCollections.unmodifiableObservableList(var1);
            NonIterableChange.GenericAddRemoveChange var4 = new NonIterableChange.GenericAddRemoveChange(0, var2, var3, this.observable);
            this.listener.onChanged(var4);
         }

      }

      protected void fireValueChangedEvent(ListChangeListener.Change var1) {
         this.listener.onChanged(new SourceAdapterChange(this.observable, var1));
      }

      // $FF: synthetic method
      SingleListChange(ObservableListValue var1, ListChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleChange extends ListExpressionHelper {
      private final ChangeListener listener;
      private ObservableList currentValue;

      private SingleChange(ObservableListValue var1, ChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableList)var1.getValue();
      }

      protected ListExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected ListExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected ListExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(ChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected ListExpressionHelper addListener(ListChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(ListChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         ObservableList var1 = this.currentValue;
         this.currentValue = (ObservableList)this.observable.getValue();
         if (this.currentValue != var1) {
            this.listener.changed(this.observable, var1, this.currentValue);
         }

      }

      protected void fireValueChangedEvent(ListChangeListener.Change var1) {
         this.listener.changed(this.observable, this.currentValue, this.currentValue);
      }

      // $FF: synthetic method
      SingleChange(ObservableListValue var1, ChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleInvalidation extends ListExpressionHelper {
      private final InvalidationListener listener;

      private SingleInvalidation(ObservableListValue var1, InvalidationListener var2) {
         super(var1);
         this.listener = var2;
      }

      protected ListExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(InvalidationListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected ListExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected ListExpressionHelper addListener(ListChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ListExpressionHelper removeListener(ListChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         this.listener.invalidated(this.observable);
      }

      protected void fireValueChangedEvent(ListChangeListener.Change var1) {
         this.listener.invalidated(this.observable);
      }

      // $FF: synthetic method
      SingleInvalidation(ObservableListValue var1, InvalidationListener var2, Object var3) {
         this(var1, var2);
      }
   }
}
