package com.sun.javafx.binding;

import java.util.Arrays;
import java.util.Iterator;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableSetValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public abstract class SetExpressionHelper extends ExpressionHelperBase {
   protected final ObservableSetValue observable;

   public static SetExpressionHelper addListener(SetExpressionHelper var0, ObservableSetValue var1, InvalidationListener var2) {
      if (var1 != null && var2 != null) {
         var1.getValue();
         return (SetExpressionHelper)(var0 == null ? new SingleInvalidation(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static SetExpressionHelper removeListener(SetExpressionHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static SetExpressionHelper addListener(SetExpressionHelper var0, ObservableSetValue var1, ChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (SetExpressionHelper)(var0 == null ? new SingleChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static SetExpressionHelper removeListener(SetExpressionHelper var0, ChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static SetExpressionHelper addListener(SetExpressionHelper var0, ObservableSetValue var1, SetChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (SetExpressionHelper)(var0 == null ? new SingleSetChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static SetExpressionHelper removeListener(SetExpressionHelper var0, SetChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static void fireValueChangedEvent(SetExpressionHelper var0) {
      if (var0 != null) {
         var0.fireValueChangedEvent();
      }

   }

   public static void fireValueChangedEvent(SetExpressionHelper var0, SetChangeListener.Change var1) {
      if (var0 != null) {
         var0.fireValueChangedEvent(var1);
      }

   }

   protected SetExpressionHelper(ObservableSetValue var1) {
      this.observable = var1;
   }

   protected abstract SetExpressionHelper addListener(InvalidationListener var1);

   protected abstract SetExpressionHelper removeListener(InvalidationListener var1);

   protected abstract SetExpressionHelper addListener(ChangeListener var1);

   protected abstract SetExpressionHelper removeListener(ChangeListener var1);

   protected abstract SetExpressionHelper addListener(SetChangeListener var1);

   protected abstract SetExpressionHelper removeListener(SetChangeListener var1);

   protected abstract void fireValueChangedEvent();

   protected abstract void fireValueChangedEvent(SetChangeListener.Change var1);

   public static class SimpleChange extends SetChangeListener.Change {
      private Object old;
      private Object added;
      private boolean addOp;

      public SimpleChange(ObservableSet var1) {
         super(var1);
      }

      public SimpleChange(ObservableSet var1, SetChangeListener.Change var2) {
         super(var1);
         this.old = var2.getElementRemoved();
         this.added = var2.getElementAdded();
         this.addOp = var2.wasAdded();
      }

      public SimpleChange setRemoved(Object var1) {
         this.old = var1;
         this.added = null;
         this.addOp = false;
         return this;
      }

      public SimpleChange setAdded(Object var1) {
         this.old = null;
         this.added = var1;
         this.addOp = true;
         return this;
      }

      public boolean wasAdded() {
         return this.addOp;
      }

      public boolean wasRemoved() {
         return !this.addOp;
      }

      public Object getElementAdded() {
         return this.added;
      }

      public Object getElementRemoved() {
         return this.old;
      }

      public String toString() {
         return this.addOp ? "added " + this.added : "removed " + this.old;
      }
   }

   private static class Generic extends SetExpressionHelper {
      private InvalidationListener[] invalidationListeners;
      private ChangeListener[] changeListeners;
      private SetChangeListener[] setChangeListeners;
      private int invalidationSize;
      private int changeSize;
      private int setChangeSize;
      private boolean locked;
      private ObservableSet currentValue;

      private Generic(ObservableSetValue var1, InvalidationListener var2, InvalidationListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2, var3};
         this.invalidationSize = 2;
      }

      private Generic(ObservableSetValue var1, ChangeListener var2, ChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2, var3};
         this.changeSize = 2;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      private Generic(ObservableSetValue var1, SetChangeListener var2, SetChangeListener var3) {
         super(var1);
         this.setChangeListeners = new SetChangeListener[]{var2, var3};
         this.setChangeSize = 2;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      private Generic(ObservableSetValue var1, InvalidationListener var2, ChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.changeListeners = new ChangeListener[]{var3};
         this.changeSize = 1;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      private Generic(ObservableSetValue var1, InvalidationListener var2, SetChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.setChangeListeners = new SetChangeListener[]{var3};
         this.setChangeSize = 1;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      private Generic(ObservableSetValue var1, ChangeListener var2, SetChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2};
         this.changeSize = 1;
         this.setChangeListeners = new SetChangeListener[]{var3};
         this.setChangeSize = 1;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      protected SetExpressionHelper addListener(InvalidationListener var1) {
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

      protected SetExpressionHelper removeListener(InvalidationListener var1) {
         if (this.invalidationListeners != null) {
            for(int var2 = 0; var2 < this.invalidationSize; ++var2) {
               if (var1.equals(this.invalidationListeners[var2])) {
                  if (this.invalidationSize == 1) {
                     if (this.changeSize == 1 && this.setChangeSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     if (this.changeSize == 0 && this.setChangeSize == 1) {
                        return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                     }

                     this.invalidationListeners = null;
                     this.invalidationSize = 0;
                  } else {
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
                        this.invalidationListeners[--this.invalidationSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected SetExpressionHelper addListener(ChangeListener var1) {
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
            this.currentValue = (ObservableSet)this.observable.getValue();
         }

         return this;
      }

      protected SetExpressionHelper removeListener(ChangeListener var1) {
         if (this.changeListeners != null) {
            for(int var2 = 0; var2 < this.changeSize; ++var2) {
               if (var1.equals(this.changeListeners[var2])) {
                  if (this.changeSize == 1) {
                     if (this.invalidationSize == 1 && this.setChangeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.setChangeSize == 1) {
                        return new SingleSetChange(this.observable, this.setChangeListeners[0]);
                     }

                     this.changeListeners = null;
                     this.changeSize = 0;
                  } else {
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

      protected SetExpressionHelper addListener(SetChangeListener var1) {
         if (this.setChangeListeners == null) {
            this.setChangeListeners = new SetChangeListener[]{var1};
            this.setChangeSize = 1;
         } else {
            int var2 = this.setChangeListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.setChangeSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.setChangeListeners = (SetChangeListener[])Arrays.copyOf(this.setChangeListeners, var3);
            } else if (this.setChangeSize == var2) {
               this.setChangeSize = trim(this.setChangeSize, this.setChangeListeners);
               if (this.setChangeSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.setChangeListeners = (SetChangeListener[])Arrays.copyOf(this.setChangeListeners, var3);
               }
            }

            this.setChangeListeners[this.setChangeSize++] = var1;
         }

         if (this.setChangeSize == 1) {
            this.currentValue = (ObservableSet)this.observable.getValue();
         }

         return this;
      }

      protected SetExpressionHelper removeListener(SetChangeListener var1) {
         if (this.setChangeListeners != null) {
            for(int var2 = 0; var2 < this.setChangeSize; ++var2) {
               if (var1.equals(this.setChangeListeners[var2])) {
                  if (this.setChangeSize == 1) {
                     if (this.invalidationSize == 1 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.changeSize == 1) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     this.setChangeListeners = null;
                     this.setChangeSize = 0;
                  } else {
                     int var3 = this.setChangeSize - var2 - 1;
                     SetChangeListener[] var4 = this.setChangeListeners;
                     if (this.locked) {
                        this.setChangeListeners = new SetChangeListener[this.setChangeListeners.length];
                        System.arraycopy(var4, 0, this.setChangeListeners, 0, var2 + 1);
                     }

                     if (var3 > 0) {
                        System.arraycopy(var4, var2 + 1, this.setChangeListeners, var2, var3);
                     }

                     --this.setChangeSize;
                     if (!this.locked) {
                        this.setChangeListeners[this.setChangeSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected void fireValueChangedEvent() {
         if (this.changeSize == 0 && this.setChangeSize == 0) {
            this.notifyListeners(this.currentValue, (SimpleChange)null);
         } else {
            ObservableSet var1 = this.currentValue;
            this.currentValue = (ObservableSet)this.observable.getValue();
            this.notifyListeners(var1, (SimpleChange)null);
         }

      }

      protected void fireValueChangedEvent(SetChangeListener.Change var1) {
         SimpleChange var2 = this.setChangeSize == 0 ? null : new SimpleChange(this.observable, var1);
         this.notifyListeners(this.currentValue, var2);
      }

      private void notifyListeners(ObservableSet var1, SimpleChange var2) {
         InvalidationListener[] var3 = this.invalidationListeners;
         int var4 = this.invalidationSize;
         ChangeListener[] var5 = this.changeListeners;
         int var6 = this.changeSize;
         SetChangeListener[] var7 = this.setChangeListeners;
         int var8 = this.setChangeSize;

         try {
            this.locked = true;

            int var9;
            for(var9 = 0; var9 < var4; ++var9) {
               var3[var9].invalidated(this.observable);
            }

            if (this.currentValue != var1 || var2 != null) {
               for(var9 = 0; var9 < var6; ++var9) {
                  var5[var9].changed(this.observable, var1, this.currentValue);
               }

               if (var8 > 0) {
                  if (var2 != null) {
                     for(var9 = 0; var9 < var8; ++var9) {
                        var7[var9].onChanged(var2);
                     }
                  } else {
                     var2 = new SimpleChange(this.observable);
                     Object var10;
                     int var11;
                     Iterator var15;
                     if (this.currentValue == null) {
                        var15 = var1.iterator();

                        while(var15.hasNext()) {
                           var10 = var15.next();
                           var2.setRemoved(var10);

                           for(var11 = 0; var11 < var8; ++var11) {
                              var7[var11].onChanged(var2);
                           }
                        }
                     } else if (var1 == null) {
                        var15 = this.currentValue.iterator();

                        while(var15.hasNext()) {
                           var10 = var15.next();
                           var2.setAdded(var10);

                           for(var11 = 0; var11 < var8; ++var11) {
                              var7[var11].onChanged(var2);
                           }
                        }
                     } else {
                        var15 = var1.iterator();

                        while(true) {
                           do {
                              if (!var15.hasNext()) {
                                 var15 = this.currentValue.iterator();

                                 while(true) {
                                    do {
                                       if (!var15.hasNext()) {
                                          return;
                                       }

                                       var10 = var15.next();
                                    } while(var1.contains(var10));

                                    var2.setAdded(var10);

                                    for(var11 = 0; var11 < var8; ++var11) {
                                       var7[var11].onChanged(var2);
                                    }
                                 }
                              }

                              var10 = var15.next();
                           } while(this.currentValue.contains(var10));

                           var2.setRemoved(var10);

                           for(var11 = 0; var11 < var8; ++var11) {
                              var7[var11].onChanged(var2);
                           }
                        }
                     }
                  }
               }
            }
         } finally {
            this.locked = false;
         }

      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, InvalidationListener var2, InvalidationListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, InvalidationListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, InvalidationListener var2, SetChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, ChangeListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, ChangeListener var2, SetChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableSetValue var1, SetChangeListener var2, SetChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class SingleSetChange extends SetExpressionHelper {
      private final SetChangeListener listener;
      private ObservableSet currentValue;

      private SingleSetChange(ObservableSetValue var1, SetChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      protected SetExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected SetExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected SetExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected SetExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected SetExpressionHelper addListener(SetChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(SetChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected void fireValueChangedEvent() {
         ObservableSet var1 = this.currentValue;
         this.currentValue = (ObservableSet)this.observable.getValue();
         if (this.currentValue != var1) {
            SimpleChange var2 = new SimpleChange(this.observable);
            Iterator var3;
            Object var4;
            if (this.currentValue == null) {
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  var4 = var3.next();
                  this.listener.onChanged(var2.setRemoved(var4));
               }
            } else if (var1 == null) {
               var3 = this.currentValue.iterator();

               while(var3.hasNext()) {
                  var4 = var3.next();
                  this.listener.onChanged(var2.setAdded(var4));
               }
            } else {
               var3 = var1.iterator();

               while(var3.hasNext()) {
                  var4 = var3.next();
                  if (!this.currentValue.contains(var4)) {
                     this.listener.onChanged(var2.setRemoved(var4));
                  }
               }

               var3 = this.currentValue.iterator();

               while(var3.hasNext()) {
                  var4 = var3.next();
                  if (!var1.contains(var4)) {
                     this.listener.onChanged(var2.setAdded(var4));
                  }
               }
            }
         }

      }

      protected void fireValueChangedEvent(SetChangeListener.Change var1) {
         this.listener.onChanged(new SimpleChange(this.observable, var1));
      }

      // $FF: synthetic method
      SingleSetChange(ObservableSetValue var1, SetChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleChange extends SetExpressionHelper {
      private final ChangeListener listener;
      private ObservableSet currentValue;

      private SingleChange(ObservableSetValue var1, ChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableSet)var1.getValue();
      }

      protected SetExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected SetExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected SetExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(ChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected SetExpressionHelper addListener(SetChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(SetChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         ObservableSet var1 = this.currentValue;
         this.currentValue = (ObservableSet)this.observable.getValue();
         if (this.currentValue != var1) {
            this.listener.changed(this.observable, var1, this.currentValue);
         }

      }

      protected void fireValueChangedEvent(SetChangeListener.Change var1) {
         this.listener.changed(this.observable, this.currentValue, this.currentValue);
      }

      // $FF: synthetic method
      SingleChange(ObservableSetValue var1, ChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleInvalidation extends SetExpressionHelper {
      private final InvalidationListener listener;

      private SingleInvalidation(ObservableSetValue var1, InvalidationListener var2) {
         super(var1);
         this.listener = var2;
      }

      protected SetExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(InvalidationListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected SetExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected SetExpressionHelper addListener(SetChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected SetExpressionHelper removeListener(SetChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         this.listener.invalidated(this.observable);
      }

      protected void fireValueChangedEvent(SetChangeListener.Change var1) {
         this.listener.invalidated(this.observable);
      }

      // $FF: synthetic method
      SingleInvalidation(ObservableSetValue var1, InvalidationListener var2, Object var3) {
         this(var1, var2);
      }
   }
}
