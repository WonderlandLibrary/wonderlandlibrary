package me.crimp.claudius.mod.modules.movement;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import static me.crimp.claudius.utils.PlayerUtil.getBlockHeight;

public class Step extends Module {
    public Step() {
        super("Step", "Allows you to automatically step up blocks Without Jumping", Category.Movement,false,false);
    }

    public final Setting<Stepmode> mode = register(new Setting<>("Mode", Stepmode.Normal));
    public final Setting<Float> height = register(new Setting<>("Height", 1f, 1f, 6f));
    public final Setting<Boolean> Rev = register(new Setting<>("Reverse", false));
    public final Setting<Double> speed = new Setting<>("RevSpeed", 1.0, 0.1, 5.0, v -> this.Rev.getValue());

    @Override
    public String getDisplayInfo() {
        return mode.getValue().name();
    }

    @Override
    public void onUpdate() {
        if (mode.getValue().equals(Stepmode.Vanilla)) {
            mc.player.stepHeight = height.getValue();
        } else if (mode.getValue().equals(Stepmode.Normal)) {
            mc.player.stepHeight = 0.6f;
            if (height.getValue() > 2) height.setValue(2f);
            double n = getBlockHeight();

            if (n < 0 || n > 2) return;

            if (n == 2.0 && height.getValue() == 2) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.78, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.63, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.51, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.9, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.21, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.45, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.43, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0, mc.player.posZ);
            }
            if (n == 1.5 && height.getValue() >= 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.24918707874468, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1707870772188, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
            if (n == 1.0 && height.getValue() >= 1) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698, mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805212, mc.player.posZ, mc.player.onGround));
                mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0, mc.player.posZ);
            }
        }
        if (mc.player.onGround && this.Rev.getValue() && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isInLava()) {
            for (double y = 0.0; y <= height.getValue() + 0.5; y += 0.1) {
                if (!mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0, -y, 0.0)).isEmpty()) {
                    mc.player.motionY -= speed.getValue();
                }
            }
        }
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.6f;
    }

    public enum Stepmode {
        Vanilla, Normal
    }
}
