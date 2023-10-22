package com.sun.javafx.event;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.event.EventDispatchChain;
import javafx.event.EventTarget;

public class CompositeEventTargetImpl implements CompositeEventTarget {
   private final Set eventTargets;

   public CompositeEventTargetImpl(EventTarget... var1) {
      HashSet var2 = new HashSet(var1.length);
      var2.addAll(Arrays.asList(var1));
      this.eventTargets = Collections.unmodifiableSet(var2);
   }

   public Set getTargets() {
      return this.eventTargets;
   }

   public boolean containsTarget(EventTarget var1) {
      return this.eventTargets.contains(var1);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      EventDispatchTree var2 = (EventDispatchTree)var1;

      EventTarget var4;
      EventDispatchTree var5;
      for(Iterator var3 = this.eventTargets.iterator(); var3.hasNext(); var2 = var2.mergeTree((EventDispatchTree)var4.buildEventDispatchChain(var5))) {
         var4 = (EventTarget)var3.next();
         var5 = var2.createTree();
      }

      return var2;
   }
}
