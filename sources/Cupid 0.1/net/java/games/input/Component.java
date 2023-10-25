package net.java.games.input;

public interface Component {
  Identifier getIdentifier();
  
  boolean isRelative();
  
  boolean isAnalog();
  
  float getDeadZone();
  
  float getPollData();
  
  String getName();
  
  public static class Identifier {
    private final String name;
    
    protected Identifier(String name) {
      this.name = name;
    }
    
    public String getName() {
      return this.name;
    }
    
    public String toString() {
      return this.name;
    }
    
    public static class Axis extends Identifier {
      protected Axis(String name) {
        super(name);
      }
      
      public static final Axis X = new Axis("x");
      
      public static final Axis Y = new Axis("y");
      
      public static final Axis Z = new Axis("z");
      
      public static final Axis RX = new Axis("rx");
      
      public static final Axis RY = new Axis("ry");
      
      public static final Axis RZ = new Axis("rz");
      
      public static final Axis SLIDER = new Axis("slider");
      
      public static final Axis SLIDER_ACCELERATION = new Axis("slider-acceleration");
      
      public static final Axis SLIDER_FORCE = new Axis("slider-force");
      
      public static final Axis SLIDER_VELOCITY = new Axis("slider-velocity");
      
      public static final Axis X_ACCELERATION = new Axis("x-acceleration");
      
      public static final Axis X_FORCE = new Axis("x-force");
      
      public static final Axis X_VELOCITY = new Axis("x-velocity");
      
      public static final Axis Y_ACCELERATION = new Axis("y-acceleration");
      
      public static final Axis Y_FORCE = new Axis("y-force");
      
      public static final Axis Y_VELOCITY = new Axis("y-velocity");
      
      public static final Axis Z_ACCELERATION = new Axis("z-acceleration");
      
      public static final Axis Z_FORCE = new Axis("z-force");
      
      public static final Axis Z_VELOCITY = new Axis("z-velocity");
      
      public static final Axis RX_ACCELERATION = new Axis("rx-acceleration");
      
      public static final Axis RX_FORCE = new Axis("rx-force");
      
      public static final Axis RX_VELOCITY = new Axis("rx-velocity");
      
      public static final Axis RY_ACCELERATION = new Axis("ry-acceleration");
      
      public static final Axis RY_FORCE = new Axis("ry-force");
      
      public static final Axis RY_VELOCITY = new Axis("ry-velocity");
      
      public static final Axis RZ_ACCELERATION = new Axis("rz-acceleration");
      
      public static final Axis RZ_FORCE = new Axis("rz-force");
      
      public static final Axis RZ_VELOCITY = new Axis("rz-velocity");
      
      public static final Axis POV = new Axis("pov");
      
      public static final Axis UNKNOWN = new Axis("unknown");
    }
    
    public static class Button extends Identifier {
      public Button(String name) {
        super(name);
      }
      
      public static final Button _0 = new Button("0");
      
      public static final Button _1 = new Button("1");
      
      public static final Button _2 = new Button("2");
      
      public static final Button _3 = new Button("3");
      
      public static final Button _4 = new Button("4");
      
      public static final Button _5 = new Button("5");
      
      public static final Button _6 = new Button("6");
      
      public static final Button _7 = new Button("7");
      
      public static final Button _8 = new Button("8");
      
      public static final Button _9 = new Button("9");
      
      public static final Button _10 = new Button("10");
      
      public static final Button _11 = new Button("11");
      
      public static final Button _12 = new Button("12");
      
      public static final Button _13 = new Button("13");
      
      public static final Button _14 = new Button("14");
      
      public static final Button _15 = new Button("15");
      
      public static final Button _16 = new Button("16");
      
      public static final Button _17 = new Button("17");
      
      public static final Button _18 = new Button("18");
      
      public static final Button _19 = new Button("19");
      
      public static final Button _20 = new Button("20");
      
      public static final Button _21 = new Button("21");
      
      public static final Button _22 = new Button("22");
      
      public static final Button _23 = new Button("23");
      
      public static final Button _24 = new Button("24");
      
      public static final Button _25 = new Button("25");
      
      public static final Button _26 = new Button("26");
      
      public static final Button _27 = new Button("27");
      
      public static final Button _28 = new Button("28");
      
      public static final Button _29 = new Button("29");
      
      public static final Button _30 = new Button("30");
      
      public static final Button _31 = new Button("31");
      
      public static final Button TRIGGER = new Button("Trigger");
      
      public static final Button THUMB = new Button("Thumb");
      
      public static final Button THUMB2 = new Button("Thumb 2");
      
      public static final Button TOP = new Button("Top");
      
      public static final Button TOP2 = new Button("Top 2");
      
      public static final Button PINKIE = new Button("Pinkie");
      
      public static final Button BASE = new Button("Base");
      
      public static final Button BASE2 = new Button("Base 2");
      
      public static final Button BASE3 = new Button("Base 3");
      
      public static final Button BASE4 = new Button("Base 4");
      
      public static final Button BASE5 = new Button("Base 5");
      
      public static final Button BASE6 = new Button("Base 6");
      
      public static final Button DEAD = new Button("Dead");
      
      public static final Button A = new Button("A");
      
      public static final Button B = new Button("B");
      
      public static final Button C = new Button("C");
      
      public static final Button X = new Button("X");
      
      public static final Button Y = new Button("Y");
      
      public static final Button Z = new Button("Z");
      
      public static final Button LEFT_THUMB = new Button("Left Thumb");
      
      public static final Button RIGHT_THUMB = new Button("Right Thumb");
      
      public static final Button LEFT_THUMB2 = new Button("Left Thumb 2");
      
      public static final Button RIGHT_THUMB2 = new Button("Right Thumb 2");
      
      public static final Button SELECT = new Button("Select");
      
      public static final Button MODE = new Button("Mode");
      
      public static final Button LEFT_THUMB3 = new Button("Left Thumb 3");
      
      public static final Button RIGHT_THUMB3 = new Button("Right Thumb 3");
      
      public static final Button TOOL_PEN = new Button("Pen");
      
      public static final Button TOOL_RUBBER = new Button("Rubber");
      
      public static final Button TOOL_BRUSH = new Button("Brush");
      
      public static final Button TOOL_PENCIL = new Button("Pencil");
      
      public static final Button TOOL_AIRBRUSH = new Button("Airbrush");
      
      public static final Button TOOL_FINGER = new Button("Finger");
      
      public static final Button TOOL_MOUSE = new Button("Mouse");
      
      public static final Button TOOL_LENS = new Button("Lens");
      
      public static final Button TOUCH = new Button("Touch");
      
      public static final Button STYLUS = new Button("Stylus");
      
      public static final Button STYLUS2 = new Button("Stylus 2");
      
      public static final Button UNKNOWN = new Button("Unknown");
      
      public static final Button BACK = new Button("Back");
      
      public static final Button EXTRA = new Button("Extra");
      
      public static final Button FORWARD = new Button("Forward");
      
      public static final Button LEFT = new Button("Left");
      
      public static final Button MIDDLE = new Button("Middle");
      
      public static final Button RIGHT = new Button("Right");
      
      public static final Button SIDE = new Button("Side");
    }
    
    public static class Key extends Identifier {
      protected Key(String name) {
        super(name);
      }
      
      public static final Key VOID = new Key("Void");
      
      public static final Key ESCAPE = new Key("Escape");
      
      public static final Key _1 = new Key("1");
      
      public static final Key _2 = new Key("2");
      
      public static final Key _3 = new Key("3");
      
      public static final Key _4 = new Key("4");
      
      public static final Key _5 = new Key("5");
      
      public static final Key _6 = new Key("6");
      
      public static final Key _7 = new Key("7");
      
      public static final Key _8 = new Key("8");
      
      public static final Key _9 = new Key("9");
      
      public static final Key _0 = new Key("0");
      
      public static final Key MINUS = new Key("-");
      
      public static final Key EQUALS = new Key("=");
      
      public static final Key BACK = new Key("Back");
      
      public static final Key TAB = new Key("Tab");
      
      public static final Key Q = new Key("Q");
      
      public static final Key W = new Key("W");
      
      public static final Key E = new Key("E");
      
      public static final Key R = new Key("R");
      
      public static final Key T = new Key("T");
      
      public static final Key Y = new Key("Y");
      
      public static final Key U = new Key("U");
      
      public static final Key I = new Key("I");
      
      public static final Key O = new Key("O");
      
      public static final Key P = new Key("P");
      
      public static final Key LBRACKET = new Key("[");
      
      public static final Key RBRACKET = new Key("]");
      
      public static final Key RETURN = new Key("Return");
      
      public static final Key LCONTROL = new Key("Left Control");
      
      public static final Key A = new Key("A");
      
      public static final Key S = new Key("S");
      
      public static final Key D = new Key("D");
      
      public static final Key F = new Key("F");
      
      public static final Key G = new Key("G");
      
      public static final Key H = new Key("H");
      
      public static final Key J = new Key("J");
      
      public static final Key K = new Key("K");
      
      public static final Key L = new Key("L");
      
      public static final Key SEMICOLON = new Key(";");
      
      public static final Key APOSTROPHE = new Key("'");
      
      public static final Key GRAVE = new Key("~");
      
      public static final Key LSHIFT = new Key("Left Shift");
      
      public static final Key BACKSLASH = new Key("\\");
      
      public static final Key Z = new Key("Z");
      
      public static final Key X = new Key("X");
      
      public static final Key C = new Key("C");
      
      public static final Key V = new Key("V");
      
      public static final Key B = new Key("B");
      
      public static final Key N = new Key("N");
      
      public static final Key M = new Key("M");
      
      public static final Key COMMA = new Key(",");
      
      public static final Key PERIOD = new Key(".");
      
      public static final Key SLASH = new Key("/");
      
      public static final Key RSHIFT = new Key("Right Shift");
      
      public static final Key MULTIPLY = new Key("Multiply");
      
      public static final Key LALT = new Key("Left Alt");
      
      public static final Key SPACE = new Key(" ");
      
      public static final Key CAPITAL = new Key("Caps Lock");
      
      public static final Key F1 = new Key("F1");
      
      public static final Key F2 = new Key("F2");
      
      public static final Key F3 = new Key("F3");
      
      public static final Key F4 = new Key("F4");
      
      public static final Key F5 = new Key("F5");
      
      public static final Key F6 = new Key("F6");
      
      public static final Key F7 = new Key("F7");
      
      public static final Key F8 = new Key("F8");
      
      public static final Key F9 = new Key("F9");
      
      public static final Key F10 = new Key("F10");
      
      public static final Key NUMLOCK = new Key("Num Lock");
      
      public static final Key SCROLL = new Key("Scroll Lock");
      
      public static final Key NUMPAD7 = new Key("Num 7");
      
      public static final Key NUMPAD8 = new Key("Num 8");
      
      public static final Key NUMPAD9 = new Key("Num 9");
      
      public static final Key SUBTRACT = new Key("Num -");
      
      public static final Key NUMPAD4 = new Key("Num 4");
      
      public static final Key NUMPAD5 = new Key("Num 5");
      
      public static final Key NUMPAD6 = new Key("Num 6");
      
      public static final Key ADD = new Key("Num +");
      
      public static final Key NUMPAD1 = new Key("Num 1");
      
      public static final Key NUMPAD2 = new Key("Num 2");
      
      public static final Key NUMPAD3 = new Key("Num 3");
      
      public static final Key NUMPAD0 = new Key("Num 0");
      
      public static final Key DECIMAL = new Key("Num .");
      
      public static final Key F11 = new Key("F11");
      
      public static final Key F12 = new Key("F12");
      
      public static final Key F13 = new Key("F13");
      
      public static final Key F14 = new Key("F14");
      
      public static final Key F15 = new Key("F15");
      
      public static final Key KANA = new Key("Kana");
      
      public static final Key CONVERT = new Key("Convert");
      
      public static final Key NOCONVERT = new Key("Noconvert");
      
      public static final Key YEN = new Key("Yen");
      
      public static final Key NUMPADEQUAL = new Key("Num =");
      
      public static final Key CIRCUMFLEX = new Key("Circumflex");
      
      public static final Key AT = new Key("At");
      
      public static final Key COLON = new Key("Colon");
      
      public static final Key UNDERLINE = new Key("Underline");
      
      public static final Key KANJI = new Key("Kanji");
      
      public static final Key STOP = new Key("Stop");
      
      public static final Key AX = new Key("Ax");
      
      public static final Key UNLABELED = new Key("Unlabeled");
      
      public static final Key NUMPADENTER = new Key("Num Enter");
      
      public static final Key RCONTROL = new Key("Right Control");
      
      public static final Key NUMPADCOMMA = new Key("Num ,");
      
      public static final Key DIVIDE = new Key("Num /");
      
      public static final Key SYSRQ = new Key("SysRq");
      
      public static final Key RALT = new Key("Right Alt");
      
      public static final Key PAUSE = new Key("Pause");
      
      public static final Key HOME = new Key("Home");
      
      public static final Key UP = new Key("Up");
      
      public static final Key PAGEUP = new Key("Pg Up");
      
      public static final Key LEFT = new Key("Left");
      
      public static final Key RIGHT = new Key("Right");
      
      public static final Key END = new Key("End");
      
      public static final Key DOWN = new Key("Down");
      
      public static final Key PAGEDOWN = new Key("Pg Down");
      
      public static final Key INSERT = new Key("Insert");
      
      public static final Key DELETE = new Key("Delete");
      
      public static final Key LWIN = new Key("Left Windows");
      
      public static final Key RWIN = new Key("Right Windows");
      
      public static final Key APPS = new Key("Apps");
      
      public static final Key POWER = new Key("Power");
      
      public static final Key SLEEP = new Key("Sleep");
      
      public static final Key UNKNOWN = new Key("Unknown");
    }
  }
  
  public static class POV {
    public static final float OFF = 0.0F;
    
    public static final float CENTER = 0.0F;
    
    public static final float UP_LEFT = 0.125F;
    
    public static final float UP = 0.25F;
    
    public static final float UP_RIGHT = 0.375F;
    
    public static final float RIGHT = 0.5F;
    
    public static final float DOWN_RIGHT = 0.625F;
    
    public static final float DOWN = 0.75F;
    
    public static final float DOWN_LEFT = 0.875F;
    
    public static final float LEFT = 1.0F;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\Component.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */