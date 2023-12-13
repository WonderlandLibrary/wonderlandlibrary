package com.sun.javafx.collections;

import com.sun.javafx.binding.ExpressionHelperBase;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.collections.MapChangeListener;

public abstract class MapListenerHelper extends ExpressionHelperBase {
   public static MapListenerHelper addListener(MapListenerHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return (MapListenerHelper)(var0 == null ? new SingleInvalidation(var1) : var0.addListener(var1));
      }
   }

   public static MapListenerHelper removeListener(MapListenerHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static MapListenerHelper addListener(MapListenerHelper var0, MapChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return (MapListenerHelper)(var0 == null ? new SingleChange(var1) : var0.addListener(var1));
      }
   }

   public static MapListenerHelper removeListener(MapListenerHelper var0, MapChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static void fireValueChangedEvent(MapListenerHelper var0, MapChangeListener.Change var1) {
      if (var0 != null) {
         var0.fireValueChangedEvent(var1);
      }

   }

   public static boolean hasListeners(MapListenerHelper var0) {
      return var0 != null;
   }

   protected abstract MapListenerHelper addListener(InvalidationListener var1);

   protected abstract MapListenerHelper removeListener(InvalidationListener var1);

   protected abstract MapListenerHelper addListener(MapChangeListener var1);

   protected abstract MapListenerHelper removeListener(MapChangeListener var1);

   protected abstract void fireValueChangedEvent(MapChangeListener.Change var1);

   private static class Generic extends MapListenerHelper {
      private InvalidationListener[] invalidationListeners;
      private MapChangeListener[] changeListeners;
      private int invalidationSize;
      private int changeSize;
      private boolean locked;

      private Generic(InvalidationListener var1, InvalidationListener var2) {
         this.invalidationListeners = new InvalidationListener[]{var1, var2};
         this.invalidationSize = 2;
      }

      private Generic(MapChangeListener var1, MapChangeListener var2) {
         this.changeListeners = new MapChangeListener[]{var1, var2};
         this.changeSize = 2;
      }

      private Generic(InvalidationListener var1, MapChangeListener var2) {
         this.invalidationListeners = new InvalidationListener[]{var1};
         this.invalidationSize = 1;
         this.changeListeners = new MapChangeListener[]{var2};
         this.changeSize = 1;
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

      protected MapListenerHelper removeListener(InvalidationListener var1) {
         if (this.invalidationListeners != null) {
            for(int var2 = 0; var2 < this.invalidationSize; ++var2) {
               if (var1.equals(this.invalidationListeners[var2])) {
                  if (this.invalidationSize == 1) {
                     if (this.changeSize == 1) {
                        return new SingleChange(this.changeListeners[0]);
                     }

                     this.invalidationListeners = null;
                     this.invalidationSize = 0;
                  } else {
                     if (this.invalidationSize == 2 && this.changeSize == 0) {
                        return new SingleInvalidation(this.invalidationListeners[1 - var2]);
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

      protected MapListenerHelper addListener(MapChangeListener var1) {
         if (this.changeListeners == null) {
            this.changeListeners = new MapChangeListener[]{var1};
            this.changeSize = 1;
         } else {
            int var2 = this.changeListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.changeSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.changeListeners = (MapChangeListener[])Arrays.copyOf(this.changeListeners, var3);
            } else if (this.changeSize == var2) {
               this.changeSize = trim(this.changeSize, this.changeListeners);
               if (this.changeSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.changeListeners = (MapChangeListener[])Arrays.copyOf(this.changeListeners, var3);
               }
            }

            this.changeListeners[this.changeSize++] = var1;
         }

         return this;
      }

      protected MapListenerHelper removeListener(MapChangeListener var1) {
         if (this.changeListeners != null) {
            for(int var2 = 0; var2 < this.changeSize; ++var2) {
               if (var1.equals(this.changeListeners[var2])) {
                  if (this.changeSize == 1) {
                     if (this.invalidationSize == 1) {
                        return new SingleInvalidation(this.invalidationListeners[0]);
                     }

                     this.changeListeners = null;
                     this.changeSize = 0;
                  } else {
                     if (this.changeSize == 2 && this.invalidationSize == 0) {
                        return new SingleChange(this.changeListeners[1 - var2]);
                     }

                     int var3 = this.changeSize - var2 - 1;
                     MapChangeListener[] var4 = this.changeListeners;
                     if (this.locked) {
                        this.changeListeners = new MapChangeListener[this.changeListeners.length];
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

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         InvalidationListener[] var2 = this.invalidationListeners;
         int var3 = this.invalidationSize;
         MapChangeListener[] var4 = this.changeListeners;
         int var5 = this.changeSize;

         try {
            this.locked = true;

            int var6;
            for(var6 = 0; var6 < var3; ++var6) {
               try {
                  var2[var6].invalidated(var1.getMap());
               } catch (Exception var13) {
                  Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var13);
               }
            }

            for(var6 = 0; var6 < var5; ++var6) {
               try {
                  var4[var6].onChanged(var1);
               } catch (Exception var12) {
                  Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var12);
               }
            }

         } finally {
            this.locked = false;
         }
      }

      // $FF: synthetic method
      Generic(InvalidationListener var1, InvalidationListener var2, Object var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      Generic(InvalidationListener var1, MapChangeListener var2, Object var3) {
         this(var1, var2);
      }

      // $FF: synthetic method
      Generic(MapChangeListener var1, MapChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleChange extends MapListenerHelper {
      private final MapChangeListener listener;

      private SingleChange(MapChangeListener var1) {
         this.listener = var1;
      }

      protected MapListenerHelper addListener(InvalidationListener var1) {
         return new Generic(var1, this.listener);
      }

      protected MapListenerHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected MapListenerHelper addListener(MapChangeListener var1) {
         return new Generic(this.listener, var1);
      }

      protected MapListenerHelper removeListener(MapChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         try {
            this.listener.onChanged(var1);
         } catch (Exception var3) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var3);
         }

      }

      // $FF: synthetic method
      SingleChange(MapChangeListener var1, Object var2) {
         this(var1);
      }
   }

   private static class SingleInvalidation extends MapListenerHelper {
      private final InvalidationListener listener;

      private SingleInvalidation(InvalidationListener var1) {
         this.listener = var1;
      }

      protected MapListenerHelper addListener(InvalidationListener var1) {
         return new Generic(this.listener, var1);
      }

      protected MapListenerHelper removeListener(InvalidationListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected MapListenerHelper addListener(MapChangeListener var1) {
         return new Generic(this.listener, var1);
      }

      protected MapListenerHelper removeListener(MapChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         try {
            this.listener.invalidated(var1.getMap());
         } catch (Exception var3) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var3);
         }

      }

      // $FF: synthetic method
      SingleInvalidation(InvalidationListener var1, Object var2) {
         this(var1);
      }
   }
}
