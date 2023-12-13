package net.minecraft.util;

public class MovementInput
{
    /**
     * The speed at which the player is strafing. Postive numbers to the left and negative to the right.
     */
    public static float moveStrafe; // was not static

    /**
     * The speed at which the player is moving forward. Negative numbers will move backwards.
     */
    public static float moveForward; // was not static
    public boolean jump;
    public boolean sneak;
    private static final String __OBFID = "CL_00000936";

    public void updatePlayerMoveState() {}
}
