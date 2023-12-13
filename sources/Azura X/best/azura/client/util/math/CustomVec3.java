package best.azura.client.util.math;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class CustomVec3 {

    private final long creationTime;
    private double x, y, z;

    public CustomVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.creationTime = System.currentTimeMillis();
    }

    public CustomVec3(Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
        this.creationTime = System.currentTimeMillis();
    }

    public CustomVec3(PathPoint point) {
        this.x = point.xCoord + 0.5;
        this.y = point.yCoord;
        this.z = point.zCoord + 0.5;
        this.creationTime = System.currentTimeMillis();
    }

    public CustomVec3(Entity entity) {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public CustomVec3(BlockPos pos) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public CustomVec3 offset(double x, double y, double z) {
        return new CustomVec3(this.x + x, this.y + y, this.z + z);
    }

    public CustomVec3 copy() {
        return new CustomVec3(this.x, this.y, this.z);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getDistance(CustomVec3 other) {
        double diffX = this.x - other.x;
        double diffY = this.y - other.y;
        double diffZ = this.z - other.z;
        return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CustomVec3) {
            CustomVec3 v = (CustomVec3) other;
            return x == v.getX() && y == v.getY() && z == v.getZ();
        }
        return false;
    }

    public BlockPos convertToBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    public double getDistance(Entity entity) {
        return getDistance(new CustomVec3(entity));
    }

    public long getCreationTime() {
        return creationTime;
    }
}