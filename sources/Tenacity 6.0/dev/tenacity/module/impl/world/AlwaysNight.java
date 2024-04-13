package dev.tenacity.module.impl.world;

import com.viaversion.viaversion.libs.kyori.adventure.util.Ticks;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.packet.PacketReceiveEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.NumberSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class AlwaysNight extends Module {

    private final NumberSetting time = new NumberSetting("Time", 12000, 0, 24000, 1000);

    public AlwaysNight() {
        super("Ambience", "Set world time", ModuleCategory.WORLD);
        initializeSettings(time);
    }

    public void update() {
        mc.theWorld.setWorldTime((long) time.getCurrentValue());
    }
    private final IEventListener<Ticks> onRender2d = e -> {
        if (mc.theWorld != null) {
            mc.theWorld.setWorldTime((long) time.getCurrentValue());
            update();
        }
    };

    @Override
    public void onEnable() {
        if (mc.thePlayer != null) {
            mc.theWorld.setWorldTime((long) time.getCurrentValue());
            update();
        }
        super.onEnable();
    }
    @Override
    public void onDisable() {
        if (mc.thePlayer == null) {
            return;
        }
        mc.theWorld.setWorldTime((0));
        super.onDisable();
    }

    private final IEventListener<PacketReceiveEvent> onPacketReceive = event -> {
        if (event.getPacket() instanceof S03PacketTimeUpdate) {
            event.cancel();
        }
    };
}
