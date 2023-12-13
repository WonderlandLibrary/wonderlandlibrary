package best.azura.client.impl.module.impl.other;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventClickMouse;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

@ModuleInfo(name = "MCF", category = Category.OTHER, description = "Add or remove friends by mid-clicking players")
public class MidClickFriends extends Module {

    @EventHandler
    public final Listener<EventClickMouse> eventClickMouseListener = e -> {
        if (e.getButton() == 2 && e.getObjectMouseOver().typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && e.getObjectMouseOver().entityHit instanceof EntityPlayer) {
            final String name = e.getObjectMouseOver().entityHit.getName();
            if (Client.INSTANCE.getFriendManager().isFriend(name)) {
                Client.INSTANCE.getFriendManager().removeFriend(name);
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Friends", "Removed " + name + " as a friend!", 3000, Type.SUCCESS));
            } else {
                Client.INSTANCE.getFriendManager().addFriend(name);
                Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Friends", "Added " + name + " as a friend", 3000, Type.SUCCESS));
            }
        }
    };

}