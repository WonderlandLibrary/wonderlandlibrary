package com.sun.javafx.scene.shape;

import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.collections.ObservableIntegerArray;
import javafx.scene.shape.ObservableFaceArray;

public class ObservableFaceArrayImpl extends ObservableIntegerArrayImpl implements ObservableFaceArray {
   public ObservableFaceArrayImpl() {
   }

   public ObservableFaceArrayImpl(int... var1) {
      super(var1);
   }

   public ObservableFaceArrayImpl(ObservableFaceArray var1) {
      super((ObservableIntegerArray)var1);
   }
}
