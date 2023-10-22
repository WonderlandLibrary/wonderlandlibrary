package com.sun.javafx.binding;

import java.lang.ref.WeakReference;
import java.text.Format;
import java.text.ParseException;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

public abstract class BidirectionalBinding implements ChangeListener, WeakListener {
   private final int cachedHashCode;

   private static void checkParameters(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         if (var0 == var1) {
            throw new IllegalArgumentException("Cannot bind property to itself");
         }
      } else {
         throw new NullPointerException("Both properties must be specified.");
      }
   }

   public static BidirectionalBinding bind(Property var0, Property var1) {
      checkParameters(var0, var1);
      Object var2 = var0 instanceof DoubleProperty && var1 instanceof DoubleProperty ? new BidirectionalDoubleBinding((DoubleProperty)var0, (DoubleProperty)var1) : (var0 instanceof FloatProperty && var1 instanceof FloatProperty ? new BidirectionalFloatBinding((FloatProperty)var0, (FloatProperty)var1) : (var0 instanceof IntegerProperty && var1 instanceof IntegerProperty ? new BidirectionalIntegerBinding((IntegerProperty)var0, (IntegerProperty)var1) : (var0 instanceof LongProperty && var1 instanceof LongProperty ? new BidirectionalLongBinding((LongProperty)var0, (LongProperty)var1) : (var0 instanceof BooleanProperty && var1 instanceof BooleanProperty ? new BidirectionalBooleanBinding((BooleanProperty)var0, (BooleanProperty)var1) : new TypedGenericBidirectionalBinding(var0, var1)))));
      var0.setValue(var1.getValue());
      var0.addListener((ChangeListener)var2);
      var1.addListener((ChangeListener)var2);
      return (BidirectionalBinding)var2;
   }

   public static Object bind(Property var0, Property var1, Format var2) {
      checkParameters(var0, var1);
      if (var2 == null) {
         throw new NullPointerException("Format cannot be null");
      } else {
         StringFormatBidirectionalBinding var3 = new StringFormatBidirectionalBinding(var0, var1, var2);
         var0.setValue(var2.format(var1.getValue()));
         var0.addListener(var3);
         var1.addListener(var3);
         return var3;
      }
   }

   public static Object bind(Property var0, Property var1, StringConverter var2) {
      checkParameters(var0, var1);
      if (var2 == null) {
         throw new NullPointerException("Converter cannot be null");
      } else {
         StringConverterBidirectionalBinding var3 = new StringConverterBidirectionalBinding(var0, var1, var2);
         var0.setValue(var2.toString(var1.getValue()));
         var0.addListener(var3);
         var1.addListener(var3);
         return var3;
      }
   }

   public static void unbind(Property var0, Property var1) {
      checkParameters(var0, var1);
      UntypedGenericBidirectionalBinding var2 = new UntypedGenericBidirectionalBinding(var0, var1);
      var0.removeListener(var2);
      var1.removeListener(var2);
   }

   public static void unbind(Object var0, Object var1) {
      checkParameters(var0, var1);
      UntypedGenericBidirectionalBinding var2 = new UntypedGenericBidirectionalBinding(var0, var1);
      if (var0 instanceof ObservableValue) {
         ((ObservableValue)var0).removeListener(var2);
      }

      if (var1 instanceof Observable) {
         ((ObservableValue)var1).removeListener(var2);
      }

   }

   public static BidirectionalBinding bindNumber(Property var0, IntegerProperty var1) {
      return bindNumber((Property)var0, (Property)var1);
   }

   public static BidirectionalBinding bindNumber(Property var0, LongProperty var1) {
      return bindNumber((Property)var0, (Property)var1);
   }

   public static BidirectionalBinding bindNumber(Property var0, FloatProperty var1) {
      return bindNumber((Property)var0, (Property)var1);
   }

   public static BidirectionalBinding bindNumber(Property var0, DoubleProperty var1) {
      return bindNumber((Property)var0, (Property)var1);
   }

   public static BidirectionalBinding bindNumber(IntegerProperty var0, Property var1) {
      return bindNumberObject(var0, var1);
   }

   public static BidirectionalBinding bindNumber(LongProperty var0, Property var1) {
      return bindNumberObject(var0, var1);
   }

   public static BidirectionalBinding bindNumber(FloatProperty var0, Property var1) {
      return bindNumberObject(var0, var1);
   }

   public static BidirectionalBinding bindNumber(DoubleProperty var0, Property var1) {
      return bindNumberObject(var0, var1);
   }

   private static BidirectionalBinding bindNumberObject(Property var0, Property var1) {
      checkParameters(var0, var1);
      TypedNumberBidirectionalBinding var2 = new TypedNumberBidirectionalBinding(var1, var0);
      var0.setValue(var1.getValue());
      var0.addListener(var2);
      var1.addListener(var2);
      return var2;
   }

   private static BidirectionalBinding bindNumber(Property var0, Property var1) {
      checkParameters(var0, var1);
      TypedNumberBidirectionalBinding var2 = new TypedNumberBidirectionalBinding(var0, var1);
      var0.setValue((Number)var1.getValue());
      var0.addListener(var2);
      var1.addListener(var2);
      return var2;
   }

   public static void unbindNumber(Property var0, Property var1) {
      checkParameters(var0, var1);
      UntypedGenericBidirectionalBinding var2 = new UntypedGenericBidirectionalBinding(var0, var1);
      if (var0 instanceof ObservableValue) {
         var0.removeListener(var2);
      }

      if (var1 instanceof Observable) {
         var1.removeListener(var2);
      }

   }

   private BidirectionalBinding(Object var1, Object var2) {
      this.cachedHashCode = var1.hashCode() * var2.hashCode();
   }

   protected abstract Object getProperty1();

   protected abstract Object getProperty2();

   public int hashCode() {
      return this.cachedHashCode;
   }

   public boolean wasGarbageCollected() {
      return this.getProperty1() == null || this.getProperty2() == null;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         Object var2 = this.getProperty1();
         Object var3 = this.getProperty2();
         if (var2 != null && var3 != null) {
            if (var1 instanceof BidirectionalBinding) {
               BidirectionalBinding var4 = (BidirectionalBinding)var1;
               Object var5 = var4.getProperty1();
               Object var6 = var4.getProperty2();
               if (var5 == null || var6 == null) {
                  return false;
               }

               if (var2 == var5 && var3 == var6) {
                  return true;
               }

               if (var2 == var6 && var3 == var5) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      }
   }

   // $FF: synthetic method
   BidirectionalBinding(Object var1, Object var2, Object var3) {
      this(var1, var2);
   }

   private static class StringConverterBidirectionalBinding extends StringConversionBidirectionalBinding {
      private final StringConverter converter;

      public StringConverterBidirectionalBinding(Property var1, Property var2, StringConverter var3) {
         super(var1, var2);
         this.converter = var3;
      }

      protected String toString(Object var1) {
         return this.converter.toString(var1);
      }

      protected Object fromString(String var1) throws ParseException {
         return this.converter.fromString(var1);
      }
   }

   private static class StringFormatBidirectionalBinding extends StringConversionBidirectionalBinding {
      private final Format format;

      public StringFormatBidirectionalBinding(Property var1, Property var2, Format var3) {
         super(var1, var2);
         this.format = var3;
      }

      protected String toString(Object var1) {
         return this.format.format(var1);
      }

      protected Object fromString(String var1) throws ParseException {
         return this.format.parseObject(var1);
      }
   }

   public abstract static class StringConversionBidirectionalBinding extends BidirectionalBinding {
      private final WeakReference stringPropertyRef;
      private final WeakReference otherPropertyRef;
      private boolean updating;

      public StringConversionBidirectionalBinding(Property var1, Property var2) {
         super(var1, var2, null);
         this.stringPropertyRef = new WeakReference(var1);
         this.otherPropertyRef = new WeakReference(var2);
      }

      protected abstract String toString(Object var1);

      protected abstract Object fromString(String var1) throws ParseException;

      protected Object getProperty1() {
         return this.stringPropertyRef.get();
      }

      protected Object getProperty2() {
         return this.otherPropertyRef.get();
      }

      public void changed(ObservableValue var1, Object var2, Object var3) {
         if (!this.updating) {
            Property var4 = (Property)this.stringPropertyRef.get();
            Property var5 = (Property)this.otherPropertyRef.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     try {
                        var5.setValue(this.fromString((String)var4.getValue()));
                     } catch (Exception var12) {
                        Logging.getLogger().warning("Exception while parsing String in bidirectional binding", var12);
                        var5.setValue((Object)null);
                     }
                  } else {
                     try {
                        var4.setValue(this.toString(var5.getValue()));
                     } catch (Exception var11) {
                        Logging.getLogger().warning("Exception while converting Object to String in bidirectional binding", var11);
                        var4.setValue("");
                     }
                  }
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }
   }

   private static class UntypedGenericBidirectionalBinding extends BidirectionalBinding {
      private final Object property1;
      private final Object property2;

      public UntypedGenericBidirectionalBinding(Object var1, Object var2) {
         super(var1, var2, null);
         this.property1 = var1;
         this.property2 = var2;
      }

      protected Object getProperty1() {
         return this.property1;
      }

      protected Object getProperty2() {
         return this.property2;
      }

      public void changed(ObservableValue var1, Object var2, Object var3) {
         throw new RuntimeException("Should not reach here");
      }
   }

   private static class TypedNumberBidirectionalBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private TypedNumberBidirectionalBinding(Property var1, Property var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (!this.updating) {
            Property var4 = (Property)this.propertyRef1.get();
            Property var5 = (Property)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.setValue(var3);
                  } else {
                     var4.setValue(var3);
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.setValue(var2);
                     } else {
                        var5.setValue(var2);
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      TypedNumberBidirectionalBinding(Property var1, Property var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class TypedGenericBidirectionalBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private TypedGenericBidirectionalBinding(Property var1, Property var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Object var2, Object var3) {
         if (!this.updating) {
            Property var4 = (Property)this.propertyRef1.get();
            Property var5 = (Property)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.setValue(var3);
                  } else {
                     var4.setValue(var3);
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.setValue(var2);
                     } else {
                        var5.setValue(var2);
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      TypedGenericBidirectionalBinding(Property var1, Property var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class BidirectionalLongBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private BidirectionalLongBinding(LongProperty var1, LongProperty var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (!this.updating) {
            LongProperty var4 = (LongProperty)this.propertyRef1.get();
            LongProperty var5 = (LongProperty)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.set(var3.longValue());
                  } else {
                     var4.set(var3.longValue());
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.set(var2.longValue());
                     } else {
                        var5.set(var2.longValue());
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      BidirectionalLongBinding(LongProperty var1, LongProperty var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class BidirectionalIntegerBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private BidirectionalIntegerBinding(IntegerProperty var1, IntegerProperty var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (!this.updating) {
            IntegerProperty var4 = (IntegerProperty)this.propertyRef1.get();
            IntegerProperty var5 = (IntegerProperty)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.set(var3.intValue());
                  } else {
                     var4.set(var3.intValue());
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.set(var2.intValue());
                     } else {
                        var5.set(var2.intValue());
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      BidirectionalIntegerBinding(IntegerProperty var1, IntegerProperty var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class BidirectionalFloatBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private BidirectionalFloatBinding(FloatProperty var1, FloatProperty var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (!this.updating) {
            FloatProperty var4 = (FloatProperty)this.propertyRef1.get();
            FloatProperty var5 = (FloatProperty)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.set(var3.floatValue());
                  } else {
                     var4.set(var3.floatValue());
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.set(var2.floatValue());
                     } else {
                        var5.set(var2.floatValue());
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      BidirectionalFloatBinding(FloatProperty var1, FloatProperty var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class BidirectionalDoubleBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private BidirectionalDoubleBinding(DoubleProperty var1, DoubleProperty var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Number var2, Number var3) {
         if (!this.updating) {
            DoubleProperty var4 = (DoubleProperty)this.propertyRef1.get();
            DoubleProperty var5 = (DoubleProperty)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.set(var3.doubleValue());
                  } else {
                     var4.set(var3.doubleValue());
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.set(var2.doubleValue());
                     } else {
                        var5.set(var2.doubleValue());
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      BidirectionalDoubleBinding(DoubleProperty var1, DoubleProperty var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class BidirectionalBooleanBinding extends BidirectionalBinding {
      private final WeakReference propertyRef1;
      private final WeakReference propertyRef2;
      private boolean updating;

      private BidirectionalBooleanBinding(BooleanProperty var1, BooleanProperty var2) {
         super(var1, var2, null);
         this.updating = false;
         this.propertyRef1 = new WeakReference(var1);
         this.propertyRef2 = new WeakReference(var2);
      }

      protected Property getProperty1() {
         return (Property)this.propertyRef1.get();
      }

      protected Property getProperty2() {
         return (Property)this.propertyRef2.get();
      }

      public void changed(ObservableValue var1, Boolean var2, Boolean var3) {
         if (!this.updating) {
            BooleanProperty var4 = (BooleanProperty)this.propertyRef1.get();
            BooleanProperty var5 = (BooleanProperty)this.propertyRef2.get();
            if (var4 != null && var5 != null) {
               try {
                  this.updating = true;
                  if (var4 == var1) {
                     var5.set(var3);
                  } else {
                     var4.set(var3);
                  }
               } catch (RuntimeException var13) {
                  try {
                     if (var4 == var1) {
                        var4.set(var2);
                     } else {
                        var5.set(var2);
                     }
                  } catch (Exception var12) {
                     var12.addSuppressed(var13);
                     unbind(var4, var5);
                     throw new RuntimeException("Bidirectional binding failed together with an attempt to restore the source property to the previous value. Removing the bidirectional binding from properties " + var4 + " and " + var5, var12);
                  }

                  throw new RuntimeException("Bidirectional binding failed, setting to the previous value", var13);
               } finally {
                  this.updating = false;
               }
            } else {
               if (var4 != null) {
                  var4.removeListener(this);
               }

               if (var5 != null) {
                  var5.removeListener(this);
               }
            }
         }

      }

      // $FF: synthetic method
      BidirectionalBooleanBinding(BooleanProperty var1, BooleanProperty var2, Object var3) {
         this(var1, var2);
      }
   }
}
