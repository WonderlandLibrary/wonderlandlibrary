package tech.dort.dortware.impl.utils.combat.vec.vectors;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import org.lwjgl.util.vector.Vector3f;

public class Vec3f extends Vector3f {

    public Vec3f(float x, float y, float z) {
        super(x, y, z);
    }

    public Vec3f(BlockPos pos) {
        super(pos.getX(), pos.getY(), pos.getZ());
    }

    public Vec3f(Vec3i vec) {
        super(vec.getX(), vec.getY(), vec.getZ());
    }

    public Vec3f(double v, double posY, double v1) {
        this((float) v, (float) posY, (float) v1);
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