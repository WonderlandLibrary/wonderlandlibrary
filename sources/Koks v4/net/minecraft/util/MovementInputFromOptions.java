package net.minecraft.util;

import god.buddy.aot.BCompiler;
import koks.api.Methods;
import koks.event.UpdatePlayerMovementState;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput implements Methods {
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn) {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState() {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --this.moveStrafe;
        }

        final UpdatePlayerMovementState updatePlayerMovementState = new UpdatePlayerMovementState(moveForward, moveStrafe, getYaw(), getPlayer().rotationYaw, false, false, this.gameSettings.keyBindSneak.isKeyDown()).onFire();
        moveForward = updatePlayerMovementState.getMoveForward();
        moveStrafe = updatePlayerMovementState.getMoveStrafe();

        if(updatePlayerMovementState.isSilentMoveFix() && (moveForward != 0 || moveStrafe != 0)) {
            getCorrectedMovement(moveForward, moveStrafe, updatePlayerMovementState.getYaw(), updatePlayerMovementState.getShouldYaw(), updatePlayerMovementState.isFixYaw());
        }

        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = updatePlayerMovementState.isSneak();

        if (this.sneak) {
            this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
            this.moveForward = (float) ((double) this.moveForward * 0.3D);
        }
    }

    /**
     * This method takes the current movement input and an rotation yaw and returns needed movement input with the closest distance to the to the real yaw and given yaw delta.
     *
     * @author Dirrrrrrrrrrrrrrrrrrrt
     * @param forward The current forward input.
     * @param strafe  The current strafe input.
     * @param yaw     The yaw to use for the calculation.
     * @return An float array containing the right forward and strafe input.
     */
    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    public void getCorrectedMovement(float forward, float strafe, float yaw, float shouldYaw, boolean fixYaw) {
        float y = getPlayer().rotationYaw;

        if(fixYaw)
            y = shouldYaw;

        if(fixYaw && yaw == y) return;

        // this is just the fraction stuff of mc
        float f4 = 0.91F;
        if (getPlayer().onGround) {
            f4 = getWorld().getBlockState(new BlockPos(MathHelper.floor_double(getPlayer().posX), MathHelper.floor_double(getPlayer().getEntityBoundingBox().minY) - 1, MathHelper.floor_double(getPlayer().posZ))).getBlock().slipperiness * 0.91F;
        }
        float f5;
        if (getPlayer().onGround) {
            f5 = getPlayer().getAIMoveSpeed() * (0.16277136F / (f4 * f4 * f4));
        } else {
            f5 = getPlayer().jumpMovementFactor;
        }
        float f = strafe * strafe + forward * forward;
        f = f5 / f;

        float fStrafe = strafe * f;
        float fForward = forward * f;
        float realYawSin = MathHelper.sin(y * (float) Math.PI / 180.0F);
        float realYawCos = MathHelper.cos(y * (float) Math.PI / 180.0F);
        // these are the correct motion values for the current rotation (ClientSide)
        float realYawMotionX = fStrafe * realYawCos - fForward * realYawSin;
        float realYawMotionZ = fForward * realYawCos + fStrafe * realYawSin;

        float rotationYawSin = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
        float rotationYawCos = MathHelper.cos(yaw * (float) Math.PI / 180.0F);

        // NaN is just used for the initial usage of the array
        float[] closest = new float[]{Float.NaN, 0, 0};

        // now go thought -1, 0 and 1 and check for the value with the lowest distance to the correct motion values
        for (int possibleStrafe = -1; possibleStrafe <= 1; possibleStrafe++) {
            for (int possibleForward = -1; possibleForward <= 1; possibleForward++) {
                float testFStrafe = possibleStrafe * f;
                float testFForward = possibleForward * f;
                float testYawMotionX = testFStrafe * rotationYawCos - testFForward * rotationYawSin;
                float testYawMotionZ = testFForward * rotationYawCos + testFStrafe * rotationYawSin;
                // calculate the distance between the test motion and the real motion
                float diffX = realYawMotionX - testYawMotionX;
                float diffZ = realYawMotionZ - testYawMotionZ;
                float distance = MathHelper.sqrt_float(diffX * diffX + diffZ * diffZ);
                // compare the distance to the stored one to fine the nearest forward and strafe values
                if (Float.isNaN(closest[0]) || distance < closest[0]) {
                    closest[0] = distance;
                    closest[1] = possibleForward;
                    closest[2] = possibleStrafe;
                }
            }
        }
        moveForward = closest[1];
        moveStrafe = closest[2];
    }
}
