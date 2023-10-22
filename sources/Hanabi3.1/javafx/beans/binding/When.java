package javafx.beans.binding;

import com.sun.javafx.binding.DoubleConstant;
import com.sun.javafx.binding.FloatConstant;
import com.sun.javafx.binding.IntegerConstant;
import com.sun.javafx.binding.Logging;
import com.sun.javafx.binding.LongConstant;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.NamedArg;
import javafx.beans.Observable;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableFloatValue;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class When {
   private final ObservableBooleanValue condition;

   public When(@NamedArg("condition") ObservableBooleanValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Condition must be specified.");
      } else {
         this.condition = var1;
      }
   }

   private static NumberBinding createNumberCondition(final ObservableBooleanValue var0, final ObservableNumberValue var1, final ObservableNumberValue var2) {
      if (!(var1 instanceof ObservableDoubleValue) && !(var2 instanceof ObservableDoubleValue)) {
         if (!(var1 instanceof ObservableFloatValue) && !(var2 instanceof ObservableFloatValue)) {
            return (NumberBinding)(!(var1 instanceof ObservableLongValue) && !(var2 instanceof ObservableLongValue) ? new IntegerBinding() {
               final InvalidationListener observer = new WhenListener(this, var0, var1, var2);

               {
                  var0.addListener(this.observer);
                  var1.addListener(this.observer);
                  var2.addListener(this.observer);
               }

               public void dispose() {
                  var0.removeListener(this.observer);
                  var1.removeListener(this.observer);
                  var2.removeListener(this.observer);
               }

               protected int computeValue() {
                  boolean var1x = var0.get();
                  Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1x});
                  return var1x ? var1.intValue() : var2.intValue();
               }

               public ObservableList getDependencies() {
                  return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])(var0, var1, var2)));
               }
            } : new LongBinding() {
               final InvalidationListener observer = new WhenListener(this, var0, var1, var2);

               {
                  var0.addListener(this.observer);
                  var1.addListener(this.observer);
                  var2.addListener(this.observer);
               }

               public void dispose() {
                  var0.removeListener(this.observer);
                  var1.removeListener(this.observer);
                  var2.removeListener(this.observer);
               }

               protected long computeValue() {
                  boolean var1x = var0.get();
                  Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1x});
                  return var1x ? var1.longValue() : var2.longValue();
               }

               public ObservableList getDependencies() {
                  return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])(var0, var1, var2)));
               }
            });
         } else {
            return new FloatBinding() {
               final InvalidationListener observer = new WhenListener(this, var0, var1, var2);

               {
                  var0.addListener(this.observer);
                  var1.addListener(this.observer);
                  var2.addListener(this.observer);
               }

               public void dispose() {
                  var0.removeListener(this.observer);
                  var1.removeListener(this.observer);
                  var2.removeListener(this.observer);
               }

               protected float computeValue() {
                  boolean var1x = var0.get();
                  Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1x});
                  return var1x ? var1.floatValue() : var2.floatValue();
               }

               public ObservableList getDependencies() {
                  return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])(var0, var1, var2)));
               }
            };
         }
      } else {
         return new DoubleBinding() {
            final InvalidationListener observer = new WhenListener(this, var0, var1, var2);

            {
               var0.addListener(this.observer);
               var1.addListener(this.observer);
               var2.addListener(this.observer);
            }

            public void dispose() {
               var0.removeListener(this.observer);
               var1.removeListener(this.observer);
               var2.removeListener(this.observer);
            }

            protected double computeValue() {
               boolean var1x = var0.get();
               Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1x});
               return var1x ? var1.doubleValue() : var2.doubleValue();
            }

            public ObservableList getDependencies() {
               return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList((Object[])(var0, var1, var2)));
            }
         };
      }
   }

   public NumberConditionBuilder then(ObservableNumberValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Value needs to be specified");
      } else {
         return new NumberConditionBuilder(var1);
      }
   }

   public NumberConditionBuilder then(double var1) {
      return new NumberConditionBuilder(DoubleConstant.valueOf(var1));
   }

   public NumberConditionBuilder then(float var1) {
      return new NumberConditionBuilder(FloatConstant.valueOf(var1));
   }

   public NumberConditionBuilder then(long var1) {
      return new NumberConditionBuilder(LongConstant.valueOf(var1));
   }

   public NumberConditionBuilder then(int var1) {
      return new NumberConditionBuilder(IntegerConstant.valueOf(var1));
   }

   public BooleanConditionBuilder then(ObservableBooleanValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Value needs to be specified");
      } else {
         return new BooleanConditionBuilder(var1);
      }
   }

   public BooleanConditionBuilder then(boolean var1) {
      return new BooleanConditionBuilder(var1);
   }

   public StringConditionBuilder then(ObservableStringValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Value needs to be specified");
      } else {
         return new StringConditionBuilder(var1);
      }
   }

   public StringConditionBuilder then(String var1) {
      return new StringConditionBuilder(var1);
   }

   public ObjectConditionBuilder then(ObservableObjectValue var1) {
      if (var1 == null) {
         throw new NullPointerException("Value needs to be specified");
      } else {
         return new ObjectConditionBuilder(var1);
      }
   }

   public ObjectConditionBuilder then(Object var1) {
      return new ObjectConditionBuilder(var1);
   }

   public class ObjectConditionBuilder {
      private ObservableObjectValue trueResult;
      private Object trueResultValue;

      private ObjectConditionBuilder(ObservableObjectValue var2) {
         this.trueResult = var2;
      }

      private ObjectConditionBuilder(Object var2) {
         this.trueResultValue = var2;
      }

      public ObjectBinding otherwise(ObservableObjectValue var1) {
         if (var1 == null) {
            throw new NullPointerException("Value needs to be specified");
         } else {
            return this.trueResult != null ? When.this.new ObjectCondition(this.trueResult, var1) : When.this.new ObjectCondition(this.trueResultValue, var1);
         }
      }

      public ObjectBinding otherwise(Object var1) {
         return this.trueResult != null ? When.this.new ObjectCondition(this.trueResult, var1) : When.this.new ObjectCondition(this.trueResultValue, var1);
      }

      // $FF: synthetic method
      ObjectConditionBuilder(ObservableObjectValue var2, Object var3) {
         this((ObservableObjectValue)var2);
      }

      // $FF: synthetic method
      ObjectConditionBuilder(Object var2, Object var3) {
         this((Object)var2);
      }
   }

   private class ObjectCondition extends ObjectBinding {
      private final ObservableObjectValue trueResult;
      private final Object trueResultValue;
      private final ObservableObjectValue falseResult;
      private final Object falseResultValue;
      private final InvalidationListener observer;

      private ObjectCondition(ObservableObjectValue var2, ObservableObjectValue var3) {
         this.trueResult = var2;
         this.trueResultValue = null;
         this.falseResult = var3;
         this.falseResultValue = null;
         this.observer = new WhenListener(this, When.this.condition, var2, var3);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private ObjectCondition(Object var2, ObservableObjectValue var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = var3;
         this.falseResultValue = null;
         this.observer = new WhenListener(this, When.this.condition, (ObservableValue)null, var3);
         When.this.condition.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private ObjectCondition(ObservableObjectValue var2, Object var3) {
         this.trueResult = var2;
         this.trueResultValue = null;
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = new WhenListener(this, When.this.condition, var2, (ObservableValue)null);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
      }

      private ObjectCondition(Object var2, Object var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = null;
         super.bind(When.this.condition);
      }

      protected Object computeValue() {
         boolean var1 = When.this.condition.get();
         Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1});
         return var1 ? (this.trueResult != null ? this.trueResult.get() : this.trueResultValue) : (this.falseResult != null ? this.falseResult.get() : this.falseResultValue);
      }

      public void dispose() {
         if (this.observer == null) {
            super.unbind(When.this.condition);
         } else {
            When.this.condition.removeListener(this.observer);
            if (this.trueResult != null) {
               this.trueResult.removeListener(this.observer);
            }

            if (this.falseResult != null) {
               this.falseResult.removeListener(this.observer);
            }
         }

      }

      public ObservableList getDependencies() {
         assert When.this.condition != null;

         ObservableList var1 = FXCollections.observableArrayList((Object[])(When.this.condition));
         if (this.trueResult != null) {
            var1.add(this.trueResult);
         }

         if (this.falseResult != null) {
            var1.add(this.falseResult);
         }

         return FXCollections.unmodifiableObservableList(var1);
      }

      // $FF: synthetic method
      ObjectCondition(ObservableObjectValue var2, ObservableObjectValue var3, Object var4) {
         this((ObservableObjectValue)var2, (ObservableObjectValue)var3);
      }

      // $FF: synthetic method
      ObjectCondition(Object var2, ObservableObjectValue var3, Object var4) {
         this((Object)var2, (ObservableObjectValue)var3);
      }

      // $FF: synthetic method
      ObjectCondition(ObservableObjectValue var2, Object var3, Object var4) {
         this((ObservableObjectValue)var2, (Object)var3);
      }

      // $FF: synthetic method
      ObjectCondition(Object var2, Object var3, Object var4) {
         this((Object)var2, (Object)var3);
      }
   }

   public class StringConditionBuilder {
      private ObservableStringValue trueResult;
      private String trueResultValue;

      private StringConditionBuilder(ObservableStringValue var2) {
         this.trueResult = var2;
      }

      private StringConditionBuilder(String var2) {
         this.trueResultValue = var2;
      }

      public StringBinding otherwise(ObservableStringValue var1) {
         return this.trueResult != null ? When.this.new StringCondition(this.trueResult, var1) : When.this.new StringCondition(this.trueResultValue, var1);
      }

      public StringBinding otherwise(String var1) {
         return this.trueResult != null ? When.this.new StringCondition(this.trueResult, var1) : When.this.new StringCondition(this.trueResultValue, var1);
      }

      // $FF: synthetic method
      StringConditionBuilder(ObservableStringValue var2, Object var3) {
         this((ObservableStringValue)var2);
      }

      // $FF: synthetic method
      StringConditionBuilder(String var2, Object var3) {
         this((String)var2);
      }
   }

   private class StringCondition extends StringBinding {
      private final ObservableStringValue trueResult;
      private final String trueResultValue;
      private final ObservableStringValue falseResult;
      private final String falseResultValue;
      private final InvalidationListener observer;

      private StringCondition(ObservableStringValue var2, ObservableStringValue var3) {
         this.trueResult = var2;
         this.trueResultValue = "";
         this.falseResult = var3;
         this.falseResultValue = "";
         this.observer = new WhenListener(this, When.this.condition, var2, var3);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private StringCondition(String var2, ObservableStringValue var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = var3;
         this.falseResultValue = "";
         this.observer = new WhenListener(this, When.this.condition, (ObservableValue)null, var3);
         When.this.condition.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private StringCondition(ObservableStringValue var2, String var3) {
         this.trueResult = var2;
         this.trueResultValue = "";
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = new WhenListener(this, When.this.condition, var2, (ObservableValue)null);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
      }

      private StringCondition(String var2, String var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = null;
         super.bind(When.this.condition);
      }

      protected String computeValue() {
         boolean var1 = When.this.condition.get();
         Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1});
         return var1 ? (this.trueResult != null ? (String)this.trueResult.get() : this.trueResultValue) : (this.falseResult != null ? (String)this.falseResult.get() : this.falseResultValue);
      }

      public void dispose() {
         if (this.observer == null) {
            super.unbind(When.this.condition);
         } else {
            When.this.condition.removeListener(this.observer);
            if (this.trueResult != null) {
               this.trueResult.removeListener(this.observer);
            }

            if (this.falseResult != null) {
               this.falseResult.removeListener(this.observer);
            }
         }

      }

      public ObservableList getDependencies() {
         assert When.this.condition != null;

         ObservableList var1 = FXCollections.observableArrayList((Object[])(When.this.condition));
         if (this.trueResult != null) {
            var1.add(this.trueResult);
         }

         if (this.falseResult != null) {
            var1.add(this.falseResult);
         }

         return FXCollections.unmodifiableObservableList(var1);
      }

      // $FF: synthetic method
      StringCondition(ObservableStringValue var2, ObservableStringValue var3, Object var4) {
         this((ObservableStringValue)var2, (ObservableStringValue)var3);
      }

      // $FF: synthetic method
      StringCondition(String var2, ObservableStringValue var3, Object var4) {
         this((String)var2, (ObservableStringValue)var3);
      }

      // $FF: synthetic method
      StringCondition(ObservableStringValue var2, String var3, Object var4) {
         this((ObservableStringValue)var2, (String)var3);
      }

      // $FF: synthetic method
      StringCondition(String var2, String var3, Object var4) {
         this((String)var2, (String)var3);
      }
   }

   public class BooleanConditionBuilder {
      private ObservableBooleanValue trueResult;
      private boolean trueResultValue;

      private BooleanConditionBuilder(ObservableBooleanValue var2) {
         this.trueResult = var2;
      }

      private BooleanConditionBuilder(boolean var2) {
         this.trueResultValue = var2;
      }

      public BooleanBinding otherwise(ObservableBooleanValue var1) {
         if (var1 == null) {
            throw new NullPointerException("Value needs to be specified");
         } else {
            return this.trueResult != null ? When.this.new BooleanCondition(this.trueResult, var1) : When.this.new BooleanCondition(this.trueResultValue, var1);
         }
      }

      public BooleanBinding otherwise(boolean var1) {
         return this.trueResult != null ? When.this.new BooleanCondition(this.trueResult, var1) : When.this.new BooleanCondition(this.trueResultValue, var1);
      }

      // $FF: synthetic method
      BooleanConditionBuilder(ObservableBooleanValue var2, Object var3) {
         this(var2);
      }

      // $FF: synthetic method
      BooleanConditionBuilder(boolean var2, Object var3) {
         this(var2);
      }
   }

   private class BooleanCondition extends BooleanBinding {
      private final ObservableBooleanValue trueResult;
      private final boolean trueResultValue;
      private final ObservableBooleanValue falseResult;
      private final boolean falseResultValue;
      private final InvalidationListener observer;

      private BooleanCondition(ObservableBooleanValue var2, ObservableBooleanValue var3) {
         this.trueResult = var2;
         this.trueResultValue = false;
         this.falseResult = var3;
         this.falseResultValue = false;
         this.observer = new WhenListener(this, When.this.condition, var2, var3);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private BooleanCondition(boolean var2, ObservableBooleanValue var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = var3;
         this.falseResultValue = false;
         this.observer = new WhenListener(this, When.this.condition, (ObservableValue)null, var3);
         When.this.condition.addListener(this.observer);
         var3.addListener(this.observer);
      }

      private BooleanCondition(ObservableBooleanValue var2, boolean var3) {
         this.trueResult = var2;
         this.trueResultValue = false;
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = new WhenListener(this, When.this.condition, var2, (ObservableValue)null);
         When.this.condition.addListener(this.observer);
         var2.addListener(this.observer);
      }

      private BooleanCondition(boolean var2, boolean var3) {
         this.trueResult = null;
         this.trueResultValue = var2;
         this.falseResult = null;
         this.falseResultValue = var3;
         this.observer = null;
         super.bind(When.this.condition);
      }

      protected boolean computeValue() {
         boolean var1 = When.this.condition.get();
         Logging.getLogger().finest("Condition of ternary binding expression was evaluated: {0}", new Object[]{var1});
         return var1 ? (this.trueResult != null ? this.trueResult.get() : this.trueResultValue) : (this.falseResult != null ? this.falseResult.get() : this.falseResultValue);
      }

      public void dispose() {
         if (this.observer == null) {
            super.unbind(When.this.condition);
         } else {
            When.this.condition.removeListener(this.observer);
            if (this.trueResult != null) {
               this.trueResult.removeListener(this.observer);
            }

            if (this.falseResult != null) {
               this.falseResult.removeListener(this.observer);
            }
         }

      }

      public ObservableList getDependencies() {
         assert When.this.condition != null;

         ObservableList var1 = FXCollections.observableArrayList((Object[])(When.this.condition));
         if (this.trueResult != null) {
            var1.add(this.trueResult);
         }

         if (this.falseResult != null) {
            var1.add(this.falseResult);
         }

         return FXCollections.unmodifiableObservableList(var1);
      }

      // $FF: synthetic method
      BooleanCondition(ObservableBooleanValue var2, ObservableBooleanValue var3, Object var4) {
         this(var2, var3);
      }

      // $FF: synthetic method
      BooleanCondition(boolean var2, ObservableBooleanValue var3, Object var4) {
         this(var2, var3);
      }

      // $FF: synthetic method
      BooleanCondition(ObservableBooleanValue var2, boolean var3, Object var4) {
         this(var2, var3);
      }

      // $FF: synthetic method
      BooleanCondition(boolean var2, boolean var3, Object var4) {
         this(var2, var3);
      }
   }

   public class NumberConditionBuilder {
      private ObservableNumberValue thenValue;

      private NumberConditionBuilder(ObservableNumberValue var2) {
         this.thenValue = var2;
      }

      public NumberBinding otherwise(ObservableNumberValue var1) {
         if (var1 == null) {
            throw new NullPointerException("Value needs to be specified");
         } else {
            return When.createNumberCondition(When.this.condition, this.thenValue, var1);
         }
      }

      public DoubleBinding otherwise(double var1) {
         return (DoubleBinding)this.otherwise(DoubleConstant.valueOf(var1));
      }

      public NumberBinding otherwise(float var1) {
         return this.otherwise(FloatConstant.valueOf(var1));
      }

      public NumberBinding otherwise(long var1) {
         return this.otherwise(LongConstant.valueOf(var1));
      }

      public NumberBinding otherwise(int var1) {
         return this.otherwise(IntegerConstant.valueOf(var1));
      }

      // $FF: synthetic method
      NumberConditionBuilder(ObservableNumberValue var2, Object var3) {
         this(var2);
      }
   }

   private static class WhenListener implements InvalidationListener {
      private final ObservableBooleanValue condition;
      private final ObservableValue thenValue;
      private final ObservableValue otherwiseValue;
      private final WeakReference ref;

      private WhenListener(Binding var1, ObservableBooleanValue var2, ObservableValue var3, ObservableValue var4) {
         this.ref = new WeakReference(var1);
         this.condition = var2;
         this.thenValue = var3;
         this.otherwiseValue = var4;
      }

      public void invalidated(Observable var1) {
         Binding var2 = (Binding)this.ref.get();
         if (var2 == null) {
            this.condition.removeListener(this);
            if (this.thenValue != null) {
               this.thenValue.removeListener(this);
            }

            if (this.otherwiseValue != null) {
               this.otherwiseValue.removeListener(this);
            }
         } else if (this.condition.equals(var1) || var2.isValid() && this.condition.get() == var1.equals(this.thenValue)) {
            var2.invalidate();
         }

      }

      // $FF: synthetic method
      WhenListener(Binding var1, ObservableBooleanValue var2, ObservableValue var3, ObservableValue var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }
}
