package fun.expensive.client.feature.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.Rich;
import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.event.events.impl.player.EventUpdate;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.notification.NotificationMode;
import fun.rich.client.ui.notification.NotificationRenderer;
import fun.rich.client.utils.other.ChatUtils;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;

public class StaffAlert extends Feature {
    private boolean isJoined;

    public StaffAlert() {
        super("StaffAlert", FeatureCategory.Player);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem) event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            this.isJoined = true;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer staffPlayer : GuiPlayerTabOverlay.getPlayers()) {
            if (staffPlayer == null || staffPlayer == mc.player || !staffPlayer.getDisplayName().getUnformattedText().contains("HELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ST.HELPER") && !staffPlayer.getDisplayName().getUnformattedText().contains("MODER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ST.MODER") && !staffPlayer.getDisplayName().getUnformattedText().contains("ADMIN") && !staffPlayer.getDisplayName().getUnformattedText().contains("Админ") && !staffPlayer.getDisplayName().getUnformattedText().contains("Хелпер") && !staffPlayer.getDisplayName().getUnformattedText().contains("Модер") || staffPlayer.ticksExisted >= 10 || !this.isJoined)
                continue;
            ChatUtils.addChatMessage(ChatFormatting.WHITE + "Администратор " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " зашел на сервер / вышел из ваниша");
            NotificationRenderer.queue("§6Staff Alert", ChatFormatting.WHITE + "Администратор " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " зашел на сервер / вышел из ваниша", 5, NotificationMode.WARNING);
            this.isJoined = false;
        }
    }
}
