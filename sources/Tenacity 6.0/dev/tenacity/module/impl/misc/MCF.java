// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.misc;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.commands.impl.FriendCommand;
import net.minecraft.util.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.module.Category;
import dev.tenacity.module.Module;

public class MCF extends Module
{
    private boolean wasDown;
    
    public MCF() {
        super("MCF", "MCF", Category.MISC, "middle click friends");
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        if (MCF.mc.inGameHasFocus) {
            final boolean down = MCF.mc.gameSettings.keyBindPickBlock.isKeyDown();
            if (down && !this.wasDown) {
                if (MCF.mc.objectMouseOver != null && MCF.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
                    final EntityPlayer player = (EntityPlayer)MCF.mc.objectMouseOver.entityHit;
                    final String name = StringUtils.stripControlCodes(player.getName());
                    if (FriendCommand.isFriend(name)) {
                        FriendCommand.friends.removeIf(f -> f.equalsIgnoreCase(name));
                        NotificationManager.post(NotificationType.SUCCESS, "Friend Manager", "You are no longer friends with " + name + "!", 2.0f);
                    }
                    else {
                        FriendCommand.friends.add(name);
                        NotificationManager.post(NotificationType.SUCCESS, "Friend Manager", "You are now friends with " + name + "!", 2.0f);
                    }
                    FriendCommand.save();
                    this.wasDown = true;
                }
            }
            else if (!down) {
                this.wasDown = false;
            }
        }
    }
}
