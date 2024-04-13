package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.KeyPressEvent;
import net.caffeinemc.phosphor.api.event.events.PacketEvent;
import net.caffeinemc.phosphor.api.event.events.TickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.util.FakePlayerEntity;
import net.caffeinemc.phosphor.module.Module;
import net.caffeinemc.phosphor.module.setting.settings.BooleanSetting;
import net.caffeinemc.phosphor.module.setting.settings.KeybindSetting;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class BlinkModule extends Module {
    private BooleanSetting renderOriginal = new BooleanSetting("Render original", this, true);
    private KeybindSetting cancelBlink = new KeybindSetting("Cancel bind", -1, this);

    public BlinkModule() {
        super("Blink", "Does blink", Category.COMBAT);
    }

    private final List<PlayerMoveC2SPacket> packets = new ArrayList<>();
    private FakePlayerEntity model;
    private final Vector3d start = new Vector3d(0, 0, 0);

    private boolean cancelled = false;
    private int timer = 0;

    @Override
    public void enable() {
        if (mc.player == null)
            return;
        super.enable();
    }

    @Override
    public void onEnable() {
        if (renderOriginal.isEnabled()) {
            model = new FakePlayerEntity(mc.player, mc.player.getGameProfile().getName(), 20, true);
            model.doNotPush = true;
            model.hideWhenInsideCamera = true;
            model.spawn();
        }

        Vec3d playerPos = mc.player.getPos();
        start.x = playerPos.x;
        start.y = playerPos.y;
        start.z = playerPos.z;
    }

    @Override
    public void onDisable() {
        dumpPackets(!cancelled);
        if (cancelled) mc.player.setPos(start.x, start.y, start.z);
        cancelled = false;
    }

    private void dumpPackets(boolean send) {
        new Thread(() -> {
            synchronized (packets) {
                if (send) packets.forEach(mc.getNetworkHandler()::sendPacket);
                packets.clear();
            }

            if (model != null) {
                model.despawn();
                model = null;
            }
        }).start();

        timer = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        timer++;
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof PlayerMoveC2SPacket p)) return;
        event.cancel();

        PlayerMoveC2SPacket prev = packets.size() == 0 ? null : packets.get(packets.size() - 1);

        if (prev != null &&
                p.isOnGround() == prev.isOnGround() &&
                p.getYaw(-1) == prev.getYaw(-1) &&
                p.getPitch(-1) == prev.getPitch(-1) &&
                p.getX(-1) == prev.getX(-1) &&
                p.getY(-1) == prev.getY(-1) &&
                p.getZ(-1) == prev.getZ(-1)
        ) return;

        synchronized (packets) {
            packets.add(p);
        }
    }

    @EventHandler
    private void onKeyPress(KeyPressEvent event) {
        if (event.action != GLFW.GLFW_RELEASE)
            return;

        if (event.key == cancelBlink.getKeyCode()) {
            cancelled = true;
            toggle();
        }
    }
}
