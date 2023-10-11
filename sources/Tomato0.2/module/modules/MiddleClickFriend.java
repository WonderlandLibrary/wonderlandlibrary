/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.MouseEvent;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.people.FriendManager;
import cow.milkgod.cheese.utils.Logger;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

public class MiddleClickFriend
extends Module {
    public MiddleClickFriend() {
        super("MiddleClickFriend", 0, Category.PLAYER, 0, true, "MiddleClickFriend", new String[]{"mcf", "midclickfriend"});
    }

    @EventTarget
    private void onMouseClick(MouseEvent event) {
        if (event.key == 2 && Wrapper.mc.objectMouseOver != null && Wrapper.mc.objectMouseOver.entityHit != null && Wrapper.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().objectMouseOver.entityHit;
            String name = player.getName();
            if (FriendManager.isFriend(name)) {
                FriendManager.deleteFriend(name);
                Logger.logChat("Removed: " + name);
            } else {
                FriendManager.addFriend(name, name);
                Logger.logChat("Added: " + name);
            }
        }
    }
}

