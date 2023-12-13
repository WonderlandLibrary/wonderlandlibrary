package com.sun.javafx.binding;

import com.sun.javafx.property.JavaBeanAccessHelper;
import com.sun.javafx.property.PropertyReference;
import java.util.Arrays;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.FloatBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public class SelectBinding {
   private SelectBinding() {
   }

   private static class SelectBindingHelper implements InvalidationListener {
      private final Binding binding;
      private final String[] propertyNames;
      private final ObservableValue[] properties;
      private final PropertyReference[] propRefs;
      private final WeakInvalidationListener observer;
      private ObservableList dependencies;

      private SelectBindingHelper(Binding var1, ObservableValue var2, String... var3) {
         if (var2 == null) {
            throw new NullPointerException("Must specify the root");
         } else {
            if (var3 == null) {
               var3 = new String[0];
            }

            this.binding = var1;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               if (var3[var5] == null) {
                  throw new NullPointerException("all steps must be specified");
               }
            }

            this.observer = new WeakInvalidationListener(this);
            this.propertyNames = new String[var4];
            System.arraycopy(var3, 0, this.propertyNames, 0, var4);
            this.propRefs = new PropertyReference[var4];
            this.properties = new ObservableValue[var4 + 1];
            this.properties[0] = var2;
            this.properties[0].addListener(this.observer);
         }
      }

      private static ObservableValue checkAndCreateFirstStep(Object var0, String[] var1) {
         if (var0 != null && var1 != null && var1[0] != null) {
            try {
               return JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(var0, var1[0]);
            } catch (NoSuchMethodException var3) {
               throw new IllegalArgumentException("The first property '" + var1[0] + "' doesn't exist");
            }
         } else {
            throw new NullPointerException("Must specify the root and the first property");
         }
      }

      private SelectBindingHelper(Binding var1, Object var2, String... var3) {
         this(var1, checkAndCreateFirstStep(var2, var3), (String[])Arrays.copyOfRange(var3, 1, var3.length));
      }

      public void invalidated(Observable var1) {
         this.binding.invalidate();
      }

      public ObservableValue getObservableValue() {
         int var1 = this.properties.length;

         for(int var2 = 0; var2 < var1 - 1; ++var2) {
            Object var3 = this.properties[var2].getValue();

            try {
               if (this.propRefs[var2] == null || !var3.getClass().equals(this.propRefs[var2].getContainingClass())) {
                  this.propRefs[var2] = new PropertyReference(var3.getClass(), this.propertyNames[var2]);
               }

               if (this.propRefs[var2].hasProperty()) {
                  this.properties[var2 + 1] = this.propRefs[var2].getProperty(var3);
               } else {
                  this.properties[var2 + 1] = JavaBeanAccessHelper.createReadOnlyJavaBeanProperty(var3, this.propRefs[var2].getName());
               }
            } catch (NoSuchMethodException var6) {
               Logging.getLogger().warning("Exception while evaluating select-binding " + this.stepsToString(), var6);
               this.updateDependencies();
               return null;
            } catch (RuntimeException var7) {
               PlatformLogger var5 = Logging.getLogger();
               if (var5.isLoggable(Level.WARNING)) {
                  Logging.getLogger().warning("Exception while evaluating select-binding " + this.stepsToString());
                  if (var7 instanceof IllegalStateException) {
                     var5.warning("Property '" + this.propertyNames[var2] + "' does not exist in " + var3.getClass(), var7);
                  } else if (var7 instanceof NullPointerException) {
                     var5.fine("Property '" + this.propertyNames[var2] + "' in " + this.properties[var2] + " is null", var7);
                  } else {
                     Logging.getLogger().warning("", var7);
                  }
               }

               this.updateDependencies();
               return null;
            }

            this.properties[var2 + 1].addListener(this.observer);
         }

         this.updateDependencies();
         ObservableValue var8 = this.properties[var1 - 1];
         if (var8 == null) {
            Logging.getLogger().fine("Property '" + this.propertyNames[var1 - 1] + "' in " + this.properties[var1 - 1] + " is null", new NullPointerException());
         }

         return var8;
      }

      private String stepsToString() {
         return Arrays.toString(this.propertyNames);
      }

      private void unregisterListener() {
         int var1 = this.properties.length;

         for(int var2 = 1; var2 < var1 && this.properties[var2] != null; ++var2) {
            this.properties[var2].removeListener(this.observer);
            this.properties[var2] = null;
         }

         this.updateDependencies();
      }

      private void updateDependencies() {
         if (this.dependencies != null) {
            this.dependencies.clear();
            int var1 = this.properties.length;

            for(int var2 = 0; var2 < var1 && this.properties[var2] != null; ++var2) {
               this.dependencies.add(this.properties[var2]);
            }
         }

      }

      public ObservableList getDependencies() {
         if (this.dependencies == null) {
            this.dependencies = FXCollections.observableArrayList();
            this.updateDependencies();
         }

         return FXCollections.unmodifiableObservableList(this.dependencies);
      }

      // $FF: synthetic method
      SelectBindingHelper(Binding var1, ObservableValue var2, String[] var3, Object var4) {
         this(var1, var2, var3);
      }

      // $FF: synthetic method
      SelectBindingHelper(Binding var1, Object var2, String[] var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   public static class AsString extends StringBinding {
      private static final String DEFAULT_VALUE = null;
      private final SelectBindingHelper helper;

      public AsString(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsString(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected String computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return DEFAULT_VALUE;
         } else {
            try {
               return var1.getValue().toString();
            } catch (RuntimeException var3) {
               Logging.getLogger().warning("Exception while evaluating select-binding", var3);
               return DEFAULT_VALUE;
            }
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsLong extends LongBinding {
      private static final long DEFAULT_VALUE = 0L;
      private final SelectBindingHelper helper;

      public AsLong(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsLong(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected long computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return 0L;
         } else if (var1 instanceof ObservableNumberValue) {
            return ((ObservableNumberValue)var1).longValue();
         } else {
            try {
               return ((Number)var1.getValue()).longValue();
            } catch (NullPointerException var3) {
               Logging.getLogger().fine("Value of select binding is null, returning default value", var3);
            } catch (ClassCastException var4) {
               Logging.getLogger().warning("Exception while evaluating select-binding", var4);
            }

            return 0L;
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsInteger extends IntegerBinding {
      private static final int DEFAULT_VALUE = 0;
      private final SelectBindingHelper helper;

      public AsInteger(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsInteger(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected int computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return 0;
         } else if (var1 instanceof ObservableNumberValue) {
            return ((ObservableNumberValue)var1).intValue();
         } else {
            try {
               return ((Number)var1.getValue()).intValue();
            } catch (NullPointerException var3) {
               Logging.getLogger().fine("Value of select binding is null, returning default value", var3);
            } catch (ClassCastException var4) {
               Logging.getLogger().warning("Exception while evaluating select-binding", var4);
            }

            return 0;
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsFloat extends FloatBinding {
      private static final float DEFAULT_VALUE = 0.0F;
      private final SelectBindingHelper helper;

      public AsFloat(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsFloat(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected float computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return 0.0F;
         } else if (var1 instanceof ObservableNumberValue) {
            return ((ObservableNumberValue)var1).floatValue();
         } else {
            try {
               return ((Number)var1.getValue()).floatValue();
            } catch (NullPointerException var3) {
               Logging.getLogger().fine("Value of select binding is null, returning default value", var3);
            } catch (ClassCastException var4) {
               Logging.getLogger().warning("Exception while evaluating select-binding", var4);
            }

            return 0.0F;
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsDouble extends DoubleBinding {
      private static final double DEFAULT_VALUE = 0.0;
      private final SelectBindingHelper helper;

      public AsDouble(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsDouble(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected double computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return 0.0;
         } else if (var1 instanceof ObservableNumberValue) {
            return ((ObservableNumberValue)var1).doubleValue();
         } else {
            try {
               return ((Number)var1.getValue()).doubleValue();
            } catch (NullPointerException var3) {
               Logging.getLogger().fine("Value of select binding is null, returning default value", var3);
            } catch (ClassCastException var4) {
               Logging.getLogger().warning("Exception while evaluating select-binding", var4);
            }

            return 0.0;
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsBoolean extends BooleanBinding {
      private static final boolean DEFAULT_VALUE = false;
      private final SelectBindingHelper helper;

      public AsBoolean(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsBoolean(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected boolean computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return false;
         } else if (var1 instanceof ObservableBooleanValue) {
            return ((ObservableBooleanValue)var1).get();
         } else {
            try {
               return (Boolean)var1.getValue();
            } catch (NullPointerException var3) {
               Logging.getLogger().fine("Value of select binding is null, returning default value", var3);
            } catch (ClassCastException var4) {
               Logging.getLogger().warning("Value of select-binding has wrong type, returning default value.", var4);
            }

            return false;
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }

   public static class AsObject extends ObjectBinding {
      private final SelectBindingHelper helper;

      public AsObject(ObservableValue var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public AsObject(Object var1, String... var2) {
         this.helper = new SelectBindingHelper(this, var1, var2);
      }

      public void dispose() {
         this.helper.unregisterListener();
      }

      protected void onInvalidating() {
         this.helper.unregisterListener();
      }

      protected Object computeValue() {
         ObservableValue var1 = this.helper.getObservableValue();
         if (var1 == null) {
            return null;
         } else {
            try {
               return var1.getValue();
            } catch (ClassCastException var3) {
               Logging.getLogger().warning("Value of select-binding has wrong type, returning null.", var3);
               return null;
            }
         }
      }

      public ObservableList getDependencies() {
         return this.helper.getDependencies();
      }
   }
}
