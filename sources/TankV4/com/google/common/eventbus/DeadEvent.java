package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;

@Beta
public class DeadEvent {
   private final Object source;
   private final Object event;

   public DeadEvent(Object var1, Object var2) {
      this.source = Preconditions.checkNotNull(var1);
      this.event = Preconditions.checkNotNull(var2);
   }

   public Object getSource() {
      return this.source;
   }

   public Object getEvent() {
      return this.event;
   }
}
