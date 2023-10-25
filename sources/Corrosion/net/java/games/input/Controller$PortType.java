package net.java.games.input;

public final class PortType {
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
