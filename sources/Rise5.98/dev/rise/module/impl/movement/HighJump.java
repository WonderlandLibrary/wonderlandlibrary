/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@ModuleInfo(name = "HighJump", description = "Shoots you into the sky", category = Category.MOVEMENT)
public final class HighJump extends Module {

    private boolean isInBoat;
    private int ticks, offGroundTicks;
    private Integer slot;
    private float oPositionY;

    private final ModeSetting mode = new ModeSetting("Mode", this, "Fireball", "Fireball", "Boat");
    private final NumberSetting timerSpeed = new NumberSetting("Timer Speed", this, 1, 0.1, 10.0, 0.1);
    private final NumberSetting verticalMotion = new NumberSetting("Vertical Motion", this, 0.25, 0.0, 8.0, 0.01);
    private final NumberSetting horizontalMotion = new NumberSetting("Horizontal Motion", this, 0.25, 0.0, 8.0, 0.01);
    private final BooleanSetting smoothCamera = new BooleanSetting("Smooth Camera", this, false);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, true);

    @Override
    protected void onEnable() {
        if (mode.is("Fireball") && !mc.thePlayer.onGround) {
            this.registerNotification("Cannot enable " + this.getModuleInfo().name() + " in air.");
            this.toggleModule();
        }

        isInBoat = false;
        slot = null;
        ticks = offGroundTicks = 0;
        oPositionY = (float) mc.thePlayer.posY;
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
        EntityPlayer.enableCameraYOffset = false;
    }

    @Override
    public void onUpdateAlwaysInGui() {
        verticalMotion.hidden = horizontalMotion.hidden = !mode.is("Boat");

        timerSpeed.hidden = !mode.is("Fireball");
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        ticks++;

        if (mc.thePlayer.onGround) {
            if (autoDisable.isEnabled() && offGroundTicks > 0) {
                this.toggleModule();
                return;
            }

            offGroundTicks = 0;
        } else {
            offGroundTicks += 1;
        }

        EntityPlayer.enableCameraYOffset = false;

        if (smoothCamera.isEnabled()) {
            if (mc.thePlayer.posY > oPositionY) {
                EntityPlayer.enableCameraYOffset = true;
                EntityPlayer.cameraYPosition = oPositionY + 0.42;
            }
        }

        switch (mode.getMode()) {
            case "Fireball":
                event.setPitch((float) (90 - Math.random() * 2));
                switch (ticks) {
                    case 2:
                        if (mc.thePlayer.hurtTime <= 0) {
                            slot = PlayerUtil.findItem(Items.fire_charge);

                            if (slot == null)
                                return;

                            if (slot != mc.thePlayer.inventory.currentItem) {
                                PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
                                Rise.addChatMessage(slot);
                            }
                        }
                        break;

                    case 3:
                        if (slot != null && mc.thePlayer.hurtTime <= 0) {
                            PacketUtil.sendPacket(new C09PacketHeldItemChange(slot));
                            PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getStackInSlot(slot)));
                        }
                        break;

                    case 4:
                        if (slot != null && slot != mc.thePlayer.inventory.currentItem && mc.thePlayer.hurtTime <= 0)
                            PacketUtil.sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                        break;

                    case 5:
                        mc.thePlayer.motionY += 0.2 + (Math.random() / 500);

                        if (slot == null) return;
                        mc.thePlayer.motionY = 3.9 + (Math.random() / 500);
                        MoveUtil.strafe(1.9 + (Math.random() / 500));
                        break;
                }

                if (ticks > 4) {
                    if (mc.thePlayer.fallDistance <= 0) {
                        mc.thePlayer.motionY += 0.027 - (Math.random() / 500);
                        mc.thePlayer.speedInAir = (float) (0.023f - (Math.random() / 500));
                    } else if (mc.thePlayer.fallDistance < 3) {
                        mc.thePlayer.motionY += 0.0235 - (Math.random() / 500);
                        mc.thePlayer.speedInAir = 0.02f;
                    }

                    if (mc.thePlayer.fallDistance != 0 && mc.thePlayer.hurtTime == 9) {
                        mc.thePlayer.motionY += 0.0265 - (Math.random() / 500);
                    }

                    if (mc.thePlayer.fallDistance > 4) {
                        mc.thePlayer.fallDistance = 0;
                        event.setGround(true);
                    }

                    if (timerSpeed.getValue() != 1)
                        mc.timer.timerSpeed = (float) (timerSpeed.getValue() - Math.random() / 100);
                }

                MoveUtil.strafe();
                break;
        }
    }

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (mc.thePlayer.ridingEntity != null) {
            isInBoat = true;
        }

        if (!isInBoat)
            switch (mode.getMode()) {
                case "Boat":
                    mc.thePlayer.motionY = verticalMotion.getValue();
                    MoveUtil.forward(horizontalMotion.getValue());
                    isInBoat = true;
                    break;
            }
    }
}
