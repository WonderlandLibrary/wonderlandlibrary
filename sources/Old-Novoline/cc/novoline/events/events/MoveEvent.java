package cc.novoline.events.events;

import cc.novoline.Novoline;
import cc.novoline.modules.combat.KillAura;
import cc.novoline.modules.move.TargetStrafe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;

/**
 * @author Gastgame
 * govno jopa barebuh suka pidor jopa
 */

public class MoveEvent implements Event {

    private double x, y, z;

    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setMoveSpeed(double moveSpeed) {
        Novoline novoline = Novoline.getInstance();
        Minecraft mc = Minecraft.getInstance();
        KillAura killAura = novoline.getModuleManager().getModule(KillAura.class);
        TargetStrafe targetStrafe = novoline.getModuleManager().getModule(TargetStrafe.class);
        MovementInput movementInput = mc.player.movementInput();
        double moveForward = movementInput.getMoveForward();
        double moveStrafe = movementInput.getMoveStrafe();
        double yaw = mc.player.rotationYaw;
        double modifier = moveForward == 0.0F ? 90.0F : moveForward < 0.0F ? -45.0F : 45.0F;
        boolean moving = moveForward != 0 || moveStrafe != 0;

        yaw += moveForward < 0.0F ? 180.0F : 0.0F;

        if (moveStrafe < 0.0F) {
            yaw += modifier;
        } else if (moveStrafe > 0.0F) {
            yaw -= modifier;
        }

        if (moving) {
            if (targetStrafe.isEnabled() && killAura.isEnabled() && killAura.getTarget() != null && killAura.shouldAttack() && targetStrafe.shouldTarget()) {
                targetStrafe.circleStrafe(this, moveSpeed, killAura.getTarget());
            } else {
                setX(-(MathHelper.sin(Math.toRadians(yaw)) * moveSpeed));
                setZ(MathHelper.cos(Math.toRadians(yaw)) * moveSpeed);
            }
        } else {
            setX(0);
            setZ(0);
        }
    }
}
