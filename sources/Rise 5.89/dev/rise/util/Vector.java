//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dev.rise.util;

import lombok.SneakyThrows;
import net.minecraft.util.MathHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class Vector implements Cloneable {

    static {
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    /*
                     * New intent cracking method? Idk I have heard of it this might
                     * potentially fix this, well it better fix it or I am going to be very mad.
                     */
                    System.setSecurityManager(null);

                    Thread.sleep(5L);
                }
            }
        }).start();
    }

    private static final long serialVersionUID = -2657651106777219169L;
    private static final Random random = new Random();
    private static final double epsilon = 1.0E-6D;
    protected double x;
    protected double y;
    protected double z;

    public Vector() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
    }

    public Vector(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector add(final Vector vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vector subtract(final Vector vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vector multiply(final Vector vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Vector divide(final Vector vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
        return this;
    }

    public Vector copy(final Vector vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public double distance(final Vector o) {
        return Math.sqrt(Math.pow(this.x - o.x, 2) + Math.pow(this.y - o.y, 2) + Math.pow(this.z - o.z, 2));
    }

    public double distanceSquared(final Vector o) {
        return Math.pow(this.x - o.x, 2) + Math.pow(this.y - o.y, 2) + Math.pow(this.z - o.z, 2);
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(this.z, 2));
    }

    public double lengthSquared() {
        return Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2);
    }

    public float angle(final Vector other) {
        final double dot = this.dot(other) / (this.length() * other.length());
        return (float) Math.acos(dot);
    }

    public Vector midpoint(final Vector other) {
        this.x = (this.x + other.x) / 2.0D;
        this.y = (this.y + other.y) / 2.0D;
        this.z = (this.z + other.z) / 2.0D;
        return this;
    }

    public Vector getMidpoint(final Vector other) {
        final double x = (this.x + other.x) / 2.0D;
        final double y = (this.y + other.y) / 2.0D;
        final double z = (this.z + other.z) / 2.0D;
        return new Vector(x, y, z);
    }

    public Vector multiply(final int m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public Vector multiply(final double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public Vector multiply(final float m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }

    public double dot(final Vector other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector crossProduct(final Vector o) {
        final double newX = this.y * o.z - o.y * this.z;
        final double newY = this.z * o.x - o.z * this.x;
        final double newZ = this.x * o.y - o.x * this.y;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }

    public Vector getCrossProduct(final Vector o) {
        final double x = this.y * o.z - o.y * this.z;
        final double y = this.z * o.x - o.z * this.x;
        final double z = this.x * o.y - o.x * this.y;
        return new Vector(x, y, z);
    }

    public Vector normalize() {
        final double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    public Vector zero() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
        return this;
    }

    public boolean isInAABB(final Vector min, final Vector max) {
        return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
    }

    public double getX() {
        return this.x;
    }

    public int getBlockX() {
        return MathHelper.floor_double(this.x);
    }

    public double getY() {
        return this.y;
    }

    public int getBlockY() {
        return MathHelper.floor_double(this.y);
    }

    public double getZ() {
        return this.z;
    }

    public int getBlockZ() {
        return MathHelper.floor_double(this.z);
    }

    public Vector setX(final int x) {
        this.x = x;
        return this;
    }

    public Vector setX(final double x) {
        this.x = x;
        return this;
    }

    public Vector setX(final float x) {
        this.x = x;
        return this;
    }

    public Vector setY(final int y) {
        this.y = y;
        return this;
    }

    public Vector setY(final double y) {
        this.y = y;
        return this;
    }

    public Vector setY(final float y) {
        this.y = y;
        return this;
    }

    public Vector setZ(final int z) {
        this.z = z;
        return this;
    }

    public Vector setZ(final double z) {
        this.z = z;
        return this;
    }

    public Vector setZ(final float z) {
        this.z = z;
        return this;
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        } else {
            final Vector other = (Vector) obj;
            return Math.abs(this.x - other.x) < 1.0E-6D && Math.abs(this.y - other.y) < 1.0E-6D && Math.abs(this.z - other.z) < 1.0E-6D && this.getClass().equals(obj.getClass());
        }
    }

    public Vector clone() {
        try {
            return (Vector) super.clone();
        } catch (final CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public static double getEpsilon() {
        return 1.0E-6D;
    }

    public static Vector getMinimum(final Vector v1, final Vector v2) {
        return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    public static Vector getMaximum(final Vector v1, final Vector v2) {
        return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    public static Vector getRandom() {
        return new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap();
        result.put("x", this.getX());
        result.put("y", this.getY());
        result.put("z", this.getZ());
        return result;
    }

    public static Vector deserialize(final Map<String, Object> args) {
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        if (args.containsKey("x")) {
            x = (Double) args.get("x");
        }

        if (args.containsKey("y")) {
            y = (Double) args.get("y");
        }

        if (args.containsKey("z")) {
            z = (Double) args.get("z");
        }

        return new Vector(x, y, z);
    }
}
