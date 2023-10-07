package best.azura.client.util.math;

import net.minecraft.util.Vec3;

public class Vec2 {

    private double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
    }

    public Vec2 offset(double x, double y, double z) {
        return new Vec2(this.x + x, this.y + y);
    }

    public Vec2 copy() {
        return new Vec2(this.x, this.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    public double getDistance(Vec2 other) {
        double diffX = this.x - other.x;
        double diffY = this.y - other.y;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Vec2) {
            Vec2 v = (Vec2) other;
            return x == v.getX() && y == v.getY();
        }
        return false;
    }
}