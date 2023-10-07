package koks.module.misc;

import de.liquiddev.ircclient.client.IrcPlayer;
import koks.api.event.Event;
import koks.api.manager.friend.FriendManager;
import koks.api.registry.module.Module;
import koks.api.utils.TimeHelper;
import koks.event.TickEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

@Module.Info(name = "MidClick", category = Module.Category.MISC, description = "")
public class MidClick extends Module implements Module.NotBypass {

    TimeHelper timer = new TimeHelper();

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof TickEvent) {
            final FriendManager friendManager = FriendManager.getInstance();
            if (getGameSettings().keyBindPickBlock.pressed && mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null) {
                getGameSettings().keyBindPickBlock.pressed = false;
                final Entity entity = mc.objectMouseOver.entityHit;
                if(entity instanceof EntityPlayer && entity.getName() != null) {
                    final IrcPlayer ircPlayer = IrcPlayer.getByIngameName(entity.getName());
                    if (!friendManager.isFriend(entity.getName())) {
                        friendManager.addFriend(entity.getName(), ircPlayer != null ? ircPlayer.getClientName().equalsIgnoreCase("Koks") ? ircPlayer.getExtra() : ircPlayer.getIrcNick() : entity.getName());
                        sendMessage("Added friend §e" + entity.getName());
                    } else {
                        friendManager.removeFriend(entity.getName());
                        sendMessage("Removed friend §e" + entity.getName());
                    }
                }
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
