package org.dreamcore.client.feature.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.EntityLivingBase;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.input.EventMouse;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.friend.Friend;
import org.dreamcore.client.helpers.misc.ChatHelper;
import org.dreamcore.client.ui.notification.NotificationManager;
import org.dreamcore.client.ui.notification.NotificationType;

public class MiddleClickFriend extends Feature {

    public MiddleClickFriend() {
        super("MiddleClickFriend", "Добавляет игрока в френд лист при нажатии на кнопку мыши", Type.Misc);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && mc.pointedEntity instanceof EntityLivingBase) {
            if (dreamcore.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(mc.pointedEntity.getName()))) {
                dreamcore.instance.friendManager.getFriends().remove(dreamcore.instance.friendManager.getFriend(mc.pointedEntity.getName()));
                ChatHelper.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + mc.pointedEntity.getName() + "'" + " as Friend!");
                NotificationManager.publicity("MCF", "Removed " + "'" + mc.pointedEntity.getName() + "'" + " as Friend!", 4, NotificationType.INFO);
            } else {
                dreamcore.instance.friendManager.addFriend(new Friend(mc.pointedEntity.getName()));
                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + mc.pointedEntity.getName() + " as Friend!");
                NotificationManager.publicity("MCF", "Added " + mc.pointedEntity.getName() + " as Friend!", 4, NotificationType.SUCCESS);
            }
        }
    }
}