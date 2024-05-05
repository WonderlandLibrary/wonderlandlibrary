package ru.smertnix.celestial.feature.impl.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.event.events.impl.player.EventPlayerState;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.notification.NotificationMode;
import ru.smertnix.celestial.ui.notification.NotificationRenderer;
import ru.smertnix.celestial.utils.other.ChatUtils;

public class StaffAlert extends Feature {
    private boolean isJoined;
    ArrayList<NetworkPlayerInfo> data2 = new ArrayList<NetworkPlayerInfo>();
    public StaffAlert() {
        super("Staff Alert", FeatureCategory.Util);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem) event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            this.isJoined = true;
        }
    }

    private boolean havePermissionFixed(String displayName) {
        return displayName.toLowerCase().contains("helper") || displayName.toLowerCase().contains("хелпер") || displayName.toLowerCase().contains("модер")
                || displayName.toLowerCase().contains("moder") || displayName.toLowerCase().contains("админ") || displayName.toLowerCase().contains("ст хелпер")
                || displayName.toLowerCase().contains("admin");
    }

    @EventTarget
    public void onEvent(EventUpdate event) {
    	for (NetworkPlayerInfo data : mc.player.connection.getPlayerInfoMap()) {
    		if (!data2.contains(data)) {
    			data2.add(data);
    		}
    	}
    	for (NetworkPlayerInfo data : data2) {
    		if (data == null || data.getDisplayName() == null) 
    			data.a = false;
    		if (data != null && data.getDisplayName() != null) {
                data.getDisplayName().getFormattedText();
                if (data.getGameProfile().getName() != null) {
                	String displayName = data.getDisplayName().getFormattedText();
                    boolean havePerm = havePermissionFixed(displayName);
                    if (havePerm && !data.a) {
                    	data.a = true;
                    	System.out.println("test");
                    	ChatUtils.addChatMessage(ChatFormatting.WHITE + "������������� " + ChatFormatting.RESET + data.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " ����� �� ������ / ����� �� ������");
                        NotificationRenderer.queue("�6Staff Alert", ChatFormatting.WHITE + "������������� " + ChatFormatting.RESET + data.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " ����� �� ������ / ����� �� ������", 5, NotificationMode.WARNING);
                        isJoined = false;
                    }
                }
            }
    	}
    }
}
