package javafx.collections;

import java.text.Collator;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public interface ObservableList extends List, Observable {
   void addListener(ListChangeListener var1);

   void removeListener(ListChangeListener var1);

   boolean addAll(Object... var1);

   boolean setAll(Object... var1);

   boolean setAll(Collection var1);

   boolean removeAll(Object... var1);

   boolean retainAll(Object... var1);

   void remove(int var1, int var2);

   default FilteredList filtered(Predicate var1) {
      return new FilteredList(this, var1);
   }

   default SortedList sorted(Comparator var1) {
      return new SortedList(this, var1);
   }

   default SortedList sorted() {
      Comparator var1 = new Comparator() {
         public int compare(Object var1, Object var2) {
            if (var1 == null && var2 == null) {
               return 0;
            } else if (var1 == null) {
               return -1;
            } else if (var2 == null) {
               return 1;
            } else {
               return var1 instanceof Comparable ? ((Comparable)var1).compareTo(var2) : Collator.getInstance().compare(var1.toString(), var2.toString());
            }
         }
      };
      return this.sorted(var1);
   }
}
