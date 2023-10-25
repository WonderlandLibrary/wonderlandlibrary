package net.java.games.input;

public interface Controller {
  Controller[] getControllers();
  
  Type getType();
  
  Component[] getComponents();
  
  Component getComponent(Component.Identifier paramIdentifier);
  
  Rumbler[] getRumblers();
  
  boolean poll();
  
  void setEventQueueSize(int paramInt);
  
  EventQueue getEventQueue();
  
  PortType getPortType();
  
  int getPortNumber();
  
  String getName();
  
  public static class Type {
    private final String name;
    
    protected Type(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static final Type UNKNOWN = new Type("Unknown");
    
    public static final Type MOUSE = new Type("Mouse");
    
    public static final Type KEYBOARD = new Type("Keyboard");
    
    public static final Type FINGERSTICK = new Type("Fingerstick");
    
    public static final Type GAMEPAD = new Type("Gamepad");
    
    public static final Type HEADTRACKER = new Type("Headtracker");
    
    public static final Type RUDDER = new Type("Rudder");
    
    public static final Type STICK = new Type("Stick");
    
    public static final Type TRACKBALL = new Type("Trackball");
    
    public static final Type TRACKPAD = new Type("Trackpad");
    
    public static final Type WHEEL = new Type("Wheel");
  }
  
  public static final class PortType {
    private final String name;
    
    protected PortType(String name) {
      this.name = name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static final PortType UNKNOWN = new PortType("Unknown");
    
    public static final PortType USB = new PortType("USB port");
    
    public static final PortType GAME = new PortType("Game port");
    
    public static final PortType NETWORK = new PortType("Network port");
    
    public static final PortType SERIAL = new PortType("Serial port");
    
    public static final PortType I8042 = new PortType("i8042 (PS/2)");
    
    public static final PortType PARALLEL = new PortType("Parallel port");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\Controller.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */