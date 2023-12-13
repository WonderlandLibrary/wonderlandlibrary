package wtf.diablo.events.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import wtf.diablo.events.Event;

@Getter
@Setter
public class MoveEvent extends Event {
    public double x;
    public double y;
    public double z;
    private boolean ground;

    public MoveEvent(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public void setZ(final double z) {
        this.z = z;
    }

    public void setMoveSpeed(double moveSpeed) {
        Minecraft mc = Minecraft.getMinecraft();
        MovementInput movementInput = mc.thePlayer.movementInput;
        double moveForward = movementInput.moveForward;
        double moveStrafe = movementInput.moveStrafe;
        double yaw = mc.thePlayer.rotationYaw;
        double modifier = moveForward == 0.0F ? 90.0F : moveForward < 0.0F ? -45.0F : 45.0F;
        boolean moving = moveForward != 0 || moveStrafe != 0;

        yaw += moveForward < 0.0F ? 180.0F : 0.0F;

        if (moveStrafe < 0.0F) {
            yaw += modifier;
        } else if (moveStrafe > 0.0F) {
            yaw -= modifier;
        }

        if (moving) {
            setX(-(MathHelper.sin((float) Math.toRadians(yaw)) * moveSpeed));
            setZ(MathHelper.cos((float) Math.toRadians(yaw)) * moveSpeed);
        } else {
            setX(0);
            setZ(0);
        }
    }
}

