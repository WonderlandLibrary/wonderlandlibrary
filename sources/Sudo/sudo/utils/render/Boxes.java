package sudo.utils.render;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;

public class Boxes {

    public static Vec3d getMinVec(Box box) {
        return new Vec3d(box.minX, box.minY, box.minZ);
    }

    public static Vec3d getMaxVec(Box box) {
        return new Vec3d(box.maxX, box.maxY, box.maxZ);
    }

    public static Box moveToZero(Box box) {
        return box.offset(getMinVec(box).negate());
    }

    public static double getCornerLength(Box box) {
        return getMinVec(box).distanceTo(getMaxVec(box));
    }

    public static double getAxisLength(Box box, Axis axis) {
        return box.getMax(axis) - box.getMin(axis);
    }

    public static Box multiply(Box box, double amount) {
        return multiply(box, amount, amount, amount);
    }

    public static Box multiply(Box box, double x, double y, double z) {
        return box.expand(
                getAxisLength(box, Axis.X) * (x - 1) / 2d,
                getAxisLength(box, Axis.Y) * (y - 1) / 2d,
                getAxisLength(box, Axis.Z) * (z - 1) / 2d);
    }
}
