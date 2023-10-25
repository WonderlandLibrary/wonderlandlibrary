/*
 * Decompiled with CFR 0.150.
 */
package net.java.games.input;

import net.java.games.input.Component;
import net.java.games.input.EventQueue;
import net.java.games.input.Rumbler;

public interface Controller {
    public Controller[] getControllers();

    public Type getType();

    public Component[] getComponents();

    public Component getComponent(Component.Identifier var1);

    public Rumbler[] getRumblers();

    public boolean poll();

    public void setEventQueueSize(int var1);

    public EventQueue getEventQueue();

    public PortType getPortType();

    public int getPortNumber();

    public String getName();

    public static final class PortType {
        private final String name;
        public static final PortType UNKNOWN = new PortType("Unknown");
        public static final PortType USB = new PortType("USB port");
        public static final PortType GAME = new PortType("Game port");
        public static final PortType NETWORK = new PortType("Network port");
        public static final PortType SERIAL = new PortType("Serial port");
        public static final PortType I8042 = new PortType("i8042 (PS/2)");
        public static final PortType PARALLEL = new PortType("Parallel port");

        protected PortType(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    public static class Type {
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
}

