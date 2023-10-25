package net.java.games.input;

public class WinTabButtonComponent extends WinTabComponent {
  private int index;
  
  protected WinTabButtonComponent(WinTabContext context, int parentDevice, String name, Component.Identifier id, int index) {
    super(context, parentDevice, name, id);
    this.index = index;
  }
  
  public Event processPacket(WinTabPacket packet) {
    Event newEvent = null;
    float newValue = ((packet.PK_BUTTONS & (int)Math.pow(2.0D, this.index)) > 0) ? 1.0F : 0.0F;
    if (newValue != getPollData()) {
      this.lastKnownValue = newValue;
      newEvent = new Event();
      newEvent.set(this, newValue, packet.PK_TIME * 1000L);
      return newEvent;
    } 
    return newEvent;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\WinTabButtonComponent.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */