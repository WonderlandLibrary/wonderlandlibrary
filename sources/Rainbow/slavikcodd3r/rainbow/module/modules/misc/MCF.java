package slavikcodd3r.rainbow.module.modules.misc;

import net.minecraft.entity.player.EntityPlayer;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.MouseEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.friend.FriendManager;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.utils.ClientUtils;
@Mod(displayName = "MCF")
public class MCF extends Module
{
	
    @EventTarget
    private void onMouseClick(final MouseEvent event) {
        if (event.getKey() == 2 && ClientUtils.mc().objectMouseOver != null && ClientUtils.mc().objectMouseOver.entityHit != null && ClientUtils.mc().objectMouseOver.entityHit instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)ClientUtils.mc().objectMouseOver.entityHit;
            final String name = player.getName();
            if (FriendManager.isFriend(name)) {
                FriendManager.removeFriend(name);
                ClientUtils.sendMessage("Removed " + name);
            }
            else {
                FriendManager.addFriend(name, name);
                ClientUtils.sendMessage("Added " + name);
            }
        }
    }
}
