package me.rhys.base.event;

public class Event {
  private Type type;
  
  private Direction direction;
  
  private boolean cancelled;
  
  public Type getType() {
    return this.type;
  }
  
  public Direction getDirection() {
    return this.direction;
  }
  
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
  
  public boolean isCancelled() {
    return this.cancelled;
  }
  
  public Event(Type type, Direction direction) {
    this.type = type;
    this.direction = direction;
    this.cancelled = false;
  }
  
  public Event() {
    this(Type.PRE, Direction.IN);
  }
  
  public Event setType(Type type) {
    this.type = type;
    return this;
  }
  
  public Event setDirection(Direction direction) {
    this.direction = direction;
    return this;
  }
  
  public enum Type {
    PRE, POST;
  }
  
  public enum Direction {
    IN, OUT;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */