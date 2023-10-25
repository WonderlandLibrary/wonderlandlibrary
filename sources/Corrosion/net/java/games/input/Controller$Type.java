package net.java.games.input;

public class Type {
    private final String name;
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

    protected Type(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
