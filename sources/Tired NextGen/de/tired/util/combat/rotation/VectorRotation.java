package de.tired.util.combat.rotation;


import net.minecraft.util.Vec3;

public final class VectorRotation extends Rotation {

    private Vec3 vector;

    public VectorRotation(final Rotation rotation, final Vec3 vector) {
        super(rotation.getYaw(), rotation.getPitch());

        this.vector = vector;
    }

    public Vec3 getVector() {
        return vector;
    }

    public void setVector(final Vec3 vector) {
        this.vector = vector;
    }

}