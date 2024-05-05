package tech.atani.client.utility.player;

public class PlayerHandler {

    public static float yaw, pitch, prevYaw, prevPitch;
    public static boolean shouldSprintReset, moveFix;
    public static MoveFixMode currentMode;

    public enum MoveFixMode {
        STRICT, SILENT, AGGRESSIVE
    }

}
