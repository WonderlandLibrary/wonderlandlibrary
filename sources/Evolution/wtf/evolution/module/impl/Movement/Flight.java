package wtf.evolution.module.impl.Movement;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventMotion;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.helpers.MovementUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

@ModuleInfo(name = "Flight", type = Category.Movement)
public class Flight extends Module {


    public ModeSetting mode = new ModeSetting("Mode", "Creative", "Creative", "Matrix Damage", "Matrix Jump", "Motion", "Big Grief", "Sunrise XD", "Really World");
    public SliderSetting height = new SliderSetting("Height", 1, 0, 9, 0.1f).setHidden(() -> !mode.is("Matrix Damage"));
    public SliderSetting speed = new SliderSetting("Speed", 1.9f, 0f, 1.9f, 0.1f).setHidden(() -> !mode.is("Matrix Damage"));
    public SliderSetting height2 = new SliderSetting("Height", 1, 0, 9, 0.1f).setHidden(() -> !mode.is("Matrix Jump"));
    public SliderSetting speed2 = new SliderSetting("Speed", 1.9f, 0f, 1.9f, 0.1f).setHidden(() -> !mode.is("Matrix Jump"));
    public SliderSetting MotionSpeed = new SliderSetting("Speed", 1f, 0.1f, 10f, 0.1f).setHidden(() -> !mode.is("Motion"));

    public Flight() {
        addSettings(mode, height, speed,height2, speed2, MotionSpeed);
    }


    @EventTarget
    public void onReceive(EventPacket e) {
        //кто спиздит у того мать больна спидом.
        //@copyright by LIPOPSKIY.
        if (mode.get().equalsIgnoreCase("Matrix Damage")) {
            if (e.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
                mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
                e.cancel();
                toggle();
            }
        }
        if (mode.get().equalsIgnoreCase("Matrix Jump")) {
            if (e.getPacket() instanceof SPacketPlayerPosLook) {
                SPacketPlayerPosLook packet = (SPacketPlayerPosLook) e.getPacket();
                mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
                mc.player.setPosition(packet.getX(), packet.getY(), packet.getZ());
                e.cancel();
                toggle();
            }
        }

    }



    @EventTarget
    public void onUpdate(EventUpdate e) {
        //кто спиздит у того мать больна спидом.
        //@copyright by LIPOPSKIY.
        setSuffix(mode.get());
        if (mode.get().equalsIgnoreCase("Matrix Damage")) {
            if (mc.player.hurtTime > 0) {
                if (mc.player.onGround) {
                    if (MovementUtil.isMoving()) {
                        mc.player.jump();
                    }
                } else {
                    if (MovementUtil.isMoving()) {
                        mc.player.motionY = height.get();
                        MovementUtil.setSpeed(speed.get());
                    }
                }
            }
        }
        if (mode.get().equalsIgnoreCase("Matrix Jump")) {
                if (mc.player.onGround) {
                    if (MovementUtil.isMoving()) {
                        mc.player.jump();
                    }
                } else {
                    if (MovementUtil.isMoving()) {
                        mc.player.motionY = height2.get();
                        MovementUtil.setSpeed(speed2.get());
                    }
                }
        }
        if (mode.is("Big Grief")) {
            mc.player.motionY = -0.003;
        }
    }

    @EventTarget
    public void onMotion(EventMotion e) {
        if (mode.get().equalsIgnoreCase("Creative")) {
            mc.player.capabilities.isFlying = true;
        }
        if (mode.is("Sunrise XD")) {
            if (mc.player.collidedVertically) mc.player.motionY = 0.1f;
        }

        if (mode.get().equalsIgnoreCase("Motion")) {
            MovementUtil.setSpeed(MotionSpeed.get());
            mc.player.motionY = 0;

            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += MotionSpeed.get();
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= MotionSpeed.get();
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.capabilities.isFlying = false;
        }
    }
}
