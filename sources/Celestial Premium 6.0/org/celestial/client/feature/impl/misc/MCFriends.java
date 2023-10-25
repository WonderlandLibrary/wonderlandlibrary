/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import baritone.events.events.player.EventUpdate;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.input.EventMouse;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.friend.Friend;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class MCFriends
extends Feature {
    private final NumberSetting maxDistance = new NumberSetting("Max Distance", 3.0f, 3.0f, 150.0f, 1.0f, () -> true);
    private final BooleanSetting safe = new BooleanSetting("Safe", false, () -> true);
    private boolean canAdd;
    private int clickCounter = 0;

    public MCFriends() {
        super("MCFriends", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 \u0432 \u0444\u0440\u0435\u043d\u0434 \u043b\u0438\u0441\u0442 \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u043a\u043d\u043e\u043f\u043a\u0443 \u043c\u044b\u0448\u0438", Type.Misc);
        this.addSettings(this.maxDistance, this.safe);
    }

    @Override
    public void onDisable() {
        this.clickCounter = 0;
        super.onDisable();
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.key == 2) {
            for (EntityPlayer player : MCFriends.mc.world.playerEntities) {
                if (player == null || player == MCFriends.mc.player || !RotationHelper.isLookingAtEntity(false, MCFriends.mc.player.rotationYaw, MCFriends.mc.player.rotationPitch, 0.06f, 0.06f, 0.06f, player, this.maxDistance.getCurrentValue())) continue;
                ++this.clickCounter;
            }
            if (this.clickCounter < 2 && this.safe.getCurrentValue()) {
                return;
            }
            this.canAdd = true;
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (EntityPlayer player : MCFriends.mc.world.playerEntities) {
            if (player == null || player == MCFriends.mc.player || !RotationHelper.isLookingAtEntity(false, MCFriends.mc.player.rotationYaw, MCFriends.mc.player.rotationPitch, 0.06f, 0.06f, 0.06f, player, this.maxDistance.getCurrentValue()) || !this.canAdd) continue;
            if (Celestial.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(player.getName()))) {
                Celestial.instance.friendManager.getFriends().remove(Celestial.instance.friendManager.getFriend(player.getName()));
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.RED) + "Removed " + (Object)((Object)ChatFormatting.RESET) + "'" + player.getName() + "' as Friend!");
                NotificationManager.publicity("MCF", "Removed '" + player.getName() + "' as Friend!", 4, NotificationType.INFO);
            } else {
                Celestial.instance.friendManager.addFriend(new Friend(player.getName()));
                ChatHelper.addChatMessage((Object)((Object)ChatFormatting.GREEN) + "Added " + (Object)((Object)ChatFormatting.RESET) + player.getName() + " as Friend!");
                NotificationManager.publicity("MCF", "Added " + player.getName() + " as Friend!", 4, NotificationType.SUCCESS);
            }
            this.canAdd = false;
            this.clickCounter = 0;
        }
    }
}

