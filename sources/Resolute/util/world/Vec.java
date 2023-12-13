// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.world;

import java.util.concurrent.ThreadLocalRandom;

public class Vec
{
    public double x;
    public double y;
    public double z;
    
    void init(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec() {
    }
    
    public Vec(final double x) {
        this.init(x, 0.0, 0.0);
    }
    
    public Vec(final double x, final double y) {
        this.init(x, y, 0.0);
    }
    
    public Vec(final double x, final double y, final double z) {
        this.init(x, y, z);
    }
    
    public Vec(final Vec vec) {
        this.init(vec.x, vec.y, vec.z);
    }
    
    public static Vec random() {
        final double rand_number = Math.random() * 6.283185307179586;
        return new Vec(Math.cos(rand_number), Math.sin(rand_number));
    }
    
    public static Vec random(final double xMin, final double xMax) {
        final double x = ThreadLocalRandom.current().nextDouble(xMin, xMax);
        return new Vec(x, 0.0);
    }
    
    public static Vec random(final double xMin, final double xMax, final double yMin, final double yMax) {
        final double x = ThreadLocalRandom.current().nextDouble(xMin, xMax);
        final double y = ThreadLocalRandom.current().nextDouble(yMin, yMax);
        return new Vec(x, y);
    }
    
    public static Vec random(final double xMin, final double xMax, final double yMin, final double yMax, final double zMin, final double zMax) {
        final double x = ThreadLocalRandom.current().nextDouble(xMin, xMax);
        final double y = ThreadLocalRandom.current().nextDouble(yMin, yMax);
        final double z = ThreadLocalRandom.current().nextDouble(zMin, zMax);
        return new Vec(x, y, z);
    }
    
    public Vec randomize(final double xMin, final double xMax) {
        this.set(random(xMin, xMax));
        return this;
    }
    
    public Vec randomize(final double xMin, final double xMax, final double yMin, final double yMax) {
        this.set(random(xMin, xMax, yMin, yMax));
        return this;
    }
    
    public Vec randomize(final double xMin, final double xMax, final double yMin, final double yMax, final double zMin, final double zMax) {
        this.set(random(xMin, xMax, yMin, yMax, zMin, zMax));
        return this;
    }
    
    public Vec set(final double x) {
        this.x = x;
        return this;
    }
    
    public Vec set(final double x, final double y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vec set(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }
    
    public Vec set(final Vec vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }
    
    public Vec add(final double x) {
        this.x += x;
        return this;
    }
    
    public Vec add(final double x, final double y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vec add(final double x, final double y, final double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vec add(final Vec vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }
    
    public Vec sub(final double x) {
        this.x -= x;
        return this;
    }
    
    public Vec sub(final double x, final double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vec sub(final double x, final double y, final double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }
    
    public Vec sub(final Vec vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }
    
    public Vec mul(final double x) {
        this.x *= x;
        return this;
    }
    
    public Vec mul(final double x, final double y) {
        this.x *= x;
        this.y *= y;
        return this;
    }
    
    public Vec mul(final double x, final double y, final double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }
    
    public Vec mul(final Vec vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }
    
    public Vec div(final double x) {
        this.x /= x;
        return this;
    }
    
    public Vec div(final double x, final double y) {
        this.x /= x;
        this.y /= y;
        return this;
    }
    
    public Vec div(final double x, final double y, final double z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }
    
    public Vec div(final Vec vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
        return this;
    }
    
    public double dot(final double x) {
        return this.x * x;
    }
    
    public double dot(final double x, final double y) {
        return this.x * x + this.y * y;
    }
    
    public double dot(final double x, final double y, final double z) {
        return this.x * x + this.y * y + this.z * z;
    }
    
    public double dot(final Vec vec) {
        return this.dot(vec.x, vec.y, vec.z);
    }
    
    public double cross(final double x, final double y) {
        return this.x * y - this.y * x;
    }
    
    public Vec cross(final double x, final double y, final double z) {
        final double xCross = this.y * z - this.z * y;
        final double yCross = this.z * x - this.x * z;
        final double zCross = this.x * y - this.y * x;
        return new Vec(xCross, yCross, zCross);
    }
    
    public Vec cross(final Vec vec) {
        return this.cross(vec.x, vec.y, vec.z);
    }
    
    public static Vec fromAngle(final double angle) {
        final double x = Math.cos(angle);
        final double y = Math.sin(angle);
        final double z = Math.tan(angle);
        return new Vec(x, y, z);
    }
    
    public Vec normalize() {
        final double magnitude = this.getMagnitude();
        if (magnitude != 0.0 && magnitude != 1.0) {
            this.div(magnitude, magnitude, magnitude);
        }
        return this;
    }
    
    public Vec limit(final double limit) {
        if (this.getMagnitude() > limit * limit) {
            this.normalize();
            this.mul(limit, limit, limit);
        }
        return this;
    }
    
    public double getMagnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public double getMagnitudeSq() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public Vec setMagnitude(final double magnitude) {
        this.normalize();
        this.mul(magnitude, magnitude, magnitude);
        return this;
    }
    
    public Vec clamp(final double xMin, final double xMax) {
        this.x = ((this.x < xMin) ? xMin : ((this.x > xMax) ? xMax : this.x));
        return this;
    }
    
    public Vec clamp(final double xMin, final double xMax, final double yMin, final double yMax) {
        this.x = ((this.x < xMin) ? xMin : ((this.x > xMax) ? xMax : this.x));
        this.y = ((this.y < yMin) ? yMin : ((this.y > yMax) ? yMax : this.y));
        return this;
    }
    
    public Vec clamp(final double xMin, final double xMax, final double yMin, final double yMax, final double zMin, final double zMax) {
        this.x = ((this.x < xMin) ? xMin : ((this.x > xMax) ? xMax : this.x));
        this.y = ((this.y < yMin) ? yMin : ((this.y > yMax) ? yMax : this.y));
        this.z = ((this.z < zMin) ? zMin : ((this.z > zMax) ? zMax : this.z));
        return this;
    }
    
    public Vec clamp(final Vec min, final Vec max) {
        this.x = ((this.x < min.x) ? min.x : ((this.x > max.x) ? max.x : this.x));
        this.y = ((this.y < min.y) ? min.y : ((this.y > max.y) ? max.y : this.y));
        this.z = ((this.z < min.z) ? min.z : ((this.z > max.z) ? max.z : this.z));
        return this;
    }
    
    public Vec scale(final double n) {
        this.x *= n;
        this.y *= n;
        this.z *= n;
        return this;
    }
    
    public Vec clone() {
        return new Vec(this);
    }
    
    public Vec mirror() {
        return new Vec(this.x * -1.0, this.y * -1.0, this.z * -1.0);
    }
    
    public Vec pos() {
        return new Vec(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
    }
    
    public Vec neg() {
        return new Vec(Math.abs(this.x) * -1.0, Math.abs(this.y) * -1.0, Math.abs(this.z) * -1.0);
    }
    
    public Vec center() {
        return new Vec(this.x / 2.0, this.y / 2.0, this.z / 2.0);
    }
    
    public Vec zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }
    
    public double direction() {
        return Math.atan2(this.y, this.x);
    }
    
    public double distance(final double x) {
        final double xDiff = this.x - x;
        return Math.sqrt(xDiff * xDiff);
    }
    
    public double distance(final double x, final double y) {
        final double xDiff = this.x - x;
        final double yDiff = this.y - y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    
    public double distance(final double x, final double y, final double z) {
        final double xDiff = this.x - x;
        final double yDiff = this.y - y;
        final double zDiff = this.z - z;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff + zDiff);
    }
    
    public double distance(final Vec vec) {
        return this.distance(vec.x, vec.y, vec.z);
    }
    
    public boolean equal(final double x) {
        return this.x == x;
    }
    
    public boolean equal(final double x, final double y) {
        return this.x == x && this.y == y;
    }
    
    public boolean equal(final double x, final double y, final double z) {
        return this.x == x && this.y == y && this.z == z;
    }
    
    public boolean equal(final Vec vec) {
        return this.equal(vec.x, vec.y, vec.z);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public double[] asArray() {
        return new double[] { this.x, this.y, this.z };
    }
    
    @Override
    public String toString() {
        return String.format("{%s, %s, %s}", this.x, this.y, this.z);
    }
}
