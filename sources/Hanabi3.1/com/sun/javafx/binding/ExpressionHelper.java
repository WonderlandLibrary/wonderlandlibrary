package com.sun.javafx.binding;

import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class ExpressionHelper extends ExpressionHelperBase {
   protected final ObservableValue observable;

   public static ExpressionHelper addListener(ExpressionHelper var0, ObservableValue var1, InvalidationListener var2) {
      if (var1 != null && var2 != null) {
         var1.getValue();
         return (ExpressionHelper)(var0 == null ? new SingleInvalidation(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static ExpressionHelper removeListener(ExpressionHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static ExpressionHelper addListener(ExpressionHelper var0, ObservableValue var1, ChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (ExpressionHelper)(var0 == null ? new SingleChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static ExpressionHelper removeListener(ExpressionHelper var0, ChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static void fireValueChangedEvent(ExpressionHelper var0) {
      if (var0 != null) {
         var0.fireValueChangedEvent();
      }

   }

   private ExpressionHelper(ObservableValue var1) {
      this.observable = var1;
   }

   protected abstract ExpressionHelper addListener(InvalidationListener var1);

   protected abstract ExpressionHelper removeListener(InvalidationListener var1);

   protected abstract ExpressionHelper addListener(ChangeListener var1);

   protected abstract ExpressionHelper removeListener(ChangeListener var1);

   protected abstract void fireValueChangedEvent();

   // $FF: synthetic method
   ExpressionHelper(ObservableValue var1, Object var2) {
      this(var1);
   }

   private static class Generic extends ExpressionHelper {
      private InvalidationListener[] invalidationListeners;
      private ChangeListener[] changeListeners;
      private int invalidationSize;
      private int changeSize;
      private boolean locked;
      private Object currentValue;

      private Generic(ObservableValue var1, InvalidationListener var2, InvalidationListener var3) {
         super(var1, null);
         this.invalidationListeners = new InvalidationListener[]{var2, var3};
         this.invalidationSize = 2;
      }

      private Generic(ObservableValue var1, ChangeListener var2, ChangeListener var3) {
         super(var1, null);
         this.changeListeners = new ChangeListener[]{var2, var3};
         this.changeSize = 2;
         this.currentValue = var1.getValue();
      }

      private Generic(ObservableValue var1, InvalidationListener var2, ChangeListener var3) {
         super(var1, null);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.changeListeners = new ChangeListener[]{var3};
         this.changeSize = 1;
         this.currentValue = var1.getValue();
      }

      protected Generic addListener(InvalidationListener var1) {
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

      protected ExpressionHelper removeListener(InvalidationListener var1) {
         if (this.invalidationListeners != null) {
            for(int var2 = 0; var2 < this.invalidationSize; ++var2) {
               if (var1.equals(this.invalidationListeners[var2])) {
                  if (this.invalidationSize == 1) {
                     if (this.changeSize == 1) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     this.invalidationListeners = null;
                     this.invalidationSize = 0;
                  } else {
                     if (this.invalidationSize == 2 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[1 - var2]);
                     }

                     int var3 = this.invalidationSize - var2 - 1;
                     InvalidationListener[] var4 = this.invalidationListeners;
                     if (this.locked) {
                        this.invalidationListeners = new InvalidationListener[this.invalidationListeners.length];
                        System.arraycopy(var4, 0, this.invalidationListeners, 0, var2);
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

      protected ExpressionHelper addListener(ChangeListener var1) {
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
            this.currentValue = this.observable.getValue();
         }

         return this;
      }

      protected ExpressionHelper removeListener(ChangeListener var1) {
         if (this.changeListeners != null) {
            for(int var2 = 0; var2 < this.changeSize; ++var2) {
               if (var1.equals(this.changeListeners[var2])) {
                  if (this.changeSize == 1) {
                     if (this.invalidationSize == 1) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     this.changeListeners = null;
                     this.changeSize = 0;
                  } else {
                     if (this.changeSize == 2 && this.invalidationSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[1 - var2]);
                     }

                     int var3 = this.changeSize - var2 - 1;
                     ChangeListener[] var4 = this.changeListeners;
                     if (this.locked) {
                        this.changeListeners = new ChangeListener[this.changeListeners.length];
                        System.arraycopy(var4, 0, this.changeListeners, 0, var2);
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

      protected void fireValueChangedEvent() {
         InvalidationListener[] var1 = this.invalidationListeners;
         int var2 = this.invalidationSize;
         ChangeListener[] var3 = this.changeListeners;
         int var4 = this.changeSize;

         try {
            this.locked = true;

            for(int var5 = 0; var5 < var2; ++var5) {
               try {
                  var1[var5].invalidated(this.observable);
               } catch (Exception var14) {
                  Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var14);
               }
            }

            if (var4 > 0) {
               Object var16 = this.currentValue;
               this.currentValue = this.observable.getValue();
               boolean var6 = this.currentValue == null ? var16 != null : !this.currentValue.equals(var16);
               if (var6) {
                  for(int var7 = 0; var7 < var4; ++var7) {
                     try {
                        var3[var7].changed(this.observable, var16, this.currentValue);
                     } catch (Exception var13) {
                        Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var13);
                     }
                  }
               }
            }
         } finally {
            this.locked = false;
         }

      }

      // $FF: synthetic method
      Generic(ObservableValue var1, InvalidationListener var2, InvalidationListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableValue var1, InvalidationListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableValue var1, ChangeListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class SingleChange extends ExpressionHelper {
      private final ChangeListener listener;
      private Object currentValue;

      private SingleChange(ObservableValue var1, ChangeListener var2) {
         super(var1, null);
         this.listener = var2;
         this.currentValue = var1.getValue();
      }

      protected ExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected ExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected ExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ExpressionHelper removeListener(ChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected void fireValueChangedEvent() {
         Object var1 = this.currentValue;
         this.currentValue = this.observable.getValue();
         boolean var2 = this.currentValue == null ? var1 != null : !this.currentValue.equals(var1);
         if (var2) {
            try {
               this.listener.changed(this.observable, var1, this.currentValue);
            } catch (Exception var4) {
               Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var4);
            }
         }

      }

      // $FF: synthetic method
      SingleChange(ObservableValue var1, ChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleInvalidation extends ExpressionHelper {
      private final InvalidationListener listener;

      private SingleInvalidation(ObservableValue var1, InvalidationListener var2) {
         super(var1, null);
         this.listener = var2;
      }

      protected ExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ExpressionHelper removeListener(InvalidationListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected ExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected ExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         try {
            this.listener.invalidated(this.observable);
         } catch (Exception var2) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var2);
         }

      }

      // $FF: synthetic method
      SingleInvalidation(ObservableValue var1, InvalidationListener var2, Object var3) {
         this(var1, var2);
      }
   }
}
