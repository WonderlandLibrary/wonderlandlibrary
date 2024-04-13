package dev.tenacity.module.impl.misc;

import dev.tenacity.command.impl.FriendCommand;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.util.misc.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

public class MCF extends Module {

    private boolean wasDown;

    public MCF() {
        super("MCF", "Middle click friends", ModuleCategory.MISC);
    }

    private final IEventListener<MotionEvent> onMotionEvent = event -> {
        if(!mc.inGameHasFocus) return;

        boolean down = mc.gameSettings.keyBindPickBlock.isKeyDown();

        if(down && !wasDown) {
            if(mc.objectMouseOver == null || !(mc.objectMouseOver.entityHit instanceof EntityPlayer)) return;

            EntityPlayer player = (EntityPlayer) mc.objectMouseOver.entityHit;
            String name = StringUtils.stripControlCodes(player.getName());

            if(FriendCommand.isFriend(name)) {
                FriendCommand.friends.removeIf(friend -> friend.equalsIgnoreCase(name));
                ChatUtil.print("§cYou are no longer friends with " + name + "!");
                return;
            }

            FriendCommand.friends.add(name);
            ChatUtil.print("§aYou are now friends with " + name + "!");
            wasDown = true;
        } else if(!down) {
            wasDown = false;
        }
    };

}
