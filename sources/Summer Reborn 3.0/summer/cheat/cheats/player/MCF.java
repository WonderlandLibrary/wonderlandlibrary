package summer.cheat.cheats.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import summer.Summer;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.ChatUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.client.EventMiddleClick;

public class MCF extends Cheats {
    public MCF() {
        super("MCF", "", Selection.PLAYER);
    }

    @EventTarget
    public void onMiddle(EventMiddleClick e) {
        if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            String s = mc.objectMouseOver.entityHit.getName();
            if (!Summer.INSTANCE.friendManager.isFriend(s)) {
                Summer.INSTANCE.friendManager.addFriend(s, s);
                ChatUtils.sendMessage("Added \"" + s + "\".");
            } else {
                Summer.INSTANCE.friendManager.deleteFriend(s);
                ChatUtils.sendMessage("Deleted \"" + s + "\".");
            }
        }
    }
}
