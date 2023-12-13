package javafx.collections;

import javafx.beans.Observable;

public interface ObservableArray extends Observable {
   void addListener(ArrayChangeListener var1);

   void removeListener(ArrayChangeListener var1);

   void resize(int var1);

   void ensureCapacity(int var1);

   void trimToSize();

   void clear();

   int size();
}
