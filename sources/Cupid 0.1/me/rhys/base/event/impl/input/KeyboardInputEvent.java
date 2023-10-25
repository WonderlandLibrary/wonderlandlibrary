package me.rhys.base.event.impl.input;

import me.rhys.base.event.Event;

public class KeyboardInputEvent extends Event {
  private final int keyCode;
  
  public KeyboardInputEvent(int keyCode) {
    this.keyCode = keyCode;
  }
  
  public int getKeyCode() {
    return this.keyCode;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\input\KeyboardInputEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */