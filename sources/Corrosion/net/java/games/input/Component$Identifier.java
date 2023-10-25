package net.java.games.input;

public class Identifier {
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

    public static class Key extends Identifier {
        public static final Identifier.Key VOID = new Identifier.Key("Void");
        public static final Identifier.Key ESCAPE = new Identifier.Key("Escape");
        public static final Identifier.Key _1 = new Identifier.Key("1");
        public static final Identifier.Key _2 = new Identifier.Key("2");
        public static final Identifier.Key _3 = new Identifier.Key("3");
        public static final Identifier.Key _4 = new Identifier.Key("4");
        public static final Identifier.Key _5 = new Identifier.Key("5");
        public static final Identifier.Key _6 = new Identifier.Key("6");
        public static final Identifier.Key _7 = new Identifier.Key("7");
        public static final Identifier.Key _8 = new Identifier.Key("8");
        public static final Identifier.Key _9 = new Identifier.Key("9");
        public static final Identifier.Key _0 = new Identifier.Key("0");
        public static final Identifier.Key MINUS = new Identifier.Key("-");
        public static final Identifier.Key EQUALS = new Identifier.Key("=");
        public static final Identifier.Key BACK = new Identifier.Key("Back");
        public static final Identifier.Key TAB = new Identifier.Key("Tab");
        public static final Identifier.Key Q = new Identifier.Key("Q");
        public static final Identifier.Key W = new Identifier.Key("W");
        public static final Identifier.Key E = new Identifier.Key("E");
        public static final Identifier.Key R = new Identifier.Key("R");
        public static final Identifier.Key T = new Identifier.Key("T");
        public static final Identifier.Key Y = new Identifier.Key("Y");
        public static final Identifier.Key U = new Identifier.Key("U");
        public static final Identifier.Key I = new Identifier.Key("I");
        public static final Identifier.Key O = new Identifier.Key("O");
        public static final Identifier.Key P = new Identifier.Key("P");
        public static final Identifier.Key LBRACKET = new Identifier.Key("[");
        public static final Identifier.Key RBRACKET = new Identifier.Key("]");
        public static final Identifier.Key RETURN = new Identifier.Key("Return");
        public static final Identifier.Key LCONTROL = new Identifier.Key("Left Control");
        public static final Identifier.Key A = new Identifier.Key("A");
        public static final Identifier.Key S = new Identifier.Key("S");
        public static final Identifier.Key D = new Identifier.Key("D");
        public static final Identifier.Key F = new Identifier.Key("F");
        public static final Identifier.Key G = new Identifier.Key("G");
        public static final Identifier.Key H = new Identifier.Key("H");
        public static final Identifier.Key J = new Identifier.Key("J");
        public static final Identifier.Key K = new Identifier.Key("K");
        public static final Identifier.Key L = new Identifier.Key("L");
        public static final Identifier.Key SEMICOLON = new Identifier.Key(";");
        public static final Identifier.Key APOSTROPHE = new Identifier.Key("'");
        public static final Identifier.Key GRAVE = new Identifier.Key("~");
        public static final Identifier.Key LSHIFT = new Identifier.Key("Left Shift");
        public static final Identifier.Key BACKSLASH = new Identifier.Key("\\");
        public static final Identifier.Key Z = new Identifier.Key("Z");
        public static final Identifier.Key X = new Identifier.Key("X");
        public static final Identifier.Key C = new Identifier.Key("C");
        public static final Identifier.Key V = new Identifier.Key("V");
        public static final Identifier.Key B = new Identifier.Key("B");
        public static final Identifier.Key N = new Identifier.Key("N");
        public static final Identifier.Key M = new Identifier.Key("M");
        public static final Identifier.Key COMMA = new Identifier.Key(",");
        public static final Identifier.Key PERIOD = new Identifier.Key(".");
        public static final Identifier.Key SLASH = new Identifier.Key("/");
        public static final Identifier.Key RSHIFT = new Identifier.Key("Right Shift");
        public static final Identifier.Key MULTIPLY = new Identifier.Key("Multiply");
        public static final Identifier.Key LALT = new Identifier.Key("Left Alt");
        public static final Identifier.Key SPACE = new Identifier.Key(" ");
        public static final Identifier.Key CAPITAL = new Identifier.Key("Caps Lock");
        public static final Identifier.Key F1 = new Identifier.Key("F1");
        public static final Identifier.Key F2 = new Identifier.Key("F2");
        public static final Identifier.Key F3 = new Identifier.Key("F3");
        public static final Identifier.Key F4 = new Identifier.Key("F4");
        public static final Identifier.Key F5 = new Identifier.Key("F5");
        public static final Identifier.Key F6 = new Identifier.Key("F6");
        public static final Identifier.Key F7 = new Identifier.Key("F7");
        public static final Identifier.Key F8 = new Identifier.Key("F8");
        public static final Identifier.Key F9 = new Identifier.Key("F9");
        public static final Identifier.Key F10 = new Identifier.Key("F10");
        public static final Identifier.Key NUMLOCK = new Identifier.Key("Num Lock");
        public static final Identifier.Key SCROLL = new Identifier.Key("Scroll Lock");
        public static final Identifier.Key NUMPAD7 = new Identifier.Key("Num 7");
        public static final Identifier.Key NUMPAD8 = new Identifier.Key("Num 8");
        public static final Identifier.Key NUMPAD9 = new Identifier.Key("Num 9");
        public static final Identifier.Key SUBTRACT = new Identifier.Key("Num -");
        public static final Identifier.Key NUMPAD4 = new Identifier.Key("Num 4");
        public static final Identifier.Key NUMPAD5 = new Identifier.Key("Num 5");
        public static final Identifier.Key NUMPAD6 = new Identifier.Key("Num 6");
        public static final Identifier.Key ADD = new Identifier.Key("Num +");
        public static final Identifier.Key NUMPAD1 = new Identifier.Key("Num 1");
        public static final Identifier.Key NUMPAD2 = new Identifier.Key("Num 2");
        public static final Identifier.Key NUMPAD3 = new Identifier.Key("Num 3");
        public static final Identifier.Key NUMPAD0 = new Identifier.Key("Num 0");
        public static final Identifier.Key DECIMAL = new Identifier.Key("Num .");
        public static final Identifier.Key F11 = new Identifier.Key("F11");
        public static final Identifier.Key F12 = new Identifier.Key("F12");
        public static final Identifier.Key F13 = new Identifier.Key("F13");
        public static final Identifier.Key F14 = new Identifier.Key("F14");
        public static final Identifier.Key F15 = new Identifier.Key("F15");
        public static final Identifier.Key KANA = new Identifier.Key("Kana");
        public static final Identifier.Key CONVERT = new Identifier.Key("Convert");
        public static final Identifier.Key NOCONVERT = new Identifier.Key("Noconvert");
        public static final Identifier.Key YEN = new Identifier.Key("Yen");
        public static final Identifier.Key NUMPADEQUAL = new Identifier.Key("Num =");
        public static final Identifier.Key CIRCUMFLEX = new Identifier.Key("Circumflex");
        public static final Identifier.Key AT = new Identifier.Key("At");
        public static final Identifier.Key COLON = new Identifier.Key("Colon");
        public static final Identifier.Key UNDERLINE = new Identifier.Key("Underline");
        public static final Identifier.Key KANJI = new Identifier.Key("Kanji");
        public static final Identifier.Key STOP = new Identifier.Key("Stop");
        public static final Identifier.Key AX = new Identifier.Key("Ax");
        public static final Identifier.Key UNLABELED = new Identifier.Key("Unlabeled");
        public static final Identifier.Key NUMPADENTER = new Identifier.Key("Num Enter");
        public static final Identifier.Key RCONTROL = new Identifier.Key("Right Control");
        public static final Identifier.Key NUMPADCOMMA = new Identifier.Key("Num ,");
        public static final Identifier.Key DIVIDE = new Identifier.Key("Num /");
        public static final Identifier.Key SYSRQ = new Identifier.Key("SysRq");
        public static final Identifier.Key RALT = new Identifier.Key("Right Alt");
        public static final Identifier.Key PAUSE = new Identifier.Key("Pause");
        public static final Identifier.Key HOME = new Identifier.Key("Home");
        public static final Identifier.Key UP = new Identifier.Key("Up");
        public static final Identifier.Key PAGEUP = new Identifier.Key("Pg Up");
        public static final Identifier.Key LEFT = new Identifier.Key("Left");
        public static final Identifier.Key RIGHT = new Identifier.Key("Right");
        public static final Identifier.Key END = new Identifier.Key("End");
        public static final Identifier.Key DOWN = new Identifier.Key("Down");
        public static final Identifier.Key PAGEDOWN = new Identifier.Key("Pg Down");
        public static final Identifier.Key INSERT = new Identifier.Key("Insert");
        public static final Identifier.Key DELETE = new Identifier.Key("Delete");
        public static final Identifier.Key LWIN = new Identifier.Key("Left Windows");
        public static final Identifier.Key RWIN = new Identifier.Key("Right Windows");
        public static final Identifier.Key APPS = new Identifier.Key("Apps");
        public static final Identifier.Key POWER = new Identifier.Key("Power");
        public static final Identifier.Key SLEEP = new Identifier.Key("Sleep");
        public static final Identifier.Key UNKNOWN = new Identifier.Key("Unknown");

        protected Key(String name) {
            super(name);
        }
    }

    public static class Button extends Identifier {
        public static final Identifier.Button _0 = new Identifier.Button("0");
        public static final Identifier.Button _1 = new Identifier.Button("1");
        public static final Identifier.Button _2 = new Identifier.Button("2");
        public static final Identifier.Button _3 = new Identifier.Button("3");
        public static final Identifier.Button _4 = new Identifier.Button("4");
        public static final Identifier.Button _5 = new Identifier.Button("5");
        public static final Identifier.Button _6 = new Identifier.Button("6");
        public static final Identifier.Button _7 = new Identifier.Button("7");
        public static final Identifier.Button _8 = new Identifier.Button("8");
        public static final Identifier.Button _9 = new Identifier.Button("9");
        public static final Identifier.Button _10 = new Identifier.Button("10");
        public static final Identifier.Button _11 = new Identifier.Button("11");
        public static final Identifier.Button _12 = new Identifier.Button("12");
        public static final Identifier.Button _13 = new Identifier.Button("13");
        public static final Identifier.Button _14 = new Identifier.Button("14");
        public static final Identifier.Button _15 = new Identifier.Button("15");
        public static final Identifier.Button _16 = new Identifier.Button("16");
        public static final Identifier.Button _17 = new Identifier.Button("17");
        public static final Identifier.Button _18 = new Identifier.Button("18");
        public static final Identifier.Button _19 = new Identifier.Button("19");
        public static final Identifier.Button _20 = new Identifier.Button("20");
        public static final Identifier.Button _21 = new Identifier.Button("21");
        public static final Identifier.Button _22 = new Identifier.Button("22");
        public static final Identifier.Button _23 = new Identifier.Button("23");
        public static final Identifier.Button _24 = new Identifier.Button("24");
        public static final Identifier.Button _25 = new Identifier.Button("25");
        public static final Identifier.Button _26 = new Identifier.Button("26");
        public static final Identifier.Button _27 = new Identifier.Button("27");
        public static final Identifier.Button _28 = new Identifier.Button("28");
        public static final Identifier.Button _29 = new Identifier.Button("29");
        public static final Identifier.Button _30 = new Identifier.Button("30");
        public static final Identifier.Button _31 = new Identifier.Button("31");
        public static final Identifier.Button TRIGGER = new Identifier.Button("Trigger");
        public static final Identifier.Button THUMB = new Identifier.Button("Thumb");
        public static final Identifier.Button THUMB2 = new Identifier.Button("Thumb 2");
        public static final Identifier.Button TOP = new Identifier.Button("Top");
        public static final Identifier.Button TOP2 = new Identifier.Button("Top 2");
        public static final Identifier.Button PINKIE = new Identifier.Button("Pinkie");
        public static final Identifier.Button BASE = new Identifier.Button("Base");
        public static final Identifier.Button BASE2 = new Identifier.Button("Base 2");
        public static final Identifier.Button BASE3 = new Identifier.Button("Base 3");
        public static final Identifier.Button BASE4 = new Identifier.Button("Base 4");
        public static final Identifier.Button BASE5 = new Identifier.Button("Base 5");
        public static final Identifier.Button BASE6 = new Identifier.Button("Base 6");
        public static final Identifier.Button DEAD = new Identifier.Button("Dead");
        public static final Identifier.Button A = new Identifier.Button("A");
        public static final Identifier.Button B = new Identifier.Button("B");
        public static final Identifier.Button C = new Identifier.Button("C");
        public static final Identifier.Button X = new Identifier.Button("X");
        public static final Identifier.Button Y = new Identifier.Button("Y");
        public static final Identifier.Button Z = new Identifier.Button("Z");
        public static final Identifier.Button LEFT_THUMB = new Identifier.Button("Left Thumb");
        public static final Identifier.Button RIGHT_THUMB = new Identifier.Button("Right Thumb");
        public static final Identifier.Button LEFT_THUMB2 = new Identifier.Button("Left Thumb 2");
        public static final Identifier.Button RIGHT_THUMB2 = new Identifier.Button("Right Thumb 2");
        public static final Identifier.Button SELECT = new Identifier.Button("Select");
        public static final Identifier.Button MODE = new Identifier.Button("Mode");
        public static final Identifier.Button LEFT_THUMB3 = new Identifier.Button("Left Thumb 3");
        public static final Identifier.Button RIGHT_THUMB3 = new Identifier.Button("Right Thumb 3");
        public static final Identifier.Button TOOL_PEN = new Identifier.Button("Pen");
        public static final Identifier.Button TOOL_RUBBER = new Identifier.Button("Rubber");
        public static final Identifier.Button TOOL_BRUSH = new Identifier.Button("Brush");
        public static final Identifier.Button TOOL_PENCIL = new Identifier.Button("Pencil");
        public static final Identifier.Button TOOL_AIRBRUSH = new Identifier.Button("Airbrush");
        public static final Identifier.Button TOOL_FINGER = new Identifier.Button("Finger");
        public static final Identifier.Button TOOL_MOUSE = new Identifier.Button("Mouse");
        public static final Identifier.Button TOOL_LENS = new Identifier.Button("Lens");
        public static final Identifier.Button TOUCH = new Identifier.Button("Touch");
        public static final Identifier.Button STYLUS = new Identifier.Button("Stylus");
        public static final Identifier.Button STYLUS2 = new Identifier.Button("Stylus 2");
        public static final Identifier.Button UNKNOWN = new Identifier.Button("Unknown");
        public static final Identifier.Button BACK = new Identifier.Button("Back");
        public static final Identifier.Button EXTRA = new Identifier.Button("Extra");
        public static final Identifier.Button FORWARD = new Identifier.Button("Forward");
        public static final Identifier.Button LEFT = new Identifier.Button("Left");
        public static final Identifier.Button MIDDLE = new Identifier.Button("Middle");
        public static final Identifier.Button RIGHT = new Identifier.Button("Right");
        public static final Identifier.Button SIDE = new Identifier.Button("Side");

        public Button(String name) {
            super(name);
        }
    }

    public static class Axis extends Identifier {
        public static final Identifier.Axis X = new Identifier.Axis("x");
        public static final Identifier.Axis Y = new Identifier.Axis("y");
        public static final Identifier.Axis Z = new Identifier.Axis("z");
        public static final Identifier.Axis RX = new Identifier.Axis("rx");
        public static final Identifier.Axis RY = new Identifier.Axis("ry");
        public static final Identifier.Axis RZ = new Identifier.Axis("rz");
        public static final Identifier.Axis SLIDER = new Identifier.Axis("slider");
        public static final Identifier.Axis SLIDER_ACCELERATION = new Identifier.Axis("slider-acceleration");
        public static final Identifier.Axis SLIDER_FORCE = new Identifier.Axis("slider-force");
        public static final Identifier.Axis SLIDER_VELOCITY = new Identifier.Axis("slider-velocity");
        public static final Identifier.Axis X_ACCELERATION = new Identifier.Axis("x-acceleration");
        public static final Identifier.Axis X_FORCE = new Identifier.Axis("x-force");
        public static final Identifier.Axis X_VELOCITY = new Identifier.Axis("x-velocity");
        public static final Identifier.Axis Y_ACCELERATION = new Identifier.Axis("y-acceleration");
        public static final Identifier.Axis Y_FORCE = new Identifier.Axis("y-force");
        public static final Identifier.Axis Y_VELOCITY = new Identifier.Axis("y-velocity");
        public static final Identifier.Axis Z_ACCELERATION = new Identifier.Axis("z-acceleration");
        public static final Identifier.Axis Z_FORCE = new Identifier.Axis("z-force");
        public static final Identifier.Axis Z_VELOCITY = new Identifier.Axis("z-velocity");
        public static final Identifier.Axis RX_ACCELERATION = new Identifier.Axis("rx-acceleration");
        public static final Identifier.Axis RX_FORCE = new Identifier.Axis("rx-force");
        public static final Identifier.Axis RX_VELOCITY = new Identifier.Axis("rx-velocity");
        public static final Identifier.Axis RY_ACCELERATION = new Identifier.Axis("ry-acceleration");
        public static final Identifier.Axis RY_FORCE = new Identifier.Axis("ry-force");
        public static final Identifier.Axis RY_VELOCITY = new Identifier.Axis("ry-velocity");
        public static final Identifier.Axis RZ_ACCELERATION = new Identifier.Axis("rz-acceleration");
        public static final Identifier.Axis RZ_FORCE = new Identifier.Axis("rz-force");
        public static final Identifier.Axis RZ_VELOCITY = new Identifier.Axis("rz-velocity");
        public static final Identifier.Axis POV = new Identifier.Axis("pov");
        public static final Identifier.Axis UNKNOWN = new Identifier.Axis("unknown");

        protected Axis(String name) {
            super(name);
        }
    }
}
