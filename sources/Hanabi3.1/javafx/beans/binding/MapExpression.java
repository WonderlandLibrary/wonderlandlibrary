package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public abstract class MapExpression implements ObservableMapValue {
   private static final ObservableMap EMPTY_MAP = new EmptyObservableMap();

   public ObservableMap getValue() {
      return (ObservableMap)this.get();
   }

   public static MapExpression mapExpression(final ObservableMapValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Map must be specified.");
      } else {
         return (MapExpression)(var0 instanceof MapExpression ? (MapExpression)var0 : new MapBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected ObservableMap computeValue() {
               return (ObservableMap)var0.get();
            }

            public ObservableList getDependencies() {
               return FXCollections.singletonObservableList(var0);
            }
         });
      }
   }

   public int getSize() {
      return this.size();
   }

   public abstract ReadOnlyIntegerProperty sizeProperty();

   public abstract ReadOnlyBooleanProperty emptyProperty();

   public ObjectBinding valueAt(Object var1) {
      return Bindings.valueAt((ObservableMap)this, (Object)var1);
   }

   public ObjectBinding valueAt(ObservableValue var1) {
      return Bindings.valueAt((ObservableMap)this, (ObservableValue)var1);
   }

   public BooleanBinding isEqualTo(ObservableMap var1) {
      return Bindings.equal((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNotEqualTo(ObservableMap var1) {
      return Bindings.notEqual((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNull() {
      return Bindings.isNull(this);
   }

   public BooleanBinding isNotNull() {
      return Bindings.isNotNull(this);
   }

   public StringBinding asString() {
      return (StringBinding)StringFormatter.convert(this);
   }

   public int size() {
      ObservableMap var1 = (ObservableMap)this.get();
      return var1 == null ? EMPTY_MAP.size() : var1.size();
   }

   public boolean isEmpty() {
      ObservableMap var1 = (ObservableMap)this.get();
      return var1 == null ? EMPTY_MAP.isEmpty() : var1.isEmpty();
   }

   public boolean containsKey(Object var1) {
      ObservableMap var2 = (ObservableMap)this.get();
      return var2 == null ? EMPTY_MAP.containsKey(var1) : var2.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      ObservableMap var2 = (ObservableMap)this.get();
      return var2 == null ? EMPTY_MAP.containsValue(var1) : var2.containsValue(var1);
   }

   public Object put(Object var1, Object var2) {
      ObservableMap var3 = (ObservableMap)this.get();
      return var3 == null ? EMPTY_MAP.put(var1, var2) : var3.put(var1, var2);
   }

   public Object remove(Object var1) {
      ObservableMap var2 = (ObservableMap)this.get();
      return var2 == null ? EMPTY_MAP.remove(var1) : var2.remove(var1);
   }

   public void putAll(Map var1) {
      ObservableMap var2 = (ObservableMap)this.get();
      if (var2 == null) {
         EMPTY_MAP.putAll(var1);
      } else {
         var2.putAll(var1);
      }

   }

   public void clear() {
      ObservableMap var1 = (ObservableMap)this.get();
      if (var1 == null) {
         EMPTY_MAP.clear();
      } else {
         var1.clear();
      }

   }

   public Set keySet() {
      ObservableMap var1 = (ObservableMap)this.get();
      return var1 == null ? EMPTY_MAP.keySet() : var1.keySet();
   }

   public Collection values() {
      ObservableMap var1 = (ObservableMap)this.get();
      return var1 == null ? EMPTY_MAP.values() : var1.values();
   }

   public Set entrySet() {
      ObservableMap var1 = (ObservableMap)this.get();
      return var1 == null ? EMPTY_MAP.entrySet() : var1.entrySet();
   }

   public Object get(Object var1) {
      ObservableMap var2 = (ObservableMap)this.get();
      return var2 == null ? EMPTY_MAP.get(var1) : var2.get(var1);
   }

   private static class EmptyObservableMap extends AbstractMap implements ObservableMap {
      private EmptyObservableMap() {
      }

      public Set entrySet() {
         return Collections.emptySet();
      }

      public void addListener(MapChangeListener var1) {
      }

      public void removeListener(MapChangeListener var1) {
      }

      public void addListener(InvalidationListener var1) {
      }

      public void removeListener(InvalidationListener var1) {
      }

      // $FF: synthetic method
      EmptyObservableMap(Object var1) {
         this();
      }
   }
}
