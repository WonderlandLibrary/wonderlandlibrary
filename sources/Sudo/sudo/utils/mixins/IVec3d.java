package sudo.utils.mixins;

import net.minecraft.util.math.Vec3i;
import sudo.utils.misc.Vec3;

public interface IVec3d {
    void set(double x, double y, double z);

    default void set(Vec3i vec) {
        set(vec.getX(), vec.getY(), vec.getZ());
    }
    default void set(Vec3 vec) {
        set(vec.x, vec.y, vec.z);
    }
    void setXZ(double x, double z);

    void setY(double y);
}
