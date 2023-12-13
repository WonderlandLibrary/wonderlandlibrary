package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterPortals
        extends Module {
    private static BetterPortals INSTANCE = new BetterPortals();
    public Setting<Boolean> portalChat = this.register(new Setting<Boolean>("Chat", Boolean.valueOf(true), "Allows you to chat in portals."));
    public Setting<Boolean> godmode = this.register(new Setting<Boolean>("Godmode", Boolean.valueOf(false), "Portal Godmode."));
    public Setting<Boolean> fastPortal = this.register(new Setting<Boolean>("FastPortal", false));
    public Setting<Integer> cooldown = this.register(new Setting<Object>("Cooldown", 5, 1, 10, v -> this.fastPortal.getValue(), "Portal cooldown."));
    public Setting<Integer> time = this.register(new Setting<Object>("Time", 5, 0, 80, v -> this.fastPortal.getValue(), "Time in Portal"));

    public BetterPortals() {
        super("BetterPortals", "Tweaks for Portals", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static BetterPortals getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BetterPortals();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public String getDisplayInfo() {
        if (this.godmode.getValue().booleanValue()) {
            return "Godmode";
        }
        return null;
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && this.godmode.getValue().booleanValue() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCanceled(true);
        }
    }
}

