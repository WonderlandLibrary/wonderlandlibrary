package info.sigmaclient.sigma.modules.world;

import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.MouseClickEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import top.fl0wowp4rty.phantomshield.annotations.Native;

public class NewChunks extends Module {
    int ticks = 0;
    public NewChunks() {
        super("NewChunks", Category.World, "Detect new chunks on not vanilla servers.");
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        super.onUpdateEvent(event);
    }
    @Override
    public void onPacketEvent(PacketEvent event) {
        super.onPacketEvent(event);
    }

    @Override
    public void onMouseClickEvent(MouseClickEvent event) {
        super.onMouseClickEvent(event);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
