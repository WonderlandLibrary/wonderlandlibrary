package de.tired.base.module.implementation.combat;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.guis.newclickgui.setting.ModeSetting;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.event.events.UpdateEvent;
import de.tired.base.event.events.VelocityEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@ModuleAnnotation(name = "NoVelocity", category = ModuleCategory.COMBAT, clickG = "Reduces Velocity")

public class NoVelocity extends Module {

    private final ModeSetting modeSetting = new ModeSetting("VelocityMode", this, new String[]{"MatrixBackTP", "Vulcan", "Cancel", "MatrixHorizontal", "Reduce", "Gamster"});

    public NumberSetting reduceStrenght = new NumberSetting("reduceStrenght", this, .5, .1, 1, .01, () -> modeSetting.getValue().equalsIgnoreCase("Reduce"));

    private int matrixTicks;

    private int velocityChance = 30;

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        if (!this.isState()) return;
        boolean didPush;
        switch (modeSetting.getValue()) {
            case "Gamster": {
                if (MC.objectMouseOver.entityHit != null) {
                    if (MC.thePlayer.hurtTime == 1) {
                        velocityChance += 1;
                    }
                    if (velocityChance >= 12) {
                        if (MC.thePlayer.hurtTime != 0) {
                            MC.thePlayer.speedInAir = (float) Math.max(0.12F * .04, 0.02F);
                            velocityChance = 0;
                        }
                    }
                }
            }
            case "Vulcan": {
                if (MC.thePlayer.hurtTime != 0) {
                    MC.thePlayer.motionZ *= 0.65;
                    MC.thePlayer.motionX *= 0.65;
                    MC.thePlayer.speedInAir = (float) Math.max(0.12F * .04, 0.02F);
                }
                break;
            }
            case "MatrixBackTP":

                if (MC.thePlayer.hurtTime != 0) {
                    matrixTicks++;
                } else {
                    matrixTicks = 0;
                }

                if (matrixTicks > 6) {
                    final float yaw = KillAura.getInstance().isState() ? KillAura.getInstance().serverRotations[0] : MC.thePlayer.rotationYaw;
                    MC.thePlayer.motionX = -Math.sin(Math.toRadians(yaw)) * .07;
                    MC.thePlayer.motionZ = Math.cos(Math.toRadians(yaw)) * .07;
                    didPush = true;
                } else {
                    didPush = false;
                }

                if (didPush) {
                    MC.thePlayer.motionY *= (MC.thePlayer.motionY) * .6F;
                }
                break;

            case "MatrixHorizontal": {
                final float yaw = KillAura.getInstance().isState() ? KillAura.getInstance().serverRotations[0] : MC.thePlayer.rotationYaw;
                double stator = 0.02;
                switch (MC.thePlayer.hurtTime) {
                    case 1:
                        stator += 0.1;
                        break;
                    case 2:
                        stator += 0.004;
                        break;
                }
                if (MC.thePlayer.hurtTime != 1 && MC.thePlayer.hurtTime != 2 && MC.thePlayer.hurtTime != 0) {
                    final double x = -Math.sin(yaw) * stator;
                    final double z = Math.cos(yaw) * stator;
                    MC.thePlayer.motionX = x;
                    MC.thePlayer.motionZ = z;

                }
                break;
            }
        }

    }

    @EventTarget
    public void onVelocity(VelocityEvent e) {

        if (!this.isState()) return;
        if (MC.objectMouseOver.entityHit != null) {
            if (modeSetting.getValue().equalsIgnoreCase("Gamster")) {
                if (velocityChance >= 12) {
                    e.setFlag(false);
                    e.setMotion(.1);
                }
            }
        }
        if (modeSetting.getValue().equalsIgnoreCase("Reduce")) {
            if (MC.thePlayer.hurtTime != 0) {
                e.setFlag(true);
                e.setSprint(true);
                e.setMotion(reduceStrenght.getValue());
            }
        }

    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        if ("Cancel".equals(modeSetting.getValue())) {
            if (e.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity velocity = (S12PacketEntityVelocity) e.getPacket();
                if (velocity.getEntityID() == MC.thePlayer.getEntityId()) {
                    e.setCancelled(true);
                }
            }
        }

    }

    @Override
    public void onState() {
        matrixTicks = 0;
    }

    @Override
    public void onUndo() {
        matrixTicks = 0;
        MC.thePlayer.speedInAir = .02f;
    }
}