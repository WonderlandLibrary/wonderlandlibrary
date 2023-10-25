package net.java.games.input;

public class Axis extends Component.Identifier {
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

    protected Axis(String name) {
        super(name);
    }
}
