package com.sun.javafx.collections;

import java.util.Comparator;
import java.util.List;

public interface SortableList extends List {
   void sort();

   void sort(Comparator var1);
}
