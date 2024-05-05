package club.dortware.client.util.impl.combat.extras;

public final class Rotation {

    private final float rotationYaw;

    private final float rotationPitch;

    /**
     * Constructs a {@code Rotation} instance.
     * @param rotationYaw - The yaw
     * @param rotationPitch - The pitch
     */
    public Rotation(float rotationYaw, float rotationPitch) {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }
}
