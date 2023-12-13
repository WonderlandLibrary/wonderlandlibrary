package com.enjoytheban.utils.math;

import com.enjoytheban.utils.render.gl.GLUtils;

/**
 * A Vec with an X and Y position
 *
 * @author Brady
 * @since 2/12/2017 12:00 PM
 */
public final class Vec2f {

    /**
     * Coordinates of this Vec2
     */
    private float x, y;

    public Vec2f() {
        this(0, 0);
    }

    public Vec2f(Vec2f vec) {
        this(vec.x, vec.y);
    }

    public Vec2f(double x, double y) {
        this((float) x, (float) y);
    }

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the Vec2 X value
     *
     * @param x The new X value
     * @return This Vec2
     */
    public final Vec2f setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * Sets the Vec2 Y value
     *
     * @param y The new Y value
     * @return This Vec2
     */
    public final Vec2f setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * @return The Vec2 x value
     */
    public final float getX() {
        return this.x;
    }

    /**
     * @return The Vec2 y value
     */
    public final float getY() {
        return this.y;
    }

    /**
     * Adds the X and Y of one Vec2 to this Vec2
     *
     * @param vec Vec2 being added
     * @return The new Vec2
     */
    public final Vec2f add(Vec2f vec) {
        return new Vec2f(this.x + vec.x, this.y + vec.y);
    }

    /**
     * Adds the specified X and Y to this Vec2
     *
     * @param x X value being added
     * @param y Y value being added
     * @return The new Vec2
     */
    public final Vec2f add(double x, double y) {
        return add(new Vec2f(x, y));
    }

    /**
     * Adds the specified X and Y to this Vec2
     *
     * @param x X value being added
     * @param y Y value being added
     * @return The new Vec2
     */
    public final Vec2f add(float x, float y) {
        return add(new Vec2f(x, y));
    }

    /**
     * Subtracts the X and Y of one Vec3 from this Vec2
     *
     * @param vec Vec2 being subtracted by
     * @return The new Vec2
     */
    public final Vec2f sub(Vec2f vec) {
        return new Vec2f(this.x - vec.x, this.y - vec.y);
    }

    /**
     * Subtracts the specified X and Y from this Vec2
     *
     * @param x X value being subtracted
     * @param y Y value being subtracted
     * @return The new Vec2
     */
    public final Vec2f sub(double x, double y) {
        return sub(new Vec2f(x, y));
    }

    /**
     * Subtracts the specified X and Y from this Vec2
     *
     * @param x X value being subtracted
     * @param y Y value being subtracted
     * @return The new Vec2
     */
    public final Vec2f sub(float x, float y) {
        return sub(new Vec2f(x, y));
    }

    /**
     * Multiplies the X and Y of this Vec2 by a scale
     *
     * @param scale The scale
     * @return The new Vec2
     */
    public final Vec2f scale(float scale) {
        return new Vec2f(this.x * scale, this.y * scale);
    }

    /**
     * Creates a Vec3 from this Vec2
     *
     * @return This Vec2 as a Vec3
     */
    public final Vec3f toVec3() {
        return new Vec3f(x, y, 0);
    }

    /**
     * Creates a new Vec2 with the same X/Y as this Vec2
     *
     * @return the new Vec2
     */
    public final Vec2f copy() {
        return new Vec2f(this);
    }

    /**
     * Transfers the X/Y from another Vec2 and sets this
     * Vec2's X/Y to it
     *
     * @return This Vec2
     */
    public final Vec2f transfer(Vec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

    /**
     * Calculates the distance to another Vec2
     *
     * @return The distance
     */
    public final float distanceTo(Vec2f vec) {
        double dx = x - vec.x;
        double dy = y - vec.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Returns the world projected coordinates of this Vec3
     *
     * @return World projected Coordinates as a Vec3
     */
    public final Vec3f toScreen() {
        return GLUtils.toWorld(this.toVec3());
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
