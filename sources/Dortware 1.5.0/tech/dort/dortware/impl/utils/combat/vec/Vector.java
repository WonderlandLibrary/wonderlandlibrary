package tech.dort.dortware.impl.utils.combat.vec;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector3f;


public class Vector<T extends Number> {

    private T x, y, z;

    public Vector(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector setX(T x) {
        this.x = x;
        return this;
    }

    public Vector setY(T y) {
        this.y = y;
        return this;
    }

    public Vector setZ(T z) {
        this.z = z;
        return this;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    public T getZ() {
        return z;
    }

    public static class Vector2<T extends Number> extends Vector<Number> {

        public Vector2(T x, T y) {
            super(x, y, 0);
        }

        public Vector3<T> toVector3() {
            return new Vector3<>((T) getX(), ((T) getY()), ((T) getZ()));
        }
    }

    public static class Vector3<T extends Number> extends Vector<Number> {

        public Vector3(T x, T y, T z) {
            super(x, y, z);
        }

        public Vector2<T> toVector2() {
            return new Vector2<>(((T) getX()), ((T) getY()));
        }

    }

    public static class Vec3f extends Vector3f {

        public Vec3f(float x, float y, float z) {
            super(x, y, z);
        }

        public Vec3f(BlockPos pos) {
            super(pos.getX(), pos.getY(), pos.getZ());
        }

        public Vec3f(Vec3i vec) {
            super(vec.getX(), vec.getY(), vec.getZ());
        }

        public double squareDistanceTo(Vec3f vec) {
            double var2 = vec.x - this.x;
            double var4 = vec.y - this.y;
            double var5 = vec.z - this.z;
            return var2 * var2 + var4 * var4 + var5 * var5;
        }

        public Vec3f add(float x, float y, float z) {
            this.x += x;
            this.y += y;
            this.z += z;
            return this;
        }

        public Vec3f add(Vec3f vec) {
            this.x += vec.x;
            this.y += vec.y;
            this.z += vec.z;
            return this;
        }

        public Vec3f scale(float amount) {
            this.x *= amount;
            this.y *= amount;
            this.z *= amount;
            return this;
        }

        public Vec3f clone() {
            return new Vec3f(x, y, z);
        }

    }
}