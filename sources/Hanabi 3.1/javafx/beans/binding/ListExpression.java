package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class ListExpression implements ObservableListValue {
   private static final ObservableList EMPTY_LIST = FXCollections.emptyObservableList();

   public ObservableList getValue() {
      return (ObservableList)this.get();
   }

   public static ListExpression listExpression(final ObservableListValue var0) {
      if (var0 == null) {
         throw new NullPointerException("List must be specified.");
      } else {
         return (ListExpression)(var0 instanceof ListExpression ? (ListExpression)var0 : new ListBinding() {
            {
               super.bind(var0);
            }

            public void dispose() {
               super.unbind(var0);
            }

            protected ObservableList computeValue() {
               return (ObservableList)var0.get();
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

   public ObjectBinding valueAt(int var1) {
      return Bindings.valueAt(this, var1);
   }

   public ObjectBinding valueAt(ObservableIntegerValue var1) {
      return Bindings.valueAt((ObservableList)this, (ObservableIntegerValue)var1);
   }

   public BooleanBinding isEqualTo(ObservableList var1) {
      return Bindings.equal((ObservableObjectValue)this, (Object)var1);
   }

   public BooleanBinding isNotEqualTo(ObservableList var1) {
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
      ObservableList var1 = (ObservableList)this.get();
      return var1 == null ? EMPTY_LIST.size() : var1.size();
   }

   public boolean isEmpty() {
      ObservableList var1 = (ObservableList)this.get();
      return var1 == null ? EMPTY_LIST.isEmpty() : var1.isEmpty();
   }

   public boolean contains(Object var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.contains(var1) : var2.contains(var1);
   }

   public Iterator iterator() {
      ObservableList var1 = (ObservableList)this.get();
      return var1 == null ? EMPTY_LIST.iterator() : var1.iterator();
   }

   public Object[] toArray() {
      ObservableList var1 = (ObservableList)this.get();
      return var1 == null ? EMPTY_LIST.toArray() : var1.toArray();
   }

   public Object[] toArray(Object[] var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? (Object[])EMPTY_LIST.toArray(var1) : var2.toArray(var1);
   }

   public boolean add(Object var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.add(var1) : var2.add(var1);
   }

   public boolean remove(Object var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.remove(var1) : var2.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.contains(var1) : var2.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.addAll(var1) : var2.addAll(var1);
   }

   public boolean addAll(int var1, Collection var2) {
      ObservableList var3 = (ObservableList)this.get();
      return var3 == null ? EMPTY_LIST.addAll(var1, var2) : var3.addAll(var1, var2);
   }

   public boolean removeAll(Collection var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.removeAll(var1) : var2.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.retainAll(var1) : var2.retainAll(var1);
   }

   public void clear() {
      ObservableList var1 = (ObservableList)this.get();
      if (var1 == null) {
         EMPTY_LIST.clear();
      } else {
         var1.clear();
      }

   }

   public Object get(int var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.get(var1) : var2.get(var1);
   }

   public Object set(int var1, Object var2) {
      ObservableList var3 = (ObservableList)this.get();
      return var3 == null ? EMPTY_LIST.set(var1, var2) : var3.set(var1, var2);
   }

   public void add(int var1, Object var2) {
      ObservableList var3 = (ObservableList)this.get();
      if (var3 == null) {
         EMPTY_LIST.add(var1, var2);
      } else {
         var3.add(var1, var2);
      }

   }

   public Object remove(int var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.remove(var1) : var2.remove(var1);
   }

   public int indexOf(Object var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.indexOf(var1) : var2.indexOf(var1);
   }

   public int lastIndexOf(Object var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.lastIndexOf(var1) : var2.lastIndexOf(var1);
   }

   public ListIterator listIterator() {
      ObservableList var1 = (ObservableList)this.get();
      return var1 == null ? EMPTY_LIST.listIterator() : var1.listIterator();
   }

   public ListIterator listIterator(int var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.listIterator(var1) : var2.listIterator(var1);
   }

   public List subList(int var1, int var2) {
      ObservableList var3 = (ObservableList)this.get();
      return var3 == null ? EMPTY_LIST.subList(var1, var2) : var3.subList(var1, var2);
   }

   public boolean addAll(Object... var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.addAll(var1) : var2.addAll(var1);
   }

   public boolean setAll(Object... var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.setAll(var1) : var2.setAll(var1);
   }

   public boolean setAll(Collection var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.setAll(var1) : var2.setAll(var1);
   }

   public boolean removeAll(Object... var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.removeAll(var1) : var2.removeAll(var1);
   }

   public boolean retainAll(Object... var1) {
      ObservableList var2 = (ObservableList)this.get();
      return var2 == null ? EMPTY_LIST.retainAll(var1) : var2.retainAll(var1);
   }

   public void remove(int var1, int var2) {
      ObservableList var3 = (ObservableList)this.get();
      if (var3 == null) {
         EMPTY_LIST.remove(var1, var2);
      } else {
         var3.remove(var1, var2);
      }

   }
}
