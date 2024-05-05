package tech.dort.dortware.impl.utils.combat.vec.vectors;

import org.lwjgl.util.vector.Vector2f;

public class Vec2f extends Vector2f {

    public Vec2f(float x, float y) {
        super(x, y);
    }

    public Vec2f() {
        super(0, 0);
    }

    public Vec2f add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2f add(int x, int y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vec2f add(Vec2f vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vec2f subtract(Vec2f pos) {
        this.x -= pos.x;
        this.y -= pos.y;
        return this;
    }

    public Vec2f subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2f subtract(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2f subtract(int x, int y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vec2f multiply() {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2f multiply(Vec2f vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    public Vec2f multiply(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vec2f set(Vec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

    public double squareDistanceTo(Vec2f vec) {
        double var2 = vec.x - this.x;
        double var4 = vec.y - this.y;
        return var2 * var2 + var4 * var4;
    }

//    public Vec2f divide(float number) {
//        this.x /= number;
//        this.y /= number;
//        return this;
//    }

    public Vec2f clone() {
        return new Vec2f(x, y);
    }

}