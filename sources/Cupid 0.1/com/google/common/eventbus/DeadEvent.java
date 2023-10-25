package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;

@Beta
public class DeadEvent {
  private final Object source;
  
  private final Object event;
  
  public DeadEvent(Object source, Object event) {
    this.source = Preconditions.checkNotNull(source);
    this.event = Preconditions.checkNotNull(event);
  }
  
  public Object getSource() {
    return this.source;
  }
  
  public Object getEvent() {
    return this.event;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\eventbus\DeadEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */