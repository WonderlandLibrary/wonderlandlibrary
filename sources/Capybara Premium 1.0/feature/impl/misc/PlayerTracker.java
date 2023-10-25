package fun.expensive.client.feature.impl.misc;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.event.events.impl.player.EventPreMotion;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.ui.settings.impl.NumberSetting;
import fun.rich.client.utils.math.MathematicHelper;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class PlayerTracker extends Feature {
    public NumberSetting radius = new NumberSetting("Radius", 1000, 100, 5000, 10, () -> true);

    public PlayerTracker() {
        super("PlayerTracker", "Поиск игроков.", FeatureCategory.Misc);
        addSettings(radius);
    }

    @EventTarget
    public void onFind(EventPreMotion eventPreMotionUpdate) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                new BlockPos(MathematicHelper.getRandomInRange(-radius.getNumberValue(), radius.getNumberValue()), 0,
                        MathematicHelper.getRandomInRange(-radius.getNumberValue(), radius.getNumberValue())), EnumFacing.DOWN));
    }

    @EventTarget
    public void onFindReceive(EventReceivePacket eventReceivePacket) {
        SPacketBlockChange packetBlockChange = (SPacketBlockChange) eventReceivePacket.getPacket();
        if (eventReceivePacket.getPacket() instanceof SPacketBlockChange) {
            ChatUtils.addChatMessage(TextFormatting.WHITE + "Игрок замечен на кординатах > " + TextFormatting.RED +
                    packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ());
            NotificationRenderer.queue("Player Tracker", "Игрок замечен на кординатах > " + TextFormatting.RED +
                    packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ(), 2, NotificationMode.INFO);

        }
    }
}
