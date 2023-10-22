package com.sun.javafx.event;

import java.util.Set;
import javafx.event.EventTarget;

public interface CompositeEventTarget extends EventTarget {
   Set getTargets();

   boolean containsTarget(EventTarget var1);
}
