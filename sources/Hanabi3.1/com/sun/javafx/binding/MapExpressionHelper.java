package com.sun.javafx.binding;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableMapValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public abstract class MapExpressionHelper extends ExpressionHelperBase {
   protected final ObservableMapValue observable;

   public static MapExpressionHelper addListener(MapExpressionHelper var0, ObservableMapValue var1, InvalidationListener var2) {
      if (var1 != null && var2 != null) {
         var1.getValue();
         return (MapExpressionHelper)(var0 == null ? new SingleInvalidation(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static MapExpressionHelper removeListener(MapExpressionHelper var0, InvalidationListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static MapExpressionHelper addListener(MapExpressionHelper var0, ObservableMapValue var1, ChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (MapExpressionHelper)(var0 == null ? new SingleChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static MapExpressionHelper removeListener(MapExpressionHelper var0, ChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static MapExpressionHelper addListener(MapExpressionHelper var0, ObservableMapValue var1, MapChangeListener var2) {
      if (var1 != null && var2 != null) {
         return (MapExpressionHelper)(var0 == null ? new SingleMapChange(var1, var2) : var0.addListener(var2));
      } else {
         throw new NullPointerException();
      }
   }

   public static MapExpressionHelper removeListener(MapExpressionHelper var0, MapChangeListener var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         return var0 == null ? null : var0.removeListener(var1);
      }
   }

   public static void fireValueChangedEvent(MapExpressionHelper var0) {
      if (var0 != null) {
         var0.fireValueChangedEvent();
      }

   }

   public static void fireValueChangedEvent(MapExpressionHelper var0, MapChangeListener.Change var1) {
      if (var0 != null) {
         var0.fireValueChangedEvent(var1);
      }

   }

   protected MapExpressionHelper(ObservableMapValue var1) {
      this.observable = var1;
   }

   protected abstract MapExpressionHelper addListener(InvalidationListener var1);

   protected abstract MapExpressionHelper removeListener(InvalidationListener var1);

   protected abstract MapExpressionHelper addListener(ChangeListener var1);

   protected abstract MapExpressionHelper removeListener(ChangeListener var1);

   protected abstract MapExpressionHelper addListener(MapChangeListener var1);

   protected abstract MapExpressionHelper removeListener(MapChangeListener var1);

   protected abstract void fireValueChangedEvent();

   protected abstract void fireValueChangedEvent(MapChangeListener.Change var1);

   public static class SimpleChange extends MapChangeListener.Change {
      private Object key;
      private Object old;
      private Object added;
      private boolean removeOp;
      private boolean addOp;

      public SimpleChange(ObservableMap var1) {
         super(var1);
      }

      public SimpleChange(ObservableMap var1, MapChangeListener.Change var2) {
         super(var1);
         this.key = var2.getKey();
         this.old = var2.getValueRemoved();
         this.added = var2.getValueAdded();
         this.addOp = var2.wasAdded();
         this.removeOp = var2.wasRemoved();
      }

      public SimpleChange setRemoved(Object var1, Object var2) {
         this.key = var1;
         this.old = var2;
         this.added = null;
         this.addOp = false;
         this.removeOp = true;
         return this;
      }

      public SimpleChange setAdded(Object var1, Object var2) {
         this.key = var1;
         this.old = null;
         this.added = var2;
         this.addOp = true;
         this.removeOp = false;
         return this;
      }

      public SimpleChange setPut(Object var1, Object var2, Object var3) {
         this.key = var1;
         this.old = var2;
         this.added = var3;
         this.addOp = true;
         this.removeOp = true;
         return this;
      }

      public boolean wasAdded() {
         return this.addOp;
      }

      public boolean wasRemoved() {
         return this.removeOp;
      }

      public Object getKey() {
         return this.key;
      }

      public Object getValueAdded() {
         return this.added;
      }

      public Object getValueRemoved() {
         return this.old;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         if (this.addOp) {
            if (this.removeOp) {
               var1.append("replaced ").append(this.old).append("by ").append(this.added);
            } else {
               var1.append("added ").append(this.added);
            }
         } else {
            var1.append("removed ").append(this.old);
         }

         var1.append(" at key ").append(this.key);
         return var1.toString();
      }
   }

   private static class Generic extends MapExpressionHelper {
      private InvalidationListener[] invalidationListeners;
      private ChangeListener[] changeListeners;
      private MapChangeListener[] mapChangeListeners;
      private int invalidationSize;
      private int changeSize;
      private int mapChangeSize;
      private boolean locked;
      private ObservableMap currentValue;

      private Generic(ObservableMapValue var1, InvalidationListener var2, InvalidationListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2, var3};
         this.invalidationSize = 2;
      }

      private Generic(ObservableMapValue var1, ChangeListener var2, ChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2, var3};
         this.changeSize = 2;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      private Generic(ObservableMapValue var1, MapChangeListener var2, MapChangeListener var3) {
         super(var1);
         this.mapChangeListeners = new MapChangeListener[]{var2, var3};
         this.mapChangeSize = 2;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      private Generic(ObservableMapValue var1, InvalidationListener var2, ChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.changeListeners = new ChangeListener[]{var3};
         this.changeSize = 1;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      private Generic(ObservableMapValue var1, InvalidationListener var2, MapChangeListener var3) {
         super(var1);
         this.invalidationListeners = new InvalidationListener[]{var2};
         this.invalidationSize = 1;
         this.mapChangeListeners = new MapChangeListener[]{var3};
         this.mapChangeSize = 1;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      private Generic(ObservableMapValue var1, ChangeListener var2, MapChangeListener var3) {
         super(var1);
         this.changeListeners = new ChangeListener[]{var2};
         this.changeSize = 1;
         this.mapChangeListeners = new MapChangeListener[]{var3};
         this.mapChangeSize = 1;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      protected MapExpressionHelper addListener(InvalidationListener var1) {
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

      protected MapExpressionHelper removeListener(InvalidationListener var1) {
         if (this.invalidationListeners != null) {
            for(int var2 = 0; var2 < this.invalidationSize; ++var2) {
               if (var1.equals(this.invalidationListeners[var2])) {
                  if (this.invalidationSize == 1) {
                     if (this.changeSize == 1 && this.mapChangeSize == 0) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     if (this.changeSize == 0 && this.mapChangeSize == 1) {
                        return new SingleMapChange(this.observable, this.mapChangeListeners[0]);
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

      protected MapExpressionHelper addListener(ChangeListener var1) {
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
            this.currentValue = (ObservableMap)this.observable.getValue();
         }

         return this;
      }

      protected MapExpressionHelper removeListener(ChangeListener var1) {
         if (this.changeListeners != null) {
            for(int var2 = 0; var2 < this.changeSize; ++var2) {
               if (var1.equals(this.changeListeners[var2])) {
                  if (this.changeSize == 1) {
                     if (this.invalidationSize == 1 && this.mapChangeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.mapChangeSize == 1) {
                        return new SingleMapChange(this.observable, this.mapChangeListeners[0]);
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
                        this.changeListeners[--this.changeSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected MapExpressionHelper addListener(MapChangeListener var1) {
         if (this.mapChangeListeners == null) {
            this.mapChangeListeners = new MapChangeListener[]{var1};
            this.mapChangeSize = 1;
         } else {
            int var2 = this.mapChangeListeners.length;
            int var3;
            if (this.locked) {
               var3 = this.mapChangeSize < var2 ? var2 : var2 * 3 / 2 + 1;
               this.mapChangeListeners = (MapChangeListener[])Arrays.copyOf(this.mapChangeListeners, var3);
            } else if (this.mapChangeSize == var2) {
               this.mapChangeSize = trim(this.mapChangeSize, this.mapChangeListeners);
               if (this.mapChangeSize == var2) {
                  var3 = var2 * 3 / 2 + 1;
                  this.mapChangeListeners = (MapChangeListener[])Arrays.copyOf(this.mapChangeListeners, var3);
               }
            }

            this.mapChangeListeners[this.mapChangeSize++] = var1;
         }

         if (this.mapChangeSize == 1) {
            this.currentValue = (ObservableMap)this.observable.getValue();
         }

         return this;
      }

      protected MapExpressionHelper removeListener(MapChangeListener var1) {
         if (this.mapChangeListeners != null) {
            for(int var2 = 0; var2 < this.mapChangeSize; ++var2) {
               if (var1.equals(this.mapChangeListeners[var2])) {
                  if (this.mapChangeSize == 1) {
                     if (this.invalidationSize == 1 && this.changeSize == 0) {
                        return new SingleInvalidation(this.observable, this.invalidationListeners[0]);
                     }

                     if (this.invalidationSize == 0 && this.changeSize == 1) {
                        return new SingleChange(this.observable, this.changeListeners[0]);
                     }

                     this.mapChangeListeners = null;
                     this.mapChangeSize = 0;
                  } else {
                     int var3 = this.mapChangeSize - var2 - 1;
                     MapChangeListener[] var4 = this.mapChangeListeners;
                     if (this.locked) {
                        this.mapChangeListeners = new MapChangeListener[this.mapChangeListeners.length];
                        System.arraycopy(var4, 0, this.mapChangeListeners, 0, var2 + 1);
                     }

                     if (var3 > 0) {
                        System.arraycopy(var4, var2 + 1, this.mapChangeListeners, var2, var3);
                     }

                     --this.mapChangeSize;
                     if (!this.locked) {
                        this.mapChangeListeners[--this.mapChangeSize] = null;
                     }
                  }
                  break;
               }
            }
         }

         return this;
      }

      protected void fireValueChangedEvent() {
         if (this.changeSize == 0 && this.mapChangeSize == 0) {
            this.notifyListeners(this.currentValue, (SimpleChange)null);
         } else {
            ObservableMap var1 = this.currentValue;
            this.currentValue = (ObservableMap)this.observable.getValue();
            this.notifyListeners(var1, (SimpleChange)null);
         }

      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         SimpleChange var2 = this.mapChangeSize == 0 ? null : new SimpleChange(this.observable, var1);
         this.notifyListeners(this.currentValue, var2);
      }

      private void notifyListeners(ObservableMap var1, SimpleChange var2) {
         InvalidationListener[] var3 = this.invalidationListeners;
         int var4 = this.invalidationSize;
         ChangeListener[] var5 = this.changeListeners;
         int var6 = this.changeSize;
         MapChangeListener[] var7 = this.mapChangeListeners;
         int var8 = this.mapChangeSize;

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
                     Map.Entry var10;
                     int var11;
                     Iterator var18;
                     if (this.currentValue == null) {
                        var18 = var1.entrySet().iterator();

                        while(var18.hasNext()) {
                           var10 = (Map.Entry)var18.next();
                           var2.setRemoved(var10.getKey(), var10.getValue());

                           for(var11 = 0; var11 < var8; ++var11) {
                              var7[var11].onChanged(var2);
                           }
                        }
                     } else if (var1 == null) {
                        var18 = this.currentValue.entrySet().iterator();

                        while(var18.hasNext()) {
                           var10 = (Map.Entry)var18.next();
                           var2.setAdded(var10.getKey(), var10.getValue());

                           for(var11 = 0; var11 < var8; ++var11) {
                              var7[var11].onChanged(var2);
                           }
                        }
                     } else {
                        var18 = var1.entrySet().iterator();

                        while(true) {
                           Object var12;
                           Object var19;
                           Object var21;
                           label241:
                           while(true) {
                              while(var18.hasNext()) {
                                 var10 = (Map.Entry)var18.next();
                                 var19 = var10.getKey();
                                 var12 = var10.getValue();
                                 if (this.currentValue.containsKey(var19)) {
                                    var21 = this.currentValue.get(var19);
                                    if (var12 == null) {
                                       if (var21 != null) {
                                          break label241;
                                       }
                                    } else if (!var21.equals(var12)) {
                                       break label241;
                                    }
                                 } else {
                                    var2.setRemoved(var19, var12);

                                    for(int var13 = 0; var13 < var8; ++var13) {
                                       var7[var13].onChanged(var2);
                                    }
                                 }
                              }

                              var18 = this.currentValue.entrySet().iterator();

                              while(true) {
                                 do {
                                    if (!var18.hasNext()) {
                                       return;
                                    }

                                    var10 = (Map.Entry)var18.next();
                                    var19 = var10.getKey();
                                 } while(var1.containsKey(var19));

                                 var2.setAdded(var19, var10.getValue());

                                 for(int var20 = 0; var20 < var8; ++var20) {
                                    var7[var20].onChanged(var2);
                                 }
                              }
                           }

                           var2.setPut(var19, var12, var21);

                           for(int var14 = 0; var14 < var8; ++var14) {
                              var7[var14].onChanged(var2);
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
      Generic(ObservableMapValue var1, InvalidationListener var2, InvalidationListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableMapValue var1, InvalidationListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableMapValue var1, InvalidationListener var2, MapChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableMapValue var1, ChangeListener var2, ChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableMapValue var1, ChangeListener var2, MapChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      Generic(ObservableMapValue var1, MapChangeListener var2, MapChangeListener var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class SingleMapChange extends MapExpressionHelper {
      private final MapChangeListener listener;
      private ObservableMap currentValue;

      private SingleMapChange(ObservableMapValue var1, MapChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      protected MapExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected MapExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected MapExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected MapExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected MapExpressionHelper addListener(MapChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(MapChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected void fireValueChangedEvent() {
         ObservableMap var1 = this.currentValue;
         this.currentValue = (ObservableMap)this.observable.getValue();
         if (this.currentValue != var1) {
            SimpleChange var2 = new SimpleChange(this.observable);
            Iterator var3;
            Map.Entry var4;
            if (this.currentValue == null) {
               var3 = var1.entrySet().iterator();

               while(var3.hasNext()) {
                  var4 = (Map.Entry)var3.next();
                  this.listener.onChanged(var2.setRemoved(var4.getKey(), var4.getValue()));
               }
            } else if (var1 == null) {
               var3 = this.currentValue.entrySet().iterator();

               while(var3.hasNext()) {
                  var4 = (Map.Entry)var3.next();
                  this.listener.onChanged(var2.setAdded(var4.getKey(), var4.getValue()));
               }
            } else {
               var3 = var1.entrySet().iterator();

               label53:
               while(true) {
                  Object var5;
                  Object var6;
                  Object var7;
                  label51:
                  while(true) {
                     while(var3.hasNext()) {
                        var4 = (Map.Entry)var3.next();
                        var5 = var4.getKey();
                        var6 = var4.getValue();
                        if (this.currentValue.containsKey(var5)) {
                           var7 = this.currentValue.get(var5);
                           if (var6 == null) {
                              if (var7 != null) {
                                 break label51;
                              }
                           } else if (!var7.equals(var6)) {
                              break label51;
                           }
                        } else {
                           this.listener.onChanged(var2.setRemoved(var5, var6));
                        }
                     }

                     var3 = this.currentValue.entrySet().iterator();

                     while(var3.hasNext()) {
                        var4 = (Map.Entry)var3.next();
                        var5 = var4.getKey();
                        if (!var1.containsKey(var5)) {
                           this.listener.onChanged(var2.setAdded(var5, var4.getValue()));
                        }
                     }
                     break label53;
                  }

                  this.listener.onChanged(var2.setPut(var5, var6, var7));
               }
            }
         }

      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         this.listener.onChanged(new SimpleChange(this.observable, var1));
      }

      // $FF: synthetic method
      SingleMapChange(ObservableMapValue var1, MapChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleChange extends MapExpressionHelper {
      private final ChangeListener listener;
      private ObservableMap currentValue;

      private SingleChange(ObservableMapValue var1, ChangeListener var2) {
         super(var1);
         this.listener = var2;
         this.currentValue = (ObservableMap)var1.getValue();
      }

      protected MapExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, var1, this.listener);
      }

      protected MapExpressionHelper removeListener(InvalidationListener var1) {
         return this;
      }

      protected MapExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(ChangeListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected MapExpressionHelper addListener(MapChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(MapChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         ObservableMap var1 = this.currentValue;
         this.currentValue = (ObservableMap)this.observable.getValue();
         if (this.currentValue != var1) {
            this.listener.changed(this.observable, var1, this.currentValue);
         }

      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         this.listener.changed(this.observable, this.currentValue, this.currentValue);
      }

      // $FF: synthetic method
      SingleChange(ObservableMapValue var1, ChangeListener var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleInvalidation extends MapExpressionHelper {
      private final InvalidationListener listener;

      private SingleInvalidation(ObservableMapValue var1, InvalidationListener var2) {
         super(var1);
         this.listener = var2;
      }

      protected MapExpressionHelper addListener(InvalidationListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(InvalidationListener var1) {
         return var1.equals(this.listener) ? null : this;
      }

      protected MapExpressionHelper addListener(ChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(ChangeListener var1) {
         return this;
      }

      protected MapExpressionHelper addListener(MapChangeListener var1) {
         return new Generic(this.observable, this.listener, var1);
      }

      protected MapExpressionHelper removeListener(MapChangeListener var1) {
         return this;
      }

      protected void fireValueChangedEvent() {
         this.listener.invalidated(this.observable);
      }

      protected void fireValueChangedEvent(MapChangeListener.Change var1) {
         this.listener.invalidated(this.observable);
      }

      // $FF: synthetic method
      SingleInvalidation(ObservableMapValue var1, InvalidationListener var2, Object var3) {
         this(var1, var2);
      }
   }
}
