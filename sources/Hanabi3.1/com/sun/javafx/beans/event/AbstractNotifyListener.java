package com.sun.javafx.beans.event;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;

public abstract class AbstractNotifyListener implements InvalidationListener {
   private final WeakInvalidationListener weakListener = new WeakInvalidationListener(this);

   public InvalidationListener getWeakListener() {
      return this.weakListener;
   }
}
