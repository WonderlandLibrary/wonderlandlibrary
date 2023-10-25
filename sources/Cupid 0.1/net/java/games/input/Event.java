package net.java.games.input;

public final class Event {
  private Component component;
  
  private float value;
  
  private long nanos;
  
  public final void set(Event other) {
    set(other.getComponent(), other.getValue(), other.getNanos());
  }
  
  public final void set(Component component, float value, long nanos) {
    this.component = component;
    this.value = value;
    this.nanos = nanos;
  }
  
  public final Component getComponent() {
    return this.component;
  }
  
  public final float getValue() {
    return this.value;
  }
  
  public final long getNanos() {
    return this.nanos;
  }
  
  public final String toString() {
    return "Event: component = " + this.component + " | value = " + this.value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\Event.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */