package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableSetValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public abstract class SetExpression implements ObservableSetValue {
   private static final ObservableSet EMPTY_SET = new EmptyObservableSet();

   public ObservableSet getValue() {
      return (ObservableSet)this.get();
   }

   public static SetExpression setExpression(final ObservableSetValue var0) {
      if (var0 == null) {
         throw new NullPointerException("Set must be specified.");
      } else {
         return (SetExpression)(var0 instanceof SetExpression ? (SetExpression)var0 : new SetBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected ObservableSet computeValue() {
               return (ObservableSet)var0.get();
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

   public BooleanBinding isEqualTo(ObservableSet var1) {
      return Bindings.equal((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNotEqualTo(ObservableSet var1) {
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
      ObservableSet var1 = (ObservableSet)this.get();
      return var1 == null ? EMPTY_SET.size() : var1.size();
   }

   public boolean isEmpty() {
      ObservableSet var1 = (ObservableSet)this.get();
      return var1 == null ? EMPTY_SET.isEmpty() : var1.isEmpty();
   }

   public boolean contains(Object var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.contains(var1) : var2.contains(var1);
   }

   public Iterator iterator() {
      ObservableSet var1 = (ObservableSet)this.get();
      return var1 == null ? EMPTY_SET.iterator() : var1.iterator();
   }

   public Object[] toArray() {
      ObservableSet var1 = (ObservableSet)this.get();
      return var1 == null ? EMPTY_SET.toArray() : var1.toArray();
   }

   public Object[] toArray(Object[] var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? (Object[])EMPTY_SET.toArray(var1) : var2.toArray(var1);
   }

   public boolean add(Object var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.add(var1) : var2.add(var1);
   }

   public boolean remove(Object var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.remove(var1) : var2.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.contains(var1) : var2.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.addAll(var1) : var2.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.removeAll(var1) : var2.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      ObservableSet var2 = (ObservableSet)this.get();
      return var2 == null ? EMPTY_SET.retainAll(var1) : var2.retainAll(var1);
   }

   public void clear() {
      ObservableSet var1 = (ObservableSet)this.get();
      if (var1 == null) {
         EMPTY_SET.clear();
      } else {
         var1.clear();
      }

   }

   private static class EmptyObservableSet extends AbstractSet implements ObservableSet {
      private static final Iterator iterator = new Iterator() {
         public boolean hasNext() {
            return false;
         }

         public Object next() {
            throw new NoSuchElementException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };

      private EmptyObservableSet() {
      }

      public Iterator iterator() {
         return iterator;
      }

      public int size() {
         return 0;
      }

      public void addListener(SetChangeListener var1) {
      }

      public void removeListener(SetChangeListener var1) {
      }

      public void addListener(InvalidationListener var1) {
      }

      public void removeListener(InvalidationListener var1) {
      }

      // $FF: synthetic method
      EmptyObservableSet(Object var1) {
         this();
      }
   }
}
