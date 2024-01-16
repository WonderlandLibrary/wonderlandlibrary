// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.misc;

import ru.fluger.client.event.EventTarget;
import ru.fluger.client.files.impl.FriendConfig;
import ru.fluger.client.friend.Friend;
import ru.fluger.client.ui.notification.NotificationManager;
import ru.fluger.client.ui.notification.NotificationType;
import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.events.impl.input.EventMouse;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.Feature;

public class MiddleClickFriend extends Feature
{
    public MiddleClickFriend() {
        super("MiddleClickFriend", "\u0414\u043e\u0431\u0430\u0432\u043b\u044f\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u0430 \u0432 \u0444\u0440\u0435\u043d\u0434 \u043b\u0438\u0441\u0442 \u043f\u0440\u0438 \u043d\u0430\u0436\u0430\u0442\u0438\u0438 \u043d\u0430 \u043a\u043d\u043e\u043f\u043a\u0443 \u043c\u044b\u0448\u0438", Type.Misc);
    }
    
    @EventTarget
    public void onMouseEvent(final EventMouse event) {
        if (event.getKey() == 2 && MiddleClickFriend.mc.i instanceof vp) {
            if (Fluger.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(MiddleClickFriend.mc.i.h_()))) {
                Fluger.instance.friendManager.getFriends().remove(Fluger.instance.friendManager.getFriend(MiddleClickFriend.mc.i.h_()));
                ChatHelper.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.i.h_() + "' as Friend!");
                NotificationManager.publicity("MiddleClickFriend", ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.i.h_() + "' as Friend!", 2, NotificationType.ERROR);
            }
            else {
                Fluger.instance.friendManager.addFriend(new Friend(MiddleClickFriend.mc.i.h_()));
                try {
                    Fluger.instance.fileManager.getFile(FriendConfig.class).saveFile();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                NotificationManager.publicity("MiddleClickFriend", ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.i.h_() + "' as Friend!", 2, NotificationType.SUCCESS);
                ChatHelper.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.i.h_() + "' as Friend!");
            }
        }
    }
}
