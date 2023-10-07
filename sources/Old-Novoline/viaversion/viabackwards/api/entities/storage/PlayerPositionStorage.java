package viaversion.viabackwards.api.entities.storage;

import viaversion.viaversion.api.PacketWrapper;
import viaversion.viaversion.api.data.StoredObject;
import viaversion.viaversion.api.data.UserConnection;
import viaversion.viaversion.api.type.Type;

public abstract class PlayerPositionStorage extends StoredObject {
    private double x;
    private double y;
    private double z;

    protected PlayerPositionStorage(UserConnection user) {
        super(user);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setCoordinates(PacketWrapper wrapper, boolean relative) throws Exception {
        setCoordinates(wrapper.get(Type.DOUBLE, 0), wrapper.get(Type.DOUBLE, 1), wrapper.get(Type.DOUBLE, 2), relative);
    }

    public void setCoordinates(double x, double y, double z, boolean relative) {
        if (relative) {
            this.x += x;
            this.y += y;
            this.z += z;
        } else {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
