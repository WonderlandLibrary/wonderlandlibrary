package dev.rise.util.player;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

@UtilityClass
public final class RotationUtil {

    /**
     * These are the rotations seen by the server.
     */
    public float serverYaw, serverPitch;

    public int uwu = 5;//0;

    /**
     * Smooths the current rotation using the last for it to make aura harder to flag.
     *
     * @param rotations     Current rotations.
     * @param lastRotations Last rotations.
     * @return Current rotation smoothed according to last.
     */
    public float[] getFixedRotation(final float[] rotations, final float[] lastRotations) {
        final Minecraft mc = Minecraft.getMinecraft();

        final float yaw = rotations[0];
        final float pitch = rotations[1];

        final float lastYaw = lastRotations[0];
        final float lastPitch = lastRotations[1];

        final float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        final float gcd = f * f * f * 1.2F;

        final float deltaYaw = yaw - lastYaw;
        final float deltaPitch = pitch - lastPitch;

        final float fixedDeltaYaw = deltaYaw - (deltaYaw % gcd);
        final float fixedDeltaPitch = deltaPitch - (deltaPitch % gcd);

        final float fixedYaw = lastYaw + fixedDeltaYaw;
        final float fixedPitch = lastPitch + fixedDeltaPitch;

        return new float[]{fixedYaw, fixedPitch};
    }

    /**
     * Limits the rotation on given limits.
     *
     * @param currRot   Current rotation.
     * @param targetRot The rotation wanted.
     * @param turnSpeed The maximum turn speed we allow.
     * @return The limited angle change.
     */
    public float[] limitAngleChange(final float[] currRot, final float[] targetRot, final float turnSpeed) {
        final float currentYaw = currRot[0];
        final float currentPitch = currRot[1];

        final float targetYaw = targetRot[0];
        final float targetPitch = targetRot[1];

        final float yawDifference = getAngleDifference(targetYaw, currentYaw);
        final float pitchDifference = getAngleDifference(targetPitch, currentPitch);

        final float limitedYaw = currentYaw + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed));
        final float limitedPitch = currentPitch + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed));

        return new float[]{limitedYaw, limitedPitch};
    }

    public float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

   /* public boolean validate() {
        try {
            final HttpsURLConnection connection =
                    (HttpsURLConnection) new URL("https://intent.store/product/25/whitelist?hwid=" + HWID.getHardwareID())
                            .openConnection();

            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String currentln;
            final ArrayList<String> response = new ArrayList<>();

            while ((currentln = in.readLine()) != null) {
                response.add(currentln);
            }

            if (!response.contains("true") || response.contains("false")) {
                for (int i = 0; i < 0; i = 2) {

                }
                return false;
            }
        } catch (final Exception e) {
            for (int i = 0; i < 0; i = 2) {

            }
            return false;
        }

        AuthGUI.sr = new ScaledResolution(Minecraft.getMinecraft());
        uwu = 5;

        return true;
    }*/

}
